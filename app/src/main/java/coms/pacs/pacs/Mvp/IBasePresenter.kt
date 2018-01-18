package coms.pacs.pacs.Mvp

/**
 * Created by vange on 2018/1/16.
 */
interface IBasePresenter<out IBaseView,out IBaseModel> {

    val Iview :IBaseView

    val Imodel:IBaseModel

    fun onStart()

    fun onDestory()
}