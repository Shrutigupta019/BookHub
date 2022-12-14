package com.internshala.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.bookhub.R
import com.internshala.bookhub.adapter.DashboardRecyclerAdapter
import com.internshala.bookhub.model.Book
import com.internshala.bookhub.util.ConnectionManager
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.json.JSONException
import java.text.AttributedCharacterIterator
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager

    //lateinit var btnCheckInternet: Button

    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    val bookInfoList = arrayListOf<Book>()

    // Created a "ratingComparator" variable to compare ->  the rating of 2 book objects.(FOR SORTING ICON !!)
    var ratingComparator = Comparator<Book> { book1, book2 ->

        if (book1.bookRating.compareTo(book2.bookRating, ignoreCase = true) == 0) {
            // sort according to name if rating is same
            book1.bookName.compareTo(book2.bookName, ignoreCase = true)
        } else {
            book1.bookRating.compareTo(book2.bookRating, ignoreCase = true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard) // Initialise RecyclerView

        layoutManager = LinearLayoutManager(activity) // Initialise LayoutManager

        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        progressLayout.visibility =
            View.VISIBLE // Want to progressLayout VISIBLE - when the fragmnet is being loaded.

        setHasOptionsMenu(true) // For SORTING icon..

                             // SENDING (GET) REQUEST TO THE SERVER & RECEIVE RESPONSES TO IT (API) !!

        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if (ConnectionManager().checkConnectivity(activity as Context)) {  // CHECK FOR INTERNET CONNECTIVITY !!

            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                   // Here we handle the response using - "TRY & CATCH"
                    try {
                        progressLayout.visibility = View.GONE    // This will "HIDE" the progressLayout when we get the output.

                        val success = it.getBoolean(("success"))

                        if (success) {
                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val bookJsonObject = data.getJSONObject(i)
                                val bookObject = Book(
                                    bookJsonObject.getString("book_id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("rating"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("image")
                                )
                                bookInfoList.add(bookObject)

                                recyclerAdapter = DashboardRecyclerAdapter(activity as Context, bookInfoList) // Initialise Adapter

                                // Attach adapter and LayoutManager to their respective files
                                recyclerDashboard.adapter = recyclerAdapter
                                recyclerDashboard.layoutManager = layoutManager

                                // Add "DIVIDER LINE" between each row !!
                                /* recyclerDashboard.addItemDecoration(
                                DividerItemDecoration(
                                    recyclerDashboard.context,
                                    (layoutManager as LinearLayoutManager).orientation
                                )
                            ) */
                            }
                        } else {
                            Toast.makeText(
                                activity as Context, "Some Error Occurred!!!", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(activity as Context, "Some unexpected error occurred!!!", Toast.LENGTH_SHORT).show()
                    }

                },
                    Response.ErrorListener {

                        // Here we handle the "ERROR"
                        if (activity != null) {
                            Toast.makeText(activity as Context, "Volley error occurred!!!", Toast.LENGTH_SHORT).show()
                        }

                    }) {
                    // We use getHeaders() method to - "Send the content of the header to the API"
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "9bf534118365f1"
                        return headers
                    }
                }

            queue.add(jsonObjectRequest)

        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent =
                    Intent(Settings.ACTION_WIRELESS_SETTINGS) // This will open the settings when we click on it.
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity) // This line will close the app completely.
            }
            dialog.create()
            dialog.show()
        }

        return view
    }
    // ADDING SORTING ICON !!

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item?.itemId
        if (id == R.id.action_sort) {
            Collections.sort(
                bookInfoList,
                ratingComparator
            ) // This will sort the list to lowest rating appear first
            bookInfoList.reverse() // This line will sort the list to Highest rating appear first
        }
        recyclerAdapter.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }
}

    /*   val bookInfoList = arrayListOf<Book>(
        Book("P.S. I love You", "Cecelia Ahern", "Rs. 299", "4.5", R.drawable.ps_ily),
        Book("The Great Gatsby", "F. Scott Fitzgerald", "Rs. 399", "4.1", R.drawable.great_gatsby),
        Book("Anna Karenina", "Leo Tolstoy", "Rs. 199", "4.3", R.drawable.anna_kare),
        Book("Madame Bovary", "Gustave Flaubert", "Rs. 500", "4.0", R.drawable.madame),
        Book("War and Peace", "Leo Tolstoy", "Rs. 249", "4.8", R.drawable.war_and_peace),
        Book("Lolita", "Vladimir Nabokov", "Rs. 349", "3.9", R.drawable.lolita),
        Book("Middlemarch", "George Eliot", "Rs. 599", "4.2", R.drawable.middlemarch),
        Book("The Adventures of Huckleberry Finn", "Mark Twain", "Rs. 699", "4.5", R.drawable.adventures_finn),
        Book("Moby-Dick", "Herman Melville", "Rs. 499", "4.5", R.drawable.moby_dick),
        Book("The Lord of the Rings", "J.R.R Tolkien", "Rs. 749", "5.0", R.drawable.lord_of_rings)
    ) */

                             // Initialise BUTTON for "check Internet connectivity" and add "setOnClickListener" with DIALOG BOX to it !!

    /*    btnCheckInternet = view.findViewById((R.id.btnCheckInternet))

        btnCheckInternet.setOnClickListener {
            if(ConnectionManager().checkConnectivity(activity as Context)){
             // Internet is available
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Success")
                dialog.setMessage("Internet Connection Found")
                dialog.setPositiveButton("Ok"){text, listener ->
                    // Do nothing
                }
                dialog.setNegativeButton("Cancel"){text, listener ->
                    // Do nothing
                }
                dialog.create()
                dialog.show()
        } else {
             // Internet is not available
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection is not Found")
                dialog.setPositiveButton("Ok"){text, listener ->
                    // Do nothing
                }
                dialog.setNegativeButton("Cancel"){text, listener ->
                    // Do nothing
                }
                dialog.create()
                dialog.show()
            }
        } */