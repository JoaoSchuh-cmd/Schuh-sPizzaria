package br.pucpr.appdev.schuhspizzaria.controller.calculators

object OrderPriceCalculator {
    var price = 0.00

    fun incOrderPrice(value : Double) {
        price += value
    }

    fun decOrderPrice(value : Double) {
        price -= value
    }
}