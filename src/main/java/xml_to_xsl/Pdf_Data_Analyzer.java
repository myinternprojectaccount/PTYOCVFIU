package xml_to_xsl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import model.Data_To_Xml;
import model.Pdf_Data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import pdf_reader.PrintTextLocations;

public class Pdf_Data_Analyzer {
	private static List<Pdf_Data> dataList;
	private List<Data_To_Xml> xmlDataList;

	public Pdf_Data_Analyzer() {
		dataList = new ArrayList<Pdf_Data>();
		xmlDataList = new ArrayList<Data_To_Xml>();
	}

	public void getData() {
		try {
			// get postion datas
			PrintTextLocations ptl = new PrintTextLocations();
			ptl.getTextPosition();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
    public void create_Xml() throws Exception
    {
    	DocumentBuilderFactory docFactor = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = docFactor.newDocumentBuilder();
		Document doc = builder.newDocument();
		Element root=doc.createElement("template");
		Element page=doc.createElement("page");
		doc.appendChild(root);
		page.setAttribute("width", "100");
		page.setAttribute("height", "100");
		page.setAttribute("margin-left", "5");
		page.setAttribute("margin-top", "10");
		root.appendChild(page);
		int temp=0;
		for(int i=0;i<xmlDataList.size();i++)
		{
			//we use block definition inside xml for the word which got from pdf file.
			//if data's have same y coordinate value,this datas are same line.
			if(i==0)
			{
				//margin hesaplamasi
				long left;
				long top;
				left=Math.round(xmlDataList.get(i).getX1());
				top=Math.round(xmlDataList.get(i).getY1());
				temp=i;
				
				Element element=doc.createElement("block");
				element.setAttribute("margin-left",String.valueOf(left));
				element.setAttribute("margin-top",String.valueOf(top));
				element.setAttribute("width",String.valueOf(Math.round(xmlDataList.get(i).getWidth())));
				element.setAttribute("font-size",String.valueOf(Math.round(dataList.get(i).getFont_size())));
				element.setAttribute("font-family",dataList.get(i).getFont());
				element.setAttribute("id", String.valueOf(i));
				element.setAttribute("flow", "bottom");
				element.appendChild(doc.createTextNode(xmlDataList.get(i).getData()));
				root.appendChild(element);
				continue;
			}
			if(i==xmlDataList.size()-1)
			{
				long left;
				long top;
				if(String.valueOf(xmlDataList.get(i).getY1()).equals(String.valueOf(xmlDataList.get(i).getY1())))
				{
					left=Math.round(xmlDataList.get(i).getX1()-(xmlDataList.get(i-1).getX1()+xmlDataList.get(i-1).getWidth()));
					top=Math.round(dataList.get(i).getY()-xmlDataList.get(temp).getY1());
				}
				else
				{
					left=Math.round(xmlDataList.get(i).getX1());
					top=Math.round(dataList.get(i).getY()-xmlDataList.get(temp).getY1());
				}
				left=Math.round(xmlDataList.get(i).getX1());
				top=Math.round(xmlDataList.get(i).getY1()-xmlDataList.get(i-1).getY1());//hop bidakka:D onceki hesaplama
				Element element=doc.createElement("block");//burda ince hesap yapmak lazims
				element.setAttribute("margin-left",String.valueOf(left));
				element.setAttribute("margin-top",String.valueOf(top));
				element.setAttribute("width",String.valueOf(dataList.get(i).getWidth()));
				element.setAttribute("font-size",String.valueOf(Math.round(dataList.get(i).getFont_size())));
				element.setAttribute("font-family",dataList.get(i).getFont());
				element.setAttribute("id", String.valueOf(i));
				element.setAttribute("flow", "bottom");
				element.appendChild(doc.createTextNode(xmlDataList.get(i).getData()));
				root.appendChild(element);
				
				break;
			}
			if(String.valueOf(xmlDataList.get(i).getY1()).equals(String.valueOf(xmlDataList.get(i+1).getY1())))
			{
				int value=0;
				//i ve i+1 y degerleri esit ise bunlar ayni satirdadir.
				if(temp-1<0)
				{
					value=0;
				}
				else if(temp==0)
				{
					value=temp-1;
				}
				long left;
				long top;
				System.out.println("value:"+value);
				top=Math.round(dataList.get(i).getY()-xmlDataList.get(value).getY1());
				left=Math.round(xmlDataList.get(i).getX1()-(xmlDataList.get(i-1).getX1()+xmlDataList.get(i-1).getWidth()));
				Element element=doc.createElement("block");
				element.setAttribute("margin-left",String.valueOf(left));
				element.setAttribute("margin-top",String.valueOf(top));
				element.setAttribute("width",String.valueOf(dataList.get(i).getWidth()));
				element.setAttribute("font-size",String.valueOf(Math.round(dataList.get(i).getFont_size())));
				element.setAttribute("font-family",dataList.get(i).getFont());
				element.setAttribute("id", String.valueOf(i));
				element.setAttribute("flow", "near");
				element.setAttribute("flow-ref",String.valueOf(temp));
				element.appendChild(doc.createTextNode(xmlDataList.get(i).getData()));
				root.appendChild(element);
				
			}
			else
			{
				temp=i;
				Element element=doc.createElement("block");
				element.setAttribute("margin-left",String.valueOf(xmlDataList.get(i).getX1()));
				element.setAttribute("margin-top",String.valueOf(Math.round(xmlDataList.get(i).getY1()-xmlDataList.get(i).getY1())));
				element.setAttribute("width",String.valueOf(xmlDataList.get(i).getWidth()));
				element.setAttribute("font-size", String.valueOf(Math.round(dataList.get(i).getFont_size())));
				element.setAttribute("font-family",dataList.get(i).getFont());
				element.setAttribute("id", String.valueOf(i));
				element.setAttribute("flow", "bottom");
				element.appendChild(doc.createTextNode(xmlDataList.get(i).getData()));
				root.appendChild(element);
			}
			
		}
		
		//olusan xml dosyasini yazdirma
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(
				"C:\\games\\olusan.xml").getPath());

		transformer.transform(source, result);

		System.out.println("islem basarili...");
    }
	public void dataList_guncelle()
	{
		//this method recalculate the x and y coordinate bucause if we dont this 
		//if else statement run wrongly.
		for (Pdf_Data temp:dataList)
		{
			String []x=String.valueOf(temp.getX()).split("\\.");
			String []y=String.valueOf(temp.getY()).split("\\.");
			String []w=String.valueOf(temp.getWidth()).split("\\.");
		    int deger=0;
			int deger2=0;
			int deger3=0;
			
			deger=Integer.parseInt(String.valueOf(x[1].charAt(0)));
			deger2=Integer.parseInt(String.valueOf(y[1].charAt(0)));
			deger3=Integer.parseInt(String.valueOf(w[1].charAt(0)));
			if(deger>=5)
			{
				temp.setX(Double.valueOf(Integer.parseInt(x[0]))+1);
			}
			else if(deger<5)
			{
				temp.setX(Double.valueOf(Integer.parseInt(x[0])));
			}
			if(deger3>=5)
			{
				temp.setWidth(Double.valueOf(Integer.parseInt(w[0]))+1);
			}
			else if(deger<5)
			{
				temp.setWidth(Double.valueOf(Integer.parseInt(w[0])));
			}
		}
	}
	public void setXmlData() {
		StringBuilder stb = new StringBuilder();
		Data_To_Xml dtx=new Data_To_Xml();
		Pdf_Data temp=new Pdf_Data();
		dataList_guncelle();
		for (int i = 0; i < dataList.size(); ++i) {
			if(i==0)
			{
				temp=dataList.get(0);
			}
			if(i==dataList.size()-1)
			{
				stb.append(dataList.get(i).getC_data());
				dtx.setX1(temp.getX());
				dtx.setY1(temp.getY());
				dtx.setFont(temp.getFont());
				dtx.setFont_size(temp.getFont_size());
				dtx.setHeight(temp.getHeight());
				dtx.setWidth(dataList.get(i).getX()-temp.getX());
				dtx.setData(stb.toString());
				xmlDataList.add(dtx);
				break;
				
			}
			if(String.valueOf(dataList.get(i).getY()).equals(String.valueOf(dataList.get(i+1).getY())))
			{
				
				
				Double veri=dataList.get(i+1).getX()-dataList.get(i).getX();
				if(veri<=dataList.get(i).getWidth())
				{
					stb.append(dataList.get(i).getC_data());
									
				}
				else
				{
					stb.append(dataList.get(i).getC_data());
					dtx.setX1(temp.getX());
					dtx.setY1(temp.getY());
					dtx.setFont(temp.getFont());
					dtx.setFont_size(temp.getFont_size());
					dtx.setHeight(temp.getHeight());
					dtx.setWidth(dataList.get(i).getX()-temp.getX());
					dtx.setData(stb.toString());
					xmlDataList.add(dtx);
					dtx=new Data_To_Xml();
					temp=dataList.get(i+1);
					stb=new StringBuilder();
				}
			}
			else
			{
				//no same line
				stb.append(dataList.get(i).getC_data());
				dtx.setX1(temp.getX());
				dtx.setY1(temp.getY());
				dtx.setFont(temp.getFont());
				dtx.setFont_size(temp.getFont_size());
				dtx.setHeight(temp.getHeight());
				dtx.setWidth(dataList.get(i).getX()-temp.getX());
				dtx.setData(stb.toString());
				xmlDataList.add(dtx);
				dtx=new Data_To_Xml();
				temp=dataList.get(i+1);
				stb=new StringBuilder();
				
			}
		}

	}

	public List<Pdf_Data> getDataList() {
		return dataList;
	}

	public void setDataList(List<Pdf_Data> dataList) {
		this.dataList = dataList;
	}

	public List<Data_To_Xml> getXmlDataList() {
		return xmlDataList;
	}

	public void setXmlDataList(List<Data_To_Xml> xmlDataList) {
		this.xmlDataList = xmlDataList;
	}

}
