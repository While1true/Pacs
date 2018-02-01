package coms.pacs.pacs.Dialog

import android.app.AlertDialog
import android.text.TextUtils
import android.widget.EditText
import coms.pacs.pacs.Api.ApiImpl
import coms.pacs.pacs.BaseComponent.BaseDialogFragment
import coms.pacs.pacs.Interfaces.MyCallBack
import coms.pacs.pacs.Model.Base
import coms.pacs.pacs.Model.Constance
import coms.pacs.pacs.Model.ReportItem
import coms.pacs.pacs.R
import coms.pacs.pacs.Rx.DataObserver
import coms.pacs.pacs.Rx.Utils.RxBus
import coms.pacs.pacs.Utils.toast
import kotlinx.android.synthetic.main.report_dialog.*

/**
 * Created by 不听话的好孩子 on 2018/1/25.
 */
class WriteReportDialog : BaseDialogFragment() {
    var namex = ""
    var bean: ReportItem? = null
    var callback: MyCallBack<Array<String>>? = null
    override fun layoutId(): Int {
        return R.layout.report_dialog
    }

    override fun initView() {

        setTitle("书写报告")
        name.text = namex

        picdes.setText(bean?.features ?: "")
        picresult.setText(bean?.diagnosed ?: "")

        button.setOnClickListener {
            if (TextUtils.isEmpty(picdes.text.toString()) || TextUtils.isEmpty(picresult.text.toString())) {
                "请输入描述信息".toast()
                return@setOnClickListener
            }
            AlertDialog.Builder(context)
                    .setTitle("确认提交吗")
                    .setNegativeButton("取消") { dialog, which ->
                    }
                    .setPositiveButton("确认", { dialog, which ->
                        ApiImpl.apiImpl.putWriteReport(bean!!.patientcode, picdes.text.toString(), picresult.text.toString())
                                .subscribe(object : DataObserver<Any>(context) {
                                    override fun OnNEXT(bean: Any?) {
                                        "成功".toast()
                                        RxBus.getDefault().post(Base("","",Constance.RECEIVE_UPDATE_REPORTLIST))
                                        dismiss()
                                        callback?.call(arrayOf(picdes.text.toString(), picresult.text.toString()))
                                    }
                                })
                    }).create().show()
        }

    }


}