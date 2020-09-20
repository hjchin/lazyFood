package world.trav.lazyfood.shared

//
// Created by  on 12/9/20.
//

class Foods(private val list: List<Food>){

    private val delta: Double = 1/(list.size * 1.0)
    companion object{
        const val GROUP_SIZE = 3
    }

    init{
        if(list.size < GROUP_SIZE){
            throw Exception("minimum food list size is 3")
        }
    }

    fun nextStopIndex(index: Int):Int{
        var ci = index
        var ri = ci
        var maxWeight =  list[ci].weight

        Log.d("list size: ${list.size}, group size:${GROUP_SIZE},  start index: $index")
        Log.d("list[$index].weight: ${list[index].weight}")
        for(i in 1 until GROUP_SIZE){
            ci = if((ci+1)> list.size-1) ci+1-list.size else ci+1
            Log.d("list[$ci].weight: ${list[ci].weight}")
            if(list[ci].weight > maxWeight){
                maxWeight = list[ci].weight
                ri = ci
            }
        }

        Log.d("stop index: $ri")
        return ri
    }

    fun voteUp(index: Int){
        list[index].weight += delta
    }

    fun voteDown(index: Int){
        list[index].weight -= delta
    }

    fun get(index: Int): Food {
        return list[index]
    }
}