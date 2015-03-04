package br.uern.ges.med.sim2ped.beans;

import java.io.Serializable;

/**
 * Created by Rodrigo on 04/10/2014 (19:32).
 * <p/>
 * <p/>
 * Package: br.uern.ges.med.sim2ped.beans
 */
public class StatisticalViewProcedure implements Serializable{

    private long id = 0;
    private long statisticalId = 0;
    private long hourView = 0;
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

    public long getHourView() {
        return hourView;
    }

    public void setHourView(long hourView) {
        this.hourView = hourView;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
