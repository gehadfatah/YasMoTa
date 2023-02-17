package com.goda.movieapp.domain.pojo


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.goda.movieapp.domain.pojo.ResultReviews
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Review(

    var id: Int,
    var page: Int,
    var results: List<ResultReviews>,
    @SerializedName("total_pages")
    var totalPages: Int,
    @SerializedName("total_results")
    var totalResults: Int
) : Parcelable
