package dev.jorgecastillo.compose.app.ui.composables.lazypane

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.compose.foundation.ExperimentalFoundationApi

@ExperimentalFoundationApi
fun getDefaultLazyLayoutKey(indexRow: Int, indexColumn: Int): Any =
    DefaultLazyKey(indexRow, indexColumn)

@SuppressLint("BanParcelableUsage")
private data class DefaultLazyKey(
    private val indexRow: Int,
    private val indexColumn: Int
) : Parcelable {
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString("$indexRow.$indexColumn")
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR: Parcelable.Creator<DefaultLazyKey> =
            object : Parcelable.Creator<DefaultLazyKey> {
                override fun createFromParcel(parcel: Parcel) =
                    parcel.readString()!!.split(".").let {
                        DefaultLazyKey(it[0].toInt(), it[1].toInt())
                    }

                override fun newArray(size: Int) = arrayOfNulls<DefaultLazyKey?>(size)
            }
    }
}