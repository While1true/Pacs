package coms.pacs.pacs.Utils.Dcm
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PointF
import coms.pacs.pacs.Utils.Dcm.Base.DrawBase

/**
 * Created by 不听话的好孩子 on 2018/1/22.
 */
class Angle : DrawBase() {
    var point1 = PointF()
    var point2 = PointF()
    var point3 = PointF()
    var isfirst = true
    override fun onDraw(canvas: Canvas, offscreen: Canvas, scalePixelSpacing: Float) {
        var canvas = canvas
        if (drawToOffscreen) {
            canvas = offscreen
        }
        paint.color = Color.GREEN
        if (point1 != point2&&point2.length()!=0f) {
            canvas.drawLine(point1.x, point1.y, point2.x, point2.y, paint)
        }
        if(point2 != point3&&point3.length()!=0f){
            canvas.drawLine(point2.x, point2.y, point3.x, point3.y, paint)
            var float=caculateRadius()
            val toString = float.toString()
            if(toString.indexOf(".")==-1){
                return
            }
            paint.color = Color.YELLOW
            canvas.drawText(toString.substring(0, toString.indexOf(".")+2),point2.x,point2.y,paint)
        }
        if (drawToOffscreen&&point3.length() != 0f) {
            reset()
        }
    }

    //   cosB = (a^2 + c^2 - b^2) / (2·a·c)
    private fun caculateRadius(): Float {
        var a=calLen(point3,point2)
        var b=calLen(point1,point3)
        var c=calLen(point2,point1)
        var cosb=(a*a+c*c-b*b)/(2*a*c)
        return (Math.acos(cosb)*180/Math.PI).toFloat()


    }

    private fun calLen(pointF: PointF,pointF2: PointF) : Double{
        return Math.sqrt(Math.pow((pointF.x-pointF2.x).toDouble(),2.0)+Math.pow((pointF.y-pointF2.y).toDouble(),2.0))

    }

    private fun reset() {
        isfirst = true
        point3.set(0f, 0f)
        point2.set(0f, 0f)
        point1.set(0f, 0f)
    }

    override fun MOVE() {
        drawToOffscreen = false
        if(!isfirst){
            point3.set(movePoint)
        }else{
            point2.set(movePoint)
        }
    }

    override fun UP() {
        drawToOffscreen = true
        if(isfirst){
            point2.set(upPoint)
        }else{
            point3.set(upPoint)
        }
        isfirst=false
    }

    override fun DOWN() {
        drawToOffscreen = false
        if (!isfirst)
            point3.set(downPoint)
        else
            point1.set(downPoint)


    }
}