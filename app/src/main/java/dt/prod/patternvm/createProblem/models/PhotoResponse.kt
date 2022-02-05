package dt.prod.patternvm.createProblem.models

import com.google.gson.annotations.SerializedName

class PhotoResponse(
    @SerializedName("link")
    var url: String,
    @SerializedName("name")
    var photo_name:String
) {
    constructor():this(
        "",
        ""
    )
}