package br.pucpr.appdev.schuhspizzaria.dao

import android.content.ContentValues
import android.content.Context
import android.util.Log
import br.pucpr.appdev.schuhspizzaria.helper.SQLiteDataHelper
import br.pucpr.appdev.schuhspizzaria.model.Pizza
import java.lang.Exception

class PizzaDao private constructor(
    private val context : Context
) : GenericDao<Pizza> {

    companion object {
        private var instance: PizzaDao? = null

        fun getInstance(context: Context): PizzaDao {
            if (instance == null) {
                synchronized(PizzaDao::class.java) {
                    if (instance == null) {
                        instance = PizzaDao(context)
                    }
                }
            }
            return instance!!
        }
    }

    val helper = SQLiteDataHelper(context)

    val FIELD_ID = "ID"
    val FIELD_SIZE = "SIZE"
    val FIELD_FLAVORS = "FLAVORS"
    val FIELD_EDGE = "EDGE"
    val FIELD_PRICE = "PRICE"
    val FIELD_ORDER_ID = "ORDER_ID"

    val columns = arrayOf(FIELD_ID, FIELD_SIZE, FIELD_FLAVORS, FIELD_EDGE, FIELD_PRICE, FIELD_ORDER_ID)

    val tableName = "PIZZA"

    override fun getAll(): MutableList<Pizza> {
        val db = helper.readableDatabase
        var pizzas = mutableListOf<Pizza>()

        val cursor = db.query(
            tableName,
            columns,
            null,
            null,
            null,
            null,
            FIELD_ID)

        with(cursor) {
            while (moveToNext()) {
                val id = getColumnIndexOrThrow(FIELD_ID).toLong()
                val size = getColumnIndexOrThrow(FIELD_SIZE).toString()
                val flavors = getColumnIndexOrThrow(FIELD_FLAVORS).toString()
                val edge = getColumnIndexOrThrow(FIELD_EDGE)
                val price = getColumnIndexOrThrow(FIELD_PRICE).toDouble()
                val orderId = getColumnIndexOrThrow(FIELD_ORDER_ID).toLong()

                val withEdge = edge != 0

                val pizza = Pizza(size, flavors, withEdge, price, orderId)
                pizza.id = id
                pizzas.add(pizza)
            }
        }

        return pizzas
    }

    override fun getById(id: Long): Pizza {
        val db = helper.readableDatabase
        var pizza = Pizza()

        val cursor = db.query(
            tableName,
            columns,
            "$FIELD_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        with(cursor) {
            if (count > 0) {
                val id = getColumnIndexOrThrow(FIELD_ID).toLong()
                val size = getColumnIndexOrThrow(FIELD_SIZE).toString()
                val flavors = getColumnIndexOrThrow(FIELD_FLAVORS).toString()
                val edge = getColumnIndexOrThrow(FIELD_EDGE)
                val price = getColumnIndexOrThrow(FIELD_PRICE).toDouble()
                val orderId = getColumnIndexOrThrow(FIELD_ORDER_ID).toLong()

                val withEdge = edge != 0

                pizza = Pizza(size, flavors, withEdge, price, orderId)
                pizza.id = id
            }
        }

        return pizza
    }

    override fun remove(pizza : Pizza): Long {
        var count = 0
        val db = helper.writableDatabase

        db.beginTransaction()
        try {
            count = db.delete(
                tableName,
                "$FIELD_ID = ?",
                arrayOf(pizza.id.toString()))
            db.setTransactionSuccessful()
        } catch (e : Exception) {
            Log.d("SchuzinsPizzaria:PizzaDAO", e.localizedMessage)
        } finally {
            db.beginTransaction()
        }

        return count.toLong()
    }

    override fun edit(pizza : Pizza): Long {
        val db = helper.writableDatabase
        var count = 0

        val values = ContentValues().apply {
            put(FIELD_SIZE, pizza.size)
            put(FIELD_FLAVORS, pizza.flavors)
            put(FIELD_EDGE, pizza.withEdge)
            put(FIELD_ORDER_ID, pizza.orderId)
            put(FIELD_PRICE, pizza.price)
        }

        db.beginTransaction()
        try {
            count = db.update(
                tableName,
                values,
                "$FIELD_ID = ?",
                arrayOf(pizza.id.toString())
            )
            db.setTransactionSuccessful()
        } catch (e : Exception) {
            Log.d("SchuzinsPizzaria:PizzaDAO", e.localizedMessage)
        } finally {
            db.beginTransaction()
        }

        return count.toLong()
    }

    override fun add(pizza: Pizza): Long {
        val db = helper.writableDatabase
        var count = 0L

        val values = ContentValues().apply {
            put(FIELD_SIZE, pizza.size)
            put(FIELD_FLAVORS, pizza.flavors)
            put(FIELD_EDGE, pizza.withEdge)
            put(FIELD_ORDER_ID, pizza.orderId)
            put(FIELD_PRICE, pizza.price)
        }

        db.beginTransaction()
        try {
            count = db.insert(
                tableName,
                null,
                values
            )
            db.setTransactionSuccessful()
        } catch (e : Exception) {
            Log.d("SchuzinsPizzaria|OrderDAO", e.localizedMessage)
        } finally {
            db.endTransaction()
        }

        return count
    }
}