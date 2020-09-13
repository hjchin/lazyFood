package world.trav.lazyfood.androidApp.ui.main

//
// Created by  on 12/9/20.
//

class Foods(private val list: List<Food>){

    private val delta: Double = 1/(list.size * 1.0)

    init {

    }

    fun nextStop(currentPosition: Int):Int{
        //until here
        return 0;
    }

    fun voteUp(index: Int){
        list[index].weight += delta
    }

    fun voteDown(index: Int){
        list[index].weight -= delta
    }

    fun get(index: Int): Food{
        return list[index]
    }
}