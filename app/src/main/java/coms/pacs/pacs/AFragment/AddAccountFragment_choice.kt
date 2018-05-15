package coms.pacs.pacs.AFragment

import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import coms.pacs.pacs.AFragment.BaseImpl.AddAccountFragment_Base
import coms.pacs.pacs.Api.ApiImpl
import coms.pacs.pacs.Model.Choice
import coms.pacs.pacs.Model.RegisterItem
import coms.pacs.pacs.R
import coms.pacs.pacs.Rx.DataObserver
import coms.pacs.pacs.Utils.mtoString
import coms.pacs.pacs.Utils.pop
import coms.pacs.pacs.Utils.toast
import kotlinx.android.synthetic.main.account_choice.*

/**
 * Created by 不听话的好孩子 on 2018/2/1.
 */
class AddAccountFragment_choice : AddAccountFragment_Base<RegisterItem>() {
    override fun init(savedInstanceState: Bundle?) {
        setTitle(item?.title ?: "")
        val observer=object :DataObserver<List<Choice>>(this){
            override fun OnNEXT(bean: List<Choice>?) {
                var index=0
                for (choice in bean!!)
                    if(choice.name==item!!.content) {
                        index = bean.indexOf(choice)
                        break
                    }

                spinner.adapter=ArrayAdapter<Choice>(context,android.R.layout.simple_list_item_1,bean)
                spinner.setSelection(index)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                "获取列表失败".toast()
            }

        }
        when(item?.title){
            "来源" -> ApiImpl.apiImpl.getSource().subscribe(observer)
            "申请科室" -> ApiImpl.apiImpl.getApplydept().subscribe(observer)
            "检查类型" -> ApiImpl.apiImpl.getChecktype().subscribe(observer)
            "检查设备" -> ApiImpl.apiImpl.getCheckdevice().subscribe(observer)
            "检查项目" -> ApiImpl.apiImpl.getCheckpart().subscribe(observer)
        }
        confirm.setOnClickListener {

            item?.content = spinner.selectedItem.mtoString()
            if("null"!=item?.content) {
                callback?.call(item)
                pop()
            }
        }
    }


    override fun getLayoutId() = R.layout.account_choice
}