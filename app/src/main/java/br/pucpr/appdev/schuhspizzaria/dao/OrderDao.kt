package br.pucpr.appdev.schuhspizzaria.dao

import android.content.ContentValues
import android.content.Context
import android.util.Log
import br.pucpr.appdev.schuhspizzaria.helper.SQLiteDataHelper
import br.pucpr.appdev.schuhspizzaria.model.Order
import java.lang.Exception

class OrderDao private constructor(
    private val context : Context
) : GenericDao<Order>{

    companion object {
        private var instance: OrderDao? = null

        fun getInstance(context: Context): OrderDao {
            if (instance == null) {
                synchronized(OrderDao::class.java) {
                    if (instance == null) {
                        instance = OrderDao(context)
                    }
                }
            }
            return instance!!
        }
    }

    val helper = SQLiteDataHelper(context)

    val FIELD_ID = "ID"
    val FIELD_PRICE = "PRICE"
    val FIELD_STATUS = "STATUS"
    val FIELD_DATE = "DATE"

    val columns = arrayOf(FIELD_ID, FIELD_PRICE, FIELD_STATUS, FIELD_DATE)

    val tableName = "ORDER_TABLE"

    override fun getAll(): MutableList<Order> {
        val db = helper.readableDatabase
        var orders = mutableListOf<Order>()

        val cursor = db.query(
            tableName,
            columns,
            null,
            null,
            null,
            null,
            FIELD_ID
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(FIELD_ID))
                val price = getDouble(getColumnIndexOrThrow(FIELD_PRICE))
                val status = getInt(getColumnIndexOrThrow(FIELD_STATUS))
                val date = getString(getColumnIndexOrThrow(FIELD_DATE))

                var order = Order(price, status, date)
                order.id = id
                orders.add(order)
            }
        }
        
        return orders
    }

    fun getAllInPreparationOrders() : MutableList<Order> {
        val db = helper.readableDatabase
        var orders = mutableListOf<Order>()

        val cursor = db.query(
            tableName,
            columns,
            "$FIELD_STATUS = ?",
            arrayOf("1"),
            null,
            null,
            FIELD_ID
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(FIELD_ID))
                val status = getInt(getColumnIndexOrThrow(FIELD_STATUS))
                val date = getString(getColumnIndexOrThrow(FIELD_DATE))
                val price = getDouble(getColumnIndexOrThrow(FIELD_PRICE))

                val order = Order(price, status, date)
                order.id = id

                orders.add(order)
            }
        }

        return orders
    }

    fun getAllCompletedOrders() : MutableList<Order> {
        val db = helper.readableDatabase
        var orders = mutableListOf<Order>()

        val cursor = db.query(
            tableName,
            columns,
            "$FIELD_STATUS = ?",
            arrayOf("0"),
            null,
            null,
            FIELD_ID
        )

        with(cursor) {
            if (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(FIELD_ID))
                val status = getInt(getColumnIndexOrThrow(FIELD_STATUS))
                val date = getString(getColumnIndexOrThrow(FIELD_DATE))
                val price = getDouble(getColumnIndexOrThrow(FIELD_PRICE))

                val order = Order(price, status, date)
                order.id = id

                orders.add(order)
            }
        }

        return orders
    }

    override fun getById(id: Long): Order {
        val db = helper.readableDatabase
        val order = Order()
        
        val cursor = db.query(
            tableName,
            columns,
            "${FIELD_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getColumnIndexOrThrow(FIELD_ID).toLong()
                val price = getColumnIndexOrThrow(FIELD_PRICE).toDouble()
                val status = getColumnIndexOrThrow(FIELD_STATUS)
                val date = getColumnIndexOrThrow(FIELD_DATE).toString()

                var order = Order(price, status, date)
                order.id = id
                
            }
        }
        
        return order
    }

    override fun remove(order: Order): Long {
        val db = helper.writableDatabase
        var count = 0

        db.beginTransaction()
        try {
            count = db.delete(
                tableName,
                "${FIELD_ID} = ?",
                arrayOf(order.id.toString())
            )
            db.setTransactionSuccessful()
        } catch (e : Exception) {
            Log.d("SchuzinsPizzaria:OrderDAO", e.localizedMessage)
        } finally {
            db.endTransaction()
        }
        
        return count.toLong()
    }

    override fun edit(order: Order): Long {
        val db = helper.writableDatabase
        var count = 0
        
        val values = ContentValues().apply {
            put(FIELD_PRICE, order.price)
            put(FIELD_STATUS, order.status)
            put(FIELD_DATE, order.date)
        }
        
        db.beginTransaction()
        try {
            count = db.update(
                tableName,
                values,
                "$FIELD_ID = ?",
                arrayOf(order.id.toString())
            )
            db.setTransactionSuccessful()
        } catch (e : Exception) {
            Log.d("SchuzinsPizzaria:OrderDAO", e.localizedMessage)
        } finally {
            db.beginTransaction()
        }
        
        return count.toLong()
    }

    override fun add(order: Order): Long {
        val db = helper.writableDatabase
        var count = 0L

        val values = ContentValues().apply {
            put(FIELD_PRICE, order.price)
            put(FIELD_DATE, order.date)
            put(FIELD_STATUS, order.status)
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

    fun getLastId() : Long {
        val db = helper.readableDatabase
        var id : Long = 0;

        val cursor = db.query(
            tableName,
            arrayOf(FIELD_ID),
            null,
            null,
            null,
            null,
           "${FIELD_ID} DESC"
        )

        if (cursor.moveToFirst())
            id = cursor.getLong(cursor.getColumnIndexOrThrow(FIELD_ID))

        return id
    }
}