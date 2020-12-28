package com.example.movies.ui.registration.finish_registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.movies.R
import com.example.movies.ui.MainActivity
import com.example.movies.ui.registration.RegistrationActivity
import com.example.movies.utils.util.moveToActivity

class FinishRegistrationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_finish_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val registrationActivity = activity as RegistrationActivity
        registrationActivity.findViewById<Button>(R.id.nextBtn).run {
            text = getString(R.string.finish)
            setOnClickListener {
                registrationActivity.moveToActivity(MainActivity::class.java)
            }
        }
    }

    companion object {
        fun newInstance() = FinishRegistrationFragment()
    }
}