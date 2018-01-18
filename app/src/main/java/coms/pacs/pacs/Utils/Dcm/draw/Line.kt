package coms.pacs.pacs.Utils.Dcm.draw

import android.graphics.*
import coms.pacs.pacs.Utils.dp2px

/**
 * Created by 不听话的好孩子 on 2018/1/18.
 */
class Line : DrawInterface {
    var movePoint: PointF? = null
    var downPoint: PointF? = null
    var paint: Paint

    var drawToOffscreen=false

    init {
        paint = Paint()
        paint.flags = paint.flags or Paint.ANTI_ALIAS_FLAG
        paint.strokeWidth = dp2px(1f).toFloat()
        paint.strokeCap = Paint.Cap.ROUND
    }

    override fun onDown(downPointx: PointF) {
        drawToOffscreen=false
        downPoint = downPointx
    }

    override fun onUp(upPoint: PointF) {
        drawToOffscreen=true
    }

    override fun onMove(movePointx: PointF) {
        drawToOffscreen=false
        movePoint = movePointx
    }

    override fun onDraw(canvas: Canvas,offscreen:Bitmap?, scalePixelSpacing: Float) {
        var canvas=canvas
        if(drawToOffscreen){
           canvas=Canvas(offscreen)
        }
        if (downPoint!=null&& movePoint!=null&&!calculate(scalePixelSpacing).equals("0.0mm")) {
            paint.color = Color.RED
            canvas.drawLine(downPoint!!.x, downPoint!!.y, movePoint!!.x, movePoint!!.y, paint)
            paint.color = Color.GREEN
            canvas.drawText(calculate(scalePixelSpacing),(movePoint!!.x - downPoint!!.x)/2+downPoint!!.x,(movePoint!!.y - downPoint!!.y)/2+downPoint!!.y,paint)
        }
    }

    fun calculate(scale: Float): String {

        return String.format("%.1f",(scale * Math.sqrt(Math.pow((movePoint!!.x - downPoint!!.x).toDouble(), 2.0) + Math.pow((movePoint!!.y - downPoint!!.y).toDouble(), 2.0))))+"mm"
    }
}