package com.emirsoylemez.budgetboost.ui.expense

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.emirsoylemez.budgetboost.ui.MainActivity
import com.emirsoylemez.budgetboost.R
import com.emirsoylemez.budgetboost.databinding.FragmentExpenseBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpenseFragment : Fragment() {
    private var _binding: FragmentExpenseBinding? = null
    private val binding get() = _binding!!
    private val auth = Firebase.auth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenseBinding.inflate(inflater, container, false)
        (activity as MainActivity).setBottomNavigationVisibility(View.VISIBLE)
        binding.textInputLayoutAddInstallment.visibility = View.GONE

        val categories = resources.getStringArray(R.array.expense_categories)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter

        return binding.root
    }

    private fun logExpense() {
        val expenseName = binding.expenseName.text.toString()
        val expenseAmount = binding.expenceAmount.text.toString().toDoubleOrNull()
        val installmentsText = binding.installmentsText.text.toString()
        val paymentType = when (binding.paymentTypeGroup.checkedRadioButtonId) {
            R.id.radio_cash -> "Cash"
            R.id.radio_installments -> "Installment"
            else -> null
        }
        val installments =
            if (paymentType == "Installment") installmentsText.toIntOrNull() else null

        val moneyType = when (binding.moneyTypeGroup.checkedRadioButtonId) {
            R.id.radio_tl -> "₺"
            R.id.radio_euro -> "€"
            R.id.radio_dolar -> "$"
            else -> null
        }


        val selectedCategory = binding.categorySpinner.selectedItem.toString()



        if (expenseName == "" || expenseAmount == null || paymentType == null) {
            Toast.makeText(
                requireContext(),
                "Please enter all required fields",
                Toast.LENGTH_LONG
            ).show()
        } else {
            val userId = auth.currentUser?.uid
            val db = Firebase.firestore

            val user = hashMapOf(
                "nameOfExpense" to expenseName,
                "amountOfExpense" to expenseAmount,
                "paymentType" to paymentType,
                "installments" to installments,
                "category" to selectedCategory,
                "userId" to userId,
                "Id" to id,
                "moneyType" to moneyType,
                "timestamp" to com.google.firebase.Timestamp.now()

            )


            //db.collection("expenses")
            //                .add(user)
            db.collection("users").document("$userId").collection("expenses")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    val documentId = documentReference.id
                    documentReference.update("id", documentId)
                        .addOnSuccessListener {
                            Log.d(TAG, "successfully updated id: $documentId")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error", e)
                        }


                    Log.d(TAG, "DocumentSnapshot successfully written with ID: $documentId")
                }
                .addOnFailureListener { _ ->
                    Log.w(
                        TAG,
                        "Error writing document"
                    )
                }
        }

        val navController = findNavController()
        navController.popBackStack()
        navController.navigate(R.id.expenseFragment)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.moneyTypeGroup.check(R.id.radio_tl)

        binding.addButton.setOnClickListener { logExpense() }
        binding.paymentTypeGroup.setOnCheckedChangeListener { _, checkedId ->
            binding.textInputLayoutAddInstallment.visibility =
                if (checkedId == R.id.radio_installments) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
    }

    //binding.installmentsText.visibility = if (checkedId == R.id.radio_installments) {
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


