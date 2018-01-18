package coms.pacs.pacs.Utils.Dcm.draw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


@SuppressLint("WrongCall")
public class PathView extends View {
    private DrawBS drawBS = null;
    private Point evevtPoint;
    Bitmap surfaceBitmap, bitmap;
    private Canvas floorCanvas, surfaceCanvas;
    float  s, s2;
    boolean isDraw=true,isClean=true;
    int left, top, right, buttom;
    public PathView(Context context) {
        super(context);
    }

    @SuppressLint("ParserError")
    public PathView(Context context, Bitmap bitmap, Bitmap surfaceBitmap, float s, float s2) {
        super(context);
        this.bitmap = bitmap;
        this.s = s;
        this.s2 = s2;
        this.surfaceBitmap=surfaceBitmap;
        drawBS = new DrawPath();
        evevtPoint = new Point();
        //floorBitmap = Bitmap.createBitmap(this.bitmap.getWidth(), this.bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        floorCanvas = new Canvas(this.bitmap);
     //   surfaceBitmap = Bitmap.createBitmap(this.bitmap.getWidth(), this.bitmap.getHeight(), Bitmap.Config.ARGB_4444);
        this.surfaceBitmap.setHasAlpha(true);
        surfaceCanvas = new Canvas(this.surfaceBitmap);
      //  surfaceCanvas.drawColor(Color.TRANSPARENT);
    }
    public void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
            canvas.drawBitmap(this.bitmap, 0, 0, null);
        drawBS.onDraw(surfaceCanvas, s, s2,isDraw, left, top, right, buttom);
        if(isClean) {
            Paint paint = new Paint();
            paint.setAlpha(0);
            canvas.drawBitmap(surfaceBitmap, 0, 0, paint);
            Log.d("vivi", "isClean");
        }else {
            canvas.drawBitmap(surfaceBitmap, 0, 0, null);
            Log.d("vivi", "!isClean");
        }
    }

    float a, b;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        evevtPoint.set((int) event.getX(), (int) event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isClean=false;
                isDraw=false;
                drawBS.onTouchDown(evevtPoint);
                a = event.getX();
                b = event.getY();
                if((left<a&a<right)&&(top< b & b < buttom)) {
                    isDraw=true;
                }
             //   Log.d("vivi","isdraw:"+isDraw+"+"+left+"+"+right+"+"+top+"+"+buttom+"+"+a+"+"+b);
                break;
            case MotionEvent.ACTION_MOVE:
                drawBS.onTouchMove(evevtPoint);
                surfaceBitmap.eraseColor(Color.TRANSPARENT);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                drawBS.onTouchUp(evevtPoint);
                break;
            default:
                break;
        }
        return true;
    }

    public void setDrawTool(int i) {
        switch (i) {
            case 0:
                drawBS = new DrawLine();
                break;
            case 1:
                drawBS = new DrawCircle();
                break;
            case 2:
                drawBS = new DrewAngle();
                break;
            case 3:
                drawBS = new DrawPath();
                Log.d("vivi", "DrawPath");
                break;
            case 4:
                drawBS = new DrawArrow();
                break;
            case 5:
                drawBS = new DrawRactangle();
                break;
            case 6:
                drawBS = new DrawFourpoint();
                break;
            default:
                break;
        }
        if(!isClean)
        floorCanvas.drawBitmap(surfaceBitmap, 0, 0, null);
    }
public void getRect(int left,int top,int right,int buttom){
    this.left=left;
    this.top=top;
    this.right=right;
    this.buttom=buttom;
}
}
