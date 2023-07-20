package br.pucpr.appdev.schuhspizzaria.builder.flavorbuilder

class PortugueseFlavor : FlavorInterface {
    val description : String = "Portuguesa"

    override fun getFlavor() : String {
        return description
    }
}