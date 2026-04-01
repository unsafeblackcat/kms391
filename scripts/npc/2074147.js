importPackage(java.sql);
importPackage(java.lang);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);

스톤 = 4310005
var count = 0;
var suc = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var leaf = cm.itemQuantity(4310006);
        말 = "\r\n#fs15#반갑네 #h #! "
        말 += "#b#z4310006##k을 정제하러 왔나?\r\n"
        말 += "정제를 하면 #r#z4310007##k를 #e랜덤#n으로 획득이 가능하다네.\r\n"
        말 += "아참! #b#z4310006##k을 분해를 많이 할수록 \r\n#r#z4310007##k의 획득 확률이 늘어나니 참고하도록 하게나.\r\n\r\n"
        말 += "자네는 #b#z4310006##k을 #r" + leaf + "#k개 가지고 있네 몇개를 분해할텐가?\r\n";
        말 += "#e(분해 가능 갯수 : #r" + leaf + "#k개)#n";
        cm.sendGetNumber(2192030, 말, leaf, 1, leaf);
    } else if (status == 1) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        if (leftslot < 2) {
            cm.sendOkS("#fs15##r기타칸 2 칸 이상을 확보하고 다시 말을 걸어주게.", 0x04, 2192030);
            cm.dispose();
            return;
        }
        suc = 5;
        count = selection;
        if (count >= 100) { // 
            for (var i = 0; i <= count; i++) {
                if (i > 0 && i % 100 == 0) {
                    suc += 0.5;
                    //cm.getPlayer().dropMessage(5, ""+i);
                }
            }
        }
        if (suc >= 20) {
            suc = 20;
        }
        var text = "#fs15#정말로 #b#z4310006# " + count + "개#k를 정제할텐가?\r\n";
        var textadd = suc >= 20 ? "현재 정제 성공 확률 : #e" + suc + "#n% (#r최대 확률입니다!#n#k)" : "현재 정제 성공 확률 : #r#e" + suc + "#n%";
        cm.sendYesNoS(text + textadd, 0x04, 2192030);
    } else if (status == 2) {
        var givecount = 0;
        for (var i = 0; i < count; i++) {
            if (Randomizer.isSuccess(suc * 10, 1000)) {
                givecount++;
            }
        }
        cm.gainItem(4310006, -count);
        cm.gainItem(4310007, givecount);
        cm.sendOkS("#fs15#정제한 결과가 나왔다네.\r\n\r\n#fUI/UIWindow2.img/QuestIcon/4/0#\r\n#b#i4310007# #z4310007# "+givecount+"개", 0x04, 2192030);
        cm.dispose();
    }
}