package br.uern.ges.med.sim2ped.beans;

import java.io.Serializable;

/**
 * Created by Rodrigo on 04/10/2014 (19:32).
 * <p/>
 * <p/>
 * Package: br.uern.ges.med.sim2ped.beans
 */
public class StatisticalReadingTips implements Serializable{

    private long id = 0;
    private long statisticalId = 0;
    private long tipId = 0;
    private long hourReading = 0;
    private long userId = 0;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStatisticalId() {
        return statisticalId;
    }

    public void setStatisticalId(long statisticalId) {
        this.statisticalId = statisticalId;
    }

    public long getTipId() {
        return tipId;
    }

    public void setTipId(long tipId) {
        this.tipId = tipId;
    }

    public long getHourReading() {
        return hourReading;
    }

    public void setHourReading(long hourReading) {
        this.hourReading = hourReading;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
