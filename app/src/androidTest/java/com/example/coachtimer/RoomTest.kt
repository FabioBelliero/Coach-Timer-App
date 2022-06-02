package com.example.coachtimer

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.coachtimer.data.db.Player
import com.example.coachtimer.data.db.PlayersDAO
import com.example.coachtimer.data.db.PlayersDB
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class RoomTest {

    private lateinit var db: PlayersDB
    private lateinit var dao: PlayersDAO

    @Before
    fun setup(){
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PlayersDB::class.java
        ).allowMainThreadQueries().build()

        dao = db.playersDao()
    }

    @After
    fun teardown(){
        db.close()
    }

    @Test
    fun insert() = runTest{
        val p = Player(1, "first", "last", "url", 0.0, 0)
        dao.insertPlayer(p)

        val all = dao.getAll()

        assertThat(all[0].name == "first").isTrue()
    }

    @Test
    fun update() = runTest{
        val p = Player(1, "first", "last", "url", 0.0, 0)
        dao.insertPlayer(p)
        p.name = "test"
        dao.updatePlayer(p)

        val all = dao.getAll()

        assertThat(all[0].name == "test").isTrue()
    }


}