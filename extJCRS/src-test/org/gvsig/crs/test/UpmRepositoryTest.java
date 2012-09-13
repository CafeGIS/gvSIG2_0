package org.gvsig.crs.test;

public class UpmRepositoryTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CSCatClient client = new CSCatClient(1);
		String result[] = client.csCatAccess("4258", null);
		System.out.println(result[0]);
	}

}
