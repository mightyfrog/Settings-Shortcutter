package org.mightyfrog.android.settingsshortcutter

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author Shigehiro Soejima
 */
class PackageChooserDialog : BottomSheetDialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance(b: Bundle): PackageChooserDialog = PackageChooserDialog().apply {
            arguments = b
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rv = inflater.inflate(R.layout.dialog_package_chooser, container, false) as RecyclerView
        rv.adapter = PackageAdapter(arguments!!.getString("action", ""), rv.context)
        rv.layoutManager = LinearLayoutManager(rv.context)
        container?.addView(rv)
        return rv
    }
}