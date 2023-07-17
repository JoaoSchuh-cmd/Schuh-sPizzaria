package br.pucpr.appdev.schuhspizzaria.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.pucpr.appdev.schuhspizzaria.R
import br.pucpr.appdev.schuhspizzaria.dao.OrderDao
import br.pucpr.appdev.schuhspizzaria.databinding.ActivityCompletedOrdersBinding
import br.pucpr.appdev.schuhspizzaria.datastore.OrderDataStore
import br.pucpr.appdev.schuhspizzaria.view.CompletedOrderAdapter
import br.pucpr.appdev.schuhspizzaria.view.PizzaAdapter

class CompletedOrdersActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCompletedOrdersBinding
    private lateinit var adapter : CompletedOrderAdapter

    private val addOrderForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let {
                    intent ->
                Toast.makeText(this, "Pizza adicionada com sucesso!!!", Toast.LENGTH_LONG).show()
                adapter.notifyDataSetChanged()
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompletedOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadRecycleView()
        configureBtBack()
    }

    private fun configureBtBack() {
        binding.btBack.setOnClickListener {
            finish()
        }
    }

    private fun loadRecycleView() {
        LinearLayoutManager(this).apply {
            this.orientation = LinearLayoutManager.VERTICAL
            binding.rvCompletedOrders.layoutManager = this
            adapter = CompletedOrderAdapter(OrderDao.getInstance(this@CompletedOrdersActivity).getAllCompletedOrders()).apply {
                binding.rvCompletedOrders.adapter = this
            }
        }
    }
}