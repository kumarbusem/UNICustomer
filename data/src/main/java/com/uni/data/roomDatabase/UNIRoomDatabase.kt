

package com.uni.data.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(RecentAddress::class), version = 1, exportSchema = false)
public abstract class UNIRoomDatabase : RoomDatabase() {

    abstract fun recentAddressDao(): RecentAddressDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: UNIRoomDatabase? = null

        fun getDatabase(context: Context): UNIRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        UNIRoomDatabase::class.java,
                        "uni_room_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
