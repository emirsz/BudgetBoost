package com.emirsoylemez.budgetboost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.emirsoylemez.budgetboost.databinding.FragmentHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private lateinit var expenseadapter: RecyclerViewAdapter
    private val expenseList = ArrayList<Expense>()
    private val auth = Firebase.auth //bu-----

   /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }*/


       val userId = auth.currentUser?.uid //bu-----


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {  //viewden sonra ? vardı
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        (activity as MainActivity).setBottomNavigationVisibility(View.VISIBLE)





        db.collection("expenses")
            .whereEqualTo("userId", userId)
                //val userId = auth.currentUser?.uid
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // Firestore'dan gelen verileri modelClass.Expense'e dönüştürüyoruz
                    val expense = document.toObject(Expense::class.java)
                    expenseList.add(expense)
                }
                // Veriler değiştiğinde adapter'i bilgilendiriyoruz
                expenseadapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Hata durumunda yapılacaklar
                println("Error getting documents: $exception")
            }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        expenseadapter = RecyclerViewAdapter(expenseList)
        binding.recyclerView.adapter = expenseadapter
        return binding.root
    }



    //expenseadapter = RecyclerViewAdapter()
    // Inflate the layout for this fragment
    //return inflater.inflate(R.layout.fragment_home, container, false)
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}