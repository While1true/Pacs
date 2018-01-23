package coms.pacs.pacs.Room

import android.arch.persistence.room.*
import coms.pacs.pacs.App

/**
 * Created by 不听话的好孩子 on 2018/1/23.
 */

@Entity
data class DownStatu constructor(
        @PrimaryKey var id: Long=0,
        @ColumnInfo(name = "FileName") var name: String="",
        @ColumnInfo(name = "FilePath") var path: String="",
        @ColumnInfo(name = "url") var url: String=""){constructor() : this(0)}

@Database(entities = [(DownStatu::class)], version = 1,exportSchema = false)
abstract class DataBase : RoomDatabase(){
    abstract fun getDownloadDao(): DownDao
}

class DownloadDao {
    companion object {
        val downDao:DownDao=App.dataBase.getDownloadDao()
    }

}