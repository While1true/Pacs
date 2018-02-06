package coms.pacs.pacs.AFragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.ck.hello.nestrefreshlib.View.Adpater.Base.MyCallBack
import coms.pacs.pacs.AFragment.BaseImpl.AddAccountFragment_Base
import coms.pacs.pacs.Api.ApiImpl
import coms.pacs.pacs.BaseComponent.BaseFragment
import coms.pacs.pacs.Model.Base
import coms.pacs.pacs.Model.Constance
import coms.pacs.pacs.Model.RegisterInfo
import coms.pacs.pacs.Model.RegisterItem
import coms.pacs.pacs.R
import coms.pacs.pacs.Rx.DataObserver
import coms.pacs.pacs.Rx.Utils.RxBus
import coms.pacs.pacs.Utils.pop
import coms.pacs.pacs.Utils.showAddFragment
import coms.pacs.pacs.Utils.toast
import coms.pacs.pacs.Views.SettingView
import kotlinx.android.synthetic.main.add_layout.*

/**
 * Created by 不听话的好孩子 on 2018/2/1.
 */
class AddAccountFragment : BaseFragment(), View.OnClickListener {

    var patientCode = ""
    val bean = RegisterInfo()
    override fun onClick(v: View) {
        val fragment_input = AddAccountFragment_input()
        performClick(v, fragment_input, MyCallBack {
            (v as SettingView).setSubText(it.content)
            when (it.title) {
                "姓名" -> bean.name = it.content
                "性别" -> bean.sex = it.content
                "来源" -> bean.patientresource = it.content
                "住院编号" -> bean.admissioncode = it.content
                "床号" -> bean.bedcode = it.content
                "检查部位" -> bean.checkpart = it.content
                "病情描述" -> bean.checkdescription = it.content
                "检查编号" -> bean.applydept = it.content
                "检查类型" -> bean.checktype = it.content
                "申请科室" -> bean.applydept = it.content
                "检查设备" -> bean.checkdevice = it.content
                "检查描述" -> bean.checkdescription = it.content
            }
        })
    }

    private fun initData() {
        if(patientCode!=""){
            ApiImpl.apiImpl.getPatientRegisterInfo(patientCode)
                    .subscribe(object : DataObserver<RegisterInfo>(this){
                        override fun OnNEXT(bean: RegisterInfo?) {
                            name.setSubText(bean?.name)
                            from.setSubText(bean?.patientresource)
                            age.setSubText(bean?.birthday)
                            sex.setSubText(bean?.sex)
                            roomid.isEnabled=false
                            roomid.setSubText(bean?.admissioncode)
                            bedid.setSubText(bean?.bedcode)
                            checkpart.setSubText(bean?.checkpart)
                            department.setSubText(bean?.applydept)
                            checktype.setSubText(bean?.checktype)
                            checkdevice.setSubText(bean?.checkdevice)
                            description.setSubText(bean?.checkdescription)
                        }
                    })
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        setTitle( if(patientCode!="") "信息查看修改" else "病症采集")

        setMenuClickListener(R.drawable.paper_plane, View.OnClickListener {
            if(TextUtils.isEmpty(name.subTextView.text.toString())){
                "姓名不能为空".toast()
                return@OnClickListener
            }
            AlertDialog.Builder(context)
                    .setTitle("确认提交吗？")
                    .setPositiveButton("确认", { _, _ ->
                        bean.patientcode = patientCode
                        ApiImpl.apiImpl.putPatientRegisterInfo(bean)
                                .subscribe(object : DataObserver<Any>(this) {
                                    override fun OnNEXT(bean: Any?) {
                                        "添加成功".toast()
                                        RxBus.getDefault().post(Base<String>("","",Constance.RECEIVE_UPDATE_ADDNEWPATIENT))
                                        pop()
                                    }

                                })
                    })
                    .setNegativeButton("取消", { _, _ -> })
                    .create().show()
        })

        name.setOnClickListener(this)
        from.setOnClickListener(this)
        age.setOnClickListener {
            val xx = it
            val fragment_age = AddAccountFragment_age()
            performClick(xx, fragment_age, MyCallBack {
                (xx as SettingView).setSubText(it.content)
                bean.birthday = it.content
                bean.age = it.age.substring(0, Math.max(0, it.age.length - 1))
                bean.agetype = it.unit
            })
        }
        sex.setOnClickListener {
            val xx = it
            val fragment_sex = AddAccountFragment_sex()
            performClick(xx, fragment_sex, MyCallBack {
                (xx as SettingView).setSubText(it.content)
                bean.sex = it.content
            })
        }
        roomid.setOnClickListener(this)
        bedid.setOnClickListener(this)
        checkpart.setOnClickListener(this)
        department.setOnClickListener(this)
        description.setOnClickListener(this)
        checktype.setOnClickListener(this)
        checkdevice.setOnClickListener(this)

        initData()
    }

    override fun onBack() {
        if(patientCode=="")
        AlertDialog.Builder(context)
                .setTitle("还未提交，确认退出吗？")
                .setPositiveButton("确认", { _, _ -> super.onBack() })
                .setNegativeButton("取消", { _, _ -> })
                .create().show()
        else
            super.onBack()
    }

    fun performClick(it: View, fragment: AddAccountFragment_Base, callBack: MyCallBack<RegisterItem>) {
        val settingView: SettingView = it as SettingView
        val title = settingView.titleView.text.toString()
        fragment.item = RegisterItem(title, "请选择$title", settingView.subTextView.text.toString())
        fragment.callback = callBack
        showAddFragment(fragment)
    }

    override fun getLayoutId() = R.layout.add_layout
}