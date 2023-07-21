package br.pucpr.appdev.schuhspizzaria.controller.calculators

object PriceCalculator {

    // ******** PIZZA FUNCTIONS **********
    fun getPizzaPrice() : Double {
        return PizzaPriceCalculator.price
    }

    fun clearPizzaPrice() {
        PizzaPriceCalculator.price = 0.00
    }

    fun calculatePizzaByFlavors(flavorsCount : Int) {
        PizzaPriceCalculator.calculateByFlavors(flavorsCount)
    }

    fun calculatePizzaByEdge(withEdge : Boolean) {
        PizzaPriceCalculator.calculateByEdge(withEdge)
    }

    fun calculatePizzaBySize(size : String) {
        PizzaPriceCalculator.calculateBySize(size)
    }

    // ******** ORDER FUNCTIONS **********
    fun getOrderPrice() : Double {
        return OrderPriceCalculator.price
    }

    fun clearOrderPrice() {
        OrderPriceCalculator.price = 0.00
    }

    fun incOrderPrice(value : Double) {
        OrderPriceCalculator.incOrderPrice(value)
    }

    fun decOrderPrice(value : Double) {
        OrderPriceCalculator.decOrderPrice(value)
    }

    // ********* CLEAR PRICES **********
    fun clearPrices() {
        clearOrderPrice()
        clearPizzaPrice()
    }
}