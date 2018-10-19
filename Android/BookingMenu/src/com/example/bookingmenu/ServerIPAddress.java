package com.example.bookingmenu;

public class ServerIPAddress {
	public static String serverIPaddress = null;
	public static String ID = "1";
	public static String Kural = null;
	public static String Meaning=null;
	public static String url=null;
	
	
	public static String getMeaning() {
		return Meaning;
	}

	public static void setMeaning(String Meaning) {
		ServerIPAddress.Meaning = Meaning;
	}

	public static String getKural() {
		return Kural;
	}

	public static void setKural(String Kural) {
		ServerIPAddress.Kural = Kural;
	}

	public static String getID() {
		return ID;
	}

	public static void setID(String ID) {
		ServerIPAddress.ID = ID;
	}

	public static String getserverIPaddress() {
		return serverIPaddress;
	}

	public static void setserverIPaddress(String serverIPaddress) {
		ServerIPAddress.serverIPaddress = serverIPaddress;
	}

	public static String geturl() {
		return url;
	}

	public static void seturl(String url) {
		ServerIPAddress.url = url;
	}
	
	

}
