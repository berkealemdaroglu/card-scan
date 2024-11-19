package com.gezebildiginkadar.mlkit_example

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class CustomOverlay @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val rect = RectF()

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private val dimPaint = Paint().apply {
        color = Color.parseColor("#80000000") // Şeffaf siyah bir renk
        style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Görünüm boyutları belli olduğunda dikdörtgeni güncelle
        rect.left = (w * 0.05).toFloat()
        rect.top = (h * 0.35).toFloat()
        rect.right = (w * 0.95).toFloat()
        rect.bottom = (h * 0.65).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), dimPaint)
        canvas.save()
        canvas.clipRect(rect)
        canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR)
        canvas.restore()
        canvas.drawRect(rect, paint)
    }

    fun getBounds(): RectF = rect
}
