package com.guilhermecallandprojects.notesapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.util.Log
import android.widget.Toast
import java.nio.ByteOrder

class DbManager(context: Context) {

    val dbName = "notesDB_"
    val dbTable = "notes"
    val colID = "id"
    val colTitle = "title"
    val colDes = "description"
    val dbVersion = 1

    var sqlDB : SQLiteDatabase ?= null

    init {
        val db = DatabaseHelperNotes(context)
        sqlDB = db.writableDatabase
    }

    inner class DatabaseHelperNotes
        (val context: Context) : SQLiteOpenHelper(context, dbName, null, dbVersion) {
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(
                "CREATE TABLE IF NOT EXISTS $dbTable ($colID INTEGER PRIMARY KEY AUTOINCREMENT, $colTitle TEXT, $colDes TEXT)"
            )
            Toast.makeText(this.context, "database created", Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
            db!!.execSQL("Drop table IF EXISTS $dbTable")
        }

    }

    fun Insert(values: ContentValues): Long{
        val id = sqlDB!!.insert(dbTable,"",values)
        return id
    }

    fun Query(projection: Array<String>, selection: String, selectionArgs: Array<String>, sortOrder: String) : Cursor {
        val qb = SQLiteQueryBuilder()
        qb.tables = dbTable
        val cursor = qb.query(sqlDB, projection, selection, selectionArgs, null, null, sortOrder)
        return cursor
    }

    fun Delete(selection:String, selectionArgs: Array<String>) : Int {
        val count = sqlDB!!.delete(dbTable, selection, selectionArgs)
        return count
    }
}