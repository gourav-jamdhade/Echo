package com.example.echo.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.echo.model.ThreadModel
import com.google.firebase.database.FirebaseDatabase
import com.parse.ParseACL
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.SaveCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class AddThreadViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    private val userRef = db.getReference("Threads")

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    private val _isPosted = MutableLiveData<Boolean?>()
    val isPosted: LiveData<Boolean?> = _isPosted


    fun saveThreadData(
        userId: String,
        thread: String,
        imageUrl: String?,


        ) {

        _isLoading.postValue(true) // Start loading
        val threadData =
            ThreadModel(
                userId = userId,
                thread = thread,
                imageUrl = imageUrl,
                timestamp = System.currentTimeMillis().toString()
            )

        userRef.child(userRef.push().key!!).setValue(threadData).addOnSuccessListener {
            _isPosted.postValue(true)
            _isLoading.postValue(false)

        }.addOnFailureListener {
            _isPosted.postValue(false)
            _isLoading.postValue(false)
        }
    }

    fun saveImageToBack4App(context: Context, imageUri: Uri, onSuccess: (String) -> Unit) {

        CoroutineScope(Dispatchers.IO).launch {
            try {


                val inputStream = context.contentResolver.openInputStream(imageUri)
                val bytes = inputStream?.readBytes()
                inputStream?.close()

                if (bytes != null) {
                    val parseFile = ParseFile("thread_ ${UUID.randomUUID()}.jpg", bytes)
                    parseFile.saveInBackground(SaveCallback { e ->
                        if (e == null) {

                            val fileObject = ParseObject("Threads")
                            fileObject.put("threadImage", parseFile)

                            val acl = ParseACL()
                            acl.publicReadAccess = true  // ✅ Allow public read access
                            fileObject.acl = acl

                            fileObject.saveInBackground()
                            onSuccess(parseFile.url)
                            // _isLoading.postValue(true)
                            Log.d("Upload", "File uploaded successfully: ${parseFile.url}")
                        } else {
                            _isLoading.postValue(false) // Stop loading if upload fails
                            Log.d("error", e.message.toString())
                        }
                    })
                }

            } catch (e: Exception) {
                _isLoading.postValue(false) // Stop loading on exception
                Log.e("saveImageToBack4App", "Error uploading image", e)
            }
        }

    }

    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }


}