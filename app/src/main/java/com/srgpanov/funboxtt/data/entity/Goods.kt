package com.srgpanov.funboxtt.data.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


// класс представляет сущность полученную из хранилища данных

@Entity(tableName = "goods")
data class Goods(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "price")
    val price: Float,
    @ColumnInfo(name = "quantity")
    val quantity: Int,
    @Ignore
    val listFields: GoodsListItem? = null
) : Parcelable {

    //room constructor
    constructor(id: Long?, name: String, price: Float, quantity: Int) :
            this(id, name, price, quantity, null)

    constructor(source: Parcel) : this(
        source.readValue(Long::class.java.classLoader) as Long?,
        source.readString().toString(),
        source.readFloat(),
        source.readInt(),
        source.readParcelable<GoodsListItem>(GoodsListItem::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeString(name)
        writeFloat(price)
        writeInt(quantity)
        writeParcelable(listFields, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Goods> = object : Parcelable.Creator<Goods> {
            override fun createFromParcel(source: Parcel): Goods = Goods(source)
            override fun newArray(size: Int): Array<Goods?> = arrayOfNulls(size)
        }
    }
}