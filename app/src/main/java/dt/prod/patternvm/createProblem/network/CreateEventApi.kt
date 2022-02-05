package dt.prod.patternvm.createProblem.network

import dt.prod.patternvm.core.model.Event
import retrofit2.http.*
import dt.prod.patternvm.core.model.ResponseWrapper
import dt.prod.patternvm.createProblem.models.CreateProblemRequest
import dt.prod.patternvm.createProblem.models.CreateProblemRequestPhoto
import dt.prod.patternvm.createProblem.models.PhotoResponse

interface CreateEventApi {

    @POST("/rzd/Api.php")
    suspend fun createEvent(
        @Body body: CreateProblemRequest
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