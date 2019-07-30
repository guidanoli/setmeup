package gui.dialog.preferences.types;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import gui.dialog.preferences.PreferenceType;
import gui.dialog.preferences.types.mixed.MixedPreferenceTypeListener;
import vars.Language;

/**
 * 
 * A mixed preference type mixes difference Preference Type into one.
 * The so called sub panels are arranged one after the other either
 * horizontally or vertically.
 * 
 * The preference type state is delegated to the listener that is
 * passed by the constructor.
 * 
 * @author guidanoli
 *
 */
public class MixedPreferenceType implements PreferenceType {

	/**
	 * 
	 * <p>Mixed preference sub panels orientation
	 * <p>Dictates in which direction will the sub panels
	 * be displayed: horizontally or vertically
	 * 
	 * @author guidanoli
	 *
	 */
	public enum Orientation {
		HORIZONTAL,
		VERTICAL;
	}
	
	private MixedPreferenceTypeListener listener;
	private JPanel panel;
	
	public MixedPreferenceType(MixedPreferenceTypeListener listener, Orientation orientation) {
		this.listener = listener;
		int orientationId = orientation == Orientation.HORIZONTAL ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS; 
		panel = new JPanel(new BoxLayout(panel, orientationId));
		for(PreferenceType subPanel : listener) panel.add((Component) subPanel);
	}
	
	public MixedPreferenceType(MixedPreferenceTypeListener listener) {
		this(listener, Orientation.VERTICAL); // default is vertical
	}

	public JPanel getPanel(Language lang) { return panel; }

	public void setState(String value) {
		for(PreferenceType subPanel : listener) {
			String subState = listener.getSubPanelString(value, subPanel);
			subPanel.setState(subState);
		}
	}

	public String getState() {
		return listener.getPanelState();
	}

	public boolean validateValue(String value) {
		for(PreferenceType subPanel : listener) {
			String subState = listener.getSubPanelString(value, subPanel);
			if(!subPanel.validateValue(subState)) return false;
		}
		return listener.validatePanelState();
	}
	
}
