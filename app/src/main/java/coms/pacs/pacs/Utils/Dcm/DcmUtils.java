package coms.pacs.pacs.Utils.Dcm;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import org.dcm4che3.android.Raster;
import org.dcm4che3.android.RasterUtil;
import org.dcm4che3.android.imageio.dicom.DicomImageReader;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomInputStream;

import java.io.File;

import coms.pacs.pacs.Model.DicAttrs;
import coms.pacs.pacs.Room.DownStatu;
import coms.pacs.pacs.Room.DownloadDao;
import coms.pacs.pacs.Rx.MyObserver;
import coms.pacs.pacs.Rx.RxSchedulers;
import coms.pacs.pacs.Utils.DownLoadUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by 不听话的好孩子 on 2018/1/18.
 */

public class DcmUtils {
    public interface DcmCallBack {
        void call(DicAttrs attrs);

        void callFailure(String message);
    }

    public static void displayDcm(final String path,final MyObserver<DicAttrs>consumer) {
        final DownStatu downStatu = DownloadDao.Companion.getDownDao().get(path);

        if(downStatu!=null&&downStatu.getState()==1&&new File(downStatu.getPath()).exists()){
           Observable.just(downStatu.getPath())
                   .flatMap(new Function<String, ObservableSource<DicAttrs>>() {
                       @Override
                       public ObservableSource<DicAttrs> apply(String s) throws Exception {
                           return Observable.just(parseAttrs(new File(s)));
                       }
                   })
                   .observeOn(Schedulers.io())
                   .compose(RxSchedulers.<DicAttrs>compose())
                   .subscribe(consumer);
            return;
        }
        long download = DownLoadUtils.Companion.download(path);
        Observable.create(new DownLoadUtils.DownObserver(download,300))
               .compose(RxSchedulers.<DownStatu>compose())
                .filter(new Predicate<DownStatu>() {
                    @Override
                    public boolean test(DownStatu progress) throws Exception {
                        consumer.onProgress(progress);
                        return progress.getState() == 1;
                    }
                })
                .flatMap(new Function<DownStatu, ObservableSource<DicAttrs>>() {
                    @Override
                    public ObservableSource<DicAttrs> apply(DownStatu progress) throws Exception {

                        return Observable.just(parseAttrs(new File(progress.getPath())));
                    }
                })
                .compose(RxSchedulers.<DicAttrs>compose())
                .subscribe(consumer);
    }

    public static void displayDcm(final Activity activity, final String path, final DcmCallBack callBack) {
        try {
            Observable.just(1)
                    .observeOn(Schedulers.io())
                    .flatMap(new Function<Integer, Observable<File>>() {
                        @Override
                        public Observable<File> apply(Integer integer) throws Exception {
                            File file = Glide.with(activity).load(path)
                                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                            return Observable.just(file);
                        }
                    })
                    .compose(RxSchedulers.compose())
                    .cast(File.class)
                    .subscribe(new MyObserver<File>(activity) {
                        @Override
                        public void onNext(File file) {
                            super.onNext(file);
                            try {
                                callBack.call(parseAttrs(file));
                            } catch (Exception e) {
                                e.printStackTrace();
                                callBack.callFailure(e.getMessage());
                            }
                        }
                    });
        } catch (Exception e) {
            callBack.callFailure(e.getMessage());
        }
    }

    public static DicAttrs parseAttrs(File file) throws Exception {
        DicomImageReader dr = new DicomImageReader();
            //dcm文件输入流
            DicomInputStream dcmInputStream = new DicomInputStream(file);
            //属性对象
            Attributes attrs = dcmInputStream.readDataset(-1, -1);
            //输出所有属性信息
            Log.e("TAG", "输出所有属性信息1:" + attrs);

            //获取行
            int rows = attrs.getInt(Tag.Rows, 1);
            //获取列
            int columns = attrs.getInt(Tag.Columns, 1);
            //窗宽窗位
            float win_center = attrs.getFloat(Tag.WindowCenter, 1);
            float win_width = attrs.getFloat(Tag.WindowWidth, 1);
            Log.e("TAG", "" + "row=" + rows + ",columns=" + rows + "row*columns = " + rows * columns);

            Log.e("TAG", "" + "win_center=" + win_center + ",win_width=" + win_width);
            //获取像素数据 ，这个像素数据不知道怎么用！！！，得到的是图片像素的两倍的长度
            //后面那个 raster.getByteData()是图片的像素数据
            byte[] b = attrs.getSafeBytes(Tag.PixelData);
            if (b != null) {
                Log.e("TAG", "" + "b.length=" + b.length);
            } else {
                Log.e("TAG", "" + "b==null");
            }

            //修改默认字符集为GB18030
            attrs.setString(Tag.SpecificCharacterSet, VR.CS, "GB18030");//解决中文乱码问题

            String patientName = attrs.getString(Tag.PatientName, "");

            //生日
            String patientBirthDate = attrs.getString(Tag.PatientBirthDate, "");

            //机构
            String institution = attrs.getString(Tag.InstitutionName, "");

            //站点
            String station = attrs.getString(Tag.StationName, "");

            //制造商
            String Manufacturer = attrs.getString(Tag.Manufacturer, "");

            //制造商模型
            String ManufacturerModelName = attrs.getString(Tag.ManufacturerModelName, "");


            //描述--心房
            String description = attrs.getString(Tag.StudyDescription, "");
            //描述--具体
            String SeriesDescription = attrs.getString(Tag.SeriesDescription, "");

            //描述时间
            String studyData = attrs.getString(Tag.StudyDate, "");
            //描述时间
            Double pixelSpacing = attrs.getDouble(Tag.PixelSpacing, 0);


            dr.open(file);
            //            Attributes ds = dr.getAttributes();
            //            String wc = ds.getString(Tag.WindowCenter);
            //            String ww = ds.getString(Tag.WindowWidth);
            //            Log.e("TAG", "" + "wc=" + wc + ",ww=" + ww);
            Raster raster = dr.applyWindowCenter(0, (int) win_width, (int) win_center);
            //             Log.e("TAG", "" + "raster.getWidth()=" + raster.getWidth() + ",raster.getHeight()=" + raster.getHeight());
            //            Log.e("TAG", "" + "raster.getByteData().length=" + raster.getByteData().length);

            //             Bitmap bmp = RasterUtil.gray8ToBitmap(raster.getWidth(), raster.getHeight(), raster.getByteData());
            //             Log.e("TAG", "b==raster.getByteData()" + (b == raster.getByteData()));
            Bitmap bmp = RasterUtil.rasterToBitmap(raster);
            DicAttrs attr = new DicAttrs(rows, columns, win_center, win_width, patientName, patientBirthDate
                    , institution, station, Manufacturer, ManufacturerModelName, description, SeriesDescription, studyData, pixelSpacing, bmp);
            return attr;
    }

    /**
     * 图片调色处理
     *
     * @author maylian7700@126.com
     */
    public static class ColorAdjust {

        private final Canvas canvas;
        private final Paint paint;
        private final Bitmap bmp;
        Bitmap bitmap;

        public ColorAdjust(Bitmap bitmap) {
            this.bitmap = bitmap;
            bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                    Bitmap.Config.ARGB_8888);
            // 得到画笔对象
            canvas = new Canvas(bmp);
            // 新建paint
            paint = new Paint();
            paint.setAntiAlias(true);
        }

        /**
         * 饱和度标识
         */
        public static final int FLAG_SATURATION = 0x0;

        /**
         * 亮度标识
         */
        public static final int FLAG_LUM = 0x1;

        /**
         * 色相标识
         */
        public static final int FLAG_HUE = 0x2;


        private ColorMatrix mLightnessMatrix;
        private ColorMatrix mSaturationMatrix;
        private ColorMatrix mHueMatrix;
        private ColorMatrix mAllMatrix;

        /**
         * 亮度
         */
        private float mLumValue = 1F;

        /**
         * 饱和度
         */
        private float mSaturationValue = 0F;

        /**
         * 色相
         */
        private float mHueValue = 0F;

        /**
         * SeekBar的中间值
         */
        private static final int MIDDLE_VALUE = 127;

        /**
         * SeekBar的最大值
         */
        private static final int MAX_VALUE = 255;


        /**
         * 设置饱和度值
         *
         * @param saturation
         */
        public void setSaturation(int saturation) {
            mSaturationValue = saturation * 1.0F / MIDDLE_VALUE;
        }

        /**
         * 设置色相值
         *
         * @param hue
         */
        public void setHue(int hue) {
            mHueValue = hue * 1.0F / MIDDLE_VALUE;
        }

        /**
         * 设置亮度值
         *
         * @param lum
         */
        public void setLum(int lum) {
            mLumValue = (lum - MIDDLE_VALUE) * 1.0F / MIDDLE_VALUE * 180;
        }


        /**
         * @param flag 比特位0 表示是否改变色相，比位1表示是否改变饱和度,比特位2表示是否改变明亮度
         */
        public Bitmap handleImage(int flag) {
            bmp.eraseColor(Color.TRANSPARENT);
            // 创建一个相同尺寸的可变的位图区,用于绘制调色后的图片
            // 设置抗锯齿,也即是边缘做平滑处理
            if (null == mAllMatrix) {
                mAllMatrix = new ColorMatrix();
            }

            if (null == mLightnessMatrix) {
                mLightnessMatrix = new ColorMatrix(); // 用于颜色变换的矩阵，android位图颜色变化处理主要是靠该对象完成
            }

            if (null == mSaturationMatrix) {
                mSaturationMatrix = new ColorMatrix();
            }

            if (null == mHueMatrix) {
                mHueMatrix = new ColorMatrix();
            }

            switch (flag) {
                case FLAG_HUE: // 需要改变色相
                    mHueMatrix.reset();
                    mHueMatrix.setScale(mHueValue, mHueValue, mHueValue, 1); // 红、绿、蓝三分量按相同的比例,最后一个参数1表示透明度不做变化，此函数详细说明参考
                    // // android
                    // doc
                    break;
                case FLAG_SATURATION: // 需要改变饱和度
                    // saturation 饱和度值，最小可设为0，此时对应的是灰度图(也就是俗话的“黑白图”)，
                    // 为1表示饱和度不变，设置大于1，就显示过饱和
                    mSaturationMatrix.reset();
                    mSaturationMatrix.setSaturation(mSaturationValue);
                    break;
                case FLAG_LUM: // 亮度
                    // hueColor就是色轮旋转的角度,正值表示顺时针旋转，负值表示逆时针旋转
                    mLightnessMatrix.reset(); // 设为默认值
                    mLightnessMatrix.setRotate(0, mLumValue); // 控制让红色区在色轮上旋转的角度
                    mLightnessMatrix.setRotate(1, mLumValue); // 控制让绿红色区在色轮上旋转的角度
                    mLightnessMatrix.setRotate(2, mLumValue); // 控制让蓝色区在色轮上旋转的角度
                    // 这里相当于改变的是全图的色相
                    break;
            }
            mAllMatrix.reset();
            mAllMatrix.postConcat(mHueMatrix);
            mAllMatrix.postConcat(mLightnessMatrix); // 效果叠加
            mAllMatrix.postConcat(mSaturationMatrix); // 效果叠加

            paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));// 设置颜色变换效果
            canvas.drawBitmap(bitmap, 0, 0, paint); // 将颜色变化后的图片输出到新创建的位图区
            // 返回新的位图，也即调色处理后的图片
            return bmp;
        }

        /**
         * 图片锐化（拉普拉斯变换）
         *
         * @param
         * @return
         */
        public Bitmap sharpenImageAmeliorate(Bitmap sharp) {
            long start = System.currentTimeMillis();
            // 拉普拉斯矩阵
            int[] laplacian = new int[]{-1, -1, -1, -1, 9, -1, -1, -1, -1};

            int width = bmp.getWidth();
            int height = bmp.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            int pixR = 0;
            int pixG = 0;
            int pixB = 0;

            int pixColor = 0;

            int newR = 0;
            int newG = 0;
            int newB = 0;

            int idx = 0;
            float alpha = 0.3F;
            int[] pixels = new int[width * height];
            Bitmap bb = null;
            if (sharp == null)
                bb = bmp;
            else
                bb = sharp;
            bb.getPixels(pixels, 0, width, 0, 0, width, height);
            for (int i = 1, length = height - 1; i < length; i++) {
                for (int k = 1, len = width - 1; k < len; k++) {
                    idx = 0;
                    for (int m = -1; m <= 1; m++) {
                        for (int n = -1; n <= 1; n++) {
                            pixColor = pixels[(i + n) * width + k + m];
                            pixR = Color.red(pixColor);
                            pixG = Color.green(pixColor);
                            pixB = Color.blue(pixColor);

                            newR = newR + (int) (pixR * laplacian[idx] * alpha);
                            newG = newG + (int) (pixG * laplacian[idx] * alpha);
                            newB = newB + (int) (pixB * laplacian[idx] * alpha);
                            idx++;
                        }
                    }

                    newR = Math.min(255, Math.max(0, newR));
                    newG = Math.min(255, Math.max(0, newG));
                    newB = Math.min(255, Math.max(0, newB));

                    pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                    newR = 0;
                    newG = 0;
                    newB = 0;
                }
            }

            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            long end = System.currentTimeMillis();
            Log.d("may", "used time=" + (end - start));
            return bitmap;
        }

    }
}
