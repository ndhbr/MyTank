package de.ndhbr.mytank.interfaces

import de.ndhbr.mytank.models.Tank

interface TankListener {
    fun onTankClick(tank: Tank)
    fun onTankLongPress(tank: Tank): Boolean
}