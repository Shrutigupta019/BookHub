package com.internshala.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.bookhub.R
import com.internshala.bookhub.adapter.DashboardRecyclerAdapter
import com.internshala.bookhub.adapter.FavouriteRecyclerAdapter
import com.internshala.bookhub.database.BookDatabase
import com.internshala.bookhub.database.BookEntity
import com.internshala.bookhub.model.Book
import com.internshala.bookhub.util.ConnectionManager
import org.json.JSONException

class FavouritesFragment : Fragment() {

    lateinit var recyclerFavourite: RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavouriteRecyclerAdapter

    var dbbookList = listOf<BookEntity>() // create a variable


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_favourites, container, false)

        recyclerFavourite = view.findViewById(R.id.recyclerFavourites)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        layoutManager = GridLayoutManager(activity as Context, 2)

        dbbookList = RetrieveFavourites(activity as Context).execute().get()

        if (activity != null) {
            progressLayout.visibility = View.GONE
            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context, dbbookList)
            recyclerFavourite.adapter = recyclerAdapter
            recyclerFavourite.layoutManager = layoutManager
        }

        // SENDING (GET) REQUEST TO THE SERVER & RECEIVE RESPONSES TO IT (API) !!

  /*      val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if(ConnectionManager().checkConnectivity(activity as Context)){  // CHECK FOR INTERNET CONNECTIVITY !!

            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener{

                // Here we handle the response using - "TRY & CATCH"
                try{
                    progressLayout.visibility = View.GONE    // This will "HIDE" the progressLayout when we get the output.

                    val success = it.getBoolean(("success"))

                    if(success){

                        val data = it.getJSONArray("data")
                        for(i in 0 until data.length()){
                            val bookJsonObject = data.getJSONObject(i)
                            val bookObject = Book(
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("image")
                            )
                         //   dbbookList.add(bookObject)

                            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context, dbbookList) // Initialise Adapter

                            // Attach adapter and LayoutManager to their respective files
                            recyclerFavourite.adapter = recyclerAdapter
                            recyclerFavourite.layoutManager = layoutManager

                        }
                    } else{
                        Toast.makeText(activity as Context,"Some Error Occurred!!!",Toast.LENGTH_SHORT).show()
                    }
                } catch(e: JSONException){
                    Toast.makeText(activity as Context, "Some unexpected error occurred!!!",Toast.LENGTH_SHORT).show()
                }

            },
                Response.ErrorListener {

                    // Here we handle the "ERROR"
                    if(activity != null){
                        Toast.makeText(activity as Context,"Volley error occurred!!!",Toast.LENGTH_SHORT).show()
                    }

                }){
                // We use getHeaders() method to - "Send the content of the header to the API"
                override fun getHeaders() : MutableMap<String,String>{
                    val headers = HashMap<String,String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "9bf534118365f1"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)

        } else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings"){text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS) // This will open the settings when we click on it.
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text, listener ->
                ActivityCompat.finishAffinity(activity as Activity) // This line will close the app completely.
            }
            dialog.create()
            dialog.show()
        }

        return view */

       return view
    }

    class RetrieveFavourites(val context: Context) : AsyncTask<Void, Void, List<BookEntity>>() {

        override fun doInBackground(vararg p0: Void?): List<BookEntity> {
            val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db")
                .build()  // Initialise the db class(initialise it globally
            // since the entire DBAsyncTask class need it).
            return db.bookDao().getAllBooks()
        }
    }
}
