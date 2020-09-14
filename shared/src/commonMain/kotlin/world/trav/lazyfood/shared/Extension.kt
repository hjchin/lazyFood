package world.trav.lazyfood.shared

//
// Created by  on 13/9/20.
//

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round((this * multiplier).toInt()) / multiplier
}
