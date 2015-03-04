package br.uern.ges.med.sim2ped.utils;

public class Utility {
	
	/**
     * Checks for Null String object
     * 
     * @param txt
     * @return true for not null and false for null String object
     */
	public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length()>0 ? true: false;
    }

}
