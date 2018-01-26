package coms.pacs.pacs.Mvp

/**
 * Created by vange on 2018/1/16.
 */
interface IBaseView<out X> {
    fun creatPresenter(): X
}