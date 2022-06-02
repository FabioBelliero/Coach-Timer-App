package com.example.coachtimer.ui.main.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coachtimer.R
import com.example.coachtimer.data.db.Player
import com.example.coachtimer.ui.main.MainViewModel
import com.example.coachtimer.ui.main.MainViewModelFactory
import com.example.coachtimer.ui.main.recyclerview.MainAdapter
import kotlinx.android.synthetic.main.dialog_input.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainFragment : Fragment(), MainAdapter.RowClickListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter = MainAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //App context
        val app = requireActivity().application

        //RecyclerView
        layoutManager = LinearLayoutManager(context)
        recyclerview_main.layoutManager = layoutManager
        recyclerview_main.adapter = adapter

        //ViewModel
        viewModel = ViewModelProvider(
            requireActivity(),
            MainViewModelFactory(app)
        )[MainViewModel::class.java]

        //Set observer
        viewModel.getList().observe(viewLifecycleOwner, Observer{
            adapter.setData(ArrayList(it))
            adapter.notifyDataSetChanged()
        })
        /*In case of DB
        //First access check
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
         */

        viewModel.getPlayersList()

    }

    //RecyclerView row click listener with the input dialog creation
    override fun onRowClick(player: Player) {
        Log.d("RecyclerView Click", "Clicked ${player.name} ${player.surname}")

        val dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_input, null)
        val builder = AlertDialog.Builder(activity)
            .setView(dialogView)
            .setTitle("${player.name} ${player.surname} Session:")

        val show = builder.show()

        //start button listener (check if input is not empty)
        dialogView.dialog_start_button.setOnClickListener {
            var distance = dialogView.dialog_edit_text.text.toString()
            if (distance != "") {
                Log.d("Click", "input: ${distance.toInt()}")

                //passing the distance
                val bundle = Bundle()
                bundle.putInt("distance", distance.toInt())

                //setting the player
                viewModel.setPlayerSession(player)

                //changing fragment
                val session = SessionFragment()
                session.arguments = bundle
                fragmentManager?.beginTransaction()?.replace(R.id.container, session)?.commit()

                show.dismiss()
            } else{
                Log.d("Click", "input: empty")
            }
        }

    }



}