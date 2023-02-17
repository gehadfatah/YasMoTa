package com.goda.movieapp.view

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.goda.movieapp.R
import com.goda.movieapp.common.ApplicationIntegration
import com.goda.movieapp.domain.pojo.MovieResult
import com.goda.movieapp.util.ConnectionLiveData
import com.goda.movieapp.util.showLongToast
import com.goda.movieapp.util.showShortToast
import com.goda.movieapp.view.ui.home.HomeFragment
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.view.*
import java.util.*

class HomeActivity : DaggerAppCompatActivity(), NavController.OnDestinationChangedListener,
    SharedPreferences.OnSharedPreferenceChangeListener {
    private var snack: Snackbar? = null
    private var thismenu: Menu? = null

    lateinit var sharedPreferences: SharedPreferences
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_popular,
                R.id.navigation_child,
                R.id.navigation_favorite
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener(this)
        navView.setupWithNavController(navController)
        updateNavViewMenuUI()
        snack = Snackbar.make(
            findViewById(android.R.id.content),
            resources.getString(R.string.no_internet_connection),
            Snackbar.LENGTH_INDEFINITE
        )
        setNetworkListner()

    }
    private fun setNetworkListner() {
        /* Live data object and setting an observer on it to monitor connection status to update countries  */
        val connectionLiveData =
            ConnectionLiveData(ApplicationIntegration.getApplication())
        connectionLiveData.observe(this, Observer { connection ->
            /* every time connection state changes, we'll be notified and can perform action accordingly */
            if (connection != null && !isDestroyed) {
                if (connection.isConnected) {
                   // RelOffline.visibility = View.GONE;
                    val navController: NavController =
                       findNavController(R.id.nav_host_fragment)
                    navController.run {

                    }
                } else {
                    //RelOffline.visibility = View.VISIBLE;

                    this.showLongToast(getString(R.string.no_internet_connection))

                    Log.d("d", "onChanged: ");

                }
            }
        })

    }
    override fun onResume() {
        super.onResume()
        sharedPreferences
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        thismenu=menu
        return configureToolbar(menu)
    }

    private fun configureToolbar(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.common_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                navController.navigate(R.id.navigateToSearch)
                true
            }
            R.id.action_favorites -> {
                navController.navigate(R.id.navigateToFavourite)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateNavViewMenuUI() {
        if (sharedPreferences.contains("favorite_menu"))
            changeMenuLabel(
                R.id.navigation_favorite,
                sharedPreferences.getString("favorite_menu", "")!!
            )
        if (sharedPreferences.contains("child_menu"))
            changeMenuLabel(R.id.navigation_child, sharedPreferences.getString("child_menu", "")!!)
        if (sharedPreferences.contains("popular_menu"))
            changeMenuLabel(
                R.id.navigation_popular,
                sharedPreferences.getString("popular_menu", "")!!
            )
        if (sharedPreferences.contains("favorite_visible"))
            changeMenuVisibility(
                R.id.navigation_favorite,
                sharedPreferences.getBoolean("favorite_visible", true)
            )
        if (sharedPreferences.contains("child_visible"))
            changeMenuVisibility(
                R.id.navigation_child,
                sharedPreferences.getBoolean("child_visible", true)
            )
        if (sharedPreferences.contains("popular_visible"))
            changeMenuVisibility(
                R.id.navigation_popular,
                sharedPreferences.getBoolean("popular_visible", true)
            )
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    @SuppressLint("MissingPermission")
    private fun isNetworkConnected(): Boolean {
        val connMgr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return if (networkInfo != null && networkInfo.isConnected) {
            true
        } else {
            showSnackBar()
            false
        }
    }


    fun showSnackBar() {

        snack?.setAction("CLOSE", View.OnClickListener { snack?.dismiss() })
        snack?.setActionTextColor(resources.getColor(R.color.grend))
        snack?.show()
    }

    private fun getNoResult() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
        builder.setMessage("not found")
        val alertDialog = builder.create()
        alertDialog.show()
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                alertDialog.dismiss()
                timer.cancel()
            }
        }, 2000)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.detail_fragment, R.id.splash_fragment, R.id.navigation_search -> {
                toolbar.visibility = View.GONE
            }
            R.id.navigation_home->{

                toolbar.visibility = View.VISIBLE
                toolbar.tvToolbarTitle.text = destination.label
                hideMenuItems(true)
            }
            else -> {
                hideMenuItems()
                toolbar.isVisible=true
                toolbar.tvToolbarTitle.text = destination.label
            }
        }

    }

    private fun hideMenuItems(enabled:Boolean=false) {

        val menuItemFav = thismenu?.findItem( R.id.action_favorites)
        val menuItemSearch =thismenu?.findItem( R.id.action_filter)
        menuItemFav?.isVisible=enabled
        menuItemSearch?.isVisible=enabled

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            "favorite_menu" -> changeMenuLabel(
                R.id.navigation_favorite,
                sharedPreferences!!.getString(key, "")!!
            )
            "child_menu" -> changeMenuLabel(
                R.id.navigation_child,
                sharedPreferences!!.getString(key, "")!!
            )
            "popular_menu" -> changeMenuLabel(
                R.id.navigation_popular,
                sharedPreferences!!.getString(key, "")!!
            )
            "favorite_visible" -> changeMenuVisibility(
                R.id.navigation_favorite,
                sharedPreferences!!.getBoolean(key, true)
            )
            "child_visible" -> changeMenuVisibility(
                R.id.navigation_child,
                sharedPreferences!!.getBoolean(key, true)
            )
            "popular_visible" -> changeMenuVisibility(
                R.id.navigation_popular,
                sharedPreferences!!.getBoolean(key, true)
            )
        }
    }


    private fun changeMenuLabel(menuId: Int, label: String) {
        val menuItem = nav_view.menu.findItem(menuId)
        menuItem.title = label
    }

    private fun changeMenuVisibility(menuId: Int, visibility: Boolean) {
        val menuItem = nav_view.menu.findItem(menuId)
        menuItem.isVisible = visibility
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


}
/*  private fun Search(query: String): String? {
       RetrofitClient.getInstance()
           .getMovieService().searchForMovies(query, API_KEY)
           .enqueue(object : Callback<MovieApiResponse?> {
               override fun onResponse(
                   call: Call<MovieApiResponse?>,
                   response: Response<MovieApiResponse?>
               ) {
                   if (response.body() != null) {
                       searchedMovieList = response.body().getMovies()
                       if (searchedMovieList.isEmpty()) {
                           getNoResult()
                       }
                       Log.d("onResponse --> ", searchedMovieList.size.toString() + " Movies")
                       searchAdapter = SearchAdapter(
                           applicationContext,
                           searchedMovieList,
                           object : SearchAdapterOnClickHandler() {
                               fun onClick(movie: Movie?) {
                                   val intent =
                                       Intent(this@MainActivity, MovieActivity::class.java)
                                   intent.putExtra(MOVIE, movie)
                                   startActivity(intent)
                               }
                           })
                   }
                   recyclerView.setAdapter(searchAdapter)
               }

               override fun onFailure(call: Call<MovieApiResponse?>, t: Throwable) {
                   Log.d("onFailure --> ", " Failed to get movies")
               }
           })
       return query
   }*/

/*   override fun onCreateOptionsMenu(menu: Menu): Boolean {
       val inflater = menuInflater
       inflater.inflate(R.menu.search, menu)
       val searchViewItem = menu.findItem(R.id.search)
       val searchView = searchViewItem.actionView as SearchView
       searchView.queryHint = resources.getString(R.string.search_for_movies)
       searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
           override fun onQueryTextSubmit(query: String): Boolean {
               if (isNetworkConnected()) {
                   Search(query)
               }
               return false
           }

           override fun onQueryTextChange(newText: String): Boolean {
               if (isNetworkConnected()) {
                   Search(newText)
               }
               return false
           }
       })
       searchView.setOnCloseListener {
           if (searchedMovieList != null) {
               searchAdapter?.clear()
               //loadMovies()
           }
           false
       }
       return super.onCreateOptionsMenu(menu)
   }*/