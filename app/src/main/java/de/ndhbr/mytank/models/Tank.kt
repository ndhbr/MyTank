package de.ndhbr.mytank.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import java.util.Date

data class Tank(
    @get:Exclude var tankId: String? = "",
    var userId: String? = "",
    var name: String? = "",
    var size: Int? = 0,
    var hasImage: Boolean? = false,
    var existsSince: Date? = Timestamp.now().toDate(),
    val createdAt: Timestamp? = Timestamp.now()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        Date(parcel.readLong()),
        parcel.readParcelable(Timestamp::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(tankId)
        parcel.writeString(userId)
        parcel.writeString(name)
        parcel.writeInt(size!!)
        parcel.writeByte(if (hasImage == true) 1.toByte() else 0.toByte())
        existsSince?.let { parcel.writeLong(it.time) }
        parcel.writeParcelable(createdAt, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Tank> {
        override fun createFromParcel(parcel: Parcel): Tank {
            return Tank(parcel)
        }

        override fun newArray(size: Int): Array<Tank?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return name ?: "No name"
    }
}