package dt.prod.patternvm.createProblem.models

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dt.prod.patternvm.createProblem.domain.CreateProblemNavigator
import dt.prod.patternvm.createProblem.domain.CreateProblemRepository
import kotlinx.coroutines.launch
import dt.prod.patternvm.core.model.Event

class CreateProblemViewModel @ViewModelInject constructor(val createProblemRepository: CreateProblemRepository) :
    ViewModel() {
    var navigator: CreateProblemNavigator? = null

    var eventCreationRequest = CreateProblemRequest()
    val createEventResponse: MutableLiveData<Event<String>> = MutableLiveData()
    fun createEvent() {
        createEventResponse.value = Event.loading()
        viewModelScope.launch {
            createEventResponse.value = createProblemRepository.createEvent(eventCreationRequest)
        }
    }

    var eventCreationRequestPhoto = CreateProblemRequestPhoto()
    val sendPhotoResponse: MutableLiveData<Event<PhotoResponse>> = MutableLiveData()
    fun sendPhoto() {
        sendPhotoResponse.value = Event.loading()
        viewModelScope.launch {
            sendPhotoResponse.value = createProblemRepository.sendPhoto(eventCreationRequestPhoto)
        }
    }
}