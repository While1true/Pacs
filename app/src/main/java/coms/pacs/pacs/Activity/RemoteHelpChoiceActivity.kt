package coms.pacs.pacs.Activity

import android.content.Intent
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.R
import kotlinx.android.synthetic.main.help_layout.*

/**
 * Created by 不听话的好孩子 on 2018/1/30.
 */
class RemoteHelpChoiceActivity : BaseActivity() {

    override fun initView() {
        setTitle("查看消息")
        val intentx=Intent(this@RemoteHelpChoiceActivity, RemoteHelpListActivity::class.java)
        get.setOnClickListener {
            intentx.putExtra("type",0)
            startActivity(intentx)
        }
        send.setOnClickListener {
            intentx.putExtra("type",1)
            startActivity(intentx)
        }
    }

    override fun loadData() {
    }

    override fun getLayoutId()= R.layout.help_layout
}