package display;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class FreeDocumentApplication extends JFrame {
	private static final long serialVersionUID = 1L;
	public FreeDocumentApplication()
	{
		//default init function...
		initUI();
	}
	public void initUI()
	{
		
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
