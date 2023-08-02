package br.pucpr.appdev.schuhspizzaria.model


data class Order (
    val price : Double = 0.00,
    val status : Int = -1,
    val date: String = "",
) {
    var id : Long = -1

}