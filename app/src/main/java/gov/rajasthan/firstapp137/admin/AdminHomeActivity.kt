package com.example.firebaseproject137.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import gov.rajasthan.firstapp137.AddCategoryActivity
import gov.rajasthan.firstapp137.NoteModel
import gov.rajasthan.firstapp137.R
import gov.rajasthan.firstapp137.RecyclerNotesAdapter
import gov.rajasthan.firstapp137.admin.UserModal
import gov.rajasthan.firstapp137.databinding.ActivityAdminHomeBinding

class AdminHomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdminHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*var arrData = ArrayList<UserModal>().apply {
            add(UserModal(R.drawable.person, "rohan", "9709123458"))
            add(UserModal(R.drawable.person, "rajit", "9709123458"))
            add(UserModal(R.drawable.person, "suman", "9709123458"))
            add(UserModal(R.drawable.person, "Sachin", "9709123458"))
        }*/

        binding.btnCat.setOnClickListener {

            startActivity(Intent(this@AdminHomeActivity, AddCategoryActivity::class.java))

        }




    }
}