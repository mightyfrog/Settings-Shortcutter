package org.mightyfrog.android.settingsshortcutter

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

/**
 * @author Shigehiro Soejima
 */
class ItemAdapter(val context: Context) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    private val mList: List<Item>

    init {
        val tt = object : TypeToken<List<Item>>() {}.type
        mList = Gson().fromJson(InputStreamReader(context.resources.openRawResource(R.raw.data)), tt)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemId(position: Int): Long {
        return mList[position].name!!.toLong()
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.vh_item, parent, false)
        val vh = ItemViewHolder(view)
        vh.itemView.setOnClickListener({
            val item = mList[vh.adapterPosition]
            sendIntent(item.constant)
//            val apiLevel = item.api!!.toInt()
//            if (apiLevel <= Build.VERSION.SDK_INT) {
//                sendIntent(item.constant)
//            } else {
//                Toast.makeText(context, context.getString(R.string.unsupported, apiLevel), Toast.LENGTH_SHORT).show()
//            }
        })

        return vh
    }

    override fun onBindViewHolder(vh: ItemViewHolder?, position: Int) {
        val item = mList[position]
        vh?.name?.text = item.name
        vh?.desc?.text = item.action
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun sendIntent(action: String?) {
        action ?: return

        val intent = Intent(action)
        when (action) {
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Settings.ACTION_IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS,
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
            Settings.ACTION_MANAGE_WRITE_SETTINGS,
            Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS -> {
                intent.data = Uri.parse("package:" + context.packageName)
            }
            Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS -> {
                val adb = Settings.Secure.getInt(context.contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0)
                if (adb != 1) {
                    Toast.makeText(context, "Developer options are not enabled", Toast.LENGTH_SHORT).show()
                    return
                }
            }
            Settings.ACTION_APP_NOTIFICATION_SETTINGS -> {
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, "test_id_1") // optional, not working as of Android O DP2
            }
            Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS -> {
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, "test_id_1")
            }
            Settings.ACTION_ADD_ACCOUNT -> {
                // https://developers.google.com/android/reference/com/google/android/gms/auth/GoogleAuthUtil.html#GOOGLE_ACCOUNT_TYPE
//                intent.putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
            }
        }
        try {
            ContextCompat.startActivity(context, intent, null)
        } catch (t: Throwable) {
            Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val desc: TextView = itemView.findViewById(R.id.desc)
    }
}
