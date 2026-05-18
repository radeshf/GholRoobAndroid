package com.mrprojects.gholrob.repository.api

import com.mrprojects.helper.payment.PaymentPost
import com.mrprojects.gholrob.model.rest.*
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface Api {

    @POST("api/v1/login")
    fun login(@Body body: LoginPost): Observable<Response<UserResponse>>

    @POST("api/v1/users/edit")
    fun editProfile(@Body body: EditProfilePost): Observable<Response<UserResponse>>

    @POST("api/v1/payments/create")
    fun createPayment(@Body body: PaymentPost): Observable<Response<PaymentResponse>>


    @POST("api/v1/gholrob/games/start")
    fun createNewGame(): Observable<Response<StartGameResponse>>

    @GET("api/v1/gholrob/games/{id}/details")
    fun getGame(@Path("id") id: Int): Observable<Response<GameResponse>>

    @GET("api/v1/gholrob/profile/ratings")
    fun getRatings(): Observable<Response<RatingResponse>>

    @GET("api/v1/gholrob/profile/game/histories")
    fun getGameHistory(): Observable<Response<GameHistoriesResponse>>

    @POST("api/v1/gholrob/games/{id}/lives/use")
    fun useLife(@Path("id") id: Int): Observable<Response<GameResponse>>

    @POST("api/v1/gholrob/profile/lives/buy")
    fun buyLife(@Body body: BuyLifePost): Observable<Response<EmptyResponse>>

    @POST("api/v1/gholrob/games/{id}/end")
    fun endGame(@Path("id") id: Int): Observable<Response<GameResponse>>

    @POST("api/v1/gholrob/games/{id}/continue")
    fun continueGame(@Path("id") id: Int): Observable<Response<GameResponse>>

    @POST("api/v1/gholrob/games/{id}/cells/{cell_id}/click")
    fun clickOnCell(@Path("id") id: Int, @Path("cell_id") cellId: Int): Observable<Response<ClickOnCellResponse>>

    @POST("api/v1/gholrob/games/{id}/cells/{cell_id}/flag")
    fun flagCell(@Path("id") id: Int, @Path("cell_id") cellId: Int): Observable<Response<FlagCellResponse>>

}