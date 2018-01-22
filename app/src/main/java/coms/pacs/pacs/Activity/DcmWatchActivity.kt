package coms.pacs.pacs.Activity

import android.graphics.Bitmap
import android.view.View
import android.widget.SeekBar
import coms.pacs.pacs.BaseComponent.BaseActivity
import coms.pacs.pacs.Dialog.DcmWatchDialog
import coms.pacs.pacs.Interfaces.seekbarListener
import coms.pacs.pacs.Model.DicAttrs
import coms.pacs.pacs.R
import coms.pacs.pacs.Utils.Dcm.Angle
import coms.pacs.pacs.Utils.Dcm.DcmUtils
import coms.pacs.pacs.Utils.Dcm.Line
import coms.pacs.pacs.Utils.K2JUtils
import coms.pacs.pacs.Utils.toast
import kotlinx.android.synthetic.main.dicwatch_activity.*

/**
 * Created by 不听话的好孩子 on 2018/1/18.
 */
class DcmWatchActivity : BaseActivity() {
    var originalBitmap: Bitmap? = null
    lateinit var dialog: DcmWatchDialog
    var attrs: DicAttrs?=null
    lateinit var colrAdjust: DcmUtils.ColorAdjust
    override fun initView() {
        setTitle("影像查看")

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
                    K2JUtils.put("showwm",true)
                        wmtext.text = "ww:" + attrs?.win_width + " wc:" + attrs?.win_center
                } else {
                    K2JUtils.put("showwm",false)
                    wmtext.text=""
                }

            }
        })

        photoView.maxScale = 4f
        DcmUtils.desplayDcm(this, "http://10.0.110.127:8080/pacsAndroid/path/1.2.840.113704.1.111.3648.1497930071.54.dcm", object : DcmUtils.DcmCallBack {
            override fun call(bitmap: Bitmap?, attrs: DicAttrs?) {

                originalBitmap = bitmap
                this@DcmWatchActivity.attrs=attrs

                dialog.attrs=attrs

                //show pic
                photoView.pxSpace = attrs?.pixelSpacing?.toFloat() ?: 0f
                progressBar.visibility = View.GONE
                colrAdjust = DcmUtils.ColorAdjust(bitmap)
                photoView.setImageBitmap(bitmap)

                //show wm
                var bool = K2JUtils.get<Boolean>("showwm", true)
                if (bool)
                    wmtext.text = "ww:" + attrs?.win_width + " wc:" + attrs?.win_center
            }

            override fun callFailure(message: String?) {
                message.toast()
                progressBar.visibility = View.GONE
            }
        })
        setMenuClickListener(0, View.OnClickListener {
            dialog.show(supportFragmentManager)
        })

        //click

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

        //绘图
        menu_length.setOnClickListener {
            photoView.drawWhat(Line())
        }
        menu_angle.setOnClickListener {
            photoView.drawWhat(Angle())
        }
        menu_scale.setOnClickListener { }
        menu_sqr.setOnClickListener { }
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