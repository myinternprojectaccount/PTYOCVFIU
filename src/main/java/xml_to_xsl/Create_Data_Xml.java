package xml_to_xsl;

import java.util.ArrayList;
import java.util.List;

import model.Data_To_Xml;

public class Create_Data_Xml {

	private List<Data_To_Xml> dataList;
	
	public Create_Data_Xml()
	{
		dataList=new ArrayList<Data_To_Xml>();
	}
	public void create_xml()
	{
		Pdf_Data_Analyzer	da=new Pdf_Data_Analyzer();
		da.getData();
		da.setXmlDataPerSentence();
		try
		{
			da.create_Xml_Sentence();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
//		System.out.println(da.getXmlDataList().size());
//		for(Data_To_Xml d:da.getXmlDataList())
//		{
//			System.out.println(d.getData()+"   " +d.getX1() + "   " + d.getY1() +"  "+d.getWidth());
//		}
	}
}
