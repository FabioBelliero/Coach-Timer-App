package com.example.coachtimer.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.coachtimer.data.Repository
import com.example.coachtimer.data.db.Player
import com.example.coachtimer.data.db.PlayersDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var players: MutableLiveData<List<Player>>
    private val repository: Repository
    private val app = application

    lateinit var playerSelected: Player

    private var sorted: MutableLiveData<List<Player>>
    private var l : List<Player> = listOf()

    private lateinit var listCSV: List<Player>

    init {
        players = MutableLiveData()
        val dao = PlayersDB.getDB(application).playersDao()
        repository = Repository(dao)

        sorted = MutableLiveData()

        postAll()

    }

    //Post the value when data changes
    private fun postAll() = viewModelScope.launch(Dispatchers.IO){
        l = repository.getPlayers()
        players.postValue(l)
    }

    //Gets the observable list
    fun getList(): MutableLiveData<List<Player>> {
        return players
    }

    //Insert a new player
    fun insertPlayer(player: Player) = viewModelScope.launch(Dispatchers.IO){
        repository.insertPlayer(player)
        postAll()
    }

    //Update an existing player
    fun updatePlayer(player: Player) = viewModelScope.launch(Dispatchers.IO){
        repository.updatePlayer(player)
        //sync(player)
        postAll()
    }

    fun setPlayerSession(player: Player){
        playerSelected = player
    }

    //Called to update the stats of a player
    fun setPerformance(performance : Array<Any>){
        if(playerSelected.explosiveness < performance[1] as Double) {
            playerSelected.explosiveness = performance[1] as Double
        }
        if(playerSelected.endurance < performance[0] as Int) {
            playerSelected.endurance = performance[0] as Int
        }
        updatePlayer(playerSelected)
    }

    //API Call to get the players list and put them into the DB
    fun getPlayersList() = viewModelScope.launch(Dispatchers.IO){
        val queue = Volley.newRequestQueue(app)
        val url = "https://randomuser.me/api/?seed=empatica&inc=name,picture&gender=male&results=10&noinfo"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("Volley", "API Responding")

                val results = response.getJSONArray("results")

                for (i in 0 until results.length()){
                    var obj = results.getJSONObject(i)
                    var name = obj.getJSONObject("name")
                    var picture = obj.getJSONObject("picture")

                    insertPlayer(Player(i+1,
                        name.getString("first"),
                        name.getString("last"),
                        picture.getString("large"),
                        0.0,
                        0
                    ))
                }

                postAll()
            },
            { error ->
                Log.d("Volley", "Error: $error")
            }
        )

        queue.add(jsonObjectRequest)
    }

    //Sorting functions for leaderboard
    fun getListSorted(): MutableLiveData<List<Player>> {
        return sorted
    }

    fun sortBy(expl : Boolean){
        var list : List<Player>

        if (expl){
            list = l.sortedBy { it.explosiveness }
        } else {
            list = l.sortedBy { it.endurance }
        }

        listCSV = list.reversed()
        postSort(list.reversed())
    }

    private fun postSort(list: List<Player>){
        sorted.postValue(list)
    }

    //CSV functionality (job that prints the list in the desired order)
    fun export(){
        val job = viewModelScope.launch {
            delay(1000)
            for (p in listCSV){
                Log.d("CSV", "Player: ${p.name} ${p.surname} Explosiveness: ${p.explosiveness} Endurance: ${p.endurance}")
            }
        }

    }

    //Sync functionality
    private fun sync(player: Player)= viewModelScope.launch(Dispatchers.IO){
        val queue = Volley.newRequestQueue(app)
        val url = "http://empatica-homework.free.beeceptor.com/trainings"

        var toSend : JSONObject = JSONObject()
        toSend.put("first", player.name)
        toSend.put("last", player.surname)
        toSend.put("expl", player.explosiveness)
        toSend.put("end", player.endurance)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, toSend,
            { response ->
                Log.d("Volley", response.toString())
            },
            { error ->
                Log.d("Volley", "Error: $error")
            }
        )

        queue.add(jsonObjectRequest)
    }
}