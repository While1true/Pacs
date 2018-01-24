package coms.pacs.pacs.Activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ck.hello.nestrefreshlib.View.Adpater.Base.ItemHolder
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter
import com.tbruyelle.rxpermissions2.RxPermissions
import coms.pacs.pacs.Api.ApiImpl
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.Interfaces.RefreshListener
import coms.pacs.pacs.Model.patient
import coms.pacs.pacs.R
import coms.pacs.pacs.Rx.DataObserver
import coms.pacs.pacs.Utils.toast
import kotlinx.android.synthetic.main.refreshlayout.*

/**
 * Created by 不听话的好孩子 on 2018/1/16.
 */
class MainActivity : BaseActivity() {
    var last: Long = 0
    var currentPage=1
    var listpatients=ArrayList<patient>()
    lateinit var sAdapter:SAdapter<patient>
    override fun initView() {

        setTitle("请选择操作对象")


       requestPermission()

        val recyclerview:RecyclerView=refreshlayout.getmScroll()

        refreshlayout.setCanHeader(false)

        refreshlayout.apply {
            setListener(object : RefreshListener(){
                override fun Refreshing() {
                }

                override fun Loading() {
                    loadpage(currentPage++)
                }
            })
        }

        sAdapter=SAdapter<patient>(listpatients).apply {

            addType(R.layout.patient_item,object :ItemHolder<patient>(){
                override fun onBind(p0: SimpleViewHolder?, p1: patient?, p2: Int) {
                    p0?.setText(R.id.name,p1?.name+"/"+(if(p1?.sex==1)"男" else "女")+"/"+ p1?.age)
                    var card = if(p1?.healthcard==null) "无" else p1?.healthcard
                    var cards = if(p1?.healthcard==null) "无" else p1?.healthcards
                    p0?.setText(R.id.card, "医保卡：$card    就诊卡：$cards")
                    p0?.itemView?.setOnClickListener {
                        var intent=Intent(this@MainActivity,MenuActivity::class.java)
                        intent.putExtra("patientcode",p1?.patientcode)
                        startActivity(intent)
                    }
                }

                override fun istype(p0: patient?, p1: Int): Boolean {
                    return true
                }
            })

            setStateListener(object :DefaultStateListener(){
                override fun netError(p0: Context?) {
                    listpatients.clear()
                    loadpage(1)
                }
            })
        }

        recyclerview.apply {
            adapter=sAdapter
            layoutManager=LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
        }

        setMenuClickListener(R.drawable.ic_search, View.OnClickListener {
            startActivity(Intent(this@MainActivity,SearchActivity::class.java))
        })
    }

    var times=1
    private fun requestPermission() {
        RxPermissions(this)
                .request(Manifest.permission.INTERNET,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe {
                    if(!it){
                        "请授权权限，否则无法使用".toast()
                        if(times==1){
                            requestPermission()
                        }else{
                            finish()
                        }
                        times+=1
                    }
                }
    }

    override fun loadData() {
        loadpage(1)
    }

    private fun loadpage(i: Int) {
        ApiImpl.apiImpl.getPatientList("",20,i).subscribe(object :DataObserver<List<patient>>(this){
            override fun OnNEXT(bean: List<patient>?) {
                refreshlayout.NotifyCompleteRefresh0()
                if(currentPage==1&&bean==null){
                    sAdapter.showEmpty()
                }else{
                    listpatients.addAll(bean!!)
                    if(bean?.size!! < 20){
                        refreshlayout.setCanFooter(false)
                        sAdapter.showNomore()
                    }else{
                        sAdapter.showItem()
                    }

                }

            }

            override fun OnERROR(error: String?) {
                super.OnERROR(error)
                if(currentPage==1){
                    sAdapter.ShowError()
                }
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.refreshlayout
    }

    override fun onBackPressed() {
        val currentTimeMillis = System.currentTimeMillis()

        if (currentTimeMillis - last > 2000) {
            last = currentTimeMillis
            toast("再次点击退出", 1)
        } else {
            finish()
        }

    }
}