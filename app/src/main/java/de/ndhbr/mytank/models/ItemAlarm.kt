package de.ndhbr.mytank.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp

data class ItemAlarm(
    var itemAlarmId: String? = "",
    var name: String? = "",
    var userId: String? = "",
    var tankId: String? = "",
    var tankName: String? = "",
    var tankItemId: String? = "",
    var tankItemName: String? = "",
    var days: MutableList<Int>? = ArrayList(),
    var onlyOddWeeks: Boolean? = false,
    var hour: Int = 0,
    val createdAt: Timestamp? = Timestamp.now()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createIntArray()?.toMutableList(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readInt().toInt(),
        parcel.readParcelable(Timestamp::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(itemAlarmId)
        parcel.writeString(name)
        parcel.writeString(userId)
        parcel.writeString(tankId)
        parcel.writeString(tankName)
        parcel.writeString(tankItemId)
        parcel.writeString(tankItemName)
        parcel.writeIntArray(days?.toIntArray())
        parcel.writeValue(onlyOddWeeks)
        parcel.writeInt(hour)
        parcel.writeParcelable(createdAt, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemAlarm> {
        override fun createFromParcel(parcel: Parcel): ItemAlarm {
            return ItemAlarm(parcel)
        }

        override fun newArray(size: Int): Array<ItemAlarm?> {
            return arrayOfNulls(size)
        }
    }

}