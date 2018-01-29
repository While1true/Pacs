package coms.pacs.pacs.Interfaces

/**
 * Created by 不听话的好孩子 on 2018/1/29.
 */
interface MyCallBack<in T> {
    fun call(t:T)
}