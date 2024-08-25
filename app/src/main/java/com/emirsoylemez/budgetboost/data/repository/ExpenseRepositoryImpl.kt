package com.emirsoylemez.budgetboost.data.repository

import com.emirsoylemez.budgetboost.data.model.ExpenseResponse
import com.emirsoylemez.budgetboost.data.network.ExpenseAPI
import com.emirsoylemez.budgetboost.domain.repository.ExpenseRepository
import com.emirsoylemez.budgetboost.util.Resource
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val expenseAPI:ExpenseAPI
) : ExpenseRepository {
    override suspend fun getExpenses(
        baseCurrency: String,
        currencies: String
    ): Resource<ExpenseResponse> {
        return try {
            val response = expenseAPI.getExpenses(baseCurrency,currencies)
            Resource.success(response)
        } catch (e:Exception){
            Resource.error(e.message ?: "Unexpected error occurred!",null)
        }
    }
}