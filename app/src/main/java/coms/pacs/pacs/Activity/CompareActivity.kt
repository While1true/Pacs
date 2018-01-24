package coms.pacs.pacs.Activity

import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.Model.DicAttrs
import coms.pacs.pacs.R
import coms.pacs.pacs.Utils.Dcm.DcmUtils
import kotlinx.android.synthetic.main.compare_activity.*
import kotlinx.android.synthetic.main.dicwatch_activity.*

/**
 * Created by 不听话的好孩子 on 2018/1/24.
 */
class CompareActivity:BaseActivity() {
    override fun initView() {
        setTitle("影像对比")
    }

    override fun loadData() {
        val path="http://dlied5.myapp.com/myapp/1104466820/sgame/2017_com.tencent.tmgp.sgame_h161_1.32.1.25_335782.apk"
        DcmUtils.displayDcm(path,{
            val text= """${String.format("%.2f",(it.current.toFloat()/1024/1024))}MB/ ${String.format("%.2f",(it.total.toFloat()/1024/1024))}MB """
            textProgressBar1.progress = it.current.toInt()
            textProgressBar1.max = it.total.toInt()
            textProgressBar2.progress = it.current.toInt()
            textProgressBar2.max = it.total.toInt()
            textProgressBar1.text=text
            textProgressBar2.text=text
        },object :DcmUtils.DcmCallBack{
            override fun call(attrs: DicAttrs?) {
            }

            override fun callFailure(message: String?) {
            }

        })
    }

    override fun getLayoutId(): Int {
        return R.layout.compare_activity
    }
}