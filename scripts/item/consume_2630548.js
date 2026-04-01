importPackage(Packages.constants);
importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.packet.creators);
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(Packages.server);
importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);

var status = 0;
var invs = Array(1, 5);
var invv;
var selected;
var slot_1 = Array();


var slot_2 = Array();
var statsSel;
var hoo = 0;

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var ask = "#fs15##e#r<유니크 강화>#n\r\n\r\n#e#b<조건 : 12성 이상 23성 미만 아이템 사용 가능>#n\r\n#d<아이템당 1회 소모 가능>\r\n\r\n#e#d<소모 아이템 : #i2630548##>\r\n\r\n#e#r공격력, 마력 : +90 , 올스텟 + 150, 보공 10%, 총데미지 2%, 올스텟 6%\r\n\r\n";
        cm.sendYesNo(ask);
    } else if (status == 1) {
        var ok = false;
        var option = "             강화할 아이템을 선택해 주세요\r\n#e#r                  [강화가안될시]\r\n [장비 인벤토리 상단으로 아이템을 옮겨주세요]#n#k\r\n#b";
        for (var x = 0; x < invs.length; x++) {
            var inv = cm.getInventory(invs[x]);
            for (var i = 0; i <= inv.getSlotLimit(); i++) {
                if (x == 0) {
                    slot_1.push(i);
                } else {
                    slot_2.push(i);
                }
                var it = inv.getItem(i);
                if (it == null) {
                    continue;
                }

                var itemid = it.getItemId();
                if (cm.isCash(it.getItemId())) {
                    var itemid = 0;
                } else {
                    var itemid = it.getItemId();
                }
                if (!GameConstants.isEquip(itemid)) {
                    continue;
                }
                ok = true;
                option += "#L" + (invs[x] * 1000 + i) + "##v" + itemid + "##t" + itemid +
                    "##l\r\n";
            }
        }
        if (!ok) {
            cm.sendOk("아이템이 없습니다. 장비창을 확인해주세요.");
            cm.dispose();
            return;
        }
        cm.sendSimple(option + "#k");
    } else if (status == 2) {
        invv = selection / 1000;
        selected = selection % 1000;
        var inzz = cm.getInventory(invv);
        if (invv == invs[0]) {
            statsSel = inzz.getItem(slot_1[selected]);
        } else {
            statsSel = inzz.getItem(slot_2[selected]);
        }
        if (statsSel == null) {
            cm.sendOk("오류가 발생했습니다. 잠시후 다시 시도해주세요.");
            cm.dispose();
            return;
        }

        //if (statsSel.getEnhance() > 35) {
        //if (statsSel.getEnhance() >= 47 || statsSel.getEnhance() < 25) {
        if (statsSel.getEnhance() >= 23 || statsSel.getEnhance() < 12) {
            cm.sendOk("이 아이템에는 사용 할 수 없습니다. 조건을 확인해주세요.");
            cm.dispose();
            return;
        }


        if (!cm.haveItem(2630548, 1)) {
            cm.sendOk("#fs15# #i2630548# 아이템이 필요합니다");
            cm.dispose();
            return;
        }

        statsSel.setStr(statsSel.getStr() + 150);
        statsSel.setDex(statsSel.getDex() + 150);
        statsSel.setInt(statsSel.getInt() + 150);
        statsSel.setLuk(statsSel.getLuk() + 150);
        statsSel.setWatk(statsSel.getWatk() + 90);
        statsSel.setMatk(statsSel.getMatk() + 90);
        statsSel.setBossDamage(statsSel.getBossDamage() + 10);
        statsSel.setAllStat(statsSel.getAllStat() + 6);
        statsSel.setTotalDamage(statsSel.getTotalDamage() + 2);
        //statsSel.setEnhance(statsSel.getEnhance() + 60);
        statsSel.setEnhance(25);
        cm.gainItem(2630548, -1);
        cm.getPlayer().fakeRelog();
        cm.sendOk("#fs 50##fn궁서체# #b강화 성공!#k");
        cm.getPlayer().dropMessage(5, "유니크 강화석 100%가 한 순간 빛나더니 신비로운 힘이 그대로 아이템에 전해졌습니다.");
		World.Broadcast.broadcastMessage(CWvsContext.serverNotice(11, "", cm.getPlayer().getName()+"님이 유니크 강화에 성공하셨습니다 "));
        //	World.Broadcast.broadcastMessage(CWvsContext.serverNotice(4, "[알림] "+ cm.getPlayer().getName()+" 님이 유니크 강화에 성공하셨습니다"));
        //        	World.Broadcast.broadcastMessage(CWvsContext.serverNotice(9, "[알림] "+ cm.getPlayer().getName()+" 님이 유니크 강화에 성공하셨습니다"));
        cm.dispose();
    }
}