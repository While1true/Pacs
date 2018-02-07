package coms.pacs.pacs.Activity

import android.text.TextUtils
import coms.pacs.pacs.Api.ApiImpl
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.Dialog.RemoteDoctorsDialog
import coms.pacs.pacs.InterfacesAndAbstract.MyCallBack
import coms.pacs.pacs.Model.Doctor
import coms.pacs.pacs.R
import coms.pacs.pacs.Rx.DataObserver
import coms.pacs.pacs.Utils.K2JUtils
import coms.pacs.pacs.Utils.toast
import kotlinx.android.synthetic.main.remote_layout.*

/**
 * Created by 不听话的好孩子 on 2018/1/29.
 */
class RemotoActivity : BaseActivity() {
    var dialog: RemoteDoctorsDialog? = null
    var doctor: String? = null
    override fun loadData() {
    }

    override fun getLayoutId(): Int {
        return R.layout.remote_layout
    }

    override fun initView() {

        setTitle("请求远程会诊")

        var dialog = RemoteDoctorsDialog()
        dialog.patientcode = intent.getStringExtra("patientcode")
        dialog.callback = object : MyCallBack<Doctor> {
            override fun call(t: Doctor) {
                doctor = t.expertcode
                choseDoctor.text=t.expertname
            }

        }
        choseDoctor.setOnClickListener {
            dialog.show(supportFragmentManager)
        }

        send.setOnClickListener {
            if (TextUtils.isEmpty(editText.text.toString())) {
                "请输入您的问题描述".toast()
                return@setOnClickListener
            }

            if (doctor == null) {
                "请选择一位医生".toast()
                return@setOnClickListener
            }

            var account: String = K2JUtils.get("username", "")
            var text = editText.text.toString()
            var patientcode = dialog.patientcode
            ApiImpl.apiImpl.getHelpApplication(account, doctor!!, patientcode!!, text)
                    .subscribe(object : DataObserver<Any>(this) {
                        override fun OnNEXT(bean: Any?) {
                            "申请成功".toast()
                            finish()
                        }
                    })
        }

    }
}