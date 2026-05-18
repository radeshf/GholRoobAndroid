package com.mrprojects.gholrob.helper

//import com.google.firebase.analytics.FirebaseAnalytics
//
//
//fun FirebaseAnalytics.appOpened(){
//    logEvent(FirebaseAnalytics.Event.APP_OPEN, null)
//}
//
//fun FirebaseAnalytics.stickerPackViewed(packId: String, name: String){
//    val bundle = Bundle().apply {
//        putString(FirebaseAnalytics.Param.ITEM_ID, packId)
//        putString(FirebaseAnalytics.Param.ITEM_NAME, name)
//        putString(FirebaseAnalytics.Param.CONTENT_TYPE, "sticker_pack")
//    }
//    logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)
//}
//
//fun FirebaseAnalytics.stickerPackAdded(packId: String, name: String){
//    val bundle = Bundle().apply {
//        putString(FirebaseAnalytics.Param.ITEM_ID, packId)
//        putString(FirebaseAnalytics.Param.ITEM_NAME, name)
//        putString(FirebaseAnalytics.Param.CONTENT_TYPE, "sticker_pack")
//    }
//    logEvent("stickerPackAdded", bundle)
//}
//
//fun FirebaseAnalytics.stickerPackAddFailed(packId: String, name: String, error: String){
//    val bundle = Bundle().apply {
//        putString(FirebaseAnalytics.Param.ITEM_ID, packId)
//        putString(FirebaseAnalytics.Param.ITEM_NAME, name)
//        putString(FirebaseAnalytics.Param.CONTENT_TYPE, "sticker_pack")
//        putString("error_message", error)
//    }
//    logEvent("stickerPackAddFailed", bundle)
//}
//
//
//fun FirebaseAnalytics.buttonClicked(buttonName: String){
//    val bundle = Bundle().apply {
//        putString("button_name", buttonName)
//    }
//    logEvent("button_click", bundle)
//}
