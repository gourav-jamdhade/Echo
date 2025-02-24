package com.example.echo.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.echo.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    private val threadRef = db.getReference("users")

    private var _users = MutableLiveData<List<UserModel>>()
    val usersList: LiveData<List<UserModel>> = _users

    init {
        fetchUsers {
            _users.postValue(it)
        }
    }


    private fun fetchUsers(onResult: (List<UserModel>) -> Unit) {
        threadRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<UserModel>()
                for (userSnapshot in snapshot.children) {

                    Log.d("HomeViewModel", snapshot.children.count().toString())
                    val user = userSnapshot.getValue(UserModel::class.java)
                    result.add(user!!)
                }

                onResult(result)

                Log.d("HomeViewModel List", result.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}