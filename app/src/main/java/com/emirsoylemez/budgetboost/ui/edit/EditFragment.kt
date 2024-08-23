package com.emirsoylemez.budgetboost.ui.edit

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.emirsoylemez.budgetboost.ui.Expense
import com.emirsoylemez.budgetboost.R
import com.emirsoylemez.budgetboost.databinding.FragmentEditBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditFragment : Fragment() {
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var expense: Expense
    private val args: EditFragmentArgs by navArgs()
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val userId = auth.currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.addButton2.setOnClickListener {
            updateExpense()
        }

        binding.paymentTypeGroup2.setOnCheckedChangeListener { _, checkedId ->
            binding.textInputLayoutEditNumberInstallment.visibility =
                if (checkedId == R.id.radio_installments2) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }

        expense = args.expense
        binding.expenseName2.setText(expense.nameOfExpense)
        binding.expenceAmount2.setText(expense.amountOfExpense.toString())
        binding.installmentsText2.setText(expense.installments?.toString() ?: "")

        when (expense.paymentType) {
            "Cash" -> binding.paymentTypeGroup2.check(R.id.radio_cash)
            "Installment" -> binding.paymentTypeGroup2.check(R.id.radio_installments)
        }
        if (expense.paymentType == "Cash") {
            binding.radioCash2.isChecked = true
        } else if (expense.paymentType == "Installment") {
            binding.radioInstallments2.isChecked = true
        }


        when (expense.moneyType) {
            "₺" -> binding.moneyTypeGroup2.check(R.id.radio_tl)
            "€" -> binding.moneyTypeGroup2.check(R.id.radio_euro)
            "$" -> binding.moneyTypeGroup2.check(R.id.radio_dolar)
        }
        if (expense.moneyType == "₺") {
            binding.radioTl2.isChecked = true
        } else if (expense.moneyType == "€") {
            binding.radioEuro2.isChecked = true
        } else if (expense.moneyType == "$") {
            binding.radioDolar2.isChecked = true
        }


        val categories = resources.getStringArray(R.array.expense_categories)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner2.adapter = adapter
        val categoryPosition = categories.indexOf(expense.category)
        if (categoryPosition != -1) {
            binding.categorySpinner2.setSelection(categoryPosition)
        }

        binding.ivArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun updateExpense() {
        val expenseName = binding.expenseName2.text.toString()
        val expenseAmount = binding.expenceAmount2.text.toString().toDoubleOrNull()
        val installmentsText = binding.installmentsText2.text.toString()
        val paymentType = when (binding.paymentTypeGroup2.checkedRadioButtonId) {
            R.id.radio_cash2 -> "Cash"
            R.id.radio_installments2 -> "Installment"
            else -> null
        }

        val moneyType = when (binding.moneyTypeGroup2.checkedRadioButtonId) {
            R.id.radio_tl2 -> "₺"
            R.id.radio_euro2 -> "€"
            R.id.radio_dolar2 -> "$"
            else -> null
        }

        val installments =
            if (paymentType == "Installment") installmentsText.toIntOrNull() else null
        val selectedCategory = binding.categorySpinner2.selectedItem.toString()

        if (expenseName.isNotEmpty() && expenseAmount != null && paymentType != null && expense.id != null) {
            val updatedExpense = mapOf(
                "nameOfExpense" to expenseName,
                "amountOfExpense" to expenseAmount,
                "paymentType" to paymentType,
                "installments" to installments,
                "moneyType" to moneyType,
                "category" to selectedCategory
            )
            expense.id?.let { expenseId ->
                db.collection("users").document("$userId").collection("expenses")
                    .document(expenseId)
                    .update(updatedExpense)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully updated!")
                        findNavController().navigateUp()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error updating document", e)
                    }
            }
            //db.collection("expenses").document(expense.id!!)

        } else {
            Toast.makeText(
                requireContext(),
                "Please fill in all required fields",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}