package com.emirsoylemez.budgetboost.data.model

import com.google.gson.annotations.SerializedName

data class ExpenseResponse(
    @SerializedName("data")
    val data:Map<String,Double>
)

