package com.example.warehouseproject.example_mvp.main

class ExampleMainPresenter(var mainView: ExampleMainView?, val findItemInteractor: ExampleFindItemInteractor) {
    fun onResume() {
        mainView?.showProgress()
        findItemInteractor.findItems(::onItemsLoaded)
    }

    private fun onItemsLoaded(items: List<String>) {
        mainView?.apply {
            setItems(items)
            hideProgress()
        }
    }

    fun onItemClicked(item: String) {
        mainView?.showData(item)
    }

    fun onDestroy() {
        mainView = null
    }
}