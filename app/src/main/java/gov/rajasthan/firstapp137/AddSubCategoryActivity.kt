package gov.rajasthan.firstapp137

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseproject137.admin.RecyclerUserAdpter
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestore.*
import com.google.firebase.storage.storage
import gov.rajasthan.firstapp137.admin.UserModal
import gov.rajasthan.firstapp137.databinding.ActivityAddSubCategoryBinding
import java.io.ByteArrayOutputStream
import java.util.Calendar
import java.util.UUID

class AddSubCategoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddSubCategoryBinding
    var selectedCatId : String? = null
    val catData = ArrayList<CategoryModel>()
    var imgUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSubCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firestore = FirebaseFirestore.getInstance()

        firestore
            .collection("category")
            .get()
            .addOnSuccessListener {

                val catNameData = ArrayList<String>()

                for(eachDoc in it.documents){
                    val mCat = eachDoc.toObject(CategoryModel::class.java)
                    catData.add(mCat!!)
                    catNameData.add(mCat.catName)
                }
                binding.spinnerCat.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, catNameData)
            }
            .addOnFailureListener{
                Log.d("Failed: ", "Error Fetching notes: ${it.message}")
                it.printStackTrace()
            }

        binding.spinnerCat.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                selectedCatId = catData[pos].catId
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        val galLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val imgBitmap = MediaStore.Images.Media.getBitmap(
                        applicationContext.contentResolver,
                        it.data!!.data
                    )

                    val baos = ByteArrayOutputStream()

                    imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                    val imgBytes = baos.toByteArray()

                    getCatImgUrl(imgBytes)

                }
            }

        binding.btnSubCatImg.setOnClickListener {
            val iGall = Intent(Intent.ACTION_GET_CONTENT)
            iGall.type = "image/*"
            galLauncher.launch(iGall)
        }

        binding.btnSave.setOnClickListener {

            selectedCatId?.let{
                val subCatName = binding.edtSubCatName.text.toString()
                val subCatId = UUID.randomUUID().toString()
                val currTimeStamp = Calendar.getInstance().timeInMillis

                if(imgUrl!=null){
                    val firestore = FirebaseFirestore.getInstance()

                    firestore.collection("sub-category").add(SubCategoryModel(selectedCatId!!, subCatId, subCatName, imgUrl!!, currTimeStamp)).addOnSuccessListener {
                        Log.d("Success", "${it.id}")
                    }.addOnFailureListener {
                        Log.d("Failure", "${it.message}")
                        it.printStackTrace()
                    }
                }

            }


        }

    }

    fun getCatImgUrl(imgBytes: ByteArray) {
        val storageRef = Firebase.storage
        val timeStamp = Calendar.getInstance().timeInMillis
        val imgRef =
            storageRef.reference.child("subCat/images/IMG_$timeStamp.png")


        imgRef.putBytes(imgBytes)
            .addOnSuccessListener {
                Log.d("Success", "${it.metadata}")

                imgRef.downloadUrl.addOnSuccessListener {
                    Log.d("ImgUrl", "$it")
                    imgUrl = it.toString()
                }.addOnFailureListener {
                    Log.d("Failure", "${it.message}")
                    it.printStackTrace()
                }

            }.addOnFailureListener {
                Log.d("Failure", "${it.message}")
                it.printStackTrace()
            }
    }
}