package gui.error;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import vars.Language;

public class LightError {

	private final static Language lang = Language.getInstance();
	
	/* Show message */
	public static void show(String msg, String title, JFrame parent)
	{
		JOptionPane.showMessageDialog(parent, msg, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/* Show exception */
	public static void show(Exception e, String title, JFrame parent) {
		String msg = e.getMessage();
		if( msg == null ) msg = lang.get("gui_lighterror_general_msg");
		show(msg,title,parent);
	}
	
	// SIGNATURE OVERLOAD -- missing title
	public static void show(String msg, JFrame parent) { show(msg,lang.get("gui_lighterror_msg_title"),parent); }
	public static void show(Exception e, JFrame parent) { show(e,lang.get("gui_lighterror_msg_title"),parent); }
	
	// SIGNATURE OVERLOAD -- missing parent
	public static void show(String msg, String title) { show(msg,title,(JFrame)null); }
	public static void show(Exception e, String title) { show(e,title,(JFrame)null); }
	
	// SIGNATURE OVERLOAD -- missing parent and title
	public static void show(String msg) { show(msg,(JFrame)null); }
	public static void show(Exception e) { show(e,(JFrame)null); }
	
}
