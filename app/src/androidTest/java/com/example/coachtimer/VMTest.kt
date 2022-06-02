package com.example.coachtimer

import android.app.ActivityManager
import android.app.Application
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.coachtimer.ui.main.MainViewModel
import com.example.coachtimer.ui.main.MainViewModelFactory
import com.example.coachtimer.ui.main.SessionViewModel
import com.example.coachtimer.ui.main.utils.Lap
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.coroutines.coroutineContext

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class VMTest {

    private lateinit var activity: Context
    private lateinit var mainVM : MainViewModel
    private lateinit var sessionVM: SessionViewModel

    @Before
    fun setup() {

    }

    @Test fun test(){


    }



    //Main viewmodel testing

    //Session viewmodel testing


}