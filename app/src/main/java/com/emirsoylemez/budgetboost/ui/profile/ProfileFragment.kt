package com.emirsoylemez.budgetboost.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.emirsoylemez.budgetboost.ui.MainActivity
import com.emirsoylemez.budgetboost.R
import com.emirsoylemez.budgetboost.databinding.FragmentProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val auth = Firebase.auth
    val userId = auth.currentUser?.uid
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        (activity as MainActivity).setBottomNavigationVisibility(View.VISIBLE)
        return binding.root
    }

    private fun logOut() {
        if (auth.currentUser != null) {
            auth.signOut()
            Navigation.findNavController(requireView())
                .navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogOut.setOnClickListener { logOut() }

        val currentUser = auth.currentUser
        currentUser?.let {
            binding.pemailText.text = it.email
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



