package gov.rajasthan.firstapp137

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import gov.rajasthan.firstapp137.databinding.ActivityMainBinding
import gov.rajasthan.firstapp137.databinding.AddNoteDialogBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firestore = Firebase.firestore

        binding.fabAdd.setOnClickListener {

            val dialogAdd = Dialog(this)
            val dialogBinding = AddNoteDialogBinding.inflate(layoutInflater)
            dialogAdd.setContentView(dialogBinding.root)

            dialogBinding.btnAdd.setOnClickListener {
                val title = dialogBinding.edtTitle.text.toString()
                val desc = dialogBinding.edtDesc.text.toString()

                if(title.isNotEmpty() && desc.isNotEmpty()){
                    // writing into firestore
                    val newNote = NoteModel(title, desc)
                    firestore
                        .collection("notes")
                        .add(newNote)
                        .addOnSuccessListener {
                            Log.d("Success: ", "Note added successfully!!")
                        }
                        .addOnFailureListener{
                            Log.d("Failed: ", "Error adding note: ${it.message}")
                            it.printStackTrace()
                        }
                    dialogAdd.dismiss()
                }

            }

            dialogBinding.btnCancel.setOnClickListener {
                dialogAdd.dismiss()
            }

            dialogAdd.show();

        }


    }
}