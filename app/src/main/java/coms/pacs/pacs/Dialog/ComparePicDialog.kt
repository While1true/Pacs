package coms.pacs.pacs.Dialog

import android.content.Context
import android.os.Bundle
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
import coms.pacs.pacs.BaseComponent.BaseDialog
import coms.pacs.pacs.Interfaces.MyCallBack
import coms.pacs.pacs.Model.CheckImg
import coms.pacs.pacs.R
import coms.pacs.pacs.Rx.DataObserver
import coms.pacs.pacs.Utils.toast
import kotlinx.android.synthetic.main.dialog_root.*
import kotlinx.android.synthetic.main.refreshlayout_elastic.*

/**
 * Created by 不听话的好孩子 on 2018/1/29.
 */
class ComparePicDialog : BaseDialog() {
    override fun layoutId(): Int {
        return R.layout.refreshlayout_elastic
    }

    private var sAdapter: SAdapter<CheckImg>? = null
    var patientcode: String? = null
    var choicelist = ArrayList<String>()
    var maxchoice = 2
    var exactly = true
    var callback: MyCallBack<ArrayList<String>>? = null
    var datas: List<CheckImg>? = null
    override fun initView() {
        setTitle("影像列表：${choicelist.size}/${maxchoice}")
        sAdapter = SAdapter()
        sAdapter!!.apply {
            showStateNotNotify(StateEnum.SHOW_LOADING, "")
            addType(R.layout.check_item, object : ItemHolder<CheckImg>() {
                override fun onBind(p0: Holder, p1: CheckImg, p2: Int) {
                    Glide.with(context).load(p1.thumbnail).into(p0.getView<ImageView>(R.id.imageView))
                    p0.setText(R.id.title, """${p1.checkpart} / ${p1.checktype}""")
                    p0.setText(R.id.subtitle, p1.checkdate)
                    p0.itemView.setOnClickListener {
                        if (choicelist.size >= maxchoice && !choicelist.contains(p1.original)) {
                            choicelist.removeAt(0)
                        }
                        if (choicelist.contains(p1.original)) {
                            choicelist.remove(p1.original)
                        } else {
                            choicelist.add(p1.original)
                        }
                        sAdapter!!.notifyDataSetChanged()
                        setTitle("影像列表：${choicelist.size}/${maxchoice}")
                    }
                    p0.setVisible(R.id.checkTag, choicelist.contains(p1.original))
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
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
            adapter = sAdapter
        }
        if (datas==null||datas!!.isEmpty())
            loadData()
        else{
            sAdapter?.setBeanList(datas)
            sAdapter?.showItem()
        }
    }

    private fun loadData() {
        ApiImpl.apiImpl.getPatientAllImages(patientcode!!)
                .subscribe(object : DataObserver<List<CheckImg>>(this) {
                    override fun OnNEXT(bean: List<CheckImg>?) {
                        datas=bean
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        close.setImageResource(R.drawable.paper_plane)
        close.setOnClickListener {
            if (exactly) {
                if (choicelist.size != maxchoice) {
                    "请选择指定数量".toast()
                    return@setOnClickListener
                }
            } else {
                if (choicelist.isEmpty()) {
                    "请至少选择一张图片".toast()
                    return@setOnClickListener
                }
            }
            callback?.call(choicelist)
        }
    }
}