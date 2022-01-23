package de.ndhbr.mytank.utilities

class Constants {
    companion object {
        // Definitions
        const val MAX_TANKS = 16L
        const val MAX_DIFFERENT_TANK_ITEMS = 16L
        const val MAX_ALARMS = 256L

        // Firebase constants
        const val COL_TANKS = "/tanks"
        const val COL_TANK_ITEMS = "/items"
        const val COL_ALARMS = "/alarms"

        // Activity parameters
        const val ACTIVITY_PARAM_TANK = "tank"
        const val ACTIVITY_PARAM_TANK_ID = "tank-id"
        const val ACTIVITY_PARAM_TANK_ITEM = "tank-item"
        const val ACTIVITY_PARAM_ALARM = "alarm"
    }
}