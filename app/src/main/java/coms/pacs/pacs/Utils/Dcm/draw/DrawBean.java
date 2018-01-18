package coms.pacs.pacs.Utils.Dcm.draw;


/**
 * Created by vange on 2015/8/13.
 */
public class DrawBean {
    private int px1;
    private int py1;
    private int px2;
    private int py2;

    public DrawBean(int px1, int py1, int px2, int py2) {
        this.py1 = py1;
        this.px1 = px1;
        this.px2 = px2;
        this.py2 = py2;
    }

    public int getPy1() {
        return py1;
    }

    public void setPy1(int py1) {
        this.py1 = py1;
    }

    public int getPx1() {
        return px1;
    }

    public void setPx1(int px1) {
        this.px1 = px1;
    }

    public int getPy2() {
        return py2;
    }

    public void setPy2(int py2) {
        this.py2 = py2;
    }

    public int getPx2() {
        return px2;
    }

    public void setPx2(int px2) {
        this.px2 = px2;
    }

}
