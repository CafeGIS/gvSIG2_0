package org.gvsig.dwg.lib;

public class DwgClass2004 extends DwgClass {

	int numberObject;
	int maintenanceVersion;
	int DwgVersion;
	int unknown1;
	int unknown2;

	public DwgClass2004(int classNum, int version, String appName, String cPlusPlusName,
			String dxfName, boolean isZombie, int id, int numberObject,
			int maintenanceVersion,int DwgVersion,int unknown1, int unknown2) {
		super( classNum, version,  appName,
    		 cPlusPlusName, dxfName, isZombie, id);

        this.numberObject = numberObject;
        this.maintenanceVersion = maintenanceVersion;
        this.DwgVersion = DwgVersion;
        this.unknown1 = unknown1;
        this.unknown2 = unknown2;

	}

	public String toString(){
    	String solution = "";
    	solution += "classNum ="+classNum;
    	solution += ", version = "+version;
    	solution += ", appName = "+appName;
    	solution += ", c++Name = "+cPlusPlusName;
    	solution += ", dxfName = "+dxfName;
    	solution += ", isZombie = " + isZombie;
    	solution += ", numberObject = " + numberObject;
    	solution += ", maintenanceVersion = " + maintenanceVersion;
    	solution += ", DwgVersion = " + DwgVersion;
    	solution += ", unknown1 = " + unknown1;
    	solution += ", unknown2 = " + unknown2;
    	return solution;
    }

	/**
	 * @return Returns the numberObject.
	 */
	public int getNumberObject() {
		return numberObject;
	}
	/**
	 * @param numberObject The numberObject to set.
	 */
	public void setNumberObject(int numberObject) {
		this.numberObject = numberObject;
	}

	/**
	 * @return Returns the maintenanceVersion.
	 */
	public int getMaintenanceVersion() {
		return maintenanceVersion;
	}
	/**
	 * @param maintenanceVersion The maintenanceVersion to set.
	 */
	public void setMaintenanceVersion(int maintenanceVersion) {
		this.maintenanceVersion = maintenanceVersion;
	}


	/**
	 * @return Returns the DwgVersion.
	 */
	public int getDwgVersion() {
		return DwgVersion;
	}
	/**
	 * @param DwgVersion The DwgVersion to set.
	 */
	public void setDwgVersion(int DwgVersion) {
		this.DwgVersion = DwgVersion;
	}


}