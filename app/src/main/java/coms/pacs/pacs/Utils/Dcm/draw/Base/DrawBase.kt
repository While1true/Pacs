package coms.pacs.pacs.Utils.Dcm.draw

import android.graphics.Paint
import android.graphics.PointF
import coms.pacs.pacs.Utils.Dcm.Base.DrawInterface
import coms.pacs.pacs.Utils.dp2px

/**
 * Created by 不听话的好孩子 on 2018/1/22.
 */
abstract class DrawBase: DrawInterface {

    var downPoint= PointF()
    var movePoint= PointF()
    var upPoint= PointF()

    var paint: Paint

    var drawToOffscreen=false

    init {
        paint = Paint()
        paint.textSize=dp2px(12f).toFloat()
        paint.flags = paint.flags or Paint.ANTI_ALIAS_FLAG
        paint.strokeWidth = dp2px(1f).toFloat()
        paint.strokeCap = Paint.Cap.ROUND
    }
    override fun onMove(movePoint: PointF) {
        this.movePoint.set(movePoint)
        MOVE()
    }

    override fun onUp(upPoint: PointF) {
        this.upPoint.set(upPoint)
        UP()
    }

    override fun onDown(downPoint: PointF) {
        this.downPoint.set(downPoint)
        DOWN()
    }

    open abstract fun DOWN()
    open abstract fun MOVE()
    open abstract fun UP()

}