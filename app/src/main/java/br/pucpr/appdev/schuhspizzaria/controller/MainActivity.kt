package br.pucpr.appdev.schuhspizzaria.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.pucpr.appdev.schuhspizzaria.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

            binding.btSignIn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java).apply {}
            startActivity(intent)
            finish()
        }
    }
}