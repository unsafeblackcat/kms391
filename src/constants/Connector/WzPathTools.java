package constants.Connector;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class WzPathTools {

	public static void main(String[] args) {
		test_Frame tf = new test_Frame();
	}
}

class test_Frame extends JFrame implements ActionListener {

	private JFileChooser jfc = new JFileChooser();
	private JButton jbt_open = new JButton("열기");
	private JLabel jlb = new JLabel(" ");
	private JTextField tf = new JTextField(10);
	private boolean save = false, saves = false;
	private String value = "";

	public test_Frame() {
		super("test");
		this.init();
		this.start();
		this.setSize(400, 200);
		this.setVisible(true);
	}

	public void init() {
		getContentPane().setLayout(new FlowLayout());
		add(jbt_open);
		add(tf);
	}

	public void start() {
		jbt_open.addActionListener(this);

		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setMultiSelectionEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		try {
			if (arg0.getSource() == jbt_open) {
				if (save && !saves) {
					File aa = jfc.getSelectedFile();
					try {
						File file = new File(aa.getPath() + "\\" + "re.txt");
						FileWriter fileWrite = new FileWriter(file, true);

						fileWrite.write(value);

						fileWrite.flush();

						fileWrite.close();
						saves = true;
					} catch (Exception e) {

						e.printStackTrace();

					}
				}
				if (!save) {
					if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
						String re = "";
						File aa = jfc.getSelectedFile();

						for (File s : aa.listFiles()) {
							if (!s.isDirectory()) {
								int index = s.getName().lastIndexOf(".");
								if (index > 0) {
									String extension = s.getName().substring(index + 1);
									Long filesize = Files.size(Paths.get(s.getPath()));
									if (extension.equals("wz") && filesize > 600) {
										System.out.println(s.getName() + "=" + filesize);
										re += s.getName() + "=" + filesize + "\r\n";
									}
								}
							} else {
								for (File ss : s.listFiles()) {
									if (!ss.isDirectory()) {
										int index = ss.getName().lastIndexOf(".");
										if (index > 0) {
											Long filesize = Files.size(Paths.get(ss.getPath()));
											String extension = ss.getName().substring(index + 1);
											if (extension.equals("wz") && filesize > 600) {
												System.out.println(s.getName() + "=" + filesize);
												re += ss.getName() + "=" + Files.size(Paths.get(ss.getPath())) + "\r\n";
											}
										}
									}
								}
							}
						}
						value = re;
					}
					jbt_open.setText("저장");
					start();
					save = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
