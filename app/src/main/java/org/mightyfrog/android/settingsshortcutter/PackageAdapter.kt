package org.mightyfrog.android.settingsshortcutter

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.mightyfrog.android.settingsshortcutter.databinding.VhPkgItemBinding

/**
 * @author Shigehiro Soejima
 */
class PackageAdapter(
    private val action: String,
    private val context: Context,
    private val extraPackageName: Boolean = false,
) : RecyclerView.Adapter<PackageAdapter.PackageViewHolder>() {

    private val packageList: List<ApplicationInfo> =
        context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

    override fun getItemCount() = packageList.size

    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) =
        holder.bind(packageList[position].packageName)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        val binding = VhPkgItemBinding.inflate(LayoutInflater.from(parent.context))
        return PackageViewHolder(binding).apply {
            itemView.setOnClickListener {
                Intent(action).apply {
                    val packageName = packageList[adapterPosition].packageName
                    if (extraPackageName) {
                        putExtra(Intent.EXTRA_PACKAGE_NAME, packageName)
                    } else {
                        data = Uri.parse("package:$packageName")
                    }
                    addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
                    try {
                        ContextCompat.startActivity(context, this, null)
                    } catch (t: Throwable) {
                        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    class PackageViewHolder(private val binding: VhPkgItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pkg: String) {
            binding.apply {
                val appInfo =
                    root.context.packageManager.getApplicationInfo(
                        pkg,
                        PackageManager.GET_META_DATA
                    )
                icon.setImageDrawable(root.context.packageManager.getApplicationIcon(pkg))
                appName.text = root.context.packageManager.getApplicationLabel(appInfo)
                pkgName.text = pkg
            }
        }
    }
}