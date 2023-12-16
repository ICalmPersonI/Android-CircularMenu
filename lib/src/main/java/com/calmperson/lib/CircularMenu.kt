package com.calmperson.lib

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.calmperson.lib.animation.Animator
import kotlin.math.min


class CircularMenu : View {

    companion object {
        private val TAG = CircularMenu::class.simpleName
        private const val DESIRED_HEIGHT = 100
        private const val DESIRED_WIDTH = 100
    }

    private val drawer = Drawer(this)
    private val animator = Animator(this, drawer).apply {
        animationEventListener = object : AnimationEventListener {
            override fun onOpenMenuAnimationStart() {}
            override fun onOpenMenuAnimationEnd() {}
            override fun onOpenMenuAnimationCancel() {}
            override fun onCloseMenuAnimationStart() {}
            //override fun onCloseMenuAnimationEnd() { removeThisViewFromParent() }
            override fun onCloseMenuAnimationEnd() { }
            override fun onCloseMenuAnimationCancel() {}
        }
    }
    private val pointCalculator = PointCalculator(drawer)

    private var onCenterButtonClick: (CircularMenu) -> Unit = { _ -> animator.startCloseAnimation() }
    private var onSectorClick: (sector: Sector, sectorIndex: Int) -> Unit = { _, _ -> }

    internal var numberOfSectors = 0
    internal var sectors = emptyArray<Sector>()
    private var colors: List<Int> = listOf(Drawer.DEFAULT_COLOR)

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        processAttributes(attrs)
    }

    constructor(
        context: Context?, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        processAttributes(attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        processAttributes(attrs)
    }

    constructor(
        context: Context?,
        @DrawableRes icons: List<Int>,
        @ColorRes colors: List<Int> = listOf(Drawer.DEFAULT_COLOR),
        @DrawableRes centerButtonIconId: Int = Drawer.DEFAULT_CENTER_BUTTON_ICON_RES_ID,
        @ColorRes centerButtonColorId: Int = Drawer.DEFAULT_COLOR,
        buttonSpacing: Float = Drawer.DEFAULT_BUTTON_SPACING,
        centerButtonPadding: Float = Drawer.DEFAULT_CENTER_BUTTON_PADDING
    ) : super(context) {
        setColors(colors)
        initSectors(icons)
        setCenterButtonIconId(centerButtonIconId)
        setButtonSpacing(buttonSpacing)
        setCenterButtonPadding(centerButtonPadding)
        setCenterButtonColor(centerButtonColorId)
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    fun startOpenAnimation() {
        animator.startOpenAnimation()
    }

    fun startCloseAnimation() {
        animator.startCloseAnimation()
    }

    fun setColors(@ColorRes colors: List<Int>) {
        this.colors = colors
    }

    fun setCenterButtonIconId(@DrawableRes centerButtonIconId: Int) {
        drawer.centerButtonIconResId = centerButtonIconId
        invalidate()
    }

    fun setCenterButtonColor(@ColorRes centerButtonColorId: Int) {
        try {
            drawer.centerButtonColor =
                context?.getColor(centerButtonColorId) ?: Drawer.DEFAULT_COLOR
        } catch (e: NotFoundException) {
            Log.e(TAG, "Error: ", e)
        }
        invalidate()
    }

    fun setButtonSpacing(buttonSpacing: Float) {
        drawer.buttonSpacing = buttonSpacing
        invalidate()
    }

    fun setCenterButtonPadding(centerButtonPadding: Float) {
        drawer.centerButtonPadding = centerButtonPadding
        invalidate()
    }

    fun setAnimationEventListener(listener: AnimationEventListener) {
        animator.animationEventListener = listener
    }

    fun setOnCenterButtonClickListener(listener: (CircularMenu) -> Unit) {
        onCenterButtonClick = listener
    }

    fun setOnSectorClickListener(listener: (Sector, sectorIndex: Int) -> Unit) {
        onSectorClick = listener
    }

    private fun processAttributes(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        try {
            typedArray = context?.obtainStyledAttributes(attrs, R.styleable.CircularMenu)

            val icons: List<Int> = typedArray?.obtainResourcesArray(
                R.styleable.CircularMenu_circleMenu_icons
            ) ?: emptyList()
            val colors: List<Int> = typedArray?.obtainResourcesArray(
                R.styleable.CircularMenu_circleMenu_colors
            ) ?: emptyList()

            typedArray?.getResourceId(
                R.styleable.CircularMenu_circleMenu_centerButtonIcon, 0
            )?.let { id ->
                if (id != 0) setCenterButtonIconId(id)
                else Log.e(TAG, "Resource not found")
            }

            typedArray?.getResourceId(
                R.styleable.CircularMenu_circleMenu_centerButtonColor, 0
            )?.let { id ->
                if (id != 0) setCenterButtonColor(id)
                else Log.e(TAG, "Resource not found")
            }

            typedArray?.getDimension(
                R.styleable.CircularMenu_circleMenu_buttonSpacing,
                Drawer.DEFAULT_BUTTON_SPACING
            )?.let { value -> setButtonSpacing(value) }

            typedArray?.getDimension(
                R.styleable.CircularMenu_circleMenu_centerButtonPadding,
                Drawer.DEFAULT_CENTER_BUTTON_PADDING
            )?.let { value -> setCenterButtonPadding(value) }

            setColors(colors)
            initSectors(icons)
        } catch (e: Exception) {
            Log.e(TAG, "Error: ", e)
        } finally {
            typedArray?.recycle()
        }
    }

    private fun TypedArray.obtainResourcesArray(resId: Int): List<Int> {
        val mutableList = mutableListOf<Int>()
        val arrayId = this.getResourceId(resId, 0)
        if (arrayId == 0) return emptyList()

        var iconsIds: TypedArray? = null
        try {
            iconsIds = resources.obtainTypedArray(arrayId)
            for (i in 0 until iconsIds.length()) {
                val resID = iconsIds.getResourceId(i, 0)
                if (resID != 0) mutableList.add(resID)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error: ", e)
        } finally {
            iconsIds?.recycle()
        }
        return mutableList
    }

    private fun initSectors(icons: List<Int>) {
        if (icons.isEmpty()) throw IllegalArgumentException("No buttons icons.")

        numberOfSectors = icons.size
        val cas = colors.size

        sectors = Array(numberOfSectors) { i ->
            val sizeOfSector = 360 / numberOfSectors
            val color = if (cas != 0) context.getColor(colors[i % cas]) else Drawer.DEFAULT_COLOR
            Sector(
                rectF = RectF(),
                startAngle = sizeOfSector * i,
                endAngle = sizeOfSector * (i + 1),
                icon = icons[i],
                defaultColor = color,
                currentColor = color,
                startCloseAnimationAfterClick = true,
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(DESIRED_WIDTH, widthSize)
            else -> DESIRED_WIDTH
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(DESIRED_HEIGHT, heightSize)
            else -> DESIRED_HEIGHT
        }

        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        with(drawer) {
            centerX = w / 2f
            centerY = h / 2f

            val padding = min(paddingLeft, min(paddingTop, min(paddingRight, paddingBottom)))
            outerRadius = min(w, h) / 2f - padding
            innerRadius = outerRadius / 2f

            outerRectFLeft = centerX - outerRadius
            outerRectFTop = centerY - outerRadius
            outerRectFRight = centerX + outerRadius
            outerRectFBottom = centerY + outerRadius
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        with(canvas) {
            with(drawer) {
                drawSectors()
                drawSectorSpacers()
                clearCenter()
                drawCenterButton()
                drawIcons()
            }
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        restoreOriginalButtonsState()

        val x = event?.x
        val y = event?.y
        val action = event?.action

        if (x == null || y == null || action == null || !isClickable) return false

        val isCenterButtonClick = pointCalculator.isPointInCenterButton(x, y)
        val sectorIndex = if (!isCenterButtonClick) getClickedSectorId(x, y) else null

        if (!isCenterButtonClick && sectorIndex == null) return false

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                if (isCenterButtonClick) drawer.centerButtonColor = Drawer.PRESSED_BUTTON_COLOR
                sectorIndex?.let { i -> sectors[i].currentColor = Drawer.PRESSED_BUTTON_COLOR }
                invalidate()
                return true
            }

            MotionEvent.ACTION_UP -> {
                if (isCenterButtonClick) onCenterButtonClick(this)
                sectorIndex?.let { i ->
                    val sector = sectors[i]
                    onSectorClick(sector, i)
                    if (sector.startCloseAnimationAfterClick) animator.startCloseAnimation()
                }
                performClick()
                invalidate()
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                if (isCenterButtonClick) drawer.centerButtonColor = Drawer.PRESSED_BUTTON_COLOR
                sectorIndex?.let { i -> sectors[i].currentColor = Drawer.PRESSED_BUTTON_COLOR }
                invalidate()
                return true
            }

            else -> return super.onTouchEvent(event)
        }
    }

    private fun getClickedSectorId(x: Float, y: Float): Int? {
        for (i in sectors.indices) {
            val sector = sectors[i]
            if (pointCalculator.isPontInSector(x, y, sector.startAngle, sector.endAngle)) {
                return i
            }
        }
        return null
    }

    private fun restoreOriginalButtonsState() {
        drawer.centerButtonColor = drawer.centerButtonColorConst
        for (sector in sectors) sector.currentColor = sector.defaultColor
        invalidate()
    }

    private fun removeThisViewFromParent() {
        with(parent as ViewGroup) {
            removeView(this@CircularMenu)
            requestLayout()
            invalidate()
        }
    }

    interface AnimationEventListener {
        fun onOpenMenuAnimationStart()
        fun onOpenMenuAnimationEnd()
        fun onOpenMenuAnimationCancel()
        fun onCloseMenuAnimationStart()
        fun onCloseMenuAnimationEnd()
        fun onCloseMenuAnimationCancel()
    }

}