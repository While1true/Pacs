package coms.pacs.pacs.Activity

import android.graphics.Bitmap
import android.view.View
import android.widget.SeekBar
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.Dialog.DcmWatchDialog
import coms.pacs.pacs.Interfaces.seekbarListener
import coms.pacs.pacs.Model.DicAttrs
import coms.pacs.pacs.R
import coms.pacs.pacs.Room.DownStatu
import coms.pacs.pacs.Rx.MyObserver
import coms.pacs.pacs.Utils.Dcm.*
import coms.pacs.pacs.Utils.K2JUtils
import kotlinx.android.synthetic.main.dicwatch_activity.*

/**
 * Created by 不听话的好孩子 on 2018/1/18.
 */
class DcmWatchActivity : BaseActivity() {
    var originalBitmap: Bitmap? = null
    lateinit var dialog: DcmWatchDialog
    var attrs: DicAttrs? = null
    lateinit var colrAdjust: DcmUtils.ColorAdjust
    override fun initView() {
        setTitle("影像查看")

        downPic()

        initMenu()

        initColorMenu()

        initDrawMenu()

    }

    private fun initMenu() {
        setMenuClickListener(0, View.OnClickListener {
            dialog.show(supportFragmentManager)
        })
        //menu
        dialog = DcmWatchDialog()
        dialog.setonClickListenr(View.OnClickListener {
            when (it.id) {
                R.id.color_adjust -> {
                    dialog.dismiss()
                    photoView.drawPath = false
                    colorlayout.visibility = View.VISIBLE
                    measure_layout.visibility = View.GONE
                }
                R.id.draw_mode -> {
                    dialog.dismiss()
                    colorlayout.visibility = View.GONE
                    measure_layout.visibility = View.VISIBLE
                }
                else -> if (it.tag as Boolean) {
                    K2JUtils.put("showwm", true)
                    wmtext.text = "ww:" + attrs?.win_width + " wc:" + attrs?.win_center
                } else {
                    K2JUtils.put("showwm", false)
                    wmtext.text = ""
                }

            }
        })
    }

    private fun downPic() {
        photoView.maxScale = 4f
        var path = "http://10.0.110.127:8080/pacsAndroid/path/1.2.840.113704.1.111.3648.1497930071.54.dcm"
        DcmUtils.displayDcm(
                path,
                object: MyObserver<DicAttrs>(this){
                    override fun onNext(attrs: DicAttrs) {
                        super.onNext(attrs)
                        originalBitmap = attrs.bitmap
                        this@DcmWatchActivity.attrs = attrs

                        dialog.attrs = attrs

                        //show pic
                        photoView.pxSpace = attrs?.pixelSpacing?.toFloat() ?: 0f
                        colrAdjust = DcmUtils.ColorAdjust(attrs.bitmap)
                        photoView.setImageBitmap(attrs.bitmap)

                        progresslayout.visibility = View.GONE

                        //show wm
                        var bool = K2JUtils.get<Boolean>("showwm", true)
                        if (bool)
                            wmtext.text = "ww:" + attrs?.win_width + " wc:" + attrs?.win_center
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        progresslayout.visibility = View.GONE
                    }

                    override fun onProgress(it: DownStatu) {
                        super.onProgress(it)
                        progresslayout.visibility = View.VISIBLE
                        progressBar.progress = it.current.toInt()
                        progressBar.max = it.total.toInt()
                        progresstv.text= """${String.format("%.2f",(it.current.toFloat()/1024/1024))}MB/${String.format("%.2f",(it.total.toFloat()/1024/1024))}MB """

                    }
                }
             )
    }

    private fun initColorMenu() {
        //色度调节
        colorbar.setOnSeekBarChangeListener(object : seekbarListener() {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                colortext.text = "色度/" + progress
                colrAdjust.setLum(progress)
                var bitmap = colrAdjust.handleImage(DcmUtils.ColorAdjust.FLAG_LUM)
                photoView.setImageBitmap(bitmap)
            }
        })
        fullbar.setOnSeekBarChangeListener(object : seekbarListener() {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                fulltext.text = "饱和/" + progress
                colrAdjust.setSaturation(progress)
                var bitmap = colrAdjust.handleImage(DcmUtils.ColorAdjust.FLAG_SATURATION)
                photoView.setImageBitmap(bitmap)
            }
        })
        lightbar.setOnSeekBarChangeListener(object : seekbarListener() {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                lighttext.text = "亮度/" + progress
                colrAdjust.setHue(progress)
                var bitmap = colrAdjust.handleImage(DcmUtils.ColorAdjust.FLAG_HUE)
                photoView.setImageBitmap(bitmap)

            }
        })

        sharp.setOnClickListener {
            val sharpenImage = colrAdjust.sharpenImageAmeliorate(photoView.screenBitmap)
            photoView.setImageBitmap(sharpenImage)
        }

        restimg.setOnClickListener {
            lightbar.progress = 127
            fullbar.progress = 127
            colorbar.progress = 127
            photoView.setImageBitmap(originalBitmap)
        }
    }

    private fun initDrawMenu() {
        //绘图
        menu_length.setOnClickListener {
            photoView.drawWhat(Line())
        }
        menu_angle.setOnClickListener {
            photoView.drawWhat(Angle())
        }
        menu_scale.setOnClickListener {
            photoView.isDrawingCacheEnabled = false//先清空缓存
            photoView.isDrawingCacheEnabled = true
            photoView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
            var map = photoView.drawingCache
            photoView.drawWhat(Scale(map))
        }
        menu_oval.setOnClickListener {
            photoView.drawWhat(Oval())
        }
        menu_path.setOnClickListener {
            photoView.drawWhat(Path())
        }
        menu_sqr.setOnClickListener {
            photoView.drawWhat(Rect())
        }
        menu_reset.setOnClickListener {
            photoView.drawPath = false
        }
    }

    override fun loadData() {
    }

    override fun getLayoutId(): Int {
        return R.layout.dicwatch_activity
    }
}