package com.emirsoylemez.budgetboost.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.emirsoylemez.budgetboost.R
import com.emirsoylemez.budgetboost.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.statusBarColor = ContextCompat.getColor(this, R.color.backgr)

        // replaceFragment(HomeFragment())
        // setContentView(R.layout.activity_main)
        // setBottomNavigationVisibility(View.INVISIBLE)

        binding.bottom.setOnItemSelectedListener { item ->
            val navController = findNavController(R.id.fragmentContainerView)
            when (item.itemId) {
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

}