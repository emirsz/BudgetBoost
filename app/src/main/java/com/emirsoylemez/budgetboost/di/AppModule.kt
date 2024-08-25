package com.emirsoylemez.budgetboost.di

import com.emirsoylemez.budgetboost.data.network.ExpenseAPI
import com.emirsoylemez.budgetboost.data.repository.ExpenseRepositoryImpl
import com.emirsoylemez.budgetboost.domain.repository.ExpenseRepository
import com.emirsoylemez.budgetboost.util.Constants.BASE_URL
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit=Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    @Singleton
    @Provides
    fun provideAPIService(retrofit: Retrofit):ExpenseAPI=
        retrofit.create(ExpenseAPI::class.java)

    @Singleton
    @Provides
    fun provideExpenseRepository(
        expenseAPI: ExpenseAPI
    ):ExpenseRepository{
        return ExpenseRepositoryImpl(expenseAPI)
    }
}