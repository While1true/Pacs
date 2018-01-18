package coms.pacs.pacs.Utils.Dcm.draw;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by vange on 2015/8/14.
 */
public class DrewAngle extends DrawBS {
    private Point cenPoint;
    private Point lPoint1, lPoint2;
    private Rect lPoint1Rect, lPoint2Rect;
    List list = new ArrayList();
    DrawBean bean;
    Boolean a = true;
    int i = 0;

    public DrewAngle() {
        // TODO Auto-generated constructor stub
        cenPoint = new Point();
        lPoint1 = new Point();
        lPoint2 = new Point();
        lPoint1Rect = new Rect();
        lPoint2Rect = new Rect();
    }

    public void onTouchDown(Point point) {

        downPoint.set(point.x, point.y);
        if (i % 2 == 1) {
            downState = 1;
        } else if (lPoint1Rect.contains(point.x, point.y)) {
            Log.d("onTouchDown", "downState = 1");
            downState = 1;
        } else if (lPoint2Rect.contains(point.x, point.y)) {
            Log.d("onTouchDown", "downState = 2");
            downState = 2;
        } else if (panduan(point)) {
            Log.d("onTouchDown", "downState = 3");
            downState = 3;
        } else {
            Log.d("onTouchDown", "downState = 0");
            downState = 0;
            if (a) {

            }
        }
    }

    public void onTouchMove(Point point) {

        switch (downState) {
            case 1:
                lPoint1.set(point.x, point.y);
                moveState = 1;
                break;
            case 2:
                lPoint2.set(point.x, point.y);
                moveState = 2;
                break;
            case 3:
                cenPoint.x = (lPoint2.x + lPoint1.x) / 2;
                cenPoint.y = (lPoint2.y + lPoint1.y) / 2;
                int movedX = point.x - cenPoint.x;
                int movedY = point.y - cenPoint.y;
                lPoint1.x = lPoint1.x + movedX;
                lPoint1.y = lPoint1.y + movedY;
                lPoint2.x = lPoint2.x + movedX;
                lPoint2.y = lPoint2.y + movedY;
                moveState = 3;
                break;
            default:
                lPoint1.set(downPoint.x, downPoint.y);
                lPoint2.set(point.x, point.y);
                break;
        }
    }

    public void onTouchUp(Point point) {
        Log.d("vivi", "long:" + Math.sqrt((lPoint1.x - lPoint2.x)
                * (lPoint1.x - lPoint2.x) + (lPoint1.y - lPoint2.y)
                * (lPoint1.y - lPoint2.y)));
        lPoint1Rect.set(lPoint1.x - 25, lPoint1.y - 25, lPoint1.x + 25,
                lPoint1.y + 25);
        lPoint2Rect.set(lPoint2.x - 25, lPoint2.y - 25, lPoint2.x + 25,
                lPoint2.y + 25);
        if (downState == 0) {
            bean = new DrawBean(lPoint1.x, lPoint1.y, lPoint2.x, lPoint2.y);
            list.add(bean);
            i++;
        }

        Log.d("onTouchDown", i + "list = " + list.size());
    }

    double lDis;

    public boolean panduan(Point point) {
        lDis = Math.sqrt((lPoint1.x - lPoint2.x)
                * (lPoint1.x - lPoint2.x) + (lPoint1.y - lPoint2.y)
                * (lPoint1.y - lPoint2.y));
        double lDis1 = Math.sqrt((point.x - lPoint1.x) * (point.x - lPoint1.x)
                + (point.y - lPoint1.y) * (point.y - lPoint1.y));
        double lDis2 = Math.sqrt((point.x - lPoint2.x) * (point.x - lPoint2.x)
                + (point.y - lPoint2.y) * (point.y - lPoint2.y));
        if (lDis1 + lDis2 >= lDis + 0.00f && lDis1 + lDis2 <= lDis + 5.00f) {
            return true;
        } else {
            return false;
        }
    }

    public void onDraw(Canvas canvas, float s, float s2, boolean isDraw, int left, int top, int  right, int buttom) {
        // TODO Auto-generated method stub
      /*  if (isDraw) {
            lPoint1.x=lPoint1.x<=left?left:(lPoint1.x>=right?right:lPoint1.x);
            lPoint1.y=lPoint1.y<=top?top:(lPoint1.y>=buttom?buttom:lPoint1.y);
            lPoint2.x= lPoint2.x<=left?left:(lPoint2.x>=right?right: lPoint2.x);
            lPoint2.y=lPoint2.y<=top?top:(lPoint1.y>=buttom?buttom: lPoint2.y);*/
            if (i % 2 == 1) {
                DrawBean bean = (DrawBean) list.get(i - 1);
                canvas.drawLine(bean.getPx1(), bean.getPy1(), lPoint1.x, lPoint1.y, paint);
                canvas.drawLine(bean.getPx1(), bean.getPy1(), bean.getPx2(), bean.getPy2(), paint);
                canvas.drawCircle(lPoint1.x, lPoint1.y, 1 / s2, newpaint);
                canvas.drawCircle(bean.getPx1(), bean.getPy1(), 10 / s2, paint);
                LineLong(canvas, s, s2);
            } else if (i % 2 == 0) {
                canvas.drawLine(lPoint1.x, lPoint1.y, lPoint2.x, lPoint2.y, paint);
                canvas.drawCircle(lPoint1.x, lPoint1.y, 1 / s2, newpaint);
            }
       // }

    }

    private void LineLong(Canvas canvas, float s, float s2) {
        Rect targetRect = new Rect((bean.getPx2() + bean.getPx1()) / 2, (bean.getPy2() + bean.getPy1()) / 2, (lPoint1.x + bean.getPx1()) / 2, ((lPoint1.y + bean.getPy1())) / 2);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(2);
        paint.setTextSize(10 / s2);
        double B = Math.sqrt((bean.getPx2() - bean.getPx1()) * (bean.getPx2() - bean.getPx1()) + (bean.getPy2() - bean.getPy1()) * (bean.getPy2() - bean.getPy1()));
        double C = Math.sqrt((lPoint1.y - bean.getPy1()) * (lPoint1.y - bean.getPy1()) + (lPoint1.x - bean.getPx1()) * (lPoint1.x - bean.getPx1()));
        double A = Math.sqrt((bean.getPx2() - lPoint1.x) * (bean.getPx2() - lPoint1.x) + (bean.getPy2() - lPoint1.y) * (bean.getPy2() - lPoint1.y));
        Log.d("vivi", A + "@" + B + "@" + C);
        double num = Math.toDegrees(Math.acos((B * B + C * C - A * A) / (2 * B * C)));
        String testString = "∠"+(double) ((int) ((num + 0.05) * 10)) / 10 + "°";
        paint.setColor(Color.TRANSPARENT);
        canvas.drawRect(targetRect, paint);
        paint.setColor(Color.CYAN);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = targetRect.top + (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(testString, targetRect.centerX(), baseline, paint);
    }

    public interface textListener {
        void Text2(int text2);
    }

    textListener listener;

    public void onTextListener(textListener listener) {
        this.listener = listener;
        listener.Text2((int) lDis);
    }
}

