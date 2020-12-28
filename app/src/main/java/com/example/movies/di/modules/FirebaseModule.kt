package com.example.movies.di.modules

import com.example.movies.network.FirebaseRDBServiceAccount
import com.example.movies.network.AccountRemoteService
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {
    @Provides
    fun provideAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    fun provideAuthUI(): AuthUI {
        return AuthUI.getInstance()
    }

    @Provides
    fun provideReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference
    }

    @Provides
    fun provideRemote(databaseReference: DatabaseReference): AccountRemoteService {
        return FirebaseRDBServiceAccount(databaseReference)
    }

    @Provides
    fun provideFirebaseStorageRef(): StorageReference{
        return FirebaseStorage.getInstance().reference
    }
}