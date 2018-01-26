package coms.pacs.pacs.BaseComponent

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.FragmentManager
import coms.pacs.pacs.R
import kotlinx.android.synthetic.main.dialog_root.*
import kotlinx.android.synthetic.main.dialog_root.view.*
import coms.pacs.pacs.Utils.InputUtils
import coms.pacs.pacs.Rx.Utils.RxLifeUtils
import android.support.v4.app.DialogFragment
import android.view.*
import coms.pacs.pacs.App


/**
 * Created by 不听话的好孩子 on 2018/1/19.
 */
abstract class BaseDialogFragment : DialogFragment() {

    lateinit var rootview:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootview = inflater.inflate(R.layout.dialog_root, container)
        inflater.inflate(layoutId(),rootview.content,true)
        return rootview
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        close.setOnClickListener { dismiss() }
        initView()

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val onCreateDialog= super.onCreateDialog(savedInstanceState)
        if(fullscrenn()) {
            onCreateDialog.window.requestFeature(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            onCreateDialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            onCreateDialog.window.decorView.setPadding(0, 0, 0, 0)
            val attributes = onCreateDialog.window.attributes
            onCreateDialog.window.setBackgroundDrawable(ColorDrawable(0xfffafafa.toInt()))
            attributes.width = App.app.resources.displayMetrics.widthPixels
            attributes.gravity = Gravity.BOTTOM
        }
        return onCreateDialog
    }

    open fun fullscrenn()=true

    abstract fun layoutId():Int

    fun setTitle(titlex:String){
        title.text = titlex
    }

    abstract fun initView()

    open fun show(manager:FragmentManager){
        show(manager,javaClass.simpleName)
    }

    override fun dismiss() {
        InputUtils.hideKeyboard(dialog)
        super.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        RxLifeUtils.getInstance().remove(this)

    }
}