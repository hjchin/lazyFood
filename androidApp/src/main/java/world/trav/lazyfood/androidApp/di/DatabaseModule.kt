package world.trav.lazyfood.androidApp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import world.trav.lazyfood.shared.cache.DatabaseDriverFactory

//
// Created by  on 19/9/20.
//

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Provides
    fun provideDatabaseDriver(@ApplicationContext context: Context): DatabaseDriverFactory{
        return DatabaseDriverFactory(context)
    }
}