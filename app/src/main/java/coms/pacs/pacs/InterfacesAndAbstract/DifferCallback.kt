package coms.pacs.pacs.InterfacesAndAbstract

import android.support.v7.util.DiffUtil

/**
 * Created by 不听话的好孩子 on 2018/2/7.
 */
class DifferCallback<T : IListDateModel> : DiffUtil.Callback {
    val oldList: List<T>
    val newList: List<T>

    constructor(oldList: List<T>, newList: List<T>) {
        this.newList = newList
        this.oldList = oldList
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = newList.get(newItemPosition).getTitle() == oldList.get(oldItemPosition).getTitle()

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = newList.get(newItemPosition).getSubTitle() == oldList.get(oldItemPosition).getSubTitle()

}