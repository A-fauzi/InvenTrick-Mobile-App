package com.example.warehouseproject.core.utils.helper

import androidx.fragment.app.FragmentActivity
import com.google.android.material.datepicker.MaterialDatePicker

class DatePickerDialog(private val fragmentActivity: FragmentActivity) {
    fun setTextTitleDate(title: String, datePickerInteractor: (datePicker: MaterialDatePicker<Long>) -> Unit) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(title)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePickerInteractor(datePicker)
    }
}