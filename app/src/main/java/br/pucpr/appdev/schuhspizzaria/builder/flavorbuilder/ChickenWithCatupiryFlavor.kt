package br.pucpr.appdev.schuhspizzaria.builder.flavorbuilder

class ChickenWithCatupiryFlavor : FlavorInterface {
    val description : String = "Frango com Catupiri"

    override fun getFlavor() : String{
        return this.description;
    }
}