package br.pucpr.appdev.schuhspizzaria.datastore

import br.pucpr.appdev.schuhspizzaria.model.Pizza

object OrderDataStore {
    private var pizzaList = mutableListOf<Pizza>()

    fun addPizza(pizza: Pizza) {
        pizzaList.add(pizza)
    }

    fun updatePizza(newPizza: Pizza, position: Int) {
        pizzaList[position] = newPizza
    }

    fun removePizza(pizza: Pizza) {
        pizzaList.remove(pizza)
    }

    fun clearOrder() {
        pizzaList.clear()
    }

    fun getOrderPizzas(): MutableList<Pizza> {
        return pizzaList
    }

    fun getOrderPizzaByPosition(position: Int) : Pizza {
        return pizzaList[position]
    }
    fun isEmpty() : Boolean {
        return pizzaList.isEmpty()
    }
}