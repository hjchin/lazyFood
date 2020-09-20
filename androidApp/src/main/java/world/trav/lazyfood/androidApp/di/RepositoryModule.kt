package world.trav.lazyfood.androidApp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import world.trav.lazyfood.shared.FoodRepository
import world.trav.lazyfood.shared.cache.DatabaseDriverFactory

//
// Created by  on 19/9/20.
//
@InstallIn(ApplicationComponent::class)
@Module
object RepositoryModule {

    @Provides
    fun provideFoodRepository(databaseDriverFactory: DatabaseDriverFactory): FoodRepository{
        return FoodRepository(databaseDriverFactory)
    }
}
