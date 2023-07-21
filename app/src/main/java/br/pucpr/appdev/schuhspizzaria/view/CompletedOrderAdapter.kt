package br.pucpr.appdev.schuhspizzaria.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.appdev.schuhspizzaria.controller.FinishOrderActivity
import br.pucpr.appdev.schuhspizzaria.controller.OrderActivity
import br.pucpr.appdev.schuhspizzaria.dao.OrderDao
import br.pucpr.appdev.schuhspizzaria.databinding.ItemCompletedOrderBinding
import br.pucpr.appdev.schuhspizzaria.datastore.OrderDataStore
import br.pucpr.appdev.schuhspizzaria.model.Order

class CompletedOrderAdapter(var orders: MutableList<Order>) : RecyclerView.Adapter<CompletedOrderAdapter.CompletedOrderHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedOrderHolder {
        ItemCompletedOrderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false).apply {
            return CompletedOrderHolder(this)
        }
    }

    override fun onBindViewHolder(holder: CompletedOrderHolder, position: Int) {
        orders[position].apply {
            holder.binding.tvOrderId.text = this.id.toString() + "#"
            holder.binding.tvOrderDate.text = this.date
            holder.binding.tvOrderStatus.text = if (this.status == 0) "Concluído" else "Em preparação"
            holder.binding.tvTotalOrderPrice.text = this.price.toString()
        }

        configureBtOrderAgain(holder, position)
    }

    override fun getItemCount() = orders.size

    inner class CompletedOrderHolder(var binding: ItemCompletedOrderBinding) : RecyclerView.ViewHolder(binding.root)

    fun configureBtOrderAgain(holder: CompletedOrderHolder, position: Int) {
        val context = holder.itemView.context

        holder.binding.btOrderAgain.setOnClickListener {
            val intent = Intent(context, FinishOrderActivity::class.java).run {
                putExtra("orderId", orders[position].id)
            }
            (context as AppCompatActivity).startActivity(intent)
            context.finish()
        }
    }
}