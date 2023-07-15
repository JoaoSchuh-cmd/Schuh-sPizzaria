package br.pucpr.appdev.schuhspizzaria.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.appdev.schuhspizzaria.databinding.ItemPizzasBinding
import br.pucpr.appdev.schuhspizzaria.model.Pizza

class PizzaAdapter(var pizzas: MutableList<Pizza>) : RecyclerView.Adapter<PizzaAdapter.PizzaHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PizzaHolder {
        ItemPizzasBinding.inflate(
            LayoutInflater.from(parent.context), parent, false).apply {
            return PizzaHolder(this)
        }
    }

    override fun onBindViewHolder(holder: PizzaHolder, position: Int) {
        pizzas[position].apply {
            holder.binding.tvSize.text = this.size
            holder.binding.tvFlavors.text = this.flavors
            holder.binding.tvEdge.text = if (this.withEdge) "Sim" else "Não"
            holder.binding.tvTotalPizzaPrice.text = this.price.toString()
        }
    }

    override fun getItemCount() = pizzas.size

    inner class PizzaHolder(var binding: ItemPizzasBinding) : RecyclerView.ViewHolder(binding.root)
}