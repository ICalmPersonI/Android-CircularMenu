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
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.calmperson.lib.Utils.pixelsToDp
import com.calmperson.lib.animation.Animator
import com.calmperson.lib.circularmenu.R
import com.calmperson.lib.model.AnimationProperties
import com.calmperson.lib.model.Sector
import kotlin.math.min


open class CircularMenu : View {

    companion object {
        private val TAG = CircularMenu::class.simpleName
        private const val DESIRED_HEIGHT = 100
        private const val DESIRED_WIDTH = 100
    }

    internal val drawer = Drawer(this)
    private val animator = Animator(
        geometricProperties = drawer.geometricProperties,
        animationProperties = drawer.animationProperties,
        setClickable = { value -> isClickable = value},
        invalidate = { invalidate() },
        getNumberOfSectors = { _sectors.size }
    )
    private val pointCalculator = PointCalculator()

    open var onCenterButtonClick: (CircularMenu) -> Unit = { _ -> startCloseAnimation() }
    open var onSectorClick: (CircularMenu, sectorIndex: Int) -> Unit = { _, _ -> }

    private var _sectors = emptyArray<Sector>()
    internal var colors: IntArray = intArrayOf(Drawer.DEFAULT_COLOR)

    private var lastPressedSectorIndex: Int? = null
    private var isCenterButtonPressed: Boolean = false

    /**
     * A set of properties for animating a circular menu.
     */
    val animationProperties: AnimationProperties
        get() = drawer.animationProperties

    val centerButtonCurrentColor: Int
        get() = drawer.centerButtonCurrentColor

    val centerButtonColor: Int
        get() = drawer.centerButtonColor

    val centerButtonPressedColor: Int
        get() = drawer.centerButtonPressedColor

    val sectors: Array<Sector>
        get() = _sectors

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
        @DrawableRes icons: IntArray,
        @DrawableRes centerButtonIconId: Int = Drawer.DEFAULT_CENTER_BUTTON_ICON_RES_ID,
        colors: IntArray = intArrayOf(Drawer.DEFAULT_COLOR),
        sectorsPressedColor: Int = Drawer.DEFAULT_PRESSED_BUTTON_COLOR,
        centerButtonColor: Int = Drawer.DEFAULT_COLOR,
        centerButtonPressedColor: Int = Drawer.DEFAULT_PRESSED_BUTTON_COLOR,
        buttonSpacingDp: Float = Drawer.DEFAULT_BUTTON_SPACING,
        centerButtonPaddingDp: Float = Drawer.DEFAULT_CENTER_BUTTON_PADDING
    ) : super(context) {
        setColors(colors, false)
        setIcons(icons, false)
        setSectorsPressedColor(sectorsPressedColor)
        setCenterButtonIconId(centerButtonIconId)
        setCenterButtonColor(centerButtonColor)
        setCenterButtonPressedColor(centerButtonPressedColor)
        setSectorSpacing(buttonSpacingDp)
        setCenterButtonPadding(centerButtonPaddingDp)
    }

    constructor(
        context: Context?,
        @ArrayRes iconsArrayId: Int,
        @ArrayRes colorsArrayId: Int = Drawer.DEFAULT_COLORS_RES,
        @ColorRes sectorsPressedColorId: Int = Drawer.DEFAULT_PRESSED_BUTTON_COLOR_RES,
        @DrawableRes centerButtonIconId: Int = Drawer.DEFAULT_CENTER_BUTTON_ICON_RES_ID,
        @ColorRes centerButtonColorId: Int = Drawer.DEFAULT_COLOR_RES,
        @ColorRes centerButtonPressedColorId: Int = Drawer.DEFAULT_PRESSED_BUTTON_COLOR_RES,
        buttonSpacingDp: Float = Drawer.DEFAULT_BUTTON_SPACING,
        centerButtonPaddingDp: Float = Drawer.DEFAULT_CENTER_BUTTON_PADDING
    ) : super(context) {
        setColors(obtainColors(colorsArrayId) ?: intArrayOf(Drawer.DEFAULT_COLOR), false)
        setIcons(obtainIconsIds(iconsArrayId) ?: intArrayOf(), false)
        setSectorsPressedColorById(sectorsPressedColorId)
        setCenterButtonIconId(centerButtonIconId)
        setSectorSpacing(buttonSpacingDp)
        setCenterButtonPadding(centerButtonPaddingDp)
        setCenterButtonColorById(centerButtonColorId)
        setCenterButtonPressedColorById(centerButtonPressedColorId)
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    /**
     * Initiates the opening animation and, according to its lifecycle,
     * triggers the corresponding method in the `AnimationEventListener`.
     */
    fun startOpenAnimation() {
        animator.startOpenAnimation()
    }

    /**
     * Initiates the closing animation and, according to its lifecycle,
     * triggers the corresponding method in the `AnimationEventListener`.
     */
    fun startCloseAnimation() {
        animator.startCloseAnimation()
    }

    /**
     * Initiates the press animation of a specific sector (sectorIndex),
     * according to its lifecycle, triggers the corresponding method in the `AnimationEventListener`.
     * @param sectorIndex index of the sector that will be animated.
     */
    fun startPressSectorAnimation(sectorIndex: Int) {
        animator.startPressSectorAnimation(sectorIndex)
    }

    /**
     * Initiates the release animation of a specific sector (sectorIndex),
     * according to its lifecycle, triggers the corresponding method in the `AnimationEventListener`.
     * @param sectorIndex index of the sector that will be animated.
     */
    fun startReleaseSectorAnimation(sectorIndex: Int) {
        animator.startReleaseSectorAnimation(sectorIndex)
    }

    /**
     * Initiates the press animation of the central button,
     * according to its lifecycle, triggers the corresponding method in the `AnimationEventListener`
     */
    fun startPressCenterButtonAnimation() {
        animator.startPressCenterButtonAnimation()
    }

    /**
     * Initiates the release animation of the central button,
     * according to its lifecycle, triggers the corresponding method in the `AnimationEventListener`
     */
    fun startReleaseCenterButtonAnimation() {
        animator.startReleaseCenterButtonAnimation()
    }

    /**
     * Initiates the collapse animation of all sectors,
     * after it ended initiates the expand animation of all sectors.
     * Using it for changing icons and colors is not recommended;
     * for this purpose, there are `setIcons` and `setColors` functions.
     * @param doWhenSectorsCollapsed action to be invoked when all sectors have collapsed,
     * and their content is no longer visible. At this moment, data can be altered
     */
    fun startChangeSectorsAnimation(doWhenSectorsCollapsed: () -> Unit) {
        animator.startChangeSectorsAnimation(
            onCollapseSectorsAnimationEnd = doWhenSectorsCollapsed
        )
    }

    /**
     * Sets the colors of the sectors.
     * When creating sectors,
     * it retrieves values from the colors array using the formula `colorIndex = sectorIndex % size`.
     * The number of colors doesn't necessarily have to match the number of sectors.
     * If the array is empty, it sets a default array with a single color.
     * @param colors array of integer color values.
     * @param useChangeSectorsAnimation determines whether to trigger the sector change animation;
     * the animation calls corresponding methods in AnimationEventListener.
     */
    fun setColors(colors: IntArray, useChangeSectorsAnimation: Boolean) {
        this.colors = if (colors.isNotEmpty()) colors else intArrayOf(Drawer.DEFAULT_COLOR)
        val size = this.colors.size
        val changeColors = {
            _sectors.forEachIndexed { index, sector ->
                val color = this.colors[index % size]
                sector.color = color
                sector.currentColor = color
            }
        }
        if (useChangeSectorsAnimation) {
            animator.startChangeSectorsAnimation(
                onCollapseSectorsAnimationEnd = { changeColors() }
            )
        } else {
            changeColors()
            invalidate()
        }
    }

    /**
     * Sets the colors of the sectors.
     * When creating sectors,
     * it retrieves values from the colors array using the formula `colorIndex = sectorIndex % size`.
     * The number of colors doesn't necessarily have to match the number of sectors.
     * If the array is empty, it sets a default array with a single color.
     * @param colorsArrayId array res id with colors.
     * @param useChangeSectorsAnimation determines whether to trigger the sector change animation;
     * the animation calls corresponding methods in AnimationEventListener.
     */
    fun setColorsById(@ArrayRes colorsArrayId: Int, useChangeSectorsAnimation: Boolean) {
        val colors = obtainColors(colorsArrayId) ?: intArrayOf(Drawer.DEFAULT_COLOR)
        setColors(colors, useChangeSectorsAnimation)
    }

    /**
     * Sets new icons for the sectors, creating new instances of the sector.
     * You can set from 2 to 10 sectors (icons).
     * @param iconsIds an array of Drawable resources. For proper display, these should be vector images.
     * @param useChangeSectorsAnimation determines whether to trigger the sector change animation;
     * the animation calls corresponding methods in AnimationEventListener.
     */
    fun setIcons(iconsIds: IntArray, useChangeSectorsAnimation: Boolean) {
        if (iconsIds.isEmpty() || iconsIds.size !in 2..10) {
            throw IllegalArgumentException("You can set from 2 to 10 icons.")
        }
        if (useChangeSectorsAnimation) {
            animator.startChangeSectorsAnimation(
                onCollapseSectorsAnimationEnd = { initSectors(iconsIds) }
            )
        } else {
            initSectors(iconsIds)
            invalidate()
        }
    }

    /**
     * Sets new icons for the sectors, creating new instances of the sector.
     * You can set from 2 to 10 sectors (icons).
     * @param iconsArrayId array res id with icons ids.
     * @param useChangeSectorsAnimation determines whether to trigger the sector change animation;
     * the animation calls corresponding methods in AnimationEventListener.
     */
    fun setIconsById(@ArrayRes iconsArrayId: Int, useChangeSectorsAnimation: Boolean) {
        val iconsIds = obtainIconsIds(iconsArrayId) ?: intArrayOf()
        setIcons(iconsIds, useChangeSectorsAnimation)
    }

    /**
     * Sets the color of the sectors, which changes when the button is pressed.
     * @param color integer value of the color.
     */
    fun setSectorsPressedColor(color: Int) {
        drawer.sectorPressedColor = color
        _sectors.forEach { sector -> sector.pressedColor = color }
        invalidate()
    }

    /**
     * Sets the color of the sectors, which changes when the button is pressed.
     * @param colorId color resource id.
     */
    fun setSectorsPressedColorById(@ColorRes colorId: Int) {
        try {
            val color = context.getColor(colorId)
            drawer.sectorPressedColor = color
            _sectors.forEach { sector -> sector.pressedColor = color }
            invalidate()
        } catch (e: NotFoundException) {
            Log.e(TAG, "Error: ", e)
        }

    }

    /**
     * Sets the icon for the central button.
     * @param iconId id of the drawable resource;
     * for optimal display, the image should be vector drawable.
     */
    fun setCenterButtonIconId(@DrawableRes iconId: Int) {
        drawer.centerButtonIconResId = iconId
        invalidate()
    }

    /**
     * Sets the distance between sectors (buttons) in dp.
     */
    fun setSectorSpacing(dp: Float) {
        drawer.geometricProperties.sectorSpacing = dp.pixelsToDp(context)
        invalidate()
    }

    /**
     * Sets the padding from the inner radius of the circle to the button in dp.
     */
    fun setCenterButtonPadding(dp: Float) {
        drawer.geometricProperties.centerButtonPadding = dp.pixelsToDp(context)
        invalidate()
    }

    /**
     * Sets the color for the central button.
     * @param colorId color resource id.
     */
    fun setCenterButtonColorById(@ColorRes colorId: Int) {
        try {
            val color = context.getColor(colorId)
            drawer.centerButtonColor = color
            drawer.centerButtonCurrentColor = color
            invalidate()
        } catch (e: NotFoundException) {
            Log.e(TAG, "Error: ", e)
        }
    }

    /**
     * Sets the color for the central button.
     * @param color integer value of the color.
     */
    fun setCenterButtonColor(color: Int) {
        drawer.centerButtonColor = color
        drawer.centerButtonCurrentColor = color
        invalidate()
    }

    /**
     * Sets the color of the central button, which changes when the button is pressed.
     * @param colorId color resource id.
     */
    fun setCenterButtonPressedColorById(@ColorRes colorId: Int) {
        try {
            drawer.centerButtonPressedColor = context.getColor(colorId)
            invalidate()
        } catch (e: NotFoundException) {
            Log.e(TAG, "Error: ", e)
        }
    }

    /**
     * Sets the color of the central button, which changes when the button is pressed.
     * @param color integer value of the color.
     */
    fun setCenterButtonPressedColor(color: Int) {
        drawer.centerButtonPressedColor = color
        invalidate()
    }

    /**
     * Sets the current color of the central button, which changes when the button is pressed.
     * @param colorId color resource id.
     */
    fun setCenterButtonCurrentColorById(@ColorRes colorId: Int) {
        try {
            drawer.centerButtonCurrentColor = context.getColor(colorId)
            invalidate()
        } catch (e: NotFoundException) {
            Log.e(TAG, "Error: ", e)
        }
    }

    /**
     * Sets the current color of the central button, which changes when the button is pressed.
     * @param color integer value of the color.
     */
    fun setCenterButtonCurrentColor(color: Int) {
        drawer.centerButtonCurrentColor = color
        invalidate()
    }

    /**
     * Overrides the `AnimationEventListener`.
     */
    fun setAnimationEventListener(listener: AnimationEventListener) {
        animator.animationEventListener = listener
    }

    /**
     * Overrides the `onCenterButtonClickListener`.
     */
    fun setOnCenterButtonClickListener(listener: (CircularMenu) -> Unit) {
        onCenterButtonClick = listener
    }

    /**
     *  Overrides the `onSectorClickListener`.
     *  @param sectorIndex index of the sector pressed, triggered on `MotionEvent.ACTION_UP`.
     */
    fun setOnSectorClickListener(listener: (CircularMenu, sectorIndex: Int) -> Unit) {
        onSectorClick = listener
    }

    /**
     * Removes the CircularMenu from the parent layout.
     */
    fun removeThisViewFromParent() {
        try {
            with(parent as ViewGroup) {
                removeView(this@CircularMenu)
                requestLayout()
                invalidate()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error: ", e)
        }
    }

    /**
     * Returns the number of sectors (buttons).
     */
    fun size(): Int = _sectors.size

    /**
     * Returns the sector index if the point (x, y) is within one of them, returns null otherwise.
     */
    fun getSectorIndex(x: Float, y: Float): Int? {
        for (i in _sectors.indices) {
            val sector = _sectors[i]
            val result = pointCalculator.isPontInSector(
                x = x,
                y = y,
                startAngle = sector.startAngle,
                endAngle = sector.endAngle,
                centerX = drawer.geometricProperties.centerX,
                centerY = drawer.geometricProperties.centerY,
                outerRadius = drawer.geometricProperties.outerRadius,
                innerRadius = drawer.geometricProperties.innerRadius
            )
            if (result) {
                return i
            }
        }
        return null
    }

    /**
     * Returns a boolean indicating whether the point (x, y) is within
     * the inner radius of the circular menu (center button).
     */
    fun isPointInCenterButton(x: Float, y: Float): Boolean {
        return pointCalculator.isPointInCenterButton(
            x = x,
            y = y,
            centerX = drawer.geometricProperties.centerX,
            centerY = drawer.geometricProperties.centerY,
            innerRadius = drawer.geometricProperties.innerRadius,
            centerButtonPadding = drawer.geometricProperties.centerButtonPadding
        )
    }

    /**
     * Clears all animations and interrupts the chain of sequential animations,
     * leading to a call of the method `AnimationEventListener.animationOnCancel()`.
     */
    override fun clearAnimation() {
        animator.cancelAnimations()
        super.clearAnimation()
    }

    /**
     * Does nothing, overridden purposefully as setting a background should not be allowed.
     */
    override fun setBackgroundColor(color: Int) {}

    /**
     * Calling the `onCenterButtonClick` function
     */
    fun centerButtonPerformClick() {
        onCenterButtonClick(this)
    }

    /**
     * Calling the `onSectorClick` function
     */
    fun sectorPerformClick(sectorIndex: Int) {
        onSectorClick(this, sectorIndex)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x
        val y = event?.y
        val action = event?.action

        if (x == null || y == null || action == null || !isClickable) return false

        if (isPointInCenterButton(x, y)) {
            handleOutOfSectorsBoundsTouch()
            return handleOnCenterButtonClick(action)
        } else {
            if (isCenterButtonPressed) releaseCenterButton()
            getSectorIndex(x, y)?.let { sectorIndex ->
                return handleOnSectorClick(sectorIndex, action)
            } ?: run(::handleOutOfSectorsBoundsTouch)
        }
        return super.onTouchEvent(event)
    }

    private fun handleOnCenterButtonClick(action: Int): Boolean {
        val pressedColor = drawer.centerButtonPressedColor
        val result = when (action) {
            MotionEvent.ACTION_DOWN -> {
                animator.startPressCenterButtonAnimation()
                drawer.centerButtonCurrentColor = pressedColor
                isCenterButtonPressed = true
                true
            }
            MotionEvent.ACTION_UP -> {
                releaseCenterButton()
                onCenterButtonClick.invoke(this)
                true
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isCenterButtonPressed) {
                    animator.startPressCenterButtonAnimation()
                    isCenterButtonPressed = true
                    drawer.centerButtonCurrentColor = pressedColor
                }
                true
            }
            else -> false
        }
        invalidate()
        return result
    }

    private fun handleOnSectorClick(currentIndex: Int, action: Int): Boolean {
        val result = when (action) {
            MotionEvent.ACTION_DOWN -> {
                pressSector(currentIndex)
                lastPressedSectorIndex = currentIndex
                true
            }
            MotionEvent.ACTION_UP -> {
                releaseSector(currentIndex)
                lastPressedSectorIndex = null
                onSectorClick(this, currentIndex)
                true
            }
            MotionEvent.ACTION_MOVE -> {
                lastPressedSectorIndex?.let { i ->
                    if (i != currentIndex) {
                        releaseSector(i)
                        pressSector(currentIndex)
                    }
                } ?: run { pressSector(currentIndex) }
                lastPressedSectorIndex = currentIndex
                true

            }
            else -> false
        }
        invalidate()
        return result
    }

    private fun releaseSector(sectorIndex: Int) {
        animator.startReleaseSectorAnimation(sectorIndex)
        _sectors[sectorIndex].currentColor = _sectors[sectorIndex].color
    }

    private fun pressSector(sectorIndex: Int) {
        animator.startPressSectorAnimation(sectorIndex)
        _sectors[sectorIndex].currentColor = _sectors[sectorIndex].pressedColor
    }

    private fun releaseCenterButton() {
        animator.startReleaseCenterButtonAnimation()
        isCenterButtonPressed = false
        drawer.centerButtonCurrentColor = drawer.centerButtonColor
    }

    private fun handleOutOfSectorsBoundsTouch() {
        lastPressedSectorIndex?.let { i -> releaseSector(i) }
        lastPressedSectorIndex = null
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
        with(drawer.geometricProperties) {
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

    private fun obtainIconsIds(@ArrayRes id: Int): IntArray? {
        var typedArray: TypedArray? = null
        try {
            typedArray = resources.obtainTypedArray(id)
            val ids = mutableListOf<Int>()
            for (i in 0 until typedArray.length()) {
                val resID = typedArray.getResourceId(i, 0)
                if (resID != 0) ids.add(resID)
            }
            return ids.toIntArray()
        } catch (e: Exception) {
            Log.e(TAG, "Error: ", e)
        } finally {
            typedArray?.recycle()
        }
        return null
    }

    private fun obtainColors(@ArrayRes id: Int): IntArray? {
        var typedArray: TypedArray? = null
        try {
            typedArray = resources.obtainTypedArray(id)
            val colors = mutableListOf<Int>()
            for (i in 0 until typedArray.length()) {
                val color = typedArray.getColor(i, 0)
                if (color != 0) colors.add(color)
            }
            return colors.toIntArray()
        } catch (e: Exception) {
            Log.e(TAG, "Error: ", e)
        } finally {
            typedArray?.recycle()
        }
        return null
    }

    private fun processAttributes(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        try {
            typedArray = context?.obtainStyledAttributes(attrs, R.styleable.CircularMenu)

            val iconsIds: IntArray = typedArray?.obtainResourcesArray(
                R.styleable.CircularMenu_circleMenu_icons
            ) ?: intArrayOf()
            val colors: IntArray = typedArray?.obtainResourcesArray(
                R.styleable.CircularMenu_circleMenu_colors
            ) ?: intArrayOf()

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
                R.styleable.CircularMenu_circleMenu_sectorSpacing,
                Drawer.DEFAULT_BUTTON_SPACING
            )?.let { value -> drawer.geometricProperties.sectorSpacing = value }

            typedArray?.getDimension(
                R.styleable.CircularMenu_circleMenu_centerButtonPadding,
                Drawer.DEFAULT_CENTER_BUTTON_PADDING
            )?.let { value -> drawer.geometricProperties.centerButtonPadding = value }

            setColors(colors, false)
            setIcons(iconsIds, false)
        } catch (e: Exception) {
            Log.e(TAG, "Error: ", e)
        } finally {
            typedArray?.recycle()
        }
    }

    private fun TypedArray.obtainResourcesArray(resId: Int): IntArray {
        val mutableList = mutableListOf<Int>()
        val arrayId = this.getResourceId(resId, 0)
        if (arrayId == 0) return intArrayOf()

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
        return mutableList.toIntArray()
    }

    private fun initSectors(icons: IntArray) {
        if (icons.isEmpty()) throw IllegalArgumentException("There are no icons to create sectors.")

        val numberOfSectors = icons.size
        val cas = colors.size

        _sectors = Array(numberOfSectors) { i ->
            val sizeOfSector = 360 / numberOfSectors
            val color = if (cas != 0) colors[i % cas] else Drawer.DEFAULT_COLOR
            Sector(
                rectF = RectF(),
                startAngle = sizeOfSector * i,
                endAngle = sizeOfSector * (i + 1),
                iconId = icons[i],
                color = color,
                currentColor = color,
                pressedColor = drawer.sectorPressedColor,
            )
        }
    }

    interface AnimationEventListener {
        fun onOpenMenuAnimationStart() { }
        fun onOpenMenuAnimationEnd() { }
        fun onOpenMenuAnimationCancel() { }
        fun onCloseMenuAnimationStart() { }
        fun onCloseMenuAnimationEnd() { }
        fun onCloseMenuAnimationCancel() { }
        fun onChangeSectorsAnimationStart() { }
        fun onChangeSectorsAnimationEnd() { }
        fun onChangeSectorsAnimationCancel() { }
        fun onPressSectorAnimationStart() { }
        fun onPressSectorAnimationEnd() { }
        fun onPressSectorAnimationCancel() { }
        fun onReleaseSectorAnimationStart() { }
        fun onReleaseSectorAnimationEnd() { }
        fun onReleaseSectorAnimationCancel() { }
        fun onPressCenterButtonAnimationStart() { }
        fun onPressCenterButtonAnimationEnd() { }
        fun onPressCenterButtonAnimationCancel() { }
        fun onReleaseCenterButtonAnimationStart() { }
        fun onReleaseCenterButtonAnimationEnd() { }
        fun onReleaseCenterButtonAnimationCancel() { }
    }

}