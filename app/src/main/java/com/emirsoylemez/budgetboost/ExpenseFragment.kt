package com.emirsoylemez.budgetboost

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.emirsoylemez.budgetboost.databinding.FragmentExpenseBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ExpenseFragment : Fragment() {
    private var _binding: FragmentExpenseBinding? = null
    private val binding get() = _binding!!
    private val auth = Firebase.auth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentExpenseBinding.inflate(inflater, container, false)
        (activity as MainActivity).setBottomNavigationVisibility(View.VISIBLE)
        return binding.root
    }

    private fun logExpense() {
        val expensename = binding.expenseName.text.toString()
        val expenseamount = binding.expenceAmount.text.toString().toDoubleOrNull()
        if (expensename == "" || expenseamount == null) {
            Toast.makeText(
                requireContext(),
                "Please enter a valid expense name and amount",
                Toast.LENGTH_LONG
            ).show()
        } else {
            val userId = auth.currentUser?.uid
            val db = Firebase.firestore

            val user = hashMapOf(
                "nameOfExpense" to expensename,
                "amountOfExpense" to expenseamount,
                "userId" to userId,
                "timestamp" to com.google.firebase.Timestamp.now()
            )

            db.collection("expenses")
                .add(user)
                //.document("EXPENSES")
                //.set(user)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshow successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addButton.setOnClickListener { logExpense() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


