package com.example.movies.di.modules

import android.content.Context
import android.net.Uri
import com.example.movies.storage.FirebaseFileStorage
import com.example.movies.storage.SharedPreferencesStorage
import com.example.movies.storage.Storage
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class SharedPref

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class FirebaseFile

@Module
class StorageModule {
    @SharedPref
    @Provides
    fun provideSharedPrefStorage(context: Context): Storage<String, String> {
        return SharedPreferencesStorage(context)
    }

    @FirebaseFile
    @Provides
    fun provideFirebase(ref: StorageReference): Storage<DatabaseReference, Uri> {
        return FirebaseFileStorage(ref)
    }
}