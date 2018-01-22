package coms.pacs.pacs.Dialog

import android.view.View
import coms.pacs.pacs.BaseComponent.BaseDialog
import coms.pacs.pacs.Model.DicAttrs
import coms.pacs.pacs.R
import coms.pacs.pacs.Utils.K2JUtils
import kotlinx.android.synthetic.main.watch_dialog.*

/**
 * Created by 不听话的好孩子 on 2018/1/19.
 */
class DcmWatchDialog : BaseDialog() {
    var attrs:DicAttrs?=null
    lateinit var listener: View.OnClickListener
    fun setonClickListenr(listener: View.OnClickListener) {
        this.listener=listener
    }

    override fun layoutId(): Int {
        return R.layout.watch_dialog
    }

    override fun initView() {
        setTitle("菜单")
        if(K2JUtils.get("showwm",true)){
            showwm.getaSwitch().isChecked=true
        }
        draw_mode.setOnClickListener(listener)
        color_adjust.setOnClickListener(listener)
        pic_info.setOnClickListener {
            val dcmInfoDialog = DcmInfoDialog()
            dcmInfoDialog.attrs=attrs
            dcmInfoDialog.show(fragmentManager)
        }
        showwm.setSwitchListener{ _, bool->
            showwm.tag = bool
            listener.onClick(showwm)
        }
    }


}