package com.example.dell.smartpos;

public class PromptPayGenerate {

	String fullPromptPayMsg = "";
	
	//ID 00
	String payLoadFormatIndicator;
	//ID01
	String pointOfInitiationMethod;
	//ID 30
	String merchantIdentifier;
	//ID 53
	String transactionCurrencyCode;
	//ID 54
	String transactionAmount;
	//ID 58
	String countryCode;
	//ID 59
	String merchantName;
	//ID 62
	String additionalDataFieldTemplate;
	//ID 63
	String crc;
	
	//ID 30-00
	String aid;
	
	
	
	int lengthPayLoadFormatIndicator = 2;
	int lengthPointofInitialMethod = 2;
	int lengthMerchantIdentifier = 99;
	int lengthTransactionCurrencyCode = 3;
	int lengthTransactionAmount = 13;
	int lengthCountryCode = 2;
	int lengthMerchantName = 29;
	int lengthAdditionalDataFieldTemplate = 99;
	int lengthCrc = 4;
	
	//Length for subdetail 30
	int lengthAID = 16;
	int lengthBillerID = 15;
	int lengthReference1 = 20;
	int lengthReference2 = 20;
	
	//Length for supdetail 63
	int lengthBillNumber = 26;
	int lengthMobileNumber = 26;
	int lengthStoreId = 26;
	int lengthLoyaltyNumber = 26;
	int lengthReferenceAdditional = 26;
	int lengthConsumer = 26;
	int lengthTerminal = 26;
	int lengthPurposeOfTransaction = 26;
	int lengthAdditionalConsumerDataRequest = 3;
	
	
	public PromptPayGenerate(){
		payLoadFormatIndicator  = "01";
		pointOfInitiationMethod = "11";
	
		aid = "A000000677010112";
	}

	public void generateCRC(){

		
		int SCBcrc = 0xFFFF;          // initial value
        int polynomial = 0x1021;   // 0001 0000 0010 0001  (0, 5, 12)

        System.out.println("Generate CRC : " + construct(false));

        byte[] bytes = construct(false).getBytes();

        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b   >> (7-i) & 1) == 1);
                boolean c15 = ((SCBcrc >> 15    & 1) == 1);
                SCBcrc <<= 1;
                if (c15 ^ bit) SCBcrc ^= polynomial;
            }
        }

        SCBcrc &= 0xffff;
//        System.out.println("CRC16-CCITT = " + Integer.toHexString(SCBcrc));

        this.crc = String.valueOf(Integer.toHexString(SCBcrc)).toUpperCase();
	}
	
	public String construct(boolean addCRC){
		String rev = "";
		
		if (payLoadFormatIndicator.trim().length() > 0){
			if (payLoadFormatIndicator.length() <= lengthPayLoadFormatIndicator){
				rev += "00" + getStringlength(lengthPayLoadFormatIndicator) + payLoadFormatIndicator;
			}
		}
		if (pointOfInitiationMethod.trim().length() > 0){
			if (pointOfInitiationMethod.length() <= lengthPointofInitialMethod){
				rev += "01" + getStringlength(lengthPointofInitialMethod) + pointOfInitiationMethod;
			}
		}
		if (merchantIdentifier.trim().length() > 0){
			if (merchantIdentifier.length() <= lengthMerchantIdentifier){
				rev += "30" + getStringlength(lengthMerchantIdentifier) + merchantIdentifier;
			}
		}
		if (transactionCurrencyCode.trim().length() > 0){
			if (transactionCurrencyCode.length() <= lengthTransactionCurrencyCode){
				rev += "53" + getStringlength(lengthTransactionCurrencyCode) + transactionCurrencyCode;
			}
		}
		if (transactionAmount.trim().length() > 0){
			if (transactionAmount.length() <= lengthTransactionAmount){
				rev += "54" + getStringlength(lengthTransactionAmount) + transactionAmount;
			}
		}
		if (countryCode.trim().length() > 0){
			if (countryCode.length() <= lengthCountryCode){
				rev += "58" + getStringlength(lengthCountryCode) + countryCode;
			}
		}
		if (merchantName.trim().length() > 0){
			if (merchantName.length() <= lengthMerchantName){
				rev += "59" + getStringlength(lengthMerchantName) + merchantName;
			}
		}
		if (additionalDataFieldTemplate.trim().length() > 0){
			if (additionalDataFieldTemplate.length() <= lengthAdditionalConsumerDataRequest){
				rev += "62" +getStringlength(lengthAdditionalConsumerDataRequest) + additionalDataFieldTemplate;
			}
		}
		
		if (addCRC) {
			if (crc.trim().length() > 0){
				if (crc.length() <= lengthCrc){
					rev += "63" + getStringlength(lengthCrc) + crc;
				}
			}
		} else {
			rev += "63" + getStringlength(lengthCrc);
		}
		
		return rev;
	}

	
	private String getStringlength(String data){
		String rev = "00";
		
		if (data.trim().length() < 10){
			rev = "0" + String.valueOf( data.length());
		}else {
			rev = String.valueOf(data.length());
		}
		
		return rev;
	}
	
	private String getStringlength(int intLength){
		String rev = "00";
		
		if (intLength < 10){
			rev = "0" + String.valueOf( intLength);
		}else {
			rev = String.valueOf(intLength);
		}
		
		return rev;
	}
	
	public String getFullPromptPayMsg() {
		return fullPromptPayMsg;
	}

	public void setFullPromptPayMsg(String fullPromptPayMsg) {
		this.fullPromptPayMsg = fullPromptPayMsg;
	}

	public String getPayLoadFormatIndicator() {
		return payLoadFormatIndicator;
	}

	public void setPayLoadFormatIndicator(String payLoadFormatIndicator) {
		this.payLoadFormatIndicator = payLoadFormatIndicator;
	}

	public String getPointOfInitiationMethod() {
		return pointOfInitiationMethod;
	}

	public void setPointOfInitiationMethod(String pointOfInitiationMethod) {
		this.pointOfInitiationMethod = pointOfInitiationMethod;
	}

	public String getMerchantIdentifier() {
		return merchantIdentifier;
	}

	public void setMerchantIdentifier(String ip_aid,String ip_billerID,String ip_reference1, String ip_reference2) {
		String messageID30 = "";
		
		if (ip_aid.trim().length() > 0){
			if (ip_aid.length() <= lengthAID){
				messageID30 += "00" + lengthAID + ip_aid;
			}
		}
		
		if (ip_billerID.trim().length() > 0){
			if (ip_billerID.length() <= lengthBillerID){
				messageID30 += "01" + lengthBillerID + ip_billerID;
			}
		}
		
		if (ip_reference1.trim().length() > 0){
			if(ip_reference1.length() <= lengthReference1){
				messageID30 += "02" + getStringlength(ip_reference1) + ip_reference1;
			}
		}
		
		if (ip_reference2.trim().length() > 0){
			if (ip_reference2.length() <= lengthReference2){
				messageID30 += "03" + getStringlength(ip_reference2)+ ip_reference2;
			}			
		}
		
		if (messageID30.trim().length() > 0){
			if (messageID30.length() <= lengthMerchantIdentifier){
				lengthMerchantIdentifier = messageID30.length() ;
				this.merchantIdentifier =  messageID30;	
			}
		}
	}

	public String getTransactionCurrencyCode() {
		return transactionCurrencyCode;
	}

	public void setTransactionCurrencyCode(String transactionCurrencyCode) {
		this.transactionCurrencyCode = transactionCurrencyCode;
	}

	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String ip_transactionAmount) {
		if (ip_transactionAmount.trim().length() > 0){
			if (ip_transactionAmount.length() <= lengthTransactionAmount){
				lengthTransactionAmount = ip_transactionAmount.length();
				this.transactionAmount = ip_transactionAmount;
			}
		}
		
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String ip_merchantName) {
		if (ip_merchantName.trim().length() > 0){
			if (ip_merchantName.length() <= lengthMerchantName){
				lengthMerchantName = ip_merchantName.length();
				this.merchantName = ip_merchantName;
			}
		}
		
	}

	public String getAdditionalDataFieldTemplate() {
		return additionalDataFieldTemplate;
	}

	public void setAdditionalDataFieldTemplate(
		String ip_billNumber, 
		String ip_mobileNumber ,
		String ip_storeId,
		String ip_loyaltyNumber, 
		String ip_referenceID,
		String ip_consumerID,
		String ip_terminalId, 
		String ip_purposeOfTransaction, 
		String ip_additionalConsumerDataRequest ) {
			
		String messageID62 = "";
		
		if (ip_billNumber.trim().length()> 0){
			if (ip_billNumber.length() <= lengthBillNumber){
				messageID62 += "01" + getStringlength(ip_billNumber) + ip_billNumber;
			}
		}
		if (ip_mobileNumber.trim().length() > 0){
			if(ip_mobileNumber.length() <= lengthMobileNumber){
				messageID62 += "02" + getStringlength(ip_mobileNumber) + ip_mobileNumber;
			}
		}
		if (ip_storeId.trim().length() > 0){
			if (ip_storeId.length() <= lengthStoreId){
				messageID62 += "03" + getStringlength(ip_storeId) + ip_storeId;
			}
		}
		if (ip_loyaltyNumber.trim().length() > 0){
			if (ip_loyaltyNumber.length()<= lengthLoyaltyNumber){
				messageID62 += "04" + getStringlength(ip_loyaltyNumber)  + ip_loyaltyNumber;
			}
		}
		if (ip_referenceID.trim().length() > 0){
			if (ip_referenceID.length() <= lengthReferenceAdditional) {
				messageID62 += "05" + getStringlength(ip_referenceID) + ip_referenceID;
			}
		}
		if (ip_consumerID.trim().length() > 0 ){
			if (ip_referenceID.length()<= lengthConsumer){
				messageID62 += "06" + getStringlength(ip_consumerID)+ ip_consumerID;
			}
		}
		if (ip_terminalId.trim().length()  >0){
			if(ip_terminalId.length() <= lengthTerminal){
				messageID62 += "07" + getStringlength(ip_terminalId) + ip_terminalId;
			}
		}
		if(ip_purposeOfTransaction.trim().length() > 0){
			if (ip_purposeOfTransaction.length() <= lengthPurposeOfTransaction){
				messageID62 += "08" + getStringlength(ip_purposeOfTransaction) + ip_purposeOfTransaction;
			}
		}
		if (ip_additionalConsumerDataRequest.trim().length() > 0){
			if (ip_additionalConsumerDataRequest.length() <= lengthAdditionalConsumerDataRequest){
				messageID62 += "09" + getStringlength(ip_additionalConsumerDataRequest) + ip_additionalConsumerDataRequest;
			}			
		}
		
//		if (messageID62.trim().length() > 0 ){
			if (messageID62.length() <= lengthAdditionalDataFieldTemplate){
				lengthAdditionalConsumerDataRequest = messageID62.length();
				this.additionalDataFieldTemplate =  messageID62;
			}
//		}
		
		
	}

	public String getCrc() {
		return crc;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	
	
	
}
