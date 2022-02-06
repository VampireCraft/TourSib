package dt.prod.patternvm.listProblem.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ListItemModel(
        var id: String,
        @SerializedName("title")
        var name: String,
        @SerializedName("definition")
        var description: String,
        @SerializedName("number")
        var tags: String,
        @SerializedName("img")
        var photo: String,
        var adress: String,
        @SerializedName("datetime_query")
        var timeCreate: String,
        @SerializedName("date_remove")
        var timeRemove: String
    ) : Serializable {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )
}
