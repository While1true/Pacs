package coms.pacs.pacs.Utils.Dcm.draw

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PointF

/**
 * Created by 不听话的好孩子 on 2018/1/18.
 */
interface DrawInterface {

    fun onDown(downPoint: PointF)

    fun onMove(movePoint: PointF)

    fun onUp(upPoint: PointF)

    fun onDraw(canvas:Canvas,offscreen:Bitmap?,scalePixelSpacing:Float)
}