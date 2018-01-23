package coms.pacs.pacs.Utils.Dcm

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import com.bm.library.PhotoView
import coms.pacs.pacs.Utils.Dcm.Base.DrawInterface
import java.lang.reflect.Field

/**
 * Created by 不听话的好孩子 on 2018/1/18.
 */
class PathImageView : PhotoView {

    var pxSpace = 0F

    var drawPath: Boolean = false
        set(value) {
            field = value
            if (offscreenBitmap != null&&!value) {
                clearDraws()
            }
        }

    var point = PointF()
    var downPoint = PointF()
    var movePoint = PointF()
    var UpPoint = PointF()

    var drawImpl: DrawInterface? = null

    var field: Field

    var offscreenCanvas: Canvas? = null
    var offscreenBitmap: Bitmap? = null
    var screenBitmap: Bitmap? = null

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
        if (drawPath || offscreenBitmap != null) {
            if (offscreenBitmap == null) {
                var offscreenBitmapx = Bitmap.createBitmap(screenBitmap?.width!!, screenBitmap?.height!!, Bitmap.Config.ARGB_4444)
                offscreenBitmapx.setHasAlpha(true)
                offscreenBitmap = offscreenBitmapx
                offscreenCanvas = Canvas(offscreenBitmap)

            }
            if (drawPath) {
                drawImpl?.onDraw(canvas, offscreenCanvas!!, pxSpace / getscale())
            }
            canvas.drawBitmap(offscreenBitmap, 0f,0f, null)

        }
    }

    override fun setImageBitmap(bm: Bitmap?) {
        screenBitmap = bm
        super.setImageBitmap(bm)
    }

     fun getscale(): Float {
        val orginalscale = width*1.0f/ (screenBitmap?.width ?: width)
        return field.get(this) as Float *orginalscale
    }

     fun clearDraws() {
        offscreenBitmap?.eraseColor(Color.TRANSPARENT)
        downPoint.set(0f, 0f)
        UpPoint.set(0f, 0f)
        movePoint.set(0f, 0f)
        point.set(0f, 0f)
        invalidate()
    }

     fun drawWhat(drawImpl: DrawInterface) {
        drawPath = true
        this.drawImpl = drawImpl
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
                    drawImpl?.onDown(downPoint)
                }
                MotionEvent.ACTION_MOVE -> {
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
}