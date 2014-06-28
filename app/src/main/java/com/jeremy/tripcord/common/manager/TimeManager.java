package com.jeremy.tripcord.common.manager;

import com.jeremy.tripcord.common.manager.exceptions.NoStartTimeException;

import java.util.Date;

/**
 * Created by asura1983 on 2014. 5. 24..
 */
public class TimeManager {

    private static TimeManager instance = null;
    private Date startTime;
    private Date endTime;

    private TimeManager() {

    }

    public static TimeManager getInstance() {

        if (instance == null) {

            instance = new TimeManager();
        }

        return instance;
    }

    public void start() {

        startTime = new Date();
    }

    public int stop() throws NoStartTimeException {

        if (startTime == null) {
            throw new NoStartTimeException();
        }

        endTime = new Date();
        int duringTime = startTime.compareTo(endTime);

        return duringTime;
    }

    public int getTimeTaken() throws NoStartTimeException {

        if (startTime == null) {
            throw new NoStartTimeException();
        }

        int timeTaken = 0;
        if (endTime == null) {

            Date currentTime = new Date();
            timeTaken = startTime.compareTo(currentTime);
        } else {

            timeTaken = startTime.compareTo(endTime);
        }

        return timeTaken;
    }

    public void reset() {

        startTime = null;
        endTime = null;
    }

    public boolean isRunning() {

        if (startTime != null) {
            return true;
        }

        return false;
    }
}
