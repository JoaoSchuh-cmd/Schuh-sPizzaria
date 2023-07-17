package br.pucpr.appdev.schuhspizzaria.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class SQLiteDataHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "SHUZINSPIZZARIA.db"
        private const val DATABASE_VERSION = 1
        private const val COLUMN_ID = "ID"

        private const val TABLE_PIZZAS = "PIZZA"
        private const val TABLE_ORDERS = "ORDER_TABLE"

        private const val createPizzaSQL = "CREATE TABLE IF NOT EXISTS $TABLE_PIZZAS(" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "SIZE VARCHAR(10) NOT NULL," +
                "FLAVORS VARCHAR(50) NOT NULL," +
                "EDGE BOOLEAN NOT NULL," +
                "PRICE DOUBLE NOT NULL," +
                "ORDER_ID INTEGER NOT NULL" +
                ")"

        private const val createOrderSQL = "CREATE TABLE IF NOT EXISTS $TABLE_ORDERS(" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "DATE VARCHAR(10)," +
                "PRICE DOUBLE," +
                "STATUS INTEGER" +
                ")"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val db = db ?: return

        db.beginTransaction()
        try {
            db.execSQL(createPizzaSQL)
            db.execSQL(createOrderSQL)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.d("ShuzinsPizzariaApp", e.localizedMessage)
        } finally {
            db.endTransaction()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val db = db ?: return

        db.beginTransaction()
        try {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_PIZZAS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDERS")
            onCreate(db)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.d("SchuzinsPizzariaApp", e.localizedMessage)
        } finally {
            db.endTransaction()
        }
    }
}