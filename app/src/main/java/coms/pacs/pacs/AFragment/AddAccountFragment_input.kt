package coms.pacs.pacs.AFragment

import android.os.Bundle
import coms.pacs.pacs.AFragment.BaseImpl.AddAccountFragment_Base
import coms.pacs.pacs.R
import coms.pacs.pacs.Utils.pop
import kotlinx.android.synthetic.main.account_input.*

/**
 * Created by 不听话的好孩子 on 2018/2/1.
 */
class AddAccountFragment_input : AddAccountFragment_Base() {
    override fun init(savedInstanceState: Bundle?) {
        setTitle(item?.title ?: "")
        editLayout.hint=item?.hint
        editText.setText(item?.content?:"")
        confirm.setOnClickListener {
            if (item == null||editText.text.toString().trim()==""||item?.content==editText.text.toString()) {
                return@setOnClickListener
            }
            item?.content=editText.text.toString()
            callback?.call(item)
            pop()
//            var info:RegisterInfo?=null
//            when(item?.title){
//                "姓名" -> info=RegisterInfo(name =editText.text.toString())
//                "性别" -> info=RegisterInfo(sex =editText.text.toString())
//                "来源" -> info=RegisterInfo(patientresource = editText.text.toString())
//                "住院编号" -> info=RegisterInfo(admissioncode = editText.text.toString())
//                "床号" -> info=RegisterInfo(bedcode = editText.text.toString())
//                "检查部位" -> info=RegisterInfo(checkpart = editText.text.toString())
//                "检查编号" -> info=RegisterInfo(applydept = editText.text.toString())
//                "检查类型" -> info=RegisterInfo(checktype = editText.text.toString())
//                "检查设备" -> info=RegisterInfo(checkdevice = editText.text.toString())
//                "检查描述" -> info=RegisterInfo(checkdescription = editText.text.toString())
//            }
//            ApiImpl.apiImpl.putPatientRegisterInfo(info!!)
//                    .subscribe(object : DataObserver<Any>(this){
//                        override fun OnNEXT(bean: Any?) {
//                            "成功".toast()
//                            item?.content=editText.text.toString()
//                            callback?.call(item)
//                        }
//
//                    })
        }
    }

    override fun getLayoutId() = R.layout.account_input
}