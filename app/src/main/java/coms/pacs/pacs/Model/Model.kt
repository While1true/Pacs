package coms.pacs.pacs.Model

import android.graphics.Bitmap
import java.io.Serializable

/**
 * Created by vange on 2018/1/16.
 */

data class Base<T> constructor(var message: String, var data: T, var code: Int) : Serializable


data class patient(
        val age: String, //14
        val healthcard: String, //A34816043
        val healthcards: String, //A34816043 就诊号卡
        val name: String, //虞曾凯
        val patientcode: String, //2c2880e45f5c7181015f61ae785b3e8e
        val sex: Int //1
)

data class DicAttrs constructor(
        var rows: Int,
        var columns: Int,
        var win_center: Float,
        var win_width: Float,
        var patientName: String,
        var patientBirthDate: String,
        var institution: String,
        var station: String,
        var Manufacturer: String,
        var ManufacturerModelName: String,
        var description: String,
        var SeriesDescription: String,
        var studyData: String,
        var pixelSpacing: Double,
        var bitmap: Bitmap

)

data class Progress constructor(
        var id:Long,
        var current:Long,
        var total:Long,
        var file:String,
        var state:Int
)