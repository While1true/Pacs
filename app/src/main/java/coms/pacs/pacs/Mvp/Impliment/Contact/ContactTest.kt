package coms.pacs.pacs.Mvp.Impliment.Contact

import coms.pacs.pacs.Mvp.IBasePresenter
import coms.pacs.pacs.Mvp.IBaseView

/**
 * Created by 不听话的好孩子 on 2018/1/26.
 */
interface ContactTest {


    interface ViewTest:IBaseView<PresenterTest>{
        fun setData(xx:Any?)
    }
    interface PresenterTest:IBasePresenter<ViewTest>{
        fun loadData(page:Int)
    }
}