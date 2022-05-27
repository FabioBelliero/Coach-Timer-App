package com.example.coachtimer.ui.main.fragments

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coachtimer.R
import com.example.coachtimer.ui.main.MainViewModel
import com.example.coachtimer.ui.main.MainViewModelFactory
import com.example.coachtimer.ui.main.recyclerview.MainAdapter
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter = MainAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val app = requireActivity().application

        layoutManager = LinearLayoutManager(context)
        recyclerview_main.layoutManager = layoutManager

        recyclerview_main.adapter = adapter

        viewModel = ViewModelProvider(
            requireActivity(),
            MainViewModelFactory(app)
        )[MainViewModel::class.java]

        viewModel.getList().observe(viewLifecycleOwner, Observer{
            adapter.setData(ArrayList(it))
            adapter.notifyDataSetChanged()
        })

        val sharedPreferences : SharedPreferences = this.requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val firstAccess = sharedPreferences.getBoolean("first_access", true)

        if (firstAccess) {
            Log.d("MainFragment", "First Access, Calling API")

            //call API
            viewModel.getPlayersList()

            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean("first_access", false)
            editor.apply()
        }
        Log.d("MainFragment", "OK")

    }

}