package br.pucpr.appdev.schuhspizzaria.builder.flavorbuilder

class PepperoniFlavor : FlavorInterface {
    val description : String = "Calabresa"

    override fun getFlavor() : String {
        return description
    }
}