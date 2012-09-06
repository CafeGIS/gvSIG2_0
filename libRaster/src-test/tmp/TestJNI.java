package tmp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.gva.cit.jgdal.Gdal;
import es.gva.cit.jgdal.GdalException;

public class TestJNI extends Thread{
	
	public class Internal extends Thread{
		public void run() {
			System.out.println("1");
			System.loadLibrary("jgdal");
			Gdal g = new Gdal();
			try {
				g.open("/home/nacho/images/03AUG23153350-M2AS-000000122423_01_P001.TIF", 0);
				System.out.println(g.getRasterXSize() + " " + g.getRasterYSize());
			} catch (GdalException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("2");
		}
	}

	public void run(){
		JFrame frame = new JFrame();
		frame.setSize(410, 200);
		JPanel p = new JPanel();
		JLabel l = new JLabel("VENTANA PRINCIPAL");
		p.setLayout(new BorderLayout());
		JButton b = new JButton("Cerrar");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("CERRAR MAIN");
			}
		});
		p.add(l, BorderLayout.NORTH);
		p.add(b, BorderLayout.CENTER);

		frame.getContentPane().add(p);
		frame.setVisible(true);
		//frame.show();
		
		/*while(true){
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("END");
		}*/
	}
	
	public static void main(String arg[]) {
		TestJNI t = new TestJNI();
		Internal t1 = t.new Internal();
		t.run();
		t1.run();
	}

}
