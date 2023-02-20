package com.example.warehouseproject.core.view.main.home_fragment

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import com.example.warehouseproject.core.model.product.Product
import com.example.warehouseproject.core.view.main.home_fragment.home_dialog_detail.DetailDialog


class HomePresenter(private val mainView: HomeView?, private val homeInteractor: DetailDialog): DetailDialog.DetailDialogOnFinished {
    fun showDetailDialog(context: Activity, layoutInflater: LayoutInflater, data: Product) {
        homeInteractor.showDialog(context, layoutInflater, data, this)
    }

    override fun onSuccessDeleted(data: Product?) {
        mainView?.moveMainActivity()
    }
}