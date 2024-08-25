package com.emirsoylemez.budgetboost.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.Navigation
import com.emirsoylemez.budgetboost.ui.MainActivity
import com.emirsoylemez.budgetboost.databinding.FragmentLoginBinding

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val auth = Firebase.auth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        (activity as MainActivity).setBottomNavigationVisibility(View.GONE)
        return binding.root
    }

    private fun gotoSignUp(view: View) {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        Navigation.findNavController(view).navigate(action)
    }

    private fun loginToApp(view: View) {
        val email = binding.editTextName.text.toString()
        val password = binding.editTextPass.text.toString()

        if (email == "" || password == "") {
            Toast.makeText(requireContext(), "Enter email and password!", Toast.LENGTH_LONG).show()
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                Navigation.findNavController(view).navigate(action)
            }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Password is not true!", Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
            Navigation.findNavController(view).navigate(action)
        }
        binding.buttonGotoSign.setOnClickListener { gotoSignUp(view) }
        binding.buttonLog.setOnClickListener {
            loginToApp(view)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}