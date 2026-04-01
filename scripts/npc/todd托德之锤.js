var status = -1;

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
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        cm.sendSimple(" 托德的錘子使用說明。請選擇您想查詢的項目：\r\n#b#L0#什麼是托德的錘子？\r\n#L1#可傳承的選項\r\n#L2#結束對話");
    } else if (status == 1) {
   status = -1;
   cm.dispose();
   if (selection == 0) {
            cm.sendNext(" 這是將一件裝備的選項轉移到另一件裝備的功能。被轉移選項的原始裝備將會消失。");
   } else if (selection == 1) {
            cm.sendNext("#d 可傳承的選項：星力強化、潛在能力（降級為史詩等級） #k \r\n#b保留的選項：附加選項（全屬性、總傷害、BOSS傷害）、剪刀使用次數\r\n剩餘卷軸次數：自動使用100%卷軸，若可傳承的潛在選項在#b史詩等級#k中不存在，則會替換為新的潛在能力。");
   }
    }
}

