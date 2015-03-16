package xml_to_xsl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import model.Block;
import model.Context;
import model.Header;
import model.Page;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlAnalyzer {
	private List<Header> headerList;
	private List<Context> contextList;
	private List<Block> blockList;
	private HashMap<String,String> bodyAttributes;
	private Page page;
	private Block[][] template_matrisi;
	private int satir,sutun;

	public XmlAnalyzer() {
		headerList = new ArrayList<Header>();
		contextList = new ArrayList<Context>();
		blockList=new ArrayList<Block>();
		bodyAttributes=new HashMap<String,String>();
		page = new Page();
	}

	public void tasarimMatrisiHazirla()
	{
		//xml analizinden sonra gelen verilere gore tasarim matrisi olsuturulmasi
		//matris yanyana gelen bloklarin icinden en cok yanyana gelenlerin sayisina gore sutun
		//elde flow=bottom olanlarin sayisi kadar satir sayisi icermektedir.
		satir=0;sutun=0;
		//satir sayisi bulma
		for(Block b:blockList)
		{
			if(b.getFlow().toString().equals("first") || b.getFlow().toString().toUpperCase().equals("BOTTOM"))
			{
				++satir;
				
			}
		}
		//sutun sayisi bulma
		for(Block b:blockList)
		{
			if(b.getType().toString().toUpperCase().equals("HEADER"))
			{
				if(sutun<b.getKomsular().size())
				{
					sutun=b.getKomsular().size();
				}
			}
		}
		sutun+=1;
		System.out.println(satir+"ve"+sutun);
		template_matrisi=new Block[satir][sutun];
		//template matrisine tasarim sablonu atamasi yapilmakta satir ve sutun eleman iceriyorsa 1 icermiyorsa 0 olur.
		//blok listesinde bulunan block nesneleri matrise uygun bicimde yerlestirilir.
		//once satirlari dolduralim
		int temp=0;
		for(Block b:blockList)
		{
			if(b.getFlow().toString().toUpperCase().equals("BOTTOM"))
			{
				template_matrisi[temp][0]=b;
				++temp;
				//burda matris asimi olabilir dikkatlice guncellenecek
			}
		}
		//satirlara eklenen blocklarin komsulari varsa onlarda ayni satira ekleniyor.
		for(int i=0;i<satir;i++)
		{
			if(template_matrisi[i][0].getKomsular().size()>0)
			{
				int k=0;
				while(k<template_matrisi[i][0].getKomsular().size()) //satirlara atanmis blocklarin komsuluk dizisine bakip varsa 
					//ayni satirda olacak sekilde ekliyoruz
				{
					for(Block t:blockList)
					{
						if(t.getBlock_id()==Integer.parseInt(template_matrisi[i][0].getKomsular().get(k)))
						{
							template_matrisi[i][k+1]=t;
						}
					}
					++k;
				}
			}
		}
		//template matrisi olusturuldu...Bundan sonra template matrisi kullanarak Xsl donusumu yapilacaktir
		//template matrisi kullanmanin amaci sayfanin kaymadan verilen genislik degerlerine gore uyumlu sekilde 
		//olusturlmasinda referans olmasidir.
		//template matrisine gore hem xsl hem de veri giris formu hazirlanacaktir.
	}
	public void komsulukHesaplama()
	{
		for(Block b:blockList)
		{
			List<String> temp=new ArrayList<String>();
			for(Block t:blockList)
			{
				if(b.getBlock_id()==t.getFlow_ref_id())
				{
					temp.add(String.valueOf(t.getBlock_id()));
				}
			}
			b.setKomsular(temp);
		}
	}
	public void analyize() {
		try {
			File xml_file = new File("/home/volkan/bitirme/xml/template.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = dbFactory.newDocumentBuilder();
			Document doc = builder.parse(xml_file);

			// optional
			doc.getDocumentElement().normalize();

			// elementler alinmaya baslandi...

			// page node header nodes and context nodes
			// page node
			 analyizePage(doc);
			 analyzeBody(doc);
			 analyzeBlock(doc);
			 komsulukHesaplama();
			 tasarimMatrisiHazirla();
			 
			 //analyizeHeader(doc);
			 //contextAnalyize(doc);
			//test(doc);

		} catch (Exception ex) {
			//attribute olarak alinan verilerin xsl de tanimlanirken hata olusturmamasi kontrolunu saglamak gerekli.
			//header ve context in yanyana oldugu tasarim modellemesi
			//header ve contextin yan yana durumu header ........
								    // ........

		}
	}

	public void analyizePage(Document doc) throws Exception {
		
		NodeList nList=doc.getElementsByTagName("page");
		for(int i=0;i<nList.getLength();i++)
		{
			Node pNode=nList.item(i);
		    page=new Page();
			if(pNode.getNodeType()==Node.ELEMENT_NODE)
			{
				if(pNode.hasAttributes())
				{
					NamedNodeMap nodeMap=pNode.getAttributes();
					for(int j=0;j<nodeMap.getLength();j++)
					{
						Node node=nodeMap.item(j);
						page.getProperties().put(node.getNodeName(), node.getNodeValue());
					}
					
				}
				
			}
		}//analiz bitti
	}

	public void analyizeHeader(Document doc) throws Exception {
		NodeList hList = doc.getElementsByTagName("header");
		for (int i = 0; i < hList.getLength(); i++) {
			Node hNode = hList.item(i);
			Header header = new Header();

			if (hNode.getNodeType() == Node.ELEMENT_NODE) {
				
				if(hNode.hasAttributes()) 
				{
					NamedNodeMap hNodeMap=hNode.getAttributes();
					for(int j=0;j<hNodeMap.getLength();j++)
					{
						Node node=hNodeMap.item(j);
						//ozellikler hashMap al
						header.getProperties().put(node.getNodeName(), node.getNodeValue());
					}
				}
				if(hNode.hasChildNodes())
				{
					NodeList hChild=hNode.getChildNodes();
					for(int k=0;k<hChild.getLength();k++)
					{
						Node headerChild=hChild.item(k);
						if(headerChild.getNodeType()==Node.ELEMENT_NODE)
						{
							
							//child data attribute
							header.setData(headerChild.getTextContent());
							
						}//bit bucket tayiz
					}
				}
				headerList.add(header);
			}
			
		}

	}

	public void contextAnalyize(Document doc) throws Exception {
		NodeList cList = doc.getElementsByTagName("context");
		for (int i = 0; i < cList.getLength(); i++) {
			
			Node cNode = cList.item(i);
			Context context = new Context();
			
			if (cNode.getNodeType() == Node.ELEMENT_NODE) {
				if(cNode.hasAttributes())
				{
					NamedNodeMap nodeMap=cNode.getAttributes();
					for(int j=0;j<nodeMap.getLength();j++)
					{
						Node node=nodeMap.item(j);
						context.getProperties().put(node.getNodeName(), node.getNodeValue());
					}
				}
				if(cNode.hasChildNodes())
				{
					NodeList childList=cNode.getChildNodes();
					for(int k=0;k<childList.getLength();k++)
					{
						Node nodes=childList.item(k);
						if(nodes.getNodeType()==Node.ELEMENT_NODE)
						{
							context.getData().add(nodes.getTextContent());
						}
					}
				}
				contextList.add(context);
			}
		}
	}

	public void test(Document doc) {
		NodeList nodeList = doc.getElementsByTagName("header");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node tempNode = nodeList.item(i);
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				System.out.println("====================================");
				System.out.println("Node Name=" + tempNode.getNodeName());
				if (tempNode.hasAttributes()) {
					NamedNodeMap nodeMap = tempNode.getAttributes();
					for (int j = 0; j < nodeMap.getLength(); j++) {
						Node node = nodeMap.item(j);
						System.out.println("att name:" + node.getNodeName());
						System.out.println("atrr value:" + node.getNodeValue());
					}
				}
				if (tempNode.hasChildNodes()) {
					NodeList tNodeList = tempNode.getChildNodes();
					for (int k = 0; k < tNodeList.getLength(); k++) {
						Node nodes = tNodeList.item(k);

						if (nodes.getNodeType() == Node.ELEMENT_NODE) {

							System.out.println("Node Name:"
									+ nodes.getNodeName());
							System.out.println("Node value"
									+ nodes.getTextContent());
						}
					}
				}
			}
		}
		
		
	}

	public void display() {
		System.out.println("Page Information");
		Iterator it = page.getProperties().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey() + "...." + pair.getValue());
			it.remove();
		}
		System.out.println("HeaderInformation");
		for (Header h : headerList) {
			System.out.println("-----");
			//System.out.println(h.getHeaderId());
			System.out.println(h.getData());
			Iterator ith = h.getProperties().entrySet().iterator();
			while (ith.hasNext()) {
				Map.Entry hpair = (Map.Entry) ith.next();
				System.out.println(hpair.getKey() + "..." + hpair.getValue());
				ith.remove();
			}
		}
		System.out.println("ContextInformation");
		for (Context c : contextList) {
			System.out.println("--------");
			System.out.println("data");
			for (String d : c.getData()) {
				System.out.println(d);
			}
			System.out.println("properties");
			Iterator cit = c.getProperties().entrySet().iterator();
			while (cit.hasNext()) {
				Map.Entry cpair = (Map.Entry) cit.next();
				System.out.println(cpair.getKey() + "...." + cpair.getValue());
				cit.remove();
			}
		}
	}
	//body tagli elementin propeties attributelari hashmapte tutulmasi islemi
	public void analyzeBody(Document doc) throws Exception
	{
		NodeList bodyList=doc.getElementsByTagName("body");
		for(int i=0;i<bodyList.getLength();i++)
		{
			Node body=bodyList.item(i);
			if(body.getNodeType()==Node.ELEMENT_NODE)
			{
				if(body.hasAttributes())
				{
					NamedNodeMap attributes=body.getAttributes();
					for(int j=0;j<attributes.getLength();j++)
					{
						Node attr=attributes.item(j);
						//put attributes to hashmap of body
						bodyAttributes.put(attr.getNodeName().toString(), attr.getNodeValue().toString());
					}
				}
			}
		}
	}
	//uygulamanin asil xmlden analiz edilecek birimi web de div mantigi ile block mantigi olsuturulmasi dusunulmustur.
	public void analyzeBlock(Document doc) throws Exception
	{
		NodeList blockNode=doc.getElementsByTagName("block");
		for(int i=0;i<blockNode.getLength();i++)
		{
			Node node=blockNode.item(i);
			Block block=new Block();
			if(node.getNodeType()==Node.ELEMENT_NODE)
			{
				if(node.hasAttributes())
				{
					NamedNodeMap atributes=node.getAttributes();
					for(int j=0;j<atributes.getLength();j++)
					{
						Node attr=atributes.item(j);
						if(attr.getNodeName().toString().equals("id")) //id ozel olarak tutulmali
						{
							block.setBlock_id(Integer.parseInt(attr.getNodeValue().toString()));
						}
						else if(attr.getNodeName().toString().equals("flow")) //flow ozel olarak tutulmali
						{
							block.setFlow(attr.getNodeValue().toString());
						}
						else if(attr.getNodeName().toString().equals("type")) //type ozel olarak tutulmali
						{
							block.setType(attr.getNodeValue().toString());
						} 
						else if(attr.getNodeName().toString().equals("flow-ref")) //flow ref varsa bu deger de ozel olarak tutulmali
						{
							block.setFlow_ref_id(Integer.parseInt(attr.getNodeValue().toString()));
						}
						else //properties olarak ne olursa olsun dinamik olarak ekleniyor.
						{
							block.getProperties().put(attr.getNodeName().toString(), attr.getNodeValue().toString());
						}
					}
				}
				blockList.add(block);
			}
		}
	}

	public List<Header> getHeaderList() {
		return headerList;
	}

	public void setHeaderList(List<Header> headerList) {
		this.headerList = headerList;
	}

	public List<Context> getContextList() {
		return contextList;
	}

	public void setContextList(List<Context> contextList) {
		this.contextList = contextList;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public List<Block> getBlockList() {
		return blockList;
	}

	public void setBlockList(List<Block> blockList) {
		this.blockList = blockList;
	}

	public HashMap<String, String> getBodyAttributes() {
		return bodyAttributes;
	}

	public void setBodyAttributes(HashMap<String, String> bodyAttributes) {
		this.bodyAttributes = bodyAttributes;
	}

	public Block[][] getTemplate_matrisi() {
		return template_matrisi;
	}

	public void setTemplate_matrisi(Block[][] template_matrisi) {
		this.template_matrisi = template_matrisi;
	}

	public int getSatir() {
		return satir;
	}

	public void setSatir(int satir) {
		this.satir = satir;
	}

	public int getSutun() {
		return sutun;
	}

	public void setSutun(int sutun) {
		this.sutun = sutun;
	}
	
	

}
