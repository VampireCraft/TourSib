package dt.prod.patternvm.createProblem.data

import androidx.fragment.app.FragmentManager
import dt.prod.patternvm.createProblem.domain.CreateProblemNavigator
import dt.prod.patternvm.createProblem.ui.*

class CreateProblemNavigatorImpl(
    private val fragmentManager: FragmentManager,
    private val rootId: Int
) :
    CreateProblemNavigator {
    override fun openStartScreen() {
        fragmentManager
            .beginTransaction()
            .add(rootId, FragmentCreateProblem())
            .addToBackStack(FragmentCreateProblem::javaClass.name)
            .commit()
    }

    override fun openCameraScreen(absolutePath: String) {
        fragmentManager
            .beginTransaction()
            .add(rootId, FragmentChooseColor().newInstance(
                CreateProblemTypeEnum.EVENT_CAMERA,
                absolutePath
            ))
            .addToBackStack(FragmentChooseColor::javaClass.name)
            .commit()
    }

    override fun openGalleryScreen(absolutePath: String) {
        fragmentManager
            .beginTransaction()
            .add(rootId, FragmentChooseColor().newInstance(
                CreateProblemTypeEnum.EVENT_GALLERY,
                absolutePath
            ))
            .addToBackStack(FragmentChooseColor::javaClass.name)
            .commit()
    }
}