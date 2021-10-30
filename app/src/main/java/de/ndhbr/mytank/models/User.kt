package de.ndhbr.mytank.models

data class User(val userId: Int) {
    override fun toString(): String {
        return userId.toString()
    }
}