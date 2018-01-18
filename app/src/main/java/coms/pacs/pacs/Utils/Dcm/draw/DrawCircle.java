package coms.pacs.pacs.Utils.Dcm.draw;

/**
 * Created by vange on 2015/8/13.
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

/*
 * 画圆
 *
 * 无论拖动也好，拉伸也好，其实都是重新画圆，
 * 只是不改变画圆需要的某些属性进行绘制，这样看起来就行是移动或拖动的
 */
public class DrawCircle extends DrawBS {

    private Point rDotsPoint;//圆心
    private int radius = 0;//半径
    private Double dtance = 0.0;//当前点到圆心的距离

    public DrawCircle() {
        // TODO Auto-generated constructor stub
        rDotsPoint = new Point();
    }


    public void onTouchDown(Point point) {
        downPoint.set(point.x, point.y);
        Log.d("vivi", "圆形1");
        if (radius != 0) {
            //计算当前所点击的点到圆心的距离
            dtance = Math.sqrt((downPoint.x - rDotsPoint.x)
                    * (downPoint.x - rDotsPoint.x)
                    + (downPoint.y - rDotsPoint.y)
                    * (downPoint.y - rDotsPoint.y));
            // 如果距离半径减20和加20个范围内，则认为用户点击在圆上
            if (dtance >= radius - 20 && dtance <= radius + 20) {
                downState = 1;
                //如果距离小于半径，则认为用户点击在圆内
            } else if (dtance < radius) {
                downState = -1;
                // 当前点击的点在园外
            } else if (dtance > radius) {
                downState = 0;
            }
        } else {
            downState = 0;
        }
    }


    public void onTouchMove(Point point) {
        Log.d("vivi", "圆形2");
        switch (downState) {
            //如果在圆内，则重新设定圆心坐标
            case -1:
                rDotsPoint.set(point.x, point.y);
                break;
            //如果在圆上，则圆心坐标不变，重新设定圆的半径
            case 1:
                radius = (int) Math.sqrt((point.x - rDotsPoint.x)
                        * (point.x - rDotsPoint.x)
                        + (point.y - rDotsPoint.y)
                        * (point.y - rDotsPoint.y));
                break;
            //如果在圆外，重新画圆
            default:
                rDotsPoint.set(downPoint.x, downPoint.y);
                radius = (int) Math.sqrt((point.x - rDotsPoint.x)
                        * (point.x - rDotsPoint.x)
                        + (point.y - rDotsPoint.y)
                        * (point.y - rDotsPoint.y));
                break;
        }
    }


    public void onDraw(Canvas canvas, float s, float s2, boolean isDraw, int left, int top, int  right, int buttom) {
        // TODO Auto-generated method stub
        Log.d("vivi", "圆形3");
        if(isDraw) {
           int X= rDotsPoint.x-left<=right-rDotsPoint.x?rDotsPoint.x-left:right-rDotsPoint.x;
            int Y=rDotsPoint.y-top<=buttom-rDotsPoint.y?rDotsPoint.y-top:buttom-rDotsPoint.y;
            int XorY=X>=Y?Y:X;
            radius=radius>=XorY?XorY:radius;
            canvas.drawCircle(rDotsPoint.x, rDotsPoint.y, radius, paint);// 画圆
            LineLong(canvas, s, s2);
        }
    }

    int lDis;

    private void LineLong(Canvas canvas, float s, float s2) {
        float textSize=10 / s2;
        Rect targetRect = new Rect(rDotsPoint.x, rDotsPoint.y, rDotsPoint.x, rDotsPoint.y);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(2);
        paint.setTextSize(textSize);
        lDis =(int)(s*s* radius * radius * 3.14);
        String testString = "area:" + (int) (lDis * s) + "\n" + "girth:" + (int) (2 * radius * 3.14 * s);
//        paint.setColor(Color.TRANSPARENT);
//        canvas.drawRect(targetRect, paint);
       paint.setColor(Color.CYAN);
//        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
//        int baseline = targetRect.top + (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
//        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
//        paint.setTextAlign(Paint.Align.CENTER);
 //       canvas.drawText(testString, targetRect.centerX(), baseline, paint);
        String firstText="area:" + lDis+"mm²";
        String secondText="girth:" + (int) (2 * radius * 3.14*s)+"mm";
        canvas.drawText(firstText, rDotsPoint.x - ((firstText.length() * textSize) / 4),rDotsPoint.y-textSize/2,paint);
        canvas.drawText(secondText, rDotsPoint.x - ((secondText.length() * textSize) / 4),rDotsPoint.y+textSize/2,paint);
    }
}

