package coms.pacs.pacs.Activity

import android.content.Intent
import coms.kxjsj.refreshlayout_master.RefreshLayout
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.Interfaces.RefreshListener
import coms.pacs.pacs.R
import coms.pacs.pacs.Utils.K2JUtils
import kotlinx.android.synthetic.main.help_layout.*

/**
 * Created by 不听话的好孩子 on 2018/1/30.
 */
class RemoteHelpChoiceActivity : BaseActivity() {

    override fun initView() {
        setTitle("查看消息")
        val intentx = Intent(this@RemoteHelpChoiceActivity, RemoteHelpListActivity::class.java)
        if (K2JUtils.get("showIndicateFrom", false)) {
            send.indicate = 999
        }
        if (K2JUtils.get("showIndicateCome", false)) {
            get.indicate = 999
        }
        get.setOnClickListener {
            get.indicate=0
            intentx.putExtra("type", 0)
            startActivity(intentx)
        }
        send.setOnClickListener {
            send.indicate=0
            intentx.putExtra("type", 1)
            startActivity(intentx)
        }
        refreshlayout.setListener(object : RefreshListener() {
            override fun Refreshing() {

            }

            override fun call(t: RefreshLayout.State?, scroll: Int) {
                println(scroll.toString()+" zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")
                header.layoutParams.height=-scroll
                header.requestLayout()
            }

            override fun Loading() {
            }
        })
    }

    override fun loadData() {
    }

    override fun getLayoutId() = R.layout.help_layout
}