package com.emirsoylemez.budgetboost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ArrayAdapter
import androidx.navigation.fragment.navArgs
import com.emirsoylemez.budgetboost.databinding.FragmentEditBinding


class EditFragment : Fragment() {
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var expense: Expense


  /*  var selectedSpinnerItem: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { //?
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        val args: EditFragmentArgs by navArgs()
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
/*
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
        //return inflater.inflate(R.layout.fragment_edit, container, false)
        return binding.root
    }




}