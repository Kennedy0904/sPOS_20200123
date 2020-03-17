package com.example.dell.smartpos;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class XML2Record extends DefaultHandler {
    private List<Record> records;
    private Record record;
    private String totalpage;

    private StringBuffer buffer = new StringBuffer();

    public List<Record> getRecords() {
        return records;
    }

    public String gettotalPage() {
        return totalpage;
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        buffer.append(ch, start, length);
        super.characters(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (localName.equals("record")) {
            records.add(record);
        } else if (localName.equals("records")) {
            record.getTotalpage(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("payType")) {
            record.setPayType(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("paymethod")) {
            record.setPaymethod(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("bank")) {
            record.setPayBankId(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("orderdate")) {
            record.setOrderdate(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("cardholder")) {
            record.setCardHolder(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("payref")) {
            record.setPayRef(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("merref")) {
            record.setMerRef(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("amt")) {
            record.setAmt(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("Surcharge")) {
            record.setSurcharge(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("merRequestAmt")) {
            record.setMerRequestAmt(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("orderstatus")) {
            record.getOrderStatus(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("accountno")) {
            record.setCardNo(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("cur")) {
            record.setCurrency(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("currency")) {
            record.setCurrency(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("remark")) {
            record.getRemark(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("settle")) {
            record.setSettle(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("invoiceNo")) {
            record.setInvoiceNo(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("batchNo")) {
            record.setBatchNo(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("traceNo")) {
            record.setTraceNo(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("settleTime")) {
            record.setSettleTime(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("bankid")) {
            record.setBankId(buffer.toString().trim());
            buffer.setLength(0);
        } else if (localName.equals("settleTime")) {
            record.setSettleTime(buffer.toString().trim());
            buffer.setLength(0);
        }
        super.endElement(uri, localName, qName);
    }

    @Override
    public void startDocument() throws SAXException {
        records = new ArrayList<Record>();
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        if (localName.equals("record")) {
            record = new Record();
        } else if (localName.equals("records")) {
            totalpage = attributes.getValue("total");
        }
        super.startElement(uri, localName, qName, attributes);
    }
}

