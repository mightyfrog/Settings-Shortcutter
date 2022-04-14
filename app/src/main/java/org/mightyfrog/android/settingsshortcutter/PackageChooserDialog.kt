package org.mightyfrog.android.settingsshortcutter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rv = inflater.inflate(
            R.layout.dialog_package_chooser,
            container,
            false
        ) as RecyclerView
        rv.adapter =
            PackageAdapter(requireArguments().getString("action", ""), rv.context)
        rv.layoutManager =
            LinearLayoutManager(rv.context)
        container?.addView(rv)
        return rv
    }
}