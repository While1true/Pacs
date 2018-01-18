package coms.pacs.pacs.Utils.Dcm.draw;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;

public class DrawBS {

    public int downState;
    public int moveState;
    public Point downPoint = new Point();
    public Point movePoint = new Point();
    public Paint paint,newpaint;


    public DrawBS() {

        paint = new Paint();
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        newpaint  = new Paint();
        newpaint.setStrokeWidth(2);
        newpaint.setColor(Color.CYAN);
        newpaint.setAntiAlias(true);

    }


    public void onTouchDown(Point point) {

    }

    public void onTouchMove(Point point) {

    }

    public void onTouchUp(Point point) {

    }


    public void onDraw(Canvas canvas, float scale, float String) {
        // TODO Auto-generated method stub
    }

}
