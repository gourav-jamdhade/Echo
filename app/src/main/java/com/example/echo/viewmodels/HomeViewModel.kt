package com.example.echo.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.echo.model.ThreadModel
import com.example.echo.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    private val threadRef = db.getReference("Threads")

    private var _threadsAndUsers = MutableLiveData<List<Pair<ThreadModel, UserModel>>>()
    val threadsAndUsers: LiveData<List<Pair<ThreadModel, UserModel>>> = _threadsAndUsers

    init {
        fetchThreadsAndUsers {
            _threadsAndUsers.postValue(it)
        }
    }


    private fun fetchThreadsAndUsers(onResult: (List<Pair<ThreadModel, UserModel>>) -> Unit) {
        threadRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<Pair<ThreadModel, UserModel>>()
                for (threadSnapshot in snapshot.children) {

                    Log.d("HomeViewModel", snapshot.children.count().toString())
                    val thread = threadSnapshot.getValue(ThreadModel::class.java)
                    thread.let {threadUser ->

                        fetchUserFromThread(threadUser!!) { user ->
                            //Log.d("HomeViewModel", user.userName)

                            result.add(0, threadUser to user)
                            if (result.size == snapshot.childrenCount.toInt()) {
                                onResult(result)
                            }
                        }
                    }
                }

                Log.d("HomeViewModel List", result.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun fetchUserFromThread(thread: ThreadModel, onResult: (UserModel) -> Unit) {
        db.getReference("users").child(thread.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    user?.let { onResult(it) }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}