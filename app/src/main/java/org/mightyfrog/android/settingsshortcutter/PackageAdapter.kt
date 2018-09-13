package org.mightyfrog.android.settingsshortcutter

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

/**
 * @author Shigehiro Soejima
 */
class PackageAdapter(private val action: String, private val context: Context) : RecyclerView.Adapter<PackageAdapter.PackageViewHolder>() {
    private val packageList: List<ApplicationInfo> = context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

    override fun getItemCount() = packageList.size

    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) {
        holder.apply {
            val pkg = packageList[position].packageName
            val appInfo = context.packageManager.getApplicationInfo(pkg, PackageManager.GET_META_DATA)
            icon.setImageDrawable(context.packageManager.getApplicationIcon(pkg))
            appName.text = context.packageManager.getApplicationLabel(appInfo)
            pkgName.text = pkg
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        return PackageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.vh_pkg_item, parent, false)).apply {
            itemView.setOnClickListener {
                Intent(action).apply {
                    data = Uri.parse("package:" + packageList[adapterPosition].packageName)
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

    class PackageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon = view.findViewById<ImageView>(R.id.icon)!!
        val appName = view.findViewById<TextView>(R.id.appName)!!
        val pkgName = view.findViewById<TextView>(R.id.pkgName)!!
    }
}