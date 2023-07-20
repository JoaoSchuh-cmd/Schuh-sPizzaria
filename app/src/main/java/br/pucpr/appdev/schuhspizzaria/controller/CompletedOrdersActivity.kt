package br.pucpr.appdev.schuhspizzaria.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.pucpr.appdev.schuhspizzaria.dao.OrderDao
import br.pucpr.appdev.schuhspizzaria.databinding.ActivityCompletedOrdersBinding
import br.pucpr.appdev.schuhspizzaria.view.CompletedOrderAdapter

class CompletedOrdersActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCompletedOrdersBinding
    private lateinit var adapter : CompletedOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompletedOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadRecycleView()
        configureBtBack()
    }


    // ************* SETTINGS ***************
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