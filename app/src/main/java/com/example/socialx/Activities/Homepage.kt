package com.example.socialx.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialx.*
import com.example.socialx.Adapters.NewsAdapter
import com.example.socialx.Retrofit.ServiceBuilder
import com.example.socialx.Retrofit.ServiceInterface
import com.example.socialx.databinding.ActivityHomepageBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Homepage : AppCompatActivity() {

    private lateinit var SEARCH: String
    private  lateinit var articl: ArrayList<Articles>
    private  lateinit var recyclerView: RecyclerView
    private  lateinit var binding: ActivityHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initiating RecyclerView
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        //Initiating SEARCH string with value "everything"
        SEARCH="everything"

        //SearchBar Listener
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // on below line we are checking
                // if query exist or not.
                SEARCH = query.toString()

                getnews()

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // if query text is change in that case we
                // are filtering our adapter with
                // new text on below line.
                SEARCH = newText.toString()

                getnews()

                return false
            }
        })

        //Getting news from the news API
        getnews()

    }

    //initiating Menu for Logout Option
    @Override
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout_menu,menu)
        return true
    }

    //Listener for Logout Option
    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout ->
            {
                Firebase.auth.signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getnews() {

        binding.progress.visibility= View.VISIBLE

        articl = ArrayList<Articles>()

        val retrofit = ServiceBuilder.buildService(ServiceInterface::class.java)


        //Passing SEARCH keyword and APIKEY
        val mediaDetails: Call<ApiResponse> = retrofit.gettallnews(SEARCH,"326ccbb6ef6e4723ba5169f233698bb5")


        mediaDetails.enqueue(object : Callback<ApiResponse>
        {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                try {
                    val responsee=response.body()!!

                    articl = responsee.articles as ArrayList<Articles>

                    Log.d("sss", "onResponse: "+articl.size)

                    val jobadapter = NewsAdapter(articl,this@Homepage)
                    recyclerView.adapter = jobadapter

                    binding.progress.visibility= View.GONE

                }
                catch (ex: java.lang.Exception){
                    ex.printStackTrace()

                }

            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@Homepage, t.toString(), Toast.LENGTH_SHORT).show()
            }

        })

    }
    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}