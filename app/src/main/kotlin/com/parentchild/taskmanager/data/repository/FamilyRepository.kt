package com.parentchild.taskmanager.data.repository

import com.google.firebase.firestore.FirebaseFirestore

class FamilyRepository {

    private val db = FirebaseFirestore.getInstance()

    fun createFamilyCode(code: String, onDone: () -> Unit) {
        val data = hashMapOf(
            "code" to code,
            "createdAt" to System.currentTimeMillis()
        )

        db.collection("families")
            .document(code)
            .set(data)
            .addOnSuccessListener { onDone() }
    }

    fun joinFamily(code: String, onDone: (Boolean) -> Unit) {
        db.collection("families")
            .document(code)
            .get()
            .addOnSuccessListener {
                onDone(it.exists())
            }
    }
}
