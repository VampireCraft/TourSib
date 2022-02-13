package dt.prod.patternvm.listProblem.models

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dt.prod.patternvm.core.model.Event
import dt.prod.patternvm.listProblem.domain.PlansRepository
import kotlinx.coroutines.launch

class PlansViewModel @ViewModelInject constructor(private val plansRepository: PlansRepository) :
    ViewModel() {

    var listItemModel = ListItemModel()
    var acceptedEvents: MutableLiveData<Event<String>> = MutableLiveData()
    fun editProblem() {
        acceptedEvents.value = Event.loading()
        viewModelScope.launch {
            acceptedEvents.value = plansRepository.editProblem(listItemModel)
        }
    }

    val myEvents: MutableLiveData<Event<List<ListItemModel>>> = MutableLiveData()
    fun getListProblem() {
        myEvents.value = Event.loading()
        viewModelScope.launch {
            myEvents.value = plansRepository.getListProblem()
        }
    }
}