package de.ndhbr.mytank.models

data class User(
    var userId: String?
) {
    @Override
    override fun toString(): String {
        return this.userId ?: ""
    }
}