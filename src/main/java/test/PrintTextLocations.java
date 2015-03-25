package test;

import java.io.*;
import org.apache.pdfbox.exceptions.InvalidPasswordException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

import java.io.IOException;
import java.util.List;

public class PrintTextLocations extends PDFTextStripper {

    public PrintTextLocations() throws IOException {
        super.setSortByPosition(true);
    }

    public static void main(String[] args) throws Exception {

        PDDocument document = null;
        try {
            File input = new File("/opt/Simple.pdf");
            document = PDDocument.load(input);
            if (document.isEncrypted()) {
                document.decrypt("");
            }
            PrintTextLocations printer = new PrintTextLocations();
            List allPages = document.getDocumentCatalog().getAllPages();
            for (int i = 0; i < allPages.size(); i++) {
                PDPage page = (PDPage) allPages.get(i);
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

    /**
     * @param text The text to be processed
     */
    @Override
    protected void processTextPosition(TextPosition text) {
        System.out.println(" String [x: " + text.getXDirAdj() + ", y: "
            + text.getY() + ", height:" + text.getHeightDir()
            + ", space: " + text.getWidthOfSpace() + ", width: "
            + text.getWidthDirAdj() + ", yScale: " + text.getYScale() + "]"
            + text.getCharacter());
    }
}
