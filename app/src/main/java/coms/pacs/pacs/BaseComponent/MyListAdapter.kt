package coms.pacs.pacs.BaseComponent

import com.ck.hello.nestrefreshlib.View.Adpater.Base.Holder
import com.ck.hello.nestrefreshlib.View.Adpater.Base.ItemHolder
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter
import coms.pacs.pacs.Interfaces.IListDateModel
import coms.pacs.pacs.Interfaces.MyCallBack

/**
 * Created by 不听话的好孩子 on 2018/1/30.
 */
class MyListAdapter<T : IListDateModel>(layoutid: Int = android.R.layout.simple_list_item_2, list: List<T>?) : SAdapter<T>(list) {
    var itemClickCallBack: MyCallBack<T>? = null

    init {
        addType(layoutid, object : ItemHolder<T>() {
            override fun onBind(simpleViewHolder: Holder, t: T, i: Int) {
                simpleViewHolder.setText(android.R.id.text1, t.getTitle())
                simpleViewHolder.setText(android.R.id.text2, t.getSubTitle())
                simpleViewHolder.itemView.setOnClickListener { itemClickCallBack?.call(t) }
            }

            override fun istype(t: T, i: Int): Boolean {
                return true
            }
        })
    }
}