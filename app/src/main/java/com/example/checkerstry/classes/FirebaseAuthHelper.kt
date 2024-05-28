package com.example.checkerstry.classes


import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.gms.tasks.Task

/**
 * Helper class for Firebase authentication actions. It provides static functions to create user accounts,
 * sign in, delete accounts, and log out, managing user credentials using Firebase.
 */
class FirebaseAuthHelper {
    companion object {
        /**
         * Creates a new user account with the specified email and password.
         * Updates the global UserData with the new account's email and password.
         *
         * @param email The email address to create the account with.
         * @param password The password for the new account.
         * @param onComplete Callback function to handle the completion of the create user task.
         */
        fun createUser(email: String, password: String, onComplete: (Task<AuthResult>) -> Unit) {
            UserData.email = email
            UserData.password = password
            Firebase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                onComplete(it)
            }
        }

        /**
         * Signs in a user using the provided email and password.
         * Updates the global UserData with the signed-in user's email and password.
         *
         * @param email The email address to sign in with.
         * @param password The password for the account.
         * @param onComplete Callback function to handle the completion of the sign-in task.
         */
        fun signIn(email: String, password: String, onComplete: (Task<AuthResult>) -> Unit) {
            UserData.email = email
            UserData.password = password
            Firebase.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                onComplete(it)
            }
        }

        /**
         * Deletes the currently authenticated user's account. Before deletion, it re-authenticates the user
         * using stored credentials from UserData to ensure the operation is secure.
         *
         * @param onComplete Callback function to handle the completion of the account deletion task.
         */
        fun deleteAccount(onComplete: () -> Unit) {
            val user = Firebase.auth.currentUser!!
            user.reauthenticate(EmailAuthProvider.getCredential(UserData.email, UserData.password)).addOnCompleteListener {
                user.delete().addOnCompleteListener {
                    onComplete()
                }
            }
        }

        /**
         * Logs out the current user from the application. This method signs out the user from Firebase authentication,
         * effectively clearing any session data and credentials from the Firebase context.
         */
        fun logOut() {
            Firebase.auth.signOut()
        }
    }
}
