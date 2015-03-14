package test;

import java.util.ArrayList;
import java.util.List;

import xml_to_xsl.PdfCreator;
import xml_to_xsl.XslParser;

public class AppMain {
	public static void main(String[] args)
	{
		//XmlAnalyzer analize=new XmlAnalyzer();
		//analize.analyize();
		//analize.display();
		XslParser parser =new XslParser();
		PdfCreator pdf=new PdfCreator();
	    try
		{
			parser.convertToXsl();
			pdf.convertToPdf();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		List<String> liste=new ArrayList<String>();
		liste.add("3");
		liste.add("4");
		liste.add("5");
		for(String s:liste)
		{
			System.out.println(s);
		}
	}
	


}
