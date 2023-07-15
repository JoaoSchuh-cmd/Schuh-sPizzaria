package br.pucpr.appdev.schuhspizzaria.builder.flavorbuilder

class StrogonoffFlavor : FlavorInterface {
    val description : String = "Estrágunoss"

    override fun getFlavor() : String {
        return description
    }
}