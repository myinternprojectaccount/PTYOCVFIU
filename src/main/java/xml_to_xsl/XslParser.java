package xml_to_xsl;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import model.Context;
import model.Header;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
public class XslParser {
	private XmlAnalyzer analyzer;
	public XslParser()
	{
		this.analyzer=new XmlAnalyzer();
		analyzer.analyize();
		//analyzer.display();
	}
	
	public void convertToXsl() throws Exception
	{
		DocumentBuilderFactory docFactor=DocumentBuilderFactory.newInstance();
		DocumentBuilder builder=docFactor.newDocumentBuilder();
		Document doc=builder.newDocument();
		//root element tamimlama
		
		Element rootElement=doc.createElement("xs:stylesheet");
		doc.appendChild(rootElement);
		rootElement.setAttribute("version", "1.0");
		rootElement.setAttribute("xmlns:xsl","http://www.w3.org/1999/XSL/Transform");
		rootElement.setAttribute("xmlns:fo","http://www.w3.org/1999/XSL/Format");
		rootElement.setAttribute("xmlns:xs", "http://www.w3.org/1999/XSL/Transform");
		Element template=doc.createElement("xs:template");
		template.setAttribute("match", "/");
		rootElement.appendChild(template);
		Element fo_root=doc.createElement("fo:root");
		fo_root.setAttribute("xmlns:fo", "http://www.w3.org/1999/XSL/Format");
		template.appendChild(fo_root);
		//page properties setup
		Element layaout_master_set=doc.createElement("fo:layout-master-set");
		fo_root.appendChild(layaout_master_set);
		Element page=doc.createElement("fo:simple-page-master");
		page.setAttribute("master-name", "all_pages");
		Iterator it=analyzer.getPage().getProperties().entrySet().iterator();
		//xmlden alinan sayfa ozellikleri page master attribute i olarak ataniyor.
		while(it.hasNext())
		{
			Map.Entry pair=(Map.Entry)it.next();
			page.setAttribute(pair.getKey().toString(), pair.getValue().toString());
		}
		layaout_master_set.appendChild(page);
		//page(simple-page-master) icin zorunlu etiketler eklenmeli
		//fo:region body,before,after,start,end
		Element region_body=doc.createElement("fo:region-body");
		Element region_before=doc.createElement("fo:region-before");
		Element region_after=doc.createElement("fo:region-after");
		Element region_start=doc.createElement("fo:region-start");
		Element region_end=doc.createElement("fo:region-end");
		page.appendChild(region_body);   //siralamasi onemlidir yoksa hata alirsin.
		page.appendChild(region_before);
		page.appendChild(region_after);
		page.appendChild(region_start);
		page.appendChild(region_end);
		
		
		//page_sequence ve fo:flow ile sayfa veri gosterim bolumu tasarimi...
		Element page_sequence=doc.createElement("fo:page-sequence");
		page_sequence.setAttribute("master-reference", "all_pages");
		fo_root.appendChild(page_sequence);
		//fo:flow ile icerik kisim tasarimi
		Element fo_flow=doc.createElement("fo:flow");
		fo_flow.setAttribute("flow-name", "xsl-region-body");
		page_sequence.appendChild(fo_flow);
		//fo_flow icerisine header ve context bilgileri ile icerik tasarim kalibi olusturulur.
		for(Header h:analyzer.getHeaderList())
		{
			String id=null;
			String column="0";
			Context selected=new Context();
			Iterator hit=h.getProperties().entrySet().iterator();
			while(hit.hasNext())
			{
				Map.Entry pair=(Map.Entry)hit.next();
				if(pair.getKey().toString().equals("id"))
				{
					id=pair.getValue().toString();
					System.out.println(id);
				}
			}
			//header ile birlesik context var mi
			for(Context c:analyzer.getContextList())
			{
				Iterator cit=c.getProperties().entrySet().iterator();
				while(cit.hasNext())
				{
					String cid=null;
					Map.Entry cpair=(Map.Entry)cit.next();
					if(id !=null)
					{
						
						if(cpair.getKey().toString().equals("id"))
						{
							if(id.equals(cpair.getValue().toString()))
							{
								selected=c;
								
							}
							
							
						}
					}
				}
			}
			Iterator col=selected.getProperties().entrySet().iterator();
			while(col.hasNext())
			{
				Map.Entry pair=(Map.Entry)col.next();
				if(pair.getKey().toString().equals("column"))
				{
					column=pair.getValue().toString();
				}
			}
			//selected header ve selected context birlestirme
			//genel table olusturma
			Element fo_block=doc.createElement("fo:block");
			fo_flow.appendChild(fo_block);
			//fo block olusturup table olusturmak lazim daha sonrasinda
			//header ve context icerikleri xsl sayfasina eklemek gerekmektedir.
			Element table=doc.createElement("fo:table");
			//Bu kisimda bir yaklasim lazim 
			//bir header ve context blogu icin bir tablo
			//iki satir olusturalim daha sonra context satirinda icerigin tasarina gore 
			//gene bir tablo olusturup dinamik olarak satirlar olustur ve test ettikce gelistir.
			
			Element table_body=doc.createElement("fo:table-body");
			Element table_row=doc.createElement("fo:table-row");
			Element table_cell_header=doc.createElement("fo:table-cell");
			Element fo_block_header=doc.createElement("fo:block");
			table_cell_header.appendChild(fo_block_header);
			table_row.appendChild(table_cell_header);
			table_body.appendChild(table_row);
			table.appendChild(table_body);
			fo_block.appendChild(table);
			//header in gosterecegimiz block icin tasarim bilgilerinin atanmasi
			hit=h.getProperties().entrySet().iterator();
			while(hit.hasNext())
			{
				Map.Entry pair=(Map.Entry)hit.next();
				fo_block_header.setAttribute(pair.getKey().toString(), pair.getValue().toString());
			}
			fo_block_header.setTextContent(h.getData());
			//header satiri bitti
			Element context_row=doc.createElement("fo:table-row");
			table_body.appendChild(context_row);
			
			Element table_cell_context=doc.createElement("fo:table-cell");
			Element block_for_context=doc.createElement("fo:block");
			//contextin gosterim formatina bagli olarak bir tablo ve sutun sayisina bakmak gerekir.
			Element table_context=doc.createElement("fo:table");
			Element table_context_body=doc.createElement("fo:table-body");
			block_for_context.appendChild(table_context);
			table_context.appendChild(table_context_body);
			table_cell_context.appendChild(block_for_context);
			context_row.appendChild(table_cell_context);
			//context tablosuna property atama
			int counter=0;
			while(counter<selected.getData().size()) //context ne kadar veri tutuyorsa o kadar satir olusmali
			{
				Element table_row_temp=doc.createElement("fo:table-row");
				if(Integer.parseInt(column)<1)
				{
					//temp table row icin eger datalarin ozel attributelari varsa onlarinda 
					//burada ekleyebilirsin....
					//tasarim olarak yatay siralaniyor
					Element table_cell_context_temp=doc.createElement("fo:table-cell");
					Element context_data_block=doc.createElement("fo:block");
					//datalarin gosterilecegi block icin stil tanimlari
					Iterator font=selected.getProperties().entrySet().iterator();
					while(font.hasNext())
					{
						Map.Entry pair=(Map.Entry)font.next();
						if(!pair.getKey().toString().equals("id"))
						{
							context_data_block.setAttribute(pair.getKey().toString(), pair.getValue().toString());
						}
					}
					context_data_block.setTextContent(selected.getData().get(counter));
					table_cell_context_temp.appendChild(context_data_block);
					table_row_temp.appendChild(table_cell_context_temp);
					//contexte ne kadar veri varsa burada eklenmis olacagiz....
				}
				else
				{
					//tasarim olarak contextte yan yana veriler olabilir.
				}
				
				table_context_body.appendChild(table_row_temp);
				counter ++;
			}
		}
		//donusturme fonksiyonlari....
		 TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         DOMSource source = new DOMSource(doc);
         StreamResult result = new StreamResult(new File("/home/volkan/bitirme/xsl/denedik.xsl").getPath());

         transformer.transform(source, result);

         System.out.println("islem basarili...");
		
		
	}
	
	
}
