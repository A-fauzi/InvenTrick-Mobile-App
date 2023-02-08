package com.example.warehouseproject.core.view.main.scan_fragment

import androidx.fragment.app.Fragment

class ScanPresenter(private val fragment: Fragment, private val interactor: ScanInteractor) {

    fun scanFromCamera() {
        interactor.scanFromCamera(fragment)
    }

}