package coms.pacs.pacs.AFragment

import android.os.Bundle
import coms.pacs.pacs.AFragment.BaseImpl.AddAccountFragment_Base
import coms.pacs.pacs.Model.RegisterItem
import coms.pacs.pacs.R
import coms.pacs.pacs.Utils.pop
import kotlinx.android.synthetic.main.account_sex.*

/**
 * Created by 不听话的好孩子 on 2018/2/1.
 */
class AddAccountFragment_sex : AddAccountFragment_Base<RegisterItem>() {
    override fun init(savedInstanceState: Bundle?) {
        setTitle(item?.title ?: "")
        sexspinner.setSelection(if(item?.content=="男") 1 else 0)
        confirm.setOnClickListener {
            val st = sexspinner.selectedItem.toString()
            if (item == null|| st ==item?.content) {
                return@setOnClickListener
            }
            item?.content=st
            callback?.call(item)
            pop()

        }
    }

    override fun getLayoutId() = R.layout.account_sex
}