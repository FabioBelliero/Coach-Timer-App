package com.example.coachtimer.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coachtimer.ui.main.utils.Lap
import com.example.coachtimer.ui.main.utils.Stats
import com.jjoe64.graphview.series.DataPoint


class SessionViewModel : ViewModel() {

    private var laps: MutableLiveData<ArrayList<Lap>>

    private var times : ArrayList<Double> = ArrayList()
    private var speeds : ArrayList<Double> = ArrayList()
    private var lapsList : ArrayList<Lap> = ArrayList()

    private var previousTime : Double = 0.0
    private var peakSpeed : Double = 0.0

    private var distance : Int = 0
    private var n = 0

    private var data: MutableLiveData<DataPoint>

    private var stats : MutableLiveData<Stats>

    init {
        laps = MutableLiveData()
        data = MutableLiveData()
        stats = MutableLiveData()

        postAll(Stats(0, 0.0, 0.0, 0.0))
    }

    fun setLapDistance(d: Int){
        distance = d
    }

    fun getLapsList(): MutableLiveData<ArrayList<Lap>> {
        return laps
    }

    fun getDatapoint(): MutableLiveData<DataPoint> {
        return data
    }

    fun getStatistics(): MutableLiveData<Stats> {
        return stats
    }

    //Post for lap list and all the stats
    private fun postAll(s : Stats){
        laps.postValue(lapsList)
        stats.postValue(s)
    }

    //Post datapoint for the chart
    private fun postData(d: DataPoint){
        data.postValue(d)
    }

    //Calculate all the stats of the lap and post them
    fun newLap(elapse : Double){
        n += 1

        var time = elapse-previousTime
        previousTime = elapse

        var speed : Double = distance/time

        if (speed>peakSpeed) peakSpeed = speed

        times.add(time)
        speeds.add(speed)

        var avgTime : Double = times.sum()/times.size
        var avgSpeed: Double = speeds.sum()/speeds.size

        lapsList.add(0,Lap(n, time, speed))

        var d = DataPoint(n.toDouble(),time)

        postAll(Stats(n, avgTime, avgSpeed, peakSpeed))
        postData(d)

    }

    //Send the relevant stats to update the db
    fun getPerformance(): Array<Any> {
        val lapN = n
        val peak = peakSpeed

        times.clear()
        speeds.clear()
        lapsList.clear()
        previousTime = 0.0
        peakSpeed = 0.0
        distance = 0
        n = 0

        laps = MutableLiveData()
        data = MutableLiveData()
        stats = MutableLiveData()
        return arrayOf(lapN, peak)
    }



}