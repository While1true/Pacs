package coms.pacs.pacs.Utils.Dcm

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import coms.pacs.pacs.Utils.Dcm.Base.DrawBase

/**
 * Created by 不听话的好孩子 on 2018/1/22.
 */
class Path : DrawBase() {
    var path = Path()
    override fun DOWN() {
        path.moveTo(downPoint.x, downPoint.y)
    }

    init {
        paint.style = Paint.Style.STROKE
        paint.color=Color.MAGENTA
    }

    override fun MOVE() {
        path.lineTo(movePoint.x, movePoint.y)
    }

    override fun UP() {
        path.lineTo(upPoint.x, upPoint.y)
    }

    override fun onDraw(canvas: Canvas, scaleSpace: Float) {
        canvas.drawPath(path, paint)
        if (drawToOffscreen) {
            path.reset()
        }
    }
}