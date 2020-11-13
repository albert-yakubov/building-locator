package com.stepashka.buildinglocator2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.stepashka.buildinglocator2.adapter.RecyclerViewAdapter
import com.stepashka.buildinglocator2.dataMVVM.apiMVVM.ApiHelper
import com.stepashka.buildinglocator2.dataMVVM.apiMVVM.ApiServiceImpl
import com.stepashka.buildinglocator2.loginMVVMnetwork.AuthViewModel
import com.stepashka.buildinglocator2.models.PostedMaps
import com.stepashka.buildinglocator2.services.LoginServiceSql
import com.stepashka.buildinglocator2.uiMVVM.ViewModelFactory
import com.stepashka.buildinglocator2.util.AppController
import com.stepashka.buildinglocator2.util.CustomeProgressDialog
import com.stepashka.buildinglocator2.util.Status
import com.stepashka.buildinglocator2.util.toast
import com.stepashka.buildinglocator2.viewmodel.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main2.enterText
import kotlinx.android.synthetic.main.activity_main2.searchButton2
import kotlinx.android.synthetic.main.activity_main2.vRecycle
import kotlinx.android.synthetic.main.activity_main2.view_floatingbutton
import kotlinx.android.synthetic.main.activity_post_map.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(),  NavigationView.OnNavigationItemSelectedListener {

    companion object{
        var map: MutableList<PostedMaps>? = null
        var title: String = ""
        var userid: Long = 12314546
        var ulatitude: Double = 0.0
        var ulongitude: Double = 0.0
        var username4D: String = ""
        lateinit var username: String
        var MAPID: Long = 1
    }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: RecyclerViewAdapter

    private lateinit var disposable2: Disposable

    var customProgressDialog: CustomeProgressDialog? = null

    @Inject
    lateinit var callService: LoginServiceSql


    private var mDrawerLayout: DrawerLayout? = null
    private var mNavigationView: NavigationView? = null
    private var toolbar: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        mDrawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        mNavigationView = findViewById<NavigationView>(R.id.navigationView)
       val mDrawerToggle = ActionBarDrawerToggle(this, mDrawerLayout,toolbar, R.string.open, R.string.close)
        mDrawerLayout!!.addDrawerListener(mDrawerToggle)
        mDrawerToggle.syncState()

        progress_news_feed.visibility = View.INVISIBLE

        customProgressDialog = CustomeProgressDialog(this)

        view_floatingbutton.setOnClickListener {
            val intent = Intent(this, PostMapActivity::class.java)
            startActivity(intent)
        }

        setupUI()
        setupViewModel()
        setupObserver()
        (application as AppController).appComponent.inject2(this)
        searchButton2.setOnClickListener {
            setupObserverForSearchTitle()
            setupObserverForSearch()
            setupUI()
            setupViewModel()
            //setupObserver()
        }
    }
    private fun setupUI() {

        vRecycle.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewAdapter(arrayListOf())
        vRecycle.addItemDecoration(
            DividerItemDecoration(
                vRecycle.context,
                (vRecycle.layoutManager as LinearLayoutManager).orientation
            )
        )
        vRecycle.adapter = adapter
    }
    private fun setupObserver() {
        mainViewModel.getMaps().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    customProgressDialog?.dismiss()
                    it.data?.let { users -> renderList(users) }
                    vRecycle.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    vRecycle.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }


    private fun setupObserverForSearch() {
        customProgressDialog?.dismiss()

        val searchedFor = enterText.text.toString()
        if (searchedFor.isNotEmpty() && searchedFor.contains(searchedFor)) {

            disposable2 = callService.getAddress(searchedFor)

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ address ->
                    if (address.isNotEmpty()) {
                        customProgressDialog?.dismiss()

                        vRecycle.adapter = RecyclerViewAdapter(address)
                    } else {
                        Toast.makeText(this, "Looking...", Toast.LENGTH_SHORT).show()
                    }
                }, { t ->
                    toast("$t")
                })
        }else {
            customProgressDialog?.dismiss()

            Toast.makeText(this, "Please enter something in search", Toast.LENGTH_SHORT).show()
        }

    }
    private fun setupObserverForSearchTitle() {
        customProgressDialog?.dismiss()

        val searchedFor = enterText.text.toString()
        if (searchedFor.isNotEmpty() && searchedFor.contains(searchedFor)) {

            disposable2 = callService.getTitle(searchedFor)

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ title ->
                    if (title.isNotEmpty()) {
                        customProgressDialog?.dismiss()

                        vRecycle.adapter = RecyclerViewAdapter(title)
                    } else {

                        Toast.makeText(this, "looking...", Toast.LENGTH_SHORT).show()
                    }
                }, { t ->
                    customProgressDialog?.dismiss()

                    toast("$t")
                })
        }else {
            customProgressDialog?.dismiss()

            Toast.makeText(this, "Please enter something in search", Toast.LENGTH_SHORT).show()
        }

    }

    private fun renderList(postedMaps: List<PostedMaps>) {

        adapter.addData(postedMaps)
        adapter.notifyDataSetChanged()
    }
    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(MainViewModel::class.java)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }
            R.id.map -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
            R.id.logout -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

        }
        mDrawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }


}