package br.pucpr.appdev.schuhspizzaria.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.appdev.schuhspizzaria.dao.OrderDao
import br.pucpr.appdev.schuhspizzaria.databinding.ActivityHomeBinding
import br.pucpr.appdev.schuhspizzaria.datastore.OrderDataStore
import br.pucpr.appdev.schuhspizzaria.view.OrderAdapter

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private lateinit var adapter : OrderAdapter
    private lateinit var gesture: GestureDetector

    private val addOrderForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadRecycleView()
        configureGesture()
        configureBtAddOrder()
        configureBtFinishedOrders()
        configureRecyclerViewEvents()
    }

    private fun configureBtAddOrder() {
        binding.btAddOrder.setOnClickListener {
            Intent(this, OrderActivity::class.java).run {
                addOrderForResult.launch(this)
            }
        }
    }

    private fun configureBtFinishedOrders() {
        binding.btFinishedOrders.setOnClickListener {
            Intent(this, CompletedOrdersActivity::class.java).run {
                startActivity(this)
            }
            finish()
        }
    }

    private fun loadRecycleView() {
        LinearLayoutManager(this).apply {
            this.orientation = LinearLayoutManager.VERTICAL
            binding.rvOrders.layoutManager = this
            adapter = OrderAdapter(OrderDao.getInstance(this@HomeActivity).getAllInPreparationOrders()).apply {
                binding.rvOrders.adapter = this
            }
        }
    }

    private fun configureGesture() {

        gesture = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {

            override fun onLongPress(e: MotionEvent) {
                super.onLongPress(e)

                binding.rvOrders.findChildViewUnder(e.x, e.y)?.let { child ->
                    val position = binding.rvOrders.getChildAdapterPosition(child)
                    val order = adapter.orders[position]

                    AlertDialog.Builder(this@HomeActivity).apply {
                        setMessage("Tem certeza que deseja excluir o pedido ${order.id}?")
                        setPositiveButton("Excluir") { _, _ ->
                            OrderDao.getInstance(this@HomeActivity).remove(order)
                            Toast.makeText(this@HomeActivity, "Ordem ${order.id}# removida com sucesso!!!", Toast.LENGTH_LONG).show()
                            adapter.notifyDataSetChanged()
                            loadRecycleView()
                        }
                        setNegativeButton("Cancelar", null)
                        show()
                    }
                }
            }
        })
    }

    private fun configureRecyclerViewEvents() {
        binding.rvOrders.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                rv.findChildViewUnder(e.x, e.y).apply {
                    return (this != null && gesture.onTouchEvent(e))
                }
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

}