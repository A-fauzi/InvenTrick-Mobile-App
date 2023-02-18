package com.example.warehouseproject.core.view.product.add_product

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import com.example.warehouseproject.R
import com.example.warehouseproject.core.constant.Constant
import com.example.warehouseproject.core.helper.DataJsonFromAssets
import com.example.warehouseproject.core.model.product.ProductModelAssets
import com.example.warehouseproject.core.model.product.ProductRequest
import com.example.warehouseproject.core.model.product.category.Category
import com.example.warehouseproject.core.service.product.category.ProductCategoryService
import com.squareup.picasso.Picasso
import io.paperdb.Paper

class AddProductInteractor {

    interface OnAddProductFinishedListener {
        fun onInputErrorCode()
        fun onInputErrorName()
        fun onInputErrorQty()
        fun onInputErrorCategory()
        fun onInputErrorSubCategory()
        fun onInputErrorSpec()
        fun onInputErrorPrice()
        fun onInputErrorLocation()
        fun onInputErrorStatus()
        fun onInputErrorModel()
        fun onInputErrorLot()
        fun onInputErrorExp()

        fun onSuccessValidationInput()

        fun onDataCategoryRequestNotEmpty(data: List<Category>)
        fun onDataCategoryRequestIsEmpty()

        fun searchItemsIsNull()
        fun searchItemsIsNotNull(data: ProductModelAssets?)

        fun onSuccessTryResultImageFromGallery(data: Intent?)
        fun onFailureCatchResultImageFromGallery(e: Exception)
    }

    fun addProduct(input: ProductRequest, listener: OnAddProductFinishedListener) {
        when {
            input.code_items.isEmpty()  -> listener.onInputErrorCode()
            input.name.isEmpty()  -> listener.onInputErrorName()
            input.qty.isEmpty() -> listener.onInputErrorQty()
            input.category.isEmpty()  -> listener.onInputErrorCategory()
            input.sub_category.isEmpty()  -> listener.onInputErrorSubCategory()
            input.specification.isEmpty()  -> listener.onInputErrorSpec()
            input.price.isEmpty() -> listener.onInputErrorPrice()
            input.location.isEmpty()  -> listener.onInputErrorLocation()
            input.status.isEmpty()  -> listener.onInputErrorStatus()
            input.model.isEmpty()  -> listener.onInputErrorModel()
            input.lot.isEmpty()  -> listener.onInputErrorLot()
            input.exp.isEmpty()  -> listener.onInputErrorExp()
            else -> listener.onSuccessValidationInput()
        }
    }

    fun requestCategoryApi(context: Context, listener: OnAddProductFinishedListener) {
        Paper.init(context)
        val token = Paper.book().read<String>("token").toString()
        ProductCategoryService(token).getCategories{ data ->
            if (data.isNotEmpty()) {
                listener.onDataCategoryRequestNotEmpty(data)
            } else {
                listener.onDataCategoryRequestIsEmpty()
            }
        }
    }

    fun searchItemProductNameByCode(context: Context, jsonFileName: String, itemCode: String, listener: OnAddProductFinishedListener) {
        DataJsonFromAssets.get(context, jsonFileName) { list ->
            val searchItem = list.find { it.itemCode == itemCode }
            if (searchItem == null) {
               listener.searchItemsIsNull()
            } else {
               listener.searchItemsIsNotNull(searchItem)
            }
        }
    }

    fun resultImageFromGallery(requestCode: Int, resultCode: Int, data: Intent?, listener: OnAddProductFinishedListener) {
        if (resultCode == Activity.RESULT_OK && requestCode == Constant.REQUEST_CODE) {
            try {
                listener.onSuccessTryResultImageFromGallery(data)
            }catch (e: Exception) {
                listener.onFailureCatchResultImageFromGallery(e)
            }
        }
    }
}