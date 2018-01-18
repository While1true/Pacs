package coms.pacs.pacs.Mvp

/**
 * Created by vange on 2018/1/16.
 */
interface IBaseModel<T> {

    fun loadData():T

    fun destory()
}