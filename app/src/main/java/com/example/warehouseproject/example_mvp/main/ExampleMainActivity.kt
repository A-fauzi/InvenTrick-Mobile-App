package com.example.warehouseproject.example_mvp.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.warehouseproject.R
import com.example.warehouseproject.databinding.ActivityExampleMainBinding

class ExampleMainActivity : AppCompatActivity(), ExampleMainView {

    private lateinit var binding: ActivityExampleMainBinding

    private val presenter = ExampleMainPresenter(this, ExampleFindItemInteractor())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExampleMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun setItems(items: List<String>) {
        binding.list.adapter = ExampleMainAdapter(items, presenter::onItemClicked)
    }

    override fun showData(data: String) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
    }
}