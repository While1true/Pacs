package coms.pacs.pacs.Activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import com.ck.hello.nestrefreshlib.View.Adpater.Base.Holder
import com.ck.hello.nestrefreshlib.View.Adpater.Base.ItemHolder
import com.ck.hello.nestrefreshlib.View.Adpater.Base.StateEnum
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter
import coms.pacs.pacs.Api.ApiImpl
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.InterfacesAndAbstract.RefreshListener
import coms.pacs.pacs.Model.Base
import coms.pacs.pacs.Model.patient
import coms.pacs.pacs.R
import coms.pacs.pacs.Rx.DataObserver
import coms.pacs.pacs.Rx.Utils.TextWatcher
import coms.pacs.pacs.Utils.InputUtils
import coms.pacs.pacs.Utils.SizeUtils
import coms.pacs.pacs.Utils.StateBarUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
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
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.enterTransition=android.transition.Explode()
        }
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val cardview = findViewById<CardView>(R.id.cardview)
            cardview.maxCardElevation = 0f
            cardview.setContentPadding(0, 0, 0, SizeUtils.dp2px(6f))
        }
    }
    override fun initView() {
        iv_back.setOnClickListener {
            InputUtils.hideKeyboard(et_input)
            onBackPressed()
        }

        initRecyclerview()

        initSearchListener()

    }

    private fun initSearchListener() {
        observer = object : DataObserver<List<patient>>(this) {
            override fun OnNEXT(bean: List<patient>?) {
                listpatients.clear()
                if (currentPage == 1 && (bean == null || bean?.size == 0)) {
                    sAdapter.setBeanList(null)
                    sAdapter.showItem()
                } else {
                    refreshlayout.NotifyCompleteRefresh0()
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
                } else {
                    refreshlayout.NotifyCompleteRefresh0()
                }
            }
        }

        Observable.create(TextWatcher(et_input))
                .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter({
                    content = it
                    if (TextUtils.isEmpty(content)) {
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

    private fun initRecyclerview() {
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
            showStateNotNotify(StateEnum.TYPE_ITEM, "")
            addType(R.layout.search_empty, object : ItemHolder<patient>() {
                override fun onBind(p0: Holder?, p1: patient?, p2: Int) {
                    refreshlayout.setCanFooter(false)
                }

                override fun istype(p0: patient?, p1: Int): Boolean {
                    return listpatients.size == 0
                }
            })

            addType(R.layout.patient_item, object : ItemHolder<patient>() {
                override fun onBind(p0: Holder?, p1: patient, p2: Int) {
                    p0?.setText(R.id.title, p1.name + "/" + (if (p1.sex == 1) getString(R.string.man) else getString(R.string.woman)) + if(p1.age==null) "" else ("/"+p1.age))
                    var card = if (p1.healthcard == null) getString(R.string.no) else p1.healthcard
                    var cards = if (p1.healthcard == null) getString(R.string.no) else p1.healthcards
                    p0?.setText(R.id.card, """${getString(R.string.medicalcard)}：$card    ${getString(R.string.sickcard)}：$cards""")
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

        val recyclerview: RecyclerView = refreshlayout.getmScroll()
        recyclerview.apply {
            adapter = sAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
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

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_search,menu!!)
//        val item = menu.findItem(R.id.toolbar_search)
//        item.expandActionView()
//        searchView = item.actionView as SearchView
//        searchView?.queryHint="姓名/医保卡号/就诊号"
//        val searchtext = searchView?.findViewById<SearchView.SearchAutoComplete>(android.support.v7.appcompat.R.id.search_src_text)
//        searchtext?.setHintTextColor(resources.getColor(R.color.hintcolor))
//        searchtext?.setTextColor(0xffffffff.toInt())
//        val mCloseButton = findViewById<ImageView>(android.support.v7.appcompat.R.id.search_close_btn)
//        mCloseButton.setOnClickListener {  }
//
//        Observable.create(SearchWatcher(searchView!!))
//                .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .filter({
//                    content = it
//                    if(TextUtils.isEmpty(content)) {
//                        sAdapter.setBeanList(null)
//                        listpatients.clear()
//                        sAdapter.showItem()
//                    }
//                    return@filter !TextUtils.isEmpty(content)
//                })
//                .flatMap(Function<String, Observable<Base<List<patient>>>> {
//                    currentPage = 1
//                    return@Function ApiImpl.apiImpl.getPatientList(it, 20, currentPage)
//                })
//                .subscribe(observer)
//
//        return super.onCreateOptionsMenu(menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
////        val searchView = item.actionView as SearchView
//
//        return true
//    }
}