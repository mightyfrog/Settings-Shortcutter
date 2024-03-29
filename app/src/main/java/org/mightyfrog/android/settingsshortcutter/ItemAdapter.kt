package org.mightyfrog.android.settingsshortcutter

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.mightyfrog.android.settingsshortcutter.databinding.VhItemBinding
import java.io.InputStreamReader

/**
 * @author Shigehiro Soejima
 */
class ItemAdapter(
    private val fragmentManager: FragmentManager,
    private val context: Context,
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val list: List<Item>

    init {
        val tt = object : TypeToken<List<Item>>() {}.type
        list = Gson().fromJson(InputStreamReader(context.resources.openRawResource(R.raw.data)), tt)
        list.sortedBy {
            it.name
        }
    }

    override fun getItemCount() = list.size

    @TargetApi(26)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = VhItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    @RequiresApi(33)
    override fun onBindViewHolder(vh: ItemViewHolder, position: Int) {
        val item = list[position]
        vh.bind(item)
        vh.itemView.setOnClickListener {
            item.api?.let { api ->
                if (api <= Build.VERSION.SDK_INT) {
                    sendIntent(item.constant)
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.unsupported, api),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    @RequiresApi(33)
    @SuppressLint("BatteryLife")
    @TargetApi(26)
    private fun sendIntent(action: String?) {
        action ?: return

        val intent = Intent()
        intent.action = action
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        when (action) {
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Settings.ACTION_IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS,
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
            Settings.ACTION_MANAGE_WRITE_SETTINGS,
            Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE,
            Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
            Settings.ACTION_APP_LOCALE_SETTINGS,
            Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS,
            Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
            -> {
                val bundle = Bundle().apply {
                    putString("action", action)
                }
                PackageChooserDialog.newInstance(bundle)
                    .show(fragmentManager, PackageChooserDialog::class.java.simpleName)
                return
            }
            Settings.ACTION_APP_USAGE_SETTINGS -> {
                val bundle = Bundle().apply {
                    putString("action", action)
                    putBoolean("extraPackageName", true)
                }
                PackageChooserDialog.newInstance(bundle)
                    .show(fragmentManager, PackageChooserDialog::class.java.simpleName)
                return
            }
            Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS -> {
                val adb = Settings.Secure.getInt(
                    context.contentResolver,
                    Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
                    0
                )
                if (adb != 1) {
                    Toast.makeText(
                        context,
                        "Developer options are not enabled",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
            }
            Settings.ACTION_APP_NOTIFICATION_SETTINGS -> {
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                intent.putExtra(
                    Settings.EXTRA_CHANNEL_ID,
                    "test_id_1"
                ) // optional, not working as of Android O DP3
            }
            Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS -> {
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, "test_id_1")
            }
            Settings.ACTION_ADD_ACCOUNT -> {
                // https://developers.google.com/android/reference/com/google/android/gms/auth/GoogleAuthUtil.html#GOOGLE_ACCOUNT_TYPE
//                intent.putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
            }
            Settings.ACTION_NOTIFICATION_LISTENER_DETAIL_SETTINGS -> {
                intent.putExtra(
                    Settings.EXTRA_NOTIFICATION_LISTENER_COMPONENT_NAME,
                    ComponentName(
                        context.packageName,
                        DummyNotificationListener::class.java.name
                    ).flattenToString()
                )
            }
            Settings.ACTION_PROCESS_WIFI_EASY_CONNECT_URI -> {
                intent.data = Uri.parse(VALID_WIFI_DPP_QR_CODE)
            }
            Settings.ACTION_WIFI_ADD_NETWORKS -> { // TODO: fix me
                val list = arrayListOf(
                    WifiNetworkSuggestion.Builder()
                        .setSsid("test111111")
                        .build(),
                    WifiNetworkSuggestion.Builder()
                        .setSsid("test222222")
                        .setWpa2Passphrase("test123456")
                        .build()
                )
                intent.putParcelableArrayListExtra(Settings.EXTRA_WIFI_NETWORK_LIST, list)
                try {
                    ActivityCompat.startActivityForResult(
                        context as Activity,
                        intent,
                        1,
                        null
                    )
                } catch (t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
                return
            }
            Settings.ACTION_VOICE_CONTROL_AIRPLANE_MODE,
            Settings.ACTION_SETTINGS_EMBED_DEEP_LINK_ACTIVITY,
            Settings.ACTION_VOICE_CONTROL_BATTERY_SAVER_MODE,
            Settings.ACTION_VOICE_CONTROL_DO_NOT_DISTURB_MODE,
            Settings.ACTION_MANAGE_SUPERVISOR_RESTRICTED_SETTING,
            -> {
                Toast.makeText(context, "not implemented", Toast.LENGTH_SHORT).show()
                return
            }
        }
        try {
            ContextCompat.startActivity(context, intent, null)
        } catch (t: Throwable) {
            Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
        }
    }

    class ItemViewHolder(private val binding: VhItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.apply {
                name.text = name.context.getString(R.string.name_and_api, item.name, item.api)
                desc.text = item.action
            }
        }
    }

    companion object {
        // https://github.com/AOSP-10/packages_apps_Settings/blob/7e28298e599841790c13bb049ac9aa38291a3b3a/tests/unit/src/com/android/settings/wifi/dpp/WifiDppConfiguratorActivityTest.java
        private const val VALID_WIFI_DPP_QR_CODE = ("DPP:I:SN=4774LH2b4044;M:010203040506;K:"
                + "MDkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDIgADURzxmttZoIRIPWGoQMV00XHWCAQIhXruVWOz0NjlkIA=;;")
    }
}
