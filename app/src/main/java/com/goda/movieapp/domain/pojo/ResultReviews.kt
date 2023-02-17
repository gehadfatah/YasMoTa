package com.goda.movieapp.domain.pojo


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "ReviewLocal")

data class ResultReviews(
    var author: String,
    var content: String,
    @PrimaryKey
    var id: String,
    var url: String
) : Parcelable
