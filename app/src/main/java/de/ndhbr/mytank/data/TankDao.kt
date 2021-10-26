package de.ndhbr.mytank.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TankDao {
    private val tankList = mutableListOf<Tank>()
    private val tanks = MutableLiveData<List<Tank>>()

    init {
        tanks.value = tankList
    }

    fun addTank(tank: Tank) {
        tankList.add(tank)
        tanks.value = tankList
    }

    fun getQuotes() = tanks as LiveData<List<Tank>>
}