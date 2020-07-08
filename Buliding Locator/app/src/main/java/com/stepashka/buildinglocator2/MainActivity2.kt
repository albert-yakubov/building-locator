package com.stepashka.buildinglocator2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stepashka.buildinglocator2.adapter.RecyclerViewAdapter
import com.stepashka.buildinglocator2.dataMVVM.apiMVVM.ApiHelper
import com.stepashka.buildinglocator2.dataMVVM.apiMVVM.ApiServiceImpl
import com.stepashka.buildinglocator2.models.PostedMaps
import com.stepashka.buildinglocator2.uiMVVM.ViewModelFactory
import com.stepashka.buildinglocator2.util.Status
import com.stepashka.buildinglocator2.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main2.*


class MainActivity2 : AppCompatActivity() {


    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
        setupViewModel()
        setupObserver()
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
        mainViewModel.getUsers().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar?.visibility = View.GONE
                    it.data?.let { users -> renderList(users) }
                    vRecycle.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    progressBar?.visibility = View.VISIBLE
                    vRecycle.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    progressBar?.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
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

}