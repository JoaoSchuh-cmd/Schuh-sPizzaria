package br.pucpr.appdev.schuhspizzaria.controller

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import br.pucpr.appdev.schuhspizzaria.databinding.ActivityOrderBinding
import br.pucpr.appdev.schuhspizzaria.model.Pizza
import br.pucpr.appdev.schuhspizzaria.builder.flavorbuilder.ChickenFlavor
import br.pucpr.appdev.schuhspizzaria.builder.flavorbuilder.ChickenWithCatupiryFlavor
import br.pucpr.appdev.schuhspizzaria.model.Flavor
import br.pucpr.appdev.schuhspizzaria.builder.flavorbuilder.PepperoniFlavor
import br.pucpr.appdev.schuhspizzaria.builder.flavorbuilder.PortugueseFlavor
import br.pucpr.appdev.schuhspizzaria.builder.flavorbuilder.StrogonoffFlavor
import br.pucpr.appdev.schuhspizzaria.controller.calculators.OrderPriceCalculator
import br.pucpr.appdev.schuhspizzaria.controller.calculators.PizzaPriceCalculator
import br.pucpr.appdev.schuhspizzaria.controller.calculators.PriceCalculator
import br.pucpr.appdev.schuhspizzaria.dao.OrderDao
import br.pucpr.appdev.schuhspizzaria.dao.PizzaDao
import br.pucpr.appdev.schuhspizzaria.datastore.OrderDataStore
import br.pucpr.appdev.schuhspizzaria.model.Order

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureBtSave()
        configureBtCancel()
        loadDataFromOuter()
    }

    fun configureBtSave() {
        binding.btSave.setOnClickListener {
            addOrEditPizzaToOrder()

            Intent(this, FinishOrderActivity::class.java).run {
                setResult(RESULT_OK)
                startActivity(this)
            }

            finish()
        }
    }

    fun configureBtCancel() {
        binding.btCancel.setOnClickListener {
            if (!OrderDataStore.isEmpty())
                Intent(this, FinishOrderActivity::class.java).run {
                    setResult(RESULT_CANCELED)
                    startActivity(this)
                }
            finish()
        }
    }

    fun addOrEditPizzaToOrder() {
        if (intent.getIntExtra("editingPizza", -1) == -1)
            OrderDataStore.addPizza(buildPizza())
        else
            OrderDataStore.updatePizza(buildPizza(), intent.getIntExtra("pizzaPos", -1))

        PriceCalculator.clearPizzaPrice()
    }

    fun buildPizza() : Pizza {
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

        OrderPriceCalculator.incOrderPrice(price)

        return Pizza(size, flavors, withEdge, price,getLastOrderId() + 1)
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
        return OrderDao.getInstance(this).getLastId()
    }

    private fun loadDataFromOuter() {
        val pizzaId = intent.getLongExtra("pizzaId", -1)
        val pizzaPos = intent.getIntExtra("pizzaPos", -1)

        var pizza = Pizza()

        if (pizzaId.toInt() != -1)
            pizza = PizzaDao.getInstance(this).getById(pizzaId)
        else if (pizzaPos != -1)
            pizza = OrderDataStore.getOrderPizzaByPosition(pizzaPos)

        if (pizza.flavors.isNotEmpty()) {
            loadSize(pizza.size)
            loadWithEdge(pizza.withEdge)
            loadFlavors(pizza.flavors)
        }
    }

    private fun loadSize(size: String) {
        var id = 0
        when (size) {
            "PEQUENA" -> id = binding.rbSmallSize.id
            "MÉDIA" -> id = binding.rbMediumSize.id
            "GRANDE" -> id = binding.rbLargeSize.id
        }
        binding.rgPizzaSize.check(id)
    }

    private fun loadWithEdge(withEdge: Boolean) {
        val id = if (withEdge) binding.rbWithEdge.id else binding.rbNoEdge.id
        binding.rgPizzaBorder.check(id)
    }

    private fun loadFlavors(flavors: String) {
        val flavorsList = flavors.split("|")

        if (flavorsList.isNotEmpty())
            for (flavor in flavorsList)
                checkFlavor(flavor)
        else
            checkFlavor(flavors)
    }

    private fun checkFlavor(flavor : String) {
        when (flavor) {
            "Frango" -> binding.cbChicken.isChecked = true
            "Frango com Catupiri" -> binding.cbChickenWithCatupiry.isChecked = true
            "Calabresa" -> binding.cbPepperoni.isChecked = true
            "Portuguesa" -> binding.cbPortuguese.isChecked = true
            "Strononoff" -> binding.cbStrogonoff.isChecked = true
        }
    }
}