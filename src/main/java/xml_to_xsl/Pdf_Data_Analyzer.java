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
		int satir=0; //bu deger yan yana olacak blocklar icin margin top hesabinda kullanilacaktir.
		//Bu fonksiyon datalari analiz eder her kelime bir block mantigi ile islem yapilmak istenmektedir.
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
				temp=i-1;
				
				Element element=doc.createElement("block");
				element.setAttribute("margin-left",String.valueOf(left));
				//element.setAttribute("margin-top",String.valueOf(top));
				element.setAttribute("width",String.valueOf(Math.round(xmlDataList.get(i).getWidth())));
				element.setAttribute("font-size",String.valueOf(Math.round(xmlDataList.get(i).getFont_size())));
				element.setAttribute("font-family",xmlDataList.get(i).getFont());
				element.setAttribute("id", String.valueOf(i+1));
				element.setAttribute("flow", "bottom");
				element.appendChild(doc.createTextNode(xmlDataList.get(i).getData()));
				element.setAttribute("color", xmlDataList.get(i).getColor());
				root.appendChild(element);
				continue;
			}
			//y degerleri esit ise ayni satirda olmasi gereken blocklardir.
			long a,b;
			a=Math.round(xmlDataList.get(i).getY1());
			b=Math.round(xmlDataList.get(i-1).getY1());
			if(a==b)//y degerleri ayni
			{
				
				long left=0;
				long top=0;
				//i ve i+1 y degerleri esit ise bunlar ayni satirdadir.
				if(temp<0)
				{
					//hala ilk satirda dolasiyoruz
					top=Math.round(dataList.get(temp+1).getY());
					left=Math.round(xmlDataList.get(i).getX1()-(xmlDataList.get(i-1).getX1()+xmlDataList.get(i-1).getWidth()));
				}
				else if(temp>0)
				{
					
					//artik ilk satirda degiliz ve margin top bir onceki y degeri ile fark olarak hesaplanmali...
					top=Math.round(xmlDataList.get(i).getY1()-xmlDataList.get(temp-1).getY1());
					left=Math.round(xmlDataList.get(i).getX1()-(xmlDataList.get(i-1).getX1()+xmlDataList.get(i-1).getWidth()));
				}
				else if(temp==0)
				{
					top=Math.round(xmlDataList.get(i).getY1()-xmlDataList.get(temp).getY1());
					left=Math.round(xmlDataList.get(i).getX1()-(xmlDataList.get(i-1).getX1()+xmlDataList.get(i-1).getWidth()));
				}
				Element element=doc.createElement("block");
				element.setAttribute("margin-left",String.valueOf(left));
				//element.setAttribute("margin-top",String.valueOf(top));
				element.setAttribute("width",String.valueOf(Math.round(xmlDataList.get(i).getWidth())));
				element.setAttribute("font-size",String.valueOf(Math.round(xmlDataList.get(i).getFont_size())));
				element.setAttribute("font-family",xmlDataList.get(i).getFont());
				element.setAttribute("id", String.valueOf(i+1));
				element.setAttribute("flow", "near");
				element.setAttribute("flow-ref",String.valueOf(temp+1));
				element.appendChild(doc.createTextNode(xmlDataList.get(i).getData()));
				element.setAttribute("color", xmlDataList.get(i).getColor());
				root.appendChild(element);
				
			}
			else
			{
				temp=i-1;
				Element element=doc.createElement("block");
				element.setAttribute("margin-left",String.valueOf(Math.round(xmlDataList.get(i).getX1())));
				//element.setAttribute("margin-top",String.valueOf(Math.round(xmlDataList.get(i).getY1()-xmlDataList.get(temp).getY1())));
				element.setAttribute("width",String.valueOf(Math.round(xmlDataList.get(i).getWidth())));
				element.setAttribute("font-size", String.valueOf(Math.round(xmlDataList.get(i).getFont_size())));
				element.setAttribute("font-family",xmlDataList.get(i).getFont());
				element.setAttribute("id", String.valueOf(i+1));
				element.setAttribute("flow", "bottom");
				element.setAttribute("color", xmlDataList.get(i).getColor());
				element.appendChild(doc.createTextNode(xmlDataList.get(i).getData()));
				root.appendChild(element);
			}
			
		}
		
		//olusan xml dosyasini yazdirma
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(
				"/home/volkan/bitirme/xml/pdf_to_xml.xml").getPath());

		transformer.transform(source, result);

		System.out.println("islem basarili...");
    }
    public void create_Xml_Sentence() throws Exception
    {
    	DocumentBuilderFactory docFactor = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = docFactor.newDocumentBuilder();
		Document doc = builder.newDocument();
		Element root=doc.createElement("template");
		Element page=doc.createElement("page");
		doc.appendChild(root);
		page.setAttribute("width" ,String.valueOf(xmlDataList.get(0).getPage_width()));
		page.setAttribute("height",String.valueOf(xmlDataList.get(0).getPage_height()));
		root.appendChild(page);
		//ayni satira sahip elemanlar block olarak eklenmektedir.
		
		for(int i=0;i<xmlDataList.size();i++)
		{
			Element element=doc.createElement("block");
			if(i==0)
			{
				element.setAttribute("margin-top",String.valueOf(Math.round(xmlDataList.get(i).getY1())));
			}
			else
			{
				element.setAttribute("margin-top",String.valueOf(Math.round(xmlDataList.get(i).getY1()-xmlDataList.get(i-1).getY1())));
			}
			element.setAttribute("margin-left",String.valueOf(Math.round(xmlDataList.get(i).getX1())));
			element.setAttribute("width",String.valueOf(Math.round(xmlDataList.get(i).getWidth())));
			element.setAttribute("font-size",String.valueOf(Math.round(xmlDataList.get(i).getFont_size())));
			element.setAttribute("font-family",xmlDataList.get(i).getFont());
			element.setAttribute("id", String.valueOf(i+1));
			element.appendChild(doc.createTextNode(xmlDataList.get(i).getData()));
			element.setAttribute("color", xmlDataList.get(i).getColor());
			root.appendChild(element);
		}
		//xml olarak yazdir.
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(
				"/home/volkanavci/bitirme/xml/pdf_to_xml.xml").getPath());

		transformer.transform(source, result);

		System.out.println("islem basarili...");
    }
	public void setXmlDataPerWords() {
		StringBuilder stb = new StringBuilder();
		Data_To_Xml dtx=new Data_To_Xml();
		Pdf_Data temp=new Pdf_Data();
		for (int i = 0; i < dataList.size(); ++i) {
			if(i==0)
			{
				temp=dataList.get(0);
			}
			if(i==dataList.size()-1)
			{
				stb.append(dataList.get(i).getC_data());
				dtx.setX1(temp.getX());
				dtx.setColor("rgb("+String.valueOf(temp.getR())+","+String.valueOf(temp.getG())+","+temp.getB()+")");
				dtx.setY1(temp.getY());
				dtx.setFont(temp.getFont());
				dtx.setFont_size(temp.getFont_size());
				dtx.setHeight(temp.getHeight());
				dtx.setWidth(dataList.get(i).getX()-temp.getX());
				dtx.setData(stb.toString());
				xmlDataList.add(dtx);
				break;
				
			}
			long a,b;
			a=Math.round(dataList.get(i).getY());
			b=Math.round(dataList.get(i+1).getY());
			if(a==b)
			{
				
				
				Double veri=dataList.get(i+1).getX()-dataList.get(i).getX();
				long veri1,veri2;
				long veri3 = 0,veri4 = 0;
				veri1=Math.round(veri);
				veri2=Math.round(dataList.get(i).getWidth());
				veri3=Math.round(dataList.get(i).getX()+dataList.get(i).getSpace_width());
				veri4=Math.round(dataList.get(i+1).getX());
				if(veri1==veri2)
				{
					stb.append(dataList.get(i).getC_data());
									
				}
				else if(veri3==veri4)
				{
					System.out.println("space kosuluna girdi...");
					//space karakteri varsa arada ne yapacagiz.
					stb.append(dataList.get(i).getC_data());
					dtx.setX1(temp.getX());
					dtx.setY1(temp.getY());
					dtx.setFont(temp.getFont());
					dtx.setFont_size(temp.getFont_size());
					dtx.setHeight(temp.getHeight());
					dtx.setWidth(dataList.get(i).getX()-temp.getX());
					dtx.setData(stb.toString());
					dtx.setColor("rgb("+String.valueOf(temp.getR())+","+String.valueOf(temp.getG())+","+temp.getB()+")");
					xmlDataList.add(dtx);
					dtx=new Data_To_Xml();
					temp=dataList.get(i+1);
					stb=new StringBuilder();
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
					dtx.setColor("rgb("+String.valueOf(temp.getR())+","+String.valueOf(temp.getG())+","+temp.getB()+")");
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
				dtx.setColor("rgb("+String.valueOf(temp.getR())+","+String.valueOf(temp.getG())+","+temp.getB()+")");
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
	public void setXmlDataPerSentence()
	{
		//kelime kelime ayirma isleminde istenilen tasarimsal olusum saglanamiyor 
		//ikinci yontem olarak cumle cumle analiz yapip bu sekilde xsl dokumani olusturmayi
		//hedeflemekteyiz...
		
		StringBuilder stb = new StringBuilder();
		Data_To_Xml dtx=new Data_To_Xml();
		Pdf_Data temp=new Pdf_Data();
		for(int i=0;i<dataList.size();i++)
		{	
			//y degerleri esit olan harfler ayni satirda bulunmaktadir.
			if(i==0)
			{
				temp=dataList.get(0);
			}
			if(i==dataList.size()-1)
			{
				stb.append(dataList.get(i).getC_data());
				dtx.setX1(temp.getX());
				dtx.setColor("rgb("+String.valueOf(temp.getR())+","+String.valueOf(temp.getG())+","+temp.getB()+")");
				dtx.setY1(temp.getY());
				dtx.setFont(temp.getFont());
				dtx.setFont_size(temp.getFont_size());
				dtx.setHeight(temp.getHeight());
				dtx.setWidth(dataList.get(i).getX()-temp.getX());
				dtx.setData(stb.toString());
				dtx.setPage_height(dataList.get(0).getPage_height());
				dtx.setPage_width(dataList.get(0).getPage_width());
				xmlDataList.add(dtx);
				break;
				
			}
			// y degerleri karsilastirilmaktadir...
			long a,b;
			a=Math.round(dataList.get(i).getY());
			b=Math.round(dataList.get(i+1).getY());
			if(a==b)
			{
				// y degerleri esit oldukca harfler ayni cumleye eklenir.
				stb.append(dataList.get(i).getC_data());
				
			}
			else
			{
				//y degerleri esit degilse bu cumle data olarak alinir ve temp degeri satir atlar...
				stb.append(dataList.get(i).getC_data());
				dtx.setX1(temp.getX());
				dtx.setY1(temp.getY());
				dtx.setFont(temp.getFont());
				dtx.setFont_size(temp.getFont_size());
				dtx.setHeight(temp.getHeight());
				dtx.setWidth(dataList.get(i).getX()-temp.getX());
				dtx.setData(stb.toString());
				dtx.setColor("rgb("+String.valueOf(temp.getR())+","+String.valueOf(temp.getG())+","+temp.getB()+")");
				dtx.setR(temp.getR());
				dtx.setG(temp.getG());
				dtx.setB(temp.getB());
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
