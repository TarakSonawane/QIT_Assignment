package com.example.socialx.Fragments

import android.R
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.socialx.databinding.FragmentSignupFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.hbb20.CountryCodePicker


class signup_fragment : Fragment(),CountryCodePicker.OnCountryChangeListener {

    private var countryCodePicker: CountryCodePicker?=null
    private var countryCode:String?="91"
    private var tnc: TextView?= null
    private lateinit var fireAuth : FirebaseAuth
    private var countryName:String?=null
    private lateinit var binding : FragmentSignupFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)

        //Initiating Firebase
        fireAuth= FirebaseAuth.getInstance()

        //initiating countryCodePicker
        countryCodePicker = binding.countryCodePicker
        countryCodePicker!!.setOnCountryChangeListener(this)
        countryCodePicker!!.setDefaultCountryUsingNameCode("IN")

        //Adding a line below "Term and Condition"
        tnc=binding.tandc
        tnc!!.setPaintFlags(tnc!!.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        //Listener for Register
        binding.register.setOnClickListener{
            binding.termsCheck.clearFocus()
            if(validatee())
            {
                register()
            }
        }
    }

    private fun register() {
        fireAuth.createUserWithEmailAndPassword(binding.email.text.toString(),binding.password.text.toString()).addOnSuccessListener {

            //Uploading Data on firestore
            val user : MutableMap<String,Any> = HashMap()
            val db = FirebaseFirestore.getInstance()
            user["username"]=binding.password.text.toString()
            user["email"]=binding.email.text.toString()
            user["number"]="+"+countryCode+binding.phone.text.toString()
            user["id"]= FirebaseAuth.getInstance().currentUser?.uid.toString()

            Firebase.auth.uid?.let { it1 ->
                db.collection("users").document(it1).set(user, SetOptions.merge())
            }

            Toast.makeText(context, "ACCOUNT CREATED SUCCESSFULLY", Toast.LENGTH_SHORT).show()


        }.addOnFailureListener{e->
            Toast.makeText(context, "ACCOUNT CREATION FAILED DUE TO $e", Toast.LENGTH_SHORT).show()

        }
    }

    private fun  validatee() : Boolean {

        if(binding.name.text?.isEmpty() == true){
            binding.name.error="Name can not be empty!"
            binding.name.requestFocus()
            return false
        }

        else if(binding.email.text?.isEmpty() == true){
            binding.email.error="Email can not be empty!"
            binding.email.requestFocus()
            return false

        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.email.text.toString()).matches()){
            binding.email.error="Invalid email format!"
            binding.email.requestFocus()
            return false


        }

        else if(binding.phone.text.toString().isEmpty() ){
            binding.phone.error="Phone number can not be empty!"
            binding.phone.requestFocus()
            return false

        }

        else if( binding.phone.text.length!=10 ){
            binding.phone.error="Phone number should be of 10 digits!"
            binding.phone.requestFocus()
            return false

        }

        else if(binding.password.text.toString().isEmpty()){
            binding.password.error="Password can not be empty!"
            binding.password.requestFocus()
            return false

        }

        else if(binding.password.text.length<6){
            binding.password.error="Password length should be 6 or more!"
            binding.password.requestFocus()
            return false

        }

        else if(!binding.termsCheck.isChecked){
            binding.termsCheck.error="Agree terms and conditions!"
            binding.termsCheck.requestFocus()
            return false

        }

            return true


    }

    override fun onCountrySelected() {
        countryCode=countryCodePicker!!.selectedCountryCode
        countryName=countryCodePicker!!.selectedCountryName

        Log.d("tttttt", "Country Code "+countryCode)

    }



}