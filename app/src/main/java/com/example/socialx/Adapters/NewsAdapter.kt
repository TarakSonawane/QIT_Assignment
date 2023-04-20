package com.example.socialx.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.RecyclerView
import com.example.socialx.Articles
import com.example.socialx.R
import com.google.type.DateTime
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.time.Clock.system
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class NewsAdapter(private val articles:ArrayList<Articles>,private val ctx : Context) : RecyclerView.Adapter<NewsAdapter.newsviewholder>() {

    class newsviewholder(view: View): RecyclerView.ViewHolder(view)
    {

        val title: TextView =view.findViewById(R.id.title)
        val des : TextView = view.findViewById(R.id.desc)
        val time: TextView =view.findViewById(R.id.time)
        val sour: TextView =view.findViewById(R.id.source)
        val pic: ImageView =view.findViewById(R.id.pic)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): newsviewholder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.news_card, parent, false)

        return newsviewholder(adapterLayout)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: newsviewholder, position: Int) {

        val data = articles[position]

        //Fetching Title
        holder.title.text= data.title.toString()

        //Fetching Description
        holder.des.text= data.description.toString()

        //CONVERTING fetched date into appropriate format and getting hours it was posted before
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        val current = dateFormat.format(date)
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val datestart = inputFormat.parse(data.publishedAt)
        val startt = outputFormat.format(datestart!!)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val start = LocalDateTime.parse(startt, formatter)
        val end = LocalDateTime.parse(current, formatter)
        val duration = Duration.between(start, end)
        val hours = duration.toHours()

        holder.time.text = hours.toString()+ " hours "+"ago"


        //Fetching Image
        if(!data.urlToImage.isNullOrBlank() ||  !data.urlToImage.isNullOrEmpty()){
          Picasso.get().load(data.urlToImage).error(R.drawable.ic_launcher_background)
              .into(holder.pic) }

        //Fetching Source
        holder.sour.text = data.source.name.toString()

        //Opening News URL
        holder.itemView.setOnClickListener()
        {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(ctx, Uri.parse(data.url))

        }


    }
    override fun getItemCount(): Int {
        return articles.size
    }
}
