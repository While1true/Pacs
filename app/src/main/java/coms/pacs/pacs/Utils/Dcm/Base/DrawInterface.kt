package coms.pacs.pacs.Utils.Dcm.Base

import android.graphics.Canvas
import android.graphics.PointF

/**
 * Created by 不听话的好孩子 on 2018/1/18.
 */
interface DrawInterface {

    fun onDown(downPoint: PointF)

    fun onMove(movePoint: PointF)

    fun onUp(upPoint: PointF)

    fun onDraw(canvas:Canvas,offscreen:Canvas,scalePixelSpacing:Float)
}