package coms.pacs.pacs.Utils.Dcm.draw;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by vange on 2015/8/15.
 */
public class DrawRectangle extends DrawBS {

    private Point point1, point2, point3, point4, cenPoint;
    private Rect rect;
    private Rect point1Rect, point2Rect, point3Rect, point4Rect;

    public DrawRectangle() {
        // TODO Auto-generated constructor stub
        point1 = new Point();
        point2 = new Point();
        point3 = new Point();
        point4 = new Point();
        cenPoint = new Point();
        rect = new Rect();
    }

    public void onTouchDown(Point point) {
        downPoint.set(point.x, point.y);

		/*
         * 判断以矩形顶点point2为中心的小矩形point2Rect是否为空，
		 *
		 * 为什么要判断point2Rect而不是point1Rect？
		 * 如果用户在当前页面只点击一下，也会产生point1Rect而不会产生point2Rect。
		 * 只有point1Rect而没有point2Rect判断是没有意义的
		 * 而如果point2Rect != null，则说明当前页面已经有画好的矩形了，可见进行判断用户多点击的点和矩形的关系
		 */
        if (point2Rect != null) {
            //判断用户所点击的点是否在矩形顶点point1为中心的矩形point1Rect内，
            //如果在这个矩形内，则我们认为用户点击了该点
            if (point1Rect.contains(downPoint.x, downPoint.y)) {
                downState = 1;
            } else if (point2Rect.contains(downPoint.x, downPoint.y)) {
                downState = 2;
            } else if (point3Rect.contains(downPoint.x, downPoint.y)) {
                downState = 3;
            } else if (point4Rect.contains(downPoint.x, downPoint.y)) {
                downState = 4;
            } else if (rect.contains(downPoint.x, downPoint.y)) {
                downState = 5;
            } else {
                downState = 0;
            }
        }

    }

    public void onTouchMove(Point point) {
        movePoint.set(point.x, point.y);

        // 根据用户所点击的坐标点画相应的矩形
        switch (downState) {
            case 1:
                //点击点point1，则point2不变；根据point1、point2重新设置点point3,point4
                point1.set(point.x, point.y);
                point3.set(point1.x, point2.y);
                point4.set(point2.x, point1.y);
                moveState = 1;
                break;
            case 2:
                //点击点point2，则point1不变；根据point1、point2重新设置点point3,point4
                point2.set(point.x, point.y);
                point3.set(point1.x, point2.y);
                point4.set(point2.x, point1.y);
                moveState = 2;
                break;
            case 3:
                //点击点point3，则重新设置矩形点point3、1、2
                point3.set(point.x, point.y);
                point1.set(point3.x, point4.y);
                point2.set(point4.x, point3.y);
                moveState = 3;
                break;
            case 4:
                //点击点point4，则重新设置矩形点point4、1、2
                point4.set(point.x, point.y);
                point1.set(point3.x, point4.y);
                point2.set(point4.x, point3.y);
                moveState = 4;
                break;
            case 5:
                // 计算矩形中间点
                cenPoint.x = (point1.x + point2.x) / 2;
                cenPoint.y = (point1.y + point2.y) / 2;
                // 移动距离
                int movedX = point.x - cenPoint.x;
                int movedY = point.y - cenPoint.y;

                point1.x = point1.x + movedX;
                point1.y = point1.y + movedY;
                point2.x = point2.x + movedX;
                point2.y = point2.y + movedY;
                point3.set(point1.x, point2.y);
                point4.set(point2.x, point1.y);
                moveState = 5;
                break;
            default:
                getStartPoint();
                moveState = 0;
                break;
        }
        //每次拖动完之后需要重新设定4个顶点小矩形。
        setRect();

    }


    // 设置矩形的开始点与结束点pont1/point2
    public void getStartPoint() {

        if (downPoint.x < movePoint.x && downPoint.y < movePoint.y) {
            point1.set(downPoint.x, downPoint.y);
            point2.set(movePoint.x, movePoint.y);
            point3.set(point1.x, point2.y);
            point4.set(point2.x, point1.y);
        } else if (downPoint.x < movePoint.x && downPoint.y > movePoint.y) {
            point3.set(downPoint.x, downPoint.y);
            point4.set(movePoint.x, movePoint.y);
            point1.set(point3.x, point4.y);
            point2.set(point4.x, point3.y);
        } else if (downPoint.x > movePoint.x && downPoint.y > movePoint.y) {
            point2.set(downPoint.x, downPoint.y);
            point1.set(movePoint.x, movePoint.y);
            point3.set(point1.x, point2.y);
            point4.set(point2.x, point1.y);
        } else if (downPoint.x > movePoint.x && downPoint.y < movePoint.y) {
            point4.set(downPoint.x, downPoint.y);
            point3.set(movePoint.x, movePoint.y);
            point1.set(point3.x, point4.y);
            point2.set(point4.x, point3.y);
        }

        setRect();

    }

    public void setRect() {
        // 设置以矩形的4个顶点为中心的小矩形
        point1Rect = new Rect(point1.x - 50, point1.y - 50, point1.x + 50,
                point1.y + 50);
        point2Rect = new Rect(point2.x - 50, point2.y - 50, point2.x + 50,
                point2.y + 50);
        point3Rect = new Rect(point3.x - 50, point3.y - 50, point3.x + 50,
                point3.y + 50);
        point4Rect = new Rect(point4.x - 50, point4.y -50, point4.x + 50,
                point4.y +50);


     //   rect.set(point1.x, point1.y, point2.x, point2.y);

    }


    public void onDraw(Canvas canvas, float s, float s2, boolean isDraw, int left, int top, int  right, int buttom) {
        // TODO Auto-generated method stub
        if(isDraw) {
            point1.x=point1.x<=left?left:(point1.x>=right?right:point1.x);
            point1.y=point1.y<=top?top:(point1.y>=buttom?buttom:point1.y);
            point2.x=point2.x<=left?left:(point2.x>=right?right:point2.x);
            point2.y=point2.y<=top?top:(point2.y>=buttom?buttom:point2.y);
            rect.set(point1.x, point1.y, point2.x, point2.y);
            canvas.drawRect(rect, paint);// 画矩形
            canvas.drawCircle(point1.x, point1.y, 1 / s2, newpaint);
            canvas.drawCircle(point2.x, point2.y, 1 / s2, newpaint);
            canvas.drawCircle(point1.x, point2.y, 1 / s2, newpaint);
            canvas.drawCircle(point2.x, point1.y, 1 / s2, newpaint);
            LineLong(canvas, s, s2);
        }
    }

    int lDis;

    private void LineLong(Canvas canvas, float s, float s2) {
        float textSize=10/s2;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(2);
        paint.setTextSize(textSize);
        lDis = (int) Math.abs((point2.x - point1.x) * (point1.y- point2.y)* Math.pow(s,2));
      //  String testString = "area:" + (lDis * s * s ) + "\n" + "girth:" + (Math.abs((point4.x - point1.x)) + Math.abs((point1.y - point3.y))) * 2 * s * s;
      //  paint.setColor(Color.TRANSPARENT);
       // canvas.drawRect(rect, paint);
        paint.setColor(Color.CYAN);
     //   Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
     //   int baseline = rect.top + (rect.bottom - rect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
     //   paint.setTextAlign(Paint.Align.CENTER);
      //  canvas.drawText(testString, rect.centerX(), baseline, paint);
        String firstText= "area:" + lDis+"mm²";
        String secondText="girth:" + (int)((Math.abs((point2.x - point1.x)) + Math.abs((point1.y - point2.y))) * 2 * s)+"mm";
        canvas.drawText(firstText,rect.centerX()-(firstText.length()*textSize/4),rect.centerY()-(textSize/2),paint);
        canvas.drawText(secondText, rect.centerX() - (secondText.length() * textSize / 4), rect.centerY() + (textSize /2),paint);
       Log.d("abcde", secondText.length() * textSize / 2+"");
    }
}
