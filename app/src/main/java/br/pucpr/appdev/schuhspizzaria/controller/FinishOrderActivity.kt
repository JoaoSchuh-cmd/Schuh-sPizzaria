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
            //val finishOrderIntent = Intent(this, FinishOrderActivity::class.java)
            //addPizzaForResult.launch(finishOrderIntent)
            finish()
        }
    }

    private fun configureBtFinishOrder() {
        binding.btFinishOrder.setOnClickListener {
            finishOrderBuilding()
            Intent(this, HomeActivity::class.java).run {
                setResult(RESULT_OK, this)
            }
            finish()
        }
    }

    private fun configureBtCancel() {
        PriceCalculator.clearPrices()

        binding.btCancel.setOnClickListener {
            Functions.showConfirmationDialog(this, "Tem certeza de que deseja cancelar o pedido? \n Você voltará para a página inicial!")
                .thenAccept { result ->
                    if (result) {
                        OrderDataStore.clearOrder()

                        setResult(RESULT_CANCELED)
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
                //for (pizza in pizzas) {
                //    if pizza
                //}
            }

            adapter = PizzaAdapter(pizzas).apply {
                binding.rvOrderPizzasList.adapter = this
            }
        }
    }
}