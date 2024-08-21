package com.emirsoylemez.budgetboost

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emirsoylemez.budgetboost.databinding.ItemCardViewBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    val expenseList = mutableListOf<Expense>()
    private var onEditClick: ((Expense) -> Unit)? = null
    private var onDeleteClick: ((Expense) -> Unit)? = null


    fun submitList(expenseList: List<Expense>) {
        this.expenseList.clear()
        this.expenseList.addAll(expenseList)
        notifyDataSetChanged()
    }

    fun setOnMenuClickListener(onEditClick: (Expense) -> Unit, onDeleteClick: (Expense) -> Unit) {
        this.onEditClick = onEditClick
        this.onDeleteClick = onDeleteClick
    }

    class ViewHolder(val binding: ItemCardViewBinding) : RecyclerView.ViewHolder(binding.root) {

        private fun calculateInstallments(
            totalAmount: Double,
            installments: Int,
            startDate: Date
        ): List<String> {
            val installmentList = mutableListOf<String>()
            var roundedAmountPerInstallment = (totalAmount / installments).let {
                kotlin.math.round(it * 100) / 100
            }
            var totalCalculatedAmount = 0.0
            val calendar = Calendar.getInstance()
            calendar.time = startDate

            for (i in 1..installments) {
                if (i == installments) {
                    roundedAmountPerInstallment =
                        kotlin.math.round((totalAmount - totalCalculatedAmount) * 100) / 100
                }

                val month = SimpleDateFormat("MMMM ", Locale("en")).format(calendar.time)
                installmentList.add("$month: $roundedAmountPerInstallment")
                totalCalculatedAmount += roundedAmountPerInstallment
                calendar.add(Calendar.MONTH, 1)
            }

            return installmentList
        }


        fun bind(
            expense: Expense,
            onEditClick: ((Expense) -> Unit)?,
            onDeleteClick: ((Expense) -> Unit)?
        ) {
            binding.tvExpenseName.text = expense.nameOfExpense
            binding.tvExpenseAmount.text = expense.amountOfExpense.toString()
            binding.tvCategoryText.text = expense.category
            binding.tvCurrentlySymbol.text = expense.moneyType
            binding.tvPaymentType.text = expense.paymentType
//            binding.moneyTypeGroup.text=expense.moneyType
//            val selectedCurrencyText = binding.selectedCurrency
//            selectedCurrencyText.text=moneyType

            val timestamp = expense.timestamp
            val date: Date = timestamp!!.toDate()
            val dateFormat = SimpleDateFormat("MMMM", Locale("en"))
            val monthName = dateFormat.format(date)
            binding.tvDate.text = monthName


            binding.viewAllInstallments.visibility = if (expense.paymentType == "Installment") {
                View.VISIBLE
            } else {
                View.GONE
            }

            binding.viewAllInstallments.setOnClickListener {
                if (binding.installmentsRecyclerView.visibility == View.GONE) {
                    binding.installmentsRecyclerView.visibility = View.VISIBLE


                    val installmentList = expense.amountOfExpense?.let {
                        calculateInstallments(it, expense.installments ?: 1, date)
                    }

                    if (installmentList != null) {
                        val adapter = InstallmentsAdapter(installmentList)
                        binding.installmentsRecyclerView.adapter = adapter
                        binding.installmentsRecyclerView.layoutManager =
                            LinearLayoutManager(binding.root.context)
                    }

                } else {
                    binding.installmentsRecyclerView.visibility = View.GONE
                }
            }


            binding.moreOptions.setOnClickListener { view ->
                val popupMenu = PopupMenu(view.context, view)
                popupMenu.inflate(R.menu.carditem_menu)

                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_edit -> {
                            onEditClick?.invoke(expense)
                            true
                        }

                        R.id.action_delete -> {
                            onDeleteClick?.invoke(expense)
                            true
                        }

                        else -> false
                    }
                }
                popupMenu.show()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.binding.tvExpenseName.text = expenseList[position].toString()
//        holder.bind(expense)
        val expense = expenseList[position]
        holder.bind(expense, onEditClick, onDeleteClick)

//        holder.binding.tvExpenseName.text = expense.nameOfExpense
//        holder.binding.tvExpenseAmount.text = expense.amountOfExpense.toString()
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }
}