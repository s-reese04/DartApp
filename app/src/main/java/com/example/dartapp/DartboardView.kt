package com.example.dartapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.*

class DartboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var onSegmentClick: ((value: Int) -> Unit)? = null

    private val dartNumbers = intArrayOf(
        20, 1, 18, 4, 13, 6, 10, 15, 2, 17,
        3, 19, 7, 16, 8, 11, 14, 9, 12, 5
    )

    private val colorBlack   = Color.parseColor("#1a1a1a")
    private val colorCream   = Color.parseColor("#F5E6C8")
    private val colorRed     = Color.parseColor("#c0392b")
    private val colorGreen   = Color.parseColor("#2c8a2c")
    private val colorWire    = Color.parseColor("#888888")
    private val colorText    = Color.parseColor("#FFFFFF")
    private val colorBull    = Color.parseColor("#c0392b")
    private val colorBullRing = Color.parseColor("#2c8a2c")
    private val colorHighlight = Color.parseColor("#e8c84a")

    private var pressedSegment = -1
    private var pressedRing    = -1

    private val paintFill  = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val paintStroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = colorWire
        strokeWidth = 1.5f
    }
    private val paintText  = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colorText
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }
    private val paintHighlight = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = colorHighlight
        alpha = 160
    }

    private var cx = 0f
    private var cy = 0f
    private var radius = 0f

    private val rBull       = 0.045f
    private val rBullRing   = 0.095f
    private val rInner      = 0.37f
    private val rTriple     = 0.43f
    private val rOuter      = 0.68f
    private val rDouble     = 0.76f
    private val rNumber     = 0.88f

    private val rectF = RectF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cx = w / 2f
        cy = h / 2f
        radius = min(w, h) / 2f * 0.95f
        paintText.textSize = radius * 0.09f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawSegments(canvas)
        drawBullseye(canvas)
        drawNumbers(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        paintFill.color = Color.parseColor("#111111")
        canvas.drawCircle(cx, cy, radius * 1.02f, paintFill)
    }

    private fun drawSegments(canvas: Canvas) {
        val segAngle = 360f / 20f
        val startOffset = -90f - segAngle / 2f

        for (i in 0 until 20) {
            val startAngle = startOffset + i * segAngle
            val isLight = (i % 2 == 0)

            val outerColor = if (isLight) colorCream else colorBlack
            drawArcRing(canvas, rTriple, rOuter, startAngle, segAngle,
                if (pressedSegment == i && pressedRing == 0) colorHighlight else outerColor)

            val tripleColor = if (isLight) colorGreen else colorRed
            drawArcRing(canvas, rInner, rTriple, startAngle, segAngle,
                if (pressedSegment == i && pressedRing == 1) colorHighlight else tripleColor)

            drawArcRing(canvas, rBullRing, rInner, startAngle, segAngle,
                if (pressedSegment == i && pressedRing == 2) colorHighlight else outerColor)

            drawArcRing(canvas, rDouble, 1.0f, startAngle, segAngle,
                if (pressedSegment == i && pressedRing == 3) colorHighlight else tripleColor)

            paintStroke.color = colorWire
            paintStroke.strokeWidth = 1.2f
            val lineAngle = Math.toRadians(startAngle.toDouble())
            canvas.drawLine(
                cx + (radius * rBullRing * cos(lineAngle)).toFloat(),
                cy + (radius * rBullRing * sin(lineAngle)).toFloat(),
                cx + (radius * cos(lineAngle)).toFloat(),
                cy + (radius * sin(lineAngle)).toFloat(),
                paintStroke
            )
        }

        paintStroke.color = colorWire
        paintStroke.strokeWidth = 1.5f
        for (r in listOf(rBullRing, rInner, rTriple, rOuter, rDouble, 1.0f)) {
            canvas.drawCircle(cx, cy, radius * r, paintStroke)
        }
    }

    private fun drawArcRing(
        canvas: Canvas,
        innerR: Float,
        outerR: Float,
        startAngle: Float,
        sweepAngle: Float,
        color: Int
    ) {
        paintFill.color = color
        val path = Path()

        val outerRad = radius * outerR
        rectF.set(cx - outerRad, cy - outerRad, cx + outerRad, cy + outerRad)
        path.arcTo(rectF, startAngle, sweepAngle)

        val innerRad = radius * innerR
        rectF.set(cx - innerRad, cy - innerRad, cx + innerRad, cy + innerRad)
        path.arcTo(rectF, startAngle + sweepAngle, -sweepAngle)

        path.close()
        canvas.drawPath(path, paintFill)
    }

    private fun drawBullseye(canvas: Canvas) {
        paintFill.color = if (pressedSegment == 21) colorHighlight else colorBullRing
        canvas.drawCircle(cx, cy, radius * rBullRing, paintFill)
        paintStroke.color = colorWire
        canvas.drawCircle(cx, cy, radius * rBullRing, paintStroke)

        paintFill.color = if (pressedSegment == 20) colorHighlight else colorBull
        canvas.drawCircle(cx, cy, radius * rBull, paintFill)
        paintStroke.color = colorWire
        canvas.drawCircle(cx, cy, radius * rBull, paintStroke)
    }

    private fun drawNumbers(canvas: Canvas) {
        val segAngle = 360f / 20f
        val startOffset = -90f

        for (i in 0 until 20) {
            val angle = startOffset + i * segAngle
            val rad = Math.toRadians(angle.toDouble())
            val nx = cx + (radius * rNumber * cos(rad)).toFloat()
            val ny = cy + (radius * rNumber * sin(rad)).toFloat()

            paintFill.color = Color.parseColor("#222222")
            canvas.drawCircle(nx, ny, paintText.textSize * 0.75f, paintFill)

            paintText.color = colorText
            val fm = paintText.fontMetrics
            canvas.drawText(
                dartNumbers[i].toString(),
                nx,
                ny - (fm.ascent + fm.descent) / 2f,
                paintText
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x - cx
        val y = event.y - cy
        val dist = sqrt(x * x + y * y) / radius

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouch(x, y, dist, press = true)
                return true
            }
            MotionEvent.ACTION_UP -> {
                handleTouch(x, y, dist, press = false)
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                pressedSegment = -1
                pressedRing    = -1
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun handleTouch(x: Float, y: Float, dist: Float, press: Boolean) {
        if (dist > 1.0f) { pressedSegment = -1; invalidate(); return }

        if (dist < rBull) {
            pressedSegment = 20
            if (!press) onSegmentClick?.invoke(50)
            invalidate()
            return
        }

        if (dist < rBullRing) {
            pressedSegment = 21
            if (!press) onSegmentClick?.invoke(25)
            invalidate()
            return
        }

        val angle = ((Math.toDegrees(atan2(y.toDouble(), x.toDouble())) + 90 + 360) % 360).toFloat()
        val segAngle = 360f / 20f
        val segIndex = ((angle + segAngle / 2) / segAngle).toInt() % 20

        val ring = when {
            dist in rInner..rTriple   -> 1  // Triple
            dist in rOuter..rDouble   -> 3  // Double
            dist in rTriple..rOuter   -> 0  // Normal outer
            dist in rBullRing..rInner -> 2  // Normal inner
            else -> -1
        }

        pressedSegment = segIndex
        pressedRing    = ring

        if (!press && ring >= 0) {
            val baseValue = dartNumbers[segIndex]
            val score = when (ring) {
                1    -> baseValue * 3   // Triple
                3    -> baseValue * 2   // Double
                else -> baseValue       // Normal
            }
            onSegmentClick?.invoke(score)
        }

        invalidate()
    }
}
