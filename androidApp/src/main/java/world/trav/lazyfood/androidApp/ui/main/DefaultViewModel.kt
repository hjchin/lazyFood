package world.trav.lazyfood.androidApp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import world.trav.lazyfood.androidApp.R
import world.trav.lazyfood.shared.Food
import world.trav.lazyfood.shared.FoodRepository

//
// Created by  on 13/9/20.
//
class DefaultViewModel : ViewModel() {

    private var foods = MutableLiveData<List<Food>>()

    fun getFoods(): LiveData<List<Food>> {
        return foods
    }

    fun loadFoods(foodRepository: FoodRepository) {

        viewModelScope.launch {
            Thread.sleep(500)
            var foodList = foodRepository.getFoods().let {
                if (it.isNotEmpty()) {
                    it
                }else{
                    arrayListOf(
                        Food.newInstance(R.drawable.food1),
                        Food.newInstance(R.drawable.food2),
                        Food.newInstance(R.drawable.food3)
                    )
                }
            }

            foods.postValue(foodList)
        }
    }
}
