package com.cowbell.cordova.geofence;


import com.google.gson.annotations.Expose;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.util.Log;

public class Period {
    @Expose public Calendar fromDate;
    @Expose public Calendar toDate;
    @Expose public Calendar fromDateNextPeriod;
    @Expose public int repeat;

    static final int ONCE        = 0;
    static final int EVERY_DAY   = 1;
    static final int EVERY_WEEK  = 2;
    static final int EVERY_MONTH = 3;
    static final int EVERY_YEAR  = 4;

    private Logger logger;

    public Period(Calendar fromDate, 
		  Calendar toDate, 
		  Calendar fromDateNextPeriod, 
		  int repeat) {
	this.fromDate              = fromDate;
	this.toDate                = toDate;
	this.fromDateNextPeriod    = fromDateNextPeriod;
	this.repeat                = repeat;
    }

    public boolean isRepeat() {
	return repeat != ONCE;
    }

    private void setFromDateNextPeriodDay(Calendar now) {
	while (this.fromDateNextPeriod.after(now) != true) {
	    this.fromDateNextPeriod.add(Calendar.DAY_OF_MONTH, 1);
	}
    }

    private void setFromDateNextPeriodWeek(Calendar now) {
	while (this.fromDateNextPeriod.after(now) != true) {
	    this.fromDateNextPeriod.add(Calendar.DAY_OF_MONTH, 7);
	}
    }

    private void setFromDateNextPeriodMonth(Calendar now) {
	while (this.fromDateNextPeriod.after(now) != true) {
	    this.fromDateNextPeriod.add(Calendar.MONTH, 1);
	}
    }

    private void setFromDateNextPeriodYear(Calendar now) {
	while (this.fromDateNextPeriod.after(now) != true) {
	    this.fromDateNextPeriod.add(Calendar.YEAR, 1);
	}
    }

    private void setFromDateNextPeriod(Calendar now) {
        Logger logger = Logger.getLogger();
	switch (repeat) {
	case ONCE:
	    break;
	case EVERY_DAY:
	    setFromDateNextPeriodDay(now);
	    break;
	case EVERY_WEEK:
	    setFromDateNextPeriodWeek(now);
	    break;
	case EVERY_MONTH:
	    setFromDateNextPeriodMonth(now);
	    break;
	case EVERY_YEAR:
	    setFromDateNextPeriodYear(now);
	    break;
	default:
	    logger.log(Log.ERROR, "Unknown state in repeat: " + repeat);
	    break;
	}
    }

    boolean isFiredInCurrentPeriod(Calendar now) {
        logger = Logger.getLogger();

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	logger.log(Log.DEBUG, "isFiredInCurrentPeriod(): fromDateNextPeriod = " 
		    + fromDateNextPeriod);
	
	logger.log(Log.DEBUG, "isFiredInCurrentPeriod(): fromDateNextPeriod = " 
		    + sdf.format(fromDateNextPeriod.getTime()));
	logger.log(Log.DEBUG, "isFiredInCurrentPeriod(): now      = "
		    + sdf.format(now.getTime()));
	logger.log(Log.DEBUG, "now.after(fromDateNextPeriod) = " 
		    + now.after(fromDateNextPeriod));

	if (now.after(fromDateNextPeriod) != true) {
	    logger.log(Log.DEBUG, "isFiredInCurrentPeriod(): isn't fired.");
	    return false;
	}
	logger.log(Log.DEBUG, "isFiredInCurrentPeriod(): fired.");
	setFromDateNextPeriod(now);
	logger.log(Log.DEBUG, "isFiredInCurrentPeriod(): fromDateNextPeriod = " 
		    + sdf.format(fromDateNextPeriod.getTime()));
	return true;
    }

    public String toString() {
        return "Repeat fromDate: " + fromDate.toString()
            + " toDate: " + toDate.toString()
	    + " repeat: " + repeat
	    ;
    }
}
