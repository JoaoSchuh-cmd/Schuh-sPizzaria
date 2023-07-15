package br.pucpr.appdev.schuhspizzaria.controller.calculators

object PriceCalculator {
    fun getPizzaPrice() : Double {
        return PizzaPriceCalculator.price
    }

    fun getOrderPrice() : Double {
        return OrderPriceCalculator.price
    }
}