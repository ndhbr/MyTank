package de.ndhbr.mytank.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import de.ndhbr.mytank.enum.TankItemType
import java.util.*

data class TankItem(
    @get:Exclude var tankItemId: String? = "",
    var name: String? = "",
    var type: TankItemType? = TankItemType.FISH,
    var count: Int = 1,
    var hasImage: Boolean? = false,
    var existsSince: Date? = Timestamp.now().toDate(),
    val createdAt: Timestamp? = Timestamp.now()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        TankItemType.valueOf(parcel.readString()!!),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        Date(parcel.readLong()),
        parcel.readParcelable(Timestamp::class.java.classLoader)
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(tankItemId)
        parcel.writeString(name)
        parcel.writeString(type?.name)
        parcel.writeInt(count)
        parcel.writeByte(if (hasImage == true) 1.toByte() else 0.toByte())
        existsSince?.let { parcel.writeLong(it.time) }
        parcel.writeParcelable(createdAt, flags)
    }

    companion object CREATOR : Parcelable.Creator<TankItem> {
        override fun createFromParcel(parcel: Parcel): TankItem {
            return TankItem(parcel)
        }

        override fun newArray(size: Int): Array<TankItem?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return name ?: "No name"
    }
}