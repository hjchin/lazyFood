package world.trav.lazyfood.shared

import world.trav.lazyfood.shared.cache.Database
import world.trav.lazyfood.shared.cache.DatabaseDriverFactory
import world.trav.lazyfood.shared.entity.RocketLaunch

//
// Created by HJ Chin on 19/9/20.
//

class FoodRepository(databaseDriverFactory: DatabaseDriverFactory){
    private val database = Database(databaseDriverFactory)

    @Throws(Exception::class) suspend fun getFoods(): List<Food> {
        return database.selectAllFood()
    }
}