package de.ndhbr.mytank.data

data class User(val userId: Int) {
    override fun toString(): String {
        return userId.toString()
    }
}