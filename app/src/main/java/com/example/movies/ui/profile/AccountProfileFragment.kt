package com.example.movies.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.movies.App
import com.example.movies.R
import javax.inject.Inject

class AccountProfileFragment : Fragment(R.layout.account_profile_fragment) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AccountProfileViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(AccountProfileViewModel::class.java)

        viewModel.currentAccount.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it.firstName, Toast.LENGTH_SHORT).show()
        })
    }

}