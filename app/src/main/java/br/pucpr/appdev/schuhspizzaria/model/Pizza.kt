package br.pucpr.appdev.schuhspizzaria.model

data class Pizza constructor(
    val size : String = "",
    val flavors : String = "",
    val withEdge : Boolean = false,
    val price : Double = 0.00,
    val orderId : Long = -1,
) {
    var id : Long = -1
}