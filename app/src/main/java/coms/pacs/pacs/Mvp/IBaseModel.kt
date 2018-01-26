package coms.pacs.pacs.Mvp

/**
 * Created by vange on 2018/1/16.
 */
@Deprecated("不太需要，直接调用ApiImpl即可")
interface IBaseModel<T> {

    fun loadData():T
}