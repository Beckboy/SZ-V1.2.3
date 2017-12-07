package com.junhsue.ksee.net.api;

import java.util.Date;

public abstract class SlideDateTimeListener
{
    public abstract void onDateTimeSet(Date date);

    public void onDateTimeCancel()
    {

    }
}
