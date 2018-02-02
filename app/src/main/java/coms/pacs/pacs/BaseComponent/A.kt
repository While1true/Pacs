package coms.pacs.pacs.BaseComponent

import com.ck.hello.nestrefreshlib.View.Adpater.Base.Holder
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter
import coms.pacs.pacs.Interfaces.MyCallBack

/**
 * Created by 不听话的好孩子 on 2018/2/2.
 */
class A : SAdapter<Any> {
    var itemClickCallBack: MyCallBack<Any?>? = null

    constructor(list: MutableList<Any>?) : super(list)
    constructor(count: Int) : super(count)
    constructor() : super()

    override fun onBindView(holder: Holder?, t: Any?, positon: Int) {
        super.onBindView(holder, t, positon)
        if (itemClickCallBack != null) {
            holder?.itemView?.setOnClickListener { itemClickCallBack?.call(t) }
        }
    }
}