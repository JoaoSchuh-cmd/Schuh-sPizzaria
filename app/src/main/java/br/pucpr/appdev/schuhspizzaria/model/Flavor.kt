package br.pucpr.appdev.schuhspizzaria.model

class Flavor private constructor(
    val listOfFlavor : MutableList<String>
){

    data class Builder(
        val listOfFlavor: MutableList<String> = arrayListOf()
    ){
      fun addFlavor(flavor : String) = apply { this.listOfFlavor.add(flavor) }
      fun build() = Flavor(listOfFlavor).toString()
    }

    override fun toString(): String {
        return listOfFlavor.joinToString("|")
    }

}