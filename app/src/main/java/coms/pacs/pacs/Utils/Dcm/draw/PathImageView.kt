package coms.pacs.pacs.Utils.Dcm.draw

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import com.bm.library.PhotoView
import java.lang.reflect.Field

/**
 * Created by 不听话的好孩子 on 2018/1/18.
 */
class PathImageView : PhotoView {

    var pxSpace = 0F

    var drawPath = false

    var point = PointF()
    var downPoint = PointF()
    var movePoint = PointF()
    var UpPoint = PointF()

    var drawImpl: DrawInterface? = null

    var field: Field

    var offscreenBitmap:Bitmap?=null

    init {
        val declaredField = PhotoView::class.java.getDeclaredField("mScale")
        declaredField.isAccessible = true
        field = declaredField
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (drawPath) {

            if(offscreenBitmap==null){
                var offscreenBitmapx= Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_4444)
                offscreenBitmapx.setHasAlpha(true)
                offscreenBitmap=offscreenBitmapx
            }else{
                canvas.drawBitmap(offscreenBitmap,0f,0f,null)
            }
            drawImpl?.onDraw(canvas, offscreenBitmap,pxSpace / getscale())
        }
    }

    open fun getscale(): Float {

        return field.get(this) as Float
    }
    open fun clearDraws(){
        offscreenBitmap?.eraseColor(Color.TRANSPARENT)
    }
    open fun drawWhat(shape: Shapex) {
        drawPath=true
        when (shape) {
            Shapex.LINE -> drawImpl = Line()
            Shapex.CIRCLE -> drawImpl = Line()
            Shapex.ANGLE -> drawImpl = Line()
            Shapex.PATH -> drawImpl = Line()
            Shapex.ARROW -> drawImpl = Line()
            Shapex.RETANGLE -> drawImpl = Line()
            Shapex.LINE -> drawImpl = Line()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (drawPath) {
            disenable()
        } else {
            enable()
        }
        return super.dispatchTouchEvent(event)
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (drawPath) {

            point.set(event.x, event.y)
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    downPoint.set(point)
                    movePoint.set(point)
                    drawImpl?.onDown(downPoint)
                }
                MotionEvent.ACTION_MOVE -> {
                    println(event?.action)
                    movePoint.set(point)
                    drawImpl?.onMove(movePoint)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    UpPoint.set(point)
                    drawImpl?.onUp(UpPoint)
                }
            }
            invalidate()
            return true
        }
        return super.onTouchEvent(event)
    }

    enum class Shapex {
        LINE, CIRCLE, ANGLE, PATH, ARROW, RETANGLE, FOURPOINT
    }
}