package coms.pacs.pacs.Views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.ProgressBar
import coms.pacs.pacs.R
import coms.pacs.pacs.Utils.dp2px
import java.lang.reflect.Field

/**
 * Created by 不听话的好孩子 on 2018/1/24.
 */
class TextProgressBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ProgressBar(context, attrs, defStyleAttr) {
    val textColor:Int
    val textSize:Float
    var text:String?=null
    set(value) {
        field=value
        invalidate()
    }
    val gravity:Int//0:Left 1:Center 2:Right 3:move
    private val paint:Paint
    private val fontMetrics:Paint.FontMetrics
    private var textLeft:Float
    private val field:Field
    var drawable:Drawable?=null
    init {
        val mAttrs = context.obtainStyledAttributes(attrs, R.styleable.TextProgressBar)
        textColor=mAttrs.getColor(R.styleable.TextProgressBar_textColor,resources.getColor(R.color.colorAccent))
        textSize=mAttrs.getDimension(R.styleable.TextProgressBar_textSize, dp2px(14f).toFloat())
        text=mAttrs.getString(R.styleable.TextProgressBar_text)
        gravity=mAttrs.getInt(R.styleable.TextProgressBar_textGravity,3)
        paint= Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize=textSize
        paint.color=textColor
        fontMetrics = paint.fontMetrics
        textLeft= paddingLeft.toFloat()
        field=ProgressBar::class.java.getDeclaredField("mCurrentDrawable")
        field.isAccessible=true
        progress=0
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(!TextUtils.isEmpty(text)){
            if(gravity==1){
                textLeft=width/2.0f-paint.measureText(text)/2
            }else if(gravity==2){
                textLeft= (width-paddingRight).toFloat()
            }else if(gravity==3){
                textLeft=paddingLeft+(width-paddingLeft-paddingRight-paint.measureText(text))*progress/max
            }

            var heightx=0
            if(drawable==null) {
                drawable= field.get(this) as Drawable?
            }
            if(drawable!=null){
                heightx= drawable!!.intrinsicHeight
            }
            val caculateBaseLine = caculateBaseLine((height / 2-heightx/2-paint.measureText("1")))
            canvas?.drawText(text,textLeft,caculateBaseLine,paint)
        }
    }
    
    fun caculateBaseLine(drawCentre:Float):Float{
        val dy = (fontMetrics.top - fontMetrics.bottom) / 2+fontMetrics.bottom
        return drawCentre-dy
    }

}