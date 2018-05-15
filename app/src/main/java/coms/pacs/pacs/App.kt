package coms.pacs.pacs

import android.app.Application
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import com.ck.hello.nestrefreshlib.View.Adpater.Base.Recorder
import com.ck.hello.nestrefreshlib.View.Adpater.Impliment.SAdapter
import coms.pacs.pacs.Utils.AdjustUtil
import coms.kxjsj.refreshlayout_master.MyRefreshWrap
import coms.kxjsj.refreshlayout_master.RefreshLayout
import coms.pacs.pacs.Location.LocationManage
import coms.pacs.pacs.Room.DataBase
import coms.pacs.pacs.Utils.MI.MiPushUtils
import coms.pacs.pacs.Utils.log
import io.reactivex.plugins.RxJavaPlugins
import java.util.*
import kotlin.math.log


/**
 * Created by vange on 2018/1/16.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AdjustUtil.adjust(this)

        LocationManage.init(this)

        RefreshLayout.init(RefreshLayout.DefaultBuilder()
                .setBaseRefreshWrap(MyRefreshWrap::class.java)
                .setHeaderLayoutidDefault(R.layout.header_layout)
                .setFooterLayoutidDefault(R.layout.footer_layout)
                .setScrollLayoutIdDefault(R.layout.recyclerview))

        SAdapter.init(Recorder.Builder()
                .setEmptyRes(R.layout.state_empty)
                .setErrorRes(R.layout.state_error)
                .setLoadingRes(R.layout.state_loading)
                .setNomoreRes(R.layout.state_nomore)
                .build())

        RxJavaPlugins.setErrorHandler { log("AppError: "+(it.message?:"e")) }

        MiPushUtils.init(this)
    }







    init {
        app=this
        dataBase= Room.databaseBuilder(App.app, DataBase::class.java, "database-name")
                .addMigrations(MIG1_2)
                .allowMainThreadQueries()
                .build()
    }
    companion object{
        lateinit var app: App
        lateinit var dataBase: DataBase
        private var MIG1_2=object :Migration(1,2){
            override fun migrate(databasex: SupportSQLiteDatabase) {
                databasex.execSQL("Alter table DownStatu ADD state INTEGER default 0")
            }
        }
    }

}