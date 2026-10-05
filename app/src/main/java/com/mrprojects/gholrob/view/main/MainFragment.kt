package com.mrprojects.gholrob.view.main

import android.os.Bundle
import android.view.View
import com.mrprojects.gholrob.databinding.MainFragmentBinding
import com.mrprojects.gholrob.helper.noInternetDialog
import com.mrprojects.gholrob.helper.openFragment
import com.mrprojects.gholrob.helper.openMarketRatePage
import com.mrprojects.gholrob.helper.openNextLevel
import com.mrprojects.gholrob.helper.payment.LifePacks
import com.mrprojects.gholrob.helper.setTime
import com.mrprojects.gholrob.helper.showSuccessDialog
import com.mrprojects.gholrob.helper.tapsell.TapSellHelper
import com.mrprojects.gholrob.helper.updateHearts
import com.mrprojects.gholrob.helper.warningDialog
import com.mrprojects.gholrob.model.User
import com.mrprojects.gholrob.model.events.OnProfileChanged
import com.mrprojects.gholrob.model.events.OnShowCoinShopCalled
import com.mrprojects.gholrob.model.events.OnShowLifeShopCalled
import com.mrprojects.gholrob.repository.Provider
import com.mrprojects.gholrob.view.dialogs.showBuyEnergyDialog
import com.mrprojects.gholrob.view.dialogs.showSettingsDialog
import com.mrprojects.gholrob.view.inventory.InventoryFragment
import com.mrprojects.gholrob.view.play.TutorialPlayFragment
import com.mrprojects.gholrob.view.play.history.HistoryFragment
import com.mrprojects.gholrob.view.profile.showProfileInfoDialog
import com.mrprojects.gholrob.view.rating.MainRatingFragment
import com.mrprojects.gholrob.view.tutorial.TutorialFragment
import com.mrprojects.gholrob.viewmodel.UserViewModel
import ir.radesh.basemodule.baseViews.BaseActivity
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.convertMillisToHuman
import ir.radesh.basemodule.commons.setEventBus
import ir.radesh.basemodule.helper.PrefHelper
import org.greenrobot.eventbus.Subscribe

class MainFragment : BaseFragment<MainFragmentBinding>(MainFragmentBinding::inflate) {
    private lateinit var userViewModel: UserViewModel


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


    }

    fun userConfig() {
        userViewModel = Provider.provideUserViewModel(this)
        userViewModel.loadUser()
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                onUserDataUpdated(user)
            }
        }
        userViewModel.heartTimer.timer.observe(viewLifecycleOwner) { millis ->
            binding.HeartsLayout.lnrTimer.setTime(millis)
        }

    }

    fun onUserDataUpdated(user: User) {
        binding.tvLifeCount.text = user.lives.toString()
        binding.tvCoins.text = user.coins.toString()
        binding.HeartsLayout.updateHearts(user, showAdd = true, showTimer = false)
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



    private fun showBuyEnergy() {

        (requireActivity() as? BaseActivity<*>)?.showBuyEnergyDialog(userViewModel)


    }

    private fun clicks() {
        binding.brnPlay.setOnClickListener {
            if (!PrefHelper(requireContext()).isTutorialFinished){
                openFragment(TutorialPlayFragment.newInstance(1))
                return@setOnClickListener
            }
            if (userViewModel.user.value == null){
                noInternetDialog(msg = "اطلاعات حساب به درستی لود نشده است، مجددا تلاش نمایید") {
                    login()
                }
                return@setOnClickListener

            }
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
            openFragment(MainRatingFragment.newInstance())
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

        binding.lnrInventory.setOnClickListener {
            openFragment(InventoryFragment.newInstance())
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
    }

    public override fun onStart() {
        super.onStart()
        setEventBus(true)
    }

    public override fun onStop() {
        super.onStop()
        setEventBus(false)
    }

    @Subscribe
    fun onProfileChanged(event: OnProfileChanged) {
        login()
    }

    private fun mockUserState(remainingHearts: Long, secondsUntilReset: Long) {
        val currentUser = userViewModel.user.value ?: User()

        currentUser.apply {
            nextBonusHeartTime = secondsUntilReset
            fillAllEnergyPrice = "120"
            buyNewEnergyPrice = "450"
        }

        userViewModel.storeUser(currentUser)
    }
}