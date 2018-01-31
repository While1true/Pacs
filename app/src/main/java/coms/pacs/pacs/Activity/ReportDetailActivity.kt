package coms.pacs.pacs.Activity

import android.content.Context
import android.view.Gravity
import android.view.View
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener
import com.ck.hello.nestrefreshlib.View.State.StateLayout
import coms.pacs.pacs.Api.ApiImpl
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.Dialog.WriteReportDialog
import coms.pacs.pacs.Model.ReportItem
import coms.pacs.pacs.R
import coms.pacs.pacs.Rx.DataObserver
import coms.pacs.pacs.Utils.dp2px
import kotlinx.android.synthetic.main.textview.*

/**
 * Created by 不听话的好孩子 on 2018/1/25.
 */
class ReportDetailActivity : BaseActivity() {
    val checkupcode: String by lazy { intent.getStringExtra("checkupcode") }
    override fun initView() {
        setTitle(checkupcode)

    }

    override fun loadData() {
        ApiImpl.apiImpl.getPatientReport(checkupcode)
                .subscribe(object : DataObserver<ReportItem>(this) {
                    override fun OnNEXT(bean: ReportItem) {
                        tv.setPadding(dp2px(16f), dp2px(20f), 0, 0)
                        tv.gravity = Gravity.LEFT
                        val textx =
                                """
                                姓名：${bean.name}
                                年龄：${bean.birthday}
                                性别：${bean.sex}
                                科别：${bean.applydept?:""}
                                住院号：${bean.patientcode}
                                ${bean.checktype}号：${bean.checkupcode}
                                检查日期：${bean.checkdate}

                                检查部位：${bean.checkpart}

                                影像所见：${bean.features.replace("\r\n", "")}

                                诊断结果：${bean.diagnosed.replace("\r\n", "")}

                                报告医生：${bean.reportdoctor}
                                审核医师：${bean.reviewdoctor}
                                审核状态：${bean.reviewstatus}
                                报告时间：${bean.reporttime}
                                """

                        tv.text = textx.trimIndent()


                    }
                })
    }

    override fun getLayoutId(): Int {
        return R.layout.textview
    }

    override fun setView(): View? {
        return StateLayout(this)
                .setContent(getLayoutId())
                .setStateListener(object : DefaultStateListener() {
                    override fun netError(p0: Context?) {
                        loadData()
                    }

                })
    }
}