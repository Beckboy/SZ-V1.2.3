package com.junhsue.ksee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.DatePicker;

/**
 * A subclass of {@link DatePicker} that uses
 * reflection to allow for customization of the default blue
 * dividers.
 *
 * @author jjobes
 *
 */
public class CustomDatePicker extends DatePicker {
//
    public CustomDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
//
//        Class<?> idClass = null;
//        Class<?> numberPickerClass = null;
//        Field selectionDividerField = null;
//        Field monthField = null;
//        Field dayField = null;
//        Field yearField = null;
//
//        try{
//            // Create an instance of the id class
//            idClass = Class.forName("com.android.internal.R$id");
//
//            // Get the fields that store the resource IDs for the month, day and year NumberPickers
//            monthField = idClass.getField("month");
//            dayField = idClass.getField("day");
//            yearField = idClass.getField("year");
//
//            numberPickerClass = Class.forName("android.widget.NumberPicker");
//
//            selectionDividerField = numberPickerClass.getDeclaredField("mSelectionDivider");
//            selectionDividerField.setAccessible(true);
//        }
//        catch (ClassNotFoundException e){
//            Log.e("FS", "ClassNotFoundException in CustomDatePicker", e);
//        }
//        catch (NoSuchFieldException e){
//            Log.e("FS", "NoSuchFieldException in CustomDatePicker", e);
//        }
//        catch (IllegalArgumentException e){
//            Log.e("FS", "IllegalArgumentException in CustomDatePicker", e);
//        }
    }
}
