package coms.pacs.pacs.Activity

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.View
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener
import com.ck.hello.nestrefreshlib.View.State.StateLayout
import coms.pacs.pacs.Api.ApiImpl
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.Dialog.WriteReportDialog
import coms.pacs.pacs.InterfacesAndAbstract.MyCallBack
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
    var bean: ReportItem?=null
    override fun initView() {
        setTitle(getString(R.string.checkreport))
        setMenuClickListener(R.drawable.flower, View.OnClickListener {
            if(bean==null){
                return@OnClickListener
            }
            WriteReportDialog().apply {
                callback = object : MyCallBack<Array<String>> {
                    override fun call(t: Array<String>) {
                        bean!!.diagnosed = t[1]
                        bean!!.features = t[0]
                        showText()
                    }
                }
                this.bean = this@ReportDetailActivity.bean
                namex = "检查号：$checkupcode"
                show(supportFragmentManager)
            }
        })
    }

    private fun showText() {
        tv.gravity = Gravity.LEFT
        tv.isVerticalScrollBarEnabled = true
        tv.movementMethod = ScrollingMovementMethod.getInstance()
        val textx =
                """
                #姓名：${bean?.name ?: ""}
                #年龄：${bean?.birthday ?: ""}
                #性别：${bean?.sex ?: ""}
                #科别：${bean?.applydept ?: ""}
                #住院号：${bean?.patientcode ?: ""}
                #${bean?.checktype ?: "检查"}号：${bean?.checkupcode}
                #检查日期：${bean?.checkdate ?: ""}

                #检查部位：${bean?.checkpart ?: ""}

                #影像所见：${bean?.features?.replace("\r\n", "") ?: ""}

                #诊断结果：${bean?.diagnosed?.replace("\r\n", "") ?: ""}

                #报告医生：${bean?.reportdoctor ?: ""}
                #审核医师：${bean?.reviewdoctor ?: ""}
                #审核状态：${bean?.reviewstatus ?: ""}
                #报告时间：${bean?.reporttime ?: ""}


                """

        tv.text = textx.trimMargin("#")
    }

    override fun loadData() {
        ApiImpl.apiImpl.getPatientReport(checkupcode)
                .subscribe(object : DataObserver<ReportItem>(this) {
                    override fun OnNEXT(bean: ReportItem) {
                        this@ReportDetailActivity.bean = bean
                        tv.setPadding(dp2px(16f), dp2px(20f), 0, 0)
                        tv.gravity = Gravity.LEFT
                        showText()


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