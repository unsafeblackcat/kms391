importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);
importPackage(Packages.constants);
importPackage(Packages.server.quest);


var enter = "\r\n";
var seld = -1;

var bjob = -1;
var cjob = -1;
var adv = false;
var level = 1;


var jobs = [
    { 'jobid': 110, 'jobname': "英雄", 'job': "冒險家", 'stat': 1, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 120, 'jobname': "聖騎士", 'job': "冒險家", 'stat': 1, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 130, 'jobname': "黑騎士", 'job': "冒險家", 'stat': 1, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 210, 'jobname': "火毒", 'job': "冒險家", 'stat': 3, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 220, 'jobname': "冰雷", 'job': "冒險家", 'stat': 3, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 230, 'jobname': "主教", 'job': "冒險家", 'stat': 3, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 310, 'jobname': "神射手", 'job': "冒險家", 'stat': 2, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 320, 'jobname': "箭神", 'job': "冒險家", 'stat': 2, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 330, 'jobname': "遠古弓", 'job': "冒險家", 'stat': 2, 'sk': [80001152, 1281, 1297, 1298, 12, 73] },
    { 'jobid': 410, 'jobname': "隱士", 'job': "冒險家", 'stat': 2, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 420, 'jobname': "獨行客", 'job': "冒險家", 'stat': 4, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 430, 'jobname': "雙刀", 'job': "冒險家", 'stat': 4, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 510, 'jobname': "衝鋒隊長", 'job': "冒險家", 'stat': 1, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 520, 'jobname': "船長", 'job': "冒險家", 'stat': 2, 'sk': [80001152, 1281, 12, 73] },
    { 'jobid': 530, 'jobname': "神炮王", 'job': "冒險家", 'stat': 1, 'sk': [80001152, 1281, 110, 109, 111, 1283, 12, 73] },
    { 'jobid': 1100, 'jobname': "魂騎士", 'job': "騎士團", 'stat': 1, 'sk': [10001244, 10000252, 80001152, 10001253, 10001254, 10001245, 10000250, 10000246, 10000012, 10000073] },
    { 'jobid': 1200, 'jobname': "炎術士", 'job': "騎士團", 'stat': 3, 'sk': [10001244, 10000252, 80001152, 10001253, 10001254, 10001245, 10000250, 10000248, 10000012, 10000073] },
    { 'jobid': 1300, 'jobname': "風靈使者", 'job': "騎士團", 'stat': 2, 'sk': [10001244, 10000252, 80001152, 10001253, 10001254, 10001245, 10000250, 10000247, 10000012, 10000073] },
    { 'jobid': 1400, 'jobname': "夜行者", 'job': "騎士團", 'stat': 4, 'sk': [10001244, 10000252, 80001152, 10001253, 10001254, 10001245, 10000250, 10000249, 10000012, 10000073] },
    { 'jobid': 1500, 'jobname': "奇襲者", 'job': "騎士團", 'stat': 1, 'sk': [10001244, 10000252, 80001152, 10001253, 10001254, 10001245, 10000250, 10000246, 10000012, 10000073] },
    { 'jobid': 5100, 'jobname': "米哈爾", 'job': "騎士團", 'stat': 1, 'sk': [50001214, 10000250, 10000074, 10000075, 50000012, 50000073] },
    { 'jobid': 2100, 'jobname': "戰神", 'job': "英雄", 'stat': 1, 'sk': [20000194, 20001295, 20001296, 20000012, 20000073] },
    { 'jobid': 2200, 'jobname': "龍神", 'job': "英雄", 'stat': 3, 'sk': [20010022, 20010194, 20011293, 20010012, 20010073] },
    { 'jobid': 2300, 'jobname': "雙弩精靈", 'job': "英雄", 'stat': 2, 'sk': [20020109, 20021110, 20020111, 20020112, 20020012, 20020073] },
    { 'jobid': 2400, 'jobname': "幻影", 'job': "英雄", 'stat': 4, 'sk': [20031208, 20040190, 20031203, 20031205, 20030206, 20031207, 20031209, 20031251, 20031260, 20030012, 20030073] },
    { 'jobid': 2500, 'jobname': "隱月", 'job': "英雄", 'stat': 1, 'sk': [20051284, 20050285, 20050286, 20050074, 20050012, 20050073] },
    { 'jobid': 2700, 'jobname': "夜光法師", 'job': "英雄", 'stat': 3, 'sk': [20040216, 20040217, 20040218, 20040219, 20040221, 20041222, 20040012, 20040073] },
    { 'jobid': 14200, 'jobname': "超能力者", 'job': "英雄", 'stat': 3, 'sk': [140000291, 14200, 14210, 14211, 14212, 140001290, 140000012, 140000073] },
    { 'jobid': 15212, 'jobname': "聖晶使徒", 'job': "英雄", 'stat': 3, 'sk': [150000079, 150011005, 15212] },
    { 'jobid': 3200, 'jobname': "幻靈鬥師", 'job': "反抗者", 'stat': 3, 'sk': [30001281, 30000012, 30000073] },
    { 'jobid': 3300, 'jobname': "豹弩游俠", 'job': "反抗者", 'stat': 2, 'sk': [30001281, 30001062, 30001061, 30000012, 30000073] },
    { 'jobid': 3500, 'jobname': "機械師", 'job': "反抗者", 'stat': 2, 'sk': [30001281, 30001068, 30000227, 30000012, 30000073] },
    { 'jobid': 3600, 'jobname': "尖兵", 'job': "反抗者", 'stat': 5, 'sk': [30001281, 30020232, 30020233, 30020234, 30020240, 30021235, 30021236, 30021237, 30020012, 30020073] },
    { 'jobid': 3100, 'jobname': "惡魔獵手", 'job': "反抗者", 'stat': 1, 'sk': [30001281, 80001152, 30001061, 30010110, 30010185, 30010112, 30010111, 30010012, 30010073] },
    { 'jobid': 3101, 'jobname': "惡魔復仇者", 'job': "反抗者", 'stat': 6, 'sk': [30001281, 80001152, 30001061, 30010110, 30010185, 30010242, 30010241, 30010230, 30010231, 30010232, 30010012, 30010073] },
    { 'jobid': 6100, 'jobname': "狂龍戰士", 'job': "諾巴", 'stat': 1, 'sk': [600000219, 60000222, 60001217, 60001216, 60001225, 60001005, 60001296, 60000012, 60000073] },
    { 'jobid': 6400, 'jobname': "魔鏈影士", 'job': "諾巴", 'stat': 4, 'sk': [60020216, 60021217, 60021005, 60020218, 60020012, 60020073] },
    { 'jobid': 6300, 'jobname': "煉獄黑客", 'job': "諾巴", 'stat': 2, 'sk': [60031005, 60030012, 60030073] },
    { 'jobid': 6500, 'jobname': "爆莉萌天使", 'job': "諾巴", 'stat': 2, 'sk': [60011216, 60010217, 60011218, 60011219, 60011220, 60011221, 60011222, 60011005, 60010012, 60010073] },
    { 'jobid': 15100, 'jobname': "阿黛爾", 'job': "異人", 'stat': 1, 'sk': [150020041, 150021000, 150020079, 150020006, 150020241] },
    { 'jobid': 15500, 'jobname': "影魂異人", 'job': "異人", 'stat': 1, 'sk': [150010079, 150011005, 150011074, 150010241, 150010012, 150010073] },
    { 'jobid': 16400, 'jobname': "虎影", 'job': "阿尼瑪", 'stat': 4, 'sk': [160001074, 160001075, 160001005, 160000000, 160000076, 160000012, 160000073, 164001004] },
    { 'jobid': 16200, 'jobname': "元素師", 'job': "阿尼瑪", 'stat': 4, 'sk': [160010000, 160011075] }
]

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
      var text = "            #e#fs15##i5069100# #fc0xFF3F0099#KMS 幻影楓葉 #i5069100#  #k  \r\n#n#fs15#";
      text += "                     #Cgray##eSince. 2025-05-19\r\n\r\n#d#n#fs15#";
      text += "#e#fs15##fc0xFF4374D9#歡迎來到Fancy Maple.\r\n";
      text += "在開始之前，我將介紹一些規則。.\r\n\r\n";
      text += "#fc0xFF3F0099##i3801300# 禁止發其他服務器言論或貶低服務器的言論\r\n";
      text += "#fc0xFF3F0099##i3801301# 禁止發表貶低用戶和運營者的言論\r\n";
      text += "#fc0xFF3F0099##i3801302# 禁止使用非授權程式、用戶端改、宏、核等\r\n";
      text += "#fc0xFF3F0099##i3801303# 遊玩中禁止助長氣汾和煽動行韋\r\n";
      text += "#fc0xFF3F0099##i3801304# 遊玩中禁止物品現金交易\r\n\r\n";
      text += "#fc0xFF4374D9#是否接受所有規則開始？";
      cm.sendYesNo(text);
   } else if (status == 1) {
        if (GameConstants.isYeti(cm.getPlayer().getJob())) {
            cm.gainItem(2634282, 1);
			cm.gainMeso(1000000000);
            cm.gainItem(2434657, 1);
            cm.gainItem(3700011, 1);
            cm.getPlayer().AutoTeachSkill();
            cm.warp(910000000);
            cm.getPlayer().resetStats(4, 4, 4, 4);
            cm.openNpc(2007, "YetiTuto0");
            return;
        } else if (GameConstants.isPinkBean(cm.getPlayer().getJob())) {
            cm.gainItem(2634282, 1);
			cm.gainMeso(1000000000);
            cm.gainItem(2434657, 1);
            cm.gainItem(3700011, 1);
            cm.getPlayer().AutoTeachSkill();
            cm.warp(910000000);
            cm.getPlayer().resetStats(4, 4, 4, 4);
            cm.openNpc(2007, "PinkBeanTuto0");
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
                case 15003:
                    cm.teachSkill(150031074, 1, 1);//飛刃沙士
                    cm.teachSkill(150030079, 1, 1);//飛刃沙士
                    cm.teachSkill(150030241, 1, 1);//飛刃沙士
                    break;

                case 2000:
                    cm.teachSkill(20001295, 1, 1);//戰勝
                    break;

                case 6000:
                    cm.teachSkill(60000219, 1, 1);//狂龍
                    cm.teachSkill(60001217, 1, 1);//狂龍
                    cm.teachSkill(60001216, 1, 1);//狂龍
                    cm.teachSkill(60001218, 1, 1);//狂龍
                    cm.teachSkill(60001219, 1, 1);//狂龍
                    cm.teachSkill(60001225, 1, 1);//狂龍
                    cm.addEquip(-10, 1352500, 0, 0, 0, 0, 0, 0);
                    break;

                case 6001:
                    cm.addEquip(-10, 1352601, 0, 0, 0, 0, 0, 0);//爆莉萌
                    break;

                case 2004:
                    cm.teachSkill(27000106, 5, 5);//夜光
                    cm.teachSkill(27000207, 5, 5);//夜光
                    cm.teachSkill(27001201, 20, 20);//夜光
                    cm.teachSkill(27001100, 20, 20);//夜光
                    break;
                case 2005:
                    cm.teachSkill(20051284, 30, 30);//隱月
                    cm.teachSkill(20050285, 30, 30);//隱月
                    cm.teachSkill(20050286, 30, 30);//隱月
                    //cm.teachSkill(25000003, 30, 30);
                    break;

                case 1000:
                    cm.teachSkill(10001251, 1, 1);//元素
                    cm.teachSkill(10001252, 1, 1);//元素
                    cm.teachSkill(10001253, 1, 1);//元素
                    cm.teachSkill(10001254, 1, 1);//元素
                    cm.teachSkill(10001255, 1, 1);//元素
                    break;
                case 15002:
                    cm.teachSkill(151001004, 1, 1);//御劍
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
                     cm.teachSkill(10000252, 1, 2);//希納斯護佑
                    break;
                case 1200:
                    cm.teachSkill(10000256, 1, 2);//希納斯護佑
                    break;
                case 1300:
                    cm.teachSkill(10000257, 1, 2);//希納斯護佑
                    break;
                case 1400:
                    cm.teachSkill(10000258, 1, 2);//希納斯護佑
                    break;
                case 1500:
                    cm.teachSkill(10000259, 1, 2);//希納斯護佑
                    break;

                case 3200:
                    cm.teachSkill(30000074, 1, 2);//自由化身
                    break;
                case 3300:
                    cm.teachSkill(30000074, 1, 2);//自由化身
                    break;
                case 3700:
                    cm.teachSkill(30000077, 1, 2);//自由化身
                    break;

                case 14000:
                    cm.teachSkill(140000291, 6, 6);//超感
                    break;

                case 15212:
                    cm.teachSkill(150000079, 1, 1);//魔力綫路
                    cm.teachSkill(150011005, 1, 1);//專注
                    break;

                case 16000:
                    cm.teachSkill(160000001, 1, 1);//自信感

                    break;

                case 16001:
                    cm.teachSkill(160010000, 1, 1);//精靈親和

                    break;

                    case 2500:
                        cm.teachSkill(20051284,30,30);//縮地
                        cm.teachSkill(20050285,30,30);//精靈凝聚第一招
                        cm.teachSkill(20050286,30,30);//九死一生
                        cm.teachSkill(25001000,30,30);//衝擊拳
                        cm.teachSkill(25001002,30,30);//閃拳
                        cm.teachSkill(25000003,30,30);//閃拳
                    break;
                case 3100:
                    cm.teachSkill(30010112, 1, 2);//惡魔之怒
                    cm.addEquip(-10, 1099000, 0, 0, 0, 0, 0, 0);
                    break;

                case 3101:
                    cm.getPlayer().setAutoJob(3120);
                    cm.teachSkill(30010241, 1, 2);//野性憤怒
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
                    cm.teachSkill(30000076, 1, 2);//自由化身（海盗）
                    cm.teachSkill(30001068, 1, 1);//喷射加速
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
            cm.gainItem(2634282, 1);
			cm.gainMeso(100000000);
            cm.getPlayer().AutoTeachSkill();
            max = seld == 2500 || seld == 430 ? 20 : level;
            cm.warp(910000000, 0);
            cm.getPlayer().resetStats(4, 4, 4, 4);
                for (var i = cm.getPlayer().getLevel(); i < level; i++) {
                    cm.gainExp(Packages.constants.GameConstants.getExpNeededForLevel(i));
            }
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
            cm.warp(910000000, 0);
            cm.getPlayer().resetStats(4, 4, 4, 4);
            cm.dispose();
            cm.changeJob(cjob);
            if (sel != 330) {
                cm.getPlayer().setAutoJob(sel);
            }
            cm.getPlayer().setReborns(sel);
            cm.getPlayer().AutoTeachSkill();
            cm.gainItem(2634282, 1);
			cm.gainMeso(100000000);
            
            for (var i = cm.getPlayer().getLevel(); i < level; i++) {
                cm.gainExp(Packages.constants.GameConstants.getExpNeededForLevel(i));
            }
        }
    }
}

function SecondJob(i) {
    var adventure = "#e#fs15##請選擇想要的職業#b" + enter;
    switch (i) {
        case 1:+ enter;
            adventure += "#L110# 英雄\r\n";
            adventure += "#L120# 聖騎士\r\n";
            adventure += "#L130# 黑騎士\r\n";
            break;
        case 2:
            //adventure += "현재 당신이 선택한 직업군은 #b마법사#k 입니다.\r\n";
            adventure += "#L210# 火毒\r\n";
            adventure += "#L220# 冰雷\r\n";
            adventure += "#L230# 主教\r\n";
            break;
        case 3:
            adventure += "現在你選擇的職業群 #b是#k 弓箭手.\r\n";
            adventure += "#L310# 神射手\r\n";
            adventure += "#L320# 箭神\r\n";
            //adventure += "#L330# #r패스파인더#k로 #bNero 서버#k에 참여할게!\r\n";
            break;
        case 4:
            //adventure += "현재 당신이 선택한 직업군은 #b도적#k 입니다.\r\n";
            adventure += "#L410# 隱士\r\n";
            adventure += "#L420# 獨行客\r\n";
            break;
        case 5:
            //adventure += "현재 당신이 선택한 직업군은 #b해적#k 입니다.\r\n";
            adventure += "#L510# 衝鋒隊長\r\n";
            adventure += "#L520# 船長\r\n";
            break;
    }
    return adventure;
}

function FirstJob(i) {
    var chat = "#e#fs15##請選擇想要的職業#b" + enter;
    switch (i) {
        case 0:
            chat += "#L1# 戰士\r\n";
            chat += "#L2# 魔法師\r\n";
            chat += "#L3# 弓箭手\r\n";
            chat += "#L4# 盜賊\r\n";
            chat += "#L5# 海盜\r\n";
            chat += "#L330# 遠古弓\r\n";
            chat += "#L430# 雙刀\r\n";
            chat += "#L530# 神砲王\r\n";
            break;
        case 1000:
            chat += "#L1100# 魂騎士\r\n";
            chat += "#L1200# 炎術士\r\n";
            chat += "#L1300# 風靈使者\r\n";
            chat += "#L1400# 夜行者\r\n";
            chat += "#L1500# 奇襲者\r\n";
            break;
        case 2000:
            chat += "#L2100# 戰神\r\n";
            break;
        case 2001:
            chat += "#L2200# 龍神\r\n";
            break;
        case 2002:
            chat += "#L2300# 雙弩精靈\r\n";
            break;
        case 2003:
            chat += "#L2400# 幻影\r\n";
            break;
        case 2004:
            chat += "#L2700# 夜光法師\r\n";
            break;
        case 2005:
            chat += "#L2500# 隱月\r\n";
            break;
        case 3000:
            chat += "#L3200# 幻靈鬥師\r\n";
            chat += "#L3300# 豹弩游俠\r\n";
            chat += "#L3500# 機械師\r\n";
            chat += "#L3700# 爆破手\r\n";
            break;
        case 3001:
            chat += "#L3100# 惡魔獵手\r\n";
            chat += "#L3101# 惡魔復仇者\r\n";
            break;
        case 3002:
            chat += "#L3600# 尖兵\r\n";
            break;
        case 5000:
            chat += "#L5100# 米哈爾\r\n";
            break;
        case 6000:
            chat += "#L6100# 狂龍戰士\r\n";
            break;
        case 6001:
            chat += "#L6500# 爆莉萌天使\r\n";
            break;
        case 6002:
            chat += "#L6400# 魔鏈影士\r\n";
            break;
        case 6003:
            chat += "#L6300# 煉獄黑客\r\n";
            break;
        case 10112:
            chat += "#L10112# 神之子\r\n";
            break;
        case 14000:
            chat += "#L14200# 超能力者\r\n";
            break;
        case 15000:
            chat += "#L15212# 聖晶使徒\r\n";
            break;
        case 15001:
            chat += "#L15500# 影魂藝人\r\n";
            break;
        case 15002:
            chat += "#L15100# 阿黛爾\r\n";
            break;
        case 16000:
            chat += "#L16400# 虎影\r\n";
            break;
        case 16001:
            chat += "#L16200# 元素師\r\n";
            break;
        case 15003:
            chat += "#L15400# 飛刃沙士\r\n";
            break;
    }
    cm.sendOkS(chat, 0x4);
}