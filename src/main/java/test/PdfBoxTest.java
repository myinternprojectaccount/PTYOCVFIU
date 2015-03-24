package test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;

public class PdfBoxTest {

	public static void main(String[] args) throws IOException {
		System.out.println("------");
		org.apache.pdfbox.pdmodel.font.PDFont pdFont;
		PDDocument doc = PDDocument.load("/opt/hello.pdf");
		List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
		for (PDPage page : pages) {
			Map<String, PDFont> pageFonts = page.getResources().getFonts();
			Set<String> fonts = pageFonts.keySet();
			for (String font : fonts) {
				// System.out.println(pageFonts.get(font));
				pdFont = pageFonts.get(font);
				System.out.println(pdFont.getFontDescriptor().getFontName());
			}
		}
	}

}
