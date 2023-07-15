package br.pucpr.appdev.schuhspizzaria.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import br.pucpr.appdev.schuhspizzaria.databinding.ActivityOrderBinding
import br.pucpr.appdev.schuhspizzaria.model.Pizza
import br.pucpr.appdev.schuhspizzaria.builder.flavorbuilder.ChickenFlavor
import br.pucpr.appdev.schuhspizzaria.builder.flavorbuilder.ChickenWithCatupiryFlavor
import br.pucpr.appdev.schuhspizzaria.model.Flavor
import br.pucpr.appdev.schuhspizzaria.builder.flavorbuilder.PepperoniFlavor
import br.pucpr.appdev.schuhspizzaria.builder.flavorbuilder.PortugueseFlavor
import br.pucpr.appdev.schuhspizzaria.builder.flavorbuilder.StrogonoffFlavor
import br.pucpr.appdev.schuhspizzaria.controller.calculators.PizzaPriceCalculator
import br.pucpr.appdev.schuhspizzaria.controller.calculators.PriceCalculator
import br.pucpr.appdev.schuhspizzaria.dao.OrderDao
import br.pucpr.appdev.schuhspizzaria.datastore.OrderDataStore
import br.pucpr.appdev.schuhspizzaria.view.OrderAdapter

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding
    private lateinit var adapter: OrderAdapter

    private val addPizzaForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let {
                    intent ->
                Toast.makeText(this, "Ordem adicionada com sucesso!!!", Toast.LENGTH_LONG).show()
                adapter.notifyDataSetChanged()
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureBtSave()
        configureBtCancel()
    }

    fun configureBtSave() {
        binding.btSave.setOnClickListener {
            addPizzaToOrder()

            Intent(this, OrderActivity::class.java).run {
                addPizzaForResult.launch(this)
            }
        }
    }

    fun configureBtCancel() {
        binding.btCancel.setOnClickListener {
            finish()
        }
    }

    fun addPizzaToOrder() {
        val size =
            when (binding.rgPizzaSize.checkedRadioButtonId) {
                binding.rbSmallSize.id -> "PEQUENA"
                binding.rbMediumSize.id -> "MÉDIA"
                binding.rbLargeSize.id -> "GRANDE"
                else -> ""
            }

        val withEdge =
            when (binding.rgPizzaBorder.checkedRadioButtonId) {
              binding.rbWithEdge.id -> true
              binding.rbNoEdge.id -> false
              else -> false
            }

        val flavors = flavorsBuild() // Aqui ja calcula o preço pelos sabores também

        PizzaPriceCalculator.calculateByEdge(withEdge)
        PizzaPriceCalculator.calculateBySize(size)

        val price = PriceCalculator.getPizzaPrice()

        OrderDataStore.addPizza(Pizza(size, flavors, withEdge, price,getLastOrderId() + 1))
    }

    fun flavorsBuild() : String {
        val flavorBuilder = Flavor.Builder()
        var count = 0

        if (binding.cbChicken.isChecked) {
            flavorBuilder.addFlavor(ChickenFlavor().getFlavor())
            count += 1
        }
        if (binding.cbChickenWithCatupiry.isChecked) {
            flavorBuilder.addFlavor(ChickenWithCatupiryFlavor().getFlavor())
            count += 1
        }
        if (binding.cbPepperoni.isChecked) {
            flavorBuilder.addFlavor(PepperoniFlavor().getFlavor())
            count += 1
        }
        if (binding.cbPortuguese.isChecked) {
            flavorBuilder.addFlavor(PortugueseFlavor().getFlavor())
            count += 1
        }
        if (binding.cbStrogonoff.isChecked) {
            flavorBuilder.addFlavor(StrogonoffFlavor().getFlavor())
            count += 1
        }

        PizzaPriceCalculator.calculateByFlavors(count)

        return flavorBuilder.build()
    }

    fun getLastOrderId() : Long {
        return OrderDao.getInstance(applicationContext).getLastId()
    }
}