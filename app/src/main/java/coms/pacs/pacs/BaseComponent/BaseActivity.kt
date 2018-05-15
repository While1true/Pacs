package coms.pacs.pacs.BaseComponent

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.view.View
import coms.pacs.pacs.R
import coms.pacs.pacs.Rx.Utils.RxLifeUtils
import coms.pacs.pacs.Utils.SizeUtils
import coms.pacs.pacs.Utils.StateBarUtils
import kotlinx.android.synthetic.main.titlebar_activity.*

/**
 * Created by vange on 2018/1/16.
 */
abstract class BaseActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(translateTitle()){
            StateBarUtils.performTransStateBar(window)
        }
        if(needTitle()) {
            setContentView(R.layout.titlebar_activity)
            setSupportActionBar(toolbar)
            layoutInflater.inflate(getLayoutId(), fl_content, true)
            handleTitlebar()
            iv_back.setOnClickListener { onBack() }
        }
        else {
            var view=setView()
            if ( view== null)
                setContentView(getLayoutId())
            else
                setContentView(view)
        }

        initView()
        loadData()
    }

    private fun handleTitlebar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val cardview = findViewById<CardView>(R.id.cardview)
            cardview.maxCardElevation = 0f
            cardview.setContentPadding(0, 0, 0, SizeUtils.dp2px(6f))
        }
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
    open fun translateTitle():Boolean{
        return true
    }

    open fun onBack(){
        onBackPressed()
    }
    open fun setView():View?{return null}

    open fun setMenuClickListener(res:Int,listener:View.OnClickListener){
        iv_menu.setOnClickListener(listener)
        if(res!=0){
            iv_menu.setImageResource(res)
        }
        iv_menu.visibility=View.VISIBLE
    }

    open fun setAddClickListener(res:Int,listener:View.OnClickListener){
        iv_add.setOnClickListener(listener)
        if(res!=0){
            iv_add.setImageResource(res)
        }
        iv_add.visibility=View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        RxLifeUtils.getInstance().remove(this)
    }


}