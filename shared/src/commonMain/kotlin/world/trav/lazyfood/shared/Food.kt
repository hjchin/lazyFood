package world.trav.lazyfood.shared

//
// Created by  on 12/9/20.
//

class Food{
    var weight: Double = 0.0
    var imagePath: String = ""
    var id: Long = 0
    var resourceId: Int? = null

    val isDefault: Boolean
        get() = resourceId != null

    companion object{
        fun newInstance(resourceId: Int): Food{
            return Food().also {
                it.resourceId = resourceId
            }
        }
    }
}