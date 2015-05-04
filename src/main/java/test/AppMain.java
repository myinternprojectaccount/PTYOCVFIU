package test;

import java.util.ArrayList;
import java.util.List;

import model.Pdf_Data;
import xml_to_xsl.Create_Data_Xml;

public class AppMain {
	static List<Pdf_Data> dataList=new ArrayList<Pdf_Data>();
	public static void main(String[] args)
	{
		//XmlAnalyzer analize=new XmlAnalyzer();
		//analize.analyize();
		//analize.display();
		//XslParser parser =new XslParser();
//		PdfCreator pdf=new PdfCreator();
	    try
		{
	    	Create_Data_Xml test=new Create_Data_Xml();
	    	test.create_xml();
	    	
	    	/*PrintTextLocations ptl=new PrintTextLocations();
	    	ptl.setDataList(dataList);
	    	ptl.getTextPosition();*/
//			parser.convertXslWithMatrix();
//			pdf.convertToPdf();
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
