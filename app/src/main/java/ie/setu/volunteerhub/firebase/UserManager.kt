package ie.setu.volunteerhub.firebase

import androidx.lifecycle.MutableLiveData
import ie.setu.volunteerhub.models.UserModel
import ie.setu.volunteerhub.models.UserStore
import timber.log.Timber

object UserManager : UserStore {

    override fun findById(userid: String, user: MutableLiveData<UserModel>) {

        FirebaseDBManager.database.child("user-details").child(userid).get().addOnSuccessListener {
                user.value = it.getValue(UserModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }


    override fun update(userid: String, user: UserModel) {
        val userdetail = user.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["user-details/$userid"] = userdetail

        FirebaseDBManager.database.updateChildren(childUpdate)
    }
}