package com.example.movies.di

import com.example.movies.di.scope.ActivityScope
import com.example.movies.ui.registration.RegistrationActivity
import com.example.movies.ui.registration.choose_picture.PictureChooseFragment
import com.example.movies.ui.registration.enter_details.EnterDetailsFragment
import dagger.Subcomponent

// Scope annotation that the RegistrationComponent uses
// Classes annotated with @ActivityScope will have a unique instance in this Component
@ActivityScope
// Definition of a Dagger subcomponent
@Subcomponent
interface RegistrationComponent {

    // Factory to create instances of RegistrationComponent
    @Subcomponent.Factory
    interface Factory {
       fun create(): RegistrationComponent
    }

    // Classes that can be injected by this Component
    fun inject(activity: RegistrationActivity)
    fun inject(fragment: EnterDetailsFragment)
    fun inject(fragment: PictureChooseFragment)
}