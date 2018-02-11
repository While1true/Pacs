package coms.pacs.pacs.Activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ck.hello.nestrefreshlib.View.Adpater.Base.Holder
import com.ck.hello.nestrefreshlib.View.Adpater.Base.ItemHolder
import com.ck.hello.nestrefreshlib.View.Adpater.Base.StateEnum
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter
import coms.pacs.pacs.Api.ApiImpl
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.Model.CheckImg
import coms.pacs.pacs.R
import coms.pacs.pacs.Rx.DataObserver
import kotlinx.android.synthetic.main.refreshlayout_elastic.*

/**
 * Created by 不听话的好孩子 on 2018/1/29.
 */
class DcmListActivity : BaseActivity() {
    var sAdapter: SAdapter<CheckImg>? = null
    var patientcode: String? = null
    override fun initView() {
        setTitle(getString(R.string.diclist))
        patientcode = intent.getStringExtra("patientcode")
        sAdapter = SAdapter()
        sAdapter!!.apply {
            showStateNotNotify(StateEnum.SHOW_LOADING, "")
            addType(R.layout.check_item, object : ItemHolder<CheckImg>() {
                override fun onBind(p0: Holder, p1: CheckImg, p2: Int) {
                    Glide.with(this@DcmListActivity).load(p1.thumbnail).into(p0.getView<ImageView>(R.id.imageView))
                    p0.setText(R.id.title, """${p1.checkpart} / ${p1.checktype}""")
                    p0.setText(R.id.subtitle, p1.checkdate)
                    p0.itemView.setOnClickListener {
                        var intentx = Intent(this@DcmListActivity, DcmWatchActivity::class.java)
                        intentx.putExtra("imgurl", p1.original)
                        startActivity(intentx)
                    }
                }

                override fun istype(p0: CheckImg?, p1: Int): Boolean {
                    return true
                }

            })

            setStateListener(object : DefaultStateListener() {
                override fun netError(p0: Context?) {
                    loadData()
                }

            })
        }
        val recyclerView = refreshlayout.getmScroll<RecyclerView>()
        recyclerView.apply {
            addItemDecoration(DividerItemDecoration(this@DcmListActivity, LinearLayoutManager.VERTICAL))
            layoutManager = LinearLayoutManager(this@DcmListActivity)
            adapter = sAdapter
        }
    }

    override fun loadData() {
        ApiImpl.apiImpl.getPatientAllImages(patientcode!!)
                .subscribe(object : DataObserver<List<CheckImg>>(this) {
                    override fun OnNEXT(bean: List<CheckImg>?) {
                        if (bean!!.isEmpty()) {
                            sAdapter?.showEmpty()
                        } else {
                            sAdapter?.setBeanList(bean)
                            sAdapter?.showItem()
                        }
                    }

                    override fun OnERROR(error: String?) {
                        super.OnERROR(error)
                        sAdapter?.showState(StateEnum.SHOW_ERROR, error)
                    }
                })
    }

    override fun getLayoutId(): Int {
        return R.layout.refreshlayout_elastic
    }
}