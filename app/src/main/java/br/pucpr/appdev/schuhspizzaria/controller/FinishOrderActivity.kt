package br.pucpr.appdev.schuhspizzaria.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.pucpr.appdev.schuhspizzaria.controller.calculators.PriceCalculator
import br.pucpr.appdev.schuhspizzaria.dao.OrderDao
import br.pucpr.appdev.schuhspizzaria.dao.PizzaDao
import br.pucpr.appdev.schuhspizzaria.databinding.ActivityFinishOrderBinding
import br.pucpr.appdev.schuhspizzaria.datastore.OrderDataStore
import br.pucpr.appdev.schuhspizzaria.model.Order
import br.pucpr.appdev.schuhspizzaria.model.Pizza
import br.pucpr.appdev.schuhspizzaria.shared.Functions
import br.pucpr.appdev.schuhspizzaria.view.PizzaAdapter
import java.text.SimpleDateFormat
import java.util.Date

class FinishOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinishOrderBinding
    private lateinit var adapter : PizzaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadRecycleView()
        configureBtAddPizza()
        configureBtFinishOrder()
        configureBtCancel()
    }

    fun finishOrderBuilding() {
        buildAndSaveOrder()
        persistPizzas()
        OrderDataStore.clearOrder()
        PriceCalculator.clearPrices()
    }

    fun buildAndSaveOrder() {
        val date = SimpleDateFormat("dd/MM/yyyy").format(Date())

        val order = Order(PriceCalculator.getOrderPrice(), 1, date)

        OrderDao.getInstance(this).add(order)
    }

    fun persistPizzas() {
        val pizzas = OrderDataStore.getOrderPizzas()

        for (pizza in pizzas) {
            PizzaDao.getInstance(this).add(pizza)
        }
    }


    // ************** SETTINGS *************

    private fun configureBtAddPizza() {
        binding.btAddPizza.setOnClickListener {
            Intent(this, OrderActivity::class.java).run {
                startActivity(this)
            }
            finish()
        }
    }

    private fun configureBtFinishOrder() {
        binding.btFinishOrder.setOnClickListener {
            finishOrderBuilding()
            Intent(this, HomeActivity::class.java).run {
                setResult(RESULT_OK, this)
            }
            Toast.makeText(this, "Pedido adicionado com sucesso!!!", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun configureBtCancel() {
        binding.btCancel.setOnClickListener {
            Functions.showConfirmationDialog(this, "Tem certeza de que deseja cancelar o pedido? \n Você voltará para a página inicial!")
                .thenAccept { result ->
                    if (result) {
                        OrderDataStore.clearOrder()
                        setResult(RESULT_CANCELED)
                        Toast.makeText(this, "Pedido cancelado!!!", Toast.LENGTH_LONG).show()
                        PriceCalculator.clearPrices()
                        finish()
                    }
                }
        }
    }

    private fun loadRecycleView() {

        val orderId = intent.getLongExtra("orderId", -1)

        LinearLayoutManager(this).apply {
            this.orientation = LinearLayoutManager.VERTICAL
            binding.rvOrderPizzasList.layoutManager = this

            var pizzas : MutableList<Pizza>

            if (orderId.toInt() == -1)
                pizzas = OrderDataStore.getOrderPizzas()
            else {
                pizzas = PizzaDao.getInstance(this@FinishOrderActivity).getAllPizzaFromOrderId(orderId)
            }

            adapter = PizzaAdapter(pizzas).apply {
                binding.rvOrderPizzasList.adapter = this
            }
        }
    }
}