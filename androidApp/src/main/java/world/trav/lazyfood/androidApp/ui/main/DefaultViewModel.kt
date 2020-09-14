package world.trav.lazyfood.androidApp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import world.trav.lazyfood.androidApp.R
import world.trav.lazyfood.androidApp.utils.AppExecutors
import world.trav.lazyfood.shared.Food

//
// Created by  on 13/9/20.
//
class DefaultViewModel : ViewModel(){

    private var foods = MutableLiveData<List<Food>>()

    fun getFoods(): LiveData<List<Food>>{
        return foods
    }

    fun loadFoods(){
        AppExecutors.instance.diskIO().execute {

            Thread.sleep(500)

            val foodList = arrayListOf(
                Food(R.drawable.food1),
                Food(R.drawable.food2),
                Food(R.drawable.food3),
                Food(R.drawable.food4),
                Food(R.drawable.food5),
            )

            AppExecutors.instance.mainThread().execute{
                foods.postValue(foodList)
            }
        }
    }
}
