package com.example.coachtimer.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coachtimer.R
import com.example.coachtimer.ui.main.MainViewModel
import com.example.coachtimer.ui.main.MainViewModelFactory
import com.example.coachtimer.ui.main.recyclerview.LeaderboardAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LeaderboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LeaderboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mainViewModel: MainViewModel
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter = LeaderboardAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        mainViewModel = ViewModelProvider(
            requireActivity(),
            MainViewModelFactory(requireActivity().application)
        )[MainViewModel::class.java]

        //Go back callback
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                //go to main
                returnToMain()
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_leaderboard, container, false)

        //Toolbar
        val toolbar = v.findViewById<Toolbar>(R.id.leaderboard_toolbar)
        toolbar.title = "Leaderboard"
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            returnToMain()
        }

        //Toolbar menu
        toolbar.inflateMenu(R.menu.leaderboard_menu)
        toolbar.setOnMenuItemClickListener {
            var id = it.itemId
            if (id == R.id.sort_expl){
                mainViewModel.sortBy(true)
                adapter.setSort(true)
            }else{
                mainViewModel.sortBy(false)
                adapter.setSort(false)
            }
            true
        }

        //RecyclerView
        layoutManager = LinearLayoutManager(context)
        v.findViewById<RecyclerView>(R.id.leaderboard_rv).layoutManager = layoutManager
        v.findViewById<RecyclerView>(R.id.leaderboard_rv).adapter = adapter

        mainViewModel.getListSorted().observe(viewLifecycleOwner, Observer{
            adapter.setData(ArrayList(it))
            adapter.notifyDataSetChanged()
        })

        mainViewModel.sortBy(true)

        //CSV Fab
        val csvFab = v.findViewById<FloatingActionButton>(R.id.csv_fab)
        csvFab.setOnClickListener{
            mainViewModel.export()
        }

        return v
    }

    fun returnToMain(){
        val main = MainFragment()
        fragmentManager?.beginTransaction()?.replace(R.id.container, main)?.commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LeaderboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LeaderboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}