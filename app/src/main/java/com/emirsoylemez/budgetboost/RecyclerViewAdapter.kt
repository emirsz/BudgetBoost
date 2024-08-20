package com.emirsoylemez.budgetboost

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.emirsoylemez.budgetboost.databinding.ItemCardViewBinding

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){

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

    class ViewHolder(val binding: ItemCardViewBinding ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(expense: Expense, onEditClick: ((Expense) -> Unit)?, onDeleteClick: ((Expense) -> Unit)?){
            binding.tvExpenseName.text = expense.nameOfExpense
            binding.tvExpenseAmount.text = expense.amountOfExpense.toString()

            binding.tvCurrentlySymbol.text= expense.moneyType
//            binding.moneyTypeGroup.text=expense.moneyType
//            val selectedCurrencyText = binding.selectedCurrency
//            selectedCurrencyText.text=moneyType

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
        val binding = ItemCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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