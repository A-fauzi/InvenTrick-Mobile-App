package com.example.warehouseproject.core.view.main

import com.example.warehouseproject.domain.modelentities.user.UserResponse

interface MainView {

    /**
     * Fungsi ini akan dipanggil ketika permintaan ke server berhasil dan respons dari server dalam bentuk UserResponse.
     * SingleResponse berhasil diterima.
     */
    fun onSuccessBodyReqStatusView(response: UserResponse.SingleResponse)

    /**
     * Fungsi ini akan dipanggil ketika permintaan ke server berhasil tetapi terjadi kesalahan dalam pemrosesan respons dari server, sehingga message akan berisi informasi tentang kesalahan tersebut.
     */
    fun onErrorBodyReqStatusView(message: String)

    /**
     * Fungsi ini akan dipanggil ketika permintaan ke server gagal dilakukan, baik itu karena masalah koneksi internet atau kesalahan lainnya.
     * message akan berisi informasi tentang kesalahan yang terjadi.
     */
    fun onFailureView(message: String)


}