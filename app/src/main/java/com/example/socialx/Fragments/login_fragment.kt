package com.example.socialx.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.socialx.Activities.Homepage
import com.example.socialx.databinding.FragmentLoginFragmentBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth

class login_fragment : Fragment() {


    private lateinit var binding : FragmentLoginFragmentBinding
    private lateinit var fireAuth : FirebaseAuth
    private val RC_SIGN_IN = 9001
    private var mGoogleSignInClient: GoogleSignInClient? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginFragmentBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        super.onCreate(savedInstanceState)

        //initiating Google Sign in methods
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        //Google Icon Click Listener
        binding.google.setOnClickListener(View.OnClickListener {
            signIn()
        })

        //initiating fireAuth
        fireAuth= FirebaseAuth.getInstance()

        //Login Listener
        binding.login.setOnClickListener{

            if(validatee())
            {
                login()
            }

        }

    }

    private fun signIn() {
        val intent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)
                HomeActivity1()
            } catch (_: ApiException) {

            }
        }
    }

    private fun HomeActivity1() {

        val intent = Intent(context, Homepage::class.java)
        startActivity(intent)
    }

    private fun  validatee() : Boolean {

        if(binding.email.text?.isEmpty() == true){
            binding.email.error="Email can not be empty!"
            binding.email.requestFocus()
            return false
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.email.text.toString()).matches()){
            binding.email.error="Invalid email format!"
            binding.email.requestFocus()
            return false
        }
        else if(binding.password.text.toString().isEmpty()){
            binding.password.error="Password can not be empty!"
            binding.password.requestFocus()
            return false

        }
        return true
    }

    private fun login() {
        fireAuth.signInWithEmailAndPassword(binding.email.text.toString(),binding.password.text.toString()).addOnSuccessListener {
            Toast.makeText(context, "LOGGED IN SUCCESSFULLY", Toast.LENGTH_SHORT).show()


            val intent = Intent(context, Homepage::class.java)
            startActivity(intent)

        }.addOnFailureListener{e->
            Toast.makeText(context, "LOGGED IN FAILED DUE TO $e", Toast.LENGTH_SHORT).show()

        }
    }

}