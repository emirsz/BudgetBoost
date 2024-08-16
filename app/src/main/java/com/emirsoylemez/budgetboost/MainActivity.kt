package com.emirsoylemez.budgetboost

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.emirsoylemez.budgetboost.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
       // replaceFragment(HomeFragment())
       // setContentView(R.layout.activity_main)

      //  setBottomNavigationVisibility(View.INVISIBLE)


        binding.bottom.setOnItemSelectedListener { item ->
            val navController = findNavController(R.id.fragmentContainerView)
            when(item.itemId){
                R.id.home -> navController.navigate(R.id.homeFragment)
                //R.id.addExpense -> replaceFragment()
                R.id.add -> navController.navigate(R.id.expenseFragment)
                R.id.profile -> navController.navigate(R.id.profileFragment)
            }
            true
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun setBottomNavigationVisibility(visibility: Int) {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom)
        bottomNavigationView.visibility = visibility
    }


/*
    fun menuFragment(view:View) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val firstFragment = BlankFragment()
        fragmentTransaction.add(R.id.frameLayout, firstFragment)
    }

 */



}