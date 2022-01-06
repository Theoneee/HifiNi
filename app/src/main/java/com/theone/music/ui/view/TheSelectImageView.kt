package com.theone.music.ui.view

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import com.theone.music.R

//  ┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//    ┃　　　┃                  神兽保佑
//    ┃　　　┃                  永无BUG！
//    ┃　　　┗━━━┓
//    ┃　　　　　　　┣┓
//    ┃　　　　　　　┏┛
//    ┗┓┓┏━┳┓┏┛
//      ┃┫┫　┃┫┫
//      ┗┻┛　┗┻┛
/**
 * @author The one
 * @date 2021-04-14 09:23
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
class TheSelectImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyle), View.OnClickListener {

    /**
     * 普通状态时的图片
     */
    var defaultDrawable: Drawable? = null
        set(value) {
            field = value
            setDefaultImage()
        }

    /**
     * 选中状态时的图片
     */
    var selectedDrawable: Drawable? = null

    /**
     * 当前选中状态
     */
    private var mIsSelected = false

    /**
     * 动画时间
     */
    var animationDuration: Float = 0f

    var animationEnable: Boolean = true

    /**
     * 状态监听
     */
    var onSelectChangedListener: OnSelectChangedListener? = null


    init {
        context.obtainStyledAttributes(attrs, R.styleable.TheSelectImageView).run {
            defaultDrawable = getDrawable(R.styleable.TheSelectImageView_img_normal)
            selectedDrawable = getDrawable(R.styleable.TheSelectImageView_img_selected)
            animationDuration = getFloat(R.styleable.TheSelectImageView_animal_duration, 150f)
            animationEnable = getBoolean(R.styleable.TheSelectImageView_animal_enable, true)
            mIsSelected = getBoolean(R.styleable.TheSelectImageView_isSelected, false)
            recycle()
        }
        setDefaultImage()
        setOnClickListener(this)
    }

    fun setSelect(select:Boolean){
        updateSelect(select)
    }

    private fun setDefaultImage() {
        defaultDrawable?.let {
            background = it
        }
    }

    private fun startAnimation() {
        if (animationEnable)
            createViewPropertyAnimation(0f, object : AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    createViewPropertyAnimation(1.0f).start()
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationStart(animation: Animator?) {

                }

            }).start()
    }

    //***   同样的动画，两种不同的写法 ****/////
    private fun createViewPropertyAnimation(
        scale: Float,
        listener: AnimatorListener? = null
    ): ViewPropertyAnimator {
        return animate().scaleX(scale)
            .scaleY(scale)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(animationDuration.toLong())
            .setListener(listener)
    }

    private fun createObjectAnimation(
        scale: Float,
        listener: AnimatorListener? = null
    ): ObjectAnimator {
        val holder1 = PropertyValuesHolder.ofFloat("scaleX", scale)
        val holder2 = PropertyValuesHolder.ofFloat("scaleY", scale)
        return ObjectAnimator.ofPropertyValuesHolder(this, holder1, holder2).apply {
            addListener(listener)
        }
    }

    /**
     * 更新选中状态
     */
    private fun updateSelect(select: Boolean) {
        // 更改选中标记状态
        mIsSelected = select
        // 根据状态切换不同的背景
        this.background = if (mIsSelected) selectedDrawable else defaultDrawable
    }

    interface OnSelectChangedListener {

        fun onSelectChanged(isSelected: Boolean)

    }

    override fun onClick(v: View?) {
        updateSelect(!mIsSelected)
        startAnimation()
        onSelectChangedListener?.onSelectChanged(mIsSelected)
    }

}