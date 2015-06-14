package display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import model.Data_To_Xml;
import xml_to_xsl.PdfCreator;
import xml_to_xsl.Pdf_Data_Analyzer;
import xml_to_xsl.XslParser;

public class FreeDocumentApplication extends JFrame {
	private static final long serialVersionUID = 1L;
	public Pdf_Data_Analyzer data_analyzer;
	private JPopupMenu pmenu;
	private Data_To_Xml secili;
	private Data_To_Xml [][] dataMatrisi;
	private int hareket=0;
	private int offset=0;
	int satir=0;
	int sutun=0;
	int s_offset=0;
	int sa_offset=0;
	StringBuilder sb;
	public FreeDocumentApplication()
	{
		//default init function...
		secili=new Data_To_Xml();
		data_analyzer=new Pdf_Data_Analyzer();
		dataMatrisi=new Data_To_Xml[100][100];
		sb=new StringBuilder();
		initUI();
	}
	public void initUI()
	{
		JPanel main_panel=new JPanel();
		GridLayout main_grid=new GridLayout(1,2);
		main_panel.setLayout(main_grid);
		prepare_text_area(main_panel);
		
		//screen
		add(main_panel);
		setTitle("Free Document App");
		setSize(1000,700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public void prepare_text_area(JPanel panel)
	{
		JPanel panel_text=new JPanel();
		JPanel panel_design=new JPanel();
		//text ve design panelleri icin gridler
		panel_text.setLayout(new BorderLayout());
		panel_text.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		//burada bos bir text area tasarlanir...
		JScrollPane pane=new JScrollPane();
		final JTextPane text=new JTextPane();
		text.setName("text_area");
		text.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
		pane.getViewport().add(text);
		
		text.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				offset++;
				switch(e.getKeyCode())
				{
				case KeyEvent.VK_ENTER:
					//saitir atlatir.
					//System.out.println(sb.toString());
					Data_To_Xml tempa=new Data_To_Xml();
					tempa.setPosa(s_offset);
					tempa.setPosb(offset-1);
					tempa.setUpdate(false);
					tempa.setData(sb.toString());
					s_offset=offset;
					dataMatrisi[satir][sutun]=tempa;
					sb=new StringBuilder();
					satir++;					
					break;
				case KeyEvent.VK_SPACE:
					//satirda devam
					//System.out.println(sb.toString());
					Data_To_Xml temp=new Data_To_Xml();
					temp.setPosa(s_offset);
					temp.setPosb(offset-1);
					temp.setUpdate(false);
					temp.setData(sb.toString());
					s_offset=offset;
					dataMatrisi[satir][sutun]=temp;
					sutun++;
					sb=new StringBuilder();
					break;
				case KeyEvent.VK_BACK_SPACE:
					sb.deleteCharAt(offset);
					offset--;
					break;
				default:
					//offset arttir...
					sb.append(e.getKeyChar());
					
				}
			}
		});
		text.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent e)
			{
				JTextPane text=(JTextPane)e.getComponent();
				 StyledDocument doc=text.getStyledDocument();
				if((e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON1))
				{
					
					 int start=text.getSelectionStart();
					 int end=text.getSelectionEnd();
					 
					 if (start == end) { // No selection, cursor position.
					        return;
					    }
					    if (start > end) { // Backwards selection?
					        int life = start;
					        start = end;
					        end = life;
					    }
					    //check data matrisi and set metada in dataMatris element
					    for(int i=0;i<100;i++)
					    {
					    	for(int j=0;j<100;j++)
					    	{
					    		//null degilse ve secili position degerleri esit ise
					    		if(dataMatrisi[i][j]!=null)
					    		{
					    			if(dataMatrisi[i][j].getPosa()==start && dataMatrisi[i][j].getPosb()==end)
					    			{
					    				//secili eleman bulundu 
					    				dataMatrisi[i][j].setFont(secili.getFont());
					    				dataMatrisi[i][j].setFont_size(secili.getFont_size());
					    				dataMatrisi[i][j].setColor(secili.getColor());
					    				dataMatrisi[i][j].setBold(secili.getBold());
					    				dataMatrisi[i][j].setItalic(secili.getItalic());
					    				dataMatrisi[i][j].setUpdate(true);
					    				System.out.println("eslesme bulundu...");
					    			}
					    		}
					    		
					    	}
					    }
					    int fsize=Integer.parseInt(String.valueOf(Math.round(secili.getFont_size())));
					    Style style = text.addStyle("base", null);
					    Color color=new Color(secili.getR(),secili.getG(),secili.getB());
					    StyleConstants.setFontSize(style, fsize);
					    StyleConstants.setFontFamily(style, secili.getFont());
					    StyleConstants.setForeground(style,color);
					    if(secili.getBold()!=null);
					    {
					    	StyleConstants.setBold(style, true);
					    }
					    if(secili.getItalic()!=null)
					    {
					    	StyleConstants.setItalic(style, true);
					    }
					    //style = textPane.getStyle("MyHilite");
					    doc.setCharacterAttributes(start, end - start, style, false);
					    try {
							System.out.println(doc.getText(start, end-start));
						} catch (BadLocationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				}
				doc=null;
				
				
			}
		});
		text.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				//secme yapacak
			}
		});
		panel_text.add(pane);
		
		//design icin pdflerden  gelen veriler gosterilir...
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
		//set layout
		GridLayout grid_design=new GridLayout(data_analyzer.getXmlDataList().size()+1,2);
		panel_design.setLayout(grid_design);
		panel_design.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		int i=1;
		for(Data_To_Xml b : data_analyzer.getXmlDataList())
		{
			JTextPane text_field =new JTextPane();
			text_field.setName(String.valueOf(i));
			text_field.setText(b.getData());
			//italic,bold,normal kontrolu
			JCheckBox check=new JCheckBox();
			check.setName(String.valueOf(i));
			Color color=new Color(b.getR(),b.getG(),b.getB());
			int weight;
			if(b.getBold()!=null)
			{
				weight=1;
			}
			else if(b.getItalic()!=null)
			{
				weight=2;
			}
			else
			{
				weight=0;
			}
			
			Font font = new Font(b.getFont(),weight,(int)b.getFont_size());
			
			text_field.setFont(font);
			text_field.setForeground(color);
			text_field.add(check);
			hareket=0;
			text_field.addMouseListener(new MouseAdapter()
			{
				//mouse suruklenmeden birakilirsa olmamali...
				 public void mouseReleased(MouseEvent e) {
		                //added check for MouseEvent.BUTTON1 which is left click
		                if ((e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON1) && hareket>0) {
		                	secili=new Data_To_Xml();
		                    JTextPane pane=(JTextPane)e.getComponent();
		                    //secili textin propertieslerini al...
		                    for(Data_To_Xml d: data_analyzer.getXmlDataList())
		                    {
		                    	if(pane.getText().equalsIgnoreCase(d.getData()))
		                    	{
		                    		secili=d;
		                    		hareket=0;
		                    	}
		                    }
		                }
		            }
			});
			text_field.addMouseMotionListener(new MouseMotionAdapter(){
				public void mouseDragged(MouseEvent e)
				{
					++hareket;
				}
			});
			
			//panel_design.add(check);
			panel_design.add(text_field);
			++i;
		}
		
		JButton button=new JButton("Save As");
		button.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e)
			{
				//System.out.println(text.getText());
				print();
				XslParser xparser=new XslParser();
				PdfCreator pdf_creator=new PdfCreator();
				try
				{
				xparser.xsl_From_Form(dataMatrisi);
				pdf_creator.convertToPdf();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
		panel_design.add(button);
		panel.add(panel_design);
		panel.add(panel_text);
	}
	public void create_popup_menu()
	{
		pmenu=new JPopupMenu();
		JMenuItem maxMi = new JMenuItem("Maximize");
        maxMi.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {

                if (getExtendedState() != JFrame.MAXIMIZED_BOTH) {
                    setExtendedState(JFrame.MAXIMIZED_BOTH);
                }

            }
        });

        pmenu.add(maxMi);

        JMenuItem quitMi = new JMenuItem("Quit");
        quitMi.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        pmenu.add(quitMi); 
	}
	public void print()
	{
		for(int i=0;i<100;++i)
		{
			for(int j=0;j<100;j++)
			{
				if(dataMatrisi[i][j] !=null)
				{
					System.out.print(dataMatrisi[i][j].getData()+"....");
				}
			}
			System.out.println();
		}
	}
	public static void main(String []args)
	{
		//main function...
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				// start application
				FreeDocumentApplication form=new FreeDocumentApplication();
				form.setVisible(true);
				
			}
		});
	}

}
