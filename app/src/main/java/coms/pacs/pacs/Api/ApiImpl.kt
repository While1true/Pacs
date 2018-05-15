package coms.pacs.pacs.Api

import coms.pacs.pacs.Rx.Net.RetrofitHttpManger
import coms.pacs.pacs.Rx.RxSchedulers
import coms.pacs.pacs.Model.Base
import coms.pacs.pacs.Model.Choice
import coms.pacs.pacs.Model.RegisterInfo
import coms.pacs.pacs.Model.patient
import coms.pacs.pacs.Rx.Net.RequestParams
import io.reactivex.Observable

/**
 * Created by vange on 2018/1/17.
 */
class ApiImpl : Api {

    override fun addLocationInfo(map: RequestParams)=api.addLocationInfo(map).compose(RxSchedulers.compose())

    override fun addEquipmentInfo(map: RequestParams)=api.addEquipmentInfo(map).compose(RxSchedulers.compose())

    override fun getSource()=api.getSource().compose(RxSchedulers.compose())
    override fun getApplydept()=api.getApplydept().compose(RxSchedulers.compose())
    override fun getChecktype()=api.getChecktype().compose(RxSchedulers.compose())
    override fun getCheckdevice()=api.getCheckdevice().compose(RxSchedulers.compose())
    override fun getCheckpart()=api.getCheckpart().compose(RxSchedulers.compose())

    override fun getPatientRegisterInfo(patientcode: String)=api.getPatientRegisterInfo(patientcode).compose(RxSchedulers.compose())

    override fun putPatientRegisterInfo(registerinfo: RegisterInfo)=api.putPatientRegisterInfo(registerinfo).compose(RxSchedulers.compose())

    override fun putWriteReport(reportdoctor:String,patientcode: String, imageSee: String, diagnosisSee: String)=api.putWriteReport(reportdoctor,patientcode,imageSee,diagnosisSee).compose(RxSchedulers.compose())

    override fun bindXiaomi(userid: String, miId: String)=api.bindXiaomi(userid,miId).compose(RxSchedulers.compose())

    override fun getHelpReply(applycode: String, content: String)=api.getHelpReply(applycode,content).compose(RxSchedulers.compose())

    override fun getHelpViewList(username: String, type: Int)=api.getHelpViewList(username,type).compose(RxSchedulers.compose())

    override fun getDoctorList()=api.getDoctorList().compose(RxSchedulers.compose())

    override fun getHelpApplication(username: String, invitedusername: String, checkupcode: String, remark: String)=api.getHelpApplication(username,invitedusername,checkupcode,remark).compose(RxSchedulers.compose())

    override fun getPatientAllImages(checkupcode: String)=api.getPatientAllImages(checkupcode).compose(RxSchedulers.compose())

    override fun getPatientAllReports(patientcode: String)=api.getPatientAllReports(patientcode).compose(RxSchedulers.compose())

    override fun getPatientReport(patientcode: String)=api.getPatientReport(patientcode).compose(RxSchedulers.compose())

    override fun login(userid: String, password: String): Observable<Base<String>>
            = api.login(userid, password).compose(RxSchedulers.compose())

    override fun getPatientList(content: String, pageSize: Int, pageNum: Int): Observable<Base<List<patient>>>
            = api.getPatientList( content, pageSize, pageNum).compose(RxSchedulers.compose())

    private val api: Api by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { RetrofitHttpManger.create(Api::class.java) }

    companion object Instance {
        val apiImpl: ApiImpl by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ApiImpl()
        }
    }
}