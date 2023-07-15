package br.pucpr.appdev.schuhspizzaria.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.appdev.schuhspizzaria.databinding.ItemOrderBinding
import br.pucpr.appdev.schuhspizzaria.model.Order

class OrderAdapter(var orders: MutableList<Order>) : RecyclerView.Adapter<OrderAdapter.OrderHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false).apply {
            return OrderHolder(this)
        }
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        orders[position].apply {
            holder.binding.tvOrderId.text = this.id.toString()
            holder.binding.tvOrderDate.text = this.date
            holder.binding.tvOrderStatus.text = if (this.status == 0) "Concluído" else "Em preparação"
            holder.binding.tvTotalOrderPrice.text = this.price.toString()
        }
    }

    override fun getItemCount() = orders.size

    inner class OrderHolder(var binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root)
}