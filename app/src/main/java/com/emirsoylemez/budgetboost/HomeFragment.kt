package com.emirsoylemez.budgetboost

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.emirsoylemez.budgetboost.databinding.FragmentHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private lateinit var expenseadapter: RecyclerViewAdapter
    private val expenseList = ArrayList<Expense>()
    private val auth = Firebase.auth
    val userId = auth.currentUser?.uid


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
        fetchExpenses()
        setupRecyclerView()
        setupSearch()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        expenseadapter = RecyclerViewAdapter().apply {
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
                if (expense.isEmpty()) {
                    binding.recyclerView.visibility = View.GONE
                    binding.emptyView.visibility = View.VISIBLE
                } else {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.emptyView.visibility = View.GONE
                }

            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
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
                            /*Toast.makeText(requireContext(), "Expense deleted", Toast.LENGTH_SHORT)
                                .show()*/
                            val position = expenseadapter.expenseList.indexOf(expense)
                            if (position != -1) {
                                expenseadapter.expenseList.removeAt(position)
                                expenseadapter.notifyItemRemoved(position)
                                if (expenseadapter.expenseList.isEmpty()) {
                                    binding.recyclerView.visibility = View.GONE
                                    binding.emptyView.visibility = View.VISIBLE
                                } else {
                                    binding.recyclerView.visibility = View.VISIBLE
                                    binding.emptyView.visibility = View.GONE
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