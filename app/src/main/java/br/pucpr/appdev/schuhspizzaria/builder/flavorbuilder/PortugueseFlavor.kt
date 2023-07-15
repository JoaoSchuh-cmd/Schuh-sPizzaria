package br.pucpr.appdev.schuhspizzaria.builder.flavorbuilder

class PortugueseFlavor : FlavorInterface {
    val description : String = "Dos Colonizadores"

    override fun getFlavor() : String {
        return description
    }
}