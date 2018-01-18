package coms.pacs.pacs.Activity

import android.graphics.Bitmap
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.Model.DicAttrs
import coms.pacs.pacs.R
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
        DcmUtils.desplayDcm(this,"",object : DcmUtils.DcmCallBack{
            override fun call(bitmap: Bitmap?, attrs: DicAttrs?) {
                photoView.setImageBitmap(bitmap)
            }

            override fun callFailure(message: String?) {
                message.toast()
            }
        })
    }

    override fun loadData() {
    }

    override fun getLayoutId(): Int {
        return R.layout.dicwatch_activity
    }
}