importPackage(Packages.tools.packet);

function start() {
	if (cm.getPlayer().getKeyValue(3, "dojo") <= 0) {
		cm.dispose();
		return;
	}
	time = Packages.tools.FileoutputUtil.CurrentReadable_Time();
	txt = "뭐, 고생 많았어. 계속 도전해보라고.\r\n";
	txt += "(현재 시간 :" + time.substring(2, 4) + "/" + time.substring(5, 7) + "/" + time.substring(8, 10) + ", " + time.substring(11, 13) + "시 " + time.substring(14, 16) + "분)\r\n";
	txt += "\r\n";
	txt += "<최근 기록 정보>\r\n#b";
	txt += " -랭킹 구간: 통달\r\n";
	txt += " -클리어 층: " + cm.getPlayer().getKeyValue(3, "dojo") + " 층\r\n";
	txt += " -걸린 시간: " + cm.getPlayer().getDojoStartTime()+ " 초\r\n";
	txt += "\r\n#k";
	txt += "이전 기록보다 좋은 기록을 달성했다면 #r무릉 순위표#k에 자동으로 등록될 거야.\r\n";
	txt += "등록에 시간이 조금 걸릴 수도 있으니 알아두라고.";
	if (cm.getPlayer().getKeyValue(3, "dojo") > cm.getPlayer().getKeyValue(100466, "Score")) {
		cm.getPlayer().setKeyValue(100466, "Score", cm.getPlayer().getKeyValue(3, "dojo")+"");
		cm.getClient().send(CField.UIPacket.detailShowInfo("- 최고 기록 달성 -", 3, 25, 4));
		cm.SaveDojoRank(cm.getPlayer());
	}
	cm.getPlayer().setDojoStartTime(0);
	cm.getPlayer().removeKeyValue(3);
	cm.sendOkS(txt, 4, 2091005);
	cm.dispose();
}