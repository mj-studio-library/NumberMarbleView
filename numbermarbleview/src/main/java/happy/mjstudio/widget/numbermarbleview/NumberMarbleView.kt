package happy.mjstudio.widget.numbermarbleview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.properties.Delegates
import kotlin.random.Random


class NumberMarbleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs), View.OnTouchListener {

    private val TAG = NumberMarbleView::class.java.simpleName


    private fun Int.toPixel() = (context.resources.displayMetrics.density * this)
    private fun Int.toDP() = this / context.resources.displayMetrics.density

    var marbleRadius: Float by Delegates.observable(8.toPixel()) { _, old, new ->
        requestLayout()
    }

    var marbleCount: Int by Delegates.observable(-1) { _, old, new ->
        requestLayout()
    }

    var lineLength: Float by Delegates.observable(16.toPixel()) { _, old, new ->
        requestLayout()
    }

    var lineWidth: Float by Delegates.observable(2.toPixel()) { _, old, new ->
        invalidate()
    }

    var lineColor: Int by Delegates.observable(Color.BLACK) { _, old, new ->
        invalidate()
    }

    var numberTextColor: Int by Delegates.observable(Color.WHITE) { _, old, new ->
        invalidate()
    }

    var numberTextSize: Float by Delegates.observable(12.toPixel()) { _, old, new ->
        invalidate()
    }

    var numberTextShadowColor: Int by Delegates.observable(Color.DKGRAY) { _, old, new ->
        invalidate()
    }

    var isVertical: Boolean by Delegates.observable(false) { _, old, new ->
        requestLayout()
    }

    private val marblePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.style = Paint.Style.FILL_AND_STROKE
        this.setShadowLayer(0.2f, 0f, 0f, Color.DKGRAY)
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.style = Paint.Style.STROKE
    }

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 32.toFloat()
        color = numberTextColor
        this.textAlign = Paint.Align.CENTER
        this.setShadowLayer(0.5f, 1f, 1f, Color.DKGRAY)
    }

    private val colorSet : MutableList<Int> = (1..200).map {
        Color.rgb(Random.nextInt(50, 150), Random.nextInt(50, 150), Random.nextInt(50, 150))
    }.toMutableList()


    init {
        if (attrs != null) {
            getStyleableAttrs(attrs)
        }

        this.setOnTouchListener(this)
    }

    fun setMarbleColor(position : Int, color : Int) {
        try {
            colorSet.set(position, color)
        }catch(t : Throwable) {
            Log.e(TAG,t.toString())
        }finally {
            invalidate()
        }
    }

    private fun getStyleableAttrs(attr: AttributeSet) {
        context.theme.obtainStyledAttributes(attr, R.styleable.NumberMarbleView, 0, 0).let { arr ->
            marbleCount = arr.getInteger(R.styleable.NumberMarbleView_marble_marble_count, marbleCount)
            marbleRadius = arr.getDimension(R.styleable.NumberMarbleView_marble_marble_radius, marbleRadius)
            lineLength = arr.getDimension(R.styleable.NumberMarbleView_marble_line_length, lineLength)
            lineWidth = arr.getDimension(R.styleable.NumberMarbleView_marble_line_width, lineWidth)
            lineColor = arr.getColor(R.styleable.NumberMarbleView_marble_line_color, lineColor)
            numberTextColor = arr.getColor(R.styleable.NumberMarbleView_marble_number_text_color, numberTextColor)
            numberTextSize = arr.getDimension(R.styleable.NumberMarbleView_marble_number_text_size, numberTextSize)
            isVertical = arr.getBoolean(R.styleable.NumberMarbleView_marble_orientation_vertical, isVertical)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (marbleCount < 1) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val width = (marbleCount * (marbleRadius * 2) + (marbleCount - 1) * lineLength).roundToInt()
        val height = (marbleRadius * 2).roundToInt()

        if (!isVertical) {
            setMeasuredDimension(width, height)
        } else {
            setMeasuredDimension(height, width)
        }
    }

    //region Draw
    override fun onDraw(canvas: Canvas?) {

        //No Draw
        if (marbleCount < 1)
            return

        //Update Paints
        linePaint.strokeWidth = lineWidth
        linePaint.color = lineColor
        textPaint.textSize = numberTextSize
        textPaint.color = numberTextColor
        textPaint.setShadowLayer(0.5f, 0.5f, 0.5f, numberTextShadowColor)

        //Draw Marbles


        var curCenterHorizontalPos = marbleRadius
        var curCenterVerticalPos = marbleRadius

        val plusLineLength = lineLength + (marbleRadius * 2)

        repeat(marbleCount) {
            marblePaint.color = colorSet[it]

            if (it != marbleCount - 1)
                canvas?.drawLine(
                    curCenterHorizontalPos,
                    curCenterVerticalPos,
                    if (!isVertical) curCenterHorizontalPos + plusLineLength else curCenterHorizontalPos,
                    if (!isVertical) curCenterVerticalPos else curCenterVerticalPos + plusLineLength,
                    linePaint
                )
            canvas?.drawCircle(
                curCenterHorizontalPos,
                curCenterVerticalPos,
                if(isAnimating && curAnimatedMarbleIndex == it) curAnimatedMarbleRadius else marbleRadius,
                marblePaint
            )
            canvas?.drawText(
                (it + 1).toString(),
                curCenterHorizontalPos,
                curCenterVerticalPos - ((textPaint.descent() + textPaint.ascent()) / 2),
                textPaint
            )

            if (!isVertical)
                curCenterHorizontalPos += ((marbleRadius * 2) + lineLength)
            else
                curCenterVerticalPos += ((marbleRadius * 2) + lineLength)
        }

        //Draw Lines


    }
    //endregion


    //region Animation
    private var curAnimatedMarbleIndex = -1
    private var isAnimating = false
    private var curAnimatedMarbleRadius : Float = -1f

    private fun animateMarble(position : Int) {
        ValueAnimator.ofFloat(marbleRadius, marbleRadius * 0.8f).apply {
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE

            duration = 150L

            addUpdateListener {
                val value = it.animatedValue as Float
                curAnimatedMarbleRadius = value

                this@NumberMarbleView.invalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
                    isAnimating = true
                    curAnimatedMarbleIndex = position
                    curAnimatedMarbleRadius = marbleRadius
                }

                override fun onAnimationEnd(animation: Animator?) {
                    isAnimating = false
                    curAnimatedMarbleIndex = -1
                    curAnimatedMarbleRadius = -1f
                }
            })

            start()
        }
    }
    //endregion


    //region TouchListener
    @FunctionalInterface
    interface OnMarbleTouchListener {
        fun onClick(position: Int)
    }

    fun setOnMarbleTouchListener(listener: (Int) -> Unit) {
        setOnMarbleTouchListener(object : OnMarbleTouchListener {
            override fun onClick(position: Int) {
                listener(position)
            }
        })
    }

    fun setOnMarbleTouchListener(listener: OnMarbleTouchListener) {
        touchListener = listener
    }

    private var touchListener: OnMarbleTouchListener? = null


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val x = event?.x ?: return false
        val y = event?.y ?: return false

        if (event.actionMasked != MotionEvent.ACTION_DOWN) return false

        if (marbleCount < 1) return false

        var centerPos = marbleRadius
        repeat(marbleCount) {

            if (calculateDistance(
                    if (!isVertical) centerPos else marbleRadius,
                    if (!isVertical) marbleRadius else centerPos,
                    x,
                    y
                ) < marbleRadius
            ) {
                touchListener?.onClick(it)
                animateMarble(it)
                return true
            }

            centerPos += ((marbleRadius * 2) + lineLength)
        }

        return false
    }

    private fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float) = sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))


    //endregion

}