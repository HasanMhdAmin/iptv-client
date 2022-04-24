package de.itshasan.iptv_core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class CoreFragment<B : ViewBinding, V : ViewModel>: Fragment() {

    private var _binding: B? = null
    protected val binding get() = _binding!!

    private var _viewModel: V? = null
    protected val viewModel get() = _viewModel!!

    abstract fun provideBinding(layoutInflater: LayoutInflater, container: ViewGroup?): B
    abstract fun provideViewModel(): V

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _viewModel = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewModel = provideViewModel()
        _binding = provideBinding(layoutInflater, container)

        return binding.root

    }


}