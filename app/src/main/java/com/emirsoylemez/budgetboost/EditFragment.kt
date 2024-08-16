package com.emirsoylemez.budgetboost

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
import com.emirsoylemez.budgetboost.databinding.FragmentEditBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditFragment : Fragment() {
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var expense: Expense
    private val args: EditFragmentArgs by navArgs()
    val db = Firebase.firestore
    val auth = Firebase.auth
    val userId = auth.currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { //?
        _binding = FragmentEditBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.addButton2.setOnClickListener {
            updateExpense()
        }

        expense = args.expense
        binding.expenseName2.setText(expense.nameOfExpense)
        binding.expenceAmount2.setText(expense.amountOfExpense.toString())
        binding.installmentsText2.setText(expense.installments.toString())

        when(expense.paymentType){
            "Cash" -> binding.paymentTypeGroup2.check(R.id.radio_cash)
            "Installment" -> binding.paymentTypeGroup2.check(R.id.radio_installments)
        }
        if (expense.paymentType == "Cash") {
            binding.radioCash2.isChecked = true
        } else if (expense.paymentType == "Installment") {
            binding.radioInstallments2.isChecked = true
        }
        val categories = resources.getStringArray(R.array.expense_categories)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
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
            val installments = if (paymentType == "Installment") installmentsText.toIntOrNull() else null
            val selectedCategory = binding.categorySpinner2.selectedItem.toString()

            if (expenseName.isNotEmpty() && expenseAmount != null && paymentType != null && expense.id != null) {
                val updatedExpense = mapOf(
                    "nameOfExpense" to expenseName,
                    "amountOfExpense" to expenseAmount,
                    "paymentType" to paymentType,
                    "installments" to installments,
                    "category" to selectedCategory
                )
                expense.id?.let { expenseId ->
                    db.collection("users").document("$userId").collection("expenses").document(expenseId)
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
                Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            }
        }

//        var expenseName = binding.expenseName2.text
//        var expenseAmount = binding.expenceAmount2.text
//        val paymentType = when (binding.paymentTypeGroup2.checkedRadioButtonId) {
//            R.id.radio_cash -> "Cash"
//            R.id.radio_installments -> "Installment"
//            else -> null
//        }
//        val installments = if (paymentType == "Installment"){
//
//        }
//        //var installments= binding.expenseName2.text
//        var  selectedCategory= binding.expenseName2.text
//        //var userId = binding.
//        val id =
//
//            hashMapOf(
//            "expenseName2" to expenseName,
//            "expenceAmount2" to expenseAmount,
//            "paymentTypeGroup2" to paymentType,
//            "installmentsText2" to installments,
//            "categorySpinner2" to selectedCategory,
//            "userId" to userId,
//            "Id" to id,
//            "timestamp" to com.google.firebase.Timestamp.now() )



/*
    private fun updateDoc(){

        val db = Firebase.firestore
        val sec= db.collection("expenses")
        db.collection("expenses")
            .update("sec",true)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }


    }*/




}/*
        binding.categorySpinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Seçilen öğeyi al
                val selectedItem = parent?.getItemAtPosition(position).toString()
                // Seçilen öğe ile yapılacak işlemler
                this@EditFragment.selectedSpinnerItem = selectedItem
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Hiçbir şey seçilmediğinde yapılacak işlemler
            }
        }
        Expense(
            nameOfExpense = ,
            amountOfExpense = ,
        )*/
//
//        val spinnerAdapter = ArrayAdapter.createFromResource(
//            requireContext(),
//            R.array.expense_categories, // Spinner'ın kaynak verisi
//            android.R.layout.simple_spinner_item
//        )
//binding.categorySpinner2.selectedItem(expense.category)

//        binding.paymentTypeGroup2.checkedRadioButtonId
//        when(expense.paymentType){
//                "Cash" -> binding.paymentTypeGroup2.check(R.id.radio_cash)
//                "Installment" -> binding.paymentTypeGroup2.check(R.id.radio_installments)
//        }
// Argümanlardan masraf ID'sini al
// val expenseId = arguments?.getString("expenseId")

// Veriyi Firestore'dan çek ve UI alanlarını doldur
//fetchExpenseData(expenseId)

// Inflate the layout for this fragment
//return inflater.inflate(R.layout.fragment_edit, container, false) /*  var selectedSpinnerItem: String? = null
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }*/