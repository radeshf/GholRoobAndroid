package com.mrprojects.gholrob.view.profile

class ProfileImage(
    val image: String,
    val drawableRes: Int,
    val name: String,
    val description: String,
    val price: Int,
    val isComingSoon: Boolean,
){
    var isPurchased: Boolean=false

    val isFree: Boolean
        get() = price == 0
}