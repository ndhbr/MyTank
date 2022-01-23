package de.ndhbr.mytank.interfaces

import de.ndhbr.mytank.models.TankItem

interface TankItemListener {
    fun onTankItemClick(tankItem: TankItem)
    fun onTankItemLongPress(tankItem: TankItem): Boolean
}