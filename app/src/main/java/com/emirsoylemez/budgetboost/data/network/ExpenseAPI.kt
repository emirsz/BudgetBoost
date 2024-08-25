package com.emirsoylemez.budgetboost.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import com.emirsoylemez.budgetboost.BuildConfig
import com.emirsoylemez.budgetboost.data.model.ExpenseResponse

interface ExpenseAPI {

    @GET("latest")
    suspend fun getExpenses(
        @Query("base_currency") baseCurrency: String = "TRY",
        @Query("currencies") currency: String = "EUR,USD",
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ):ExpenseResponse
}