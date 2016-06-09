package pdf_reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Pdf_Data;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.PDGraphicsState;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.ResourceLoader;
import org.apache.pdfbox.util.TextPosition;

import xml_to_xsl.Pdf_Data_Analyzer;

public class PrintTextLocations extends PDFTextStripper {

	Pdf_Data_Analyzer main =new Pdf_Data_Analyzer();
	private double height;
	private double width;
    public PrintTextLocations() throws IOException {
    	super(ResourceLoader.loadProperties(
                "org/apache/pdfbox/resources/PageDrawer.properties", true));
        super.setSortByPosition(true);
    }
    public void getTextPosition() throws Exception {

        PDDocument document = null;
        try {
            File input = new File("/home/volkanavci/bitirme/pdf/TestPdf.pdf");
            document = PDDocument.load(input);
            if (document.isEncrypted()) {
                document.decrypt("");
            
            }
            
            PrintTextLocations printer = new PrintTextLocations();
            List allPages = document.getDocumentCatalog().getAllPages();
            for (int i = 0; i < allPages.size(); i++) {
                PDPage page = (PDPage) allPages.get(i);
                //sayfa genisligi ve yuksekligi
                width=page.getMediaBox().getWidth();
                height=page.getMediaBox().getHeight();
                System.out.println("Processing page: " + i);
                PDStream contents = page.getContents();
                
                if (contents != null) {
                    printer.processStream(page, page.findResources(), page.getContents().getStream());
                }
            }
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }
    public void display_data()
    {
   
    }

    /**
     * @param text The text to be processed
     */
    @Override
    protected void processTextPosition(TextPosition text) {
    	
    	Pdf_Data data=new Pdf_Data();
    	data.setX(text.getXDirAdj());
    	data.setY(text.getY());
    	data.setHeight(text.getHeightDir());
    	data.setWidth(text.getWidthDirAdj());
    	data.setYscale(text.getYScale());
    	data.setC_data(text.getCharacter());
    	String [] fonts=text.getFont().getBaseFont().split(",");
    	data.setFont(fonts[0]);
    	data.setFont_size(text.getFontSize());
    	data.setPage_height(height);
    	data.setPage_width(width);
    	data.setSpace_width(text.getWidthOfSpace());
    	//font hesapla
    	System.out.println(data.getFont());
    	System.out.println(text.getFontSize());
    	int bold=Math.round(text.getFont().getFontDescriptor().getFontWeight());
    	if(bold>500)
    	{
    		data.setBold("bold");
    	}
    	float italic=text.getFont().getFontDescriptor().getItalicAngle();
    	if(italic!=0)
    	{
    		data.setItalic("italic");
    	}
    	System.out.println(text.getFont().getFontDescriptor().getFontWeight());
    	//System.out.println(data.getBold()+"*****"+data.getItalic());
    	//System.out.println(data.getFont());
    	try {
    		//renk atamasi yapiliyor...
            PDGraphicsState graphicsState = getGraphicsState();
            data.setR(graphicsState.getNonStrokingColor().getJavaColor().getRed());
            data.setG(graphicsState.getNonStrokingColor().getJavaColor().getGreen());
            data.setB(graphicsState.getNonStrokingColor().getJavaColor().getBlue());
        }
        catch (IOException ioe) {}
    	main.getDataList().add(data);
//              System.out.println(" String [x: " + text.getXDirAdj() + ", y: "
//            + text.getY() + ", height:" + text.getHeightDir()
//            + ", space: " + text.getWidthOfSpace() + ", width: "
//            + text.getWidthDirAdj() + ", yScale: " + text.getYScale() + "]"
//            + text.getCharacter());
        
    }
   
}
