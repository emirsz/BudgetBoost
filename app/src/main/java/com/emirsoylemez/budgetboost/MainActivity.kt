package com.emirsoylemez.budgetboost

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
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


        binding.bottom.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment())
                //R.id.addExpense -> replaceFragment()
                R.id.add -> replaceFragment(ExpenseFragment())
                R.id.profile -> replaceFragment(ProfileFragment())

                else ->{

                }
            }
            true
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView,fragment)
        fragmentTransaction.commit()
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