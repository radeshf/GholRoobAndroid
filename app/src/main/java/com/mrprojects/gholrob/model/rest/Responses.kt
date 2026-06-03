package com.mrprojects.gholrob.model.rest

import com.google.gson.annotations.SerializedName
import com.mrprojects.gholrob.model.Attempt
import com.mrprojects.gholrob.model.AttemptHistory
import com.mrprojects.gholrob.model.GameCell
import com.mrprojects.gholrob.model.Payment
import com.mrprojects.gholrob.model.Rating
import com.mrprojects.gholrob.model.User

import ir.radesh.basemodule.helper.RadBaseResponse

class EmptyResponse(): RadBaseResponse()


class StringResponse: RadBaseResponse() {
    @SerializedName("data") val data: String? = null
}

class BooleanResponse: RadBaseResponse() {
    @SerializedName("data") val data: Boolean? = null
}


open class BaseListDataResponse: RadBaseResponse() {
    @SerializedName("page_count") val pageCount: Int = 0
}


class UserResponse: RadBaseResponse() {
    @SerializedName("data") val data: User = User()

}
class GameResponse: RadBaseResponse() {
    @SerializedName("data") val data: Data = Data()
    class Data(){
        @SerializedName("attempt") val attempt: Attempt = Attempt()

    }
}
class GameHistoriesResponse: BaseListDataResponse() {
    @SerializedName("data") val data: List<AttemptHistory> = listOf()
}

class RatingResponse: RadBaseResponse() {
    @SerializedName("data") val data: Data = Data()
    class Data(){
        @SerializedName("me") val me: Rating = Rating()
        @SerializedName("ratings") val ratings: List<Rating> = listOf()
        @SerializedName("season_reward") val seasonReward: SeasonReward = SeasonReward()

        class SeasonReward(){
            @SerializedName("is_active") val isActive: Boolean = false
            @SerializedName("price_pool") val pricePool: String? = ""
            @SerializedName("season_end") val seasonEnd: String? = ""
            @SerializedName("description") val description: String? = ""

        }

    }
}
class ClickOnCellResponse: RadBaseResponse() {
    @SerializedName("data") val data: Data = Data()
    class Data(){
        @SerializedName("is_game_over") val isGameOver: Boolean = false
        @SerializedName("is_game_passed") val isGamePassed: Boolean = false
        @SerializedName("is_duplicate") val isDuplicate: Boolean = false
        @SerializedName("cell") val cell: GameCell? = null
        @SerializedName("attempt") val game: Attempt? = null

    }
}

class FlagCellResponse: RadBaseResponse() {
    @SerializedName("data") val data: Data = Data()
    class Data(){
        @SerializedName("cell") val cell: GameCell? = null
        @SerializedName("attempt") val game: Attempt? = null

    }
}


class StartGameResponse: RadBaseResponse() {
    @SerializedName("data") val data: Data = Data()
    class Data(){
        @SerializedName("attempt") val attempt: Attempt = Attempt()

    }
}


class ContactUsResponse: RadBaseResponse() {
    @SerializedName("data") val data: Data = Data()
    class Data(){
        @SerializedName("whatsapp_support") val whatsappSupport: String = ""
        @SerializedName("telegram_support") val telegramSupport: String = ""
        @SerializedName("phone_support") val phoneSupport: String = ""
        @SerializedName("email") val email: String = ""
        @SerializedName("about_us") val aboutUs: String = ""

    }
}

class PaymentResponse: RadBaseResponse() {
    @SerializedName("data")
    val data: Payment = Payment()
}