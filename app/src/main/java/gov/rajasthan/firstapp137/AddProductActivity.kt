package gov.rajasthan.firstapp137

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.firestore.FirebaseFirestore
import gov.rajasthan.firstapp137.databinding.ActivityAddProductBinding

class AddProductActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddProductBinding
    var selectedCatId : String? = null
    var selectedSubCatId : String? = null
    val catData = ArrayList<CategoryModel>()
    val subCatData = ArrayList<SubCategoryModel>()
    val firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // for units dropdown
        val UnitDropDownItems = arrayOf("kg", "gm", "ml", "Ltr", "packets")
        val adapter = ArrayAdapter(this, R.layout.simple_expandable_list_item_1, UnitDropDownItems)
        binding.UnitDropDown.setAdapter(adapter)


        getCategoryData()

        binding.ProductCategoryDropDown.onItemClickListener = object : AdapterView.OnItemClickListener {


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
        val Coloradapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, productColor)
        binding.productColor.setAdapter(Coloradapter)




    }

    fun getCategoryData(){



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
                binding.ProductCategoryDropDown.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, catNameData))
            }
            .addOnFailureListener{
                Log.d("Failed: ", "Error Fetching notes: ${it.message}")
                it.printStackTrace()
            }

    }

    fun getSubCategoryData(){

        firestore
            .collection("sub-category")
            .whereEqualTo("catId", selectedCatId!!)
            .get()
            .addOnSuccessListener {
                Log.d("CatId", selectedCatId!!)
                Log.d("SubCat", it.size().toString())
                val subCatNameData = ArrayList<String>()

                for(eachDoc in it.documents){
                    val mSubCat = eachDoc.toObject(SubCategoryModel::class.java)
                    subCatData.add(mSubCat!!)
                    subCatNameData.add(mSubCat.subCatName)
                }
                binding.ProductTypeDropDown.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, subCatNameData))
            }
            .addOnFailureListener{
                Log.d("Failed: ", "Error Fetching notes: ${it.message}")
                it.printStackTrace()
            }

    }
}