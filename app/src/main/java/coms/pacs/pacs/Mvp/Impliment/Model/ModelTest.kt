package coms.pacs.pacs.Mvp.Impliment.Model

import coms.pacs.pacs.Api.ApiImpl

/**
 * Created by 不听话的好孩子 on 2018/1/26.
 */
@Deprecated("不太需要,我们直接调用ApiImpl")
class ModelTest {

    companion object {
        fun getData(xx:String):Any?=ApiImpl.apiImpl.getPatientReport(xx)
    }
}