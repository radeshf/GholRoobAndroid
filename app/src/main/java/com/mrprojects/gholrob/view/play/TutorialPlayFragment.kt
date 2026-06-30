package com.mrprojects.gholrob.view.play

import android.os.Bundle
import android.view.View
import animateRiseAndSplit
import com.mrprojects.gholrob.R
import com.mrprojects.gholrob.databinding.TutorialPlayFragmentBinding
import com.mrprojects.gholrob.helper.haptics.OnVibrate
import com.mrprojects.gholrob.helper.haptics.VibrateTypes
import com.mrprojects.gholrob.helper.initToolbar
import com.mrprojects.gholrob.helper.openFragment
import com.mrprojects.gholrob.helper.setTutorial1Text
import com.mrprojects.gholrob.helper.setTutorialText
import com.mrprojects.gholrob.helper.showSuccessDialog
import com.mrprojects.gholrob.helper.showTutorialDoneDialog
import com.mrprojects.gholrob.helper.sound.sfx.OnPlaySfx
import com.mrprojects.gholrob.helper.sound.sfx.SfxTypes
import com.mrprojects.gholrob.helper.warningDialog
import com.mrprojects.gholrob.model.Attempt
import com.mrprojects.gholrob.model.GameCell
import com.mrprojects.gholrob.repository.Provider
import com.mrprojects.gholrob.view.tutorial.TutorialFragment
import ir.radesh.basemodule.baseViews.BaseFragment
import ir.radesh.basemodule.commons.getAdp
import ir.radesh.basemodule.commons.inVisibleByBoolean
import ir.radesh.basemodule.commons.initGrid
import ir.radesh.basemodule.commons.showToast
import ir.radesh.basemodule.commons.visibleByBoolean
import ir.radesh.basemodule.helper.PrefHelper
import ir.radesh.basemodule.interfaces.OnItemClickListener
import timber.log.Timber

class TutorialPlayFragment : BaseFragment<TutorialPlayFragmentBinding>(TutorialPlayFragmentBinding::inflate) {

    var tutorialId: Int = 0
    var killedSnacks: Int = 0
    lateinit var attempt: Attempt

    var isFlagSelected = false

    companion object {
        fun newInstance(tutorialId: Int): TutorialPlayFragment {
            val frag = TutorialPlayFragment()
            val b = Bundle()
            b.putInt("tutorialId", tutorialId)
            frag.arguments = b
            return frag
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tutorialId = arguments?.getInt("tutorialId")!!
        initToolbar(title = "راهنمای $tutorialId")

        binding.rvTiles.initGrid(7, canScroll = false)
        binding.rvTiles.adapter = CellsAdapter({ item, position, view ->
            onCellClicked(item, position, view)
        }, true)

        getData()
        clicks()
    }



    private fun clicks() {
        binding.btnFlag.setOnClickListener {
            isFlagSelected = !isFlagSelected
            if (isFlagSelected) {
                binding.btnFlag.setBackgroundResource(R.drawable.box_btn_selected)
                if (tutorialId == 3){
                    checkTutorialMission(1)
                }
            } else {
                binding.btnFlag.setBackgroundResource(R.drawable.box_btn)
            }
        }
        binding.btnHeart.setOnClickListener {
            warningDialog("نشان دهنده میزان جان شما در مقابله با دشمنان")
        }
        binding.btnTutorial.setOnClickListener {
            openFragment(TutorialFragment.newInstance())
        }

        binding.ivLast.setOnClickListener {
            openFragment(newInstance(tutorialId - 1), false)
        }

        binding.ivNext.setOnClickListener {
            openFragment(newInstance(tutorialId + 1), false)
        }
    }


    private fun getData() {
        binding.tvArrows.visibleByBoolean(tutorialId == 1)
        binding.lnrArrows.visibleByBoolean(tutorialId == 1)
        binding.lnrHeartArrows.visibleByBoolean(tutorialId == 2)
        binding.lnrFlagArrows.visibleByBoolean(tutorialId == 3)

        binding.ivLast.visibleByBoolean(tutorialId != 1)
        binding.ivNext.visibleByBoolean(tutorialId != 5 && PrefHelper(requireContext()).isTutorialFinished)

        binding.tvTutorialNumber.text = "راهنمای $tutorialId"

        if (tutorialId == 1){
            attempt = Attempt.tutorialKill()
            binding.tvTarget.setTutorial1Text(requireContext().getString(R.string.tutorial_msg_1))

        }
        if (tutorialId == 2){
            attempt = Attempt.tutorialHeart()
            binding.tvTarget.setTutorial1Text(requireContext().getString(R.string.tutorial_msg_2))

        }
        if (tutorialId == 3){
            attempt = Attempt.tutorialFlag()
            binding.tvTarget.setTutorial1Text(requireContext().getString(R.string.tutorial_msg_3))

        }
        if (tutorialId == 4){
            attempt = Attempt.tutorialEye()
            binding.tvTarget.setTutorial1Text(requireContext().getString(R.string.tutorial_msg_4))

        }
        if (tutorialId == 5){
            attempt = Attempt.tutorialFinal()
            binding.tvTarget.setTutorial1Text(requireContext().getString(R.string.tutorial_msg_5))

        }
        loadGame()
    }

    private fun tutorialActions(item: GameCell){
        if(tutorialId == 1){
            if (item.isHyena()){
                checkTutorialMission(1)
            }
            if (item.isWolf()){
                checkTutorialMission(2)
            }
            if (item.isLion()){
                checkTutorialMission(3)
            }
            if (attempt.hearts == 0){
                showTutorialDoneDialog("آفرین","ماموریت اول رو انجام دادی! بریم برای آموزش شکست ضحاک") { dialog ->
                    openFragment(newInstance(2), false)
                    dialog.dismiss()
                }
            }
        }
        else if(tutorialId == 2){
            if (item.isSnake()){
                if (killedSnacks < 2){
                    killedSnacks += 1
                }else{
                    val msg = binding.tvTarget.text.toString().replace("اختیاری)", "✅")
                    binding.tvTarget.setTutorial1Text(msg)
                }
            }
            if(item.isHeart()){
                checkTutorialMission(1)
            }

            if(item.isBossSmall()){
                checkTutorialMission(2)
                showTutorialDoneDialog("دست خوش", "ضحاک رو شکست دادی. بریم سراغ کار با پرچم") { dialog ->
                    openFragment(newInstance(3), false)
                    dialog.dismiss()
                }
            }

        }
        else if(tutorialId == 3){
            Timber.e("item: $item")
            if (item.isFlagged && item.isBottomLessPit()){
                checkTutorialMission(2)
                showTutorialDoneDialog("ایول", "اینم از پرچم گذاری. بزن بریم ") { dialog ->
                    openFragment(newInstance(4), false)
                    dialog.dismiss()
                }
            }
        }
        else if(tutorialId == 4){
            if (item.isDessert()){
                checkTutorialMission(1)
            }
            if (item.isGhost()){
                checkTutorialMission(2)
                showTutorialDoneDialog("عالیه", "بریم سراغ آخرین آموزش") { dialog ->
                    openFragment(newInstance(5), false)
                    dialog.dismiss()
                }
            }


        }
        else if(tutorialId == 5){
            if (item.isBossBig()){
                checkTutorialMission(1)
            }
            if (item.isBossFinal()){
                checkTutorialMission(2)
                postEvent(OnPlaySfx(SfxTypes.Win))
                showTutorialDoneDialog("بی نظیر", "وقتشه خودت رو برای نبرد اصلی آماده کنی. آماده ای؟", "آماده نبرد") { dialog ->
                    PrefHelper(requireContext()).isTutorialFinished = true
                    onBackPressed()
                    dialog.dismiss()
                }
            }

        }
    }

    private fun checkTutorialMission(number: Int){
        val msg = binding.tvTarget.text.toString().replace("$number)", "✅")
        binding.tvTarget.setTutorial1Text(msg)
    }

    private fun loadGame() {
        binding.tvTotalHearts.text = attempt.totalHearts.toString()
        binding.tvHearts.text = attempt.hearts.toString()
        binding.rvTiles.getAdp<CellsAdapter>().setData(attempt.cells)

    }



    private fun defeatEnemy(item: GameCell, position: Int, view: View){
        if (item.isBottomLessPit()){
            warningDialog("چاه شغاد باعث مرگ شما میشود و هیچ راه بازگشتی ندارد. با پرچم میتونید چاه هارو بپوشونید")
            return
        }
        if (item.damage > attempt.hearts){
            warningDialog("جون شما برای شکست این دشمن کافی نیست و باعث باخت شما میشود. با نوش دارو جونت رو پر کن")
            return
        }
        if (item.isHeart()){
            attempt.hearts = attempt.totalHearts
        }else if(item.isBossSmall()){
            attempt.totalHearts = 10
            attempt.hearts = attempt.totalHearts
        }else if(item.isBossBig()){
            attempt.totalHearts = 15
            attempt.hearts = attempt.totalHearts
        }else if(item.isBossFinal()){

        } else{
            attempt.hearts -= item.damage
        }
        item.isDefeated = true
        item.isRevealed = true
        binding.rvTiles.getAdp<CellsAdapter>().notifyDataSetChanged()
        if (item.isDefeated) {
            playSfxOnDefeat(item)
        }
        if (!item.isEmpty() && item.isDefeated) {
            animateRiseAndSplit(binding.lnrMain, view, item.image())
        }
        loadGame()

        tutorialActions(item)
    }
    fun onCellClicked(item: GameCell, position: Int, view: View) {
        if (item.isDefeated) {
            warningDialog("اعداد سفید نشان دهنده میزان آسیب در کاشی های اطراف می باشد")
            return
        }

        if (isFlagSelected) {
            val newFlagStatus = !item.isFlagged
            item.isFlagged = newFlagStatus
            binding.rvTiles.getAdp<CellsAdapter>().notifyItemChanged(position)
            playSfxOnFlag(newFlagStatus)
            tutorialActions(item)
        } else {
            defeatEnemy(item, position, view)

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
//            binding.lnrMain.shakeAnimation()
        } else {
            postEvent(OnPlaySfx(SfxTypes.FlagOff))
            postEvent(OnVibrate(VibrateTypes.FlagOff))
//            binding.lnrMain.shakeAnimation()
        }

    }



    override fun showMsg(s: String) {
        super.showMsg(s)
        warningDialog(s)
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}