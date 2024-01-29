package com.example.firebaseproject137.admin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import gov.rajasthan.firstapp137.admin.UserModal
import gov.rajasthan.firstapp137.databinding.UserRowBinding


class RecyclerUserAdpter(val context: Context, val arrUserList : ArrayList<UserModal>) : RecyclerView.Adapter<RecyclerUserAdpter.ViewHolder>() {
    class ViewHolder (val binding : UserRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(UserRowBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return arrUserList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val arrIds = ArrayList<String>()
        arrIds.apply {

            add("Active")
            add("In-Active")
            add("Pending")
            add("Block")

        }


        val mAdapter = ArrayAdapter<String>(
            context,
            android.R.layout.simple_spinner_dropdown_item,
            arrIds
        )

        holder.binding.userStatus.adapter = mAdapter

        holder.binding.userStatus.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                val firestore = FirebaseFirestore.getInstance()
                firestore.collection("Users")
                    .document(arrUserList[position].userId).update("status", pos)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

       /* holder.binding.btnUpdate.setOnClickListener {
            context.startActivity(Intent(context, snjvnsfdv::class.java).putExtra("userId", arrUserList[position].userId))
        }*/

        //holder.binding.userImg.setImageResource(arrUserList[position].imgPath)
        holder.binding.txtUserName.text = arrUserList[position].name
        holder.binding.txtUserMob.text = arrUserList[position].phnNo
        holder.binding.userStatus.setSelection(arrUserList[position].status!!)
    }
}