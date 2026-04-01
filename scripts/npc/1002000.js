


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    ★ 에 의해 만들어 졌습니다.

    엔피시아이디 : 1002000

    엔피시 이름 : 필

    엔피시가 있는 맵 : 헤네시스 : 헤네시스 (100000000)

    엔피시 설명 : MISSINGNO


*/
importPackage(Packages.constants);
importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.tools.packet);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
importPackage(Packages.server.enchant);
importPackage(java.sql);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);

var status = -1;
var sel = 0;
var selectitem = 0;
var used = 0;
var use = 0;
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
        말 = "#fs15#想要解放黎明徽章的力量嗎!\r\n"
        말 += "#fc0xFFD5D5D5#─────────────────────────────#k\r\n";
        말 += "#L1##i1182001##b#t1182001# 第一階段解放#l\r\n"
        말 += "#L2##i1182002##t1182002# 第二階段解放#l\r\n"
        말 += "#L3##i1182003##t1182003# 第三階段解放#l\r\n"
        말 += "#L4##i1182004##t1182004# 第四階段解放#l\r\n"
        말 += "#L5##i1182005##t1182005# 第五階段解放#l\r\n"
        cm.sendSimpleS(말, 0x04, 2192030);
    } else if (status == 1) {
        var check = false;
        sel = selection;
        used = (1182000 + (selection - 1));// 當前階級徽章ID 
        use = (4310000 + (selection - 1)); // 所需材料ID 
        inz = cm.getInventory(1);
        txt = "#fs15##d次元石#n#k可用於升級 #b徽章#k.\r\n";
        txt += "#b升級後將解鎖#k, #fc0xFFDB9700#黎明屬性#k第 #r1#k 階段!\r\n";
        txt += "使用#d#z" + use + "##k 成功率為 #r100%#k .\r\n";
        txt += "#fc0xFFD5D5D5#─────────────────────────────#k\r\n";
        txt += "";
        for (w = 0; w < inz.getSlotLimit(); w++) {
            if (!inz.getItem(w)) {
                continue;
            }
            if (inz.getItem(w).getItemId() == used) {
                txt += "#L" + w + "##i" + inz.getItem(w).getItemId() + "# #z" + inz.getItem(w).getItemId() + "##l\r\n";
                check = true;
            }
        }
        if (!check) {
            cm.sendOkS("#fs15##i" + used + "# #r#z" + used + "##k 未持有該徽章.", 4, 2192030);
            cm.dispose();
        } else {
            cm.sendSimpleS(txt, 0x04, 2192030);
        }
    } else if (status == 2) {
        selectitem = inz.getItem(selection);
        var state = selectitem.getState() == 20 ? "#fUI/UIWindow2.img/AdditionalOptionTooltip/legendary# #k#e#fs15##fc0xFFAAD34A#레전더리#fs15##n#k" : selectitem.getState() == 19 ? "#fUI/UIWindow2.img/AdditionalOptionTooltip/unique# #k#e#fs15##fc0xFFEDA900#유니크#fs15##n" : selectitem.getState() == 18 ? "#fUI/UIWindow2.img/AdditionalOptionTooltip/epic# #k#e#fs15##fc0xFF5F00FF#에픽#fs15##n" : selectitem.getState() == 17 ? "#fUI/UIWindow2.img/AdditionalOptionTooltip/rare# #k#e#fs15##fc0xFF1266FF#레어#fs15##n" : "없음";

        txt = "#fs15#確認要升級#b[ #i" + selectitem.getItemId() + "# #z" + selectitem.getItemId() + "##l]#k嗎？?\r\n";
        txt += "將消耗#r#z" + use + "##k .\r\n"; //차원에 따라 표기 다르게
        txt += "#fc0xFFD5D5D5#─────────────────────────────#k\r\n";
        txt += "當前徽章屬性#b#z" + selectitem.getItemId() + "#.\r\n"
        txt += "#fc0xFFBDBDBD#陞級時，選項將保留，並更改為帳戶內可移動。.\r\n\r\n#d"

        if (selectitem.getStr() > 0)
            txt += "力量 : +" + (selectitem.getStr() + selectitem.getEnchantStr()) + "\r\n"
        if (selectitem.getDex() > 0)
            txt += "敏捷 : +" + (selectitem.getDex() + selectitem.getEnchantDex()) + "\r\n"
        if (selectitem.getInt() > 0)
            txt += "智力 : +" + (selectitem.getInt() + selectitem.getEnchantInt()) + "\r\n"
        if (selectitem.getLuk() > 0)
            txt += "幸運 : +" + (selectitem.getLuk() + selectitem.getEnchantLuk()) + "\r\n"
        if (selectitem.getHp() > 0)
            txt += "最大HP : +" + (selectitem.getHp() + selectitem.getEnchantHp()) + "\r\n"
        if (selectitem.getMp() > 0)
            txt += "最大MP : +" + (selectitem.getMp() + selectitem.getEnchantMp()) + "\r\n"
        if (selectitem.getWatk() > 0)
            txt += "物理攻擊力 : +" + (selectitem.getWatk() + selectitem.getEnchantWatk()) + "\r\n"
        if (selectitem.getMatk() > 0)
            txt += "魔法攻擊力 : +" + (selectitem.getMatk() + selectitem.getEnchantMatk()) + "\r\n"
        if (selectitem.getWdef() > 0)
            txt += "物理防禦力 : +" + (selectitem.getWdef() + selectitem.getEnchantWdef()) + "\r\n"
        if (selectitem.getAcc() > 0)
            txt += "命中率 : +" + (selectitem.getAcc() + selectitem.getEnchantAcc()) + "\r\n"
        if (selectitem.getAvoid() > 0)
            txt += "迴避率 : +" + (selectitem.getAvoid() + selectitem.getEnchantAviod()) + "\r\n"
        if (selectitem.getSpeed() > 0)
            txt += "移動速度 : +" + selectitem.getSpeed() + "\r\n"
        if (selectitem.getJump() > 0)
            txt += "跳躍力 : +" + selectitem.getJump() + "\r\n"
        if (selectitem.getBossDamage() > 0)
            txt += "BOSS傷害加成 : +" + selectitem.getBossDamage() + "\r\n"
        if (selectitem.getIgnorePDR() > 0)
            txt += "無視怪物防禦率 : +" + selectitem.getIgnorePDR() + "\r\n"
        if (selectitem.getTotalDamage() > 0)
            txt += "總傷害加成 : +" + selectitem.getTotalDamage() + "\r\n"
        if (selectitem.getAllStat() > 0)
            txt += "全屬性 : +" + selectitem.getAllStat() + "\r\n"
        if (selectitem.getReqLevel() > 0)
            txt += "需求等級降低 : +" + selectitem.getReqLevel() + "\r\n"
        if (selectitem.getEnhance() > 0)
            txt += "星力強化等級 : +" + selectitem.getEnhance() + "\r\n"
        if (selectitem.getLevel() > 0)
            txt += "卷軸強化次數 : + " + selectitem.getLevel() + "\r\n"
        if (selectitem.getUpgradeSlots() > 0)
            txt += "可升級次數 : +" + selectitem.getUpgradeSlots() + "\r\n"
        if (state != null) {
            txt += "潛在能力等級 : " + state + "\r\n";
        }
        if (selectitem.getViciousHammer() > 0) {
            txt += "黃金鐵鎚效果已應用#k\r\n\r\n"
        }

        txt += "若確定要進行解放，請在輸入欄鍵入：\r\n#e#r確認解放#n#k\r\n\r\n";
        txt += "#fs15##r※ 潛在能力選項雖不顯示但會完整繼承#k\r\n";
        cm.sendGetText(txt, 2192030);
    } else if (status == 3) {
        text = cm.getText().replaceAll(" ", "");;
        if (!text.contains("確認解放")) {
            cm.dispose();
        } else {
            if (!cm.haveItem(use, 1)) {   //재료체크
                cm.sendOkS("#fs15##r#z"  + use + "##k 道具不足，請再次確認", 0x04, 2192030);
                cm.dispose();
                return;
            }
            var starforce = selectitem.getEnhance();
            wonitem = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(selectitem.getItemId());
            giveitem = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(selectitem.getItemId() + 1);
            var name = giveitem.getItemId() == 1182001 ? "第一階段" : giveitem.getItemId() == 1182002 ? "第二階段" : giveitem.getItemId() == 1182003 ? "第三階段" : giveitem.getItemId() == 1182004 ? "第四階段" : giveitem.getItemId() == 1182005 ? "第五階段" : "";
            giveitem.addStr((selectitem.getStr()) - wonitem.getStr());
            giveitem.addDex((selectitem.getDex()) - wonitem.getDex());
            giveitem.addInt((selectitem.getInt()) - wonitem.getInt());
            giveitem.addLuk((selectitem.getLuk()) - wonitem.getLuk());
            giveitem.addHp((selectitem.getHp()) - wonitem.getHp());
            giveitem.addMp((selectitem.getMp()) - wonitem.getMp());
            giveitem.addWatk((selectitem.getWatk()) - wonitem.getWatk());
            giveitem.addMatk((selectitem.getMatk()) - wonitem.getMatk());
            giveitem.addWdef((selectitem.getWdef()) - wonitem.getWdef());
            giveitem.addMdef((selectitem.getMdef()) - wonitem.getMdef());
            //    giveitem.addAcc((selectitem.getAcc()) - wonitem.getAcc());
            //    giveitem.addAvoid((selectitem.getAvoid()) - wonitem.getAvoid());
            giveitem.addSpeed((selectitem.getSpeed()) - wonitem.getSpeed());
            giveitem.addJump((selectitem.getJump()) - wonitem.getJump());
            giveitem.addBossDamage((selectitem.getBossDamage()) - wonitem.getBossDamage());
            giveitem.addIgnoreWdef((selectitem.getIgnorePDR()) - wonitem.getIgnorePDR());
            giveitem.addAllStat((selectitem.getAllStat()) - wonitem.getAllStat());
            giveitem.setUpgradeSlots(selectitem.getUpgradeSlots());
            giveitem.setLevel(selectitem.getLevel());
            giveitem.setState(selectitem.getState());
            giveitem.setPotential1(selectitem.getPotential1());
            giveitem.setPotential2(selectitem.getPotential2());
            giveitem.setPotential3(selectitem.getPotential3());
            giveitem.setPotential4(selectitem.getPotential4());
            giveitem.setPotential5(selectitem.getPotential5());
            giveitem.setPotential6(selectitem.getPotential6());
            giveitem.setViciousHammer(selectitem.getViciousHammer());
            cm.JoinNubGuild(giveitem, starforce)
            MapleInventoryManipulator.removeFromSlot(cm.getClient(), GameConstants.getInventoryType(selectitem.getItemId()), selectitem.getPosition(), selectitem.getQuantity(), false);
            MapleInventoryManipulator.addbyItem(cm.getClient(), giveitem);
            World.Broadcast.broadcastMessage(CWvsContext.serverMessage(11, cm.getPlayer().getClient().getChannel(), "", cm.getPlayer().getName() + "님이 차원의 스톤을 사용하여 {}를 획득하셨습니다!", true, giveitem));

            cm.gainItem(use, -1);
            말 = "#fs15#" + name + " 解放成功了!\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i" + giveitem.getItemId() + "##b#z" + giveitem.getItemId() + "##k";
            cm.sendOkS(말, 0x04, 2192030);
            cm.dispose();
        }

    }
}

