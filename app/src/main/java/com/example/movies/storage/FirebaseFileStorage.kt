package com.example.movies.storage

import android.net.Uri
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.example.movies.utils.util.getSnapshotValue
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@Suppress("PrivatePropertyName")
class FirebaseFileStorage @Inject constructor(
    private val storageReference: StorageReference
) :
    Storage<DatabaseReference, Uri> {

    private val KEY_PHOTO_URL = "photoUrl"

    override suspend fun getObjectAtLocation(key: DatabaseReference): Uri? {
        key.child(KEY_PHOTO_URL)
            .getSnapshotValue()
            .getValue(String::class.java)
            .also {
                return Uri.parse(it)
            }
    }

    override suspend fun setObjectAtLocation(key: DatabaseReference, value: Uri): Boolean {
        return try {
            key.child(KEY_PHOTO_URL)
                .setValue(putFileToStorage(value))
                .await()
            true
        } catch (e: CancellationException) {
            false
        }
    }

    @Throws(CancellationException::class)
    private suspend fun putFileToStorage(uri: Uri): String {
        var url = ""
        val picRef = storageReference
            .child(System.currentTimeMillis().toString())
        picRef.putFile(uri)
            .continueWithTask {
                if (!it.isSuccessful && it.exception != null) {
                    throw it.exception!!
                }
                picRef.downloadUrl
            }.addOnSuccessListener {
                url = it.toString()
            }.await()
        return url
    }
}