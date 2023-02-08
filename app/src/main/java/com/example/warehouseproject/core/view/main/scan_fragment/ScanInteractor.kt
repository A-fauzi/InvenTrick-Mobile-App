package com.example.warehouseproject.core.view.main.scan_fragment

import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator

class ScanInteractor {

    fun scanFromCamera(fragment: Fragment) {
        val qrScan = IntentIntegrator.forSupportFragment(fragment)
        qrScan.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        qrScan.setPrompt("Scan a QR Code | warehouse-project")
        qrScan.setOrientationLocked(true)
        qrScan.setBeepEnabled(true)
        qrScan.setBarcodeImageEnabled(true)
        //initiating the qr code scan
        qrScan.initiateScan()
    }

}