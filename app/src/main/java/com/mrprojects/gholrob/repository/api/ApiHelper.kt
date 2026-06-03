package com.mrprojects.gholrob.repository.api

import android.content.Context
import com.mrprojects.gholrob.model.Attempt
import com.mrprojects.helper.payment.PaymentPost
import com.mrprojects.gholrob.model.rest.*
import com.mrprojects.gholrob.repository.db.AppDatabase
import ir.radesh.basemodule.baseViews.ApiSubscriber
import ir.radesh.basemodule.helper.RadResponseHelper

class ApiHelper(private val context: Context, private val apiSubscriber: ApiSubscriber, private val apiRepo: ApiRepo, private val db: AppDatabase) {

    fun login(doOnDone: (response: UserResponse)->Unit, onNoInternet: (msg: String)->Unit){
        val config = ConfigPost.newInstance(context)
        val data = LoginPost(config)
        apiSubscriber.subscribe(apiRepo.login(data).subscribeWith(object : RadResponseHelper<UserResponse>(apiSubscriber){
            override fun onSuccessful(response: UserResponse) {
                doOnDone(response)
            }

            override fun onError(msg: String, code: Int) {
                onNoInternet(msg)
//                super.onError(msg, code)

            }
        }))
    }

    fun editProfile(nickName: String, bio: String, profileImage: String, doOnDone: (response: UserResponse)->Unit){
        val data = EditProfilePost(nickName, bio, profileImage)
        apiSubscriber.subscribe(apiRepo.editProfile(data).subscribeWith(object : RadResponseHelper<UserResponse>(apiSubscriber){
            override fun onSuccessful(response: UserResponse) {
                doOnDone(response)
            }
        }))
    }


    fun createPayment(body: PaymentPost, doOnDone: (response: PaymentResponse)->Unit){
        apiSubscriber.subscribe(apiRepo.createPayment( body).subscribeWith(object : RadResponseHelper<PaymentResponse>(apiSubscriber){
            override fun onSuccessful(response: PaymentResponse) {
                doOnDone(response)
            }

        }))

    }


    fun buyItem(body: BuyItemPost, doOnDone: (response: UserResponse)->Unit){
        apiSubscriber.subscribe(apiRepo.buyItem(body).subscribeWith(object : RadResponseHelper<UserResponse>(apiSubscriber){
            override fun onSuccessful(response: UserResponse) {
                doOnDone(response)
            }

        }))

    }
    fun buyCoin(body: BuyCoinPost, doOnDone: (response: UserResponse)->Unit){
        apiSubscriber.subscribe(apiRepo.buyCoin( body).subscribeWith(object : RadResponseHelper<UserResponse>(apiSubscriber){
            override fun onSuccessful(response: UserResponse) {
                doOnDone(response)
            }

        }))

    }

    fun buyEnergy(body: BuyEnergyPost, doOnDone: (response: UserResponse)->Unit){
        apiSubscriber.subscribe(apiRepo.buyEnergy(body).subscribeWith(object : RadResponseHelper<UserResponse>(apiSubscriber){
            override fun onSuccessful(response: UserResponse) {
                doOnDone(response)
            }

        }))

    }

    fun createNewGame(doOnDone: (response: Attempt)->Unit, doOnNoHeart: ()->Unit){
        apiSubscriber.subscribe(apiRepo.createNewGame().subscribeWith(object : RadResponseHelper<StartGameResponse>(apiSubscriber){
            override fun onSuccessful(response: StartGameResponse) {
                doOnDone(response.data.attempt)
            }

            override fun onError(msg: String, code: Int) {
                if (code == 1100){
                    doOnNoHeart()
                }else{
                    super.onError(msg, code)
                }
            }


        }))
    }

    fun getGame(gameId: Int, doOnDone: (game: Attempt)->Unit){
        apiSubscriber.subscribe(apiRepo.getGame(gameId).subscribeWith(object : RadResponseHelper<GameResponse>(apiSubscriber){
            override fun onSuccessful(response: GameResponse) {
                doOnDone(response.data.attempt)
            }

        }))

    }

    fun getRatings(doOnDone: (response: RatingResponse)->Unit){
        apiSubscriber.subscribe(apiRepo.getRatings().subscribeWith(object : RadResponseHelper<RatingResponse>(apiSubscriber){
            override fun onSuccessful(response: RatingResponse) {
                doOnDone(response)
            }

        }))

    }
    fun getGameHistory(doOnDone: (response: GameHistoriesResponse)->Unit){
        apiSubscriber.subscribe(apiRepo.getGameHistory().subscribeWith(object : RadResponseHelper<GameHistoriesResponse>(apiSubscriber){
            override fun onSuccessful(response: GameHistoriesResponse) {
                doOnDone(response)
            }

        }))

    }
    fun useLife(gameId: Int, doOnDone: (game: Attempt)->Unit){
        apiSubscriber.subscribe(apiRepo.useLife(gameId).subscribeWith(object : RadResponseHelper<GameResponse>(apiSubscriber){
            override fun onSuccessful(response: GameResponse) {
                doOnDone(response.data.attempt)
            }

        }))

    }

    fun endGame(gameId: Int, doOnDone: (game: Attempt)->Unit){
        apiSubscriber.subscribe(apiRepo.endGame(gameId).subscribeWith(object : RadResponseHelper<GameResponse>(apiSubscriber){
            override fun onSuccessful(response: GameResponse) {
                doOnDone(response.data.attempt)
            }

        }))

    }

    fun continueGame(gameId: Int, doOnDone: (game: Attempt)->Unit){
        apiSubscriber.subscribe(apiRepo.continueGame(gameId).subscribeWith(object : RadResponseHelper<GameResponse>(apiSubscriber){
            override fun onSuccessful(response: GameResponse) {
                doOnDone(response.data.attempt)
            }

        }))

    }

    fun clickOnCell(gameId: Int, cellId: Int, doOnDone: (response: ClickOnCellResponse)->Unit){
        apiSubscriber.subscribe(apiRepo.clickOnCell(gameId, cellId).subscribeWith(object : RadResponseHelper<ClickOnCellResponse>(apiSubscriber){
            override fun onSuccessful(response: ClickOnCellResponse) {
                doOnDone(response)
            }
            override fun onShowLoading() {

            }

        }))

    }

    fun flagCell(gameId: Int, cellId: Int, doOnDone: (response: FlagCellResponse)->Unit){
        apiSubscriber.subscribe(apiRepo.flagCell(gameId, cellId).subscribeWith(object : RadResponseHelper<FlagCellResponse>(apiSubscriber){
            override fun onSuccessful(response: FlagCellResponse) {
                doOnDone(response)
            }

            override fun onShowLoading() {

            }

        }))

    }



}