package coms.pacs.pacs.Room

import android.arch.persistence.room.*

/**
 * Created by 不听话的好孩子 on 2018/1/23.
 */
@Dao
interface DownDao {
    @Query("SELECT * FROM DownStatu")
    fun getAll():List<DownStatu>

    @Query("SELECT * FROM DownStatu where id = :arg0")
    fun get(id:Long):DownStatu

    @Query("SELECT * FROM DownStatu where url = :arg0 order by id desc")
    fun get(url:String):DownStatu

    @Insert(onConflict =  OnConflictStrategy.IGNORE)
    fun insert(vararg downStatu: DownStatu)

    @Update(onConflict =  OnConflictStrategy.REPLACE)
    fun update(vararg downStatu: DownStatu)
}