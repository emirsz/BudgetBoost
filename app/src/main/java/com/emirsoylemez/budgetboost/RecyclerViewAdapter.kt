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
        /*holder.binding.tvExpenseName.text = expenseList[position].toString()
        holder.bind(expense)*/
        val expense = expenseList[position]
        holder.bind(expense, onEditClick, onDeleteClick)
        /*
        holder.binding.tvExpenseName.text = expense.nameOfExpense
        holder.binding.tvExpenseAmount.text = expense.amountOfExpense.toString()*/
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }
}
/*package com.emirsoylemez.budgetboost

import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.emirsoylemez.budgetboost.databinding.ItemCardViewBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RecyclerViewAdapter(val expenseList: MutableList<Expense>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){

    class ViewHolder(val binding: ItemCardViewBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(expense: Expense , adapter: RecyclerViewAdapter){
            binding.tvExpenseName.text = expense.nameOfExpense
            binding.tvExpenseAmount.text = expense.amountOfExpense.toString()

            binding.moreOptions.setOnClickListener { view ->
                val popupMenu = PopupMenu(view.context, view)
                popupMenu.inflate(R.menu.carditem_menu)  // Daha önce oluşturduğumuz menü dosyasını kullan
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {

                        R.id.action_edit -> {
                            // Düzenleme işlemini buraya ekleyin
                            val action = HomeFragmentDirections.actionHomeFragmentToEditFragment(expense)
                            Navigation.findNavController(view).navigate(action)
                            true
                        }

                        R.id.action_delete -> {


                            val alert = AlertDialog.Builder(binding.root.context)
                            alert.setTitle("Warning")
                            alert.setMessage("Are you sure you want to delete?")

                            alert.setPositiveButton("Yes"
                            ) { p0, p1 ->

                                if(!expense.id.isNullOrEmpty()){
                                    val db = FirebaseFirestore.getInstance()


                                    db.collection("expenses").document(expense.id)
                                        .delete()
                                        .addOnSuccessListener {
                                            // Silme başarılı olduğunda yapılacak işlemler
                                            Toast.makeText(itemView.context, "Expense deleted", Toast.LENGTH_SHORT).show()
                                            adapter.expenseList.remove(expense)
                                            adapter.notifyItemRemoved(adapterPosition)
                                            adapter.notifyItemRangeChanged(adapterPosition, adapter.expenseList.size)
                                            Log.d(TAG, "sUCCESS")
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(
                                                itemView.context,
                                                "Error deleting expense: ${e.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }


                                }


                               /* Toast.makeText(binding.root.context, "Deleted!", Toast.LENGTH_LONG)
                                    .show()*/
                            }

                            alert.setNegativeButton("No",){ p0, p1 ->
                                Toast.makeText(binding.root.context,"Not Deleted!",Toast.LENGTH_LONG).show()
                            }
                            alert.show()

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
        /*holder.binding.tvExpenseName.text = expenseList[position].toString()
        holder.bind(expense)*/
        val expense = expenseList[position]
        holder.bind(expense,this)
        /*
        holder.binding.tvExpenseName.text = expense.nameOfExpense
        holder.binding.tvExpenseAmount.text = expense.amountOfExpense.toString()*/

    }



    override fun getItemCount(): Int {
        return expenseList.size
    }

}
*/




