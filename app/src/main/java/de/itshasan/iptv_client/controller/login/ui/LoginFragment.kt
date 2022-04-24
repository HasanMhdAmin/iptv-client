package de.itshasan.iptv_client.controller.login.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.itshasan.iptv_client.databinding.FragmentLoginBinding
import de.itshasan.iptv_client.utils.DialogUtils
import de.itshasan.iptv_client.utils.navigator.Navigator

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            viewModel.requestLoginUser(
                binding.serverUrlTextView.text.toString(),
                binding.usernameTextView.text.toString(),
                binding.passwordTextView.text.toString()
            )
        }

        this.viewModel.user.observe(requireActivity()) {
            if (it != null) {
                viewModel.saveUserCredential()
                requireActivity().finish()
                Navigator.goToMainActivity(requireActivity())
            } else {
                DialogUtils.showAlertDialog(
                    requireActivity(),
                    cancelable = true,
                    message = "Check your credential!",
                    positiveButtonText = "Close",
                )
            }
        }


    }

}