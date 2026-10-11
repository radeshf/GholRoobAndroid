package com.mrprojects.gholrob.model

import com.google.gson.annotations.SerializedName

class NewVersion {
    @SerializedName("is_available") var isAvailable: Boolean = false
    @SerializedName("is_mandatory") var isMandatory: Boolean = false
    @SerializedName("latest_version") var latestVersion: Int? = null
    @SerializedName("latest_version_name") var latestVersionName: String? = null
    @SerializedName("bazaar_link") var bazaarLink: String? = ""
    @SerializedName("myket_link") var myketLink: String? = ""
    @SerializedName("title") var title: String? = ""
    @SerializedName("msg") var msg: String? = ""



}