package gov.rajasthan.firstapp137

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import gov.rajasthan.firstapp137.databinding.ActivitySignUpBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    val arrIds = ArrayList<String>()
    var dateFormate = SimpleDateFormat("dd/MM/YYYY")
    var dob = ""
    var gender = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //for Date Start
        binding.selectDate.setOnClickListener {

            val getDate = Calendar.getInstance()

            val datepicker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->

                    val selectDate = Calendar.getInstance()
                    selectDate.set(Calendar.YEAR, i)
                    selectDate.set(Calendar.MONTH, i2)
                    selectDate.set(Calendar.DAY_OF_MONTH, i3)

                    val date = dateFormate.format(selectDate.time)
                    Toast.makeText(this, "Date : " + date, Toast.LENGTH_SHORT).show()
                    binding.selectDate.text = date
                    dob = date

                },
                getDate.get(Calendar.YEAR),
                getDate.get(Calendar.MONTH),
                getDate.get(Calendar.DAY_OF_MONTH)
            )
            datepicker.show()
        }

        //Date Close


        //Spinner State Select Start
        arrIds.apply {

            add("Select State")
            add("West Bengal")
            add("Rajasthan")
            add("Orissa")
            add("Madhya Pradesh")
        }


        val mAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrIds)

        binding.spinnerID.adapter = mAdapter

        //Spinner State Select Close

        binding.radioGrpGender.setOnCheckedChangeListener { radioGroup, i ->
            gender = if (R.id.radio_male == i) {
                "Male"
            } else if(R.id.radio_female==i) {
                "Female"
            } else {
                "Other"
            }
        }

        binding.btnSubmit.setOnClickListener {
            var name = binding.editFullName.text.toString()
            var phnNo = binding.phoneEditText.text.toString()
            var email = binding.editEmail.text.toString()

            var pass = binding.editPass.text.toString()

            var firestore = Firebase.firestore


            var userMap = mapOf<String, Any>(
                "name" to name,
                "phnNo" to phnNo,
                "email" to email,
                "pass" to pass,
                "dob" to dob,
                "gender" to gender
            )

            firestore
                .collection("Users")
                .add(userMap)
                .addOnSuccessListener {
                    Log.d("Success", "User Added ${it.id}!!")
                }.addOnFailureListener {
                    Log.d("Failure", "User not Added ${it.message}!!")
                    it.printStackTrace()
                }

        }
    }
}