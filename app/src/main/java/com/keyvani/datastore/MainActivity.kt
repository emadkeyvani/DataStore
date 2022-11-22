package com.keyvani.datastore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.coroutineScope
import com.keyvani.datastore.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivityMainBinding

    //Datastore
    private val dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by
    preferencesDataStore("userinfo")
    private val userName = stringPreferencesKey("USERNAME")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initViews
        binding.apply {
            btnSave.setOnClickListener {
                CoroutineScope(IO).launch {
                    storeUserInfo(edtInfo.text.toString())
                    edtInfo.setText("")

                }
            }
            lifecycle.coroutineScope.launchWhenCreated {
                getInfo().collect {
                    tvInfo.text = it

                }
            }

        }
    }

    private suspend fun storeUserInfo(name: String) {
        dataStore.edit {
            it[userName] = name
        }

    }

    private fun getInfo() = dataStore.data.map {
        it[userName] ?: ""
    }
}