package constants.programs;

import constants.GameConstants;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import server.MapleItemInformationProvider;
import tools.Pair;

// 定义一个继承自 javax.swing.JFrame 的 SearchToCode 类，用于创建一个图形用户界面窗口
public class SearchToCode extends javax.swing.JFrame {

	private static final long serialVersionUID = -8100769599645202286L;

	// 构造函数，初始化窗口位置并调用 initComponents 方法进行组件初始化
	public SearchToCode() {
		// 获取屏幕尺寸
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// 设置窗口位置，使其居中显示
		this.setLocation((screenSize.width - 877) / 2, (screenSize.height - 530) / 2);
		// 调用 initComponents 方法初始化窗口组件
		initComponents();
	}

	// 自动生成的组件初始化方法，用于创建和布局窗口中的各种组件
	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		검색어 = new javax.swing.JTextField();
		장비 = new javax.swing.JRadioButton();
		아이템 = new javax.swing.JRadioButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		출력L = new javax.swing.JList<String>();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jButton3 = new javax.swing.JButton();

		// 设置窗口不可调整大小
		setResizable(false);

		// 设置标签字体和文本
		jLabel1.setFont(new java.awt.Font("굴림", 1, 12));
		jLabel1.setText("項目名稱");

		// 设置“設備”单选按钮文本，并添加事件监听器
		장비.setText("設備");
		장비.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				장비ActionPerformed(evt);
			}
		});

		// 设置“物品”单选按钮文本，并添加事件监听器
		아이템.setText("物品");
		아이템.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				아이템ActionPerformed(evt);
			}
		});

		// 将 출력列表添加到滚动面板中
		jScrollPane1.setViewportView(출력L);

		// 设置“，以分隔保存”按钮文本，并添加事件监听器
		jButton1.setText("，以分隔保存");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		// 设置“下一格保存”按钮文本，并添加事件监听器
		jButton2.setText("下一格保存");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		// 设置“金币”按钮文本，并添加事件监听器
		jButton3.setText("金币");
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
			}
		});

		// 定义窗口布局管理器
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1)
						.addGroup(layout.createSequentialGroup()
								.addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGap(18, 18, 18).addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 417,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGroup(layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE).addComponent(장비)
								.addGap(147, 147, 147).addComponent(아이템).addGap(284, 284, 284))
						.addGroup(layout.createSequentialGroup()
								.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 83,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(검색어, javax.swing.GroupLayout.PREFERRED_SIZE, 670,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1)
						.addComponent(검색어, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(jButton3))
				.addGap(18, 18, 18)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(장비)
						.addComponent(아이템))
				.addGap(18, 18, 18)
				.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 347,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
						.addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
						.addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		// 调整窗口大小以适应组件
		pack();
	}

	// “，以分隔保存”按钮点击事件处理方法
	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		// 弹出输入对话框，让用户输入文件名
		String name = JOptionPane.showInputDialog(null, "파일명을 입력해 주십시오");
		FileOutputStream out = null;
		try {
			if (name != null && !name.isEmpty()) {
				// 创建文件输出流，覆盖原有文件
				out = new FileOutputStream(name + ".txt", false);
				// 将 임시1字符串写入文件
				out.write(임시1.toString().getBytes());
			}
		} catch (Exception ex) {
			// 捕获异常并输出错误信息
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "保存文件时发生错误: " + ex.getMessage());
		} finally {
			try {
				if (out != null) {
					// 关闭文件输出流
					out.close();
				}
			} catch (IOException ex) {
				// 捕获关闭文件流时的异常并输出错误信息
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "关闭文件时发生错误: " + ex.getMessage());
			}
			// 弹出提示框，提示保存完成
			JOptionPane.showMessageDialog(null, "저장이 완료되었습니다.");
		}
	}

	// “設備”单选按钮点击事件处理方法
	private void 장비ActionPerformed(java.awt.event.ActionEvent evt) {
		// 取消“物品”单选按钮的选择
		아이템.setSelected(false);
		// 设置 체크数组，标记“設備”被选中
		체크[1] = false;
		체크[0] = true;
	}

	// “物品”单选按钮点击事件处理方法
	private void 아이템ActionPerformed(java.awt.event.ActionEvent evt) {
		// 取消“設備”单选按钮的选择
		장비.setSelected(false);
		// 设置 체크数组，标记“物品”被选中
		체크[0] = false;
		체크[1] = true;
	}

	// “金币”按钮点击事件处理方法
	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
		// 检查是否选择了分类
		if (!체크[0] && !체크[1]) {
			// 若未选择分类，弹出提示框要求用户选择
			JOptionPane.showMessageDialog(null, "분류를 선택해 주십시오.");
			return;
		}
		// 清空输出列表
		출력.clear();
		// 更新输出列表的模型
		출력L.setModel(출력);
		// 清空临时字符串
		임시1 = "";
		임시2 = "";
		// 创建一个列表用于存储搜索结果
		List<String> retItems = new ArrayList<String>();
		// 获取所有物品信息
		for (Pair<Integer, String> itemPair : MapleItemInformationProvider.getInstance().getAllItems()) {
			// 检查物品名称是否包含搜索词
			if (itemPair.getRight().toLowerCase().contains(검색어.getText().toLowerCase())) {
				if (체크[0]) {
					// 若选择了“設備”，检查物品是否为装备
					if (GameConstants.isEquip(itemPair.getLeft())) {
						// 拼接设备代码和逗号
						임시1 = 임시1 + itemPair.getLeft() + ", ";
						// 拼接设备代码、名称和换行符
						임시2 = 임시2 + itemPair.getLeft() + " - " + itemPair.getRight() + "\r\n";
						// 将设备信息添加到结果列表中
						retItems.add(itemPair.getLeft() + " - " + itemPair.getRight());
					}
				} else if (체크[1]) {
					// 若选择了“物品”，检查物品是否不是装备
					if (!GameConstants.isEquip(itemPair.getLeft())) {
						// 拼接物品代码和逗号
						임시1 = 임시1 + itemPair.getLeft() + ", ";
						// 拼接物品代码、名称和换行符
						임시2 = 임시2 + itemPair.getLeft() + " - " + itemPair.getRight() + "\r\n";
						// 将物品信息添加到结果列表中
						retItems.add(itemPair.getLeft() + " - " + itemPair.getRight());
					}
				}
			}
		}
		if (retItems != null && retItems.size() > 0) {
			// 若有搜索结果，将结果添加到输出列表中
			for (String singleRetItem : retItems) {
				출력.addElement(singleRetItem.toString());
			}
			// 更新输出列表的模型
			출력L.setModel(출력);
		} else {
			// 若没有搜索结果，弹出提示框告知用户
			JOptionPane.showMessageDialog(null, "발견된 아이템이 없습니다.");
		}
	}

	// “下一格保存”按钮点击事件处理方法
	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
		// 弹出输入对话框，让用户输入文件名
		String name = JOptionPane.showInputDialog(null, "파일명을 입력해 주십시오");
		FileOutputStream out = null;
		try {
			if (name != null && !name.isEmpty()) {
				// 创建文件输出流，覆盖原有文件
				out = new FileOutputStream(name + ".txt", false);
				// 将 임시2字符串写入文件
				out.write(임시2.toString().getBytes());
			}
		} catch (Exception ex) {
			// 捕获异常并输出错误信息
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "保存文件时发生错误: " + ex.getMessage());
		} finally {
			try {
				if (out != null) {
					// 关闭文件输出流
					out.close();
				}
			} catch (IOException ex) {
				// 捕获关闭文件流时的异常并输出错误信息
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "关闭文件时发生错误: " + ex.getMessage());
			}
			// 弹出提示框，提示保存完成
			JOptionPane.showMessageDialog(null, "저장이 완료되었습니다.");
		}
	}

	// 主方法，用于启动程序
	public static void main(String args[]) {
		// 在事件调度线程中创建并显示窗口
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new SearchToCode().setVisible(true);
			}
		});
	}

	// 临时字符串，用于存储设备或物品代码，以逗号分隔
	public static String 임시2 = "";
	// 临时字符串，用于存储设备或物品代码和名称，以换行符分隔
	public static String 임시1 = "";
	// 布尔数组，用于标记“設備”和“物品”的选择状态
	private boolean 체크[] = { false, false };
	// 输出列表的模型
	public static DefaultListModel<String> 출력 = new DefaultListModel<String>();
	// 以下是自动生成的组件声明
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextField 검색어;
	private javax.swing.JRadioButton 아이템;
	private javax.swing.JRadioButton 장비;
	private javax.swing.JList<String> 출력L;
}