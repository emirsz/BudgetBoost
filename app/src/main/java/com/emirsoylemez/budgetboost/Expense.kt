package com.emirsoylemez.budgetboost

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Expense(
    var nameOfExpense: String? = null,
    var amountOfExpense: Double? = null,
    var userId: String? = null,
    val id: String? = null,
    var installments: Int? = null,
    var paymentType: String? = null,
    var category: String? = null,
    var moneyType: String? = null,
    var timestamp: com.google.firebase.Timestamp? = null
) : Parcelable
