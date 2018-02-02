package coms.pacs.pacs.AFragment.BaseImpl

import android.os.Bundle
import com.ck.hello.nestrefreshlib.View.Adpater.Base.MyCallBack
import coms.pacs.pacs.BaseComponent.BaseFragment
import coms.pacs.pacs.Model.RegisterItem
import coms.pacs.pacs.R
import coms.pacs.pacs.Utils.pop
import kotlinx.android.synthetic.main.account_sex.*

/**
 * Created by 不听话的好孩子 on 2018/2/1.
 */
abstract class AddAccountFragment_Base : BaseFragment() {
    var callback: MyCallBack<RegisterItem>? = null
    var item: RegisterItem? = null
}