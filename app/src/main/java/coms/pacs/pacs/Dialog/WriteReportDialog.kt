package coms.pacs.pacs.Dialog

import coms.pacs.pacs.BaseComponent.BaseDialogFragment
import coms.pacs.pacs.R

/**
 * Created by 不听话的好孩子 on 2018/1/25.
 */
class WriteReportDialog:BaseDialogFragment() {
    override fun layoutId(): Int {
        return R.layout.report_dialog
    }

    override fun initView() {

        setTitle("书写报告")
    }

}