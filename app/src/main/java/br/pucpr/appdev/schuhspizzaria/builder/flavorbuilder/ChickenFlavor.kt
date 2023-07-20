package br.pucpr.appdev.schuhspizzaria.builder.flavorbuilder

class ChickenFlavor : FlavorInterface {
    val description : String = "Frango"

    override fun getFlavor(): String {
        return description
    }
}