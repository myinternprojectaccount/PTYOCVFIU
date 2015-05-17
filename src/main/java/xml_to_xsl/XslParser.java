package xml_to_xsl;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import model.Block;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XslParser {
	private XmlAnalyzer analyzer;
	private int sayfa_genisligi=0;

	public XslParser() {
		this.analyzer = new XmlAnalyzer();
		analyzer.analyize();
		// analyzer.display();

	}
	public void convertXslFromXML() throws Exception
	{
		DocumentBuilderFactory docFactor = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = docFactor.newDocumentBuilder();
		Document doc = builder.newDocument();

		// sayfanin tasarim atamalari
		Element rootElement = doc.createElement("xs:stylesheet");
		doc.appendChild(rootElement);
		rootElement.setAttribute("version", "1.0");
		rootElement.setAttribute("xmlns:xsl",
				"http://www.w3.org/1999/XSL/Transform");
		rootElement.setAttribute("xmlns:fo",
				"http://www.w3.org/1999/XSL/Format");
		rootElement.setAttribute("xmlns:xs",
				"http://www.w3.org/1999/XSL/Transform");
		Element template = doc.createElement("xs:template");
		template.setAttribute("match", "/");
		rootElement.appendChild(template);
		Element fo_root = doc.createElement("fo:root");
		fo_root.setAttribute("xmlns:fo", "http://www.w3.org/1999/XSL/Format");
		template.appendChild(fo_root);
		// page properties setup
		Element layaout_master_set = doc.createElement("fo:layout-master-set");
		fo_root.appendChild(layaout_master_set);
		Element page = doc.createElement("fo:simple-page-master");
		page.setAttribute("master-name", "all_pages");
		Iterator it = analyzer.getPage().getProperties().entrySet().iterator();
		// xmlden alinan sayfa ozellikleri page master attribute i olarak
		// ataniyor.
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			page.setAttribute(pair.getKey().toString(), pair.getValue()
					.toString());
		}
		layaout_master_set.appendChild(page);
		// page(simple-page-master) icin zorunlu etiketler eklenmeli
		// fo:region body,before,after,start,end
		Element region_body = doc.createElement("fo:region-body");
		Element region_before = doc.createElement("fo:region-before");
		Element region_after = doc.createElement("fo:region-after");
		Element region_start = doc.createElement("fo:region-start");
		Element region_end = doc.createElement("fo:region-end");
		page.appendChild(region_body); // siralamasi onemlidir yoksa hata
										// alirsin.
		page.appendChild(region_before);
		page.appendChild(region_after);
		page.appendChild(region_start);
		page.appendChild(region_end);

		// burdan sonrasi page-sequence tanimlamasi ve sayfanin icerik kismina
		// ait tasarimlarin yapilmasi
		// page_sequence ve fo:flow ile sayfa veri gosterim bolumu tasarimi...
		Element page_sequence = doc.createElement("fo:page-sequence");
		page_sequence.setAttribute("master-reference", "all_pages");
		fo_root.appendChild(page_sequence);
		// fo:flow ile icerik kisim tasarimi
		Element fo_flow = doc.createElement("fo:flow");
		fo_flow.setAttribute("flow-name", "xsl-region-body");
		page_sequence.appendChild(fo_flow);
		//sayfayi body genisligine gore olceklendirmek icin body genisliginin elde edilmesi
		// fo_flow icerisine header ve context bilgileri ile icerik tasarim
		// kalibi olusturulur.
		// sayfa numarasini gostermek istersek burada ayarla ilerde
		// tasarim matrisi kullanilarak adim adim xsl sayfasi olusturulur
		Element master_block = doc.createElement("fo:block");
		fo_flow.appendChild(master_block);
		//master block elementinin icine blocklar eklenecek
		//eklenme kurali ayni satirda olanlarin tespiti ile onemlidir.
		Element master_table = doc.createElement("fo:table");
		master_block.appendChild(master_table);
		Element master_table_body = doc.createElement("fo:table-body");
		master_table.appendChild(master_table_body);
		//master_table.setAttribute("border","solid rgb(255,0,255) 1px");
		//master_table.setAttribute("width", "15cm");
		for(Block b:analyzer.getBlockList())
		{
			Element master_row = doc.createElement("fo:table-row");
			master_table_body.appendChild(master_row);
			if(b.getKomsular().size()>0)
			{
				//ayni satirda birden fazla var demek bu da table -cell demek
				Element table_cell=doc.createElement("fo:table-cell");
				Element table_block=doc.createElement("fo:block");
				master_row.appendChild(table_cell);
				table_cell.appendChild(table_block);
				//table block icinde column numarasi kadar table cell olusturulacak
				Element temp_table=doc.createElement("fo:table");
				Element temp_table_body=doc.createElement("fo:table-body");
				Element temp_row =doc.createElement("fo:table-row");
				//bu block eklenir
				Element temp_cell=doc.createElement("fo:table-cell");
				Element temp_block=doc.createElement("fo:block");
				Set set_temp = b.getProperties().entrySet();
				Iterator iterator_temp = set_temp.iterator();
				while (iterator_temp.hasNext()) {
					Map.Entry me = (Map.Entry) iterator_temp.next();
					temp_block.setAttribute(me.getKey().toString(),me.getValue().toString());
				}
				temp_cell.setTextContent(b.getData());
				table_block.appendChild(temp_table);
				temp_table.appendChild(temp_table_body);
				temp_table_body.appendChild(temp_row);
				temp_row.appendChild(temp_cell);
				temp_cell.appendChild(temp_block);
				//inner
				for(int i=0;i<b.getKomsular().size();i++)
				{
					//komsular eklenir...
					Element temp_inner_cell=doc.createElement("fo:table-cell");
					Element temp_inner_block=doc.createElement("fo:block");
					Set set = b.getKomsular().get(i).getProperties().entrySet();
					Iterator iterator = set.iterator();
					while (iterator.hasNext()) {
						Map.Entry me = (Map.Entry) iterator.next();
						temp_inner_block.setAttribute(me.getKey().toString(),me.getValue().toString());
					}
					temp_inner_block.setTextContent(b.getKomsular().get(i).getData());
					table_block.appendChild(temp_table);
					temp_table.appendChild(temp_table_body);
					temp_table_body.appendChild(temp_row);
					temp_row.appendChild(temp_inner_cell);
					temp_inner_cell.appendChild(temp_inner_block);
				}
				
			}
			else if(b.getKomsular().size()==0)
			{
				Element table_cell=doc.createElement("fo:table-cell");
				Element cell_block=doc.createElement("fo:block");
				master_row.appendChild(table_cell);
				table_cell.appendChild(cell_block);
				//block icin datanin propertiesleri eklenmelidir.
				cell_block.setTextContent(b.getData());
				Set set = b.getProperties().entrySet();
				Iterator iterator = set.iterator();
				while (iterator.hasNext()) {
					Map.Entry me = (Map.Entry) iterator.next();
					cell_block.setAttribute(me.getKey().toString(),me.getValue().toString());
				}
			}
			//for her dondugunde genel tabloda bir satir atla
		}
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(
				"/home/volkan/bitirme/xsl/denedik3.xsl").getPath());

		transformer.transform(source, result);

		System.out.println("islem basarili...");
	}
	public void sayfaGenisliginiBul()
	{
		Set set=analyzer.getBodyAttributes().entrySet();
		Iterator iterator=set.iterator();
		while(iterator.hasNext())
		{
			Map.Entry me=(Map.Entry)iterator.next();
			if(me.getKey().toString().equals("width"))
			{
				sayfa_genisligi=Integer.parseInt(me.getValue().toString());
			}
		}
	}

}
