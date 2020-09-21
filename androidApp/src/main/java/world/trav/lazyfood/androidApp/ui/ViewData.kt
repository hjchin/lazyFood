package world.trav.lazyfood.androidApp.ui

//
// Created by  on 21/9/20.
//

class ViewData<T>(public val data: T, public var state: ViewDataState){
    enum class ViewDataState {
        REFRESH, DELETED, ADDED, IDLE
    }
}
