package es.unex.sextante.gvsig.core;

import javax.swing.JDialog;

import com.iver.andami.Utilities;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.project.Project;

import es.unex.sextante.core.ITaskMonitor;
import es.unex.sextante.core.OutputFactory;
import es.unex.sextante.dataObjects.IRasterLayer;
import es.unex.sextante.dataObjects.ITable;
import es.unex.sextante.dataObjects.IVectorLayer;
import es.unex.sextante.exceptions.UnsupportedOutputChannelException;
import es.unex.sextante.gui.core.DefaultTaskMonitor;
import es.unex.sextante.outputs.FileOutputChannel;
import es.unex.sextante.outputs.IOutputChannel;
import es.unex.sextante.rasterWrappers.GridExtent;

/**
 * An OutputFactory based on the gvSIG data model.
 * Supports only file-based outputs.
 * @author volaya
 *
 */
public class gvOutputFactory extends OutputFactory {

	public IVectorLayer getNewVectorLayer(String sName,
										  int iShapeType,
										  Class[] types,
										  String[] sFields,
										  IOutputChannel channel,
										  Object crs)
				throws UnsupportedOutputChannelException {

		if (channel instanceof FileOutputChannel){
			String sFilename = ((FileOutputChannel)channel).getFilename();
			gvVectorLayer layer = new gvVectorLayer();
			layer.create(sName, sFilename, iShapeType,
				types, sFields, crs);

			return layer;
		}
		else{
			throw new UnsupportedOutputChannelException();
		}

	}


	public IRasterLayer getNewRasterLayer(String sName,
											int iDataType,
											GridExtent extent,
											int iBands,
											IOutputChannel channel,
											Object crs)
				throws UnsupportedOutputChannelException {


		if (channel instanceof FileOutputChannel){
			String sFilename = ((FileOutputChannel)channel).getFilename();
			gvRasterLayer layer = new gvRasterLayer();
			layer.create(sName, sFilename, extent,
						iDataType, iBands, crs);
			return layer;
		}
		else{
			throw new UnsupportedOutputChannelException();
		}



	}

	public ITable getNewTable(String sName, Class types[],
							String[] sFields, IOutputChannel channel)
					throws UnsupportedOutputChannelException{

		if (channel instanceof FileOutputChannel){
			String sFilename = ((FileOutputChannel)channel).getFilename();
			gvTable table = new gvTable();
			table.create(sName, sFilename, types, sFields);
			return table;
		}
		else{
			throw new UnsupportedOutputChannelException();
		}


	}

	protected String getTempFolder() {

		return Utilities.createTempDirectory();

	}

	public String[] getRasterLayerOutputExtensions() {

		return new String[]{"tif", "asc"};

	}

	public String[] getVectorLayerOutputExtensions() {

		return new String[]{"shp", "dxf"};

	}

	public String[] getTableOutputExtensions() {

		return new String[]{"dbf"};

	}


	public void addMessage(String s) {

		NotificationManager.addInfo(s, null);

	}


	@Override
	public ITaskMonitor getTaskMonitor(String sTitle, boolean bDeterminate,
			JDialog parent) {

		return new DefaultTaskMonitor(sTitle, bDeterminate, parent);

	}


	@Override
	public Object getDefaultCRS() {

		return Project.getDefaultProjection();

	}




}
