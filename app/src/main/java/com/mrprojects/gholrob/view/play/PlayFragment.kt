package com.mrprojects.gholrob.view.play

import android.app.Dialog
import android.os.Bundle
import android.view.View
import animateRiseAndSplit
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.GameOverDialogBinding
import com.mrprojects.gholrob.databinding.GamePassedDialogBinding
import com.mrprojects.gholrob.databinding.PlayFragmentBinding
import com.mrprojects.gholrob.helper.doOnTry
import com.mrprojects.gholrob.helper.haptics.OnVibrate
import com.mrprojects.gholrob.helper.haptics.VibrateTypes
import com.mrprojects.gholrob.helper.openFragment
import com.mrprojects.gholrob.helper.openNextLevel
import com.mrprojects.gholrob.helper.showEnemyInfoDialog
import com.mrprojects.gholrob.helper.showSuccessDialog
import com.mrprojects.gholrob.helper.showUseLifeDialog
import com.mrprojects.gholrob.helper.sound.sfx.OnPlaySfx
import com.mrprojects.gholrob.helper.sound.sfx.SfxTypes
import com.mrprojects.gholrob.helper.warningDialog
import com.mrprojects.gholrob.model.Attempt
import com.mrprojects.gholrob.model.CellTypes
import com.mrprojects.gholrob.model.GameCell
import com.mrprojects.gholrob.model.User
import com.mrprojects.gholrob.model.events.OnShowLifeShopCalled
import com.mrprojects.gholrob.model.play.GameKill
import com.mrprojects.gholrob.repository.Provider
import com.mrprojects.gholrob.view.dialogs.showBuyEyeDialog
import com.mrprojects.gholrob.view.dialogs.showBuyShieldDialog
import com.mrprojects.gholrob.view.main.MainActivity
import com.mrprojects.gholrob.view.tutorial.TutorialFragment
import com.mrprojects.gholrob.viewmodel.GameViewModel
import com.mrprojects.gholrob.viewmodel.UserViewModel
import ir.radesh.basemodule.baseViews.BaseActivity
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.applyPressAnimation
import ir.radesh.basemodule.commons.basicConfig
import ir.radesh.basemodule.commons.disableAlphaByBoolean
import ir.radesh.basemodule.commons.dropDownAnimation
import ir.radesh.basemodule.commons.getAdp
import ir.radesh.basemodule.commons.heartBeatAnimation
import ir.radesh.basemodule.commons.initGrid
import ir.radesh.basemodule.commons.makeWordRed
import ir.radesh.basemodule.commons.shakeAnimation
import ir.radesh.basemodule.commons.visibleByBoolean
import ir.radesh.basemodule.interfaces.OnItemClickListener
import timber.log.Timber

class PlayFragment :
    BaseFragment<PlayFragmentBinding>(PlayFragmentBinding::inflate) {
    private lateinit var userViewModel: UserViewModel
    private lateinit var gameViewModel: GameViewModel
    private lateinit var user: User
    private lateinit var attempt: Attempt

    var gameId: Int = 0

    var isFlagSelected = false
    var isEyeSelected = false
    var isShieldSelected = false
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
        binding.rvKills.initGrid(7, canScroll = false)
        binding.rvOptions.adapter = CellsAdapter({ item, position, view ->
            onCellClicked(item, position, view)
        })
        binding.rvKills.adapter = KillsAdapter(object : OnItemClickListener<GameKill> {
            override fun onItemClick(item: GameKill) {
                onKillItemClicked(item)
            }
        })

        getData()
        userConfig()
//        gameConfig()
        clicks()
    }

    fun userConfig() {
        userViewModel = Provider.provideUserViewModel(this)
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            this.user = user
            binding.layEye.tvActionCounts.text = "${user.eyes}"
            binding.layShield.tvActionCounts.text = "${user.shields}"
        }
    }

    fun gameConfig() {
        gameViewModel = Provider.provideGameViewModel(this)
        gameViewModel.fetchGame(gameId)
        gameViewModel.attempt.observe(viewLifecycleOwner) { attempt ->
            loadGame(attempt)
        }
    }

    private fun toggleFlag(isOn: Boolean) {
        isFlagSelected = isOn
        binding.layFlag.btnAction.isSelected = isOn

        binding.layFlagHeader.root.visibleByBoolean(isOn)
        binding.layTargetHeader.root.visibleByBoolean(!isOn)

    }

    private fun toggleEye(isOn: Boolean) {
        isEyeSelected = isOn
        binding.layEye.btnAction.isSelected = isOn

        binding.layEyeHeader.root.visibleByBoolean(isOn)
        binding.layTargetHeader.root.visibleByBoolean(!isOn)

    }

    private fun toggleShield(isOn: Boolean) {
        isShieldSelected = isOn
        binding.layShield.btnAction.isSelected = isOn

        binding.layShieldHeader.root.visibleByBoolean(isOn)
        binding.layTargetHeader.root.visibleByBoolean(!isOn)

    }


    private fun clicks() {
        binding.layFlag.btnAction.background.mutate()
        binding.layFlag.ivIcon.setImageResource(R.drawable.ig_flag)
        binding.layFlag.btnAction.applyPressAnimation()
        binding.layFlag.btnAction.setOnClickListener {
            toggleEye(false)
            toggleShield(false)
            toggleFlag(!isFlagSelected)
        }

        binding.layEye.btnAction.background.mutate()
        binding.layEye.ivIcon.setImageResource(R.drawable.ig_eye)
        binding.layEye.btnAction.applyPressAnimation()
        binding.layEye.btnAction.setOnClickListener {
            toggleFlag(false)
            toggleShield(false)
            toggleEye(!isEyeSelected)

        }

        binding.layShield.btnAction.background.mutate()
        binding.layShield.ivIcon.setImageResource(R.drawable.ig_shield)
        binding.layShield.btnAction.applyPressAnimation()
        binding.layShield.btnAction.setOnClickListener {
            toggleFlag(false)
            toggleEye(false)
            toggleShield(!isShieldSelected)

        }
        binding.btnHeart.setOnClickListener {
            showUseLifeDialog(user) { dialog ->
                useLifeApi(dialog)
            }
        }
        binding.btnTutorial.setOnClickListener {
            openFragment(TutorialFragment.newInstance())
        }

        binding.layTargetHeader.root.setOnClickListener {
            when (attempt.totalHearts) {
                6 -> showEnemyInfoDialog(GameKill.fromCellType(CellTypes.SMALL_BOSS), true)
                10 -> showEnemyInfoDialog(GameKill.fromCellType(CellTypes.BIG_BOSS), true)
                15 -> showEnemyInfoDialog(GameKill.fromCellType(CellTypes.FINAL_BOSS), true)
            }
        }
        binding.layFlagHeader.root.setOnClickListener {
            showEnemyInfoDialog(GameKill.fromCellType(CellTypes.BOTTOMLESS_PIT, remained=attempt.remainingFlags), true)
        }
        binding.layEyeHeader.root.setOnClickListener {
            (requireActivity() as? BaseActivity<*>)?.showBuyEyeDialog(userViewModel)
        }
        binding.layShieldHeader.root.setOnClickListener {
            (requireActivity() as? BaseActivity<*>)?.showBuyShieldDialog(userViewModel)
        }

    }


    private fun getData(onEnded: (() -> Unit)? = null) {
        Provider.provideApiHelper(this).getGame(gameId) {
            loadGame(it)
            onEnded?.invoke()
        }
    }


    private fun loadGame(attempt: Attempt) {
        this.attempt = attempt
        if (attempt.isShowPassedDialog) {
            postEvent(OnPlaySfx(SfxTypes.Win))
            postEvent(OnVibrate(VibrateTypes.Win))
            showPassedDialog(attempt)

        }
        if (attempt.isShowFailDialog) {
            postEvent(OnPlaySfx(SfxTypes.Lose))
            postEvent(OnVibrate(VibrateTypes.Lose))
            showFailedDialog(attempt, attempt.killedBy!!)
        }
        binding.tvHeartStatus.text = "${attempt.hearts} / ${attempt.totalHearts}"
        binding.layFlag.tvActionCounts.text = "${attempt.remainingFlags}"


        binding.rvOptions.getAdp<CellsAdapter>().setData(attempt.cells)
        binding.rvKills.getAdp<KillsAdapter>().setData(attempt.kills)
        when (attempt.totalHearts) {
            6 -> {
                binding.layTargetHeader.ivBossImage.setImageResource(CellTypes.SMALL_BOSS.image)
                binding.layTargetHeader.tvBossName.text = CellTypes.SMALL_BOSS.title
            }

            10 -> {
                binding.layTargetHeader.ivBossImage.setImageResource(CellTypes.BIG_BOSS.image)
                binding.layTargetHeader.tvBossName.text = CellTypes.BIG_BOSS.title
            }

            15 -> {
                binding.layTargetHeader.ivBossImage.setImageResource(CellTypes.FINAL_BOSS.image)
                binding.layTargetHeader.tvBossName.text = CellTypes.FINAL_BOSS.title
            }
        }
    }

    fun onKillItemClicked(item: GameKill) {
        showEnemyInfoDialog(item, true)
    }

    fun onCellClicked(item: GameCell, position: Int, view: View) {
        if (item.isDefeated) {
            Timber.e("item is Defeated")
            return
        }
        if (binding.rvOptions.getAdp<CellsAdapter>().isGlobalLoading) {
            Timber.e("puzzle is locked cause isGlobalLoading is true")
            return
        }

        isLoading = true
        binding.rvOptions.getAdp<CellsAdapter>().isGlobalLoading = true

        if (isFlagSelected) {
            onFlagCell(item)
        } else if (isEyeSelected) {
            onEyeCell(attempt, item, view)
        } else if (isShieldSelected) {
            onShieldCell(item, view)
        } else {
            onRevealCell(item, view)
        }
    }

    private fun onFlagCell(cell: GameCell) {
        if (attempt.remainingFlags <= 0 && !cell.isFlagged){
            warningDialog("یه جای کارت میلنگه! کلا 8 تا چاه شغاد داریم")
            offLoading(cell)
            return
        }
        Provider.provideApiHelper(this).flagCell(
            gameId, cellId = cell.id,
            doOnDone = {
                offLoading(cell)

                val cell = it.data.cell!!
                val attempt = it.data.game!!

                playSfxOnFlag(cell.isFlagged)
                loadGame(attempt)
            }, onError = {
                offLoading(cell)
            }
        )
    }

    private fun onEyeCell(attempt: Attempt, cell: GameCell, cellView: View) {
        if (userViewModel.user.value?.eyes!! <= 0){
            (requireActivity() as? BaseActivity<*>)?.showBuyEyeDialog(userViewModel)
            offLoading(cell)
            return
        }
        if (cell.isFlagged){
            warningDialog("نمیتونی روی پرچم بزنی! احتمالا بیوفتی تو چاه! بهتره اول پرچمش رو برداری")
            offLoading(cell)
            return
        }
        Provider.provideApiHelper(this).useEye(
            gameId, cellId = cell.id,
            doOnDone = {
                offLoading(cell)
                val cell = it.data.cell!!
                val attempt = it.data.game!!
                postEvent(OnPlaySfx(SfxTypes.EYE))
                onCellRevealedSuccessfully(attempt, cell, cellView)
                userViewModel.useEye()
            }, onError = {
                offLoading(cell)
            })
    }


    private fun onShieldCell(cell: GameCell, cellView: View) {
        if (userViewModel.user.value?.shields!! <= 0){
            (requireActivity() as? BaseActivity<*>)?.showBuyShieldDialog(userViewModel)
            offLoading(cell)
            return
        }
        if (cell.isFlagged){
            warningDialog("نمیتونی روی پرچم بزنی! احتمالا بیوفتی تو چاه! بهتره اول پرچمش رو برداری")
            offLoading(cell)
            return
        }
        Provider.provideApiHelper(this).useShield(
            gameId, cellId = cell.id,
            doOnDone = {
                offLoading(cell)
                val cell = it.data.cell!!
                val attempt = it.data.game!!
                if (cell.isRevealed && !cell.isDefeated) {
                    postEvent(OnPlaySfx(SfxTypes.SHIELD))
                }
                userViewModel.useShield()
                onCellRevealedSuccessfully(attempt, cell, cellView)
            }, onError = {
                offLoading(cell)
            })
    }

    private fun onRevealCell(cell: GameCell, cellView: View) {
        if (cell.isFlagged){
            warningDialog("نمیتونی روی پرچم بزنی! احتمالا بیوفتی تو چاه! بهتره اول پرچمش رو برداری")
            offLoading(cell)
            return
        }
        Provider.provideApiHelper(this).clickOnCell(
            gameId, cellId = cell.id,
            doOnDone = {
                offLoading(cell)
                val cell = it.data.cell!!
                val attempt = it.data.game!!
                if (it.data.isGameOver) {
                    loadGame(attempt)
                } else {
                    onCellRevealedSuccessfully(attempt, cell, cellView)
                }
            }, onError = {
                offLoading(cell)
            })
    }

    private fun offLoading(cell: GameCell) {
        isLoading = false
        binding.rvOptions.getAdp<CellsAdapter>().isGlobalLoading = false
        binding.rvOptions.getAdp<CellsAdapter>().setCellLoading(cell, false)

    }

    private fun onCellRevealedSuccessfully(attempt: Attempt, cell: GameCell, cellView: View) {
        loadGame(attempt)
        if (cell.isDefeated) {
            playSfxOnDefeat(cell)
            binding.ivHeart.heartBeatAnimation()
        }
        if (!cell.isEmpty() && cell.isDefeated) {
            animateRiseAndSplit(binding.lnrMain, cellView, cell.image())
        }
    }

    private fun useLifeApi(dialog: Dialog) {
        Provider.provideApiHelper(this).useLife(gameId) { attempt ->
            showSuccessDialog("قلب هات پر شد و میتونی به ادامه مبارزه بپردازی")
            userViewModel.useLife()
            loadGame(attempt)
            dialog.dismiss()
        }
    }


    private fun playSfxOnDefeat(cell: GameCell) {
        if (cell.isHeart()) {
            postEvent(OnPlaySfx(SfxTypes.ClickOnHeal))
            postEvent(OnVibrate(VibrateTypes.ClickOnHeal))
            binding.healingOverlay.showHeal(1f)

        } else if (cell.isEmpty()) {
            postEvent(OnPlaySfx(SfxTypes.DefeatEmpty))
            postEvent(OnVibrate(VibrateTypes.DefeatEmpty))
        } else if (cell.isBossSmall()) {
            postEvent(OnPlaySfx(SfxTypes.Boss1))
            postEvent(OnVibrate(VibrateTypes.Boss1))
            binding.healingOverlay.showHeal(1f)

        } else if (cell.isBossBig()) {
            postEvent(OnPlaySfx(SfxTypes.Boss2))
            postEvent(OnVibrate(VibrateTypes.Boss2))
            binding.healingOverlay.showHeal(1f)

        } else if (cell.isBossFinal()) {
            postEvent(OnPlaySfx(SfxTypes.Boss3))
            postEvent(OnVibrate(VibrateTypes.Boss3))
            binding.healingOverlay.showHeal(1f)

        } else {
            postEvent(OnPlaySfx(SfxTypes.DefeatEnemy))
            postEvent(OnVibrate(VibrateTypes.DefeatEnemy))
            binding.damageOverlay.showDamage(cell.damage)

        }
    }

    private fun playSfxOnFlag(isFlagged: Boolean) {
        if (isFlagged) {
            postEvent(OnPlaySfx(SfxTypes.FlagOn))
            postEvent(OnVibrate(VibrateTypes.FlagOn))
        } else {
            postEvent(OnPlaySfx(SfxTypes.FlagOff))
            postEvent(OnVibrate(VibrateTypes.FlagOff))
        }

    }

    private fun showPassedDialog(attempt: Attempt) {
        val binding = GamePassedDialogBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        dialog.basicConfig(binding.root)
        binding.tvCoin.text = "+${attempt.rewardCoin}"
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

        binding.winConfettiView.startConfetti()

        doOnTry({
            dialog.show()
        })
    }

    fun View.updateContinueButton(dialog: Dialog, haveLife: Boolean) {
        this.disableAlphaByBoolean(haveLife)
        this.setOnClickListener {
            if (haveLife) {
                useLifeApi(dialog)
            } else {
                warningDialog("شما هیچ نوش دارویی ندارید!")
            }
        }
    }

    private fun showFailedDialog(attempt: Attempt, cell: GameCell) {
        val binding = GameOverDialogBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        dialog.basicConfig(binding.root)

        binding.ivEnemy.setImageResource(cell.image())
        binding.tvTitle.text = "${cell.name} شما را کشت "
        binding.tvTitle.makeWordRed(cell.name)
        binding.tvEnemyDamage.text = "-${cell.damage}"

        binding.lnrBoxes.tvXp.text = "+${attempt.score}"
        binding.lnrBoxes.tvTime.text = attempt.speed
        binding.lnrBoxes.tvEnemies.text = "${attempt.defeatedMonsters}"

        binding.btnBuyLife.disableAlphaByBoolean(!cell.isBottomLessPit())
        binding.btnContinue.visibleByBoolean(!cell.isBottomLessPit())
        binding.tvDetails.visibleByBoolean(!cell.isBottomLessPit())

        binding.tvFinalDead.visibleByBoolean(cell.isBottomLessPit())
        binding.tvFinalDead.makeWordRed("پایان نبرد")

        binding.btnContinue.updateContinueButton(dialog, attempt.profile.lives > 0)

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.btnContinue.updateContinueButton(dialog, user.lives > 0)
        }

        binding.ivEnemy.setOnClickListener {
            binding.root.shakeAnimation()
        }

        binding.btnExit.setOnClickListener {
            Provider.provideApiHelper(this).endGame(gameId) {
                dialog.dismiss()
                finishGame()
            }
        }
        binding.btnBuyLife.setOnClickListener {
            if (cell.isBottomLessPit()) {
                warningDialog(getString(R.string.shoghad_loss_msg))
                return@setOnClickListener
            }
            postEvent(OnShowLifeShopCalled())

        }

        doOnTry({
            dialog.show()
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet!!)

            behavior.peekHeight = (100 * resources.displayMetrics.density).toInt()
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.ivMinimize.setOnClickListener {
                val isCollapsed = behavior.state == BottomSheetBehavior.STATE_COLLAPSED
                behavior.state = if (isCollapsed) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_COLLAPSED
                binding.ivMinimize.dropDownAnimation(!isCollapsed)
            }
        })
    }

    fun finishGame() {
        (requireActivity() as MainActivity).closeWithoutDialog()
    }

    override fun showMsg(s: String) {
        super.showMsg(s)
        warningDialog(s)
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}