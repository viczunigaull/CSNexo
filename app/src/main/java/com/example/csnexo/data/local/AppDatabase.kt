package com.example.csnexo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.csnexo.data.local.dao.ContactoDao
import com.example.csnexo.data.local.dao.EventoDao
import com.example.csnexo.data.local.dao.OportunidadDao
import com.example.csnexo.data.local.dao.OrganizacionDao
import com.example.csnexo.data.local.dao.SeguimientoDao
import com.example.csnexo.data.local.entity.ContactoEntity
import com.example.csnexo.data.local.entity.EventoEntity
import com.example.csnexo.data.local.entity.OportunidadEntity
import com.example.csnexo.data.local.entity.OrganizacionEntity
import com.example.csnexo.data.local.entity.SeguimientoEntity

@Database(
    entities = [
        OportunidadEntity::class,
        OrganizacionEntity::class,
        EventoEntity::class,
        ContactoEntity::class,
        SeguimientoEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun oportunidadDao(): OportunidadDao

    abstract fun organizacionDao(): OrganizacionDao

    abstract fun eventoDao(): EventoDao

    abstract fun contactoDao(): ContactoDao

    abstract fun seguimientoDao(): SeguimientoDao

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
