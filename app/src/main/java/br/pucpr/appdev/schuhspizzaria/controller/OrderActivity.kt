package br.pucpr.appdev.schuhspizzaria.controller

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
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
    private var maxFlavorCount : Int = 0
    private var flavorCount : Int = 0
    private var lastCheckedId : Int = -1

    private val flavorsCbOnClickListener = { checkBox : CheckBox ->
        if (checkBox.isChecked)
            flavorCount += 1
        else
            if (flavorCount > 0) flavorCount -= 1

        if (flavorCount > maxFlavorCount) {
            checkBox.isChecked = false
            flavorCount -= 1
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureRgPizzaSizeClick()
        configureFlavorsCbCheck()
        configureBtSave()
        configureBtCancel()
        loadDataFromOuter()
    }

    private fun configureFlavorsCbCheck() {
        binding.cbChicken.setOnClickListener { flavorsCbOnClickListener(it as CheckBox) }
        binding.cbPepperoni.setOnClickListener { flavorsCbOnClickListener(it as CheckBox) }
        binding.cbPortuguese.setOnClickListener { flavorsCbOnClickListener(it as CheckBox) }
        binding.cbStrogonoff.setOnClickListener { flavorsCbOnClickListener(it as CheckBox) }
        binding.cbChickenWithCatupiry.setOnClickListener { flavorsCbOnClickListener(it as CheckBox) }
    }

    fun configureRgPizzaSizeClick() {
        binding.rgPizzaSize.setOnCheckedChangeListener { radiouGroup, checkedId ->
            var x = ""
            when (checkedId) {
                binding.rbSmallSize.id -> x = "2"
                binding.rbMediumSize.id -> x = "3"
                binding.rbLargeSize.id -> x = "4"
            }

            if (flavorCount > x.toInt()) {
                Toast.makeText(this, "Por favor, deixe apenas $x sabores para alterar para esse tamanho!", Toast.LENGTH_LONG).show()
                binding.rgPizzaSize.check(lastCheckedId)
            } else {
                maxFlavorCount = x.toInt()

                binding.tvFlavorCountText.setText("Selecione até $x sabores")

                lastCheckedId = checkedId
            }
        }

        binding.rgPizzaSize.check(binding.rbSmallSize.id) // Just to execute de Change Event
    }
    fun configureBtSave() {
        binding.btSave.setOnClickListener {
            if (flavorCount == 0) {
                Toast.makeText(this, "Selecione pelo menos um sabor", Toast.LENGTH_LONG).show()
            } else {
                addOrEditPizzaToOrder()

                Intent(this, FinishOrderActivity::class.java).run {
                    setResult(RESULT_OK)
                    startActivity(this)
                }

                finish()
            }
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

        PriceCalculator.calculatePizzaByEdge(withEdge)
        PriceCalculator.calculatePizzaBySize(size)

        val price = PriceCalculator.getPizzaPrice()

        PriceCalculator.incOrderPrice(price)

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

        PriceCalculator.calculatePizzaByFlavors(count)

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
            PriceCalculator.decOrderPrice(pizza.price)
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
            for (flavor in flavorsList) {
                checkFlavor(flavor)
                flavorCount += 1
            }
        else {
            checkFlavor(flavors)
            flavorCount += 1
        }
    }

    private fun checkFlavor(flavor : String) {
        when (flavor) {
            "Frango" -> binding.cbChicken.isChecked = true
            "Frango com Catupiri" -> binding.cbChickenWithCatupiry.isChecked = true
            "Calabresa" -> binding.cbPepperoni.isChecked = true
            "Portuguesa" -> binding.cbPortuguese.isChecked = true
            "Stroganoff" -> binding.cbStrogonoff.isChecked = true
        }
    }
}