package com.example.checkerstry.classes

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseAuthHelper()
{
    companion object
    {
        fun createUser(email: String, password: String): com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult>
        {
            UserData.email = email
            UserData.password = password
            return Firebase.auth.createUserWithEmailAndPassword(email, password)
        }

        fun singIn(email: String, password: String): com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult>
        {
            UserData.email = email
            UserData.password = password
            return Firebase.auth.signInWithEmailAndPassword(email, password)
        }

        fun deleteAccount(onComplete: () -> Unit)
        {
            val user = Firebase.auth.currentUser!!
            user.reauthenticate(EmailAuthProvider.getCredential(UserData.email, UserData.password)).addOnCompleteListener {
                user.delete().addOnCompleteListener {
                    onComplete()
                }
            }
        }
    }
}