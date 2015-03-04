package br.uern.ges.med.sim2ped.beans;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Tip implements Serializable{

	private long id = 0;
    private long careId = 0;
	private String text;
    private int priority = 0;
	private long createdAt;
	private long modifiedAt;
	private long insertedAt;

	public Tip(long iD, String text, int priority){
		this.id = iD;
		this.text = text;
		this.priority = priority;
	}

	public Tip(){ }

	public long getID() {
		return id;
	}

    public long getCareId(){
        return careId;
    }

	public String getText() {
		return text;
	}

	public String getColor() {
        String color = "0";
        if(priority == 1)
			color = "green";
		else if(priority == 2)
			color = "orange";
		else
			color = "red";

		return color;
	}

	public int getPriority() {
		return priority;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public long getInsertedAt() {
		return insertedAt;
	}

	public long getModifiedAt() {
		return modifiedAt;
	}

	public void setText(String texto) {
		this.text = texto;
	}

	public void setID(long ID) {
		this.id = ID;
	}

    public void setCareId(long careId) {
        this.careId = careId;
    }

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public void setModifiedAt(long modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public void setInsertedAt(long insertedAt){
		this.insertedAt = insertedAt;
	}

}
