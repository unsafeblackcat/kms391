﻿importPackage(Packages.client.inventory);
importPackage(Packages.client.MapleCharacter);
importPackage(Packages.client);
importPackage(Packages.client.MapleStat);
importPackage(Packages.server);
importPackage(Packages.tools.packet);
importPackage(Packages.constants);
importPackage(Packages.server.quest);

var enter = "\r\n";
var seld = -1;

var bjob = -1;
var cjob = -1;
var adv = false;
黑色 = "#fc0xFF191919#";
灰色 = "#fc0xFFB4B4B4#";


var jobs = [
    { 'jobid': 110, 'jobname': "英雄", 'job': "冒險家", 'stat': 1, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 120, 'jobname': "聖殿騎士", 'job': "冒險家", 'stat': 1, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 130, 'jobname': "黑騎士", 'job': "冒險家", 'stat': 1, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 210, 'jobname': "火毒法師", 'job': "冒險家", 'stat': 3, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 220, 'jobname': "冰雷法師", 'job': "冒險家", 'stat': 3, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 230, 'jobname': "主教", 'job': "冒險家", 'stat': 3, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 310, 'jobname': "弓手大師", 'job': "冒險家", 'stat': 2, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 320, 'jobname': "神箭手", 'job': "冒險家", 'stat': 2, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 330, 'jobname': "遊俠", 'job': "冒險家", 'stat': 2, 'sk': [80001152, 1281, 1297, 1298, 12, 73] },
    { 'jobid': 410, 'jobname': "夜行者", 'job': "冒險家", 'stat': 2, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 420, 'jobname': "俠盜", 'job': "冒險家", 'stat': 4, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 430, 'jobname': "雙刃刺客", 'job': "冒險家", 'stat': 4, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 510, 'jobname': "狙擊手", 'job': "冒險家", 'stat': 1, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 520, 'jobname': "艦長", 'job': "冒險家", 'stat': 2, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 530, 'jobname': "火砲手", 'job': "冒險家", 'stat': 1, 'sk': [80001152, 1281, 110, 109, 111, 1283, 12, 73] },
    { 'jobid': 1100, 'jobname': "靈魂法師", 'job': "希纳斯", 'stat': 1, 'sk': [10001244, 10000252, 80001152, 10001253, 10001254, 10001245, 10000250, 10000246, 10000012, 10000073] },
    { 'jobid': 1200, 'jobname': "火焰法師", 'job': "希纳斯", 'stat': 3, 'sk': [10001244, 10000252, 80001152, 10001253, 10001254, 10001245, 10000250, 10000248, 10000012, 10000073] },
    { 'jobid': 1300, 'jobname': "風行者", 'job': "希纳斯", 'stat': 2, 'sk': [10001244, 10000252, 80001152, 10001253, 10001254, 10001245, 10000250, 10000247, 10000012, 10000073] },
    { 'jobid': 1400, 'jobname': "暗夜行者", 'job': "希纳斯", 'stat': 4, 'sk': [10001244, 10000252, 80001152, 10001253, 10001254, 10001245, 10000250, 10000249, 10000012, 10000073] },
    { 'jobid': 1500, 'jobname': "鬥士", 'job': "希纳斯", 'stat': 1, 'sk': [10001244, 10000252, 80001152, 10001253, 10001254, 10001245, 10000250, 10000246, 10000012, 10000073] },
    { 'jobid': 5100, 'jobname': "米哈伊爾", 'job': "希纳斯", 'stat': 1, 'sk': [50001214, 10000250, 10000074, 10000075, 50000012, 50000073] },
    { 'jobid': 2100, 'jobname': "阿蘭", 'job': "英雄", 'stat': 1, 'sk': [20000194, 20001295, 20001296, 20000012, 20000073] },
    { 'jobid': 2200, 'jobname': "伊萬", 'job': "英雄", 'stat': 3, 'sk': [20010022, 20010194, 20011293, 20010012, 20010073] },
    { 'jobid': 2300, 'jobname': "梅賽德斯", 'job': "英雄", 'stat': 2, 'sk': [20020109, 20021110, 20020111, 20020112, 20020012, 20020073] },
    { 'jobid': 2400, 'jobname': "幻影", 'job': "英雄", 'stat': 4, 'sk': [20031208, 20040190, 20031203, 20031205, 20030206, 20031207, 20031209, 20031251, 20031260, 20030012, 20030073] },
    { 'jobid': 2500, 'jobname': "銀月", 'job': "英雄", 'stat': 1, 'sk': [20051284, 20050285, 20050286, 20050074, 20050012, 20050073] },
    { 'jobid': 2700, 'jobname': "夜光", 'job': "英雄", 'stat': 3, 'sk': [20040216, 20040217, 20040218, 20040219, 20040221, 20041222, 20040012, 20040073] },
    { 'jobid': 14200, 'jobname': "凱涅西斯", 'job': "英雄", 'stat': 3, 'sk': [140000291, 14200, 14210, 14211, 14212, 140001290, 140000012, 140000073] },
    { 'jobid': 3200, 'jobname': "戰鬥法師", 'job': "反抗者", 'stat': 3, 'sk': [30001281, 30000012, 30000073] },
    { 'jobid': 3300, 'jobname': "野獸獵人", 'job': "反抗者", 'stat': 2, 'sk': [30001281, 30001062, 30001061, 30000012, 30000073] },
    { 'jobid': 3500, 'jobname': "機械師", 'job': "反抗者", 'stat': 2, 'sk': [30001281, 30001068, 30000227, 30000012, 30000073] },
    { 'jobid': 3600, 'jobname': "宙斯", 'job': "反抗者", 'stat': 5, 'sk': [30001281, 30020232, 30020233, 30020234, 30020240, 30021235, 30021236, 30021237, 30020012, 30020073] },
    { 'jobid': 3100, 'jobname': "惡魔獵手", 'job': "反抗者", 'stat': 1, 'sk': [30001281, 80001152, 30001061, 30010110, 30010185, 30010112, 30010111, 30010012, 30010073] },
    { 'jobid': 3101, 'jobname': "惡魔复仇者", 'job': "反抗者", 'stat': 6, 'sk': [30001281, 80001152, 30001061, 30010110, 30010185, 30010242, 30010241, 30010230, 30010231, 30010232, 30010012, 30010073] },
    { 'jobid': 6100, 'jobname': "凱撒", 'job': "諾巴", 'stat': 1, 'sk': [600000219, 60000222, 60001217, 60001216, 60001225, 60001005, 60001296, 60000012, 60000073] },
    { 'jobid': 6400, 'jobname': "卡德娜", 'job': "諾巴", 'stat': 4, 'sk': [60020216, 60021217, 60021005, 60020218, 60020012, 60020073] },
    { 'jobid': 6300, 'jobname': "凱因", 'job': "諾巴", 'stat': 2, 'sk': [60031005, 60030012, 60030073] },
    { 'jobid': 6500, 'jobname': "天使爆破手", 'job': "諾巴", 'stat': 2, 'sk': [60011216, 60010217, 60011218, 60011219, 60011220, 60011221, 60011222, 60011005, 60010012, 60010073] },
    { 'jobid': 15100, 'jobname': "阿黛爾", 'job': "雷普", 'stat': 1, 'sk': [150020041, 150021000, 150020079, 150020006, 150020241] },
    { 'jobid': 15200, 'jobname': "伊利姆", 'job': "雷普", 'stat': 3, 'sk': [150001021, 150000017, 150000079, 150000012, 150000073] },
    { 'jobid': 15500, 'jobname': "亞克", 'job': "雷普", 'stat': 1, 'sk': [150010079, 150011005, 150011074, 150010241, 150010012, 150010073] },
    { 'jobid': 16400, 'jobname': "豪英", 'job': "阿尼瑪", 'stat': 4, 'sk': [160001074, 160001075, 160001005, 160000000, 160000076, 160000012, 160000073, 164001004] },
    { 'jobid': 16200, 'jobname': "拉拉", 'job': "阿尼瑪", 'stat': 4, 'sk': [160010000, 160011075] },
    { 'jobid': 15400, 'jobname': "卡莉", 'job': "雷普", 'stat': 4, 'sk': [150033224] }
];

var SFQ = "PRAY";

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}
if (status == 0) {
    var talk = "#fs15#       "+ SFQ + "轉職助手\r\n【注意:重複點擊引導可能會導致客戶端38】\r\n";
    talk += "" + 灰色 + "────────────────────────────#fs13#\r\n";
    talk += " " + 黑色 + "#e" + SFQ + "#n#k歡迎 #b#h ##k 勇士您的到來！很高興見到您！\r\n";
    talk += "我是 " + 黑色 + "守護布烏島的#k #fs15##e" + 黑色 + "崔克特#n#k 喔！\r\n";
    talk += " #e" + 黑色 + "" + SFQ + "#k#n 是一個和 #b勇士們一起打造的伺服器#k 喔！\r\n";
    talk += "今後請多多指教，若有任何事情，請 #r隨時#k 呼叫我！\r\n";
    talk += "" + 灰色 + "────────────────────────────\r\n";
    talk += "#b#h ##k 您在 " + 黑色 + "#e" + SFQ + "#n#k 的 #r所有遊戲記錄#k 都會被儲存在伺服器中，必要時可能會對他人 #r公開#k 或作為 #r證據#k 使用。\r\n\r\n";
    talk += "若您對以上內容 #r無異議#k，請按下 #b'是'#k 按鈕。";

    cm.sendYesNoS(talk, 0x4);
} else if (status == 1) {
		if (GameConstants.isYeti(cm.getPlayer().getJob())) {
			cm.gainItem(2000054, 1);
			cm.gainMeso(3000000);
			cm.gainItem(2431774, 1);
            cm.gainItem(2433444, 1);
            cm.gainItem(3700011, 1);
			cm.getPlayer().AutoTeachSkill();
			cm.warp(1000);
			cm.getPlayer().resetStats(4, 4, 4, 4);
			cm.openNpc(2007, "YetiTuto0");
			cm.getPlayer().setKeyValue(100592, "point", 0);
			return;
		} else if (GameConstants.isPinkBean(cm.getPlayer().getJob())) {
			cm.gainItem(2000054, 1);
			cm.gainMeso(3000000);
			cm.gainItem(2431774, 1);
            cm.gainItem(2433444, 1);
            cm.gainItem(3700011, 1);
			cm.getPlayer().AutoTeachSkill();
			cm.warp(1000);
			cm.getPlayer().resetStats(4, 4, 4, 4);
			cm.openNpc(2007, "PinkBeanTuto0");
			cm.getPlayer().setKeyValue(100592, "point", 0);
			return;
		}
		FirstJob(cm.getJob());
	} else if (status == 2) {
		seld = sel;
		if (seld >= 1 && seld <= 5) {
			adv = true;
			cm.sendSimple(SecondJob(seld));
		} else {
			nocast = false;
			switch (cm.getJob()) {

				case 2000:
					cm.teachSkill(20001295, 1, 1);
					break;

				case 6000:
					cm.teachSkill(60000219, 1, 1);
					cm.teachSkill(60001217, 1, 1);
					cm.teachSkill(60001216, 1, 1);
					cm.teachSkill(60001218, 1, 1);
					cm.teachSkill(60001219, 1, 1);
					cm.teachSkill(60001225, 1, 1);
					cm.addEquip(-10, 1352500, 0, 0, 0, 0, 0, 0);
					break;

				case 6001:
					cm.addEquip(-10, 1352601, 0, 0, 0, 0, 0, 0);
					break;

				case 2004:
					cm.teachSkill(27000106, 5, 5);
					cm.teachSkill(27000207, 5, 5);
					cm.teachSkill(27001201, 20, 20);
					cm.teachSkill(27001100, 20, 20);
					break;
				case 2005:
					cm.teachSkill(20051284, 30, 30);
					cm.teachSkill(20050285, 30, 30);
					cm.teachSkill(20050286, 30, 30);
					//cm.teachSkill(25000003, 30, 30);
					break;

				case 1000:
					cm.teachSkill(10001251, 1, 1);
					cm.teachSkill(10001252, 1, 1);
					cm.teachSkill(10001253, 1, 1);
					cm.teachSkill(10001254, 1, 1);
					cm.teachSkill(10001255, 1, 1);
					break;
				case 15002:
					cm.teachSkill(151001004, 1, 1);
					break;
			}
			switch (seld) {
				case 330:
					cjob = 301;
					cm.getPlayer().setReborns(sel);
					cm.getPlayer().setAutoJob(330);
					nocast = true;
					break;
				case 430:
					nocast = true;
					cjob = sel - 30;
					cm.getPlayer().changeJob(400);
					cm.getPlayer().setAutoJob(430);
					cm.teachSkill(263, 1, 2);
					cm.getPlayer().setReborns(sel);
					break;

				case 530:
					cjob = 501;
					cm.teachSkill(266, 1, 2);
					cm.getPlayer().setReborns(sel);
					cm.getPlayer().setAutoJob(530);
					nocast = true;
					break;

				case 1100:
					cm.teachSkill(10000255, 1, 2);
					break;
				case 1200:
					cm.teachSkill(10000256, 1, 2);
					break;
				case 1300:
					cm.teachSkill(10000257, 1, 2);
					break;
				case 1400:
					cm.teachSkill(10000258, 1, 2);
					break;
				case 1500:
					cm.teachSkill(10000259, 1, 2);
					break;

				case 3200:
					cm.teachSkill(30000074, 1, 2);
					break;
				case 3300:
					cm.teachSkill(30000074, 1, 2);
					break;
				case 3700:
					cm.teachSkill(30000077, 1, 2);
					break;

				case 14000:
					cm.teachSkill(140000291, 6, 6);
					break;

				case 15000:
					cm.teachSkill(150000079, 1, 1);
					cm.teachSkill(150011005, 1, 1);
					break;

				case 16000:
					cm.teachSkill(160000001, 1, 1);
					break;

				case 16001:
					cm.teachSkill(160010000, 1, 1);
					break;

                 case 15003:
                   cm.teachSkill(150033224, 1, 1);
                   break;

				case 3100:
					cm.teachSkill(30010112, 1, 2);
					cm.addEquip(-10, 1099000, 0, 0, 0, 0, 0, 0);
					break;

				case 3101:
					cm.getPlayer().setAutoJob(3120);
					cm.teachSkill(30010241, 1, 2);
					cm.getPlayer().setHair(36460);
					cm.getPlayer().setFace(20284);
					cm.addEquip(-5, 1050249, 0, 0, 0, 0, 0, 0);
					cm.addEquip(-7, 1070029, 0, 0, 0, 0, 0, 0);
					cm.addEquip(-9, 1102505, 0, 0, 0, 0, 0, 0);
					cm.addEquip(-10, 1099000, 0, 0, 0, 0, 0, 0);
					cm.addEquip(-11, 1232000, 0, 0, 0, 0, 0, 0);
					cm.getPlayer().fakeRelog();
					nocast = true;
					break;

				case 3500:
					cm.teachSkill(30000076, 1, 2);
					cm.teachSkill(30001068, 1, 1);
					break;

				case 10112:
					cm.getPlayer().setAutoJob(110112);
					cm.addEquip(-10, 1562000, 97, 0, 0, 0, 0, 0);
					cm.addEquip(-11, 1572000, 95, 0, 0, 0, 0, 0);
					nocast = true;
					break;
			}

			if (cjob == -1) {
				cjob = seld;
			}
			if (seld == 15200) {
				for (var a = 34900; a < 34950; a++) {
					if (cm.getPlayer().getQuestStatus(a) != 2) {
						MapleQuest.getInstance(a).forceComplete(cm.getPlayer(), 0);
					}
				}
			}
			for (i = 0; i < jobs.length; i++) {
				if (jobs[i]['jobid'] == cjob) {
					for (j = 0; j < jobs[i]['sk'].length; j++) {
						cm.teachSkill(jobs[i]['sk'][j], 30, 30);
					}
					break;
				}
			}
			if (!nocast) {
				job = cjob + 10;
				cm.getPlayer().setAutoJob(job);
			}

			cm.dispose();

			if (seld != 430)
			cm.changeJob(cjob);
            cm.gainItem(1142358, 1);
			cm.gainItem(2000054, 1);
            cm.gainItem(3018469, 1);
			cm.gainMeso(3000000);
            cm.gainItem(2433444, 1);
            cm.gainItem(3700011, 1);
			cm.getPlayer().getStat().setMaxHp(10000, cm.getPlayer());
			cm.getPlayer().getStat().setHp(10000, cm.getPlayer());
			cm.getPlayer().updateSingleStat(MapleStat.HP, 10000);
			cm.getPlayer().AutoTeachSkill();
			max = seld == 2500 || seld == 430 ? 200 : 200;
			if (seld != 10112) {
				for (var i = cm.getPlayer().getLevel(); i < max; i++) {
					cm.gainExp(Packages.constants.GameConstants.getExpNeededForLevel(i));
				}
			}
			cm.getPlayer().setKeyValue(100592, "point", 0);
			cm.warp(1001, 0);
			cm.getPlayer().resetStats(4, 4, 4, 4);
			cm.dispose();

		}
	} else if (status == 3) {
		if (adv) {
			switch (sel) {
				case 110:
					cm.teachSkill(252, 1, 2);
					cjob = 100;
					break;
				case 120:
					cm.teachSkill(253, 1, 2);
					cjob = 100;
					break;
				case 130:
					cm.teachSkill(254, 1, 2);
					cjob = 100;
					break;
				case 210:
					cm.teachSkill(255, 1, 2);
					cjob = 200;
					break;
				case 220:
					cm.teachSkill(256, 1, 2);
					cjob = 200;
					break;
				case 230:
					cm.teachSkill(257, 1, 2);
					cjob = 200;
					break;
				case 310:
					cm.teachSkill(258, 1, 2);
					cjob = 300;
					break;
				case 320:
					cm.teachSkill(259, 1, 2);
					cjob = 300;
					break;
				case 330:
					cm.getPlayer().setAutoJob(330);
					cm.teachSkill(260, 1, 2);
					break;
				case 410:
					cm.teachSkill(261, 1, 2);
					cjob = 400;
					break;
				case 420:
					cm.teachSkill(262, 1, 2);
					cjob = 400;
					break;
				case 510:
					cm.teachSkill(264, 1, 2);
					cjob = 500;
					break;
				case 520:
					cm.teachSkill(265, 1, 2);
					cjob = 500;
					break;
			}
			for (i = 0; i < jobs.length; i++) {
				if (jobs[i]['jobid'] == sel) {
					for (j = 0; j < jobs[i]['sk'].length; j++) {
						cm.teachSkill(jobs[i]['sk'][j], 30, 30);
					}
					break;
				}
			}
		
			cm.warp(1001, 0);
			cm.getPlayer().resetStats(4, 4, 4, 4);
			cm.dispose();
			cm.changeJob(cjob);
			if (sel != 330) {
				cm.getPlayer().setAutoJob(sel);
			}
				for (var i = cm.getPlayer().getLevel(); i < 200; i++) {
				cm.gainExp(Packages.constants.GameConstants.getExpNeededForLevel(i));
			}
			cm.getPlayer().setReborns(sel);
			cm.getPlayer().AutoTeachSkill();
			cm.gainItem(1142358, 1);
			cm.gainItem(2433444, 1);
			cm.gainItem(3018469, 1);
			cm.gainItem(2000054, 1);
			cm.gainItem(2431774, 1);
            cm.gainItem(3700011, 1);
			cm.getPlayer().getStat().setMaxHp(10000, cm.getPlayer());
			cm.getPlayer().getStat().setHp(10000, cm.getPlayer());
			cm.getPlayer().updateSingleStat(MapleStat.HP, 10000);
			cm.getPlayer().setKeyValue(100592, "point", 0);
			cm.gainMeso(3000000);
			//cm.JoinNubGuild();
			//cm.openNpc(2008, "tutorial");
		}
	}
}

function FirstJob(i) {
    var chat = "#fs15#勇士님! #b"+SFQ+"伺服器#k中可以選擇你想玩的職業喔！" + enter;
    switch (i) {
        case 0:
            chat += "#L1# #r戰士#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            chat += "#L2# #r法師#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            chat += "#L3# #r弓箭手#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            chat += "#L4# #r盜賊#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            chat += "#L5# #r海盜#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            chat += "#L330# #r遊俠#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            chat += "#L430# #r雙刃刀#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            chat += "#L530# #r火砲手#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 1000:
            chat += "#L1100# #r靈魂法師#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            chat += "#L1200# #r火焰法師#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            chat += "#L1300# #r風之使者#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            chat += "#L1400# #r夜行者#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            chat += "#L1500# #r神槍手#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 2000:
            chat += "#L2100# #r阿蘭#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 2001:
            chat += "#L2200# #r艾文#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 2002:
            chat += "#L2300# #r梅賽德斯#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 2003:
            chat += "#L2400# #r幽靈#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 2004:
            chat += "#L2700# #r光明使者#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 2005:
            chat += "#L2500# #r銀月#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 3000:
            chat += "#L3200# #r戰鬥法師#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            chat += "#L3300# #r野獸獵人#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            chat += "#L3500# #r機械師#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            chat += "#L3700# #r爆破手#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 3001:
            chat += "#L3100# #r惡魔獵手#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            chat += "#L3101# #r惡魔复仇者#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 3002:
            chat += "#L3600# #r希恩#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 5000:
            chat += "#L5100# #r米哈伊爾#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 6000:
            chat += "#L6100# #r凱撒#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 6001:
            chat += "#L6500# #r天使爆破手#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 6002:
            chat += "#L6400# #r卡德娜#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 6003:
            chat += "#L6300# #r凱因#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 10112:
            chat += "#L10112# #r零#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 14000:
            chat += "#L14200# #r奇奈西斯#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 15000:
            chat += "#L15200# #r伊利姆#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 15001:
            chat += "#L15500# #r亞克#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 15002:
            chat += "#L15100# #r阿黛爾#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 15003:
            chat += "#L15400# #r卡莉#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 16000:
            chat += "#L16400# #r浩英#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
        case 16001:
            chat += "#L16200# #r拉拉#k參與 #b"+SFQ+"伺服器#k的冒險！\r\n";
            break;
    }
    cm.sendOkS(chat, 0x4);
}