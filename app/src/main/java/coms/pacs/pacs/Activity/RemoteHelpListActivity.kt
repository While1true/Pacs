package coms.pacs.pacs.Activity

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.ck.hello.nestrefreshlib.View.Adpater.Base.StateEnum
import coms.pacs.pacs.Api.ApiImpl
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.BaseComponent.MyListAdapter
import coms.pacs.pacs.InterfacesAndAbstract.MyCallBack
import coms.pacs.pacs.Model.HelpBean
import coms.pacs.pacs.R
import coms.pacs.pacs.Rx.DataObserver
import coms.pacs.pacs.Utils.K2JUtils
import kotlinx.android.synthetic.main.refreshlayout_elastic.*

/**
 * Created by 不听话的好孩子 on 2018/1/30.
 */
class RemoteHelpListActivity : BaseActivity() {
    var sAdapter: MyListAdapter<HelpBean>? = null
    var beans: MutableList<HelpBean>? = ArrayList()
    override fun initView() {
        val intent = Intent(this@RemoteHelpListActivity, RemoteHelpDetailActivity::class.java)
        val recyclerView = refreshlayout.getmScroll<RecyclerView>()
        sAdapter = MyListAdapter(list = beans)
        sAdapter!!.itemClickCallBack = object : MyCallBack<HelpBean> {
            override fun call(t: HelpBean) {
                intent.putExtra("bean", t)
                startActivityForResult(intent, 100)
            }

        }
        sAdapter?.showStateNotNotify(StateEnum.SHOW_LOADING, "")
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@RemoteHelpListActivity)
            adapter = sAdapter
            addItemDecoration(DividerItemDecoration(this@RemoteHelpListActivity, LinearLayout.VERTICAL))
        }
    }

    override fun loadData() {
        var account: String = K2JUtils.get("username", "")
        val type = intent.getIntExtra("type", 0)

        //清除上一界面角标
        if (type == 1) {
            K2JUtils.put("showIndicateFrom", false)
        } else {
            K2JUtils.put("showIndicateCome", false)
        }

        setTitle((if (type == 0) "收到的请求" else "发出的请求"))
        ApiImpl.apiImpl.getHelpViewList(account, type)
                .subscribe(object : DataObserver<List<HelpBean>>(this) {
                    override fun OnNEXT(bean: List<HelpBean>) {
                        beans!!.addAll(bean)
                        sAdapter?.showItem()
                    }

                    override fun OnERROR(error: String?) {
                        super.OnERROR(error)
                        sAdapter?.ShowError()
                    }

                })
    }

    override fun getLayoutId() = R.layout.refreshlayout_elastic

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (100 == requestCode && resultCode == Activity.RESULT_OK) {
            beans?.clear()
            loadData()
        }
    }
}