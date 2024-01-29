package gov.rajasthan.firstapp137

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseproject137.admin.RecyclerUserAdpter
import com.google.firebase.firestore.FirebaseFirestore
import gov.rajasthan.firstapp137.admin.UserModal
import gov.rajasthan.firstapp137.databinding.ActivityAdminHomeBinding
import gov.rajasthan.firstapp137.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {

    lateinit var binding:ActivityUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val firestore = FirebaseFirestore.getInstance()

        firestore
            .collection("Users")
            .get()
            .addOnSuccessListener {
                val userData = ArrayList<UserModal>()

                for(eachDoc in it.documents){
                    val mUser = eachDoc.toObject(UserModal::class.java)
                    mUser!!.userId = eachDoc.id
                    userData.add(mUser)
                }
                binding.recyclerViewUser.layoutManager = LinearLayoutManager(this)
                binding.recyclerViewUser.adapter = RecyclerUserAdpter(this, userData)

            }
            .addOnFailureListener{
                Log.d("Failed: ", "Error Fetching notes: ${it.message}")
                it.printStackTrace()
            }
    }
}