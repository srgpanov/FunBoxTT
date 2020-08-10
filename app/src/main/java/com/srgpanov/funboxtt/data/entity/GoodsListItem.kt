package com.srgpanov.funboxtt.data.entity

import android.graphics.drawable.AnimatedImageDrawable
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo

// класс представляет сущность предназанченную для отображения в списке
data class GoodsListItem(
    val name: String,
    val price: String,
    val quantity: String,
    @DrawableRes
    val imageDrawable: Int? = null,
    val showLoading: Boolean = false
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString().toString(),
        source.readString().toString(),
        source.readString().toString(),
        source.readValue(Int::class.java.classLoader) as Int?,
        1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(price)
        writeString(quantity)
        writeValue(imageDrawable)
        writeInt((if (showLoading) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GoodsListItem> =
            object : Parcelable.Creator<GoodsListItem> {
                override fun createFromParcel(source: Parcel): GoodsListItem = GoodsListItem(source)
                override fun newArray(size: Int): Array<GoodsListItem?> = arrayOfNulls(size)
            }
    }
}