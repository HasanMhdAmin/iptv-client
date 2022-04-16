package de.itshasan.iptv_core

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding

abstract class CoreDialog<B : ViewBinding>(private val layoutRes: Int) : DialogFragment() {

    private var _binding: B? = null
    protected val binding get() = _binding!!

    abstract fun provideBinding(layoutInflater: LayoutInflater): B

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = provideBinding(layoutInflater)
        val builder: AlertDialog.Builder = AlertDialog.Builder(context, theme)
        builder.setView(binding.root)
        return builder.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.statusBarColor =
            resources.getColor(android.R.color.transparent, requireActivity().theme)
        dialog?.window?.navigationBarColor =
            resources.getColor(android.R.color.transparent, requireActivity().theme)
    }
}