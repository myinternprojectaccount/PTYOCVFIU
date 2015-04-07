import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.PDGraphicsState;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.ResourceLoader;
import org.apache.pdfbox.util.TextPosition;

public class Parser extends PDFTextStripper {

public Parser() throws IOException {
    super(ResourceLoader.loadProperties(
            "org/apache/pdfbox/resources/PageDrawer.properties", true));
    super.setSortByPosition(true);
}

public void parse(String path) throws IOException{
    PDDocument doc = PDDocument.load(path);
    List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
    for (PDPage page : pages) {
        this.processStream(page, page.getResources(), page.getContents().getStream());
    }
}

@Override
protected void processTextPosition(TextPosition text) {
    try {
        PDGraphicsState graphicsState = getGraphicsState();
        System.out.println("R = " + graphicsState.getNonStrokingColor().getJavaColor().getRed());
        System.out.println("G = " + graphicsState.getNonStrokingColor().getJavaColor().getGreen());
        System.out.println("B = " + graphicsState.getNonStrokingColor().getJavaColor().getBlue());
    }
    catch (IOException ioe) {}

}

protected void processTextPosition2(TextPosition text) {

        Composite com;
        Color col;
        switch(this.getGraphicsState().getTextState().getRenderingMode()) {
        case PDTextState.RENDERING_MODE_FILL_TEXT:
            com = this.getGraphicsState().getNonStrokeJavaComposite();
            int r =       this.getGraphicsState().getNonStrokingColor().getJavaColor().getRed();
            int g = this.getGraphicsState().getNonStrokingColor().getJavaColor().getGreen();
            int b = this.getGraphicsState().getNonStrokingColor().getJavaColor().getBlue();
            int rgb = this.getGraphicsState().getNonStrokingColor().getJavaColor().getRGB();
            float []cosp = this.getGraphicsState().getNonStrokingColor().getColorSpaceValue();
            PDColorSpace pd = this.getGraphicsState().getNonStrokingColor().getColorSpace();
            break;
        case PDTextState.RENDERING_MODE_STROKE_TEXT:
            System.out.println(this.getGraphicsState().getStrokeJavaComposite().toString());
            System.out.println(this.getGraphicsState().getStrokingColor().getJavaColor().getRGB());
           break;
        case PDTextState.RENDERING_MODE_NEITHER_FILL_NOR_STROKE_TEXT:
            //basic support for text rendering mode "invisible"
            Color nsc = this.getGraphicsState().getStrokingColor().getJavaColor();
            float[] components = {Color.black.getRed(),Color.black.getGreen(),Color.black.getBlue()};
            Color  c1 = new Color(nsc.getColorSpace(),components,0f);
            System.out.println(this.getGraphicsState().getStrokeJavaComposite().toString());
            break;
        default:
            System.out.println(this.getGraphicsState().getNonStrokeJavaComposite().toString());
            System.out.println(this.getGraphicsState().getNonStrokingColor().getJavaColor().getRGB());
            
        if (operation.equals("RG")) {
   // stroking color space
   System.out.println(operation);
   System.out.println(arguments);
} else if (operation.equals("rg")) {
   // non-stroking color space
   System.out.println(operation);
   System.out.println(arguments);
} else if (operation.equals("BT")) {
   System.out.println(operation);    
} else if (operation.equals("ET")) {
   System.out.println(operation);           
}

    }

public static void main(String[] args) throws IOException, COSVisitorException {
    Parser p = new Parser();
    p.parse("/Users/apple/Desktop/123.pdf");
}

}