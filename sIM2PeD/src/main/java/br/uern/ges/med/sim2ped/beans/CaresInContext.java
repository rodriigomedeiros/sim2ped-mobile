package br.uern.ges.med.sim2ped.beans;

import java.io.Serializable;

/**
 * Created by Rodrigo on 15/09/2014 (12:33).
 * <p/>
 * Package: br.uern.ges.med.sim2ped.beans
 */

@SuppressWarnings("serial")
public class CaresInContext  implements Serializable {
    private long contextId = 0;
    private long careId = 0;

    public long getContextId() {
        return contextId;
    }

    public void setContextId(long contextId) {
        this.contextId = contextId;
    }

    public long getCareId() {
        return careId;
    }

    public void setCareId(long careId) {
        this.careId = careId;
    }
}
