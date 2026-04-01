
var status = -1;
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
importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(Packages.launch.world);
importPackage(Packages.scripting);

var check = false;
var a = 0;
inz = null;
function start() {
    status = -1;
    action(1, 0, 0);
}
검정 = "#fc0xFF191919#"
파랑 = "#fc0xFF4641D9#"
빨강 = "#fc0xFFF15F5F#"
보라 = "#fc0xFF8041D9#"
하늘 = "#fc0xFF4374D9#"
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
        말 = "#fs15#" + 검정 + "자네, 안드로이드에 #d신비한 힘#k" + 검정 + "을 새기는 #e" + 파랑 + "각인 시스템#k#n" + 검정 + "이라고 알고 있나?\r\n"
        말 += "#fc0xFFD5D5D5#────────────────────────────#k" + 검정 + "\r\n";
        말 += "#L0##b각인 시스템을 이용하겠습니다.\r\n"
        말 += "#L99##r설명을 듣겠습니다."
        cm.sendOk(말);
    } else if (status == 1) {
        if (!cm.haveItem(2430034, 1)) {
            cm.sendOk("#fs15#" + 검정 + "#i2430034# #z2430034# 아이템이 없는거 같군.");
            cm.dispose();
            return;
        }
        if (selection == 0) {
            a = selection;
            var c = false;
            inz = cm.getInventory(1)
            txt = 검정 + "#fs15#" + 파랑 + "각인#k" + 검정 + "을 사용할 안드로이드를 선택해보게.\r\n\r\n";
            for (w = 0; w < inz.getSlotLimit(); w++) {
                if (!inz.getItem(w) || inz.getItem(w).getItemId() == null) {
                    continue;
                }
                if ((inz.getItem(w).getItemId() >= 1662000 && inz.getItem(w).getItemId() <= 1662999)) {
                    c = true;
                    txt += "#L" + w + "##b#i" + inz.getItem(w).getItemId() + "# #z" + inz.getItem(w).getItemId() + "##l\r\n";
                }
            }
            if (!c) {
                cm.sendOk("#fs15#" + 검정 + "이봐, #h # 각인서를 사용할 안드로이드가 없는거 같네.");
                cm.dispose();
            } else {
                cm.sendSimple(txt);
            }
        } else if (selection == 99) {
            말 = "#fs15#" + 파랑 + "#e각인 시스템#n#k" + 검정 + "이란?\r\n\r\n"
            말 += "안드로이드에 " + 보라 + "추가적인 성능#k" + 검정 + "을 부여하는 시스템입니다.\r\n"
            말 += "각인은 " + 빨강 + "최대 10회#k" + 검정 + "까지 가능하며, 각인의 옵션은 아래와 같습니다.\r\n\r\n"
            말 += "- 올스텟 #r10" + 검정 + "\r\n"
            말 += "- 공격력 / 마력 #r10" + 검정 + "\r\n"
            말 += "- 최대 체력 #r500" + 검정 + "\r\n\r\n"
            말 += "#r10회의 각인을 마친 아이템은 더 이상 강화가 불가능합니다."
            cm.sendOk(말);
        }
    } else if (status == 2) {
        inz = cm.getInventory(1).getItem(selection);
        txt = "#fs15#" + 검정 + "정말 #i"+inz.getItemId()+"# #b#z"+inz.getItemId()+"##k"+검정+"을(를) 각인 시키겠나?\r\n\r\n\r\n";
        if (!ItemFlag.UNTRADEABLE.check(inz.getFlag())) {
            txt += "#fs15##r※ 해당 아이템은 각인 시스템을 적용 시 교환불가 상태로 변경 됩니다.\r\n\r\n"
            txt += "계속 진행 하시겠다면 '예' 버튼을 눌러주세요.";
        }
        txt += "";
        cm.sendYesNo(txt);
    } else if (status == 3) {
        올스텟 = 10;
        공마 = 10;
        체력 = 500;
        if (inz.getEnhance() >= 10) {
            cm.sendOk("#fs15#" + 검정 + "현재 아이템은 각인이 완료되어 더 이상 각인이 불가능하다네. 크크크.");
            cm.dispose();
            return;
        }
        inz.setStr(inz.getStr() + 올스텟);
        inz.setDex(inz.getDex() + 올스텟);
        inz.setInt(inz.getInt() + 올스텟);
        inz.setLuk(inz.getLuk() + 올스텟);
        inz.setWatk(inz.getWatk() + 공마);
        inz.setMatk(inz.getMatk() + 공마);
        inz.setHp(inz.getHp() + 체력);
        inz.setEnhance(inz.getEnhance() + 1);
        flag = inz.getFlag();
        flag |= ItemFlag.UNTRADEABLE.getValue();
        inz.setFlag(flag);
        cm.getClient().send(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, inz));
        if (inz.getEnhance() > 9) {
        World.Broadcast.broadcastMessage(CWvsContext.serverMessage(11, cm.getPlayer().getClient().getChannel(), "", cm.getPlayer().getName() + "님이 {} 각인을 마쳤습니다.", true, inz));
        }
        cm.gainItem(2430034, -1);
        각인횟수 = inz.getEnhance() == 1 ? "첫번째" : inz.getEnhance() == 2 ? "두번째" : inz.getEnhance() == 3 ? "세번째" : inz.getEnhance() == 4 ? "네번째" : inz.getEnhance() == 5 ? "다섯번째" : inz.getEnhance() == 6 ? "여섯번째" : inz.getEnhance() == 7 ? "일곱번째" : inz.getEnhance() == 8 ? "여덟번째" : inz.getEnhance() == 9 ? "아홉째" : inz.getEnhance() == 10 ? "열번째" : "";
        말 = "#fs15#"+검정+""+각인횟수+" 각인의 결과라네. 크크.\r\n"
        말 += "#fc0xFFD5D5D5#─────────────────────────────#k#fs15#" + 파랑 + "\r\n";
        말 += "STR : + " + (inz.getStr() + inz.getEnchantStr()) + " #r(증가 수치 : #e"+올스텟+"#n)#k" + 파랑 + "\r\n"
        말 += "DEX : + " + (inz.getDex() + inz.getEnchantDex()) + " #r(증가 수치 : #e"+올스텟+"#n)#k" + 파랑 + "\r\n"
        말 += "INT : + " + (inz.getInt() + inz.getEnchantInt()) + " #r(증가 수치 : #e"+올스텟+"#n)#k" + 파랑 + "\r\n"
        말 += "LUK : + " + (inz.getLuk() + inz.getEnchantLuk()) + " #r(증가 수치 : #e"+올스텟+"#n)#k" + 파랑 + "\r\n"
        말 += "MAXHP : + " + (inz.getHp() + inz.getEnchantHp()) + " #r(증가 수치 : #e"+체력+"#n)#k" + 파랑 + "\r\n"
        말 += "공격력 : + " + (inz.getWatk() + inz.getEnchantWatk()) + " #r(증가 수치 : #e"+공마+"#n)#k" + 파랑 + "\r\n"
        말 += "마력 : + " + (inz.getMatk() + inz.getEnchantMatk()) + " #r(증가 수치 : #e"+공마+"#n)#k" + 파랑 + "\r\n"
        말 += "보스 몬스터 공격 시 데미지 : + " + (inz.getBossDamage()) + "% #r(증가 수치 : #e"+보공+"%#n)#k" + 파랑 + "\r\n\r\n"
        말 += "#L0# #b한번 더 각인을 진행 하겠습니다.\r\n"
        말 += "#L1# #r그만하겠습니다."
        cm.sendSimple(말);
    } else if (status == 4) {
        if (selection == 0) {
            if (!cm.haveItem(2430034, 1)) {
                cm.sendOk("#fs15#" + 검정 + "#i2430034# #z2430034# 아이템이 없는거 같군.");
                cm.dispose();
                return;
            }
            cm.dispose();
            cm.getClient().removeClickedNPC();
            NPCScriptManager.getInstance().startItem(cm.getPlayer().getClient(), 9000030, "consume_2430034");
        } else if (selection == 1) {
            cm.getClient().removeClickedNPC();
            cm.dispose();
        }
    }
}
