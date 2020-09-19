package world.trav.lazyfood.shared.cache

//
// Created by HJ Chin on 16/9/20.
//
import com.squareup.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
