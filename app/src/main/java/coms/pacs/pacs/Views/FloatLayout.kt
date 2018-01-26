package coms.pacs.pacs.Views

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import coms.pacs.pacs.Utils.ViewUtils

/**
 * Created by 不听话的好孩子 on 2018/1/26.
 * 子viewTag为float会被抓住
 */
class FloatLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    var MOVE_FLAT = "float"
    var sticky = true
    private var viewDragHelper: ViewDragHelper? = null
    val callback = object : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View?, pointerId: Int)= MOVE_FLAT == child?.tag

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            var verticalscroll = 0
            if (top > height - child.height) {
                verticalscroll = height - child.height
            } else {
                if (top > 0)
                    verticalscroll = top
            }
            return verticalscroll
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            var horizontalScroll = 0
            if (left > width - child.width) {
                horizontalScroll = width - child.width
            } else {
                if (left > 0)
                    horizontalScroll = left
            }
            return horizontalScroll
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return height - child.height
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return width - child.width
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            if (sticky) {
                if (releasedChild.left + releasedChild.width / 2 > width / 2) {
                    viewDragHelper?.settleCapturedViewAt(width - releasedChild.width, releasedChild.top)
                } else {
                    viewDragHelper?.settleCapturedViewAt(0, releasedChild.top)
                }
                invalidate()
            }
        }
    }

    override fun computeScroll() {
        if (viewDragHelper!!.continueSettling(true)) {
           invalidate()
        }
    }

    init {
        viewDragHelper = ViewDragHelper.create(this, 1f, callback)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return viewDragHelper!!.shouldInterceptTouchEvent(ev)
    }



    override fun onTouchEvent(event: MotionEvent): Boolean {
        viewDragHelper?.processTouchEvent(event)
        for (childid in 0..childCount) {
            if (MOVE_FLAT == getChildAt(childid).tag) {
                return ViewUtils.isPointInChildBounds(this,getChildAt(childid), event.x.toInt(), event.y.toInt())
            }
        }

        return true
    }
}