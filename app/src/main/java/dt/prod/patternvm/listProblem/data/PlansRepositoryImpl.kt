package dt.prod.patternvm.listProblem.data

import dt.prod.patternvm.core.model.Event
import dt.prod.patternvm.core.network.TokenRepository
import dt.prod.patternvm.listProblem.domain.EventApi
import dt.prod.patternvm.listProblem.domain.PlansRepository
import dt.prod.patternvm.listProblem.models.ListItemModel

class PlansRepositoryImpl(private val eventApi: EventApi) : PlansRepository {

    override suspend fun editProblem(listItemModel: ListItemModel): Event<String> {
        return try {
            val result = eventApi.editProblem(
                apicall = "editproblem",
                id_problem = listItemModel.id,
                tag = listItemModel.tags,
                description = listItemModel.description,
                typeUsers = listItemModel.adress,
                name = listItemModel.name,
                timeRemove = listItemModel.timeRemove,
                image = listItemModel.photo,
                timeAccept = listItemModel.timeAccept,
                timeOver = listItemModel.timeOver
            )
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

    override suspend fun getListProblem(): Event<List<ListItemModel>> {
        return try {
            val users = when (TokenRepository.accessToken){
                "01" -> "101"
                "02" -> "102"
                "03" -> "0"
                else -> "228"
            }
            val result = eventApi.getListProblem(
                apicall = "getproblem",
                adress_num = users
            )
            when (result.statusId) {
                200 -> {
                    if (result.data.isNullOrEmpty()) {
                        Event.success(result.data)
                    } else {
                        val list: MutableList<ListItemModel> = mutableListOf()
                        for (itemModel in result.data){
                            if (itemModel.name.isNotEmpty())
                                if (itemModel.tags.isNotEmpty())
                                    if(itemModel.adress.isNotEmpty())
                                        if (itemModel.photo.isNotEmpty())
                                            list.add(0,itemModel)
                        }
                        Event.success(list)
                    }
                }
                else -> Event.error(result.error ?: "Ошибка запроса")
            }
        } catch (e: Exception) {
            Event.error(e.message.toString())
        }
    }
}