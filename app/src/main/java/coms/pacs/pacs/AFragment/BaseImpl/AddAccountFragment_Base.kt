package coms.pacs.pacs.AFragment.BaseImpl

import com.ck.hello.nestrefreshlib.View.Adpater.Base.MyCallBack
import coms.pacs.pacs.BaseComponent.BaseFragment
import coms.pacs.pacs.Model.RegisterItem

/**
 * Created by 不听话的好孩子 on 2018/2/1.
 */
abstract class AddAccountFragment_Base : BaseFragment() {
    var callback: MyCallBack<RegisterItem>? = null
    var item: RegisterItem? = null
}