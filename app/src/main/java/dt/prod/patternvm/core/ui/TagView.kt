package dt.prod.patternvm.core.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import dt.prod.patternvm.createProblem.ui.TagClickListener
import dt.prod.patternvm.R


class TagView(
    context: Context, tag: String, cancelTag: TagClickListener = object : TagClickListener {
        override fun onTagClosed(tagId: String) {}
    }
) : LinearLayout(context, null) {
    var tvTitle: TextView
    var clRoot: ConstraintLayout

    init {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_tag, this, true)
        tvTitle = view.findViewById(R.id.tvTitle)
        clRoot = view.findViewById(R.id.clRoot)
        tvTitle.text = tag

//        ivCancel.setColorFilter(
//            ContextCompat.getColor(
//                context,
//                R.color.secondary
//            ), android.graphics.PorterDuff.Mode.SRC_IN
//        )
        clRoot.setOnClickListener {
            Log.d("test", "ivCancel")
            cancelTag.onTagClosed(tag)
        }
    }
}