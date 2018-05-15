package coms.pacs.pacs.Activity

import android.content.Intent
import android.text.TextUtils
import coms.pacs.pacs.Api.ApiImpl
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.R
import coms.pacs.pacs.Rx.DataObserver
import coms.pacs.pacs.Utils.K2JUtils
import coms.pacs.pacs.Utils.save
import coms.pacs.pacs.Utils.toast
import kotlinx.android.synthetic.main.login_activity.*

/**
 * Created by vange on 2018/1/16.
 */
class LoginActivity : BaseActivity() {

    override fun loadData() {

    }

    override fun getLayoutId(): Int {
        return R.layout.login_activity
    }

    override fun initView() {
        var account: String = K2JUtils.get("username", "")
        var password: String = K2JUtils.get("password", "")
        if (!TextUtils.isEmpty(account)) {
            et_account.setText(account)
            et_password.setText(password)
        }

        login.setOnClickListener {
            val account: String = et_account.text.toString()
            val password: String = et_password.text.toString()
            if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                getString(R.string.accountpasswordnotnull).toast()
                return@setOnClickListener
            }

            ApiImpl.apiImpl.login(account, password)
                    .doOnNext { it.message.toString().toast() }
                    .filter { it.code==200 }
                    .flatMap { ApiImpl.apiImpl.bindXiaomi(account, K2JUtils.get("miID", "")) }
                    .subscribe(object : DataObserver<Any>(this) {
                        override fun OnNEXT(bean: Any?) {
                            getString(R.string.loginsuccess).toast()
                            if (checkbox.isChecked) {
                                account.save("username")
                                account.save("password")
                            } else {
                                "".save("username")
                                "".save("password")
                            }
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                    })

        }

    }

    override fun needTitle(): Boolean {
        return false
    }

    override fun translateTitle(): Boolean {
        return false
    }
}