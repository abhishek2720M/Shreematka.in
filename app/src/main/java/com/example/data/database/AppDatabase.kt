package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.MatkaDao
import com.example.data.entity.BetEntity
import com.example.data.entity.MarketEntity
import com.example.data.entity.NotificationEntity
import com.example.data.entity.PromoCodeEntity
import com.example.data.entity.TransactionEntity
import com.example.data.entity.UserWalletEntity

@Database(
    entities = [
        UserWalletEntity::class,
        MarketEntity::class,
        BetEntity::class,
        TransactionEntity::class,
        PromoCodeEntity::class,
        NotificationEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun matkaDao(): MatkaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shree_matka_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
