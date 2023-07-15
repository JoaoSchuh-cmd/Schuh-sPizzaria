package br.pucpr.appdev.schuhspizzaria.controller.calculators

object PizzaPriceCalculator {
    var price = 0.00

    fun calculateByFlavors(flavorsCount : Int) {
        price += (5.00 * flavorsCount)
    }

    fun calculateByEdge(withEdge : Boolean) {
        if (withEdge)
            price += 5.00
    }

    fun calculateBySize(size : String) {

    }
}