package gov.rajasthan.firstapp137

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
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
    var imgBytes: ByteArray? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val galLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {

                    if (it.data!!.clipData != null) {
                        var arrImg = ArrayList<Bitmap>()
                        for(i in 0 until it.data!!.clipData!!.itemCount){
                            val imgBitmap = MediaStore.Images.Media.getBitmap(
                                applicationContext.contentResolver,
                                it.data!!.clipData!!.getItemAt(i).uri
                            )
                            arrImg.add(imgBitmap)
                        }

                    } else {

                        val imgBitmap = MediaStore.Images.Media.getBitmap(
                            applicationContext.contentResolver,
                            it.data!!.data
                        )

                        val baos = ByteArrayOutputStream()

                        imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                        imgBytes = baos.toByteArray()


                    }
                }
            }

        binding.btnCatImg.setOnClickListener {
            val iGall = Intent(Intent.ACTION_GET_CONTENT)
            iGall.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            iGall.type = "image/*"
            galLauncher.launch(iGall)
        }

        binding.btnSave.setOnClickListener {

            showLoading()

            getCatImgUrl(imgBytes!!)


        }


    }

    fun showLoading(){
        binding.pgBar.visibility = View.VISIBLE
        binding.btnSave.visibility = View.GONE
    }

    fun dismissLoading(){
        binding.pgBar.visibility = View.GONE
        binding.btnSave.visibility = View.VISIBLE
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

                    val firestore = FirebaseFirestore.getInstance()
                    val catName = binding.edtCatName.text.toString()
                    val catId = UUID.randomUUID().toString()
                    val currTimeStamp = Calendar.getInstance().timeInMillis

                    firestore.collection("category")
                        .add(CategoryModel(catId, catName, imgUrl!!, currTimeStamp))
                        .addOnSuccessListener {
                            Log.d("Success", "${it.id}")

                            dismissLoading()

                        }.addOnFailureListener {
                        Log.d("Failure", "${it.message}")
                        it.printStackTrace()
                            dismissLoading()
                    }

                }.addOnFailureListener {
                    Log.d("Failure", "${it.message}")
                    it.printStackTrace()
                    dismissLoading()
                }

            }.addOnFailureListener {
                Log.d("Failure", "${it.message}")
                it.printStackTrace()
                dismissLoading()

            }
    }
}