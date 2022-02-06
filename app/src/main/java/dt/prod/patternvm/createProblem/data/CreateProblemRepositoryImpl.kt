package dt.prod.patternvm.createProblem.data

import dt.prod.patternvm.core.model.Event
import dt.prod.patternvm.createProblem.domain.CreateProblemRepository
import dt.prod.patternvm.createProblem.models.CreateProblemRequest
import dt.prod.patternvm.createProblem.models.CreateProblemRequestPhoto
import dt.prod.patternvm.createProblem.models.PhotoResponse
import dt.prod.patternvm.createProblem.network.CreateEventApi
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class CreateProblemRepositoryImpl(val createEventApi: CreateEventApi) :
    CreateProblemRepository {

    override suspend fun createEvent(createEventRequest: CreateProblemRequest): Event<String> {
        return try {
            val result = createEventApi.createEvent(
                apicall = "postproblem",
                tag = createEventRequest.tags,
                description = createEventRequest.description,
                typeUsers = "101",
                image = createEventRequest.photo,
                name = createEventRequest.name
            )
            when (result.statusId) {
                200 -> {
                    Event.success(result.data)
                }
                else -> Event.error(result?.error ?: "Ошибка запроса")
            }
        } catch (e: Exception) {
            Event.error(e.message.toString())
        }
    }

    override suspend fun sendPhoto(eventCreationRequestPhoto: CreateProblemRequestPhoto): Event<PhotoResponse> {
        return try {
            val result = createEventApi.sendPhoto(eventCreationRequestPhoto)
            when (result.statusId) {
                200 -> {
                    Event.success(result.data)
                }
                else -> Event.error(result.error ?: "Ошибка запроса")
            }
        } catch (e: Exception) {
            Event.error(e.message.toString())
        }
    }

      private fun prepareFilePart(imageUri: String): MultipartBody.Part {
        val file = File(imageUri)
        val requestFile = RequestBody.create(
            MediaType.parse("image/*"),
            file
        )
        return MultipartBody.Part.createFormData("photo_event", file.name, requestFile)
    }

}