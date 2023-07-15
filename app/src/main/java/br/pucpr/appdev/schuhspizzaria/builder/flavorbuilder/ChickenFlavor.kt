package br.pucpr.appdev.schuhspizzaria.builder.flavorbuilder

class ChickenFlavor : FlavorInterface {
    val description : String = "Flango"

    override fun getFlavor(): String {
        return description
    }
}