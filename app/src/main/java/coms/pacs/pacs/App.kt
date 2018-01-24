package coms.pacs.pacs

import android.app.Application
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import coms.pacs.pacs.Utils.AdjustUtil
import coms.kxjsj.refreshlayout_master.MyRefreshWrap
import coms.kxjsj.refreshlayout_master.RefreshLayout
import coms.pacs.pacs.Room.DataBase


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