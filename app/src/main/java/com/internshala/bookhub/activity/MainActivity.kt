package com.internshala.bookhub.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.internshala.bookhub.*
import com.internshala.bookhub.fragment.AboutAppFragment
import com.internshala.bookhub.fragment.DashboardFragment
import com.internshala.bookhub.fragment.FavouritesFragment
import com.internshala.bookhub.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView

    lateinit var sharedPreferences: SharedPreferences

    var previousMenuItem: MenuItem? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)

        setUpToolbar() // CALL setUpToolbar() method
        openDashboard() // CALL openDashboard() method

                                               // ADD CLICK LISTENER METHOD!!

        navigationView.setNavigationItemSelectedListener {
                                                   // ADD HIGHLIGHT ON THE BUTTON WE "CLICKED !!"
            if(previousMenuItem !=  null){
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it    // Till here (Highlight)

            when(it.itemId){
                R.id.dashboard ->{
                 //  Toast.makeText(this@MainActivity,"Clicked on Dashboard",Toast.LENGTH_SHORT).show()
                    openDashboard() // As we already created a separate function for it.
                    supportActionBar?.title = "Dashboard"
                    drawerLayout.closeDrawers()
                }
                R.id.favourites ->{
                  //  Toast.makeText(this@MainActivity,"Clicked on Favourites",Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FavouritesFragment()
                        )
                     //   .addToBackStack("Favourites")
                        .commit()

                    supportActionBar?.title = "Favourites"
                    drawerLayout.closeDrawers()
                }
                R.id.profile ->{
                 //   Toast.makeText(this@MainActivity,"Clicked on Profile",Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            ProfileFragment()
                        )
                     //   .addToBackStack("Profile")
                        .commit()

                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.aboutApp -> {
                    //   Toast.makeText(this@MainActivity,"Clicked on About App",Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            AboutAppFragment()
                        )
                        //   .addToBackStack("About App")
                        .commit()

                    supportActionBar?.title = "About App"
                    drawerLayout.closeDrawers()
                }
                R.id.logOut -> {
                  //  Toast.makeText(this@MainActivity, "Clicked on Log Out", Toast.LENGTH_SHORT).show()


                    drawerLayout.closeDrawers()

                    val dialog = AlertDialog.Builder(this@MainActivity as Context)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Are you sure you want to exit?")
                    dialog.setPositiveButton("No") { text, listener ->
                        // Do nothing
                    }
                    dialog.setNegativeButton("Yes") { text, listener ->

                        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

                        sharedPreferences.edit().clear().apply()
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        finish()
                        Volley.newRequestQueue(this).cancelAll(this::class.java.simpleName)
                        ActivityCompat.finishAffinity(this)

                        // USE OF SHARED PREFERENCES !!
                      /*  sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

                        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false)
                        setContentView(R.layout.activity_login)
                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()

                        if(isLoggedIn){
                            val intent = Intent(this@MainActivity,LoginActivity::class.java)
                            startActivity(intent)
                            finish()

                        } */

                     /*   val intent = Intent(this@MainActivity,LoginActivity::class.java)
                        startActivity(intent)
                        finish() */
                       // ActivityCompat.finishAffinity(this@MainActivity as Activity)
                    }
                    dialog.create()
                    dialog.show()
                }
            }
            return@setNavigationItemSelectedListener true
        }


                                                            // FOR TOOLBAR SETUP!!

       // Use of ActionBarDrawerToggle for " CREATING A HAMBURGER ICON "
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle) // This will "set a clickListener icon" an ActionBarDrawerToggle
        actionBarDrawerToggle.syncState() // This line is use to "change Hamburger icon to the Back-Arrow icon and vice-versa"
    }

    fun setUpToolbar() {
         setSupportActionBar(toolbar)
         supportActionBar?.title = "Toolbar Title"
         supportActionBar?.setHomeButtonEnabled(true)       // This will create an ARROW button "Not a Hamburger icon"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
     }

    // To open the drawer from one side(left) so that ActionBarDrawerToggle work when we click on it
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if(id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START) // Make the drawer open from the LEFT side of an app
        }
        return super.onOptionsItemSelected(item)
    }
                               // Whenever we  Open our app "DASHBOARD FRAGMENT will Open" BY DEFAULT !!
    fun openDashboard() {
        val fragment = DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame,fragment)
        transaction.commit()
        supportActionBar?.title = "Dashboard"
        navigationView.setCheckedItem(R.id.dashboard) // When we open our app dashboard screen will appear with "HIGHLIGHT" on DASHBOARD menu item.
      }
                                                 // Work " onBackPressed Button " !!
    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)

        when(frag){
            !is DashboardFragment -> openDashboard()

            else -> super.onBackPressed()
        }
    }
}
