package coms.pacs.pacs.Dialog

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import coms.pacs.pacs.BaseComponent.BaseDialog
import coms.pacs.pacs.R

/**
 * Created by 不听话的好孩子 on 2018/1/25.
 */
class WriteReportDialog:BaseDialog() {
    override fun layoutId(): Int {
        return R.layout.report_dialog
    }

    override fun initView() {
        setTitle("书写报告")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val onCreateDialog = super.onCreateDialog(savedInstanceState)
        onCreateDialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        return onCreateDialog
    }
}