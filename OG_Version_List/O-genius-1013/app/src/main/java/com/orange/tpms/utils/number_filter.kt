package com.orange.tpms.utils

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.orango.electronic.orange_og_lib.Util.jzString
import com.orange.tpms.R

/**
 * The `CustomTextWatcher` class is used to add a TextWatcher to the
 * list of those whose methods are called whenever this TextView's text changes.
 *
 * @author IndexCqq
 * @version 1.00.00, 11 May 2015
 */
class number_filter
/**
 * Creates an instance of `CustomTextWatcher`.
 *
 * @param editText
 * the editText to edit text.
 */
    (
    /**
     * The editText to edit text.
     */
    private val mEditText: EditText, private val count: Int, private val context: Context
) : TextWatcher {

    private var mFormat: Boolean = false

    private var mInvalid: Boolean = false

    private var mSelection: Int = 0
    private var mLastText: String? = null

    init {
        mFormat = false
        mInvalid = false
        mLastText = ""
        this.mEditText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    override fun beforeTextChanged(
        charSequence: CharSequence, start: Int,
        count: Int, after: Int
    ) {

    }

    override fun onTextChanged(
        charSequence: CharSequence, start: Int, before: Int,
        count: Int
    ) {

        try {

            var temp = charSequence.toString()

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

                    mEditText.setSelection(length)
                } else {

                    mEditText.setSelection(mSelection)
                }
                mFormat = false
                mInvalid = false
                return
            }

            mFormat = true
            mSelection = start

            // Delete operation.
            if (count == 0) {
                if (mSelection >= 1 && temp.length > mSelection - 1
                    && temp[mSelection - 1] == ' '
                ) {
                    mSelection -= 1
                }

                return
            }

            // Input operation.
            mSelection += count
            val lastChar = temp.substring(start, start + count)
                .toCharArray()
            val mid = lastChar[0].toInt()
            if (mid >= 48 && mid <= 57) {
                Log.e("long", "" + temp)
                if (Integer.parseInt(temp) > 4) {
                    mInvalid = true
                    mEditText.setText("4")
                    Toast.makeText(
                        context,
                        context.resources!!.jzString(R.string.maxsize),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                } else if (Integer.parseInt(temp) == 0) {
                    mInvalid = true
                    mEditText.setText("1")
                    return
                }
            } else {
                /* Invalid input. */
                mInvalid = true
                temp = temp.substring(0, start) + temp.substring(start + count)
                mEditText.setText(temp)
                return
            }

        } catch (e: Exception) {
            Log.i(TAG, e.toString())
        }

    }

    override fun afterTextChanged(editable: Editable) {

        try {
            /* Format input. */
            if (mFormat) {
                val text = StringBuilder()
                text.append(editable.toString().replace(" ", ""))
                mLastText = text.toString()
                mEditText.setText(text)
            }
        } catch (e: Exception) {
            Log.i(TAG, e.toString())
        }

    }

    companion object {

        private val TAG = "CustomTextWatcher"
    }

}