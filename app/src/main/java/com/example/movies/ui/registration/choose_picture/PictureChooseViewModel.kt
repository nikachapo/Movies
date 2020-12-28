package com.example.movies.ui.registration.choose_picture

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.example.movies.ui.registration.Success
import com.example.movies.ui.registration.UploadFailed
import com.example.movies.ui.registration.UploadState
import com.example.movies.storage.FirebaseFileStorage
import com.example.movies.utils.util.KEY_ACCOUNTS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PictureChooseViewModel @Inject constructor(
    private val storage: FirebaseFileStorage,
    private val reference: DatabaseReference,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uploadState = MutableLiveData<UploadState>()

    val uploadState: LiveData<UploadState> = _uploadState

    fun uploadPicture(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            if (storage.setObjectAtLocation(
                    reference.child(KEY_ACCOUNTS)
                        .child(firebaseAuth.currentUser!!.uid), uri
                )
            ) {
                _uploadState.postValue(Success)
            } else {
                _uploadState.postValue(UploadFailed())
            }
        }
    }

}