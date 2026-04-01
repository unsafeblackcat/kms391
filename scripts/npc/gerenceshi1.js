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
		
		var chat = "#fs25#  功能菜單" + enter;
		chat += "#L0#" + "口袋槽"+"  #L1#" + "圣杯抽奖"+"  #L2#" + "勋章相关"+"  #L3#" + "物品合成"+enter;
		chat += "#L4#" + "获取符号"+"  #L5#" + "矩阵系统"+"  #L6#" + "五转任务"+"  #L7#" + "符号扩展"+enter;
		chat += "#L8#" + "基础商店"+"  #L9#" + "转生商店"+"  #L10#" + "硬币商店"+"  #L11#" + "赞助商店"+enter;
		chat += "#L12#" + "副武器"+"  #L13#" + "公会相关"+"  #L14#" + "灯泡任务"+"  #L15#" + "能力值"+enter;
		chat +="\r\n#L16##r回到大廳!"
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
				cm.openNpc(1052232);
				break;
			case 5:
				cm.getClient().getSession().write(Packages.tools.packet.CField.UIPacket.openUI(1131));
				break;
			case 6:
				cm.openNpc(9020011);
				break;
			case 7:
			if (cm.getPlayer().getLevel() < 225) {
			cm.cm.sendOk("아직 레벨이 부족합니다. 225레벨을 달성해주세요.");
		} else if (cm.getPlayer().getQuest(Packages.server.quest.MapleQuest.getInstance(1465)).getStatus() != 2) {
			cm.cm.sendOk("5차전직을 완료해 주시기 바랍니다.");
		} else if (cm.getPlayer().getQuest(Packages.server.quest.MapleQuest.getInstance(34478)).getStatus() == 2) {
			cm.cm.sendOk("이미 확장되었습니다.");
		} else {
			cm.forceCompleteQuest(34478);
			cm.cm.sendOk("심볼 확장에 성공하였습니다.");
		}
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