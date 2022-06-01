package com.example.coachtimer.ui.main.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coachtimer.R
import com.example.coachtimer.ui.main.MainViewModel
import com.example.coachtimer.ui.main.MainViewModelFactory
import com.example.coachtimer.ui.main.SessionViewModel
import com.example.coachtimer.ui.main.recyclerview.SessionAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SessionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SessionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var sessionViewModel: SessionViewModel
    private lateinit var mainViewModel: MainViewModel
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter = SessionAdapter()
    lateinit var chrono : Chronometer
    lateinit var graph: GraphView
    lateinit var series: LineGraphSeries<DataPoint>
    lateinit var avg: LineGraphSeries<DataPoint>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        //Viewmodels
        sessionViewModel = ViewModelProvider(requireActivity())[SessionViewModel::class.java]

        mainViewModel = ViewModelProvider(
            requireActivity(),
            MainViewModelFactory(requireActivity().application)
        )[MainViewModel::class.java]

        //Go back callback
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Log.d("test", "heh")
                //save data
                stopSession()
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
        //args passed
        val args = this.arguments
        sessionViewModel.setLapDistance(args!!.getInt("distance"))

        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_session, container, false)

        //Toolbar
        val toolbar = v.findViewById<Toolbar>(R.id.toolbar_session)
        toolbar.title = args.get("distance").toString() + " meters session"
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            Log.d("test", "heh")
            //save data
            stopSession()
            //go to main
            returnToMain()
        }

        //Stats
        v.findViewById<TextView>(R.id.name_text).text = mainViewModel.playerSelected.name
        v.findViewById<TextView>(R.id.surname_text).text = mainViewModel.playerSelected.surname
        v.findViewById<TextView>(R.id.explosiveness_text).text = "Expl. : " + String.format("%.3f", mainViewModel.playerSelected.explosiveness) + " m/s"
        v.findViewById<TextView>(R.id.endurance_text).text = "End. : " + mainViewModel.playerSelected.endurance.toString() + " laps"

        //RecyclerView
        layoutManager = LinearLayoutManager(context)
        v.findViewById<RecyclerView>(R.id.recyclerview_session).layoutManager = layoutManager
        v.findViewById<RecyclerView>(R.id.recyclerview_session).adapter = adapter

        sessionViewModel.getLapsList().observe(viewLifecycleOwner, Observer{
            adapter.setData(ArrayList(it))
            adapter.notifyDataSetChanged()
        })

        //Chronometer
        chrono = v.findViewById(R.id.chronometer)

        chrono.base = SystemClock.elapsedRealtime();
        chrono.start();


        //Lap fab
        val lapFab = v.findViewById<FloatingActionButton>(R.id.lap_fab)
        lapFab.setOnClickListener{
            val elapsedMillis: Long = SystemClock.elapsedRealtime() - chrono.base
            val time = elapsedMillis.toDouble()/1000

            sessionViewModel.newLap(time)
        }

        //Stop fab
        val stopFab = v.findViewById<FloatingActionButton>(R.id.stop_fab)
        stopFab.setOnClickListener{
            //save data
            stopSession()

            //go to leaderboard
            val leaderboard = LeaderboardFragment()
            fragmentManager?.beginTransaction()?.replace(R.id.container, leaderboard)?.commit()
        }

        //Chart
        graph = v.findViewById(R.id.graph)
        series= LineGraphSeries(arrayOf())
        avg= LineGraphSeries(arrayOf())

        val t = 10

        series.title = "Random Curve 1";
        series.color = Color.BLUE;
        series.isDrawDataPoints = true;
        series.dataPointsRadius = t.toFloat();
        series.thickness = 8;

        graph.viewport.isXAxisBoundsManual = true;
        graph.viewport.setMinX(0.0);
        graph.viewport.setMaxX(10.0);
        graph.viewport.isScrollable = true;
        graph.gridLabelRenderer.horizontalAxisTitle = "Laps"
        graph.gridLabelRenderer.verticalAxisTitle = "Time (sec)"

        graph.addSeries(series)
        graph.addSeries(avg)

        sessionViewModel.getDatapoint().observe(viewLifecycleOwner, Observer {
            series.appendData(it, false , 100)
        })

        //Stats observer
        sessionViewModel.getStatsObservable().observe(viewLifecycleOwner, Observer {

            //lap number
            v.findViewById<TextView>(R.id.laps_text).text = it.n.toString()

            //avg time/lap
            var mil : Int = (it.avgTimeLap.mod(1.0)*1000).toInt()
            var min : Int = (it.avgTimeLap/60).toInt()
            var sec : Int = it.avgTimeLap.mod(60.0).toInt()
            v.findViewById<TextView>(R.id.time_lap_text).text = "$min:$sec:$mil"

            //avg speed
            v.findViewById<TextView>(R.id.speed_avg_text).text = String.format("%.3f", it.avgSpeed) + " m/s"

            //top speed
            v.findViewById<TextView>(R.id.speed_peak_text).text = String.format("%.3f", it.topSpeed) + " m/s"

            //graph mean
            avg.resetData(arrayOf(
                DataPoint(0.0,it.avgTimeLap), DataPoint(series.highestValueX +1 ,it.avgTimeLap)
            ))
        })
        return v
    }

    fun stopSession(){
        mainViewModel.setPerformance(sessionViewModel.getPerformance())
    }

    fun returnToMain(){
        val main = MainFragment()
        fragmentManager?.beginTransaction()?.replace(R.id.container, main)?.remove(this)?.commit()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SessionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SessionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}