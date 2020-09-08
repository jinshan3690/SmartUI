package com.hpcang.smart.ui.util;

import android.view.View;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

/**
 * 防抖动点击
 */
public class AntiShakeUtils {

    private final static long INTERNAL_TIME = 800;

    /**
     * Whether this click event is invalid.
     *
     * @param target target view
     * @return true, invalid click event.
     * @see #isInvalidClick(View, long)
     */
    public static boolean isInvalidClick(@NonNull View target) {
        return isInvalidClick(target, INTERNAL_TIME);
    }

    /**
     * Whether this click event is invalid.
     *
     * @param target       target view
     * @param internalTime the internal time. The unit is millisecond.
     * @return true, invalid click event.
     */
    public static boolean isInvalidClick(@NonNull View target, @IntRange(from = 0) long internalTime) {
        long curTimeStamp = System.currentTimeMillis();
        long lastClickTimeStamp = 0;
        Object o = target.getTag(target.getId());
        if (o == null){
            target.setTag(target.getId(), curTimeStamp);
            return false;
        }
        lastClickTimeStamp = (Long) o;
        boolean isInvalid = curTimeStamp - lastClickTimeStamp < internalTime;
        if (!isInvalid)
            target.setTag(target.getId(), curTimeStamp);
        return isInvalid;
    }
}