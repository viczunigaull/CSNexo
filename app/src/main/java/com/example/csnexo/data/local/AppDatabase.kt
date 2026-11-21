package com.example.csnexo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.csnexo.data.local.dao.OportunidadDao
import com.example.csnexo.data.local.entity.OportunidadEntity

@Database(
    entities = [OportunidadEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun oportunidadDao(): OportunidadDao

    companion object {
        private const val NOMBRE_BASE = "csnexo.db"

        fun crear(context: Context): AppDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                NOMBRE_BASE
            ).build()
    }
}
