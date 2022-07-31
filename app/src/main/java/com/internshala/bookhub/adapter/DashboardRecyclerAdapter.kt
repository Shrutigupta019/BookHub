package com.internshala.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.bookhub.R
import com.internshala.bookhub.activity.DescriptionActivity
import com.internshala.bookhub.model.Book
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val context: Context, val itemList: ArrayList<Book>) : RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>(){

// These 3 methods are responsible for setting a adapter to a list..

    // 1. onCreateViewHolder is responsible for creating the initial 10 ViewHolders.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)

        return DashboardViewHolder(view)
    }

    // 3. getItemCount() stores the total no. of items in the list.
    override fun getItemCount(): Int {
        return itemList.size
    }

    /* 2. # onBindViewHolder() is responsible for the recycling and reusing of the ViewHolders again again acc. to the no. of items we entered.
         #  Here we also need to take care that the correct data goes into correct position.  */
    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book = itemList[position]
        holder.txtBookName.text = book.bookName
        holder.txtBookAuthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating
       // holder.imgBookImage.setImageResource(book.bookImage)
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgBookImage)

                                               // Add "setOnClickListener()" method !!

        holder.llContent.setOnClickListener {
           // Toast.makeText(context,"Clicked on ${holder.txtBookName.text}",Toast.LENGTH_SHORT).show()

            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("book_id",book.bookId)
            context.startActivity(intent)
        }
    }
                                              // CREATE A VIEW HOLDER !!

    class DashboardViewHolder(view: View):RecyclerView.ViewHolder(view) {
          val txtBookName: TextView = view.findViewById(R.id.txtBookName)
          val txtBookAuthor: TextView = view.findViewById(R.id.txtBookAuthor)
          val txtBookPrice: TextView = view.findViewById(R.id.txtBookPrice)
          val txtBookRating: TextView = view.findViewById(R.id.txtBookRating)
          val imgBookImage: ImageView = view.findViewById(R.id.imgBookImage)

          val llContent: LinearLayout = view.findViewById(R.id.llContent) //Initialise Parent id
    }
}