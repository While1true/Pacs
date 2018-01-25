package coms.pacs.pacs.Activity

import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.R
import coms.pacs.pacs.Room.DownStatu
import coms.pacs.pacs.Rx.MyObserver
import coms.pacs.pacs.Utils.DownLoadUtils
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.compare_activity.*

/**
 * Created by 不听话的好孩子 on 2018/1/24.
 */
class CompareActivity:BaseActivity() {
    override fun initView() {
        setTitle("影像对比")
    }

    override fun loadData() {
        val path="https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk"
        DownLoadUtils.downloadWithProgress(path,object :MyObserver<DownStatu>(this){
            override fun onNext(t: DownStatu) {
                super.onNext(t)
                textProgressBar1.reset("下载完成")
                textProgressBar2.reset("下载完成")
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                textProgressBar1.reset("下载失败！用户删除文件")
                textProgressBar2.reset("下载失败！用户删除文件")
            }

            override fun onProgress(it: DownStatu) {
                super.onProgress(it)
                val text= """${String.format("%.2f",(it.current.toFloat()/1024/1024))}MB/${String.format("%.2f",(it.total.toFloat()/1024/1024))}MB """
                textProgressBar1.progress = it.current.toInt()
                textProgressBar1.max = it.total.toInt()
                textProgressBar2.progress = it.current.toInt()
                textProgressBar2.max = it.total.toInt()
                textProgressBar1.text=text
                textProgressBar2.text=text
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.compare_activity
    }
}