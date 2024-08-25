package com.emirsoylemez.budgetboost.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emirsoylemez.budgetboost.data.model.ExpenseResponse
import com.emirsoylemez.budgetboost.domain.repository.ExpenseRepository
import com.emirsoylemez.budgetboost.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {
    private val _currencyData = MutableLiveData<Resource<ExpenseResponse>>()
    val currencyData: LiveData<Resource<ExpenseResponse>> get() = _currencyData
    fun fetchExpenses(baseCurrency: String) {
        val convertedBaseCurrency = when (baseCurrency) {
            "₺" -> "TRY"
            "$" -> "USD"
            "€" -> "EUR"
            else -> null
        }
        val currencies = when (convertedBaseCurrency) {
            "TRY" -> "USD,EUR"
            "USD" -> "TRY,EUR"
            "EUR" -> "TRY,USD"
            else -> null
        }
        viewModelScope.launch {
            _currencyData.postValue(Resource.loading(null))
            try {
                if (convertedBaseCurrency != null && currencies != null) {
                    val response = repository.getExpenses(convertedBaseCurrency, currencies)
                    _currencyData.postValue(response)
                }
            } catch (e: Exception) {
                _currencyData.postValue(Resource.error(e.message ?: "error", null))
            }
        }
    }
}