package br.pucpr.appdev.schuhspizzaria.builder.flavorbuilder

class StrogonoffFlavor : FlavorInterface {
    val description : String = "Stroganoff"

    override fun getFlavor() : String {
        return description
    }
}