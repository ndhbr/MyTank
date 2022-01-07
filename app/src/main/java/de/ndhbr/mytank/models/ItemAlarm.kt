package de.ndhbr.mytank.models

import android.os.Parcel
import android.os.Parcelable

data class ItemAlarm(
    var itemAlarmId: String? = "",
    var userId: String? = "",
    var tankId: String? = "",
    var tankItemId: String? = "",
    var days: List<String>? = ArrayList(),
    var onlyOddWeeks: Boolean? = false,
    var hour: Short = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readInt().toShort()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(itemAlarmId)
        parcel.writeString(userId)
        parcel.writeString(tankId)
        parcel.writeString(tankItemId)
        parcel.writeStringList(days)
        parcel.writeValue(onlyOddWeeks)
        parcel.writeInt(hour.toInt())
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