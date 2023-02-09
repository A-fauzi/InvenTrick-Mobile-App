package com.example.warehouseproject.product

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.warehouseproject.core.view.main.MainActivity
import com.example.warehouseproject.core.view.product.add_product.AddProductActivity
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddProductActivityTest {
    @get : Rule
    var myActivityRule = ActivityScenarioRule(MainActivity::class.java)

}