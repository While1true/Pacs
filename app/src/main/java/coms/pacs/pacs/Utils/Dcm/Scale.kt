package coms.pacs.pacs.Utils.Dcm

import android.graphics.*
import android.graphics.Rect
import coms.pacs.pacs.Utils.Dcm.Base.DrawBase
import coms.pacs.pacs.Utils.dp2px

/**
 * Created by 不听话的好孩子 on 2018/1/22.
 */
class Scale(cache: Bitmap) : DrawBase() {
    var cacheBitmap:Bitmap = cache
    var src=Rect()
    var desc=RectF()
    var radius=0
    var scale=1f
    val porterDuffXfermode:PorterDuffXfermode

    var point:PointF= PointF()
    override fun DOWN() {
        point.set(downPoint)
    }

    override fun MOVE() {
        point.set(movePoint)
    }

    override fun UP() {
        point.set(upPoint)
    }
    init {
        radius=dp2px(30f)
        scale=3f
        porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    override fun onDraw(canvas: Canvas, offscreen: Canvas, scalePixelSpacing: Float) {
        point.x-=scale * radius*0.70711f
        point.y-=scale * radius*0.70711f
        src.set(point.x.toInt() - radius, point.y.toInt() - radius, point.x.toInt() + radius, point.y.toInt() + radius)
        desc.set(point.x - scale * radius, point.y - scale * radius, point.x + scale * radius, point.y + scale * radius)
        val save = canvas.saveLayer(0f,0f, cacheBitmap.width.toFloat(), cacheBitmap.height.toFloat(),null,Canvas.ALL_SAVE_FLAG)
        canvas.drawCircle(point.x, point.y, scale * radius, paint)
        paint.xfermode = porterDuffXfermode
        if(!cacheBitmap.isRecycled)
            canvas.drawBitmap(cacheBitmap, src, desc, paint)
        paint.xfermode = null
        canvas.restoreToCount(save)
    }
    override fun onDraw(canvas: Canvas, scaleSpace: Float) {

    }
}