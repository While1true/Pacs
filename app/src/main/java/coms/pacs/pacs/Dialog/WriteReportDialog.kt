package coms.pacs.pacs.Dialog

import coms.pacs.pacs.BaseComponent.BaseDialogFragment
import coms.pacs.pacs.R
import kotlinx.android.synthetic.main.report_dialog.*

/**
 * Created by 不听话的好孩子 on 2018/1/25.
 */
class WriteReportDialog:BaseDialogFragment() {
    var namex=""
    override fun layoutId(): Int {
        return R.layout.report_dialog
    }

    override fun initView() {

        setTitle("书写报告")
        name.text=namex
    }


}