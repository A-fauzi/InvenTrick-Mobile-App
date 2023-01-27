package com.example.warehouseproject.core.product.add_product

class AddProductPresenter(var addProductView: AddProductView?, val addProductInteractor: AddProductInteractor): AddProductInteractor.OnAddProductFinishedListener {
    fun validateAddProduct(inputFormAddProduct: ModelRequestAddProduct) {
        addProductView?.showProgress()
        addProductInteractor.addProduct(inputFormAddProduct, this)
    }

    override fun onInputError() {
        addProductView?.apply {
            setInputError()
            hideProgress()
        }
    }

    override fun onSuccess() {
        addProductView?.navigateToHome()
        addProductView?.getDataProduct()
    }
}