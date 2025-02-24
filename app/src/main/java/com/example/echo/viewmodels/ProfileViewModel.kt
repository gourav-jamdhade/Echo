package com.example.echo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.echo.model.ThreadModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileViewModel :ViewModel(){

    private val db = FirebaseDatabase.getInstance()
    private val firebaseUser = FirebaseAuth.getInstance().currentUser

    private val threadRef = db.getReference("Threads")


    private var _threads = MutableLiveData<List<ThreadModel?>>()
    val threads: LiveData<List<ThreadModel?>> = _threads


    init {
        fetchThread {
            _threads.postValue(it)
        }
    }

    private fun fetchThread(onResult: (List<ThreadModel>) -> Unit) {

        threadRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<ThreadModel>()
                for (threadSnapshot in snapshot.children) {
                    val thread = threadSnapshot.getValue(ThreadModel::class.java)
                    if (thread?.userId == firebaseUser?.uid) {
                        result.add(0, thread!!)
                    }


                }

                onResult(result)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}