package coms.pacs.pacs.Dialog

import android.content.Context
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ck.hello.nestrefreshlib.View.Adpater.Base.Holder
import com.ck.hello.nestrefreshlib.View.Adpater.Base.ItemHolder
import com.ck.hello.nestrefreshlib.View.Adpater.Base.StateEnum
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.DefaultStateListener
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter
import coms.pacs.pacs.Api.ApiImpl
import coms.pacs.pacs.BaseComponent.BaseDialog
import coms.pacs.pacs.Interfaces.MyCallBack
import coms.pacs.pacs.Model.Doctor
import coms.pacs.pacs.R
import coms.pacs.pacs.Rx.DataObserver
import coms.pacs.pacs.Rx.MyObserver
import coms.pacs.pacs.Utils.K2JUtils
import coms.pacs.pacs.Utils.dp2px
import io.reactivex.Observable
import kotlinx.android.synthetic.main.recyclerview.*

/**
 * Created by 不听话的好孩子 on 2018/1/29.
 */
class RemoteDoctorsDialog : BaseDialog() {
    override fun layoutId(): Int {
        return R.layout.recyclerview
    }

    private var sAdapter: SAdapter<Doctor>? = null
    var patientcode: String? = null
    var callback: MyCallBack<Doctor>? = null
    var datas: List<Doctor>? = null
    override fun initView() {
        setTitle("医生列表")
        sAdapter = SAdapter()
        sAdapter!!.apply {
            showStateNotNotify(StateEnum.SHOW_LOADING, "")
            addType(R.layout.textview, object : ItemHolder<Doctor>() {
                override fun onBind(p0: Holder, p1: Doctor, p2: Int) {
                    p0.setText(R.id.tv, p1.expertname)
                    p0.getView<View>(R.id.tv).setPadding(0, dp2px(16f), 0, dp2px(16f))
                    p0.getView<View>(R.id.tv).layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    p0.itemView.setOnClickListener {
                        callback?.call(p1)
                        dismiss()
                    }
                }

                override fun istype(p0: Doctor?, p1: Int): Boolean {
                    return true
                }

            })

            setStateListener(object : DefaultStateListener() {
                override fun netError(p0: Context?) {
                    loadData()
                }

            })
        }
        recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = sAdapter
            addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        }
        if (datas == null || datas!!.isEmpty())
            loadData()
        else {
            sAdapter?.setBeanList(datas)
            sAdapter?.showItem()
        }
    }

    private fun loadData() {
        ApiImpl.apiImpl.getDoctorList()
                .subscribe(object : DataObserver<List<Doctor>>(this) {
                    override fun OnNEXT(bean: List<Doctor>?) {
                        if (bean!!.isEmpty()) {
                            sAdapter?.showEmpty()
                        } else {
                            //过滤掉自己
                            var account: String = K2JUtils.get("username", "")
                            Observable.fromIterable(bean)
                                    .filter { t -> t.expertcode != account }
                                    .buffer(bean!!.size - 1)
                                    .subscribe(object : MyObserver<List<Doctor>>(this@RemoteDoctorsDialog) {
                                        override fun onNext(t: List<Doctor>) {
                                            super.onNext(t)
                                            datas = t
                                            sAdapter?.setBeanList(datas)
                                            sAdapter?.showItem()
                                        }
                                    })
                        }
                    }

                    override fun OnERROR(error: String?) {
                        super.OnERROR(error)
                        sAdapter?.showState(StateEnum.SHOW_ERROR, error)
                    }
                })
    }
}