package com.emirsoylemez.budgetboost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.emirsoylemez.budgetboost.databinding.FragmentRegisterBinding

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val auth = Firebase.auth

   /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }*/


    private fun passwordSignUp() {
        val email = binding.editTextSignName.text.toString()
        val password = binding.editTextSignPass.text.toString()
        if (email == "" || password == "") {
            Toast.makeText(requireContext(), "Enter email and password", Toast.LENGTH_LONG).show()
        } else {
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                Toast.makeText(requireContext(), "Sign up successful", Toast.LENGTH_LONG).show()
                //val intent=Intent(context,BlankFragment)
                //val action = BlankFragment2Directions.actionBlankFragment2ToBlankFragment()
                // Navigation.findNavController(view).navigate(action)
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_registerFragment_to_loginFragment)
            }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {  // ?

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        (activity as MainActivity).setBottomNavigationVisibility(View.GONE)
        return binding.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_blank2, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSign.setOnClickListener { passwordSignUp() }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}