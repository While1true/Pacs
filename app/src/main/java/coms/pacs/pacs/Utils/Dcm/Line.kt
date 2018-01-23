
package coms.pacs.pacs.Utils.Dcm

import android.graphics.Canvas

import android.graphics.*
import coms.pacs.pacs.Utils.Dcm.Base.DrawBase

/**
 * Created by 不听话的好孩子 on 2018/1/18.
 */
class Line : DrawBase() {
    override fun MOVE() {

    }

    override fun UP() {
    }

    override fun DOWN() {
        movePoint.set(0f,0f)
        upPoint.set(0f,0f)
    }


    override fun onDraw(canvas: Canvas, scalePixelSpacing: Float) {
        if (downPoint.length()!=0f&&movePoint.length()!=0f&&calculate(scalePixelSpacing) != "0.0mm") {
            paint.color = Color.RED
            canvas.drawLine(downPoint.x, downPoint.y, movePoint.x, movePoint.y, paint)
            paint.color = Color.GREEN
            canvas.drawText(calculate(scalePixelSpacing),(movePoint.x - downPoint.x)/2+downPoint.x,(movePoint.y - downPoint.y)/2+downPoint.y,paint)
        }
    }

    fun calculate(scale: Float): String {

        return String.format("%.1f",(scale * Math.sqrt(Math.pow((movePoint.x - downPoint.x).toDouble(), 2.0) + Math.pow((movePoint.y - downPoint.y).toDouble(), 2.0))))+"mm"
    }
}