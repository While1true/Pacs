package coms.pacs.pacs.Activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import com.ck.hello.nestrefreshlib.View.Adpater.Base.ItemHolder
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter
import coms.pacs.pacs.Api.ApiImpl
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.Interfaces.RefreshListener
import coms.pacs.pacs.Model.Base
import coms.pacs.pacs.Model.patient
import coms.pacs.pacs.R
import coms.pacs.pacs.Rx.DataObserver
import coms.pacs.pacs.Rx.Utils.TextWatcher
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.search_activity.*
import java.util.concurrent.TimeUnit

/**
 * Created by 不听话的好孩子 on 2018/1/16.
 */
class SearchActivity : BaseActivity() {
    var currentPage = 1
    var listpatients = ArrayList<patient>()
    var content: String = ""
    lateinit var observer : DataObserver<List<patient>>
    lateinit var sAdapter: SAdapter<patient>
    override fun initView() {

        val recyclerview: RecyclerView = refreshlayout.getmScroll()

        iv_back.setOnClickListener { finish() }

        refreshlayout.apply {
            refreshlayout.setCanHeader(false)
            setListener(object : RefreshListener() {
                override fun Refreshing() {
                }

                override fun Loading() {
                    loadPager(++currentPage)
                }
            })
        }
        sAdapter = SAdapter<patient>(1).apply {

            showStateNotNotify(SAdapter.TYPE_ITEM, "")

            addType(R.layout.search_empty, object : ItemHolder<patient>() {
                override fun onBind(p0: SimpleViewHolder?, p1: patient?, p2: Int) {
                    refreshlayout.setCanFooter(false)
                }

                override fun istype(p0: patient?, p1: Int): Boolean {
                    return listpatients.size==0
                }
            })

            addType(R.layout.patient_item, object : ItemHolder<patient>() {
                override fun onBind(p0: SimpleViewHolder?, p1: patient?, p2: Int) {
                    p0?.setText(R.id.name, p1?.name + "/" + (if (p1?.sex == 1) "男" else "女")+"/"+p1?.age)
                    var card = if(p1?.healthcard==null) "无" else p1?.healthcard
                    var cards = if(p1?.healthcard==null) "无" else p1?.healthcards
                    p0?.setText(R.id.card, "医保卡：$card    就诊卡：$cards")
                    p0?.itemView?.setOnClickListener {
                        var intent = Intent(this@SearchActivity, MenuActivity::class.java)
                        intent.putExtra("patientcode", p1?.patientcode)
                        startActivity(intent)
                    }
                }

                override fun istype(p0: patient?, p1: Int): Boolean {
                    return true
                }
            })

            setStateListener(object : DefaultStateListener() {
                override fun netError(p0: Context?) {
                    listpatients.clear()
                    currentPage = 1
                    loadPager(1)
                }
            })
        }

        recyclerview.apply {
            adapter = sAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }

        observer=object :DataObserver<List<patient>>(this) {
            override fun OnNEXT(bean: List<patient>?) {
                listpatients.clear()
                if (currentPage == 1 && (bean == null||bean?.size == 0)) {
                    sAdapter.setBeanList(null)
                    sAdapter.showItem()
                } else {
                    listpatients.addAll(bean!!)
                    if (bean?.size!! < 20) {
                        refreshlayout.setCanFooter(false)
                    }
                    sAdapter.setBeanList(listpatients)
                    sAdapter.showItem()
                }

            }

            override fun OnERROR(error: String?) {
                super.OnERROR(error)
                if (currentPage == 1) {
                    sAdapter.ShowError()
                    refreshlayout.setCanFooter(false)
                }
            }
        }

        Observable.create(TextWatcher(et_input))
                .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter({
                    content = it
                    if(TextUtils.isEmpty(content)) {
                        sAdapter.setBeanList(null)
                        listpatients.clear()
                        sAdapter.showItem()
                    }
                    return@filter !TextUtils.isEmpty(content)
                })
                .flatMap(Function<String, Observable<Base<List<patient>>>> {
                    currentPage = 1
                    return@Function ApiImpl.apiImpl.getPatientList(it, 20, currentPage)
                })
                .subscribe(observer)


    }

    override fun loadData() {

    }

    private fun loadPager(i: Int) {
        ApiImpl.apiImpl.getPatientList(content!!, 20, i).subscribe(observer)

    }

    override fun getLayoutId(): Int {
        return R.layout.search_activity
    }

    override fun needTitle(): Boolean {
        return false
    }
}