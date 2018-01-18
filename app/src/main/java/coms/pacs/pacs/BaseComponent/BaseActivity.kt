package coms.pacs.pacs.BaseComponent

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import coms.pacs.pacs.R
import coms.pacs.pacs.Rx.Utils.RxLifeUtils
import coms.pacs.pacs.Utils.StateBarUtils
import kotlinx.android.synthetic.main.titlebar_activity.*

/**
 * Created by vange on 2018/1/16.
 */
abstract class BaseActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(needTitle()) {
            setContentView(R.layout.titlebar_activity)
            layoutInflater.inflate(getLayoutId(),fl_content,true)
            iv_back.setOnClickListener { onBack() }
        }
        else
            setContentView(getLayoutId())

        initView()
        loadData()
    }

    abstract fun initView()

    abstract fun loadData()

    abstract fun getLayoutId():Int

     fun setTitle(title:String){
         tv_title.text = title
    }

    open fun needTitle():Boolean{
        return true
    }

    open fun onBack(){
        finish()
    }

    open fun setMenuClickListener(res:Int,listener:View.OnClickListener){
        iv_menu.setOnClickListener(listener)
        if(res!=0){
            iv_menu.setImageResource(res)
        }
        iv_menu.visibility=View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        RxLifeUtils.getInstance().remove(this)
    }


}