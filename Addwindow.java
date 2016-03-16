import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Addwindow extends JInternalFrame implements ActionListener {

	//	Dimension screen 	= Toolkit.getDefaultToolkit().getScreenSize();
	JFrame JFParentFrame;
	JDesktopPane desktop;
	private JPanel panel1;
	private JPanel panel2;
	private JButton AddBtn;
	private JButton ResetBtn;
	private JButton ExitBtn;
	private JLabel LblEmp_Code, LblEmp_Name1, LblEmp_Name2, LblEmp_Desi, LblEmp_Add, LblEmp_No;
	private JTextField TxtEmp_Code, TxtEmp_Name1, TxtEmp_Name2, TxtEmp_Add, TxtEmp_No;
	private JComboBox Emp_Type;
	String dialogmessage;
	String dialogs;
	int dialogtype = JOptionPane.PLAIN_MESSAGE;

	public static int record;
	String Emp_Code = "";
	String Emp_Name1 = "";
	String Emp_Name2 = "";
	String Emp_Desi = "";
	String Emp_Add = "";
	String Emp_No = "";

	// Class Variables

	clsConfiguracions settings = new clsConfiguracions();
	clsConnection connect = new clsConnection();

	Connection conn;

	public Addwindow(JFrame getParentFrame) {

		super("Add - Employee ", true, true, true, true);
		setSize(400, 800);
		JFParentFrame = getParentFrame;
		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(7, 7));

		LblEmp_Code = new JLabel(" Employee Code :");
		LblEmp_Name1 = new JLabel(" First Name    :");
		LblEmp_Name2 = new JLabel(" Last Name     :");
		LblEmp_Desi = new JLabel(" Designation   :");
		LblEmp_Add = new JLabel(" Address       :");
		LblEmp_No = new JLabel(" Contact No    :");

		TxtEmp_Code = new JTextField(20);
		Emp_Type = new JComboBox();
		Emp_Type.addActionListener(this);
		Emp_Type.setEditable(false);
		add_Cat_combo(Emp_Type);
		TxtEmp_Name1 = new JTextField(20);
		TxtEmp_Name2 = new JTextField(20);

		TxtEmp_Add = new JTextField(20);
		TxtEmp_No = new JTextField(20);

		panel1.add(LblEmp_Code);
		panel1.add(TxtEmp_Code);

		panel1.add(LblEmp_Desi);
		panel1.add(Emp_Type);

		panel1.add(LblEmp_Name1);
		panel1.add(TxtEmp_Name1);

		panel1.add(LblEmp_Name2);
		panel1.add(TxtEmp_Name2);

		panel1.add(LblEmp_Add);
		panel1.add(TxtEmp_Add);

		panel1.add(LblEmp_No);
		panel1.add(TxtEmp_No);
		panel1.setOpaque(true);

		panel2 = new JPanel();
		panel2.setLayout(new FlowLayout());
		AddBtn = new JButton("Add");
		ResetBtn = new JButton("Reset");
		ExitBtn = new JButton("Exit");


		panel2.add(AddBtn);
		AddBtn.addActionListener(this);
		panel2.add(ResetBtn);
		ResetBtn.addActionListener(this);
		panel2.add(ExitBtn);
		ExitBtn.addActionListener(this);
		panel2.setOpaque(true);


		getContentPane().setLayout(new GridLayout(2, 1));
		getContentPane().add(panel1, "CENTER");
		getContentPane().add(panel2, "CENTER");
		setFrameIcon(new ImageIcon("images/backup.gif"));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();

		settings.Numvalidator(TxtEmp_No);

	}

	public void actionPerformed(ActionEvent event) {

		Object source = event.getSource();

		if (source.equals(Emp_Type)) {

			Emp_Desi = (String) Emp_Type.getSelectedItem();


		}


		if (source.equals(AddBtn)) {

			Emp_Code = "";
			Emp_Name1 = "";
			Emp_Name2 = "";
			Emp_Desi = "";
			Emp_Add = "";
			Emp_No = "";


			Emp_Code = TxtEmp_Code.getText().trim();
			Emp_Name1 = TxtEmp_Name1.getText().trim();
			Emp_Name2 = TxtEmp_Name2.getText().trim();
			Emp_Desi = (String) Emp_Type.getSelectedItem();
			Emp_Add = TxtEmp_Add.getText().trim();
			Emp_No = TxtEmp_No.getText().trim();

			try {
				conn = connect.setConnection(conn, "", "");
			} catch (Exception e) {
			}
			try {

				Statement stmt = conn.createStatement();
				if (!Emp_Code.equals("") &&
						!Emp_Name1.equals("") &&
						!Emp_Name2.equals("") &&
						!Emp_Desi.equals("") &&
						!Emp_Add.equals("") &&
						!Emp_No.equals(""))

				{


					String query = "SELECT * FROM EMPLOYEE WHERE Emp_Code='" + Emp_Code + "'";
					ResultSet rs = stmt.executeQuery(query);
					int foundrec = 0;
					while (rs.next()) {
						dialogmessage = "Record Already Exists in DataBase!!!";
						dialogtype = JOptionPane.WARNING_MESSAGE;
						JOptionPane.showMessageDialog((Component) null, dialogmessage, dialogs, dialogtype);

						foundrec = 1;

					}
					if (foundrec == 0) {

						String temp = "INSERT INTO EMPLOYEE VALUES ('" + Emp_Code + "','"
								+ Emp_Name1 + "','"
								+ Emp_Name2 + "','"
								+ Emp_Desi + "','"
								+ Emp_Add + "','"
								+ Emp_No + "')";

						int result = stmt.executeUpdate(temp);
						if (result == 1) {
							System.out.println("Recorded Added");
							ResetRecord();


						} else {
							dialogmessage = "Failed To Insert";
							JOptionPane.showMessageDialog(null, "Failed To Insert in DataBase",
									"WARNING!!", JOptionPane.WARNING_MESSAGE);


						}
					}


				} else {
					dialogmessage = "Empty Record !!!";
					dialogtype = JOptionPane.WARNING_MESSAGE;
					JOptionPane.showMessageDialog((Component) null, dialogmessage, dialogs, dialogtype);

				}

				conn.close();

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "GENERAL EXCEPTION", "WARNING!!!", JOptionPane.INFORMATION_MESSAGE);
			}


		} else if (source == ResetBtn) {
			ResetRecord();
		} else if (source == ExitBtn) {
			setVisible(false);
			dispose();
		}

	}


	private void ResetRecord() {
		TxtEmp_Code.setText("");
		TxtEmp_Name1.setText("");
		TxtEmp_Name2.setText("");
		TxtEmp_Add.setText("");
		TxtEmp_No.setText("");
	}


	public void add_Cat_combo(JComboBox cmb) {


		try {
			conn = connect.setConnection(conn, "", "");
		} catch (Exception e) {
		}
		try {


			Statement stmt = conn.createStatement();

			String query = "SELECT * FROM Settings";
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {


				String Txtcmb = rs.getString(2).trim();
				record = rs.getInt("Category_Type");

				cmb.addItem(Txtcmb);

			}
			conn.close();
		} catch (Exception ex) {

		}

	}
/*
	public class variables_get_setter {
		public Connection getConn() {
			return conn;
		}

		public void setConn(Connection conn) {
			this.conn = conn;
		}

		public JDesktopPane getDesktop() {
			return desktop;
		}

		public void setDesktop(JDesktopPane desktop) {
			this.desktop = desktop;
		}

		public JFrame getJFParentFrame() {
			return JFParentFrame;
		}

		public void setJFParentFrame(JFrame JFParentFrame) {
			this.JFParentFrame = JFParentFrame;
		}

		public JPanel getPanel1() {
			return panel1;
		}

		public void setPanel1(JPanel panel1) {
			this.panel1 = panel1;
		}

		public JPanel getPanel2() {
			return panel2;
		}

		public void setPanel2(JPanel panel2) {
			this.panel2 = panel2;
		}

		public JButton getAddBtn() {
			return AddBtn;
		}

		public void setAddBtn(JButton addBtn) {
			AddBtn = addBtn;
		}

		public JButton getResetBtn() {
			return ResetBtn;
		}

		public void setResetBtn(JButton resetBtn) {
			ResetBtn = resetBtn;
		}

		public JButton getExitBtn() {
			return ExitBtn;
		}

		public void setExitBtn(JButton exitBtn) {
			ExitBtn = exitBtn;
		}

		public JLabel getLblEmp_Code() {
			return LblEmp_Code;
		}

		public void setLblEmp_Code(JLabel lblEmp_Code) {
			LblEmp_Code = lblEmp_Code;
		}

		public JLabel getLblEmp_Name1() {
			return LblEmp_Name1;
		}

		public void setLblEmp_Name1(JLabel lblEmp_Name1) {
			LblEmp_Name1 = lblEmp_Name1;
		}

		public JLabel getLblEmp_Name2() {
			return LblEmp_Name2;
		}

		public void setLblEmp_Name2(JLabel lblEmp_Name2) {
			LblEmp_Name2 = lblEmp_Name2;
		}

		public JLabel getLblEmp_Desi() {
			return LblEmp_Desi;
		}

		public void setLblEmp_Desi(JLabel lblEmp_Desi) {
			LblEmp_Desi = lblEmp_Desi;
		}

		public JLabel getLblEmp_Add() {
			return LblEmp_Add;
		}

		public void setLblEmp_Add(JLabel lblEmp_Add) {
			LblEmp_Add = lblEmp_Add;
		}

		public JLabel getLblEmp_No() {
			return LblEmp_No;
		}

		public void setLblEmp_No(JLabel lblEmp_No) {
			LblEmp_No = lblEmp_No;
		}

		public JTextField getTxtEmp_Code() {
			return TxtEmp_Code;
		}

		public void setTxtEmp_Code(JTextField txtEmp_Code) {
			TxtEmp_Code = txtEmp_Code;
		}

		public JTextField getTxtEmp_Name1() {
			return TxtEmp_Name1;
		}

		public void setTxtEmp_Name1(JTextField txtEmp_Name1) {
			TxtEmp_Name1 = txtEmp_Name1;
		}

		public JTextField getTxtEmp_Name2() {
			return TxtEmp_Name2;
		}

		public void setTxtEmp_Name2(JTextField txtEmp_Name2) {
			TxtEmp_Name2 = txtEmp_Name2;
		}

		public JTextField getTxtEmp_Add() {
			return TxtEmp_Add;
		}

		public void setTxtEmp_Add(JTextField txtEmp_Add) {
			TxtEmp_Add = txtEmp_Add;
		}

		public JTextField getTxtEmp_No() {
			return TxtEmp_No;
		}

		public void setTxtEmp_No(JTextField txtEmp_No) {
			TxtEmp_No = txtEmp_No;
		}

		public JComboBox getEmp_Type() {
			return Emp_Type;
		}

		public void setEmp_Type(JComboBox emp_Type) {
			Emp_Type = emp_Type;
		}

		public String getDialogmessage() {
			return dialogmessage;
		}

		public void setDialogmessage(String dialogmessage) {
			this.dialogmessage = dialogmessage;
		}

		public String getDialogs() {
			return dialogs;
		}

		public void setDialogs(String dialogs) {
			this.dialogs = dialogs;
		}

		public int getDialogtype() {
			return dialogtype;
		}

		public void setDialogtype(int dialogtype) {
			this.dialogtype = dialogtype;
		}

		public String getEmp_Code() {
			return Emp_Code;
		}

		public void setEmp_Code(String emp_Code) {
			Emp_Code = emp_Code;
		}

		public static int getRecord() {
			return record;
		}

		public static void setRecord(int record) {
			Addwindow.record = record;
		}

		public String getEmp_Name1() {
			return Emp_Name1;
		}

		public void setEmp_Name1(String emp_Name1) {
			Emp_Name1 = emp_Name1;
		}

		public String getEmp_Name2() {
			return Emp_Name2;
		}

		public void setEmp_Name2(String emp_Name2) {
			Emp_Name2 = emp_Name2;
		}

		public String getEmp_Desi() {
			return Emp_Desi;
		}

		public void setEmp_Desi(String emp_Desi) {
			Emp_Desi = emp_Desi;
		}

		public String getEmp_Add() {
			return Emp_Add;
		}

		public void setEmp_Add(String emp_Add) {
			Emp_Add = emp_Add;
		}

		public String getEmp_No() {
			return Emp_No;
		}

		public void setEmp_No(String emp_No) {
			Emp_No = emp_No;
		}

		public clsConfiguracions getSettings() {
			return settings;
		}

		public void setSettings(clsConfiguracions settings) {
			this.settings = settings;
		}

		public clsConnection getConnect() {
			return connect;
		}

		public void setConnect(clsConnection connect) {
			this.connect = connect;
		}
	}*/
}
