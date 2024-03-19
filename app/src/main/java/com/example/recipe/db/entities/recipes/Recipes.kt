package com.example.recipe.db.entities.recipes

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "ingredients")
    var ingredients: String?,

    @ColumnInfo(name = "description")
    var description: String?,

    @ColumnInfo(name = "servings")
    var servings: String?,

    @ColumnInfo(name = "total_time")
    var totalTime: Int,

    @ColumnInfo(name = "img_src")
    var imgSrc: String?,

    @ColumnInfo(name = "pub_date")
    var pubDate: String?,

    @ColumnInfo(name = "cost")
    var cost: String,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(ingredients)
        parcel.writeString(description)
        parcel.writeString(servings)
        parcel.writeInt(totalTime)
        parcel.writeString(imgSrc)
        parcel.writeString(pubDate)
        parcel.writeString(cost)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Recipe> {
        override fun createFromParcel(parcel: Parcel): Recipe {
            return Recipe(parcel)
        }

        override fun newArray(size: Int): Array<Recipe?> {
            return arrayOfNulls(size)
        }
    }
}
