package br.uern.ges.med.sim2ped.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Rodrigo on 04/10/2014 (19:30).
 *
 *
 * Package: br.uern.ges.med.sim2ped.beans
 */
public class StatisticalUsage implements Serializable{

    private long id = 0;
    private long entryTime = 0;
    private long userId = 0;
    private int sentServer = 0;
    private ArrayList<StatisticalReadingTips> statisticalReadingTips;
    private ArrayList<StatisticalViewProcedure> statisticalViewProcedures;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(long entryTime) {
        this.entryTime = entryTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getSentServer() {
        return sentServer;
    }

    public void setSentServer(int sentServer) {
        this.sentServer = sentServer;
    }

    public ArrayList<StatisticalReadingTips> getStatisticalReadingTips() {
        return statisticalReadingTips;
    }

    public void setStatisticalReadingTips(ArrayList<StatisticalReadingTips> statisticalReadingTips) {
        this.statisticalReadingTips = statisticalReadingTips;
    }

    public ArrayList<StatisticalViewProcedure> getStatisticalViewProcedures() {
        return statisticalViewProcedures;
    }

    public void setStatisticalViewProcedures(ArrayList<StatisticalViewProcedure> statisticalViewProcedures) {
        this.statisticalViewProcedures = statisticalViewProcedures;
    }
}
