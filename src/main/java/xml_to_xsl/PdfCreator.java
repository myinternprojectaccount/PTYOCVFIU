package xml_to_xsl;

import java.io.File;
import java.io.OutputStream;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

public class PdfCreator {

	public void convertToPdf() throws Exception
	{
            System.out.println("Preparing...");
 
            // Setup directories
            File baseDir = new File("/home/");
            File outDir = new File(baseDir, "volkan/bitirme/pdf");
            outDir.mkdirs();
 
            // Setup input and output files 
            File xmlfile = new File(baseDir, "volkan/bitirme/xml/template.xml");
            File xsltfile = new File(baseDir,"volkan/bitirme/xsl/denedik3.xsl");
            File pdffile = new File(outDir, "bitirme.pdf");
 
            System.out.println("Input: XML (" + xmlfile + ")");
            System.out.println("Stylesheet: " + xsltfile);
            System.out.println("Output: PDF (" + pdffile + ")");
            System.out.println();
            System.out.println("Transforming...");
 
            // configure fopFactory as desired
            FopFactory fopFactory = FopFactory.newInstance();
 
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            // configure foUserAgent as desired
 
            // Setup output
            OutputStream out = new java.io.FileOutputStream(pdffile.getPath());
            out = new java.io.BufferedOutputStream(out);

            
                // Construct fop with desired output format
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF,
                        foUserAgent, out);
 
                // Setup XSLT
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xsltfile.getPath()));
 
                // Set the value of a <param> in the stylesheet
                transformer.setParameter("versionParam", "2.0");
 
                // Setup input for XSLT transformation
                Source src = new StreamSource(xmlfile.getPath());
 
                // Resulting SAX events (the generated FO) must be piped through
                // to FOP
                Result res = new SAXResult(fop.getDefaultHandler());
 
                // Start XSLT transformation and FOP processing
               
                transformer.transform(src, res);
            
                out.close();
            
 
            System.out.println("Success!");
        
          
           
          
        
	}
}
