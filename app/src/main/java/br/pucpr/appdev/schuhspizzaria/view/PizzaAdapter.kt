package br.pucpr.appdev.schuhspizzaria.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.appdev.schuhspizzaria.controller.OrderActivity
import br.pucpr.appdev.schuhspizzaria.dao.PizzaDao
import br.pucpr.appdev.schuhspizzaria.databinding.ItemPizzasBinding
import br.pucpr.appdev.schuhspizzaria.datastore.OrderDataStore
import br.pucpr.appdev.schuhspizzaria.model.Pizza

class PizzaAdapter(var pizzas: MutableList<Pizza>) : RecyclerView.Adapter<PizzaAdapter.PizzaHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PizzaHolder {
        ItemPizzasBinding.inflate(
            LayoutInflater.from(parent.context), parent, false).apply {
            return PizzaHolder(this)
        }
    }

    override fun onBindViewHolder(holder: PizzaHolder, position: Int) {
        val context = holder.itemView.context

        pizzas[position].apply {
            holder.binding.tvSize.text = this.size
            holder.binding.tvFlavors.text = this.flavors
            holder.binding.tvEdge.text = if (this.withEdge) "Sim" else "Não"
            holder.binding.tvTotalPizzaPrice.text = this.price.toString()
        }

        holder.binding.btEditPizza.setOnClickListener {
            val intent = Intent(context, OrderActivity::class.java).run {
                putExtra("pizzaId", pizzas[position].id)
                putExtra("pizzaPos", position)
                putExtra("editingPizza", 1)
            }
            (context as AppCompatActivity).startActivity(intent)
            context.finish()
        }

        //configureBtEditPizza(holder, position)
    }

    override fun getItemCount() = pizzas.size

    inner class PizzaHolder(var binding: ItemPizzasBinding) : RecyclerView.ViewHolder(binding.root)

    fun configureBtEditPizza(holder: PizzaHolder, position: Int) {
//        val context = holder.itemView.context
//
//        val pizzas = PizzaDao.getInstance(context).
//
//        val pizza = OrderDataStore.getOrderByPosition(position)
//
//        holder.binding.btEditPizza.setOnClickListener {
//            val intent = Intent(context, OrderActivity::class.java).run {
//                putExtra("order", order.id)
//            }
//            (context as AppCompatActivity).startActivity(intent)
//            context.finish()
//        }
    }
}