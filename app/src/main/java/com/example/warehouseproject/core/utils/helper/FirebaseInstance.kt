package com.example.warehouseproject.core.utils.helper

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object FirebaseInstance {
    var database: DatabaseReference = Firebase.database.reference
}