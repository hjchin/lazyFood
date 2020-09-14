package world.trav.lazyfood.androidApp.ui.main

import timber.log.Timber

//
// Created by  on 12/9/20.
//

class Foods(private val list: List<Food>){

    private val delta: Double = 1/(list.size * 1.0)
    private val groupSize = 3

    init{
        if(list.size < groupSize){
            throw Exception("minimum food list size is 3")
        }
    }

    fun nextStopCount(index: Int):Int{
        var ci = index
        var step = 0
        var maxWeight =  list[ci].weight

        Timber.d("list size: ${list.size}, group size:${groupSize},  start index: $index")
        Timber.d("list[$index].weight: ${list[index].weight}")
        for(i in 1 until groupSize){
            ci = if((ci+1)> list.size-1) ci+1-list.size else ci+1
            Timber.d("list[$ci].weight: ${list[ci].weight}")
            if(list[ci].weight > maxWeight){
                maxWeight = list[ci].weight
                step = i
            }
        }

        Timber.d("increment step: $step")
        return step
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