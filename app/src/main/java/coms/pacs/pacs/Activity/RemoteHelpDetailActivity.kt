package coms.pacs.pacs.Activity

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import coms.pacs.pacs.Api.ApiImpl
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.Model.HelpBean
import coms.pacs.pacs.Model.ReportItem
import coms.pacs.pacs.R
import coms.pacs.pacs.Rx.DataObserver
import coms.pacs.pacs.Utils.K2JUtils
import coms.pacs.pacs.Utils.toast
import kotlinx.android.synthetic.main.remote_help_detail.*

/**
 * Created by 不听话的好孩子 on 2018/1/30.
 */
class RemoteHelpDetailActivity : BaseActivity(), View.OnTouchListener {
    lateinit var helpbean: HelpBean

    override fun initView() {
        setTitle("详细信息")

        //节约scrollview 内editext滑动冲突
        etinput.setOnTouchListener(this)

        helpbean = intent.getSerializableExtra("bean") as HelpBean

        setUIData()

        commit.setOnClickListener {
            val advice = etinput.text.toString()
            if (TextUtils.isEmpty(advice)) {
                "请输入你的建议".toast()
                return@setOnClickListener
            }
            ApiImpl.apiImpl.getHelpReply(helpbean.applycode, advice)
                    .subscribe(object : DataObserver<Any>(this@RemoteHelpDetailActivity) {
                        override fun OnNEXT(bean: Any?) {
                            "提交成功".toast()
                            setResult(Activity.RESULT_OK)
                            finish()
                        }

                        override fun OnERROR(error: String?) {
                            super.OnERROR(error)
                            "提交失败".toast()
                        }

                    })
        }

        watch.setOnClickListener {
            val intent = Intent(this@RemoteHelpDetailActivity, DcmListActivity::class.java)
            intent.putExtra("patientcode", helpbean.checkupcode)
            startActivity(intent)
        }
    }

    private fun setUIData() {
        ask.text = (helpbean.applyusername ?: "") + "提问：" + helpbean.remark
        var account: String = K2JUtils.get("username", "")
        if (helpbean.status == "已协助") {
            commit.isEnabled = false
            etinput.isEnabled = false
            if (!TextUtils.isEmpty(helpbean.checkadvice))
                etinput.setText(helpbean.invitedusername + ":" + helpbean.checkadvice)
        }
        if (helpbean.applyusercode == account) {
            etinput.setBackgroundResource(0)
            commit.visibility = View.INVISIBLE
            etinput.isEnabled = false
        }
    }

    override fun loadData() {

        ApiImpl.apiImpl.getPatientReport(helpbean.checkupcode)
                .subscribe(object : DataObserver<ReportItem>(this) {
                    override fun OnNEXT(bean: ReportItem) {
                        val textx =
                                """
                                姓名：${bean.name?:""}
                                年龄：${bean.birthday?:""}
                                性别：${bean.sex?:""}
                                科别：${bean.applydept?:""}
                                住院号：${bean.patientcode?:""}
                                ${bean.checktype?:""}号：${bean.checkupcode?:""}
                                检查日期：${bean.checkdate?:""}
                                检查部位：${bean.checkpart?:""}
                                """
                        info.text = textx.trimIndent()
                    }

                })
    }

    override fun getLayoutId() = R.layout.remote_help_detail
    override fun onTouch(v: View, event: MotionEvent?): Boolean {
        if (canVerticalScroll(v)) {
            v.parent.requestDisallowInterceptTouchEvent(true)
            if (event?.action == MotionEvent.ACTION_UP) {
                v.parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return false
    }

    private fun canVerticalScroll(v: View): Boolean {
        val scrollY = v.scrollY
        val editText = v as EditText
        val scrollRange = editText.layout.height
        val scrollExtent = editText.height - editText.compoundPaddingTop - editText.compoundPaddingBottom
        val scrollDifference = scrollRange - scrollExtent
        if (scrollDifference == 0)
            return false
        return (scrollY > 0) || (scrollY < scrollDifference - 1)
    }
}