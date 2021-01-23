package com.example.movies.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.movies.App
import com.example.movies.R
import com.example.movies.databinding.AccountProfileFragmentBinding
import com.example.movies.ui.login.LoginActivity
import com.example.movies.utils.loadImage
import com.example.movies.utils.moveToActivity
import javax.inject.Inject

class AccountProfileFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AccountProfileViewModel
    private lateinit var binding: AccountProfileFragmentBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AccountProfileFragmentBinding.bind(
            inflater.inflate(R.layout.account_profile_fragment, container, false)
        )
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(AccountProfileViewModel::class.java)

        viewModel.currentAccount.observe(viewLifecycleOwner, { account ->
            if (account != null) {
                binding.profileImageIV.loadImage(Uri.parse(account.photoUrl))
                binding.userNameTV.text = "${account.firstName} ${account.lastName}"
                binding.phoneTV.text = account.phoneNumber
            } else {
                activity?.moveToActivity(LoginActivity::class.java)
                activity?.finish()
            }
        })

        binding.signOutBtn.setOnClickListener {
            viewModel.logOut()
        }
    }

}