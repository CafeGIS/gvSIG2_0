package org.gvsig.gui.beans.comboboxconfigurablelookup.programmertests;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.comboboxconfigurablelookup.DefaultComboBoxConfigurableLookUpModel;
import org.gvsig.gui.beans.comboboxconfigurablelookup.JComboBoxConfigurableLookUp;



/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */

/**
 * <p>Tests the class {@link JComboBoxConfigurableLookUp JComboBoxConfigurableLookUp} .</p>
 * 
 * <p>Notes:
 *  <ul>
 *   <li>MORE TESTS COULD BE ADDED!!!</li>
 *   <li>DON'T REMOVE TEST COMMENTED!!!</li>
 *  </ul>
 * </p>
 *
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 * @version 07/02/2008
 */
public class TestJComboBoxConfigurableLookUp extends JFrame {
	private static final long serialVersionUID = -422198204525608333L;

	/**
	 * Test method for the <code>TestJComboBoxConfigurableLookUp</code>
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		int width = 300;
		int height = 60;
		
		try
		{
			// Objects creation
			JFrame jF = new JFrame();
	
			// Create a TestJComboBoxConfigurableLookUp, sets the behavior configuration and add Items
			
			// ---- TESTS ----
			// Test the default behavior
			JComboBoxConfigurableLookUp jCBSD = new JComboBoxConfigurableLookUp();

			// -- TESTS FLAGS FOR CONTROL --
			// Test the change of the flag 'onlyOneColor'
			// jCBSD.setOnlyOneColorOnText(true);
			// jCBSD.setOnlyOneColorOnText(false);
			
			// Test the change of the flag 'beepEnabled'
			// jCBSD.setBeepEnabled(false);
			// jCBSD.setBeepEnabled(true);

			// Test the change of the flag 'hidePopupIfThereAreNoItems'
			// jCBSD.setHidePopupIfThereAreNoItems(false);
			// jCBSD.setHidePopupIfThereAreNoItems(true);
			
			// Test the change of the flag 'toForceSelectAnItem'
			// jCBSD.setToForceSelectAnItem(false);
			// jCBSD.setToForceSelectAnItem(true);
			

			// --- TESTS ADDING A MODEL ---
			// -- TESTS FLAGS FOR THE MODEL --
			// DefaultComboBoxConfigurableLookUpModel model = new DefaultComboBoxConfigurableLookUpModel();
			DefaultComboBoxConfigurableLookUpModel model = new DefaultComboBoxConfigurableLookUpModel(new Object[] {"Item1", "Item2", "Item3", "Item4", "Item5"});
			// DefaultComboBoxConfigurableLookUpModel model = (DefaultComboBoxConfigurableLookUpModel) jCBSD.getModel();
				
			// Test the change of the flag 'itemsShownInListBox'
			// model.setLanguageRules("en_US");
			// model.setLanguageRules("fr_FR");
			// model.setLanguageRules("es_ES");
			// model.setLanguageRules(DefaultComboBoxConfigurableLookUpModel.DEFAULT_LANGUAGE_RULES_CONFIGURATION);
				
			// Test the change of the flag 'caseSensitive'
			// model.setCaseSensitive(false);
			// model.setCaseSensitive(DefaultComboBoxConfigurableLookUpModel.CASE_INSENSITIVE);
			// model.setCaseSensitive(true);
			// model.setCaseSensitive(DefaultComboBoxConfigurableLookUpModel.CASE_SENSITIVE);
			 
			// Test the change of the flag 'itemsOrder'
			// model.setItemsOrder(DefaultComboBoxConfigurableLookUpModel.MAINTAIN_POSITION);
			// model.setItemsOrder(DefaultComboBoxConfigurableLookUpModel.ALPHABETICAL_ORDERED);
			// model.setItemsOrder(DefaultComboBoxConfigurableLookUpModel.MAINTAIN_AGENT_POSITIONS);
			
			// Test the change of the flag 'itemsShownInListBox'
			// model.setShowAllItemsInListBox(DefaultComboBoxConfigurableLookUpModel.SHOW_ALL_ITEMS);
			// model.setShowAllItemsInListBox(true);
			// model.setShowAllItemsInListBox(DefaultComboBoxConfigurableLookUpModel.SHOW_ONLY_MATCHES);
			// model.setShowAllItemsInListBox(false);

			// Test the change of the flag 'completeArrowKeySelection'
			// jCBSD.setCompleteArrowKeySelection(true);
			// jCBSD.setCompleteArrowKeySelection(false);

			// Test the change of the flag 'displayAllItemsWithArrowButton'
			// jCBSD.setDisplayAllItemsWithArrowButton(false);
			 jCBSD.setDisplayAllItemsWithArrowButton(true);

			// Adds the new model
			jCBSD.setModel(model);

			// --- ADD ITEMS TO THE COMPONENT AND OTHER TESTS ---
			// Add some items to test if has too much delay with an average number of items
			jCBSD.addItem("extAddEventTheme");
			jCBSD.addItem("ñandú");
			jCBSD.addItem("_fwAndami");
			jCBSD.addItem("extJDBC");

			jCBSD.addItem("extCAD");
			jCBSD.addItem("libuiDownCase");
			jCBSD.addItem("appgvSig");
			// jCBSD.setSelectedItem("appgvSig");
			// jCBSD.removeAllItems();
			jCBSD.addItem("RemoteServices");
			jCBSD.addItem("Ñandú");	
			jCBSD.addItem("Ç");
			// jCBSD.setSelectedItem("appgvSig");
			// jCBSD.setSelectedItem(jCBSD.getItemAt(3)); //Test setSelectedItem
			jCBSD.addItem("extNomenclatorIGN");

            // jCBSD.setSelectedItem(jCBSD.getItemAt(1)); //Test setSelectedItem
            // jCBSD.setSelectedIndex(1); //Test setSelectedIndex
			jCBSD.addItem("extWMS");
			jCBSD.addItem("ÑANDÚ");
			jCBSD.addItem("window");
			jCBSD.addItem("ç");
			jCBSD.addItem("LIBUI");
            jCBSD.setSelectedItem("window");
			jCBSD.addItem("libCorePlugin");
			jCBSD.addItem("libCq CMS for java");
			jCBSD.addItem("libDriverManager");

            // jCBSD.setSelectedItem("libDriverManager");
			////				jCBSD.addItem("libFMap");
			////				jCBSD.addItem("libG_fwAndamiDBMS");
			////				jCBSD.addItem("libIverUtiles");
			////				jCBSD.addItem("libNomenclatorIGN");
			////				jCBSD.addItem("libNomenclatorIGN_GUI");
			////				jCBSD.addItem("libRemoteServices");
			//////				jCBSD.addItem("extNomenclatorIGN"); // Duplication test
			////				jCBSD.addItem("libUI");
			////				jCBSD.addItem("a");
			////				jCBSD.addItem("aa");
			////				jCBSD.addItem("aaa");
			////				jCBSD.addItem("aaaa");
			////				jCBSD.addItem("aaaaa");
			////				jCBSD.addItem("b");
			////				jCBSD.addItem("bbb");
			////				jCBSD.addItem("bb");
			////				jCBSD.addItem("c");
			////				jCBSD.addItem(".");
			////				jCBSD.addItem("ccc");
			////				jCBSD.addItem("cc");
			////				jCBSD.addItem("cccc");
			////				jCBSD.addItem("ddd");
			////				jCBSD.addItem("d");
			////				jCBSD.addItem("dd");
			////				jCBSD.addItem("dddd");
			////				jCBSD.addItem("e");
			////				jCBSD.addItem("eee");
			////				jCBSD.addItem("ee");
			////				jCBSD.addItem("eeee");
			//				jCBSD.addItem("ff");
			//				jCBSD.addItem("f");
			////				jCBSD.addItem("LIBZZMAYUSCULA1");
			////				jCBSD.addItem("ff"); // Duplication test
			////				jCBSD.addItem("asldf");
			////				jCBSD.addItem("wej");
			////				jCBSD.addItem("asdf");
			////				jCBSD.addItem("LIBMAYUSCULA2");
			////				jCBSD.addItem("qera");
			////				jCBSD.addItem("zxvcas");
			////				jCBSD.addItem("wea");
			////				jCBSD.addItem("asr");
			////				jCBSD.addItem("asra");
			////				jCBSD.addItem("LIBMAYUSCULA1");
			////				jCBSD.addItem("rar");
			////				jCBSD.addItem("afda");
			////				jCBSD.addItem("ljvkgk");
			////				jCBSD.addItem("zcv");
			////				jCBSD.addItem("gfhdt");
			////				jCBSD.addItem("dfhgd");
			////				jCBSD.addItem("dfh");
			////				jCBSD.addItem("dfhs");
			////				jCBSD.addItem("sfszv");
			////				jCBSD.addItem("qer");
			////				jCBSD.addItem("adfazva");
			////				jCBSD.addItem("xnd");
			////				jCBSD.addItem("vdkjgd");
			////				jCBSD.addItem("ddhd");
			////				jCBSD.addItem("dxgx");
			
			////				jCBSD.addItem("cnsf");
			////				jCBSD.addItem("ssfgs");
			////				jCBSD.addItem("sgfsbx");
			////				jCBSD.addItem("sfxb");
			////				jCBSD.addItem("chdgk");
			////				jCBSD.addItem("jgu");
			////				jCBSD.addItem("gkgj");
			////				jCBSD.addItem("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccddddddddddddddddddddddddddddddddddddddddddddddeeeeeeeeeeeeeeeeeeeeeeeeeee");
			////				jCBSD.addItem("ljkoñj.n");
			////				jCBSD.addItem("hfjf");
			////				jCBSD.addItem("xbxb");
			////				jCBSD.addItem(".,lhhi");
			////				jCBSD.addItem("hklx_fwAndami");
			////				jCBSD.addItem("cvn");
			////				jCBSD.addItem("dgh");
			////				jCBSD.addItem("cncv");
			////				jCBSD.addItem("sg");
			////				jCBSD.addItem("b,mjk");
			////				jCBSD.addItem("xv");
			////				jCBSD.addItem("sgghu");
			////				jCBSD.addItem("saxc");
			////				jCBSD.addItem("srts");
			////				jCBSD.addItem("sduyg");
			////				jCBSD.addItem("sxfsrs");
			////				jCBSD.addItem("rsdtf");
			////				jCBSD.addItem("po`llh");
			////				jCBSD.addItem("fghtf54j");
			////				jCBSD.addItem("s25ewsd");
			////				jCBSD.addItem("uo8khkj");
			////				jCBSD.addItem("sfg45");
			////				jCBSD.addItem("3gd");
			////				jCBSD.addItem("5dhd");
			////				jCBSD.addItem("sg4dh6");
			////				jCBSD.addItem("dfh5");
			////				jCBSD.addItem("s4hfj74");
			////				jCBSD.addItem("sdg534");
			////				jCBSD.addItem("2452etdfg");
			////				jCBSD.addItem("2ui068");
			////				jCBSD.addItem("3dsd sgr");
			////				jCBSD.addItem("sr sgsr");
			////				jCBSD.addItem("sssssssss");
			////				jCBSD.addItem("aaaaass");
			////				jCBSD.addItem("ss");
			////				jCBSD.addItem("sry");
			////				jCBSD.addItem("hñhj");
			////				jCBSD.addItem("gkgb");
			////				jCBSD.addItem("gkgu");
			////				jCBSD.addItem("gjugvfc");
			////				jCBSD.addItem("dghdd");
			////				jCBSD.addItem("dhdt");
			////				jCBSD.addItem("dhgd");
			////				jCBSD.addItem("ddtughd");
			////				jCBSD.addItem("ffukljñd");
			////				jCBSD.addItem("jñolk.j");
			////				jCBSD.addItem("las alupoai añieurpay");
			////				jCBSD.addItem("tytresd");
			////				jCBSD.addItem("fgkg");
			////				jCBSD.addItem("lhh");
			////				jCBSD.addItem("hkhlhip");
			////				jCBSD.addItem("k´popi0¿?=(J");
			////				jCBSD.addItem("af¿?=)(/?/");
			////				jCBSD.addItem("-a,malj'=)/");
			////				jCBSD.addItem("2gw");
			////				jCBSD.addItem("5teu");
			////				jCBSD.addItem("4576jfff");
			////				jCBSD.addItem("urtyu");
			////				jCBSD.addItem("fi7");
			////				jCBSD.addItem("git7t4h");
			////				jCBSD.addItem("hr6frr");
			////				jCBSD.addItem("ehe5et");
			////				jCBSD.addItem("eue5klhj");
			////				jCBSD.addItem("yoiyoy");
			////				jCBSD.addItem("yoyi");
			////				jCBSD.addItem("sgcsc");
			////				jCBSD.addItem("sgmff74");
			////				jCBSD.addItem("dd43d d6dhlhju");
			////				jCBSD.addItem("gkjngr");
			////				jCBSD.addItem("dbddht");
			////				jCBSD.addItem("sbd");
			////				jCBSD.addItem("dcbd5opihjj");
			////				jCBSD.addItem("pujtth");
			////				jCBSD.addItem("ZZZZZ");
			////				jCBSD.addItem("ytht");
			////				jCBSD.addItem("tjgt");
			////				jCBSD.addItem("rrf");
			////				jCBSD.addItem("BLIUEÑ ");
			////				jCBSD.addItem("ÑANDÚ");
			////				jCBSD.addItem("axaaa4iktiu");
			////				jCBSD.addItem("tttyir");
			////				jCBSD.addItem("ruyrrruu4");
			////				jCBSD.addItem("rryewrsw");
			////				jCBSD.addItem("wertw");
			////				jCBSD.addItem("wer26e");
			////				jCBSD.addItem("eceye");
			////				jCBSD.addItem("etye7yyy-er");
			////				jCBSD.addItem("eyert7e7e7");
			////				jCBSD.addItem("_abcdefghijklmnñopqrstuvwxyz");
			////				jCBSD.addItem("_0123456789");
			////				jCBSD.addItem("sstsnmmsutt");
			////				jCBSD.addItem("dcytdtyd");
			////				jCBSD.addItem("dcdtdty");
			////				jCBSD.addItem("dctycd");
			////				jCBSD.addItem("cstyd");
			////				jCBSD.addItem("dbddcytdsssc");
			////				jCBSD.addItem("cdydt");
			////				jCBSD.addItem("scscydu");
			////				jCBSD.addItem("axct");
			////				jCBSD.addItem("joobfvd");
			////				jCBSD.addItem("jomj");
			////				jCBSD.addItem("omjo");
			////				jCBSD.addItem("mol");
			////				jCBSD.addItem("agdhfj");
			////				jCBSD.addItem("fjfhjuh,ooom");
			////				jCBSD.addItem("affjfa");
			////				jCBSD.addItem("afjfjyfga");
			////				jCBSD.addItem("NOMENCLATOR");
			////				jCBSD.addItem("asg");
			////				jCBSD.addItem("afaea");
			////				jCBSD.addItem("gvSIG");
			////				jCBSD.addItem("yoiey");
			////				jCBSD.addItem("35");
			////				jCBSD.addItem("ewgw2dh");
			////				jCBSD.addItem("titgr");
			////				jCBSD.addItem("o9yky");
			////				jCBSD.addItem("kyio8gbr");
			////				jCBSD.addItem("eve5yed574rir");
			////				jCBSD.addItem("578tygmtio");
			////				jCBSD.addItem("t43262ryteye");
			////				jCBSD.addItem("wvwx");
			////				jCBSD.addItem("zssscwwwwww");
			////				jCBSD.addItem("sgvr");
			////				jCBSD.addItem("...");
			//				jCBSD.addItem("ÚLTIMO ITEM EN AÑADIRSE");
			
			// --- END ADD ITEMS TO THE COMPONENT AND OTHER TESTS ---
			
			// Test of no item default selection 
			// jCBSD.setSelectedIndex(-1);
			
			// Other tests
			// jCBSD.setSelectedIndex(0);
			// System.out.println("Selected: " + jCBSD.getSelectedItem());
			// jCBSD.setSelectedIndex(3);
			// System.out.println("Item at 3: " + jCBSD.getItemAt(3));
			// jCBSD.setSelectedItem("ÑANDÚ");

			// System.out.println("Index of LIBUI: " + ((DefaultComboBoxConfigurableLookUpModel)jCBSD.getModel()).getIndexOf("LIBUI"));
			// jCBSD.addItem("AÑADIDO");
			// jCBSD.removeItemAt(0);
			// jCBSD.removeAllItems();
			// ((DefaultComboBoxConfigurableLookUpModel)jCBSD.getModel()).removeAllElements();
			// System.out.println("Number of elements (list box): " + jCBSD.getItemCount());

			// Tests the method ''setPrototypeDisplayValue''
			// jCBSD.addItem("ABCDEFGHIJKLMNÑOPQRSTUVWXYZ-ABCDEFGHIJKLMNÑOPQRSTUVWXYZ-ABCDEFGHIJKLMNÑOPQRSTUVWXYZ");
			// jCBSD.setSelectedItem("ABCDEFGHIJKLMNÑOPQRSTUVWXYZ-ABCDEFGHIJKLMNÑOPQRSTUVWXYZ-ABCDEFGHIJKLMNÑOPQRSTUVWXYZ");
			// jCBSD.setPrototypeDisplayValue("ABCDEFGHIJKLMNÑOPQRSTUVWXYZ");
			
			// Configure the JFrame
			jF.setTitle("Test JComboBoxConfigurableLookUp");
			jF.setSize(new Dimension(width, height));	    
		    jF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    jF.getContentPane().add(jCBSD);
		    jF.setVisible(true);

		    // More tests
			// jCBSD.setSelectedIndex(0); // Failures before the component could be showed
			// System.out.println("Selected: " + jCBSD.getSelectedItem());
			// jCBSD.setSelectedIndex(3); // Failures before the component could be showed
			// System.out.println("Item at 3: " + jCBSD.getItemAt(3));
			// jCBSD.setSelectedItem("ÑANDÚ"); // Failures before the component could be showed
		    
		    // Test a Look up Agent
		    // model.setLookUpAgent(new SampleAgent());
		    
		    // Test changing the renderer
		    // jCBSD.setRenderer(new SampleBasicComboBoxRenderer());
		    
			// More tests
			// jCBSD.removeAllItems();
			// jCBSD.addItem("extCAD");
			// jCBSD.addItem("libuiDownCase");
			// jCBSD.addItem("appgvSig");
			// jCBSD.removeAllItems();
			// jCBSD.addItem("extCAD");
			// jCBSD.addItem("libuiDownCase");
			// jCBSD.addItem("appgvSig");
			// jCBSD.setSelectedItem("libuiDownCase");
		    
		    // Test change the editor
		    // jCBSD.setEditor(new SampleComboBoxEditor());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, Messages.getText("testJComboBoxConfigurableLookUpErrorMessage"), Messages.getText("error"), JOptionPane.ERROR_MESSAGE);
		}
	}
}
