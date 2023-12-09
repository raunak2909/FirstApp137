package gov.rajasthan.firstapp137

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import gov.rajasthan.firstapp137.databinding.ActivityMainBinding
import gov.rajasthan.firstapp137.databinding.AddNoteDialogBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var firestore : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = Firebase.firestore

        getAllNotes()

        binding.fabAdd.setOnClickListener {

            val dialogAdd = Dialog(this)
            val dialogBinding = AddNoteDialogBinding.inflate(layoutInflater)
            dialogAdd.setContentView(dialogBinding.root)

            dialogBinding.btnAdd.setOnClickListener {
                val title = dialogBinding.edtTitle.text.toString()
                val desc = dialogBinding.edtDesc.text.toString()

                if(title.isNotEmpty() && desc.isNotEmpty()){
                    // writing into firestore
                    val currTimeStamp = Calendar.getInstance().timeInMillis
                    val newNote = NoteModel(title, desc, currTimeStamp)

                    firestore
                        .collection("notes")
                        .document("$currTimeStamp")
                        .set(newNote)
                        .addOnSuccessListener {
                            Log.d("Success: ", "Note added successfully!!")
                            getAllNotes()
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

    fun getAllNotes(){
        firestore
            .collection("notes")
            .orderBy("timeSTamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                val notesData = ArrayList<NoteModel>()

                for(eachDoc in it.documents){
                    val mNote = eachDoc.toObject(NoteModel::class.java)
                    notesData.add(mNote!!)
                }

                binding.recyclerNotes.layoutManager = LinearLayoutManager(this)
                binding.recyclerNotes.adapter = RecyclerNotesAdapter(this, notesData)

            }
            .addOnFailureListener{
                Log.d("Failed: ", "Error Fetching notes: ${it.message}")
                it.printStackTrace()
            }
    }
}