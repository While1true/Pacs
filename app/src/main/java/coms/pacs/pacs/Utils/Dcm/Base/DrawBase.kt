package coms.pacs.pacs.Utils.Dcm.Base

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import coms.pacs.pacs.Utils.dp2px


/**
 * Created by 不听话的好孩子 on 2018/1/22.
 */
abstract class DrawBase : DrawInterface {

    var downPoint = PointF()
    var movePoint = PointF()
    var upPoint = PointF()

    var paint: Paint

    var drawToOffscreen = false

    init {
        paint = Paint()
        paint.textSize = dp2px(12f).toFloat()
        paint.flags = paint.flags or Paint.ANTI_ALIAS_FLAG
        paint.strokeWidth = dp2px(1f).toFloat()
        paint.strokeCap = Paint.Cap.ROUND
    }

    override fun onMove(movePoint: PointF) {
        this.movePoint.set(movePoint)
        drawToOffscreen = false
        MOVE()
    }

    override fun onUp(upPoint: PointF) {
        this.upPoint.set(upPoint)
        drawToOffscreen = true
        UP()
    }

    override fun onDown(downPoint: PointF) {
        this.downPoint.set(downPoint)
        drawToOffscreen = false
        DOWN()
    }

    /**
     * 只管画，手抬起就画到上层的图片上
     */
    override fun onDraw(canvas: Canvas, offscreen: Canvas, scalePixelSpacing: Float) {
        var canvasx = canvas
        if (drawToOffscreen)
            canvasx = offscreen
        onDraw(canvasx, scalePixelSpacing)
    }

    abstract fun DOWN()
    abstract fun MOVE()
    abstract fun UP()
    abstract fun onDraw(canvas: Canvas, scaleSpace: Float)

}