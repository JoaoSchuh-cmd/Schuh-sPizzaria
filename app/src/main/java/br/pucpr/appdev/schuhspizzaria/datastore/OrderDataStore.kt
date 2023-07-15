package br.pucpr.appdev.schuhspizzaria.datastore

import br.pucpr.appdev.schuhspizzaria.model.Pizza

object OrderDataStore {
    private var pizzaList = mutableListOf<Pizza>()

    fun addPizza(pizza: Pizza) {
        pizzaList.add(pizza)
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
}