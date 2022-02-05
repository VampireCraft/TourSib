package dt.prod.patternvm.createProblem.domain

interface CreateProblemNavigator {
    fun openStartScreen()
    fun openCameraScreen(absolutePath: String)
    fun openGalleryScreen(absolutePath: String)
}