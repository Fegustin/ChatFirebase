package com.example.chatfirebase.pojo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val uid: String = "", val email: String = "") : Parcelable
