package br.pucpr.appdev.schuhspizzaria.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.pucpr.appdev.schuhspizzaria.dao.OrderDao
import br.pucpr.appdev.schuhspizzaria.databinding.ActivityHomeBinding
import br.pucpr.appdev.schuhspizzaria.view.OrderAdapter

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private lateinit var adapter : OrderAdapter

    private val addOrderForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let {
                    intent ->
                Toast.makeText(this, "Pedido adicionado com sucesso!!!", Toast.LENGTH_LONG).show()
                adapter.notifyDataSetChanged()
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateOrderList()
        configureBtAddOrder()
    }

    private fun updateOrderList() {
        LinearLayoutManager(this).apply {
            this.orientation = LinearLayoutManager.VERTICAL
            binding.rvOrders.layoutManager = this

            adapter = OrderAdapter(OrderDao.getInstance(applicationContext).getAll()).apply {
                binding.rvOrders.adapter = this
            }
        }
    }

    private fun configureBtAddOrder() {
        binding.btAddOrder.setOnClickListener {
            Intent(this, OrderActivity::class.java).run {
                addOrderForResult.launch(this)
            }
        }
    }

}