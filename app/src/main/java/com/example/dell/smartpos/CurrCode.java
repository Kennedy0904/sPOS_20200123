package com.example.dell.smartpos;

public class CurrCode {
	public final static int size = 49;

	public final static String[] curName = { "HKD", "USD", "SGD", "RMB", "JPY", "TWD", "AUD", "EUR", "GBP", "CAD",
			"MOP", "PHP", "THB", "MYR", "IDR", "KRW", "BND", "NZD", "SAR", "AED", 
			"BRL", "INR", "TRY", "ZAR", "VND", "DKK", "ILS", "NOK", "RUB", "SEK", 
			"CHF", "ARS", "CLP", "COP", "CZK", "EGP", "HUF", "KZT", "LBP", "MXN", 
			"NGN", "PKR", "PEN", "PLN", "QAR", "RON", "UAH", "VEF", "BHD" };

	public final static String[] curCode = { "344", "840", "702", "156", "392", "901", "036", "978", "826", "124",
			"446", "608", "764", "458", "360", "410", "096", "554", "682", "784", 
			"986", "356", "949", "710", "704", "208", "376", "578", "643", "752",
			"756", "032", "152", "170", "203", "818", "348", "398", "422", "484",
			"566", "586", "604", "985", "634", "946", "980", "937", "048" };
	
	// reference to http://www.xe.com/symbols.php
	public final static String[] curSymbol = { "&#36", "&#36", "&#36", "&#165", "&#165", "NT&#36", "&#36", "&#8364", "&#163", "&#36",
		"&#36", "P", "&#3647", "&#77", "&#112", "&#8361", "&#36", "&#36", "&#65020", "DH", 
		"R&#36", "INR", "TL", "R", "&#8363", "kr", "&#8362;", "kr", "&#1088;&#1091;&#1073;", "kr", 
		"CHF", "$", "$", "$", "K&#269;", "&#163;", "Ft", "&#1083;&#1074;", "&#163;", "$", 
		"&#8358;", "&#8360;", "S/.", "z&#322;", "&#65020;", "lei", "&#8372;", "Bs", "&#36" };
	 
	public final static int[] curExponent = { 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 
		1, 2, 2, 2, 0, 0, 2, 2, 2, 2, 
		2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 
		2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
		2, 2, 2, 2, 2, 2, 2, 2, 3 };

	public final static double[] fxRate = { 
		1.0, // HKD 
		7.7500, // USD
		6.3473, // SGD
		1.2446, // RMB
		0.09411, // JPY
		0.2668, // TWD
		8.0561, // AUD 
		10.0758, // EUR
		12.4194, // GBP
		7.8007, // CAD
		0.9708, // MOP
		0.1895, // PHP
		0.2524, // THB
		2.5502, // MYR
		0.0008060, // IDR
		0.007153, // KRW
		6.3478, // BND 
		6.3349, // NZD
		2.0666, // SAR
		2.1100, // AED
		3.6283, // BRL
		0.1428, // INR
		4.3359, // TRY 
		0.8704, // ZAR
		0.0003720, // VND
		1.3506, //DKK
		2.0303, //ILS
		1.3658, //NOK
		0.2511, //RUB
		1.1649, //SEK
		8.3594, //CHF
		1.6022, //ARS
		0.01613, //CLP
		0.004270, //COP
		0.3990, //CZK
		1.2684, //EGP
		0.03582, //HUF
		0.05147, //KZT
		0.005146, //LBP
		0.5987, //MXN
		0.04920, //NGN
		0.08024, //PKR
		3.0050, //PEN
		2.4513, //PLN
		2.1287, //QAR
		2.2298, //RON
		0.9463, //UAH
		0.003612, //VEF
		20.5571 // BHD
		};

	static public String select(String ip_name, String ip_curName,
                                String ip_init) {
		String statment = "<select name=\"" + ip_name + "\">";

		if (ip_init != null)
			statment += "<option value=\"" + ip_init + "\" >" + ip_init
					+ "</option>";

		for (int i = 0; i < size; i++) {
			if (ip_curName.equals(curCode[i])) {
				statment += "<option value=\"" + curCode[i] + "\" selected>"
						+ curName[i] + "</option>";
			} else {
				statment += "<option value=\"" + curCode[i] + "\">"
						+ curName[i] + "</option>";
			}
		}
		statment += "</select>";
		return statment;
	}

	static public double getFxRateByCode(String ip_curCode) {
		for (int i = 0; i < size; i++) {
			if (curCode[i].equals(ip_curCode)) {
				return fxRate[i];
			}
		}
		return -1;
	}
	
	static public double getFxRateByCode(String ip_curCode, String base_curCode) {
		double baseFxRate = 0.0000;
		for (int i = 0; i < size; i++) {
			if (curCode[i].equals(base_curCode)) {
				baseFxRate = fxRate[i];
				break;
			}
		}
		for (int i = 0; i < size; i++) {
			if (curCode[i].equals(ip_curCode)) {
				return fxRate[i]/baseFxRate;
			}
		}
		return -1;
	}

	static public String getName(String ip_curCode) {
		for (int i = 0; i < size; i++) {
			if (curCode[i].equals(ip_curCode)) {
				return curName[i];
			}
		}
		return null;
	}

	static public String getCode(String ip_curName) {
		for (int i = 0; i < size; i++) {
			if (curName[i].equals(ip_curName)) {
				return curCode[i];
			}
		}
		return null;
	}
	
	static public int getExponent(String ip_curCode) {
		for (int i = 0; i < size; i++) {
			if (curCode[i].equals(ip_curCode)) {
				return curExponent[i];
			}
		}
		return 2;
	}
	
	static public String getSymbol(String ip_curCode) {
		for (int i = 0; i < size; i++) {
			if (curCode[i].equals(ip_curCode)) {
				return curSymbol[i];
			}
		}
		return null;
	}

	static public boolean isCurCode(String ip_curCode) {
		for (int i = 0; i < size; i++)
			if (curCode[i].equals(ip_curCode))
				return true;
		return false;
	}
}
