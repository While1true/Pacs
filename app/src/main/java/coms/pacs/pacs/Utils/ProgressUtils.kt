package coms.pacs.pacs.Utils

import android.app.ProgressDialog
import android.content.Context

/**
 * Created by 不听话的好孩子 on 2018/1/23.
 */
class ProgressUtils {
    companion object {
        fun CreatProgressDialog(context:Context):ProgressDialog{
            val progressDialog = ProgressDialog(context)
            progressDialog.setTitle("下载中...")
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            progressDialog.setCancelable(false)
            progressDialog.max=0
            progressDialog.setProgressNumberFormat("%1d kb/%2d kb")
            return progressDialog
        }
    }
}