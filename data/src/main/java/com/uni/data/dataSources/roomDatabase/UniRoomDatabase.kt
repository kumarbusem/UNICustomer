package com.uni.data.dataSources.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.uni.data.models.SavedAddress

@Database(entities = arrayOf(SavedAddress::class), version = 1, exportSchema = false)
public abstract class UniRoomDatabase : RoomDatabase() {

    abstract fun savedAddressDao(): SavedAddressDao

    companion object {

        private var instance: UniRoomDatabase? = null

        fun initialize(context: Context) {
            if (instance == null) {
                instance = Room.databaseBuilder(
                        context.applicationContext,
                        UniRoomDatabase::class.java,
                        "word_database"
                ).build()
            }
        }

        fun getDatabase(): UniRoomDatabase {
            checkNotNull(instance) { "SharedPreferences not initialized" }
            return instance!!
        }
    }
}


