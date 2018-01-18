package coms.pacs.pacs.Activity

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.view.View
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.Model.DicAttrs
import coms.pacs.pacs.R
import coms.pacs.pacs.Utils.Dcm.draw.PathImageView
import coms.pacs.pacs.Utils.DcmUtils
import coms.pacs.pacs.Utils.toast
import kotlinx.android.synthetic.main.dicwatch_activity.*

/**
 * Created by 不听话的好孩子 on 2018/1/18.
 */
class DcmWatchActivity :BaseActivity() {
    override fun initView() {
        setTitle("影像查看")
        photoView.maxScale=4f
        DcmUtils.desplayDcm(this,"http://10.0.110.127:8080/pacsAndroid/path/1.2.840.113704.1.111.3648.1497930071.54.dcm",object : DcmUtils.DcmCallBack{
            override fun call(bitmap: Bitmap?, attrs: DicAttrs?) {
                photoView.pxSpace=attrs?.pixelSpacing?.toFloat()?:0f
                photoView.drawWhat(PathImageView.Shapex.LINE)
                progressBar.visibility= View.GONE
                photoView.setImageBitmap(bitmap)
            }

            override fun callFailure(message: String?) {
                message.toast()
                progressBar.visibility= View.GONE
            }
        })
    }

    override fun loadData() {
    }

    override fun getLayoutId(): Int {
        return R.layout.dicwatch_activity
    }
}