package com.example.warehouseproject.core.utils.helper

import android.content.Context
import android.widget.Toast
import com.example.warehouseproject.core.utils.helper.FirebaseInstance.database
import com.example.warehouseproject.core.model.user.UserRequest

class RealtimeDatabase(private val context: Context) {
    fun write(uid: String, userActivity: UserRequest.StatusActivity) {
        database.child("users").child(uid).setValue(userActivity).addOnSuccessListener {
//            Toast.makeText(context, "success upload status", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }
}