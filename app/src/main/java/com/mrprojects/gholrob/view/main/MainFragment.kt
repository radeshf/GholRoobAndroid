package com.mrprojects.gholrob.view.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mrprojects.gholrob.databinding.MainFragmentBinding
import com.mrprojects.gholrob.helper.log
import com.mrprojects.gholrob.helper.noInternetDialog
import com.mrprojects.gholrob.helper.openFragment
import com.mrprojects.gholrob.helper.openMarketRatePage
import com.mrprojects.gholrob.helper.openNextLevel
import com.mrprojects.gholrob.helper.payment.LifePacks
import com.mrprojects.gholrob.helper.showBuyEnergyDialog
import com.mrprojects.gholrob.helper.showBuyLifeDoneDialog
import com.mrprojects.gholrob.helper.showCloseGameDialog
import com.mrprojects.gholrob.helper.showCoinNotEnoughDialog
import com.mrprojects.gholrob.helper.showSettingsDialog
import com.mrprojects.gholrob.helper.showSuccessDialog
import com.mrprojects.gholrob.helper.tapsell.TapSellHelper
import com.mrprojects.gholrob.helper.updateHearts
import com.mrprojects.gholrob.helper.warningDialog
import com.mrprojects.gholrob.model.ErrorTypes
import com.mrprojects.gholrob.repository.Provider
import com.mrprojects.gholrob.model.User
import com.mrprojects.gholrob.model.events.OnBuyNewLifeCalled
import com.mrprojects.gholrob.model.events.OnBuyRefillEnergyCalled
import com.mrprojects.gholrob.model.events.OnShowCoinShopCalled
import com.mrprojects.gholrob.model.events.OnShowLifeShopCalled
import com.mrprojects.gholrob.view.play.history.HistoryFragment
import com.mrprojects.gholrob.view.profile.showProfileInfoDialog
import com.mrprojects.gholrob.view.rating.RatingFragment
import com.mrprojects.gholrob.view.tutorial.TutorialFragment
import com.mrprojects.gholrob.viewmodel.UserViewModel
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.showToast
import kotlinx.coroutines.launch

class MainFragment : BaseFragment<MainFragmentBinding>(MainFragmentBinding::inflate) {
    private lateinit var userViewModel: UserViewModel

    lateinit var adsHelper: TapSellHelper


    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userConfig()
        clicks()
        login()
        configAdHelper()

    }

    fun userConfig() {
        userViewModel = Provider.provideUserViewModel(this)
        userViewModel.loadUser()
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                onUserDataUpdated(user)
            }
        }
        userViewModel.nextHeartTimer.observe(viewLifecycleOwner) { millis ->
            if (millis <= 0) {
                binding.HeartsLayout.tvHeartTimer.text = ""
            } else {
                val minutes = (millis / 1000) / 60
                val seconds = (millis / 1000) % 60
                binding.HeartsLayout.tvHeartTimer.text = String.format("%02d:%02d", minutes, seconds)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.errorEvent.collect { error ->
                    when (error) {
                        ErrorTypes.COIN_NOT_ENOUGH -> showCoinNotEnoughDialog()
                        else -> warningDialog(error.key)
                    }

                }
            }
        }
    }

    fun onUserDataUpdated(user: User) {
        binding.tvLifeCount.text = user.lives.toString()
        binding.tvCoins.text = user.coins.toString()
        binding.HeartsLayout.updateHearts(user, showAdd = true, showTimer = true)
        binding.tvStart.text = if (user.haveUnfinishedAttempt()) "ادامه" else "شروع"
        binding.profileLayout.tvUsername.text = user.name
        binding.profileLayout.ivProfileImage.setImageResource(user.getProfileResource())
    }

    private fun login() {
        Provider.provideApiHelper(this).login(
            {
                userViewModel.storeUser(it.data)
            }, { msg ->
                noInternetDialog(msg = msg) {
                    login()
                }
            })
    }

    private fun configAdHelper() {
        adsHelper = TapSellHelper(
            requireActivity(),
            onAddHeartRewarded = {
                addEnergyByAds()
            }
        )
    }

    private fun showBuyEnergy() {
        showBuyEnergyDialog(userViewModel,
            {
                if (!userViewModel.checkUserHeartBeforeFill()) return@showBuyEnergyDialog
                adsHelper.requestAddHeartAds()
            },
            {
                if (!userViewModel.checkUserHeartBeforeFill()) return@showBuyEnergyDialog
                postEvent(OnBuyRefillEnergyCalled())
            }, {
                if (!userViewModel.checkUserHeartBeforeBuy()) return@showBuyEnergyDialog
                postEvent(OnBuyNewLifeCalled())

            }
        )


    }

    private fun clicks() {
        binding.brnPlay.setOnClickListener {
            val user = userViewModel.user.value!!
            if (user.haveUnfinishedAttempt()) {
                openNextLevel(user.unfinishedAttemptId!!, true)
            } else {
                Provider.provideApiHelper(this).createNewGame({
                    openNextLevel(it.id, true)
                }, {
                    showBuyEnergy()
                })
            }


        }
        binding.brnRating.setOnClickListener {
            openFragment(RatingFragment.newInstance())
        }
        binding.HeartsLayout.ivHeartAdd.setOnClickListener {
            showBuyEnergy()
        }

        binding.lnrCoin.setOnClickListener {
            postEvent(OnShowCoinShopCalled())
        }

        binding.lnrLife.setOnClickListener {
            postEvent(OnShowLifeShopCalled())
        }
        binding.lnrHistory.setOnClickListener {
            openFragment(HistoryFragment.newInstance())
        }

        binding.ivBox1.setOnClickListener {
            requireContext().openMarketRatePage()
        }
        binding.ivBox2.setOnClickListener {
            showSettingsDialog()
        }

        binding.profileLayout.root.setOnClickListener {
            val user = userViewModel.user.value!!
            showProfileInfoDialog(user, isSelf=true)
        }

        binding.lnrTutorial.setOnClickListener {
            openFragment(TutorialFragment.newInstance())
        }

    }

    fun addEnergyByAds() {
        val post = LifePacks.BUY_ONE_ENERGY.convertToItemPost()
        Provider.provideApiHelper(this).buyItem(post) {
            userViewModel.storeUser(it.data)
            showSuccessDialog(it.getMessage())
        }
    }

    override fun showMsg(s: String) {
        super.showMsg(s)
        warningDialog(s)
    }

    override fun onDestroy() {
        super.onDestroy()
        adsHelper.destroyAd()
    }


}