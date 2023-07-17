package br.pucpr.appdev.schuhspizzaria.controller.calculators

object PizzaPriceCalculator {
    const val SMALL_PRICE : Double = 20.00
    const val MEDIUM_PRICE : Double = 30.00
    const val LARGE_PRICE : Double = 40.00

    var price = 0.00

    fun calculateByFlavors(flavorsCount : Int) {
        price += (5.00 * flavorsCount)
    }

    fun calculateByEdge(withEdge : Boolean) {
        if (withEdge)
            price += 5.00
    }

    fun calculateBySize(size : String) {
        when (size) {
            "PEQUENA" -> {
                price += SMALL_PRICE
            }
            "MÉDIA" -> {
                price += MEDIUM_PRICE
            }
            "GRANDE" -> {
                price += LARGE_PRICE
            }
        }
    }
}