var status = -1;
var enter = "\r\n";
var talkType = 0x86;
var NoramlPass = "Serenity_Noraml_Pass_Info";
var PrimeumPass = "Serenity_Premium_Pass_Info";
var DTCS = "DiTuChuanS";
var SHAND = "ShanDian";
var MRRW = "MeiRRW";
var QTGN = "QiTaGongNeng";
var YJQH = "YiJianQiangHua";
var LMXT = "LianMengXiTong";
var HDXT = "HuoDongXiTong";
var ZZXT = "ZanZhuXiTing";
var PMXT = "PaiMingXiTong";
var ZYZZ = "ZiYouZhuanZhi";
var CJXT = "ChouJiangXiTong";
var MRMF = "MeiRongMeiFa";
var FLXT = "FuLiXiTong";
var GRZX = "GeRenZiXun";
var GRCS = "Gerenceshi";

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        giveCharacterToEventKeyValue();
		
		var chat = "#fs20##fn微軟正黑體# #fc0xFF000000##e當前#n #b#h0##n#k#l #fc0xFF000000##e的資訊 ::#n\r\n #e通行證等級 :#n#fc0xFFFF3300# " + cm.getPlayer().getKeyValue(100592,  "point") + "級#l#n#fc0xFF000000#  / #e組隊點數 : #n#r0 P #fc0xFF000000#/ #e排名等級 :  #n#r" + cm.getPlayer().getKeyValue(190823,  "grade") + " 級#k" + enter;
		chat += "#L0#" + "地圖傳送"+"  #L1#" + "打開商店"+"  #L2#" + "任務中心"+"  #L3#" + "福利領取"+enter;
		chat += "#L4#" + "一鍵强化"+"  #L5#" + "聯盟系統"+"  #L6#" + "活動系統"+"  #L7#" + "贊助系統"+enter;
		chat += "#L8#" + "個人咨詢"+"  #L9#" + "道具兌換"+"  #L10#" + "自由轉職"+"  #L11#" + "美容美髮"+enter;
		chat += "#L12#" + "其他功能"+"  #L13#" + "隨身倉庫"+"  #L14#" + "抽奬系統"+"  #L15#" + "排名系統"+enter;
		chat +="#L16##r回到大廳!"
		cm.sendSimpleS(chat, talkType);
	} else if (status == 1) {
		switch (selection) {
			case 0:
				cm.openNpcCustom(cm.getClient(), 9010061, DTCS);
				break;
			case 1:
				cm.openNpcCustom(cm.getClient(), 9010061, SHAND);
				break;
			case 2:
				cm.openNpcCustom(cm.getClient(), 9010061, MRRW);
				break;
			case 3:
				cm.openNpcCustom(cm.getClient(), 9010061, FLXT);
				break;
			case 4:
				cm.openNpcCustom(cm.getClient(), 3006188, YJQH);
				break;
			case 5:
				cm.openNpcCustom(cm.getClient(), 9010061, LMXT);
				break;
			case 6:
				cm.openNpcCustom(cm.getClient(), 9010061, HDXT);
				break;
			case 7:
				cm.openNpcCustom(cm.getClient(), 9010061, ZZXT);
				break;
			case 8:
				cm.openNpcCustom(cm.getClient(), 9010061, GRZX);
				break;
			case 9:
				cm.openNpcCustom(cm.getClient(), 9010061, GRCS);
				break;
			case 10:
				cm.openNpcCustom(cm.getClient(), 9010061, ZYZZ);
				break;
			case 11:
				cm.openNpcCustom(cm.getClient(), 9010061, MRMF);
				break;
			case 12:
				cm.openNpcCustom(cm.getClient(), 9010061, QTGN);
				break;
			case 13:
				cm.dispose();
                cm.openNpc(9031016);
				break;
			case 14:
				cm.openNpcCustom(cm.getClient(), 9010061, CJXT);
				break;
			case 15:
				cm.openNpcCustom(cm.getClient(), 9010061, PMXT);
				break;
			case 16:
				cm.warp(910000000);
				cm.dispose();
				break;
		}
	}
}

function FancyPassExplaination() {

}

function giveCharacterToEventKeyValue() {

}