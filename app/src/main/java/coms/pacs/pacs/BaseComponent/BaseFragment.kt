package coms.pacs.pacs.BaseComponent

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coms.pacs.pacs.R
import coms.pacs.pacs.Rx.Utils.RxLifeUtils
import kotlinx.android.synthetic.main.titlebar_activity.*

/**
 * Created by vange on 2018/1/16.
 */
abstract class BaseFragment : Fragment() {
    var contentView: View? = null
    private var firestLoad = true
    private var viewCreated = false
    private var isvisable = false
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var inflate: View? = null
        if (needTitle()) {
            inflate = layoutInflater.inflate(R.layout.titlebar_activity, container, false)
            layoutInflater.inflate(getLayoutId(), inflate.findViewById(R.id.fl_content) as ViewGroup, true)
            inflate.findViewById<View>(R.id.iv_back).setOnClickListener { onBack() }

        } else {
            contentView = contentView()
            if (contentView == null) {
                contentView = layoutInflater.inflate(getLayoutId(), null)
            }
            inflate = contentView
        }
        return inflate
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
        viewCreated = true
        if (firestLoad && isvisable) {
            firestLoad = false
            loadLazy()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isvisable = isVisibleToUser
        if (isvisable && viewCreated && firestLoad) {
            firestLoad = false
            loadLazy()
        }
    }

    abstract fun getLayoutId(): Int
    abstract fun init(savedInstanceState: Bundle?)

    open fun loadLazy() {}

    open fun contentView(): View? = null
    open fun needTitle(): Boolean {
        return true
    }

    open fun onBack() {
        fragmentManager.popBackStack()
    }

    fun setTitle(title: String) {
        tv_title.text = title
    }

    open fun setMenuClickListener(res: Int, listener: View.OnClickListener) {
        iv_menu.setOnClickListener(listener)
        if (res != 0) {
            iv_menu.setImageResource(res)
        }
        iv_menu.visibility = View.VISIBLE
    }

    open fun setAddClickListener(res: Int, listener: View.OnClickListener) {
        iv_add.setOnClickListener(listener)
        if (res != 0) {
            iv_add.setImageResource(res)
        }
        iv_add.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        RxLifeUtils.getInstance().remove(this)
    }

    fun stop(){
        fragmentManager.popBackStack()
    }

}