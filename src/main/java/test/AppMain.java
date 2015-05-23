package test;

import java.util.ArrayList;
import java.util.List;

import model.Pdf_Data;
import xml_to_xsl.Create_Data_Xml;
import xml_to_xsl.PdfCreator;
import xml_to_xsl.XslParser;

public class AppMain {
	static List<Pdf_Data> dataList=new ArrayList<Pdf_Data>();
	public static void main(String[] args)
	{
		XslParser parser =new XslParser();
		PdfCreator pdf=new PdfCreator();
	    try
		{
	    	Create_Data_Xml test=new Create_Data_Xml();
	    	test.create_xml();
	    	parser.convertXslFromXMLSentence();;
			pdf.convertToPdf();			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static List<Pdf_Data> getDataList() {
		return dataList;
	}
	public static void setDataList(List<Pdf_Data> dataList) {
		AppMain.dataList = dataList;
	}
	
	


}
