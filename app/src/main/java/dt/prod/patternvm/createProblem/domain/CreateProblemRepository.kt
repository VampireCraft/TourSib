package dt.prod.patternvm.createProblem.domain

import dt.prod.patternvm.core.model.Event
import dt.prod.patternvm.createProblem.models.CreateProblemRequest
import dt.prod.patternvm.createProblem.models.CreateProblemRequestPhoto
import dt.prod.patternvm.createProblem.models.PhotoResponse

interface CreateProblemRepository {
    suspend fun createEvent(createEventRequest: CreateProblemRequest): Event<String>
    suspend fun sendPhoto(eventCreationRequestPhoto: CreateProblemRequestPhoto): Event<PhotoResponse>
}