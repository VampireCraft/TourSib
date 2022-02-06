package dt.prod.patternvm.createProblem.domain

import retrofit2.http.*
import dt.prod.patternvm.core.model.ResponseWrapper
import dt.prod.patternvm.createProblem.models.CreateProblemRequestPhoto
import dt.prod.patternvm.createProblem.models.PhotoResponse

interface CreateEventApi {

    @POST("/rzd/Api.php")
    suspend fun createEvent(
        @Query("apicall") apicall: String,
        @Query("room_num") tag:String,
        @Query("definition") description: String,
        @Query("adress_num") typeUsers:String,
        @Query("image_problem") image:String,
        @Query("title") name:String
    ): ResponseWrapper<String>

    @POST("/rzd/Test.php?")
    suspend fun sendPhoto(
        @Body body: CreateProblemRequestPhoto
    ): ResponseWrapper<PhotoResponse>
}

//@POST("/rzd/Test.php?")
//suspend fun sendPhoto(
//    @Body body: CreateProblemRequestPhoto
//): ResponseWrapper<String>


//@POST("/rzd/Test.php")
//suspend fun sendPhoto(
//    @Query("name_img") name_img: String,
//    @Query("image") image: String
//): ResponseWrapper<String>