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
import kotlinx.coroutines.launch
import org.json.JSONArray

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var players: MutableLiveData<List<Player>>
    private val repository: Repository
    private val app = application

    init {
        players = MutableLiveData()
        val dao = PlayersDB.getDB(application).playersDao()
        repository = Repository(dao)

        postAll()
    }

    private fun postAll() = viewModelScope.launch(Dispatchers.IO){
        players.postValue(repository.getPlayers())
    }

    fun getList(): MutableLiveData<List<Player>> {
        return players
    }

    fun insertPlayer(player: Player) = viewModelScope.launch(Dispatchers.IO){
        repository.insertPlayer(player)
        postAll()
    }

    fun updatePlayer(player: Player) = viewModelScope.launch(Dispatchers.IO){
        repository.updatePlayer(player)
        postAll()
    }

    //API Call to get the players list and put them into the DB

    fun getPlayersList() = viewModelScope.launch(Dispatchers.IO){
        val queue = Volley.newRequestQueue(app)
        val url = "https://randomuser.me/api/?seed=empatica&inc=name,picture&gender=male&results=10&noinfo"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("Volley", "API Responding")
                var results = response.getJSONArray("results")

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
                // TODO: Handle error
            }
        )

        queue.add(jsonObjectRequest)
    }

    private fun jsonInDB(results: JSONArray){
        for (i in 0 until results.length()){
            var obj = results.getJSONObject(i)
            var name = obj.getJSONObject("name")
            var picture = obj.getJSONObject("picture")

            insertPlayer(Player(i,
                name.getString("first"),
                name.getString("last"),
                picture.getString("large"),
                0.0,
                0
            ))
        }

        postAll()
    }
}