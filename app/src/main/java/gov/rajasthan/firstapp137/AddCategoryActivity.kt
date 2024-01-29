package gov.rajasthan.firstapp137

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import gov.rajasthan.firstapp137.databinding.ActivityAddCategoryBinding
import java.io.ByteArrayOutputStream
import java.util.Calendar
import java.util.UUID

class AddCategoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddCategoryBinding
    var imgUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


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

        binding.btnCatImg.setOnClickListener {
            val iGall = Intent(Intent.ACTION_GET_CONTENT)
            iGall.type = "image/*"
            galLauncher.launch(iGall)
        }

        binding.btnSave.setOnClickListener {

            val catName = binding.edtCatName.text.toString()
            val catId = UUID.randomUUID().toString()
            val currTimeStamp = Calendar.getInstance().timeInMillis

            if(imgUrl!=null){
                val firestore = FirebaseFirestore.getInstance()

                firestore.collection("category").add(CategoryModel(catId, catName, imgUrl!!, currTimeStamp)).addOnSuccessListener {
                    Log.d("Success", "${it.id}")
                }.addOnFailureListener {
                    Log.d("Failure", "${it.message}")
                    it.printStackTrace()
                }
            }

        }


    }

    fun getCatImgUrl(imgBytes: ByteArray) {
        val storageRef = Firebase.storage
        val timeStamp = Calendar.getInstance().timeInMillis
        val imgRef =
            storageRef.reference.child("cat/images/IMG_$timeStamp.png")


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