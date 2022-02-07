package dt.prod.patternvm.createProblem.models

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

class CreateProblemRequest(
    var name: String,
    var description: String,
    @SerializedName("category")
    var tags: String,
    @SerializedName("photo_event")
    var photo: String,
    var typeUser: String
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        ""
    )
}

class CreateProblemRequestPhoto(
    var name: String,
    @SerializedName("image")
    var photo: String
) {
    constructor():this(
        "test228",
        ""
    )
}

