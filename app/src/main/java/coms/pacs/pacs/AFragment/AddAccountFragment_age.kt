package coms.pacs.pacs.AFragment

import android.os.Bundle
import android.text.TextUtils
import cn.qqtheme.framework.picker.DatePicker
import coms.pacs.pacs.AFragment.BaseImpl.AddAccountFragment_Base
import coms.pacs.pacs.Model.RegisterItem
import coms.pacs.pacs.R
import coms.pacs.pacs.Utils.pop
import kotlinx.android.synthetic.main.account_age.*
import java.util.*

/**
 * Created by 不听话的好孩子 on 2018/2/1.
 */
class AddAccountFragment_age : AddAccountFragment_Base<RegisterItem>() {
    override fun init(savedInstanceState: Bundle?) {
        val yearx = Calendar.getInstance().get(Calendar.YEAR)
        val monthx = Calendar.getInstance().get(Calendar.MONTH)
        val dayx = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val split = item?.content?.split("-")
        val datePicker = DatePicker(activity!!)

        //设置时间
        if (TextUtils.isEmpty(item?.content)) {
            calculate("1991", "1", "1", yearx, monthx, dayx)
        } else {
            try {
                calculate(split!![0], split[1], split[2], yearx, monthx, dayx)
            } catch (e: Exception) {
            }
        }

        setTitle(item?.title ?: "")

        //时间选择器
        datePicker.setOnDatePickListener(DatePicker.OnYearMonthDayPickListener { year, month, day ->
            calculate(year, month, day, yearx, monthx, dayx)
        })
        datePicker.setCancelable(false)
        datePicker.setRangeStart(1900, 1, 1)
        datePicker.setRangeEnd(yearx, monthx, dayx)
        agelayout.setOnClickListener {
            try {
                datePicker.setSelectedItem(split!![0].toInt(), split[1].toInt(), split[2].toInt())
            } catch (e: Exception) {
                datePicker.setSelectedItem(1991, 1, 1)
            }
            datePicker.show()
        }


        confirm.setOnClickListener {
            if (item?.content != tvbirthday.text.toString()) {
                item?.content = tvbirthday.text.toString()
                item?.age = tvage.text.toString()
                if (callback != null) {
                    callback?.call(item!!)
                }
                pop()
            }
        }
    }

    /**
     * 计算年龄
     */
    private fun calculate(year: String, month: String, day: String, yearx: Int, monthx: Int, dayx: Int) {
        tvbirthday.text = "$year-$month-$day"

        var temp = ""
        if (yearx.toString() == year) {
            if (monthx.toString() == month) {
                item?.unit = "日"
                temp = (dayx - day.toInt()).toString()
            } else {
                item?.unit = "月"
                temp = (monthx - month.toInt() + 1).toString()
            }
        } else {
            item?.unit = "岁"
            temp = (yearx - year.toInt()).toString()
        }
        tvage.text = temp + item?.unit
    }

    override fun getLayoutId() = R.layout.account_age
}