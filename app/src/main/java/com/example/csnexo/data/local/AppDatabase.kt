package com.example.csnexo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.csnexo.data.local.dao.OportunidadDao
import com.example.csnexo.data.local.dao.OrganizacionDao
import com.example.csnexo.data.local.entity.OportunidadEntity
import com.example.csnexo.data.local.entity.OrganizacionEntity

@Database(
    entities = [OportunidadEntity::class, OrganizacionEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun oportunidadDao(): OportunidadDao

    abstract fun organizacionDao(): OrganizacionDao

    companion object {
        private const val NOMBRE_BASE = "csnexo.db"

        fun crear(context: Context): AppDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                NOMBRE_BASE
            )
                // Si cambia el esquema entre versiones, se recrea la base.
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
    }
}
