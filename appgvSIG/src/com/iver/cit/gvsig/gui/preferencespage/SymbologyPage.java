package com.iver.cit.gvsig.gui.preferencespage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gui.beans.swing.JBlank;
import org.gvsig.gui.beans.swing.JComboBoxFontSizes;
import org.gvsig.gui.beans.swing.JComboBoxFonts;
import org.gvsig.gui.beans.swing.JFileChooser;

import com.iver.andami.PluginServices;
import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.StoreException;
import com.iver.cit.gvsig.gui.panels.ColorChooserPanel;
import com.iver.utiles.StringUtilities;
import com.iver.utiles.XMLEntity;


/**
 * This class extends AbstractPreferencesPage. This component is a preferences page for
 * symbology and allows select default fore color, fill color, font and size text and
 * the path where the images that compound symbols are located.
 *
 */
public class SymbologyPage extends AbstractPreferencePage{

	private static final long serialVersionUID = 1L;

	private ColorChooserPanel defaultColor = null;
	private ColorChooserPanel defaultFillColor = null;
	private JSlider jsDefaultSelectionAlpha = null;
	private JSlider jsFillSelelctionAlpha = null;
	protected String id;
	private boolean panelStarted = false;
	private JButton btnSelectProjectsFolder=null;
	private ActionListener btnFileChooserAction=null;
	private JTextField txtProjectsFolder=null;
	private JComboBoxFonts fonts= null;
	private JComboBoxFontSizes sizes= null;
	private ImageIcon icon=null;
	private JCheckBox aleatoryFillColor;

	private static final String SYMBOL_FORECOLOR_PROPERTY_NAME = "ForeColor";
	private static final String SYMBOL_FILLCOLOR_PROPERTY_NAME = "FillColor";
	private static final String SYMBOL_FONT_TYPE_PROPERTY_NAME = "FontType";
	private static final String SYMBOL_FONT_SIZE_PROPERTY_NAME = "FontSize";
	private static final String SYMBOL_IMAGE_FOLDER_PROPERTY_NAME = "ImageFolder";
	private static final String SYMBOL_ALEATORY_FORECOLOR = "AleatoryForeColor";
	private static final String SYMBOL_ALEATORY_FILLCOLOR = "AleatoryFillColor";


	public SymbologyPage(){
		super();
		id = this.getClass().getName();
	}

	// pending of a proposed refactor, don't erase
//	@Override
//	public void persistPreferences() throws StoreException {
//		File f;
//		String path, propertyName;
//		Color foreColor=defaultColor.getColor();
//		Color fillColor=defaultFillColor.getColor();
//		PluginServices ps = PluginServices.getPluginServices(this);
//		XMLEntity xml = ps.getPersistentXML();
//
//		xml.putProperty(SYMBOL_FORECOLOR_PROPERTY_NAME,
//				StringUtilities.color2String(foreColor));
//
//		xml.putProperty(SYMBOL_FILLCOLOR_PROPERTY_NAME,
//				StringUtilities.color2String(fillColor));
//
//		propertyName = SYMBOL_IMAGE_FOLDER_PROPERTY_NAME;
//		path = txtProjectsFolder.getText();
//
//		if (path.equals("")) {
//			if (xml.contains(propertyName)) {
//				xml.remove(propertyName);
//			}
//		} else {
//			f = new File(path);
//			if (f.exists()) {
//				if (xml.contains(propertyName)) {
//					xml.remove(propertyName);
//				}
//				xml.putProperty(propertyName, f.getAbsolutePath());
//			}
//		}
//
//		String nameFont=(String)fonts.getSelectedItem();
//		Integer sizeFontAux =(Integer)sizes.getSelectedItem();
//		int sizeFont=sizeFontAux.intValue();
//
//		propertyName=SYMBOL_FONT_TYPE_PROPERTY_NAME;
//		xml.putProperty(propertyName, nameFont);
//
//		propertyName=SYMBOL_FONT_SIZE_PROPERTY_NAME;
//		xml.putProperty(propertyName, sizeFont);
//
//
//	}

	public void persistPreferences() throws StoreException {
		File f;
		String path, propertyName;
		Color foreColor=defaultColor.getColor();
		Color fillColor=defaultFillColor.getColor();
		PluginServices ps = PluginServices.getPluginServices(this);
		XMLEntity xml = ps.getPersistentXML();

		xml.putProperty(SYMBOL_FORECOLOR_PROPERTY_NAME,
				StringUtilities.color2String(foreColor));


		xml.putProperty(SYMBOL_FILLCOLOR_PROPERTY_NAME,
				StringUtilities.color2String(fillColor));
		xml.putProperty(SYMBOL_ALEATORY_FILLCOLOR,aleatoryFillColor.isSelected());

		propertyName = SYMBOL_IMAGE_FOLDER_PROPERTY_NAME;
		path = txtProjectsFolder.getText();

		if (path.equals("")) {
			if (xml.contains(propertyName)) {
				xml.remove(propertyName);
			}
		} else {
			f = new File(path);
			if (f.exists()) {
				if (xml.contains(propertyName)) {
					xml.remove(propertyName);
				}
				xml.putProperty(propertyName, f.getAbsolutePath());
			}
		}

		String nameFont=(String)fonts.getSelectedItem();
		Integer sizeFontAux =(Integer)sizes.getSelectedItem();
		int sizeFont=sizeFontAux.intValue();

		propertyName=SYMBOL_FONT_TYPE_PROPERTY_NAME;
		xml.putProperty(propertyName, nameFont);

		propertyName=SYMBOL_FONT_SIZE_PROPERTY_NAME;
		xml.putProperty(propertyName, sizeFont);


	}


	@Override
	public void setChangesApplied() {
		setChanged(false);

	}

	public void applyValuesFromPersistence() throws StoreException {
		PluginServices ps = PluginServices.getPluginServices(this);
		XMLEntity xml = ps.getPersistentXML();
		xml = ps.getPersistentXML();



		if(xml.contains(SYMBOL_FORECOLOR_PROPERTY_NAME)){
		//Color
			Color color=StringUtilities.string2Color(xml.getStringProperty(SYMBOL_FORECOLOR_PROPERTY_NAME));
			SymbologyFactory.DefaultSymbolColor=color;
		}else{
			SymbologyFactory.DefaultSymbolColor=SymbologyFactory.FactoryDefaultSymbolColor;
		}


		//FillColor
		if(xml.contains(SYMBOL_FILLCOLOR_PROPERTY_NAME)){
			Color colorFill=StringUtilities.string2Color(xml.getStringProperty(SYMBOL_FILLCOLOR_PROPERTY_NAME));
			SymbologyFactory.DefaultFillSymbolColor=colorFill;
		}else{
			SymbologyFactory.DefaultFillSymbolColor=SymbologyFactory.FactoryDefaultFillSymbolColor;
		}

		//Path
		if(xml.contains(SYMBOL_IMAGE_FOLDER_PROPERTY_NAME)){
			SymbologyFactory.SymbolLibraryPath=xml.getStringProperty(SYMBOL_IMAGE_FOLDER_PROPERTY_NAME);
		}else{
			SymbologyFactory.SymbolLibraryPath=SymbologyFactory.FactorySymbolLibraryPath;
		}
		//Font
		if(xml.contains(SYMBOL_FONT_TYPE_PROPERTY_NAME)){
			int size=xml.getIntProperty(SYMBOL_FONT_SIZE_PROPERTY_NAME);
			String type=xml.getStringProperty(SYMBOL_FONT_TYPE_PROPERTY_NAME);
			Font font= new Font(type, Font.BOLD, size);
			SymbologyFactory.DefaultTextFont=font;
		}else{
			SymbologyFactory.DefaultTextFont=SymbologyFactory.FactoryDefaultTextFont;
		}

	}

	public String getID() {
		return id;
	}

	public ImageIcon getIcon() {
		if (icon == null){
			icon=PluginServices.getIconTheme().get("symbol-pref");
		}
		return icon;
	}

	public JPanel getPanel() {
		if(panelStarted)return this;
		panelStarted=true;
		addComponent(new JLabel(" "));

		GridBagLayoutPanel selectionDefaultColorPanel = new GridBagLayoutPanel();
		selectionDefaultColorPanel.setBorder(new TitledBorder(PluginServices.getText(this, "default_color")));
		selectionDefaultColorPanel.setLayout(new GridBagLayout());
		selectionDefaultColorPanel.add(new JLabel(PluginServices.getText(this,"fill")));
		selectionDefaultColorPanel.add(defaultColor = new ColorChooserPanel());

		selectionDefaultColorPanel.add(new JLabel(PluginServices.getText(this,"alpha")));
		selectionDefaultColorPanel.add(jsDefaultSelectionAlpha = new JSlider(0,255));
		selectionDefaultColorPanel.add(new JBlank(50,50));

		jsDefaultSelectionAlpha.setPreferredSize(new Dimension(100,30));
		jsDefaultSelectionAlpha.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				defaultColor.setAlpha(((JSlider)e.getSource()).getValue());
		}});
		addComponent(new JLabel(" "));
		addComponent(selectionDefaultColorPanel);

		GridBagLayoutPanel selectionFillColor = new GridBagLayoutPanel();
		selectionFillColor.setBorder(new TitledBorder(PluginServices.getText(this, "default_fill_color")));
		selectionFillColor.setLayout(new GridBagLayout());
		selectionFillColor.add(new JLabel(PluginServices.getText(this,"fill")));
		selectionFillColor.add(defaultFillColor = new ColorChooserPanel());

		selectionFillColor.add(new JLabel(PluginServices.getText(this,"alpha")));
		selectionFillColor.add(jsFillSelelctionAlpha = new JSlider(0,255));

		jsFillSelelctionAlpha.setPreferredSize(new Dimension(100,30));
		jsFillSelelctionAlpha.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				defaultFillColor.setAlpha(((JSlider)e.getSource()).getValue());
		}});

		selectionFillColor.add(new JBlank(50,50));
		selectionFillColor.add(aleatoryFillColor = new JCheckBox());
		selectionFillColor.add(new JLabel("   " + PluginServices.getText(this,"aleatory")));


		aleatoryFillColor.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == aleatoryFillColor){
					defaultFillColor.setEnabled(!aleatoryFillColor.isSelected());
					jsFillSelelctionAlpha.setEnabled(!aleatoryFillColor.isSelected());
				}
			}

		});

		addComponent(new JLabel(" "));
		addComponent(selectionFillColor);

		btnFileChooserAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path;
				if (e.getSource().equals(btnSelectProjectsFolder)) {
					path = txtProjectsFolder.getText();


				FileFilter def =  new FileFilter(){
					public boolean accept(File f) {
						return (f.isDirectory());
					}

					public String getDescription() {
						return null;
					}
				};

				File file = new File(path);
				JFileChooser fc;
				if (file.exists()) {
					fc = new JFileChooser("SYMBOLOGY_PREFERENCE_PAGE_FILECHOOSER", file);
				} else {
					fc= new JFileChooser("SYMBOLOGY_PREFERENCE_PAGE_FILECHOOSER",JFileChooser.getLastPath("SYMBOLOGY_PREFERENCE_PAGE_FILECHOOSER", file));
				}


				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.setMultiSelectionEnabled(false);
                fc.setAcceptAllFileFilterUsed(false);
                fc.addChoosableFileFilter(def);
                int result = fc.showOpenDialog(SymbologyPage.this);

                if (result == JFileChooser.APPROVE_OPTION && (file = fc.getSelectedFile()) != null)
                	if (e.getSource().equals(btnSelectProjectsFolder))
    					txtProjectsFolder.setText(file.getAbsolutePath());
                }
			}

		};
		btnSelectProjectsFolder = new JButton(PluginServices.getText(this, "browse"));
		btnSelectProjectsFolder.addActionListener(btnFileChooserAction);

		JPanel panelBrowser = new JPanel();
		panelBrowser.setBorder(new TitledBorder(PluginServices.getText(this, "folder_images")));

		panelBrowser.add(txtProjectsFolder = new JTextField(30));
		panelBrowser.add(btnSelectProjectsFolder);

		addComponent(panelBrowser);

		fonts= new JComboBoxFonts();
		sizes = new JComboBoxFontSizes();

		JPanel panelFont = new JPanel(new FlowLayout());
		panelFont.setBorder(new TitledBorder(PluginServices.getText(this, "default_font")));
		panelFont.add(fonts);
		panelFont.add(sizes);

		addComponent(panelFont);
		initializeValues();
		return this;
	}

	public String getTitle() {
		return PluginServices.getText(this, "symbology");
	}
	// pending of a refactoring do not delete (swap commented lines)
//	public void initializeComponents() {
	public void initializeValues() {
		if (!panelStarted) getPanel();

		PluginServices ps = PluginServices.getPluginServices(this);
		XMLEntity xml = ps.getPersistentXML();

		//Default Color
		if (xml.contains(SYMBOL_FORECOLOR_PROPERTY_NAME)) {
			Color color=StringUtilities.string2Color(xml.getStringProperty(SYMBOL_FORECOLOR_PROPERTY_NAME));
			defaultColor.setColor(color);
			defaultColor.setAlpha(color.getAlpha());
			jsDefaultSelectionAlpha.setValue(color.getAlpha());
			SymbologyFactory.DefaultSymbolColor = color;
		}else{
			Color color=SymbologyFactory.FactoryDefaultSymbolColor;
			defaultColor.setColor(color);
			defaultColor.setAlpha(color.getAlpha());
			jsDefaultSelectionAlpha.setValue(color.getAlpha());
			SymbologyFactory.DefaultSymbolColor = color;
		}

		if(xml.contains(SYMBOL_ALEATORY_FILLCOLOR)){
			aleatoryFillColor.setSelected(xml.getBooleanProperty(SYMBOL_ALEATORY_FILLCOLOR));
			SymbologyFactory.DefaultAleatoryFillColor = aleatoryFillColor.isSelected();
		}else{
			aleatoryFillColor.setSelected(SymbologyFactory.FactoryDefaultAleatoryFillColor);
			SymbologyFactory.DefaultAleatoryFillColor = aleatoryFillColor.isSelected();
		}

		//Fill Color
		if (xml.contains(SYMBOL_FILLCOLOR_PROPERTY_NAME)) {
			Color color=StringUtilities.string2Color(xml.getStringProperty(SYMBOL_FILLCOLOR_PROPERTY_NAME));
			defaultFillColor.setColor(color);
			defaultFillColor.setAlpha(color.getAlpha());
			jsFillSelelctionAlpha.setValue(color.getAlpha());
			SymbologyFactory.DefaultFillSymbolColor = color;
		}else{
			Color color=SymbologyFactory.FactoryDefaultFillSymbolColor;
			defaultFillColor.setColor(color);
			defaultFillColor.setAlpha(color.getAlpha());
			jsFillSelelctionAlpha.setValue(color.getAlpha());
			SymbologyFactory.DefaultFillSymbolColor = color;
		}

		defaultFillColor.setEnabled(!aleatoryFillColor.isSelected());
		jsFillSelelctionAlpha.setEnabled(!aleatoryFillColor.isSelected());

		//Path
		if (xml.contains(SYMBOL_IMAGE_FOLDER_PROPERTY_NAME)) {
			String path=xml.getStringProperty(SYMBOL_IMAGE_FOLDER_PROPERTY_NAME);
			txtProjectsFolder.setText(path);
			SymbologyFactory.SymbolLibraryPath = path;
		}else{
			txtProjectsFolder.setText(SymbologyFactory.FactorySymbolLibraryPath);
			SymbologyFactory.SymbolLibraryPath = SymbologyFactory.FactorySymbolLibraryPath;
		}

		//Font Size
		if(xml.contains(SYMBOL_FONT_TYPE_PROPERTY_NAME)){
			String type=xml.getStringProperty(SYMBOL_FONT_TYPE_PROPERTY_NAME);
			fonts.setSelectedItem(type);
		}
		else{
			String font =SymbologyFactory.FactoryDefaultTextFont.getFontName();
			fonts.setSelectedItem(font);
		}

		if(xml.contains(SYMBOL_FONT_SIZE_PROPERTY_NAME)){
			int size=xml.getIntProperty(SYMBOL_FONT_SIZE_PROPERTY_NAME);
			sizes.setSelectedItem(size);
		}
		else{
			int size =SymbologyFactory.FactoryDefaultTextFont.getSize();
			sizes.setSelectedItem(size);
		}
		SymbologyFactory.DefaultTextFont = new Font(fonts.getFont().getName(),fonts.getFont().getStyle(),(Integer) sizes.getSelectedItem());

	}

	public void initializeDefaults() {
		defaultColor.setColor(SymbologyFactory.FactoryDefaultSymbolColor);
		jsDefaultSelectionAlpha.setValue(255);

		defaultFillColor.setColor(SymbologyFactory.FactoryDefaultFillSymbolColor);
		jsFillSelelctionAlpha.setValue(255);

		fonts.setSelectedItem(SymbologyFactory.FactoryDefaultTextFont.getFamily());
		sizes.setSelectedItem(SymbologyFactory.FactoryDefaultTextFont.getSize());
		txtProjectsFolder.setText(SymbologyFactory.FactorySymbolLibraryPath);

		aleatoryFillColor.setSelected(SymbologyFactory.FactoryDefaultAleatoryFillColor);
	}

	public boolean isValueChanged() {
		return super.hasChanged();
	}

	// pending of a refactoring, following method would be removed
	@Override
	public void storeValues() throws StoreException {
		setPropertiesFromPanel();
		persistPreferences();
	}

	private void setPropertiesFromPanel(){

		if(defaultColor.getColor()!=null){
			Color color = defaultColor.getColor();
			color = new Color(color.getRed(),color.getGreen(),color.getBlue(),jsDefaultSelectionAlpha.getValue());
			SymbologyFactory.DefaultSymbolColor = color;
		}

		if(aleatoryFillColor != null){
			SymbologyFactory.DefaultAleatoryFillColor = aleatoryFillColor.isSelected();
		}
		//Fill Color
		if (defaultFillColor.getColor()  !=  null) {
			Color color = defaultFillColor.getColor();
			color = new Color(color.getRed(),color.getGreen(),color.getBlue(),jsFillSelelctionAlpha.getValue());
			SymbologyFactory.DefaultFillSymbolColor = color;
		}

		//Path
		if (txtProjectsFolder.getText() != null) {
			SymbologyFactory.SymbolLibraryPath = txtProjectsFolder.getText();
		}

		//Font Size
		if(fonts.getFont() != null && sizes.getSelectedItem() != null){
			Font font = fonts.getFont();
			font= new Font(fonts.getFont().getFontName(), Font.BOLD, (Integer)sizes.getSelectedItem());
			SymbologyFactory.DefaultTextFont = font;
		}
	}
}
