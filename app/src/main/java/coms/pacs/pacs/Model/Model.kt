package coms.pacs.pacs.Model

import android.graphics.Bitmap
import coms.pacs.pacs.Interfaces.IListDateModel
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

data class ReportTitle constructor(
        var checkupcode:String,
        var name:String,
        var sex:String,
        var registertime:String,
        var checktype:String,
        var checkpart:String
)


data class ReportItem(
		val applydept: String, //外科
		val birthday: String, //25岁
		val checkdate: String, //2014-01-08 15:27:23
		val checkpart: String, //头颅平扫
		val checktype: String, //CT
		val checkupcode: String, //1401081526426875
		val diagnosed: String, //    1.颅内未见明显血肿征象，建议随访复查；2.右侧额部皮下血肿。
		val enname: String, //huang li lin
		val features: String, //    脑实质未见明显异常密度影，脑室、脑池无明显扩大，脑沟无明显增宽，中线结构无明显移位，所见颅骨未见明显骨折征。右侧额部见皮下软组织肿胀。
		val masculine: String, //1
		val name: String, //黄丽琳
		val patientcode: String, //1401081526426875
		val reportdoctor: String, //彭清
		val reporttime: Long, //1389167076000
		val reviewdoctor: String, //叶灿烽
		val reviewstatus: String, //2
		val sex: String //女
)

data class CheckImg constructor(
		var thumbnail:String,
		var original:String,
		var checkpart:String,
		var checktype:String,
		var checkdate:String

)

data class Doctor constructor(var expertcode:String,var expertname:String)


data class HelpBean(
		val applyforhelptime: String, //2016-09-08 15:44:09
		val applyusercode: String, //350102199211115811
		val applyusername: String, //刘诗诗
		val invitedusername: String, //刘诗诗
		val checkadvice: String, // 太卡了
		val applycode: String, // 太卡了
		val remark: String, // 太卡了
		val invitedusercode: String, //admin
		val status: String, //已协助,
		val checkupcode: String, //已协助,
        var type:Int=0
):Serializable,IListDateModel {
	override fun getTitle(): String {
		return (if(type==0) """$applyusername/$remark""" else """$invitedusername/$checkadvice""" )
	}

	override fun getSubTitle(): String {
		return """$status/$applyforhelptime"""
	}
}
