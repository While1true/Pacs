package coms.pacs.pacs.Activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.ck.hello.nestrefreshlib.View.Adpater.Base.Holder
import com.ck.hello.nestrefreshlib.View.Adpater.Base.ItemHolder
import com.ck.hello.nestrefreshlib.View.Adpater.Base.StateEnum
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter
import coms.pacs.pacs.Api.ApiImpl
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.Model.Base
import coms.pacs.pacs.Model.Constance
import coms.pacs.pacs.Model.ReportTitle
import coms.pacs.pacs.R
import coms.pacs.pacs.Rx.DataObserver
import coms.pacs.pacs.Rx.MyObserver
import coms.pacs.pacs.Rx.Utils.RxBus
import kotlinx.android.synthetic.main.refreshlayout_elastic.*

/**
 * Created by 不听话的好孩子 on 2018/1/25.
 */
class ReportListActivity : BaseActivity() {
    private lateinit var patientcode:String
    private var sAdapter: SAdapter<ReportTitle>?=null

    override fun initView() {
        setTitle("报告列表")
        patientcode= intent.getStringExtra("patientcode")
        var recyclerview = refreshlayout.getmScroll<RecyclerView>()
        sAdapter= SAdapter<ReportTitle>()
        .apply {
            showStateNotNotify(StateEnum.SHOW_LOADING,"")
            addType(R.layout.patient_item,object : ItemHolder<ReportTitle>(){
                override fun onBind(p0: Holder?, p1: ReportTitle?, p2: Int) {
                    p0?.setText(R.id.title,"""${p1?.name?:""}/${p1?.sex?:""}/${p1?.checktype?:""}""")
                    p0?.setText(R.id.card,"""${p1?.checkpart?:""}/${p1?.registertime?:""}""")
                    p0?.itemView?.setOnClickListener {
                        val intent=Intent(this@ReportListActivity,ReportDetailActivity::class.java)
                        intent.putExtra("checkupcode",p1?.checkupcode)
                        startActivity(intent)
                    }
                }

                override fun istype(p0: ReportTitle?, p1: Int): Boolean {
                    return true
                }

            })

            setStateListener(object :DefaultStateListener(){
                override fun netError(p0: Context?) {
                    loadData()
                }
            })
        }
        recyclerview.apply {
            layoutManager=LinearLayoutManager(context)
            adapter=sAdapter
            addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
        }

        RxBus.getDefault().toObservable(Base::class.java)
                .filter { it.code==Constance.RECEIVE_UPDATE_REPORTLIST }
                .subscribe(object : MyObserver<Base<*>>(this){
                    override fun onNext(t: Base<*>) {
                        super.onNext(t)
                        loadData()
                    }
                })
    }

    override fun loadData() {
        ApiImpl.apiImpl
                .getPatientAllReports(patientcode)
                .subscribe(object : DataObserver<List<ReportTitle>>(this){
                    override fun OnNEXT(bean: List<ReportTitle>?) {
                        if(bean?.size==0){
                            sAdapter?.showEmpty()
                        }else{
                            sAdapter?.setBeanList(bean)
                            sAdapter?.showItem()
                        }
                    }

                    override fun OnERROR(error: String?) {
                        super.OnERROR(error)
                        sAdapter?.ShowError()
                    }
                })
    }

    override fun getLayoutId() = R.layout.refreshlayout_elastic
}