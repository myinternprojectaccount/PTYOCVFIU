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
		//XmlAnalyzer analize=new XmlAnalyzer();
		//analize.analyize();
		//analize.display();
		XslParser parser =new XslParser();
		PdfCreator pdf=new PdfCreator();
	    try
		{
	    	Create_Data_Xml test=new Create_Data_Xml();
	    	test.create_xml();
	    	parser.convertXslFromXML();
			pdf.convertToPdf();
//	    	Double doub=new Double(4.45526892232322);
//			Double dou=new Double(4.45526892232322);
//			long sonuc=doub.compareTo(dou);
//			if(sonuc==0)
//			{
//				System.out.println("esit degerler");
//			}else
//			{
//				System.out.println("esit degil");
//			}
	    	/*PrintTextLocations ptl=new PrintTextLocations();
	    	ptl.setDataList(dataList);
	    	ptl.getTextPosition();*/
			
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
