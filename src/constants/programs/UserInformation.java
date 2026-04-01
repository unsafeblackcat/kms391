package constants.programs;

import constants.ServerConstants;
import java.awt.Dimension;
import java.awt.Toolkit;

public class UserInformation extends javax.swing.JFrame {

	private String ip; // 세션 아이피
	private String id; // 계정 아이디
	private int accountId; // 계정 번호
	private String name; // 캐릭터 닉네임
	private int characterId; // 캐릭터 번호
	private short level; // 레벨
	private long seasonPassLevel; // 시즌패스 레벨
	private long meso; // 메소
	private int num_4310282; // 보스 코인
	private String accountCreate; // 계정 생성일

	public UserInformation(String ip, String id, int accountId, String name, int characterId, short level,
			long seasonPassLevel, long meso, int num_4310282, String accountCreate) {
		this.ip = ip;
		this.id = id;
		this.accountId = accountId;
		this.name = name;
		this.characterId = characterId;
		this.level = level;
		this.seasonPassLevel = seasonPassLevel;
		this.meso = meso;
		this.num_4310282 = num_4310282;
		this.accountCreate = accountCreate;

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - 1505) / 2, (screenSize.height - 576) / 2);

		initComponents();

		setTitle("[KMS Project] ControlUnit :: Ver 1.2." + ServerConstants.MAPLE_VERSION);

		jLabel1.setText((jLabel1.getText() + ip));
		jLabel2.setText((jLabel2.getText() + id + "(" + accountId + ")"));
		jLabel4.setText((jLabel4.getText() + name + "(" + characterId + ")"));
		jLabel6.setText((jLabel6.getText() + "Lv. " + level));
		jLabel7.setText((jLabel7.getText() + "Lv. " + seasonPassLevel));
		jLabel10.setText((jLabel10.getText() + meso + "억 메소"));
		jLabel11.setText((jLabel11.getText() + num_4310282 + "개"));
		jLabel12.setText((jLabel12.getText() + accountCreate));
	}

	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		jLabel10 = new javax.swing.JLabel();
		jLabel11 = new javax.swing.JLabel();
		jLabel12 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();

		setResizable(false);

		jLabel1.setText("[會話IP]");

		jLabel2.setText("[帳號ID]");

		jLabel4.setText("[角色昵稱]");

		jLabel6.setText("[等級]");

		jLabel7.setText("[賽季通行等級]");

		jLabel10.setText("[金币]");

		jLabel11.setText("[BOSS硬幣數量]");

		jLabel12.setText("[帳戶創建日]");

		jLabel3.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
		jLabel3.setForeground(new java.awt.Color(0, 153, 153));
		jLabel3.setText("角色資訊");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1)
						.addComponent(jLabel2).addComponent(jLabel4).addComponent(jLabel6).addComponent(jLabel7)
						.addComponent(jLabel10).addComponent(jLabel11).addComponent(jLabel12).addComponent(jLabel3))
				.addContainerGap(294, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap()
						.addComponent(jLabel3).addGap(29, 29, 29).addComponent(jLabel1)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel2)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel4)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel6)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel7)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel10)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel11)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel12)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	public static void main(String args[]) {
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
		// (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(UserInformation.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(UserInformation.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(UserInformation.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(UserInformation.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		}
		// </editor-fold>
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// new UserInformation().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	// End of variables declaration//GEN-END:variables
}