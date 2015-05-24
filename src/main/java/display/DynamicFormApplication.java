package display;

import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import model.Data_To_Xml;
import xml_to_xsl.Create_Data_Xml;
import xml_to_xsl.PdfCreator;
import xml_to_xsl.Pdf_Data_Analyzer;
import xml_to_xsl.XmlAnalyzer;
import xml_to_xsl.XslParser;

public class DynamicFormApplication extends JFrame
{
	private static final long serialVersionUID = 1L;
	public XslParser parser;
	public Create_Data_Xml xml_creator;
	public PdfCreator pdf_creator;
	public XmlAnalyzer analyzer;
	public Pdf_Data_Analyzer data_analyzer;
	JPanel text_panel=new JPanel();
	public DynamicFormApplication()
	{
		
		analyzer=new XmlAnalyzer();
		data_analyzer=new Pdf_Data_Analyzer();
		parser=new XslParser();
		xml_creator=new Create_Data_Xml();
		pdf_creator=new PdfCreator();
		initUI();
	}
	public void initUI()
	{
		//dinamik textboxlar icin taminlanan panel
		text_panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		createMenu();
		textTasarim(text_panel);
		add(text_panel);
		//add(button_panel);
		setTitle("Dinamik Veri Formu");
		setSize(700,700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public void createMenu()
	{
		//bu method menu bar olusturur.
		JMenuBar menu_bar=new JMenuBar();
		JMenu menu=new JMenu("Islemler");
		JMenuItem exit=new JMenuItem("Cikis");
		exit.setMnemonic(KeyEvent.VK_E);
		exit.setToolTipText("uygulamadan cikma");
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,ActionEvent.CTRL_MASK));
		exit.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
//				for(Data_To_Xml d: data_analyzer.getXmlDataList())
//				{
//					System.out.println(d.getData());
//				}
				System.exit(0);
				
			}
		});
		menu.add(exit);
		menu_bar.add(menu);
		setJMenuBar(menu_bar);
	}
	public void textTasarim(JPanel panel)
	{	//textboxlar dinamik olarak olsuturulacak
		//xml den alinan block elemanlarinin ozelliklerine gore bir bir eklenir...
		try
		{
			//xml_creator.create_xml();
			//analyzer.analyize();
			data_analyzer.getData();
			data_analyzer.setXmlDataPerSentence();
			
		}catch(Exception ex)
		{
			System.out.println("analizer calismadi");
		}
		//veriler hazir...
		GridLayout grid=new GridLayout(data_analyzer.getXmlDataList().size()+1,1);
		panel.setLayout(grid);
		int i=1;
		for(Data_To_Xml b : data_analyzer.getXmlDataList())
		{
			JTextPane text_field =new JTextPane();
			text_field.setName(String.valueOf(i));
			text_field.setText(b.getData());
			//italic,bold,normal kontrolu
			
			Color color=new Color(b.getR(),b.getG(),b.getB());
			
			Font font = new Font(b.getFont(),0,(int)b.getFont_size());
			text_field.setFont(font);
			text_field.setForeground(color);
			panel.add(text_field);
			++i;
		}
		Button button=new Button("onayla");
		button.addActionListener(new ButtonActionListener());
		panel.add(button);
	}
	private class ButtonActionListener implements ActionListener
	{

		public void actionPerformed(ActionEvent e) {
			// button click
			String command=e.getActionCommand();
			if(command.equals("onayla")){
				//button olayi
				//burada JPane de yazilan datalar alinir ve xsl ardindan pdf olarak kaydedilir.
				Component [] liste= text_panel.getComponents();
				for(int i=1;i<liste.length;++i)
				{
					for(int j=1;j<liste.length;j++)
					{
						if(liste[i].getName().equals(String.valueOf(j)))
						{
							JTextPane temp=(JTextPane)liste[i];
							data_analyzer.getXmlDataList().get(j-1).setData(temp.getText());
						}
					}
				}
			}
			//datalist guncellikten sonra bunlari xml olarak kaydedebiliriz...
			//buradan sonra islemler tamamlaniyor ve bu tasarim bilgileri ile kendi pdf dokumanimizi olusturuyourz...
			try
			{
				data_analyzer.create_Xml_Sentence();
				parser.convertXslFromXMLSentence();
				pdf_creator.convertToPdf();
			}
			catch(Exception ex)
			{
				
			}
		}
		
	}
	public static void main(String [] args)
	{	
			
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				// uygulama baslatma
				DynamicFormApplication form=new DynamicFormApplication();
				form.setVisible(true);
				
			}
		});
		
	}
}