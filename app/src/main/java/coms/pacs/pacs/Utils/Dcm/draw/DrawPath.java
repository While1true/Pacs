package coms.pacs.pacs.Utils.Dcm.draw;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Point;

public class DrawPath extends DrawBS {
    private Path path = new Path();
    private float mX, mY;

    public DrawPath() {
        // TODO Auto-generated constructor stub
    }

    public void onTouchDown(Point point) {
        path.moveTo(point.x, point.y);
        mX = point.x;
        mY = point.y;
    }

    public void onTouchMove(Point point) {
        float dx = Math.abs(point.x - mX);
        float dy = Math.abs(point.y - mY);
        if (dx > 3 || dy > 3) {
            path.quadTo(mX, mY, (point.x + mX) / 2, (point.y + mY) / 2);
            mX = point.x;
            mY = point.y;
        }
    }

    public void onDraw(Canvas canvas, float s, float s2, boolean isDraw, int left, int top, int  right, int buttom) {
        // TODO Auto-generated method stub
        /*canvas.clipRect(left,top,right,buttom);
        canvas.save();*/
        canvas.drawPath(path, paint);
    }

}
