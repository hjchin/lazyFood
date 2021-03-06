package world.trav.lazyfood.shared.cache

import com.jetbrains.handson.kmm.shared.cache.AppDatabase
import world.trav.lazyfood.shared.Food
import world.trav.lazyfood.shared.entity.Links
import world.trav.lazyfood.shared.entity.Rocket
import world.trav.lazyfood.shared.entity.RocketLaunch

//
// Created by HJ Chin on 17/9/20.
//

class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    fun selectAllFood(): List<Food>{
        return dbQuery.selectAllFood(::mapFood).executeAsList()
    }

    fun selectSortedFood(): List<Food>{
        return dbQuery.selectSortedFood(::mapFood).executeAsList()
    }

    fun insertFood(food: Food): Long{
        var id: Long = 0

        dbQuery.transaction {
            dbQuery.insertFood(food.imagePath, food.weight)
            id = dbQuery.selectLastInsertedFood().executeAsOne()
        }

        return id
    }

    fun updateFood(food: Food){
        dbQuery.updateFoodById(food.imagePath,food.weight, food.id)
    }

    fun deleteFood(food: Food){
        dbQuery.removeFoodById(food.id)
    }

    private fun mapFood(id: Long, imagePath: String, weight: Double): Food{
        return Food().also {
            it.id = id
            it.imagePath = imagePath
            it.weight = weight
        }
    }
}