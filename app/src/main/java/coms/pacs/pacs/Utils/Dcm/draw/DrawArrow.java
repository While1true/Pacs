package coms.pacs.pacs.Utils.Dcm.draw;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by vange on 2015/8/15.
 */
public class DrawArrow extends DrawBS {
    private Point cenPoint;
    private Point lPoint1, lPoint2;
    private Rect lPoint1Rect, lPoint2Rect;

    public DrawArrow() {
        // TODO Auto-generated constructor stub
        cenPoint = new Point();
        lPoint1 = new Point();
        lPoint2 = new Point();
        lPoint1Rect = new Rect();
        lPoint2Rect = new Rect();
    }

    public void onTouchDown(Point point) {

        downPoint.set(point.x, point.y);
        if (lPoint1Rect.contains(point.x, point.y)) {
            downState = 1;
        } else if (lPoint2Rect.contains(point.x, point.y)) {
            downState = 2;
        } else if (panduan(point)) {
            downState = 3;
        } else {
            downState = 0;
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
        lPoint1Rect.set(lPoint1.x - 25, lPoint1.y - 25, lPoint1.x + 25,
                lPoint1.y + 25);
        lPoint2Rect.set(lPoint2.x - 25, lPoint2.y - 25, lPoint2.x + 25,
                lPoint2.y + 25);
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
            return  lDis1 + lDis2 >= lDis + 0.00f && lDis1 + lDis2 <= lDis + 5.00f ;
    }

    public void onDraw(Canvas canvas, float s, float s2, boolean isDraw, int left, int top, int  right, int buttom) {
        // TODO Auto-generated method stub
           /* lPoint1.x=lPoint1.x<=left?left:(lPoint1.x>=right?right:lPoint1.x);
            lPoint1.y=lPoint1.y<=top?top:(lPoint1.y>=buttom?buttom:lPoint1.y);
            lPoint2.x= lPoint2.x<=left?left:(lPoint2.x>=right?right:lPoint2.x);
            lPoint2.y= lPoint2.y<=top?top:(lPoint2.y>=buttom?buttom:lPoint2.y);*/
            canvas.drawLine(lPoint1.x, lPoint1.y, lPoint2.x, lPoint2.y, paint);
            double angle = Math.atan2(lPoint2.y - lPoint1.y, lPoint2.x - lPoint1.x);
            Log.d("vivi", "angle:" + angle);
            double x = lPoint2.x + 10 * Math.cos(angle + 60) / s2;
            double y = lPoint2.y + 10 * Math.sin(angle + 60) / s2;
            canvas.drawLine((float) x, (float) y, lPoint2.x, lPoint2.y, paint);
            double x1 = lPoint2.x + 10 * Math.cos(angle - 60) / s2;
            double y1 = lPoint2.y + 10 * Math.sin(angle - 60) / s2;
            canvas.drawLine((float) x1, (float) y1, lPoint2.x, lPoint2.y, paint);
            canvas.drawCircle(lPoint1.x, lPoint1.y, 1 / s2, newpaint);
    }
}
