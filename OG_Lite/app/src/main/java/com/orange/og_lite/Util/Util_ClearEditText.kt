package com.orange.og_lite.Util


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

import androidx.constraintlayout.widget.Constraints.TAG
import com.example.jztaskhandler.TaskHandler
import com.orange.jzchi.jzframework.JzActivity

/**
 * 带清除按钮的EditText
 * Created by haide.yin() on 2019/1/23 15:38.
 */
class Util_ClearEditText
/**
 * 构造函数
 *
 * @param context the context
 * @param attrs   the attrs
 */
    (context: Context, attrs: AttributeSet) :
    AppCompatEditText(context, attrs, android.R.attr.editTextStyle), View.OnFocusChangeListener,
    TextWatcher {

    var textchange: com.orange.og_lite.callback.textchange? = null
    var Keyboardtext: com.orange.og_lite.callback.Keyboardtext? = null
    private var mLastText: String? = null
    private var mFormat: Boolean = false
    private var mInvalid: Boolean = false
    public  var mSelection: Int = 0
    private var mStartSelection: Int = 0
    private var mClearDrawable: Drawable? = null // 删除按钮的引用
    private var hasFocus: Boolean = false // 控件是否有焦点
    private var mClearStatusListener: ClearStatusListener? = null//回调接口
    /**
     * 最大数字限制
     * @return 设置最大数字限制
     */
    /**
     * 设置最大数字限制
     * @param maxWords 设置最大数字限制
     */
    var maxWords = 30
        set(maxWords) {
            field = maxWords
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxWords))
        }//限制最大输入的个数

    init {
        init()
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
            if(Keyboardtext!=null)
            {
                Keyboardtext!!.start(selStart)
                Keyboardtext!!.end(selEnd)
            }
    }
    /**
     * 设置回调函数
     *
     * @param clearStatusListener 回调接口
     */
    fun setClearStatusListener(clearStatusListener: ClearStatusListener) {
        this.mClearStatusListener = clearStatusListener
    }

    /**
     * 初始化
     */
    private fun init() {
        //设置最大数字限制
        mFormat = false
        mInvalid = false
        mLastText = ""
        maxWords = this.maxWords
        //获取EditText的DrawableEnd和DrawableRight
        mClearDrawable = compoundDrawablesRelative[2]
        if (mClearDrawable == null) {
            mClearDrawable = compoundDrawables[2]
            if (mClearDrawable == null) {
                //假如没有设置我们就使用默认的图片
                //mClearDrawable = getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel);
            }
        }
        if (mClearDrawable != null) {
            mClearDrawable!!.setBounds(
                0,
                0,
                mClearDrawable!!.intrinsicWidth,
                mClearDrawable!!.intrinsicHeight
            )
        }
        //默认设置隐藏图标
        setClearIconVisible(false)
        //设置焦点改变的监听
        onFocusChangeListener = this
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mClearDrawable != null && event.action == MotionEvent.ACTION_UP) {
            val x = event.x.toInt()
            //判断触摸点是否在水平范围内
            val isInnerWidth = x > width - totalPaddingRight && x < width - paddingRight
            //获取删除图标的边界，返回一个Rect对象
            val rect = mClearDrawable!!.bounds
            //获取删除图标的高度
            val height = rect.height()
            val y = event.y.toInt()
            //计算图标底部到控件底部的距离
            val distance = (getHeight() - height) / 2
            //判断触摸点是否在竖直范围内(可能会有点误差)
            //触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中删除图标
            val isInnerHeight = y > distance && y < distance + height
            if (isInnerHeight && isInnerWidth) {
                this.setText("")
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible 是否可见
     */
    private fun setClearIconVisible(visible: Boolean) {
        onStatus(visible)
        val right = if (visible && isEnabled) mClearDrawable else null
        setCompoundDrawables(
            compoundDrawables[0],
            compoundDrawables[1],
            right,
            compoundDrawables[3]
        )
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible 是否可见
     */
    fun hideClearIcon(visible: Boolean) {
        val right = if (visible) mClearDrawable else null
        setCompoundDrawables(
            compoundDrawables[0],
            compoundDrawables[1],
            right,
            compoundDrawables[3]
        )
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        this.hasFocus = hasFocus
        if (hasFocus) {
            val content = text!!.toString().trim { it <= ' ' }
            setClearIconVisible(!TextUtils.isEmpty(content))
        } else {
            setClearIconVisible(false)
        }
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {

        try {

            var temp = text.toString()

            if (start < text.length - 1 || lengthAfter > lengthBefore) {
                //                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(txtColor)), start, start+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            // Set selection.
            if (mLastText == temp) {
                if (mInvalid) {
                    mSelection -= 1
                } else {
                    if (mSelection >= 1 && temp.length > mSelection - 1
                        && temp[mSelection - 1] == ' '
                    ) {
                        mSelection += 1
                    }
                }
                val length = mLastText!!.length
                if (mSelection > length) {

//                    this.setSelection(length)
                } else {

//                    this.setSelection(mSelection)
                }
                mFormat = false
                mInvalid = false
                return
            }

            mFormat = true
            mSelection = start

            // Delete operation.
            if (lengthAfter == 0) {
                if (mSelection >= 1 && temp.length > mSelection - 1
                    && temp[mSelection - 1] == ' '
                ) {
                    mSelection -= 1
                }

                return
            }

            // Input operation.
            mSelection += lengthAfter
            val lastChar = temp.substring(start, start + lengthAfter)
                .toCharArray()
            val mid = lastChar[0].toInt()
            if (mid >= 48 && mid <= 57) {
            } else if (mid >= 65 && mid <= 70) {
            } else if (mid >= 97 && mid <= 102) {
            } else {
                mInvalid = true
                temp = temp.substring(0, start) + temp.substring(start + lengthAfter)
                this.setText(temp)
                return
            }

        } catch (e: Exception) {
            Log.i(TAG, e.toString())
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        //Keyboardtext!!.start(start)
    }

    override fun afterTextChanged(s: Editable) {
        if (mFormat) {
            val text = StringBuilder()
            text.append(s.toString().replace(" ", "").toUpperCase())
            if (mLastText!!.length > text.toString().length) {
                this.setText(text)
            }
            if (textchange != null) {
                textchange!!.callback()

            }
            if (Keyboardtext != null) {
                Keyboardtext!!.callback()
            }
        }
    }


    /**
     * 是否显示清除按钮
     *
     * @param show the show
     */
    private fun onStatus(show: Boolean) {
        if (mClearStatusListener != null) {
            mClearStatusListener!!.onStatus(show)
        }
    }

    /*************** 接口  */

    interface ClearStatusListener {

        /**
         * 是否显示清除按钮
         *
         * @param empty 是否是空的
         */
        fun onStatus(empty: Boolean)
    }
}