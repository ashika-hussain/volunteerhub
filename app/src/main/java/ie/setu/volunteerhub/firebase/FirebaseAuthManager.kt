package ie.setu.volunteerhub.firebase

import android.app.Application
import android.content.ContentValues.TAG
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ie.setu.volunteerhub.R
import ie.setu.volunteerhub.models.UserModel
import timber.log.Timber


class FirebaseAuthManager(application: Application) {

    private var application: Application? = null
    var googleSignInClient = MutableLiveData<GoogleSignInClient>()

    var firebaseAuth: FirebaseAuth? = null
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var loggedOut = MutableLiveData<Boolean>()
    var errorStatus = MutableLiveData<Boolean>()
    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    init {
        this.application = application
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth!!.currentUser != null) {
            liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
            loggedOut.postValue(false)
            errorStatus.postValue(false)
            FirebaseImageManager.checkStorageForExistingProfilePic(
                firebaseAuth!!.currentUser!!.uid)
        }
        configureGoogleSignIn()
    }


    private fun configureGoogleSignIn() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(application!!.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient.value = GoogleSignIn.getClient(application!!.applicationContext,gso)
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Timber.i( "VolunteerHub firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(application!!.mainExecutor) { task ->
                if (task.isSuccessful) {
                    firebaseAuth!!.addAuthStateListener { firebaseAuth ->
                        Timber.i("Creating user in db")
                        val user = firebaseAuth.currentUser
                        if (user != null) {
                            // User is signed in
                            val userId = user.uid
                            val userEmail = user.email

                            // Create an entry in the database for the new user
                            if (userEmail != null) {
                                createUserInDatabase(userId, userEmail)
                            }
                        } else {
                            logOut()
                        }
                    }
                    // Sign in success, update with the signed-in user's information
                    Timber.i( "signInWithCredential:success")
                    liveFirebaseUser.postValue(firebaseAuth!!.currentUser)

                } else {
                    // If sign in fails, display a message to the user.
                    Timber.i( "signInWithCredential:failure $task.exception")
                    errorStatus.postValue(true)
                }
            }
    }

    fun login(email: String?, password: String?) {
        firebaseAuth!!.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(application!!.mainExecutor) { task ->
                if (task.isSuccessful) {
                    liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
                    errorStatus.postValue(false)
                } else {
                    Timber.i("Login Failure: $task.exception!!.message")
                    errorStatus.postValue(true)
                }
            }
    }

    fun register(email: String?, password: String?) {
        firebaseAuth!!.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(application!!.mainExecutor) { task ->
                if (task.isSuccessful) {
                    firebaseAuth!!.addAuthStateListener { firebaseAuth ->
                        Timber.i("CReating user in db")
                        val user = firebaseAuth.currentUser
                        if (user != null) {
                            // User is signed in
                            val userId = user.uid
                            val userEmail = user.email

                            // Create an entry in the database for the new user
                            if (userEmail != null) {
                                createUserInDatabase(userId, userEmail)
                            }
                        } else {
                            logOut()
                        }
                    }
                    liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
                    errorStatus.postValue(false)

                } else {
                    Timber.i("Registration Failure: $task.exception!!.message")
                    errorStatus.postValue(true)
                }
            }
    }

    fun logOut() {
        firebaseAuth!!.signOut()
        googleSignInClient.value!!.signOut()
        loggedOut.postValue(true)
        errorStatus.postValue(false)
    }
    private fun createUserInDatabase(userId: String, userEmail: String) {
        // You can customize this method to add more user details to the database if needed
        val newUser: UserModel = UserModel(userId) // Assuming User is a model class representing user data
      // database.child("user-details").child(userId).setValue(newUser)

        newUser.uid = userId
        newUser.email = userEmail
        val userdetails = newUser.toMap()
        val userRef = database.child("user-details").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    Timber.i("User exists")
                } else {
                    val childAdd = HashMap<String, Any>()
                    childAdd["/user-details/$userId"] = userdetails
                    database.updateChildren(childAdd)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Timber.e(TAG, "Database operation canceled: ${error.message}")
            }
        })


    }
}