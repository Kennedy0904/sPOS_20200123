package com.example.dell.smartpos;

public class Record {
  public String paymentRef;
  public String merchantRef;
  public String time;
  public String amount;
  public String Surcharge;
  public String merRequestAmt;

  public String currCode;
  public String remark;
  public String status;
  public String merName;
  public String payMethod;
  public String payType;
  public String payBankId;
  public String cardNo;
  public String cardHolder;
  public int currentPosition;

  public String orderdate;
  public String payref;
  public String merref;
  public String totalpage;
  public String amt;
  public String orderstatus;

  //public String accountNo;
  public String currency;
  public String settle;

  public String batchNo;
  public String settleTime;
  public String invoiceNo;
  public String traceNo;
  public String bankId;

  public Record() {
    super();
  }

  public Record(String paymentRef,
                String merchantRef,
                String orderdate,
                String currency,
                String amount,
                String Surcharge,
                String merRequestAmt,
                String remark,
                String status,
                String merName,
                String payType,
                String payMethod,
                String payBankId,
                String cardNo,
                String cardHolder,
                String settle,
                String bankId,
                String settleTime,
                String batchNo,
                String traceNo,
                String invoiceNo) {
    super();
    this.paymentRef = paymentRef;
    this.merName = merName;
    this.merchantRef = merchantRef;
    this.orderdate = orderdate;
    this.currency = currency;
    this.amt = amount;
    this.Surcharge = Surcharge;
    this.merRequestAmt = merRequestAmt;
    this.remark = remark;
    this.status = status;
    this.payMethod = payMethod;
    this.payType = payType;
    this.payBankId = payBankId;
    this.cardNo = cardNo;
    this.cardHolder = cardHolder;
    this.payref = paymentRef;
    this.settle = settle;
    this.merref = merchantRef;
    this.bankId = bankId;
    this.settleTime = settleTime;
    this.batchNo = batchNo;
    this.traceNo = traceNo;
    this.invoiceNo = invoiceNo;
  }

  public String getBankId() {
    return bankId;
  }

  public void setBankId(String bankId) {
    this.bankId = bankId;
  }

  public String getPayType() {
    return payType;
  }

  public void setPayType(String payType) {
    this.payType = payType;
  }

  public String getPaymethod() {
    return payMethod;
  }

  public void setPaymethod(String paymethod) {
    this.payMethod = paymethod;
  }

  public String getPayBankId() {
    return payBankId;
  }

  public void setPayBankId(String payBankId) {
    this.payBankId = payBankId;
  }

  public String getMerName() {
    return merName;
  }

  public void setMerName(String merName) {
    this.merName = merName;
  }

  public String getPaymentRef() {
    return paymentRef;
  }

  public void setPaymentRef(String paymentRef) {
    this.paymentRef = paymentRef;
  }

  public String getMerchantRef() {
    return merchantRef;
  }

  public void setMerchantRef(String merchantRef) {
    this.merchantRef = merchantRef;
  }

  public String gettime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getCurrCode() {
    return currCode;
  }

  public void setCurrCode(String currCode) {
    this.currCode = currCode;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getSurcharge() {
    return Surcharge;
  }

  public void setSurcharge(String Surcharge) {
    this.Surcharge = Surcharge;
  }

  public String getMerRequestAmt() {
    return merRequestAmt;
  }

  public void setMerRequestAmt(String merRequestAmt) {
    this.merRequestAmt = merRequestAmt;
  }

  public String remark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String status() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String payMethod() {
    return payMethod;
  }

  public void getPayMethod(String payMethod) {
    this.payMethod = payMethod;
  }

  public String getCardNo() {
    return cardNo;
  }

  public void setCardNo(String cardNo) {
    this.cardNo = cardNo;
  }

  public void setCardHolder(String cardholder) {
    this.cardHolder = cardholder;
  }

  public String getCardHolder() {
    return cardHolder;
  }

  public String getOrderdate() {
    return orderdate;
  }

  public void setOrderdate(String orderdate) {
    this.orderdate = orderdate;
  }

  public String PayRef() {
    return payref;
  }

  public void setPayRef(String payref) {
    this.payref = payref;
  }

  public String merref() {
    return merref;
  }

  public void setMerRef(String merref) {
    this.merref = merref;
  }

  public String totalpage() {
    return totalpage;
  }

  public void getTotalpage(String totalpage) {

    this.totalpage = totalpage;
  }

  public String getamt() {
    return amt;
  }

  public void setAmt(String amt) {
    this.amt = amt;
  }

  public String orderstatus() {
    return orderstatus;
  }

  public void getOrderStatus(String orderstatus) {
    this.orderstatus = orderstatus;
  }

  public String currency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public void getRemark(String remark) {
    this.remark = remark;
  }

  public void setSettle(String settle) {
    this.settle = settle;
  }

  public String getSettle() {
    return settle;
  }

  public void setBatchNo(String batchNo) {
    this.batchNo = batchNo;
  }

  public String getBatchNo() {
    return batchNo;
  }

  public void setSettleTime(String settleTime) {
    this.settleTime = settleTime;
  }

  public String getSettleTime() {
    return settleTime;
  }

  public void setInvoiceNo(String invoiceNo) {
    this.invoiceNo = invoiceNo;
  }

  public String getInvoiceNo() {
    return invoiceNo;
  }

  public void setTraceNo(String traceNo) {
    this.traceNo = traceNo;
  }

  public String getTraceNo() {
    return traceNo;
  }

  @Override
  public String toString() {
    return this.amount;
  }

}

