package coms.pacs.pacs.Utils.Dcm

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import coms.pacs.pacs.Utils.Dcm.Base.DrawBase

/**
 * Created by 不听话的好孩子 on 2018/1/22.
 */
class Rect : DrawBase() {
    var rect = RectF()

    override fun DOWN() {
    }

    override fun MOVE() {
        upPoint.set(movePoint)
    }

    override fun UP() {

    }

    init {
        paint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas, scalePixelSpacing: Float) {
        if (movePoint.length() != 0f) {
            paint.color = Color.BLUE
            val calculate = calculate(scalePixelSpacing)
            canvas.drawRect(rect, paint)
            paint.color = Color.GREEN
            canvas.drawText(calculate,downPoint.x,downPoint.y, paint)
        }
        if (drawToOffscreen) {
            movePoint.set(0f, 0f)
            upPoint.set(0f, 0f)
            downPoint.set(0f, 0f)
        }
    }

    private fun calculate(scalePixelSpacing: Float): String {
        rect.set(Math.min(downPoint.x, upPoint.x), Math.min(downPoint.y, upPoint.y), Math.max(downPoint.x, upPoint.x), Math.max(downPoint.y, upPoint.y))
        var xx = (scalePixelSpacing * Math.abs(downPoint.x - upPoint.x))
        var yy = (scalePixelSpacing * Math.abs(downPoint.y - upPoint.y))
        var space = xx * yy
        return """${xx.toString().substring(0,xx.toString().indexOf("."))} mm * ${yy.toString().substring(0,yy.toString().indexOf("."))} mm= ${space.toString().substring(0,space.toString().indexOf("."))} mm^2"""
    }

}