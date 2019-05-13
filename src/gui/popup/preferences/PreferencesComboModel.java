package gui.popup.preferences;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;



@SuppressWarnings("serial")
public class PreferencesComboModel extends AbstractListModel<String> implements ComboBoxModel<String> {

	public final static int KEY = 0;
	public final static int LABEL = 1;
	public final static int TYPE = 2;
	public final static int RESET = 3;
	
	protected String selected;
	protected Object[][] list = { 
			{ "path" , vars.Language.get("gui_popup_preferences_proplabel_path") , new DirectoryPreferenceType() , false } ,
			{ "lang" , vars.Language.get("gui_popup_preferences_proplabel_lang") , new ComboPreferenceType(vars.Language.langs) , true } ,
	};
	
	public String getElementAt(int i) { return (String) list[i][LABEL]; }
	public int getSize() { return list.length; }
	public void setSelectedItem(Object anItem) { selected = (String) anItem; }
	public String getSelectedItem() { return (String) selected; }
	public Object getSelectedItemProperty(int index)
	{
		for( Object[] item : list )
		{
			if( item[LABEL] == selected )
				return item[index];
		}
		return null;
	}

}
