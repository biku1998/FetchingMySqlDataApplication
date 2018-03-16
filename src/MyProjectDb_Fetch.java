import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.sql.*;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class MyProjectDb_Fetch extends JFrame implements ActionListener {
	JComboBox db, table;
	JButton b1, b2, b3,adddb , removedb;
	JLabel dbt, tabt, headadd,dbnam,removedbl;
	String dbselect;
	JTextArea ad_dbtext,removedbt;

	public MyProjectDb_Fetch() {
		super.getContentPane().setBackground(Color.LIGHT_GRAY);
		super.setTitle("Database Fetch Application");
		super.setBounds(300, 150, 900, 500);
		super.setResizable(false);
		// --

		dbt = new JLabel("Databases On this Machine");
		dbt.setBounds(50, 20, 200, 30);
		super.add(dbt);

		db = new JComboBox();
		db.setBounds(50, 50, 200, 30);
		super.add(db);

		tabt = new JLabel("Tables In Database");
		tabt.setBounds(490, 20, 200, 30);
		super.add(tabt);

		table = new JComboBox();
		table.setBounds(490, 50, 200, 30);
		super.add(table);

		b1 = new JButton("Select Database");
		b1.setBounds(258, 50, 150, 30);
		super.add(b1);
		b1.setVisible(false);
		b1.addActionListener(this);

		b2 = new JButton("Select Table");
		b2.setBounds(700, 50, 150, 30);
		super.add(b2);
		b2.setVisible(false);
		b2.addActionListener(this);

		b3 = new JButton("Sync");
		b3.setBounds(500, 300, 230, 50);
		b3.setBackground(Color.lightGray);
		super.add(b3);
		b3.addActionListener(this);
		
		
		headadd  = new JLabel("Add Database ");
		headadd.setBounds(50, 250, 100, 35);
		super.add(headadd);
		
		ad_dbtext = new JTextArea();
		ad_dbtext.setBounds(190, 280, 190, 25);
		super.add(ad_dbtext);
		
		dbnam  = new JLabel("Enter Database Name ");
		dbnam.setBounds(50, 280, 150, 35);
		super.add(dbnam);
		
		adddb =  new JButton("Add");
		adddb.setBounds(399, 280, 65, 30);
		super.add(adddb);
		adddb.addActionListener(this);
		
		/*
		removedb=  new JButton("Remove this database");
		removedb.setBounds(180, 350, 200, 30);
		super.add(removedb);
		removedb.addActionListener(this);*/
		
		
		
		
	
		
		
		

		// --
		super.setLayout(null);
		super.setVisible(true);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public static void main(String[] args) {
		MyProjectDb_Fetch ref = new MyProjectDb_Fetch();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == b3) {
			
			db.removeAllItems();
			b1.setVisible(true);
			
			

			Connection conn = ConnectionProvider.getConnection();

			try {
				Statement st = conn.createStatement();

				ResultSet rs = st.executeQuery("show databases");

				String dbName = "";

				while (rs.next()) {

					dbName = dbName + rs.getString(1) + ":";
				}

				String[] dname = dbName.split(":");

				for (int i = 0; i < dname.length; i++) {
					db.addItem(dname[i]);
				}

			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}

		if (e.getSource() == b1) {
			b2.setVisible(true);
			
			dbselect = (String) db.getSelectedItem();
			
			Connection conn = ConnectionProvider.getConnection();
			try {
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery("show tables in " + dbselect);
				String tabName = "";
				while (rs.next()) {
					tabName = tabName + rs.getString(1) + ":";
				}

				table.removeAllItems();

				String[] tabar = tabName.split(":");
				for (int i = 0; i < tabar.length; i++) {
					table.addItem(tabar[i]);
				}

			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}

		if (e.getSource() == b2) {
			String tbselect = (String) table.getSelectedItem();
			Connection con = ConnectionProvider.getConnection();
			try {

				Statement st = con.createStatement();

				ResultSet rs = st.executeQuery("select * from " + dbselect + "." + tbselect);
				ResultSetMetaData rsmd = rs.getMetaData();
				int cols = rsmd.getColumnCount();
				
				String colName = "";

				for (int i = 1; i <= cols; i++) {
					colName = colName + rsmd.getColumnName(i) + "\t";
				}

				colName = colName + "\n\n";

				while (rs.next()) {
					for (int i = 1; i <= cols; i++) {

						colName = colName + rs.getString(rsmd.getColumnName(i)) + "\t:";

					}

					colName = colName + "\n";
				}
				
				
				FileOutputStream fos = new FileOutputStream("c:/abc/"+tbselect+".txt");
				fos.write(colName.getBytes());
				fos.close();
				
				Runtime.getRuntime().exec("cmd /c start "+"c:/abc/"+tbselect+".txt");

			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
		
		
		if(e.getSource()==adddb) {
			Connection conn = ConnectionProvider.getConnection();
			try {
				Statement st = conn.createStatement();
				String dbgen = ad_dbtext.getText().toString();
				
				if(dbgen.equals("")) {
					JOptionPane.showMessageDialog(null, "Database Name required !!!!");
				}else {
					st.executeUpdate("create database "+dbgen);
					JOptionPane.showMessageDialog(null, "Database added pls Sync !!!!");
					ad_dbtext.setText("");
				}
				
				
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
		
		if(e.getSource()==removedb) {
			
			/*
			 * Connection conn = ConnectionProvider.getConnection(); try { String dbtormv =
			 * (String)db.getSelectedItem(); Statement st = conn.createStatement(); int
			 * index = db.getSelectedIndex();
			 * 
			 * st.executeUpdate("drop database "+dbtormv); db.remove(index);
			 * JOptionPane.showMessageDialog(null, index);
			 * 
			 * 
			 * 
			 * } catch (Exception e2) { e2.printStackTrace(); }
			 */
		

	}}}


