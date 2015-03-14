package xml_to_xsl;

import java.io.File;
import java.nio.file.attribute.AclEntry.Builder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import model.Page;
import model.Header;
import model.Context;

public class XmlAnalyzer {
	private List<Header> headerList;
	private List<Context> contextList;
	private Page page;

	public XmlAnalyzer() {
		headerList = new ArrayList<Header>();
		contextList = new ArrayList<Context>();
		page = new Page();
	}

	public void analyize() {
		try {
			File xml_file = new File("/home/volkan/bitirme/xml/deneme.xml");
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
			 analyizeHeader(doc);
			 contextAnalyize(doc);
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
							
						}
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
	

}
