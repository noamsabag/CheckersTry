package com.example.checkerstry.classes

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.awaitAll

class FirebaseAuthHelper()
{
    companion object Helper
    {
        fun createUser(email: String, password: String): com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult>
        {
            return Firebase.auth.createUserWithEmailAndPassword(email, password)
        }

        fun singIn(email: String, password: String): com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult>
        {
            return Firebase.auth.signInWithEmailAndPassword(email, password)
        }
    }
}