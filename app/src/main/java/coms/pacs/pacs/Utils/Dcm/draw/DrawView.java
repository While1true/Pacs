package coms.pacs.pacs.Utils.Dcm.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

/**
 * Created by vange on 2015/7/23.
 */
public class DrawView extends View {
    int oldX, oldY, newX, newY;
    private Bitmap surfaceBitmap;// 底层与表层bitmap
    private Canvas surfaceCanvas;// bitmap对应的canvas
    Paint p = new Paint();

    public DrawView(Context context, int oldX, int oldY, int newX, int newY) {
        super(context);
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
        Log.d("vivi", newX + "=" + oldX + "=" + newY + "=" + oldY);
        surfaceBitmap = Bitmap.createBitmap(400, 600, Bitmap.Config.ARGB_8888);
        surfaceCanvas = new Canvas(surfaceBitmap);
        surfaceCanvas.drawColor(Color.TRANSPARENT);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas = new Canvas();
        p.setColor(Color.GREEN);
        canvas.drawLine(this.oldX, this.oldY, this.newX, this.newY, p);
        super.onDraw(canvas);
    }
}
