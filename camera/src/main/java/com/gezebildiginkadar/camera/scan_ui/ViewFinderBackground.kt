package com.gezebildiginkadar.camera.scan_ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.gezebildiginkadar.camera.R
import kotlin.math.roundToInt

/**
 * This class draws a background with a hole in the middle of it.
 */
@Deprecated(
    message = "Replaced by stripe card scan. See https://github.com/stripe/stripe-android/tree/master/stripecardscan",
    replaceWith = ReplaceWith("StripeCardScan"),
)
class ViewFinderBackground(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private var viewFinderRect: Rect? = null
    private var onDrawListener: (() -> Unit)? = null

    fun setViewFinderRect(viewFinderRect: Rect) {
        this.viewFinderRect = viewFinderRect
        requestLayout()
    }

    fun clearViewFinderRect() {
        this.viewFinderRect = null
    }

    override fun setBackgroundColor(@ColorInt color: Int) {
        paintBackground.color = color
        requestLayout()
    }

    fun getBackgroundLuminance(): Int {
        val color = paintBackground.color
        val r = (color shr 16 and 0xff) / 255F
        val g = (color shr 8 and 0xff) / 255F
        val b = (color and 0xff) / 255F

        return ((0.2126F * r + 0.7152F * g + 0.0722F * b) * 255F).roundToInt()
    }

    fun setOnDrawListener(onDrawListener: () -> Unit) {
        this.onDrawListener = onDrawListener
    }

    fun clearOnDrawListener() {
        this.onDrawListener = null
    }

    private val theme = context.theme
    private val attributes = theme.obtainStyledAttributes(attrs, R.styleable.ViewFinderBackground, 0, 0)



    private var paintBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        //c/olor = backgroundColor
        style = Paint.Style.FILL
    }

    private val paintWindow = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        style = Paint.Style.FILL
    }

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPaint(paintBackground)

        val viewFinderRect = this.viewFinderRect
        if (viewFinderRect != null) {
            canvas.drawRect(viewFinderRect, paintWindow)
        }

        val onDrawListener = this.onDrawListener
        if (onDrawListener != null) {
            onDrawListener()
        }
    }
}