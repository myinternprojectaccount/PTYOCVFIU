package display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.StyledEditorKit;

import model.Data_To_Xml;
import xml_to_xsl.Pdf_Data_Analyzer;

public class FreeDocumentApplication extends JFrame {
	private static final long serialVersionUID = 1L;
	public Pdf_Data_Analyzer data_analyzer;
	private JPopupMenu pmenu;
	private Data_To_Xml secili;
	private int hareket=0;
	public FreeDocumentApplication()
	{
		//default init function...
		secili=new Data_To_Xml();
		data_analyzer=new Pdf_Data_Analyzer();
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
		JEditorPane text=new JEditorPane();
		text.setName("text_area");
		text.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
		pane.getViewport().add(text);
		text.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent e)
			{
				if((e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON1))
				{
					JEditorPane text=(JEditorPane)e.getComponent();
					 JOptionPane.showMessageDialog(null,text.getSelectedText());
					 JTextArea test=new JTextArea();
				}
				
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
			
			Font font = new Font(b.getFont(),0,(int)b.getFont_size());
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
		                    JOptionPane.showMessageDialog(null,pane.getText()+"..."+secili.getData()+"..."+secili.getColor());
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
