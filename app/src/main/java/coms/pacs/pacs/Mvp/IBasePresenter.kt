package coms.pacs.pacs.Mvp

/**
 * Created by vange on 2018/1/16.
 */
interface IBasePresenter<in IBaseView> {

    fun Attached(viewHandler:IBaseView)

    fun onDetached()
}