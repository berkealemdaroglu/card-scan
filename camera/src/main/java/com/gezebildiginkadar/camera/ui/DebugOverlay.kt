package com.gezebildiginkadar.camera.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Size
import android.util.TypedValue
import android.view.View
import androidx.annotation.VisibleForTesting
import com.gezebildiginkadar.camera.scan_ui.DebugDetectionBox

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Deprecated(
    message = "Replaced by stripe card scan. See https://github.com/stripe/stripe-android/tree/master/stripecardscan",
    replaceWith = ReplaceWith("StripeCardScan"),
)
internal fun RectF.scaled(scaledSize: Size): RectF {
    return RectF(
        this.left * scaledSize.width,
        this.top * scaledSize.height,
        this.right * scaledSize.width,
        this.bottom * scaledSize.height
    )
}

/**
 * A detection box to display on the debug overlay.
 */
@Deprecated(
    message = "Replaced by stripe card scan. See https://github.com/stripe/stripe-android/tree/master/stripecardscan",
    replaceWith = ReplaceWith("StripeCardScan"),
)
data class DebugDetectionBox(
    val rect: RectF,

    val confidence: Float,

    val label: String
)

@Deprecated(
    message = "Replaced by stripe card scan. See https://github.com/stripe/stripe-android/tree/master/stripecardscan",
    replaceWith = ReplaceWith("StripeCardScan"),
)
class DebugOverlay(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2F
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            20F,
            resources.displayMetrics
        )
        textAlign = Paint.Align.LEFT
    }

    private var boxes: Collection<DebugDetectionBox>? = null




    fun setBoxes(boxes: Collection<DebugDetectionBox>?) {
        this.boxes = boxes
        invalidate()
        requestLayout()
    }

    fun clearBoxes() {
        setBoxes(emptyList())
    }
}
