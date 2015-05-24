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
import model.Page;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlAnalyzer {
	private List<Block> blockList;
	private HashMap<String,String> bodyAttributes;
	private Page page;

	public XmlAnalyzer() {
		blockList=new ArrayList<Block>();
		bodyAttributes=new HashMap<String,String>();
		page = new Page();
	}
	public void komsulukHesaplama()
	{
		System.out.println(blockList.size());
		
		for(Block b:blockList)
		{
			if(b.getFlow_ref_id()==0)
			{
				for(Block bx:blockList)
				{
					if(b.getBlock_id()==bx.getFlow_ref_id())
					{
						if(b.getBlock_id()==bx.getBlock_id())
						{
							continue;
						}
						b.getKomsular().add(bx);
					}
				}
			}
			
		}
		for(Block b:blockList)
		{
			System.out.println(b.getKomsular().size());
		}
	}
	public void analyize() {
		try {
			File xml_file = new File("/home/volkanavci/bitirme/xml/pdf_to_xml.xml");
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

		} catch (Exception ex) {
			//attribute olarak alinan verilerin xsl de tanimlanirken hata olusturmamasi kontrolunu saglamak gerekli.
			//header ve context in yanyana oldugu tasarim modellemesi
			//header ve contextin yan yana durumu header ........
								    // ........
			System.out.println("xml okunamadi...");

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
				block.setData(node.getTextContent());
				blockList.add(block);
			}
		}
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
}
