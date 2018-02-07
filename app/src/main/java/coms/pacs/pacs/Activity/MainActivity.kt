package coms.pacs.pacs.Activity

import android.Manifest
import android.animation.RectEvaluator
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import com.ck.hello.nestrefreshlib.View.Adpater.Base.Holder
import com.ck.hello.nestrefreshlib.View.Adpater.Base.StateEnum
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.BaseHolder
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter
import com.tbruyelle.rxpermissions2.RxPermissions
import coms.pacs.pacs.Api.ApiImpl
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.AFragment.AddAccountFragment
import coms.pacs.pacs.InterfacesAndAbstract.DifferCallback
import coms.pacs.pacs.InterfacesAndAbstract.RefreshListener
import coms.pacs.pacs.Model.Base
import coms.pacs.pacs.Model.Constance
import coms.pacs.pacs.Model.patient
import coms.pacs.pacs.R
import coms.pacs.pacs.Rx.DataObserver
import coms.pacs.pacs.Rx.MyObserver
import coms.pacs.pacs.Rx.Utils.RxBus
import coms.pacs.pacs.Utils.K2JUtils
import coms.pacs.pacs.Utils.pop
import coms.pacs.pacs.Utils.showReplaceFragment
import coms.pacs.pacs.Utils.toast
import kotlinx.android.synthetic.main.main_layout.*
import kotlinx.android.synthetic.main.titlebar_activity.*

/**
 * Created by 不听话的好孩子 on 2018/1/16.
 */
class MainActivity : BaseActivity() {
    var last: Long = 0
    var currentPage = 1
    var listpatients = ArrayList<patient>()
    lateinit var sAdapter: SAdapter<patient>
    override fun initView() {

        setTitle("请选择操作对象")

        iv_back.visibility = View.GONE

        requestPermission()

        initRecyclerview()

        //float view
        indicate.setOnClickListener {
            indicate.indicate = 0
            startActivity(Intent(this@MainActivity, RemoteHelpChoiceActivity::class.java))
        }

        //receive Message
        RxBus.getDefault().toObservable(Base::class.java)
                .subscribe(object : MyObserver<Base<*>>(this) {
                    override fun onNext(t: Base<*>) {
                        super.onNext(t)
                        if (t.code == Constance.RECEIVE_NOTIFICATION) {
                            indicate.indicate = indicate.indicate + 1
                        } else if (t.code == Constance.RECEIVE_UPDATE_ADDNEWPATIENT) {
                            currentPage = 1
                            listpatients.clear()
                            loadpage(1)
                        }
                    }
                })
    }

    private fun initRecyclerview() {
        val recyclerview: RecyclerView = refreshlayout.getmScroll()

        refreshlayout.apply {

//            setCanHeader(false)

            setListener(object : RefreshListener() {
                override fun Refreshing() {
                    currentPage = 1
                    loadpage(currentPage)
                }

                override fun Loading() {
                    loadpage(currentPage++)
                }
            })
        }

        sAdapter = SAdapter(listpatients).apply {

            showStateNotNotify(StateEnum.SHOW_LOADING, "")
            addType(object : BaseHolder<patient>(R.layout.patient_item) {
                override fun onViewBind(p0: Holder, p1: patient, p2: Int) {
                    p0?.setText(R.id.title, p1?.name + "/" + (if (p1?.sex == 1) "男" else "女") + "/" + p1?.age)
                    var card = if (p1?.healthcard == null) "无" else p1?.healthcard
                    var cards = if (p1?.healthcard == null) "无" else p1?.healthcards
                    p0?.setText(R.id.card, "医保卡：$card    就诊卡：$cards")
                    p0?.itemView?.setOnClickListener {
                        var intent = Intent(this@MainActivity, MenuActivity::class.java)
                        intent.putExtra("patientcode", p1?.patientcode)
                        startActivity(intent)
                    }
                }
            })

            setStateListener(object : DefaultStateListener() {
                override fun netError(p0: Context?) {
                    listpatients.clear()
                    loadpage(1)
                }
            })
        }

        recyclerview.apply {
            adapter = sAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
        SAdapter(ArrayList<String>())
    }

    var times = 1
    private fun requestPermission() {
        RxPermissions(this)
                .request(Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
                .subscribe {
                    if (!it) {
                        "请授权权限，否则无法使用".toast()
                        if (times == 1) {
                            requestPermission()
                        } else {
                            finish()
                        }
                        times += 1
                    }
                }
    }

    override fun loadData() {
        loadpage(1)
    }

    private fun loadpage(i: Int) {
        ApiImpl.apiImpl.getPatientList("", 20, i).subscribe(object : DataObserver<List<patient>>(this) {
            override fun OnNEXT(bean: List<patient>?) {
                refreshlayout.NotifyCompleteRefresh0()
                if (currentPage == 1 && bean == null) {
                    refreshlayout.setCanFooter(false)
                    sAdapter.showEmpty()
                } else {
                    if(currentPage==1)
                        listpatients.clear()
                    refreshlayout.setCanFooter(true)
                    refreshlayout.NotifyCompleteRefresh0()
                    listpatients.addAll(bean!!)
                    if (bean?.size!! < 20) {
                        refreshlayout.setCanFooter(false)
                        sAdapter.showNomore()
                    } else {
                        sAdapter.showItem()
                    }
                }

            }

            override fun OnERROR(error: String?) {
                super.OnERROR(error)
                if (currentPage == 1) {
                    refreshlayout.setCanFooter(false)
                    sAdapter.ShowError()
                } else {
                    refreshlayout.NotifyCompleteRefresh0()
                }
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.main_layout
    }

    override fun onBack() {
        AlertDialog.Builder(this)
                .setTitle("确认退出吗？")
                .setPositiveButton("确认", { _, _ -> super.onBack() })
                .setNegativeButton("取消", { _, _ -> })
                .create().show()

    }

    override fun onBackPressed() {
        if (!pop()) {
            val currentTimeMillis = System.currentTimeMillis()

            if (currentTimeMillis - last > 2000) {
                last = currentTimeMillis
                toast("再次点击退出", 1)
            } else {
                finish()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> showReplaceFragment(AddAccountFragment())
            R.id.toolbar_search -> startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            R.id.loginout -> {
                K2JUtils.put("username", "")
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }
        return true
    }
}