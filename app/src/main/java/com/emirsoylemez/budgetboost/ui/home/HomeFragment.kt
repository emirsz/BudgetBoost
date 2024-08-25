package com.emirsoylemez.budgetboost.ui.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.emirsoylemez.budgetboost.R
import com.emirsoylemez.budgetboost.domain.model.Expense
import com.emirsoylemez.budgetboost.ui.MainActivity
import com.emirsoylemez.budgetboost.databinding.FragmentHomeBinding
import com.emirsoylemez.budgetboost.util.Status
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private lateinit var expenseadapter: ExpenseAdapter
    private val expenseList = ArrayList<Expense>()
    private val auth = Firebase.auth
    val userId = auth.currentUser?.uid
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setBottomNavigationVisibility(View.VISIBLE)
        viewModel=ViewModelProvider(this).get(HomeViewModel::class.java)
        setupRecyclerView()
        setupSearch()
        setupChipGroupListener()
        observeCurrencyData()
        fetchExpenses()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        expenseadapter = ExpenseAdapter().apply {
            setOnMenuClickListener(
                onEditClick = { expense ->
                    val action = HomeFragmentDirections.actionHomeFragmentToEditFragment(expense)
                    findNavController().navigate(action)
                },
                onDeleteClick = { expense ->
                    showDeletePopup(expense)
                }
            )
        }
        binding.recyclerView.adapter = expenseadapter
    }

    private fun fetchExpenses() {

        db.collection("users").document("$userId").collection("expenses")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val expense = result.toObjects(Expense::class.java)
                expenseList.clear()
                expenseList.addAll(expense)
                expenseadapter.submitList(expense)
                with(binding){
                    if (expense.isEmpty()) {
                        recyclerView.visibility = View.GONE
                        chipGroupExpenses.visibility=View.GONE
                        emptyView.visibility = View.VISIBLE
                        searchView.visibility = View.GONE
                    } else {
                        recyclerView.visibility = View.VISIBLE
                        emptyView.visibility = View.GONE
                        chipGroupExpenses.visibility = View.VISIBLE
                        searchView.visibility = View.VISIBLE
                    }
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }

    private fun observeCurrencyData() {
        viewModel.currencyData.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.let { expenseResponse ->
                        val selectedChipId = binding.chipGroupExpenses.checkedChipId
                        val updatedExpenses = expenseList.map { expense ->
                            val convertedAmount = when (expense.moneyType) {
                                "₺" -> expense.amountOfExpense?.div(
                                    expenseResponse.data["TRY"] ?: 1.0
                                )

                                "$" -> expense.amountOfExpense?.div(
                                    expenseResponse.data["USD"] ?: 1.0
                                )

                                "€" -> expense.amountOfExpense?.div(
                                    expenseResponse.data["EUR"] ?: 1.0
                                )

                                else -> expense.amountOfExpense
                            }

                            val updatedMoneyType: String? = when (selectedChipId) {
                                R.id.chipTRY -> "₺"
                                R.id.chipUSD -> "$"
                                R.id.chipEUR -> "€"
                                else -> expense.moneyType
                            }

                            val formattedAmount = String.format("%.1f", convertedAmount)
                            val updatedExpense = expense.copy(
                                amountOfExpense = formattedAmount.toDouble(),
                                moneyType = updatedMoneyType
                            )

                            expenseadapter.expenseList.find { it.id == updatedExpense.id }?.let {
                                val viewHolder = binding.recyclerView.findViewHolderForAdapterPosition(
                                    expenseadapter.expenseList.indexOf(it)
                                ) as? ExpenseAdapter.ViewHolder
                                viewHolder?.updateInstallments(updatedExpense)
                            }
                            updatedExpense
                        }
                        expenseadapter.submitList(updatedExpenses)
                    }
                }

                Status.ERROR -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }

                Status.LOADING -> {
                    // no-op
                }
            }
        }
    }

    private fun setupChipGroupListener() {
        binding.chipGroupExpenses.setOnCheckedStateChangeListener { _, checkedIds ->
            when (checkedIds.firstOrNull()) {
                R.id.chipTRY -> viewModel.fetchExpenses("₺")
                R.id.chipEUR -> viewModel.fetchExpenses("€")
                R.id.chipUSD -> viewModel.fetchExpenses("$")
                else -> fetchExpenses()
            }
            }
        }

    private fun showDeletePopup(expense: Expense) {
        val alert = AlertDialog.Builder(binding.root.context)
        alert.setTitle("Warning")
        alert.setMessage("Are you sure you want to delete?")

        alert.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
            val db = FirebaseFirestore.getInstance()
            override fun onClick(p0: DialogInterface?, p1: Int) {
                if (!expense.id.isNullOrEmpty()) {
                    Log.d("ExpenseID", "Expense ID: ${expense.id}")
                    //db.collection("expenses").document(expense.id)
                    db.collection("users").document("$userId").collection("expenses")
                        .document(expense.id)
                        .delete()
                        .addOnSuccessListener {
                            val position = expenseadapter.expenseList.indexOf(expense)
                            if (position != -1) {
                                expenseadapter.expenseList.removeAt(position)
                                expenseadapter.notifyItemRemoved(position)
                                with(binding){
                                    if (expenseadapter.expenseList.isEmpty()) {
                                        recyclerView.visibility = View.GONE
                                        emptyView.visibility = View.VISIBLE
                                        chipGroupExpenses.visibility = View.GONE
                                    } else {
                                        recyclerView.visibility = View.VISIBLE
                                        emptyView.visibility = View.GONE
                                        chipGroupExpenses.visibility = View.VISIBLE
                                    }
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                requireContext(),
                                "Error deleting expense: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    Toast.makeText(binding.root.context, "Deleted!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Invalid expense", Toast.LENGTH_SHORT).show()
                }
            }
        })

        alert.setNegativeButton("No") { _, _ ->
            Toast.makeText(binding.root.context, "Not Deleted!", Toast.LENGTH_LONG).show()
        }
        alert.show()
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    filterExpenses(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    filterExpenses(it)
                }
                return true
            }

        })
    }

    private fun filterExpenses(query: String) {
        val filterList = expenseList.filter {
            it.nameOfExpense?.contains(query, ignoreCase = true) == true
        }
        expenseadapter.submitList(filterList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}