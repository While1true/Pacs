package coms.pacs.pacs

import android.app.Application
import coms.pacs.pacs.Utils.AdjustUtil
import coms.kxjsj.refreshlayout_master.MyRefreshWrap
import coms.kxjsj.refreshlayout_master.RefreshLayout



/**
 * Created by vange on 2018/1/16.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AdjustUtil.adjust(this)

        RefreshLayout.init(RefreshLayout.DefaultBuilder()
                .setBaseRefreshWrap(MyRefreshWrap::class.java)
                .setHeaderLayoutidDefault(R.layout.header_layout)
                .setFooterLayoutidDefault(R.layout.footer_layout)
                .setScrollLayoutIdDefault(R.layout.recyclerview))
    }







    init {
        app=this
    }
    companion object{
        lateinit var app: App
    }

}