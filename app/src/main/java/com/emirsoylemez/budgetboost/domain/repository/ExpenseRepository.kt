package com.emirsoylemez.budgetboost.domain.repository

import com.emirsoylemez.budgetboost.data.model.ExpenseResponse
import com.emirsoylemez.budgetboost.util.Resource

interface ExpenseRepository {
    suspend fun getExpenses(baseCurrency:String, currencies:String):Resource<ExpenseResponse>
}