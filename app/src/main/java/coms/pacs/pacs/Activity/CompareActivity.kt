package coms.pacs.pacs.Activity

import android.view.View
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.Dialog.ComparePicDialog
import coms.pacs.pacs.Interfaces.MyCallBack
import coms.pacs.pacs.Model.DicAttrs
import coms.pacs.pacs.R
import coms.pacs.pacs.Room.DownStatu
import coms.pacs.pacs.Rx.MyObserver
import coms.pacs.pacs.Utils.Dcm.DcmUtils
import kotlinx.android.synthetic.main.compare_activity.*

/**
 * Created by 不听话的好孩子 on 2018/1/24.
 */
class CompareActivity:BaseActivity() {
    override fun loadData() {
    }

    lateinit var dialog:ComparePicDialog
    override fun initView() {
        setTitle("影像对比")
        dialog= ComparePicDialog()
        dialog.apply {
            patientcode=intent.getStringExtra("patientcode")
            callback=object : MyCallBack<ArrayList<String>>{
                override fun call(t: ArrayList<String>) {
                    dismiss()
                   for (i in 0 until t.size){
                       loadData(t[i],i)
                   }
                }

            }
            show(supportFragmentManager)
        }

        compare.setOnClickListener { dialog.show(supportFragmentManager) }
    }

     fun loadData(path:String,who:Int) {
         if(who==0){
             textProgressBar1.text="准备下载中..."
         }else{
             textProgressBar2.text="准备下载中..."
         }
        DcmUtils.displayDcm(path,object :MyObserver<DicAttrs>(this){
            override fun onNext(t: DicAttrs) {
                super.onNext(t)
                if(who==0) {
                    pathView1.setImageBitmap(t.bitmap)
                    textProgressBar1.visibility= View.GONE
                }else {
                    pathView2.setImageBitmap(t.bitmap)
                    textProgressBar2.visibility= View.GONE
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                if(who==0) {
                    textProgressBar1.reset("下载失败！用户删除文件")
                }else {
                    textProgressBar2.reset("下载失败！用户删除文件")
                }
            }

            override fun onProgress(it: DownStatu) {
                super.onProgress(it)
                val text= """${String.format("%.2f",(it.current.toFloat()/1024/1024))}MB/${String.format("%.2f",(it.total.toFloat()/1024/1024))}MB """
                if(who==0) {
                    textProgressBar1.progress = it.current.toInt()
                    textProgressBar1.max = it.total.toInt()
                    textProgressBar1.text = text
                }else {
                    textProgressBar2.progress = it.current.toInt()
                    textProgressBar2.max = it.total.toInt()
                    textProgressBar2.text = text
                }
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.compare_activity
    }
}