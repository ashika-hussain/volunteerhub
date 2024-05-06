package ie.setu.volunteerhub.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import ie.setu.volunteerhub.firebase.UserManager

interface UserStore {


    fun findById(userid:String,user: MutableLiveData<UserModel>)
    fun update(userid:String, donation: UserModel)
}