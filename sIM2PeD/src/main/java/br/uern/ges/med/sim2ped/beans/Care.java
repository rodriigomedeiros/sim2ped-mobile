package br.uern.ges.med.sim2ped.beans;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Care implements Serializable{

	private String titleCare;
	private String careQuestion;
	private long id;
	private long createdAt;
	private long modifiedAt;
	private long insertedAt;
	
	public Care() {}
	
	public Care(int ID, String titleCare){
		this.id = ID;
		this.titleCare = titleCare;
	}
	
	public long getID() {
		return id;
	}
	
	public String getTitleCare() {
		return titleCare;
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
	
	public String getCareQuestion() {
		return careQuestion;
	}
	
	public void setID(long iD) {
		this.id = iD;
	}
	
	public void setTitleCare(String titleCare) {
		this.titleCare = titleCare;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}
	
	public void setModifiedAt(long modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
	
	public void setInsertedAt(long insertedAt) {
		this.insertedAt = insertedAt;
	}
	
	public void setCareQuestion(String titleCareInQuestion) {
		this.careQuestion = titleCareInQuestion;
	}
	
}