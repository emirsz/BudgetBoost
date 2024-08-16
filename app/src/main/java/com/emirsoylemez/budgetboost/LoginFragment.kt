package com.emirsoylemez.budgetboost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.emirsoylemez.budgetboost.databinding.FragmentLoginBinding

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
        //binding.buttonLog.setOnClickListener {  }
        (activity as MainActivity).setBottomNavigationVisibility(View.GONE)
        return binding.root

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_blank, container, false)

    }

    /* fun passwordSignUp(){
         val email=binding.editTextName.text.toString()
         val password=binding.editTextPass.text.toString()
         if (email.equals("") || password.equals("")){
             Toast.makeText(requireContext(),"Enter email and password",Toast.LENGTH_LONG).show()
         }

     }*/

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
                    Toast.makeText(requireContext(), "Yanlis girdin", Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        /* binding.buttonLog.setOnClickListener {
                // passwordSignUp()
         }*/

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
            Navigation.findNavController(view).navigate(action)
        }
        binding.buttonGotoSign.setOnClickListener { gotoSignUp(view) }
        binding.buttonLog.setOnClickListener {
            loginToApp(view)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*

    val currentUser = auth.currentUser
            if(currentUser != null){
                val action = BlankFragmentDirections.actionBlankFragmentToHomeFragment()
                Navigation.findNavController(view).navigate(action)
     */


}