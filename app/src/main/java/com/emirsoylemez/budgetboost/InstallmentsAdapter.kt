package com.emirsoylemez.budgetboost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emirsoylemez.budgetboost.databinding.ItemInstallmentBinding

class InstallmentsAdapter(private val installments: List<String>) :
    RecyclerView.Adapter<InstallmentsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemInstallmentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(installment: String) {
            binding.installmentTextView.text = installment
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemInstallmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(installments[position])
    }

    override fun getItemCount(): Int {
        return installments.size
    }
}