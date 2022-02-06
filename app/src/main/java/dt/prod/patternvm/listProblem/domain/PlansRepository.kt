package dt.prod.patternvm.listProblem.domain

import dt.prod.patternvm.core.model.Event
import dt.prod.patternvm.listProblem.models.ListItemModel

interface PlansRepository {
    suspend fun editProblem(listItemModel: ListItemModel): Event<String>
    suspend fun getListProblem(): Event<List<ListItemModel>>
}