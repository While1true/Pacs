package coms.pacs.pacs.BaseComponent

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coms.pacs.pacs.R
import kotlinx.android.synthetic.main.dialog_root.*
import kotlinx.android.synthetic.main.dialog_root.view.*
import coms.pacs.pacs.Utils.InputUtils
import coms.pacs.pacs.Rx.Utils.RxLifeUtils
import android.support.design.widget.BottomSheetBehavior






/**
 * Created by 不听话的好孩子 on 2018/1/19.
 */
abstract class BaseDialog : BottomSheetDialogFragment() {

    lateinit var rootview:View
    var callback:BottomSheetBehavior.BottomSheetCallback?=null
    lateinit var behavior:BottomSheetBehavior<View>
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

    abstract fun layoutId():Int

    fun setTitle(titlex:String){
        title.text = titlex
    }

    abstract fun initView()

    open fun show(manager:FragmentManager){
        show(manager,javaClass.simpleName)
    }


    override fun onResume() {
        super.onResume()
        if(callback==null) {
            callback = object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                         dismiss()
                    }
                }
            }
            behavior = BottomSheetBehavior.from<View>(rootview.rootView.findViewById(android.support.design.R.id.design_bottom_sheet))
            behavior.setBottomSheetCallback(callback)
//            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun dismiss() {
        try {
            InputUtils.hideKeyboard(dialog)
        } catch (e: Exception) {
        }
        super.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        behavior.setBottomSheetCallback(null)
        RxLifeUtils.getInstance().remove(this)

    }
}