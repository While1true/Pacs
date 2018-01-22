package coms.pacs.pacs.Dialog

import android.view.Gravity
import coms.pacs.pacs.BaseComponent.BaseDialog
import coms.pacs.pacs.Model.DicAttrs
import coms.pacs.pacs.R
import kotlinx.android.synthetic.main.textview.*

/**
 * Created by 不听话的好孩子 on 2018/1/22.
 */
class DcmInfoDialog : BaseDialog() {
    var attrs:DicAttrs?=null
    override fun layoutId(): Int {
        return R.layout.textview
    }

    override fun initView() {
        setTitle("图片包含信息")
        var string=
            """

            姓名：${attrs?.patientName}

            出生日期：${attrs?.patientBirthDate}

            检查医院：${attrs?.institution}

            检查部位：${attrs?.description}

            情况说明：${attrs?.SeriesDescription}

            """
        tv.gravity=Gravity.LEFT
        tv.text = string
    }
}