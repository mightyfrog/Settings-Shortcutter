package org.mightyfrog.android.settingsshortcutter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.mightyfrog.android.settingsshortcutter.databinding.DialogPackageChooserBinding

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
        savedInstanceState: Bundle?,
    ): View {
        val binding = DialogPackageChooserBinding.inflate(LayoutInflater.from(requireContext()))
        binding.rv.adapter = PackageAdapter(
            requireArguments().getString("action", ""),
            binding.root.context,
            requireArguments().getBoolean("extraPackageName", false)
        )
        container?.addView(binding.root)

        return binding.root
    }
}