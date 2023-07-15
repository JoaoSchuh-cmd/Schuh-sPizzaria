package br.pucpr.appdev.schuhspizzaria.builder.flavorbuilder

class ChickenWithCatupiryFlavor : FlavorInterface {
    val description : String = "Flango com Catupily"

    override fun getFlavor() : String{
        return this.description;
    }
}