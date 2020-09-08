package com.hpcang.smart.ui.util

import android.view.View
import androidx.annotation.IntRange

/**
 * 防抖动点击
 */
object AntiShakeUtils {
    private const val INTERNAL_TIME: Long = 800

    /**
     * Whether this click event is invalid.
     *
     * @param target target view
     * @return true, invalid click event.
     * @see .isInvalidClick
     */
    fun isInvalidClick(target: View): Boolean {
        return isInvalidClick(target, INTERNAL_TIME)
    }

    /**
     * Whether this click event is invalid.
     *
     * @param target       target view
     * @param internalTime the internal time. The unit is millisecond.
     * @return true, invalid click event.
     */
    fun isInvalidClick(
        target: View,
        @IntRange(from = 0) internalTime: Long
    ): Boolean {
        val curTimeStamp = System.currentTimeMillis()
        var lastClickTimeStamp: Long = 0
        val o = target.getTag(target.id)
        if (o == null) {
            target.setTag(target.id, curTimeStamp)
            return false
        }
        lastClickTimeStamp = o as Long
        val isInvalid = curTimeStamp - lastClickTimeStamp < internalTime
        if (!isInvalid) target.setTag(target.id, curTimeStamp)
        return isInvalid
    }
}