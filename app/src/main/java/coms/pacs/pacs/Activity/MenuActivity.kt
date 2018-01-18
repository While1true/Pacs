package coms.pacs.pacs.Activity

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ck.hello.nestrefreshlib.View.Adpater.Base.SimpleViewHolder
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.PositionHolder
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.R
import coms.pacs.pacs.Utils.dp2px
import kotlinx.android.synthetic.main.refreshlayout.*

/**
 * Created by 不听话的好孩子 on 2018/1/18.
 */
class MenuActivity:BaseActivity() {
    var funtions=arrayOf("病症采集","影像调阅","影像对比","远程会诊","影像报告")
    var drawables= intArrayOf(R.drawable.water,R.drawable.flower,R.drawable.forest,R.drawable.save_the_world,R.drawable.paper_recycling)
    var intents= arrayOf(DcmWatchActivity::class.java,DcmWatchActivity::class.java,DcmWatchActivity::class.java,DcmWatchActivity::class.java,DcmWatchActivity::class.java)
    override fun initView() {
        setTitle("选择功能")
        var recyclerview:RecyclerView=refreshlayout.getmScroll()
        recyclerview.apply {
            layoutManager=GridLayoutManager(this@MenuActivity,2)
            adapter=SAdapter<Any>(funtions.size)
                    .addType(R.layout.item_menu,object :PositionHolder(){
                        override fun onBind(p0: SimpleViewHolder?, p1: Int) {
                            if(p1<2)
                                p0?.itemView?.setPadding(0,dp2px(20f),0,dp2px(20f))

                            p0?.setText(R.id.tv1,funtions[p1])
                            p0?.setImageResource(R.id.iv1,drawables[p1])
                            p0?.itemView?.setOnClickListener{
                                startActivity(Intent(this@MenuActivity,intents[p1]))
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