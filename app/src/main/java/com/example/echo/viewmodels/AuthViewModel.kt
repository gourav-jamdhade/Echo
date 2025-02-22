package com.example.echo.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.echo.model.UserModel
import com.example.echo.utils.SharedPrefs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.parse.ParseACL
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.SaveCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    private val userRef = db.getReference("users")

    private val _firebaseUser = MutableLiveData<FirebaseUser>()
    val firebaseUser: LiveData<FirebaseUser> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        _firebaseUser.value = auth.currentUser
    }

    fun login(email: String, password: String, context: Context) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { login ->
            if (login.isSuccessful) {

                _firebaseUser.postValue(auth.currentUser)

                getData(auth.currentUser?.uid,context)

            } else {
                _error.postValue(login.exception?.message)
            }
        }
    }

    private fun getData(uid: String?,context: Context) {

        userRef.child(uid!!).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserModel::class.java)
                SharedPrefs.storeData(userData!!.name, userData.email, userData.userName, userData.bio, userData.imageUrl!!, context)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    fun register(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: Uri?,
        context: Context
    ) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                _firebaseUser.postValue(auth.currentUser)
                if (imageUri != null) {
                    //Saving Profile Image to Back4app
                    saveImageToBack4App(context, imageUri) { imageUrl ->

                        saveUserData(userId, email, name, bio, userName, imageUrl, context)

                    }

                } else {
                    saveUserData(userId, email, name, bio, userName, null, context)
                }
            } else {
                _error.postValue(task.exception?.message)
            }
        }
    }

    private fun saveUserData(
        userId: String?,
        email: String,
        name: String,
        bio: String,
        userName: String,
        imageUrl: String?,
        context: Context

    ) {

        val userData = UserModel(userId, email, name, bio, userName, imageUrl)

        userRef.child(userId!!).setValue(userData)
            .addOnSuccessListener {
                SharedPrefs.storeData(name, email, userName, bio, imageUrl, context)

            }.addOnFailureListener {

            }
    }

    private fun saveImageToBack4App(context: Context, imageUri: Uri, onSuccess: (String) -> Unit) {

        CoroutineScope(Dispatchers.IO).launch {
            try {


                val inputStream = context.contentResolver.openInputStream(imageUri)
                val bytes = inputStream?.readBytes()
                inputStream?.close()

                if (bytes != null) {
                    val parseFile = ParseFile("image_${System.currentTimeMillis()}.jpg", bytes)
                    parseFile.saveInBackground(SaveCallback { e ->
                        if (e == null) {
                            val fileObject = ParseObject("UserProfile")
                            fileObject.put("profileImage", parseFile)

                            val acl = ParseACL()
                            acl.publicReadAccess = true  // ✅ Allow public read access
                            fileObject.acl = acl

                            fileObject.saveInBackground()
                            onSuccess(parseFile.url)
                            Log.d("Upload", "File uploaded successfully: ${parseFile.url}")
                        } else {
                            Log.d("error", e.message.toString())
                        }
                    })
                }

            } catch (e: Exception) {

            }
        }

    }

    @SuppressLint("NullSafeMutableLiveData")
    fun logout() {
        auth.signOut()
        _firebaseUser.postValue(null)
    }


}