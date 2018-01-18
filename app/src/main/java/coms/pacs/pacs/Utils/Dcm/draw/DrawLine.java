package coms.pacs.pacs.Utils.Dcm.draw;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;



public class DrawLine extends DrawBS {
    private Point cenPoint;
    private Point lPoint1, lPoint2;
    private Rect lPoint1Rect, lPoint2Rect;
//    List list = new ArrayList();
    DrawBean bean;
    int i = 0;

    public DrawLine() {
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

    public double getlDis() {
        double reIDis;
        reIDis = Math.sqrt((lPoint1.x - lPoint2.x)
                * (lPoint1.x - lPoint2.x) + (lPoint1.y - lPoint2.y)
                * (lPoint1.y - lPoint2.y));
        return reIDis;
    }

    public void onTouchUp(Point point) {
        lPoint1Rect.set(lPoint1.x - 25, lPoint1.y - 25, lPoint1.x + 25,
                lPoint1.y + 25);
        lPoint2Rect.set(lPoint2.x - 25, lPoint2.y - 25, lPoint2.x + 25,
                lPoint2.y + 25);
        if (downState == 0) {
            bean = new DrawBean(lPoint1.x, lPoint1.y, lPoint2.x, lPoint2.y);
         //   list.add(bean);
            i++;
        }
     //   Log.d("onTouchDown画下", i + "list = " + list.size());
    }


    public boolean panduan(Point point) {
        double lDis1 = Math.sqrt((point.x - lPoint1.x) * (point.x - lPoint1.x)
                + (point.y - lPoint1.y) * (point.y - lPoint1.y));
        double lDis2 = Math.sqrt((point.x - lPoint2.x) * (point.x - lPoint2.x)
                + (point.y - lPoint2.y) * (point.y - lPoint2.y));
            return lDis1 + lDis2 >= getlDis() + 0.00f && lDis1 + lDis2 <= getlDis() + 5.00f;
    }

    public void onDraw(Canvas canvas, float s, float s2, boolean isDraw, int left, int top, int  right, int buttom) {
        // TODO Auto-generated method stub
        if(isDraw) {
            lPoint1.x=lPoint1.x<=left?left:(lPoint1.x>=right?right:lPoint1.x);
            lPoint1.y=lPoint1.y<=top?top:(lPoint1.y>=buttom?buttom:lPoint1.y);
           lPoint2.x= lPoint2.x<=left?left:(lPoint2.x>=right?right:lPoint2.x);
           lPoint2.y= lPoint2.y<=top?top:(lPoint2.y>=buttom?buttom:lPoint2.y);
           /* canvas.clipRect(left,top,right,buttom);
            canvas.save();*/
            canvas.drawLine(lPoint1.x, lPoint1.y, lPoint2.x, lPoint2.y, paint);
            canvas.drawCircle(lPoint1.x, lPoint1.y, 1 / s2, newpaint);
            canvas.drawCircle(lPoint2.x, lPoint2.y, 1 / s2, newpaint);
       /* paint2.setColor(Color.BLUE);
        paint2.setTextSize(10 / s2);
        canvas.drawText(LineLong2(s), (lPoint1.x + lPoint2.x) / 2, (lPoint1.y + lPoint2.y) / 2, paint2);*/
            LineLong(canvas, s, s2);
        }
    }

    /*  private String LineLong2(float s){
          lDis = getlDis();
          String testString = (Math.round(lDis * s * 100) / 100.0) + "mm";
          return testString;

      }*/
    private void LineLong(Canvas canvas, float s, float s2) {
        Rect targetRect = new Rect(lPoint1.x, lPoint1.y, lPoint2.x, lPoint2.y);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(2);
        paint.setTextSize(10 / s2);
        String testString;
        if(s==0){ testString = (Math.round(getlDis()  * 100) / 100.0) + "px";}
       else testString = (Math.round(getlDis() * s * 100) / 100.0) + "mm";
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
       // void Text2(List list);
    }

    textListener listener;

    public void onTextListener(textListener listener) {
        this.listener = listener;
      //  listener.Text2(list);
    //    Log.d("直线保存", i + "   /   list2 = " + list.size());
    }
}
