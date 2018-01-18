package coms.pacs.pacs.Api

import coms.pacs.pacs.Model.Base
import coms.pacs.pacs.Model.patient
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by vange on 2018/1/17.
 */
interface Api {
    @FormUrlEncoded
    @POST("login/login")
    fun login(@Field("userid") userid:String, @Field("password")password:String): Observable<Base<String>>

    @FormUrlEncoded
    @POST("login/getPatientList")
    fun getPatientList(@Field("content")content:String,
                       @Field("pageSize")pageSize:Int, @Field("pageNum")pageNum:Int):Observable<Base<List<patient>>>
}