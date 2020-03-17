package com.example.dell.smartpos;

import java.util.ArrayList;
import java.util.TreeMap;

public interface ReportInterface {

    public void setValues(TreeMap<String, String> report, ArrayList<Record> ALIPAYHKOFFLrecords,
						  ArrayList<Record> ALIPAYCNOFFLrecords,
						  ArrayList<Record> ALIPAYOFFLrecords,
						  ArrayList<Record> BOOSTOFFLrecords,
						  ArrayList<Record> GCASHOFFLrecords,
						  ArrayList<Record> GRABPAYOFFLrecords,
						  ArrayList<Record> masterRecords,
						  ArrayList<Record> OEPAYOFFLrecords,
						  ArrayList<Record> PROMPTPAYOFFLrecords,
						  ArrayList<Record> UNIONPAYOFFLrecords,
						  ArrayList<Record> visaRecords,
						  ArrayList<Record> WECHATOFFLrecords,
						  ArrayList<Record> WECHATHKOFFLrecords,
						  ArrayList<Record> WECHATONLrecords);
}
