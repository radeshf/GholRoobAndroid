package com.mrprojects.gholrob.view.play

import CellTypes
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.GameOverDialogBinding
import com.mrprojects.gholrob.databinding.GamePassedDialogBinding
import com.mrprojects.gholrob.databinding.PlayFragmentBinding
import com.mrprojects.gholrob.helper.doOnTry
import com.mrprojects.gholrob.helper.openNextLevel
import com.mrprojects.gholrob.helper.showClosePlayDialog
import com.mrprojects.gholrob.helper.showCoinNotEnoughDialog
import com.mrprojects.gholrob.helper.showSuccessDialog
import com.mrprojects.gholrob.helper.showUseLifeDialog
import com.mrprojects.gholrob.helper.submitHint
import com.mrprojects.gholrob.helper.tapsell.TapSellHelper
import com.mrprojects.gholrob.helper.warningDialog
import com.mrprojects.gholrob.model.Attempt
import com.mrprojects.gholrob.model.ErrorTypes
import com.mrprojects.gholrob.model.GameCell
import com.mrprojects.gholrob.model.HintType
import com.mrprojects.gholrob.model.Puzzle
import com.mrprojects.gholrob.model.User
import com.mrprojects.gholrob.model.events.OnShowLifeShopCalled
import com.mrprojects.gholrob.repository.Provider
import com.mrprojects.gholrob.viewmodel.UserViewModel
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.basicConfig
import ir.radesh.basemodule.commons.clickOnTileAnimation
import ir.radesh.basemodule.commons.disableAlphaByBoolean
import ir.radesh.basemodule.commons.getAdp
import ir.radesh.basemodule.commons.inVisibleByBoolean
import ir.radesh.basemodule.commons.initGrid
import ir.radesh.basemodule.commons.makeWordRed
import ir.radesh.basemodule.commons.setTextCollor
import ir.radesh.basemodule.commons.showToast
import ir.radesh.basemodule.interfaces.OnItemClickListener
import kotlinx.coroutines.launch
import timber.log.Timber

class PlayFragment : BaseFragment<PlayFragmentBinding>(PlayFragmentBinding::inflate), OnItemClickListener<GameCell> {
    var puzzle: Puzzle = Puzzle()
    private lateinit var userViewModel: UserViewModel
    lateinit var adsHelper: TapSellHelper
    private lateinit var user: User


    var gameId: Int = 0

    var isFlagSelected = false
    var isLoading = false

    companion object {
        fun newInstance(gameId: Int): PlayFragment {
            val frag = PlayFragment()
            val b = Bundle()
            b.putInt("gameId", gameId)
            frag.arguments = b
            return frag
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameId = arguments?.getInt("gameId")!!
        binding.rvOptions.initGrid(7, canScroll = false)

        binding.rvOptions.adapter = CellsAdapter(this)
        getData()
        userConfig()
        clicks()
        configAdHelper()
    }

    fun userConfig() {
        userViewModel = Provider.provideUserViewModel(this)
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            this.user = user
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.errorEvent.collect { error ->
                    when(error){
                        ErrorTypes.COIN_NOT_ENOUGH-> showCoinNotEnoughDialog()
                        else -> showToast(error.key, Toast.LENGTH_SHORT)
                    }

                }
            }
        }
    }

    private fun hintDoubleCoin() {
        val coin = puzzle.reward * 2
        userViewModel.addCoins(coin)
        submitHint(puzzle.id, HintType.DOUBLE_COIN.key, coin, false)
        showSuccessDialog("تعداد ${coin}{ سکه جایزه دریافت کردی! ")

    }

    private fun hintAddHeart() {
        userViewModel.addHeart()
        submitHint(puzzle.id, HintType.ADD_HEART_AFTER_FAIL.key)
        showSuccessDialog("یکی از قلب هات پر شد")

    }

    private fun clicks() {
        binding.btnFlag.setOnClickListener {
            isFlagSelected = !isFlagSelected
            if (isFlagSelected){
                binding.btnFlag.setBackgroundResource(R.drawable.box_btn_selected)
                binding.lnrTarget.inVisibleByBoolean(false)
                binding.lnrFlagOn.inVisibleByBoolean(true)
            }else{
                binding.btnFlag.setBackgroundResource(R.drawable.box_btn)
                binding.lnrTarget.inVisibleByBoolean(true)
                binding.lnrFlagOn.inVisibleByBoolean(false)
            }
        }
        binding.btnHeart.setOnClickListener {
            showUseLifeDialog(user) { dialog ->
                useLifeApi(dialog)
            }
        }
        registerBackCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showClosePlayDialog {
                    finishGame()
                }
            }
        })

    }

    private fun configAdHelper() {
        adsHelper = TapSellHelper(
            requireActivity(),
            onAddHeartRewarded = {
                hintAddHeart()
            },
            onDoubleCoinRewarded = {
                hintDoubleCoin()
            },
            onHintRewarded = {
//                hintAfterFailed()
            }
        )
        adsHelper.requestBannerAds(binding.standardBanner)
    }

    private fun getData(onEnded: (() -> Unit)? = null) {
        Provider.provideApiHelper(this).getGame(gameId) {
            loadGame(it)
            onEnded?.invoke()
        }
    }

    private fun loadGame(attempt: Attempt){
        if (attempt.isShowPassedDialog){
            showPassedDialog(attempt)
        }
        if (attempt.isShowFailDialog){
            showFailedDialog(attempt, attempt.killedBy!!)
        }
        binding.tvTotalHearts.text = attempt.totalHearts.toString()
        binding.tvHearts.text = attempt.hearts.toString()
        binding.rvOptions.getAdp<CellsAdapter>().setData(attempt.cells)
        when (attempt.totalHearts) {
            6 -> {
                binding.ivBossImage.setImageResource(CellTypes.SMALL_BOSS.image)
                binding.tvBossName.setText(CellTypes.SMALL_BOSS.title)
            }
            10 -> {
                binding.ivBossImage.setImageResource(CellTypes.BIG_BOSS.image)
                binding.tvBossName.setText(CellTypes.BIG_BOSS.title)
            }
            15 -> {
                binding.ivBossImage.setImageResource(CellTypes.FINAL_BOSS.image)
                binding.tvBossName.setText(CellTypes.FINAL_BOSS.title)
            }
        }
    }


    override fun onItemClick(item: GameCell) {
        if (item.isDefeated){
            Timber.e("item is Defeated")
            return
        }
        if (binding.rvOptions.getAdp<CellsAdapter>().isGlobalLoading){
            Timber.e("puzzle is locked cause isGlobalLoading is true")
            return
        }

        isLoading = true
        binding.rvOptions.getAdp<CellsAdapter>().isGlobalLoading = true

        if (isFlagSelected){
            Provider.provideApiHelper(this).flagCell(gameId, cellId = item.id) {
                isLoading = false
                binding.rvOptions.getAdp<CellsAdapter>().isGlobalLoading = false
                loadGame(it.data.game!!)
            }
        }else{
            Provider.provideApiHelper(this).clickOnCell(gameId, cellId = item.id) {
                isLoading = false
                binding.rvOptions.getAdp<CellsAdapter>().isGlobalLoading = false
                val cell = it.data.cell!!
                val attempt = it.data.game!!
                if (it.data.isGameOver){
                    loadGame(attempt)
                }else{
                    loadGame(attempt)
                    if (!cell.isEmpty() && cell.isDefeated){
                        showKilledEnemy(cell)
                    }
                }
            }
        }
    }

    private fun useLifeApi(dialog: Dialog){
        Provider.provideApiHelper(this).useLife(gameId) { attempt ->
            showSuccessDialog("قلب هات پر شد و میتونی به ادامه مبارزه بپردازی")
            userViewModel.useLife()
            loadGame(attempt)
            dialog.dismiss()
        }
    }

    private fun showKilledEnemy(cell: GameCell){
        binding.ivClickResult.setImageResource(cell.image())
        binding.tvClickResultName.text = cell.name
        binding.tvClickResultName.setTextCollor(if (cell.isHeart()) R.color.green else R.color.white)
        binding.tvClickResultDamage.text = if (cell.isHeart()) "" else "-${cell.damage}"
        binding.lnrClickResult.clickOnTileAnimation()
    }
    private fun showPassedDialog(attempt: Attempt) {
        val binding = GamePassedDialogBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        dialog.basicConfig()
        dialog.setContentView(binding.root)

        binding.lnrBoxes.tvXp.text = "+${attempt.score}"
        binding.lnrBoxes.tvTime.text = attempt.speed
        binding.lnrBoxes.tvEnemies.text = "${attempt.defeatedMonsters}"

        binding.btnExit.setOnClickListener {
            Provider.provideApiHelper(this).endGame(gameId) {
                dialog.dismiss()
                finishGame()
            }
        }
        binding.btnContinue.setOnClickListener {
            Provider.provideApiHelper(this).continueGame(gameId) {
                openNextLevel(it.id, false)
                dialog.dismiss()
            }
        }


        doOnTry({
            dialog.show()
        })
    }
    fun View.updateContinueButton(dialog: Dialog, haveLife: Boolean){
        this.disableAlphaByBoolean(haveLife)
        this.setOnClickListener {
            if(haveLife) {
                useLifeApi(dialog)
            }else{
                warningDialog("شما هیچ نوش دارویی ندارید!")
            }
        }
    }

    private fun showFailedDialog(attempt: Attempt, cell: GameCell) {
        val binding = GameOverDialogBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        dialog.basicConfig()
        dialog.setContentView(binding.root)

        binding.ivEnemy.setImageResource(cell.image())
        binding.tvTitle.text = "${cell.name} شما را کشت "
        binding.tvTitle.makeWordRed(cell.name)
        binding.tvEnemyDamage.text = "-${cell.damage}"

        binding.lnrBoxes.tvXp.text = "+${attempt.score}"
        binding.lnrBoxes.tvTime.text = attempt.speed
        binding.lnrBoxes.tvEnemies.text = "${attempt.defeatedMonsters}"

        binding.btnBuyLife.disableAlphaByBoolean(!cell.isBottomLessPit())

        binding.btnContinue.updateContinueButton(dialog, attempt.profile.lives > 0)

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.btnContinue.updateContinueButton(dialog, user.lives > 0)

        }


        binding.btnExit.setOnClickListener {
            Provider.provideApiHelper(this).endGame(gameId) {
                dialog.dismiss()
                finishGame()
            }
        }
        binding.btnBuyLife.setOnClickListener {
            if (cell.isBottomLessPit()){
                warningDialog("افتادن در چاه شغاد یعنی پایان کار! نوش دارو کمکی بهت نمیکنه")
                return@setOnClickListener
            }
            postEvent(OnShowLifeShopCalled())

        }
        doOnTry({
            dialog.show()
        })
    }

    fun finishGame() {
        backCallback?.isEnabled = false
        onBackPressed()

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