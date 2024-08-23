package com.emirsoylemez.budgetboost.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emirsoylemez.budgetboost.databinding.ItemInstallmentBinding

class InstallmentsAdapter(
    private val installments: List<String>,
    private val moneyType: String
) :
    RecyclerView.Adapter<InstallmentsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemInstallmentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(installment: String, moneyType: String) {
            binding.installmentTextView.text = "$installment $moneyType"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemInstallmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(installments[position], moneyType)
    }

    override fun getItemCount(): Int {
        return installments.size
    }
}