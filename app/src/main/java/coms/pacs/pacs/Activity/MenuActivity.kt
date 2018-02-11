package coms.pacs.pacs.Activity

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.ck.hello.nestrefreshlib.View.Adpater.Base.Holder
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.PositionHolder
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter
import coms.pacs.pacs.AFragment.AddAccountFragment
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.R
import coms.pacs.pacs.Utils.dp2px
import coms.pacs.pacs.Utils.showAddFragment
import kotlinx.android.synthetic.main.refreshlayout.*

/**
 * Created by 不听话的好孩子 on 2018/1/18.
 */
class MenuActivity : BaseActivity() {
    val funtions:Array<String>by lazy { arrayOf(getString(R.string.baseinfo), getString(R.string.dic_watch), getString(R.string.dic_compare), getString(R.string.remote_check), "影像报告") }
    var drawables = intArrayOf(R.drawable.water, R.drawable.flower, R.drawable.forest, R.drawable.save_the_world, R.drawable.paper_recycling)
    var intents = arrayOf(DcmWatchActivity::class.java, DcmListActivity::class.java, CompareActivity::class.java, RemotoActivity::class.java, ReportListActivity::class.java)
    private lateinit var patientcode:String
    override fun initView() {
        setTitle(getString(R.string.chosefunction))
        patientcode= intent.getStringExtra("patientcode")
        var recyclerview: RecyclerView = refreshlayout.getmScroll()
        recyclerview.apply {
            layoutManager = GridLayoutManager(this@MenuActivity, 2)
            adapter = SAdapter<Any>(funtions.size)
                    .addType(R.layout.item_menu, object : PositionHolder() {
                        override fun onBind(p0: Holder?, p1: Int) {
                            if (p1 < 2)
                                p0?.itemView?.setPadding(0, dp2px(20f), 0, dp2px(20f))

                            p0?.setText(R.id.tv1, funtions[p1])
                            p0?.setImageResource(R.id.iv1, drawables[p1])

                            p0?.itemView?.setOnClickListener {
                                if (p1 == 0) {
                                    val fragment = AddAccountFragment()
                                    fragment.patientCode=patientcode
                                    showAddFragment(fragment)
                                    return@setOnClickListener
                                }
                                val intent = Intent(this@MenuActivity, intents[p1])
                                intent.putExtra("patientcode",patientcode)
                                startActivity(intent)
                            }

                        }

                        override fun istype(p0: Int): Boolean {
                            return true
                        }

                    })

        }
    }

    override fun loadData() {
    }

    override fun getLayoutId(): Int {

        return R.layout.refreshlayout_elastic
    }
}