package com.example.movies.ui.registration.choose_picture

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.movies.R
import com.example.movies.databinding.PictureChooseFragmentBinding
import com.example.movies.ui.registration.RegistrationActivity
import com.example.movies.ui.registration.Success
import com.example.movies.ui.registration.UploadFailed
import com.example.movies.utils.loadImage
import com.example.movies.utils.util.PICK_PHOTO_FOR_AVATAR
import com.example.movies.utils.util.openGallery
import com.example.movies.utils.util.permission.PermissionHelper
import com.example.movies.utils.util.showToast
import javax.inject.Inject

class PictureChooseFragment : Fragment() {

    private lateinit var mActivity: RegistrationActivity

    private var photoUri: Uri? = null

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: PictureChooseViewModel

    private lateinit var binding: PictureChooseFragmentBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as RegistrationActivity).registrationComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PictureChooseFragmentBinding.bind(
            inflater.inflate(R.layout.picture_choose_fragment, container, false)
        )
        binding.chooseProfileIV.setOnClickListener {
            checkReadPermission()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mActivity = activity as RegistrationActivity

        requireActivity().findViewById<Button>(R.id.nextBtn).setOnClickListener {
            photoUri.run {
                if (this != null) viewModel.uploadPicture(this)
                else mActivity.showToast(R.string.choose_profile_image)
            }
        }

        viewModel = ViewModelProvider(this, factory).get(PictureChooseViewModel::class.java)
        viewModel.uploadState.observe(viewLifecycleOwner, Observer { uploadState ->
            when (uploadState) {
                is Success -> mActivity.onRegistrationFinished()
                is UploadFailed -> mActivity.showToast(uploadState.errorText)
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PermissionHelper.CODE_PERMISSION && grantResults.isNotEmpty()) {
            grantResults.forEach {
                if (it == PackageManager.PERMISSION_GRANTED) {
                    mActivity.showToast("Granted")
                    mActivity.openGallery()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK && data != null) {
            photoUri = data.data.also { binding.chooseProfileIV.loadImage(it) }
        }
    }

    private fun checkReadPermission() {
        if (!PermissionHelper.hasPermission(
                requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            requestReadPermission()
        } else {
            openGallery()
        }
    }

    private fun requestReadPermission() {
        PermissionHelper.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
    }

    companion object {
        fun newInstance() = PictureChooseFragment()
    }

}