package world.trav.lazyfood.androidApp.ui.main

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.FileUtils
import android.provider.OpenableColumns
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import timber.log.Timber
import world.trav.lazyfood.androidApp.R
import world.trav.lazyfood.shared.Food
import world.trav.lazyfood.shared.FoodRepository
import world.trav.lazyfood.shared.Foods
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

//
// Created by  on 13/9/20.
//

class DefaultViewModel @ViewModelInject constructor(var foodRepository: FoodRepository) : ViewModel() {

    private var foodList = MutableLiveData<ArrayList<Food>>()
    private var lastInsertedFood = MutableLiveData<Food>()
    private var lastRemovedFood = MutableLiveData<Food>()
    private lateinit var foods: Foods

    init{
        loadFoods()
    }

    fun voteFoodDown(index: Int){
        foods.voteDown(index)
    }

    fun voteFoodUp(index: Int){
        foods.voteUp(index)
    }

    fun getFoodWeight(index: Int): Double{
        return foods.get(index).weight
    }

    fun getNextStopIndex(currentIndex: Int): Int{
        return foods.nextStopIndex(currentIndex)
    }

    fun getFoods(): LiveData<ArrayList<Food>> {
        return foodList
    }

    fun getLastAddedFood(): LiveData<Food>{
        return lastInsertedFood
    }

    fun getLastRemovedFood(): LiveData<Food>{
        return lastRemovedFood
    }

    private fun loadFoods() {
        viewModelScope.launch {
            var foodList = foodRepository.getFoods().let {
                val rs = ArrayList(it)
                if(rs.size < 3){
                    rs.addAll(
                        arrayListOf(
                            Food.newInstance(R.drawable.food1),
                            Food.newInstance(R.drawable.food2),
                            Food.newInstance(R.drawable.food4)
                        )
                    )
                }
                rs
            }

            foods = Foods(foodList)
            this@DefaultViewModel.foodList.postValue(foodList)
        }
    }

    fun addFoodByUri(context: Context, uri: Uri){
        viewModelScope.launch {
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r", null)
            parcelFileDescriptor?.let {
                val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                val file = File(context.cacheDir, context.contentResolver.getFileName(uri))
                val outputStream = FileOutputStream(file)
                FileUtils.copy(inputStream, outputStream)
                val food = Food.newInstance(file.path)
                val id = foodRepository.insertFood(food)
                food.id = id
                Timber.d("food: id-${food.id}, image-${food.imagePath}")
                lastInsertedFood.postValue(food)
            }
        }
    }

    fun deleteFood(food: Food){
        viewModelScope.launch {
            foodList.value?.let{
                foodRepository.deleteFood(food)
                it.remove(food)
                lastRemovedFood.postValue(food)
            }
        }
    }

    private fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = ""
        val returnCursor = this.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }

        return name
    }

    fun clearLastAddedFood() {
        lastInsertedFood.value = null
    }

    fun clearLastRemovedFood(){
        lastRemovedFood.value = null
    }
}
