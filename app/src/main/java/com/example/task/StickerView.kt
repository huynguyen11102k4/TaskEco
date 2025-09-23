//package com.example.task
//
//import android.Manifest
//import android.R
//import android.animation.Animator
//import android.animation.AnimatorListenerAdapter
//import android.animation.ValueAnimator
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.app.WallpaperManager
//import android.content.Context
//import android.content.res.TypedArray
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.DashPathEffect
//import android.graphics.Paint
//import android.graphics.PointF
//import android.graphics.RectF
//import android.opengl.Matrix
//import android.os.Bundle
//import android.os.SystemClock
//import android.util.AttributeSet
//import android.util.DisplayMetrics
//import android.util.EventLog.Event
//import android.util.Log
//import android.view.MotionEvent
//import android.view.ViewConfiguration
//import android.widget.RelativeLayout
//import androidx.annotation.RequiresPermission
//import androidx.core.content.ContextCompat
//import androidx.core.graphics.createBitmap
//import androidx.core.graphics.toColorInt
//import androidx.core.view.MotionEventCompat
//import androidx.lifecycle.Lifecycle.Event
//import java.io.File
//import java.io.IOException
//import java.lang.Math.toDegrees
//import java.util.Collections
//import kotlin.collections.isNotEmpty
//import kotlin.math.abs
//import kotlin.math.atan2
//import kotlin.math.sqrt
//import androidx.core.graphics.withRotation
//
//@SuppressLint("Recycle")
//open class StickerView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
//    RelativeLayout(context, attrs, defStyleAttr) {
//    private var showBorder = false
//    private var bringToFrontCurrentSticker = false
//    private var drawSticker = true
//    private var drawLine = true
//    private var showBackground = true
//    private var showIcons = true
//    private var mappingPoint: FloatArray = floatArrayOf(0f)
//    private var allowShowLineHorizon = false
//    private var allowShowLineVertical = false
//
//    companion object {
//        const val DEFAULT_MIN_CLICK_DELAY_TIME = 250
//        const val FLIP_HORIZONTALLY = 1
//        const val FLIP_VERTICALLY = 1 shl 1
//    }
//
//    private val stickers: MutableList<Sticker> = ArrayList()
//
//    private val icons: MutableList<BitmapStickerIcon> = ArrayList(4)
//
//    private val borderPaint = Paint()
//    private val borderPaint1 = Paint()
//    private val lineUpPaint = Paint()
//    private val stickerRect = RectF()
//
//    private val sizeMatrix = Matrix()
//    private val downMatrix = Matrix()
//    private val moveMatrix = Matrix()
//
//    private val bitmapPoints = FloatArray(8)
//    private val bitmapPointCurve = FloatArray(8)
//    private val bounds = FloatArray(8)
//    private val point = FloatArray(2)
//    private val currentCenterPoint = PointF()
//    private val tmp = FloatArray(2)
//    private var midPoint = PointF()
//
//    private var touchSlop = 0
//    private var x = 0f
//    private var y = 0f
//    private var currentIcon: BitmapStickerIcon? = null
//
//    private var downX = 0f
//    private var downY = 0f
//
//    private var oldDistance = 0f
//    private var oldRotation = 0f
//    private var currentSticker = 0
//    private var oldSticker = 0
//    var rectCurve: RectF? = null
//    private var marginSize = 40
//
//    private var currentMode = ActionMode.NONE
//    private var handlingSticker: Sticker? = null
//    private var locked = false
//    private var disableClick = false
//
//    private var constrained = false
//
//    private var onStickerOperationListener: OnStickerOperationListener? = null
//
//    private var lastClickTime: Long = 0
//    private var minClickDelayTime = DEFAULT_MIN_CLICK_DELAY_TIME
//    private var isShowLine = false
//
//    private var originalLeft = 0f
//    private var originalTop = 0f
//
//    fun getCurrentMode(): Int {
//        return currentMode
//    }
//
//    constructor(context: Context) : this(context, null)
//
//    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
//
//    init {
//        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
//        val a: TypedArray? = null
//        try {
//            a = context.obtainStyledAttributes(attrs, R.styleable.StickerView)
//            showIcons = a.getBoolean(R.styleable.StickerView_showIcons, false)
//            showBorder = a.getBoolean(R.styleable.StickerView_showBorder, false)
//            bringToFrontCurrentSticker =
//                a.getBoolean(R.styleable.StickerView_bringToFrontCurrentSticker, false)
//            borderPaint1.style = Paint.Style.STROKE
//            borderPaint1.strokeWidth = 6f
//            borderPaint1.setPathEffect(DashPathEffect(floatArrayOf(15f, 20f), 0f))
//            borderPaint1.isAntiAlias = true
//            borderPaint1.setColor(Color.WHITE)
//
//            lineUpPaint.strokeWidth = 2f
//            lineUpPaint.style = Paint.Style.STROKE
//            lineUpPaint.setColor("#03A9F4".toColorInt())
//
//            borderPaint.isAntiAlias = true
//            borderPaint.setColor(
//                a.getColor(
//                    R.styleable.StickerView_borderColor,
//                    context.resources.getColor(R.color.colorBGCustomview)
//                )
//            )
//            borderPaint.setAlpha(a.getInteger(R.styleable.StickerView_borderAlpha, 128))
//            marginSize = resources.getDimensionPixelSize(R.dimen._16sdp)
//            configDefaultIcons()
//        } finally {
//            a?.recycle()
//        }
//    }
//
//    fun getStickers(): List<Sticker> {
//        return stickers
//    }
//
//    fun setDrawSticker(drawSticker: Boolean) {
//        this.drawSticker = drawSticker
//    }
//
//    fun isDrawSticker(): Boolean {
//        return drawSticker
//    }
//
//    fun setShowIcons(showIcons: Boolean) {
//        this.showIcons = showIcons
//    }
//
//    fun isDrawLine(): Boolean {
//        return drawLine
//    }
//
//    fun setDrawLine(drawLine: Boolean) {
//        this.drawLine = drawLine
//    }
//
//    fun isShowBackground(): Boolean {
//        return showBackground
//    }
//
//    fun setShowBackground(showBackground: Boolean) {
//        this.showBackground = showBackground
//    }
//
//    fun configDefaultIcons() {
//        try {
//            val deleteIcon = BitmapStickerIcon(
//                ContextCompat.getDrawable(context, R.drawable.ic_close_1),
//                BitmapStickerIcon.LEFT_TOP
//            )
//            deleteIcon.setIconEvent(DeleteIconEvent())
//            val zoomIcon = BitmapStickerIcon(
//                ContextCompat.getDrawable(context, R.drawable.ic_zoom_1),
//                BitmapStickerIcon.RIGHT_BOTTOM
//            )
//            zoomIcon.setIconEvent(ZoomIconEvent())
//            icons.clear()
//            icons.add(zoomIcon)
//            icons.add(deleteIcon)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    fun moveToTop(sticker: Sticker) {
//        if (sticker is TextSticker) {
//            originalLeft = getRectFBase(bitmapPoints).left
//            originalTop = getRectFBase(bitmapPoints).top
//            if (handlingSticker is TextSticker && ((handlingSticker as TextSticker).curve != 0f)) {
//                val rectF = getRectFBase(bitmapPointCurve)
//                val stickerWidth = rectF.width()
//                val leftOffset = (width - stickerWidth) / 2
//                val targetX = leftOffset - rectF.left
//                val targetY = height / 3f - rectF.top
//                updateAnimation(sticker, rectF, targetX, targetY)
//            } else {
//                val rectF = getRectFBase(bitmapPoints)
//                val stickerWidth = rectF.width()
//                val leftOffset = (width - stickerWidth) / 2
//                val targetX = leftOffset - rectF.left
//                val targetY = height / 3f - rectF.top
//                updateAnimation(sticker, rectF, targetX, targetY)
//            }
//        }
//    }
//
//    private fun updateAnimation(sticker: Sticker, rectF: RectF, targetX: Float, targetY: Float) {
//        val animator = ValueAnimator.ofFloat(0f, 1f)
//        animator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
//            var startX = 0f
//            var startY = 0f
//
//            override fun onAnimationUpdate(animator: ValueAnimator) {
//                val animatedFraction = animation.animatedFraction
//                val newX = startX + animatedFraction * (targetX - startX)
//                val newY = startY + animatedFraction * (targetY - startY)
//                sticker.matrix.postTranslate(newX - startX, newY - startY)
//                startX = newX
//                startY = newY
//                invalidate()
//            }
//        })
//
//        animator.addListener(object : AnimatorListenerAdapter() {
//            override fun onAnimationEnd(animation: Animator) {
//                super.onAnimationEnd(animation)
//            }
//        })
//        animator.duration = 500
//        animator.start()
//    }
//
//    fun moveToOriginalPosition(sticker: Sticker) {
//        updateAnimation(
//            sticker,
//            getRectFBase(bitmapPoints),
//            originalLeft - getRectFBase(bitmapPoints).left,
//            originalTop - getRectFBase(bitmapPoints).top
//        );
//    }
//
//    fun swapLayers(oldPos: Int, newPos: Int) {
//        if (stickers.size >= oldPos && stickers.size >= newPos) {
//            Collections.swap(stickers, oldPos, newPos)
//            invalidate()
//        }
//    }
//
//    fun sendToLayer(oldPos: Int, newPos: Int) {
//        if (stickers.size >= oldPos && stickers.size >= newPos) {
//            val s = stickers[oldPos]
//            stickers.remove(oldPos)
//            stickers.add(newPos, s)
//            invalidate()
//        }
//    }
//
//    protected override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
//        super.onLayout(changed, left, top, right, bottom)
//        if (changed) {
//            stickerRect.left = left.toFloat()
//            stickerRect.top = top.toFloat()
//            stickerRect.right = right.toFloat()
//            stickerRect.bottom = bottom.toFloat()
//        }
//    }
//
//    protected override fun dispatchDraw(canvas: Canvas) {
//        super.dispatchDraw(canvas)
//        if (drawSticker) {
//            try {
//                if (!showBackground) {
//                    Paint().apply {
//                        color = resources.getColor(R.color.color_0000000)
//                        style = Paint.Style.FILL
//                        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), this)
//                    }
//                }
//                drawStickers(canvas)
//            } catch (e: Throwable) {
//            }
//        }
//    }
//
//    fun drawStickers(canvas: Canvas) {
//        var isCurve = false
//        if (handlingSticker != null && !locked && (showBorder || showIcons)) {
//            getStickerPoints(handlingSticker, bitmapPoints)
//        }
//
//        stickers.forEach { sticker ->
//            sticker.draw(canvas)
//        }
//
//        if (handlingSticker != null && !locked && (showBorder || showIcons)) {
//            if (handlingSticker is TextSticker && ((handlingSticker as TextSticker).getCurve() != 0)) {
//                isCurve = true
//            }
//            var x1 = 0f
//            var y1 = 0f
//            var x2 = 0f
//            var y2 = 0f
//            var x3 = 0f
//            var y3 = 0f
//            var x4 = 0f
//            var y4 = 0f
//            if (isCurve) {
//                getStickerPoints2(handlingSticker, bitmapPointCurve)
//                x1 = bitmapPointCurve[0]
//                y1 = bitmapPointCurve[1]
//                x2 = bitmapPointCurve[2]
//                y2 = bitmapPointCurve[3]
//                x3 = bitmapPointCurve[4]
//                y3 = bitmapPointCurve[5]
//                x4 = bitmapPointCurve[6]
//                y4 = bitmapPointCurve[7]
//            } else {
//                x1 = bitmapPoints[0]
//                y1 = bitmapPoints[1]
//                x2 = bitmapPoints[2]
//                y2 = bitmapPoints[3]
//                x3 = bitmapPoints[4]
//                y3 = bitmapPoints[5]
//                x4 = bitmapPoints[6]
//                y4 = bitmapPoints[7]
//            }
//            val rotation = calculateRotation(x4, y4, x3, y3)
//
//            if (showBorder) {
//                canvas.withRotation(rotation, (x4 - x1) / 2 + x1, (y4 - y1) / 2 + y1) {
//                    if (handlingSticker is TextSticker && ((handlingSticker as TextSticker).getCurve() != 0)) {
//                        rectCurve = getRectFBase(bitmapPointCurve)
//                        rectCurve?.let { drawLineText(this, it) }
//                    } else {
//                        drawLineText(this, getRectFBase(bitmapPoints));
//                        if (handlingSticker is TexSticker) {
//
//                        }
//                    }
//                }
//            }
//
//            if (showIcons) {
//                icons.forEach { icon ->
//                    var x = 0f
//                    var y = 0f
//                    when (icon.position) {
//                        BitmapStickerIcon.LEFT_TOP -> {
//                            x = x1
//                            y = y1
//                        }
//
//                        BitmapStickerIcon.RIGHT_TOP -> {
//                            x = x2
//                            y = y2
//                        }
//
//                        BitmapStickerIcon.LEFT_BOTTOM -> {
//                            x = x3
//                            y = y3
//                        }
//
//                        BitmapStickerIcon.RIGHT_BOTTOM -> {
//                            x = x4
//                            y = y4
//                        }
//                    }
//                    configIconMatrix(icon, x, y, rotation)
//                    icon.draw(canvas, borderPaint)
//                }
//            }
//        }
//
//        if (this.handlingSticker != null && !locked && this.allowShowLineHorizon && (currentMode == ActionMode.DRAG || currentMode == ActionMode.DRAG) && isShowLine) {
//            canvas.drawLine(width / 2f, 0f, width / 2f, height.toFloat(), lineUpPaint)
//        }
//        if (this.handlingSticker != null && !locked && this.allowShowLineVertical && (currentMode == ActionMode.DRAG || currentMode == ActionMode.DRAG) && isShowLine) {
//            canvas.drawLine(0f, height / 2f, width.toFloat(), height / 2f, lineUpPaint)
//        }
//
//    }
//
//    private fun drawCircle(canvas: Canvas, rectF: RectF) {
//        Paint(Paint.ANTI_ALIAS_FLAG).apply {
//            style = Paint.Style.FILL
//            color = Color.WHITE
//            canvas.drawCircle(
//                rectF.left,
//                rectF.top,
//                resources.getDimension(R.dimen._2sdp),
//                this
//            )
//            canvas.drawCircle(
//                rectF.right,
//                rectF.top,
//                resources.getDimension(R.dimen._2sdp),
//                this
//            )
//            canvas.drawCircle(
//                rectF.left,
//                rectF.bottom,
//                resources.getDimension(R.dimen._2sdp),
//                this
//            )
//            canvas.drawCircle(
//                rectF.right,
//                rectF.bottom,
//                resources.getDimension(R.dimen._2sdp),
//                this
//            )
//        }
//    }
//
//    private fun getRectFBase(coordinate: FloatArray): RectF {
//        val x1 = coordinate[0]
//        val y1 = coordinate[1]
//        val x2 = coordinate[2]
//        val y2 = coordinate[3]
//        val x3 = coordinate[4]
//        val y3 = coordinate[5]
//        val x4 = coordinate[6]
//        val y4 = coordinate[7]
//
//        val topBound = calculateDistance(x1, y1, x2, y2) / 2
//        val leftBound = calculateDistance(x1, y1, x3, y3) / 2
//        val middlePointX = (x4 - x1) / 2 + x1
//        val middlePointY = (y4 - y1) / 2 + y1
//
//        stickerRect.top = middlePointY - leftBound
//        stickerRect.left = middlePointX - topBound
//        stickerRect.right = middlePointX + topBound
//        stickerRect.bottom = middlePointY + leftBound
//
//        return stickerRect
//    }
//
//    fun getStickerRect(): RectF {
//        return stickerRect
//    }
//
//    fun drawLineText(canvas: Canvas, rect: RectF) {
//        if (drawLine) {
//            Paint().apply {
//                style = Paint.Style.STROKE
//                strokeWidth = 2f
//                color = Color.WHITE
//                canvas.drawRect(rect, this)
//            }
//            drawCircle(canvas, rect)
//        }
//    }
//
//    protected fun configIconMatrix(icon: BitmapStickerIcon, x: Float, y: Float, rotation: Float) {
//        icon.x = x
//        icon.y = y
//        icon.matrix.reset()
//        icon.matrix.postRotate(rotation, icon.width / 2, icon.height / 2)
//        icon.matrix.postTranslate(x - icon.width / 2, y - icon.height / 2)
//    }
//
//    override fun onInterceptHoverEvent(ev: MotionEvent?): Boolean {
//        when (ev!!.action) {
//            MotionEvent.ACTION_DOWN -> {
//                downX = ev.x
//                downY = ev.y
//            }
//        }
//
//        return super.onInterceptHoverEvent(ev)
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        val action = MotionEventCompat.getActionMasked(event)
//        if (!disableClick) {
//            when (action) {
//                MotionEvent.ACTION_DOWN -> {
//                    if (!onTouchDown(event!!)) {
//                        return false
//                    }
//                }
//
//                MotionEvent.ACTION_POINTER_DOWN -> {
//                    oldDistance = calculateDistance(event)
//                    oldRotation = calculateRotation(event)
//                    midPoint = calculateMidPoint(event)
//
//                    if (handlingSticker != null && isInStickerArea(
//                            handlingSticker,
//                            event?.getX(1),
//                            event?.getY(1)
//                        ) && findCurrentIconTouched() == null
//                    ) {
//                        currentMode = ActionMode.ZOOM_WITH_TWO_FINGER
//                    }
//                }
//
//                MotionEvent.ACTION_MOVE -> {
//                    handleCurrentMode(event!!)
//                    invalidate()
//                }
//
//                MotionEvent.ACTION_UP -> {
//                    this.allowShowLineVertical = false
//                    this.allowShowLineHorizon = false
//                    updatePositionSticker()
//                    onTouchUp(event!!)
//                }
//
//                MotionEvent.ACTION_POINTER_UP -> {
//                    if (currentMode == ActionMode.ZOOM_WITH_TWO_FINGER && handlingSticker != null) {
//                        onStickerOperationListener?.onStickerZoomFinished(handlingSticker)
//                    }
//                    currentMode = ActionMode.NONE
//                }
//            }
//        }
//        return true
//    }
//
//    protected fun onTouchDown(event: MotionEvent): Boolean {
//        currentMode = ActionMode.DRAG
//        isShowLine = true
//        downX = event.x
//        downY = event.y
//        midPoint = calculateMidPoint()
//
//        oldDistance = calculateDistance(midPoint.x, midPoint.y, downX, downY)
//        oldRotation = calculateRotation(midPoint.x, midPoint.y, downX, downY)
//
//        currentIcon = findCurrentIconTouched()
//        if (currentIcon != null) {
//            currentMode = ActionMode.ICON
//            currentIcon.onActionDown(this, event)
//        } else {
//            handlingSticker = findHandlingSticker()
//        }
//        if (handlingSticker != null) {
//            onStickerOperationListener.onStickerTouchedDown(handlingSticker)
//            downMatrix.set(handlingSticker.getMatrix())
//
//            mappingPoint = floatArrayOf(handlingSticker.getMappedBound().centerX())
//            downMatrix.mapPoints(mappingPoint)
//            if (bringToFrontCurrentSticker) {
//                stickers.remove(handlingSticker)
//                stickers.add(handlingSticker)
//            }
//
//        } else {
//            EventBus.getDefault().postSticky(StickerEvent(false))
//        }
//
//        stickerCheck()
//
//        if (currentIcon == null && handlingSticker == null) {
//            invalidate()
//            return false
//        }
//        invalidate()
//        return true
//    }
//
//    fun clearSelectedSticker() {
//        Log.d("LAM", "clearSelectedSticker: ")
//        handlingSticker = null
//    }
//
//    fun findLastSticker(): Sticker {
//        if (getStickers().isNotEmpty()) {
//            handlingSticker = getStickers().get(getStickers().size - 1)
//            return handlingSticker
//        }
//        return null
//    }
//
//    protected fun onTouchUp(event: MotionEvent) {
//        isShowLine = false
//        val currentTime = SystemClock.uptimeMillis()
//
//        if (currentMode == ActionMode.ICON && currentIcon != null && handlingSticker != null) {
//            currentIcon.onActionUp(this, event)
//        }
//
//        if (currentMode == ActionMode.DRAG && abs(event.x - downX) < touchSlop && abs(event.y - downY) < touchSlop && handlingSticker != null) {
//            currentMode = ActionMode.CLICK
//            onStickerOperationListener?.onStickerClicked(handlingSticker)
//            if (currentTime - lastClickTime < minClickDelayTime) {
//                onStickerOperationListener?.onStickerDoubleTapped(handlingSticker)
//            }
//            invalidate()
//        }
//
//        if (currentMode == ActionMode.DRAG && handlingSticker != null) {
//            if (onStickerOperationListener != null) {
//                onStickerOperationListener!!.onStickerDragFinished(handlingSticker)
//                invalidate()
//            }
//        }
//
//        currentMode = ActionMode.NONE
//        lastClickTime = currentTime;
//
//    }
//
//    protected fun handleCurrentMode(event: MotionEvent) {
//        when (currentMode) {
//            ActionMode.NONE -> {
//            }
//
//            ActionMode.CLICK -> {
//            }
//
//            ActionMode.DRAG -> {
//                if (handlingSticker != null) {
//                    moveMatrix.set(downMatrix)
//                    moveMatrix.postTranslate(event.x - downX, event.y - downY)
//                    handlingSticker.setMatrix(moveMatrix)
//                    if (constrained) {
//                        constrainSticker(handlingSticker)
//                    }
//                    caculateRangerLockSticker(handlingSticker)
//                }
//            }
//
//            ActionMode.ZOOM_WITH_TWO_FINGER -> {
//                if (handlingSticker != null) {
//                    var newDistance = calculateDistance(event)
//                    var newRotation = calculateRotation(event)
//                    if (newDistance <= width.toFloat() / 15f) {
//                        newDistance = width.toFloat() / 15f
//                        if (oldDistance <= width / 15f) {
//                            oldDistance = width.toFloat() / 15f
//                        }
//                    }
//                    moveMatrix.set(downMatrix)
//                    moveMatrix.postScale(
//                        newDistance / oldDistance,
//                        newDistance / oldDistance,
//                        midPoint.x,
//                        midPoint.y
//                    )
//                    moveMatrix.postRotate(newRotation - oldRotation, midPoint.x, midPoint.y)
//                    handlingSticker.matrix = moveMatrix
//                }
//            }
//
//            ActionMode.ICON -> {
//                if (handlingSticker != null && currentIcon != null) {
//                    currentIcon.onActionMove(this, event)
//                }
//            }
//        }
//    }
//
//    fun zoomAndRotateCurrentSticker(event: MotionEvent) {
//        zoomAndRotateSticker(handlingSticker, event)
//    }
//
//    fun zoomAndRotateSticker(sticker: Sticker?, event: MotionEvent) {
//        if (sticker != null) {
//            var newDistance = calculateDistance(midPoint.x, midPoint.y, event.x, event.y)
//            var newRotation = calculateRotation(midPoint.x, midPoint.y, event.x, event.y)
//            moveMatrix.set(downMatrix)
//
//            if (newDistance <= width.toFloat() / 15f) {
//                newDistance = width.toFloat() / 15f
//                if (oldDistance < width.toFloat() / 15f) {
//                    oldDistance = width.toFloat() / 15f
//                }
//            }
//            moveMatrix.postScale(
//                newDistance / oldDistance,
//                newDistance / oldDistance,
//                midPoint.x,
//                midPoint.y
//            )
//
//            moveMatrix.postRotate(newRotation - oldRotation, midPoint.x, midPoint.y)
//            handlingSticker.matrix = moveMatrix
//        }
//    }
//
//    fun setTextSizeUp(sticker: Sticker?) {
//        sticker?.matrix?.postScale(1f, 1.05f, x, y)
//    }
//
//    fun setTextSizeDown(sticker: Sticker?) {
//        sticker?.matrix?.postScale(1f, (100f / 105f), x, y)
//    }
//
//    fun rotateSticker(sticker: Sticker?, rotate: Float, scale: Float, point: PointF) {
//        if (sticker != null && sticker.rotate) {
//            val matrix: Matrix = sticker.matrix
//            matrix.reset()
//            matrix.postScale(scale, scale, sticker.centerPoint.x, sticker.centerPoint.y)
//            matrix.postRotate(rotate, sticker.centerPoint.x, sticker.centerPoint.y)
//            matrix.postTranslate(
//                point.x - sticker.width.toFloat() / 2f,
//                point.y - sticker.height.toFloat() / 2f
//            )
//            handlingSticker.matrix = matrix
//            invalidate()
//        }
//    }
//
//    fun constrainSticker(sticker: Sticker) {
//        var moveX = 0f
//        var moveY = 0f
//        val width = width.toFloat()
//        val height = height.toFloat()
//
//        if (sticker is TextSticker && (sticker as TextSticker).getCurve() != 0) {
//            sticker.getMappedCenterPointCurve(
//                currentCenterPoint,
//                point,
//                tmp,
//                (sticker as TextSticker).getTextRectCurve()
//            );
//        } else {
//            sticker.getMappedCenterPoint(currentCenterPoint, point, tmp);
//        }
//
//        if (currentCenterPoint.x < 0) {
//            moveX = -currentCenterPoint.x
//        }
//
//        if (currentCenterPoint.x > width) {
//            moveX = width - currentCenterPoint.x
//        }
//
//        if (currentCenterPoint.y < 0) {
//            moveY = -currentCenterPoint.y
//        }
//
//        if (currentCenterPoint.y > height) {
//            moveY = height - currentCenterPoint.y
//        }
//        sticker.matrix.postTranslate(moveX, moveY)
//    }
//
//    protected fun findCurrentIconTouched(): BitmapStickerIcon? {
//        for (icon in icons) {
//            val x = icon.x - downX
//            val y = icon.y - downY
//            val distance_pow_2 = x * x + y * y
//
//            val additionalRadius = resources.getDimension(R.dimen._12sdp)
//            val touchRadius = icon.iconRadius + additionalRadius
//
//            if (distance_pow_2 <= touchRadius * touchRadius) {
//                return icon
//            }
//        }
//        return null
//    }
//
//    protected fun findHandlingSticker(): Sticker? {
//        for (i in stickers.indices.reversed()) {
//            if (isInStickerArea(stickers[i], downX, downY)) {
//                oldSticker = currentSticker
//                currentSticker = i
//                return stickers[i]
//            }
//        }
//        return null
//    }
//
//    protected fun isInStickerArea(sticker: Sticker, downX: Float, downY: Float): Boolean {
//        tmp[0] = downX
//        tmp[1] = downY
//        return sticker.contains(tmp)
//    }
//
//    protected fun calculateMidPoint(event: MotionEvent?): PointF {
//        if (event == null || event.pointerCount < 2) {
//            midPoint.set(0f, 0f)
//            return midPoint
//        }
//        val x = (event.getX(0) + event.getX(1)) / 2
//        val y = (event.getY(0) + event.getY(1)) / 2
//        midPoint.set(x, y)
//        return midPoint
//    }
//
//    protected fun calculateMidPoint(): PointF {
//        if (handlingSticker == null) {
//            midPoint.set(0f, 0f)
//            return midPoint
//        }
//        if (handlingSticker is TextSticker && ((handlingSticker as TextSticker).curve !== 0)) {
//            handlingSticker.getMappedCenterPointCurve(
//                midPoint,
//                point,
//                tmp,
//                (handlingSticker as TextSticker).textRectCurve
//            )
//        } else {
//            handlingSticker.getMappedCenterPoint(midPoint, point, tmp)
//        }
//        return midPoint
//    }
//
//    protected fun calculateRotation(event: MotionEvent?): Float {
//        if (event == null || event.pointerCount < 2) {
//            return 0f
//        }
//        return calculateRotation(
//            event.getX(0),
//            event.getY(0),
//            event.getX(1),
//            event.getY(1)
//        )
//    }
//
//    protected fun calculateRotation(x1: Float, y1: Float, x2: Float, y2: Float): Float {
//        val x = x1 - x2
//        val y = y1 - y2
//        val radians = atan2(y.toDouble(), x.toDouble())
//        return toDegrees(radians).toFloat()
//    }
//
//    protected fun calculateDistance(event: MotionEvent?): Float {
//        if (event == null || event.pointerCount < 2) {
//            return 0f
//        }
//        return calculateDistance(event.getX(0), event.getY(0), event.getX(1), event.getY(1))
//    }
//
//    protected fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
//        val dx = x2 - x1
//        val dy = y2 - y1
//        return sqrt((dx * dx + dy * dy).toDouble()).toFloat()
//    }
//
//    protected override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
//        super.onSizeChanged(w, h, oldW, oldH)
//        onStickerOperationListener?.onSizeChanged(w, h, oldW, oldH)
//    }
//
//    fun setSize(w: Int, h: Int) {
//        onSizeChanged(w, h, w, h)
//    }
//
//    fun gava(sticker: Sticker?) {
//        if (sticker == null) {
//            return
//        }
//
//        val width = width
//        val height = height
//        val stickerWidth = sticker.width
//        val stickerHeight = sticker.height
//        val offsetX = ((width - stickerWidth) / 2).toFloat()
//        val offsetY = ((height - stickerHeight) / 2).toFloat()
//
//        sizeMatrix.postTranslate(offsetX, offsetY)
//        invalidate()
//    }
//
//    protected fun transformSticker(sticker: Sticker?) {
//        if (sticker == null) {
//            return
//        }
//
//        sizeMatrix.reset()
//
//        val width = width
//        val height = height
//        val stickerWidth = sticker.width
//        val stickerHeight = sticker.height
//        val offsetX = ((width - stickerWidth) / 2).toFloat()
//        val offsetY = ((height - stickerHeight) / 2).toFloat()
//
//        sizeMatrix.postTranslate(offsetX, offsetY)
//
//        var scaleFactor = 0f
//        if (width < height) {
//            scaleFactor = width / stickerWidth
//        } else {
//            scaleFactor = height / stickerHeight
//        }
//
//        sizeMatrix.postScale(scaleFactor / 2f, scaleFactor / 2f, width / 2f, height / 2f)
//
//        sticker.matrix.reset()
//        sticker.matrix = sizeMatrix
//
//        invalidate()
//    }
//
//    fun flipCurrentSticker(direction: Int) {
//        flip(handlingSticker, direction)
//    }
//
//    fun flip(sticker: Sticker?, direction: Int) {
//        if (sticker != null) {
//            sticker.getCenterPoint(midPoint)
//            if ((direction and FLIP_HORIZONTALLY) != 0) {
//                sticker.matrix.preScale(-1, 1, midPoint.x, midPoint.y)
//                sticker.flippedHorizontally = !sticker.isFlippedHorizontally()
//            }
//            if ((direction and FLIP_VERTICALLY) != 0) {
//                sticker.getMatrix().preScale(1, -1, midPoint.x, midPoint.y)
//                sticker.flippedVertically = !sticker.isFlippedVertically()
//            }
//
//            onStickerOperationListener?.onStickerFlipped(sticker)
//
//            invalidate()
//        }
//    }
//
//    fun replace(sticker: Sticker?): Boolean {
//        return replace(sticker, false)
//    }
//
//    fun replace(sticker: Sticker?, needStayState: Boolean): Boolean {
//        if (handlingSticker != null && sticker != null) {
//            val with = width
//            val height = height
//            if (needStayState) {
//                sticker.matrix = handlingSticker.matrix
//                sticker.flippedVertically = handlingSticker.isFlippedVertically()
//                sticker.flippedHorizontally = handlingSticker.isFlippedHorizontally()
//            } else {
//                handlingSticker.matrix.reset()
//                val offsetX = ((with - handlingSticker.width) / 2f).toFloat()
//                val offsetY = ((height - handlingSticker.height) / 2f).toFloat()
//                sticker.matrix.postTranslate(offsetX, offsetY)
//                val scaleFactor: Float = if (with < height) {
//                    width / handlingSticker.drawable.intrinsicWidth
//                } else {
//                    height / handlingSticker.drawable.intrinsicHeight
//                }
//                sticker.matrix.postScale(
//                    scaleFactor / 2f,
//                    scaleFactor / 2f,
//                    width / 2f,
//                    height / 2f
//                )
//            }
//            val index = stickers.indexOf(handlingSticker)
//            stickers.set(index, sticker)
//            handlingSticker = sticker
//
//            invalidate()
//            return true
//        } else {
//            return false
//        }
//    }
//
//    fun remove(sticker: Sticker?): Boolean {
//        if (stickers.contains(sticker)) {
//            stickers.remove(sticker)
//            onStickerOperationListener?.onStickerDeleted(sticker)
//            if (handlingSticker == sticker) {
//                handlingSticker = null
//            }
//            invalidate()
//
//            return true
//        } else {
//            return false
//        }
//    }
//
//    fun removeCurrentSticker(): Boolean {
//        return remove(handlingSticker)
//    }
//
//    fun removeAllStickers() {
//        stickers.clear()
//        if (handlingSticker != null) {
//            handlingSticker.release()
//            handlingSticker = null
//        }
//        invalidate()
//    }
//
//    fun addSticker(sticker: Sticker): StickerView {
//        addSticker(sticker, Sticker.Position.CENTER)
//    }
//
//    fun addSticker(sticker: Sticker, position: Sticker.Position): StickerView {
//        Log.d("LAM", "addSticker: ")
//        post { addStickerImmediately(sticker, position) }
//        return this
//    }
//
//    protected fun addStickerImmediately(sticker: Sticker, position: Sticker.Position) {
//        setStickerPosition(sticker, position)
//        var scaleFactor = 0f
//        var widthScaleFactor = 0f
//        var heightScaleFactor = 0f
//        widthScaleFactor = width.toFloat() / sticker.drawable.intrinsicWidth
//        heightScaleFactor = height.toFloat() / sticker.drawable.intrinsicHeight
//        scaleFactor =
//            if (widthScaleFactor > heightScaleFactor) heightScaleFactor else widthScaleFactor
//
//        if (sticker is DrawableSticker) {
//            sticker.matrix.postScale(scaleFactor / 4, scaleFactor / 4, width / 2, height / 2)
//        } else if (sticker is BitmapSticker) {
//            val displayMetrics = DisplayMetrics()
//            (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
//            val targetWidth = displayMetrics.widthPixels / 3f
//            val originalWidth: Float = sticker.width
//            val scale = targetWidth / originalWidth
//            sticker.matrix.postScale(scale, scale, width / 2, height) / 2)
//        } else {
//            sticker.matrix.postScale(scaleFactor / 2, scaleFactor / 2, width / 2, height) / 2)
//        }
//
//        handlingSticker = sticker
//        stickers.add(sticker)
//        if (currentSticker < stickers.size) {
//            stickers[currentSticker].selected = false
//        } else {
//            stickers[stickers.size - 1].selected = false
//        }
//        onStickerOperationListener?.onsStickerAdded(sticker)
//        updatePositionSticker()
//
//        invalidate()
//    }
//
//    private fun updatePositionSticker() {
//        try {
//            if (handlingSticker is TextSticker && (handlingSticker as TextSticker).getCurve() !== 0) {
//                handlingSticker.getMappedCenterPointCurve(
//                    currentCenterPoint,
//                    point,
//                    tmp,
//                    (handlingSticker as TextSticker).getTextRectCurve()
//                )
//                x =
//                    handlingSticker.getMappedCenterPointCurve((handlingSticker as TextSticker).getTextRectCurve()).x
//                y =
//                    handlingSticker.getMappedCenterPointCurve((handlingSticker as TextSticker).getTextRectCurve()).y
//            } else {
//                x = sticker.mappedCenterPoint.x
//                y = sticker.mappedCenterPoint.y
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    fun setStickerPosition(sticker: Sticker, position: Sticker.Position) {
//        val width = width.toFloat()
//        val height = height.toFloat()
//        var offsetX: Float = width - sticker.width
//        var offsetY: Float = height - sticker.height
//        if ((position and Sticker.Position.TOP) > 0) {
//            offsetY /= 4f
//        } else if ((position and Sticker.Position.BOTTOM) > 0) {
//            offsetY *= 3f / 4f
//        } else {
//            offsetY /= 2f
//        }
//        if ((position and Sticker.Position.LEFT) > 0) {
//            offsetX /= 4f
//        } else if ((position and Sticker.Position.RIGHT) > 0) {
//            offsetX *= 3f / 4f
//        } else {
//            offsetX /= 2f
//        }
//
//        sticker.matrix.postTranslate(offsetX, offsetY)
//    }
//
//    fun getStickerPoints(sticker: Sticker?, dst: FloatArray) {
//        if (sticker == null) {
//            dst.fill(0f)
//            return
//        }
//        sticker.getBoundPoints(bounds)
//        sticker.getMappedPoints(dst, bounds)
//    }
//
//    fun getStickerPoints2(sticker: Sticker?, dst: FloatArray) {
//        if (sticker == null) {
//            dst.fill(0f)
//            return
//        }
//        (sticker as TextSticker).getBoundPointsCurve(bounds)
//        sticker.getMappedPoints(dst, bounds)
//    }
//
//    @RequiresPermission(Manifest.permission.SET_WALLPAPER)
//    fun save(
//        context: Context,
//        file: File,
//        setWallpaper: Boolean,
//        listener: StickerUtils.ProgressListener
//    ): String {
//        try {
//            val path = StickerUtils.saveImageToGallery(context, file, createBitmap(), listener)
//            if (setWallpaper && HawkHelper.isWallPaperAuto()) {
//                val bitmap = BitmapFactory.decodeFile(file.path, BitmapFactory.Options())
//                val wallpaperManager = WallpaperManager.getInstance(context.applicationContext)
//                try {
//                    wallpaperManager.setBitmap(bitmap)
//                } catch (e: IOException) {
//                }
//            }
//            return path
//        } catch (ignored: IllegalArgumentException) {
//            return ""
//        }
//    }
//
//    fun saveImage(context: Context, folderName: String): String {
//        return StickerUtils.saveImageToExternalFileDir(context, createBitmapForSave(), folderName)
//    }
//
//    fun createBitmap(): Bitmap {
//        if (isDrawSticker()) {
//            handlingSticker = null
//        }
//        if (currentSticker < stickers.size) {
//            stickers[currentSticker].selected = false
//        }
//        var bitmap: Bitmap
//        try {
//            bitmap = createBitmap(width, height)
//            val canvas = Canvas(bitmap)
//            this.draw(canvas)
//        } catch (error: OutOfMemoryError) {
//            AnalyticsManager.getInstance().trackEvent(Event("OUTOFMEMORY_CREATEBITMAP", Bundle()))
//            val height = 300 * width / height
//            bitmap = createBitmap(300, height)
//            Canvas(bitmap).also { this.draw(it) }
//        } catch (error: Exception) {
//            bitmap = createBitmap(300, 300)
//            Canvas(bitmap).apply {
//                Paint().apply {
//                    color = Color.BLACK
//                    drawRect(0f, 0f, 300f, 300f, this)
//                }
//            }
//        }
//        return bitmap
//    }
//
//    fun createBitmapForSave(): Bitmap? {
//        if (isDrawSticker()) {
//            handlingSticker = null
//        }
//        if (currentSticker < stickers.size) {
//            stickers[currentSticker].selected = false
//        }
//        var bitmap: Bitmap
//        try {
//            bitmap = createBitmap(width, height)
//            val canvas = Canvas(bitmap)
//            this.draw(canvas)
//        } catch (error: OutOfMemoryError) {
//            AnalyticsManager.getInstance().trackEvent(Event("OUTOFMEMORY_CREATEBITMAP", Bundle()))
//            return null
//        } catch (error: Exception) {
//            return null
//        }
//        return bitmap
//    }
//
//    fun getStickerCount(): Int {
//        return stickers.size
//    }
//
//    fun isNoneSticker(): Boolean {
//        return stickers.isEmpty()
//    }
//
//    fun isLocked(): Boolean {
//        return locked
//    }
//
//    fun isDisableClick(): Boolean {
//        return disableClick
//    }
//
//    fun setDisableClick(disableClick: Boolean) {
//        this.disableClick = disableClick
//    }
//
//    fun setLocked(locked: Boolean): StickerView {
//        this.locked = locked
//        invalidate()
//        return this
//    }
//
//    fun setMinClickDelayTime(minClickDelayTime: Int): StickerView {
//        this.minClickDelayTime = minClickDelayTime
//        return this
//    }
//
//    fun getMinClickDelayTime(): Int {
//        return minClickDelayTime
//    }
//
//    fun isConstrained(): Boolean {
//        return constrained
//    }
//
//    fun setConstrained(constrained: Boolean): StickerView {
//        this.constrained = constrained
//        postInvalidate()
//        return this
//    }
//
//    fun setOnStickerOperationListener(listener: OnStickerOperationListener?): StickerView {
//        this.onStickerOperationListener = listener
//        return this
//    }
//
//    fun getOnStickerOperationListener(): OnStickerOperationListener? {
//        return onStickerOperationListener
//    }
//
//    fun getCurrentSticker(): Sticker? {
//        return handlingSticker
//    }
//
//    fun getIcons(): List<BitmapStickerIcon> {
//        return icons
//    }
//
//    fun setIcons(icons: List<BitmapStickerIcon>) {
//        this.icons.clear()
//        Log.d("LAM", "setIcons: ${icons.size}")
//        this.icons.addAll(icons)
//        invalidate()
//    }
//
//    private fun stickerCheck() {
//        if (handlingSticker != null) {
//            if (currentSticker < stickers.size) {
//                stickers[currentSticker].selected = true
//            }
//            if (oldSticker != currentSticker && oldSticker < stickers.size) {
//                stickers[oldSticker].selected = false
//            }
//        } else {
//            if (currentSticker < stickers.size) {
//                stickers[currentSticker].selected = false
//            }
//        }
//    }
//
//    interface OnStickerOperationListener {
//        fun onStickerAdded(sticker: Sticker)
//        fun onStickerClicked(sticker: Sticker)
//        fun onStickerDeleted(sticker: Sticker)
//        fun onStickerDragFinished(sticker: Sticker)
//        fun onStickerTouchedDown(sticker: Sticker)
//        fun onStickerZoomFinished(sticker: Sticker)
//        fun onStickerFlipped(sticker: Sticker)
//        fun onStickerDoubleTapped(sticker: Sticker)
//        fun onSizeChange(w: Int, h: Int, oldW: Int, oldH: Int)
//    }
//
//    fun caculateRangerLockSticker(sticker: Sticker) {
//        if (sticker is TextSticker && sticker.getCurve() != 0f) {
//            sticker.getMappedCenterPointCurve(
//                currentCenterPoint,
//                point,
//                tmp,
//                sticker.getTextRectCurve()
//            )
//        } else {
//            sticker.getMappedCenterPoint(currentCenterPoint, point, tmp)
//        }
//
//        val dx = when {
//            currentCenterPoint.x <= width / 2f - 15f || currentCenterPoint.x >= width / 2f + 15f -> {
//                allowShowLineHorizon = false
//                0f
//            }
//            else -> {
//                allowShowLineHorizon = true
//                width / 2f - currentCenterPoint.x
//            }
//        }
//
//        val dy = when {
//            currentCenterPoint.y <= height / 2f - 15f || currentCenterPoint.y >= height / 2f + 15f -> {
//                allowShowLineVertical = false
//                0f
//            }
//            else -> {
//                allowShowLineVertical = true
//                height / 2f - currentCenterPoint.y
//            }
//        }
//
//        sticker.matrix.postTranslate(dx, dy)
//    }
//
//}