package gui.dialog.preferences.types.mixed;

import java.util.ArrayList;
import java.util.Iterator;

import gui.dialog.preferences.PreferenceType;
import gui.dialog.preferences.types.ComboPreferenceType;
import gui.dialog.preferences.types.NumberPreferenceType;
import gui.dialog.preferences.types.combo.ByteUnitsCombo;
import gui.dialog.preferences.types.combo.ComboPreferenceTypeListener;
import gui.error.FatalError;
import vars.Language;

public class FileSizePreferenceType implements MixedPreferenceTypeListener {

	private NumberPreferenceType numberPanel;
	private ComboPreferenceType dimesionPanel;
	private ArrayList<PreferenceType> subPanels;
	private ComboPreferenceTypeListener comboListener;
	
	private long maxSize;
	
	public FileSizePreferenceType(long maximumFileSize) {
		if( maximumFileSize <= 0 ) FatalError.show("Invalid maximum file size");
		maxSize = maximumFileSize;
		subPanels = new ArrayList<PreferenceType>();
		numberPanel = new NumberPreferenceType(1,1023);
		comboListener = new ByteUnitsCombo();
		dimesionPanel = new ComboPreferenceType(comboListener);
		subPanels.add(numberPanel);
		subPanels.add(dimesionPanel);
	}

	public Iterator<PreferenceType> iterator() { return subPanels.iterator(); }

	private Long getNumericValue() {
		String numberString = numberPanel.getState();
		long number = Long.parseLong(numberString);
		String multiplierString = dimesionPanel.getState();
		long multiplier = Long.parseLong(multiplierString);
		return number * multiplier;
	}
	
	public String getPanelState() {
		return Long.toString(getNumericValue());
	}

	public boolean validatePanelState() {
		return getNumericValue() <= maxSize;
	}

	public String getSubPanelString(String state, PreferenceType subPanel) {
		Long numericValue = Long.parseLong(state);
		if( subPanel == numberPanel ) {
			return Long.toString(numericValue % 1024);
		}
		else {
			int index = (int) (numericValue / 1024);
			Language lang = Language.getInstance();
			String [] dimensionArray = comboListener.getOptionToolTips(lang);
			return dimensionArray[index];
		}
	}

	public Orientation getOrientation() { return Orientation.HORIZONTAL; }

}
