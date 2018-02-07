package coms.pacs.pacs.Api

import coms.pacs.pacs.Model.*
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by vange on 2018/1/17.
 */
interface Api {
    @FormUrlEncoded
    @POST("login/login")
    fun login(@Field("userid") userid: String, @Field("password") password: String): Observable<Base<String>>

    @FormUrlEncoded
    @POST("login/getPatientList")
    fun getPatientList(@Field("content") content: String,
                       @Field("pageSize") pageSize: Int, @Field("pageNum") pageNum: Int): Observable<Base<List<patient>>>

    @FormUrlEncoded
    @POST(value = "function/getPatientReport")
    fun getPatientReport(@Field("checkupcode") checkupcode: String): Observable<Base<ReportItem>>

    @FormUrlEncoded
    @POST(value = "function/getPatientAllReports")
    fun getPatientAllReports(@Field("patientcode") patientcode: String): Observable<Base<List<ReportTitle>>>

    @FormUrlEncoded
    @POST(value = "function/getPatientAllImages")
    fun getPatientAllImages(@Field("checkupcode") checkupcode: String): Observable<Base<List<CheckImg>>>

    @GET(value = "function/getDoctorList")
    fun getDoctorList(): Observable<Base<List<Doctor>>>

    @FormUrlEncoded
    @POST(value = "function/getHelpApplication")
    fun getHelpApplication(@Field("username") username: String, @Field("invitedusername") invitedusername: String, @Field("checkupcode") checkupcode: String, @Field("remark") remark: String): Observable<Base<Any>>

    @FormUrlEncoded
    @POST(value = "function/getHelpViewList")
    fun getHelpViewList(@Field("username") username: String, @Field("type") type: Int): Observable<Base<List<HelpBean>>>

    @FormUrlEncoded
    @POST(value = "function/getHelpReply")
    fun getHelpReply(@Field("applycode") applycode: String, @Field("content") content: String): Observable<Base<Any>>

    @FormUrlEncoded
    @POST(value = "function/bindXiaomi")
    fun bindXiaomi(@Field("userId") userid: String, @Field("miId") miId: String): Observable<Base<Any>>

    @FormUrlEncoded
    @POST(value = "function/putWriteReport")
    fun putWriteReport(@Field("reportdoctor")reportdoctor:String,@Field("patientcode") patientcode: String, @Field("imageSee") imageSee: String, @Field("diagnosisSee") diagnosisSee: String): Observable<Base<Any>>

    @POST(value = "function/putPatientRegisterInfo")
    fun putPatientRegisterInfo(@Body registerinfo: RegisterInfo): Observable<Base<Any>>

    @FormUrlEncoded
    @POST(value = "function/getPatientRegisterInfo")
    fun getPatientRegisterInfo(@Field("patientcode") patientcode: String): Observable<Base<RegisterInfo>>

}