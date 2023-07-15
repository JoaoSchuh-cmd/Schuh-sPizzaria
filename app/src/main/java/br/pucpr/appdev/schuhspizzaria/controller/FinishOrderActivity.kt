package br.pucpr.appdev.schuhspizzaria.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.pucpr.appdev.schuhspizzaria.dao.OrderDao
import br.pucpr.appdev.schuhspizzaria.dao.PizzaDao
import br.pucpr.appdev.schuhspizzaria.databinding.ActivityFinishOrderBinding
import br.pucpr.appdev.schuhspizzaria.datastore.OrderDataStore
import br.pucpr.appdev.schuhspizzaria.model.Order
import br.pucpr.appdev.schuhspizzaria.shared.Functions
import br.pucpr.appdev.schuhspizzaria.view.PizzaAdapter
import java.text.SimpleDateFormat
import java.util.Date

class FinishOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinishOrderBinding
    private lateinit var adapter : PizzaAdapter

    private val addPizzaForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
        binding = ActivityFinishOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureBtAddPizza()
        configureBtFinishOrder()
        configureBtCancel()
    }

    fun finishOrderBuilding() {
        buildAndSaveOrder()
        persistPizzas()
    }

    fun buildAndSaveOrder() {
        val date = SimpleDateFormat("dd/mm/yyyy").format(Date())

        val order = Order(0.00, 1, date)

        OrderDao.getInstance(applicationContext).add(order)
    }

    fun persistPizzas() {
        val pizzas = OrderDataStore.getOrderPizzas()

        for (pizza in pizzas) {
            PizzaDao.getInstance(applicationContext).add(pizza)
        }
    }

    private fun updatePizzaList() {
        LinearLayoutManager(this).apply {
            this.orientation = LinearLayoutManager.VERTICAL
            binding.rvOrderPizzasList.layoutManager = this

            adapter = PizzaAdapter(PizzaDao.getInstance(applicationContext).getAll()).apply {
                binding.rvOrderPizzasList.adapter = this
            }
        }
    }

    private fun configureBtAddPizza() {
        binding.btAddPizza.setOnClickListener {
            Intent(this, OrderActivity::class.java).run {
                addPizzaForResult.launch(this)
            }
        }
    }

    private fun configureBtFinishOrder() {
        binding.btFinishOrder.setOnClickListener {
            finishOrderBuilding()
            Intent().run {
                setResult(RESULT_OK)
            }
            finish()
        }
    }

    private fun configureBtCancel() {
        binding.btCancel.setOnClickListener {
            Functions.showConfirmationDialog(applicationContext, "Tem certeza de que deseja cancelar o pedido? \n Você voltará para a página inicial!")
                .thenAccept { result ->
                    if (result) {
                        OrderDataStore.clearOrder()
                        finish()
                    }
                }
        }
    }
}