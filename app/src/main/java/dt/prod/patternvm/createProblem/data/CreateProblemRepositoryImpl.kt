package dt.prod.patternvm.createProblem.data

import dt.prod.patternvm.core.model.Event
import dt.prod.patternvm.createProblem.domain.CreateProblemRepository
import dt.prod.patternvm.createProblem.models.CreateProblemRequest
import dt.prod.patternvm.createProblem.models.CreateProblemRequestPhoto
import dt.prod.patternvm.createProblem.models.PhotoResponse
import dt.prod.patternvm.createProblem.domain.CreateEventApi

class CreateProblemRepositoryImpl(val createEventApi: CreateEventApi) :
    CreateProblemRepository {

    override suspend fun createEvent(createEventRequest: CreateProblemRequest): Event<String> {
        return try {
            val result = createEventApi.createEvent(
                apicall = "postproblem",
                tag = createEventRequest.tags,
                description = createEventRequest.description,
                typeUsers = createEventRequest.typeUser,
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
}