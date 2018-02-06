package coms.pacs.pacs.Interfaces

import coms.kxjsj.refreshlayout_master.RefreshLayout

/**
 * Created by 不听话的好孩子 on 2018/1/17.
 */
abstract class RefreshListener:RefreshLayout.Callback() {
    override fun call(p0: RefreshLayout.State?) {
        if(p0==RefreshLayout.State.REFRESHING){
            Refreshing()
        }else if(p0==RefreshLayout.State.LOADING){
            Loading()
        }

    }

    open abstract fun Refreshing()

    open abstract fun Loading()
}