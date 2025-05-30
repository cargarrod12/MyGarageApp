package com.application.carlosgarro.mygarageapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "clave") val clave: String
)