package gov.rajasthan.firstapp137

import android.R
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import gov.rajasthan.firstapp137.databinding.ActivityAddProductBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.Calendar
import java.util.UUID

class AddProductActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddProductBinding
    var selectedCatId: String? = null
    var selectedSubCatId: String? = null
    var selectedUnit: String? = null
    var selectedColor: String? = null
    val catData = ArrayList<CategoryModel>()
    val subCatData = ArrayList<SubCategoryModel>()
    val firestore = FirebaseFirestore.getInstance()
    var imgUrl: String? = null
    var imgBytes: ByteArray? = null
    var imgThumbNail: ByteArray? = null
    var arrImgSlider: ArrayList<ByteArray> = ArrayList()
    var arrImgSliderUrls: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val galMultiLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {

                    if (it.data!!.clipData != null) {
                        var arrImg = ArrayList<Bitmap>()
                        for (i in 0 until it.data!!.clipData!!.itemCount) {
                            val imgBitmap = MediaStore.Images.Media.getBitmap(
                                applicationContext.contentResolver,
                                it.data!!.clipData!!.getItemAt(i).uri
                            )
                            arrImg.add(imgBitmap)

                            val baos = ByteArrayOutputStream()

                            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                            arrImgSlider.add(baos.toByteArray())

                        }

                        binding.recyclerProductSlider.layoutManager =
                            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                        binding.recyclerProductSlider.adapter =
                            RecyclerProductSliderAdapter(this, arrImg)


                    } else {

                        val imgBitmap = MediaStore.Images.Media.getBitmap(
                            applicationContext.contentResolver,
                            it.data!!.data
                        )

                        val baos = ByteArrayOutputStream()

                        imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                        imgBytes = baos.toByteArray()
                        binding.imgProThumb.setImageBitmap(imgBitmap)


                    }
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
                    imgThumbNail = baos.toByteArray()
                    binding.imgProThumb.setImageBitmap(imgBitmap)

                }
            }


        binding.btnAddProThumb.setOnClickListener {
            val iGall = Intent(Intent.ACTION_GET_CONTENT)
            iGall.type = "image/*"
            galLauncher.launch(iGall)
        }

        binding.btnProductsImg.setOnClickListener {
            val iGall = Intent(Intent.ACTION_GET_CONTENT)
            iGall.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            iGall.type = "image/*"
            galMultiLauncher.launch(iGall)
        }


        // for units dropdown
        val UnitDropDownItems = arrayOf("kg", "gm", "ml", "Ltr", "packets", "pcs")
        val adapter = ArrayAdapter(this, R.layout.simple_expandable_list_item_1, UnitDropDownItems)
        binding.UnitDropDown.setAdapter(adapter)

        binding.UnitDropDown.onItemClickListener = object : AdapterView.OnItemClickListener {


            override fun onItemClick(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                selectedUnit = UnitDropDownItems[pos]
            }

        }


        getCategoryData()

        binding.ProductCategoryDropDown.onItemClickListener =
            object : AdapterView.OnItemClickListener {


                override fun onItemClick(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                    selectedCatId = catData[pos].catId
                    Log.d("CatId", selectedCatId!!)
                    getSubCategoryData()
                }

            }

        binding.ProductTypeDropDown.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                selectedSubCatId = subCatData[pos].subCatId
            }


        }

        // for units dropdown
        val productColor = arrayOf("Red", "Green", "Yellow", "Pink", "Black")
        val Coloradapter =
            ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, productColor)
        binding.productColor.setAdapter(Coloradapter)

        binding.productColor.onItemClickListener = object : AdapterView.OnItemClickListener {


            override fun onItemClick(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                selectedColor = productColor[pos]
            }

        }


        binding.btnAddProduct.setOnClickListener {



            //upload thumbnail
            val storageRef = Firebase.storage
            val timeStamp = Calendar.getInstance().timeInMillis
            val imgRef =
                storageRef.reference.child("product/images/IMG_$timeStamp.png")




            imgRef.putBytes(imgThumbNail!!)
                .addOnSuccessListener {
                    Log.d("Success", "${it.metadata}")

                    imgRef.downloadUrl.addOnSuccessListener {
                        Log.d("ImgUrl", "$it")
                        imgUrl = it.toString()

                        GlobalScope.launch(Dispatchers.IO) {
                            for (eachSliderImg in arrImgSlider) {
                                imgRef.putBytes(eachSliderImg).await()
                                val url = imgRef.downloadUrl.await()
                                Log.d("ImgUrl", "$url")
                                val eachSliderImgUrl = url
                                arrImgSliderUrls.add(eachSliderImgUrl.toString())

                                if (arrImgSliderUrls.size == arrImgSlider.size) {
                                    // add product


                                    val firestore = FirebaseFirestore.getInstance()
                                    val productId = UUID.randomUUID().toString()
                                    val currTimeStamp = Calendar.getInstance().timeInMillis

                                    var title = binding.edtProTitle.text.toString()
                                    var desc = binding.edtProDesc.text.toString()
                                    var qty = binding.edtProQty.text.toString().toInt()
                                    var price = binding.edtProPrice.text.toString().toDouble()
                                    var disPer = binding.edtProDisPer.text.toString().toDouble()


                                    firestore.collection("product")
                                        .add(
                                            ProductModel(
                                                selectedCatId!!,
                                                selectedSubCatId!!,
                                                productId,
                                                title,
                                                desc,
                                                qty,
                                                price,
                                                disPer,
                                                imgUrl!!,
                                                selectedColor!!,
                                                selectedUnit!!,
                                                arrImgSliderUrls,
                                                currTimeStamp)
                                        )
                                        .addOnSuccessListener {
                                            Log.d("Success", "${it.id}")


                                        }.addOnFailureListener {
                                            Log.d("Failure", "${it.message}")
                                            it.printStackTrace()
                                        }


                                }

                            }
                        }
                        // upload Slider



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

    fun getCategoryData() {


        firestore
            .collection("category")
            .get()
            .addOnSuccessListener {

                val catNameData = ArrayList<String>()

                for (eachDoc in it.documents) {
                    val mCat = eachDoc.toObject(CategoryModel::class.java)
                    catData.add(mCat!!)
                    catNameData.add(mCat.catName)
                }
                binding.ProductCategoryDropDown.setAdapter(
                    ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        catNameData
                    )
                )
            }
            .addOnFailureListener {
                Log.d("Failed: ", "Error Fetching notes: ${it.message}")
                it.printStackTrace()
            }

    }

    fun getSubCategoryData() {

        firestore
            .collection("sub-category")
            .whereEqualTo("catId", selectedCatId!!)
            .get()
            .addOnSuccessListener {
                Log.d("CatId", selectedCatId!!)
                Log.d("SubCat", it.size().toString())
                val subCatNameData = ArrayList<String>()

                for (eachDoc in it.documents) {
                    val mSubCat = eachDoc.toObject(SubCategoryModel::class.java)
                    subCatData.add(mSubCat!!)
                    subCatNameData.add(mSubCat.subCatName)
                }
                binding.ProductTypeDropDown.setAdapter(
                    ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        subCatNameData
                    )
                )
            }
            .addOnFailureListener {
                Log.d("Failed: ", "Error Fetching notes: ${it.message}")
                it.printStackTrace()
            }

    }
}