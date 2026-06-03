package com.mrprojects.gholrob.repository.api

import com.mrprojects.helper.payment.PaymentPost
import com.mrprojects.gholrob.model.rest.*
import io.reactivex.Observable
import ir.radesh.basemodule.commons.networkSchedulers
import retrofit2.Response


class ApiRepo(private val api: Api) {

    fun login(post: LoginPost) : Observable<Response<UserResponse>> {
        return api.login(post).networkSchedulers()
    }

    fun editProfile(post: EditProfilePost) : Observable<Response<UserResponse>> {
        return api.editProfile(post).networkSchedulers()
    }

    fun createPayment(body: PaymentPost) : Observable<Response<PaymentResponse>> {
        return api.createPayment(body).networkSchedulers()
    }

    fun buyItem(body: BuyItemPost) : Observable<Response<UserResponse>> {
        return api.buyItem(body).networkSchedulers()
    }

    fun buyCoin(body: BuyCoinPost) : Observable<Response<UserResponse>> {
        return api.buyCoin(body).networkSchedulers()
    }


    fun buyEnergy(body: BuyEnergyPost) : Observable<Response<UserResponse>> {
        return api.buyEnergy(body).networkSchedulers()
    }


    fun createNewGame() : Observable<Response<StartGameResponse>> {
        return api.createNewGame().networkSchedulers()
    }


    fun getGame(gameId: Int) : Observable<Response<GameResponse>> {
        return api.getGame(gameId).networkSchedulers()
    }

    fun getRatings() : Observable<Response<RatingResponse>> {
        return api.getRatings().networkSchedulers()
    }

    fun getGameHistory() : Observable<Response<GameHistoriesResponse>> {
        return api.getGameHistory().networkSchedulers()
    }


    fun useLife(gameId: Int) : Observable<Response<GameResponse>> {
        return api.useLife(gameId).networkSchedulers()
    }

    fun endGame(gameId: Int) : Observable<Response<GameResponse>> {
        return api.endGame(gameId).networkSchedulers()
    }

    fun continueGame(gameId: Int) : Observable<Response<GameResponse>> {
        return api.continueGame(gameId).networkSchedulers()
    }


    fun clickOnCell(gameId: Int, cellId: Int) : Observable<Response<ClickOnCellResponse>> {
        return api.clickOnCell(gameId, cellId).networkSchedulers()
    }


    fun flagCell(gameId: Int, cellId: Int) : Observable<Response<FlagCellResponse>> {
        return api.flagCell(gameId, cellId).networkSchedulers()
    }


}