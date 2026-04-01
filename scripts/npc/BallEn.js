importPackage(Packages.client.inventory); 
importPackage(Packages.server); 
importPackage(java.text); 
 
var St = -1;
isReady = false;
var nf = java.text.NumberFormat.getInstance(); 
geti = null;
hm = 0;
 
var banitem = [1712001, 1712002, 1712003, 1712004, 1712005, 1712006, 1713000, 1713001];
 
function start() {
    St = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    enhance = ["攻擊BOSS時傷害 +%", "無視怪物防禦率 +%", "傷害 +%", "全屬性 +%"]
 
    itemid = 4310010;
    qty = [100, 50, 200, 80]; //1%所需道具數量 
    limit = [40, 50, 80, 10];
 
    if (mode == 1) {
        if (St == 3 && selection != 10) {
            if (selection == 0) {
                hw -= hm;
                hm = 0;
            } else if (selection == 1) {
                hw -= 10;
                hm -= 10;
            } else if (selection == 2) {
                hw -= 1;
                hm -= 1;
            } else if (selection == 3) {
                hw += 1;
                hm += 1;
            } else if (selection == 4) {
                hw += 10;
                hm += 10;
            } else {
                hw += limit[st2] - hw;
                hm = hw - parseInt(gE);
            }
        } else {
            St++;
        }
    } else {
        cm.dispose(); 
        return;
    }
    if (St == 0) {
        對話 = "#fs20#持有#r#i"+itemid+"##z"+itemid+"##n#k#l可以讓所有升級過的裝備使用後援幣附加特殊選項#fc0xFF000000#。\r\n\r\n";
        對話 += "#L0# #b附加選項#k#l";
        cm.sendSimpleS(對話,0x86);
    }else if (St == 1) {
    對話 = "#fs15##b請選擇要附加能力的裝備。\r\n\r\n";
    var counter = 0; // 新增计数器 
    var rowContent = ""; // 存储每行的内容
    
    for (i = 0; i < cm.getInventory(1).getSlotLimit();  i++) {
        if ((cm.getInventory(1).getItem(i)  != null) && !cm.isCash(cm.getInventory(1).getItem(i).getItemId()))  {
            if(cm.getInventory(1).getItem(i).getItemId()  == banitem[0] || cm.getInventory(1).getItem(i).getItemId()  == banitem[1] || cm.getInventory(1).getItem(i).getItemId()  == banitem[2] || cm.getInventory(1).getItem(i).getItemId()  == banitem[3] || cm.getInventory(1).getItem(i).getItemId()  == banitem[4] || cm.getInventory(1).getItem(i).getItemId()  == banitem[5] || cm.getInventory(1).getItem(i).getItemId()  == banitem[6] || cm.getInventory(1).getItem(i).getItemId()  == banitem[7]){
                continue;
            }
            
            // 添加道具到当前行
            rowContent += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId()  + "#";
            counter++;
            
            // 每10个道具换行
            if (counter % 8 == 0) {
                對話 += rowContent + "\r\n";
                rowContent = ""; // 清空当前行内容 
            }
        }
    }
    
    // 添加剩余不足10个的道具 
    if (rowContent.length  > 0) {
        對話 += rowContent + "\r\n";
    }
    
    cm.sendSimpleS( 對話,0x86);
}else if (St == 2) {
        st = selection;
        對話 = "#fs15##b選擇的裝備 :#k#n #i" + cm.getInventory(1).getItem(st).getItemId()  + "#\r\n";
        對話 += "請選擇要强化的選項。\r\n";
        if (isWeapon(cm.getInventory(1).getItem(st).getItemId()))  {
        }
            對話 += "#L0##b攻擊BOSS時傷害 % (最大 40%)#l\r\n";
            對話 += "#L1#無視怪物防禦率 % (最大 50%)#l\r\n";
            對話 += "#L2#傷害 % (最大 80%)#l\r\n";
            對話 += "#L3#全屬性 % (最大 20%)#l\r\n"
        cm.sendSimpleS(對話,0x86);
    } else if (St == 3) {
        if (!isReady) {
            gI = cm.getInventory(1).getItem(st) 
            st2 = selection;
        }
        if (st2 == 0) {
            if(gI.getBossDamage()  > limit[0]){
                cm.sendOk(" 無法再強化。");
                cm.dispose(); 
                return;
            }
            gE = gI.getBossDamage(); 
        } else if (st2 == 1) {
            if(gI.getIgnorePDR()  > limit[1]){
                cm.sendOk(" 無法再強化。");
                cm.dispose(); 
                return;
            }
            gE = gI.getIgnorePDR(); 
        } else if (st2 == 2) {
            if(gI.getTotalDamage()  > limit[2]){
                cm.sendOk(" 無法再強化。");
                cm.dispose(); 
                return;
            }
            gE = gI.getTotalDamage(); 
        } else {
            if(gI.getAllStat()  > limit[3]){
                cm.sendOk(" 無法再強化。");
                cm.dispose(); 
                return;
            }
            gE = gI.getAllStat(); 
        }
        if (!isReady) {
            hw = parseInt(gE) + hm;
            isReady = true;
        }
        對話 = "#fs15##b選擇的裝備 :#k#n #i" + cm.getInventory(1).getItem(st).getItemId()  + "#\r\n\r\n";
        對話 += "#fs15##b特殊附加選項 :#k#n " + enhance[st2] + "\r\n";
        對話 += "#fs15##b請輸入想要的增加數埴。\r\n#r(1% 需要 #i"+itemid+"# "+qty[st2]+"個)#k\r\n";
 
        對話 += "#fs15#數埴變化 : #b" + gE + "%#k → #r" + hw + "% #fc0xFF0BCACC#(+" + hm + "%)#b#n\r\n\r\n"
        if (hm >= 10) {
            對話 += "#L1# -10%#l "
        }
        if (hm >= 1) {
            對話 += "#L2# -1%#l "
        }
        if (hw + 1 <= limit[st2]) {
            對話 += "#L3# +1%#l ";
        }
        if (hw + 10 <= limit[st2]) {
            對話 += "#L4# +10%#l ";
        }
        對話 += "\r\n"
        對話 += "#L10# #fs15##r設定完成。"
        cm.sendSimpleS(對話,0x86);
    } else if (St == 4) {
        if (cm.itemQuantity(itemid)  >= qty[st2] * hm) {
            cm.gainItem(itemid,  -qty[st2] * hm);
          if (st2 == 0) {
                gI.setBossDamage(hw); 
            } else if (st2 == 1) {
                gI.setIgnorePDR(hw); 
            } else if (st2 == 2) {
                gI.setTotalDamage(hw); 
            } else {
                gI.setAllStat(hw); 
            }
           
            cm.sendOk("#fs15# 能力附加完成。");
            cm.getPlayer().forceReAddItem(gI,  Packages.client.inventory.MapleInventoryType.EQUIP); 
            cm.dispose(); 
        } else {
            cm.sendOk("#fs15# 材料不足。");
            cm.dispose(); 
        }
    }
}
 
 
function isWeapon(itemid) {
    if (Math.floor(itemid  / 10000) >= 121 && Math.floor(itemid  / 10000) <= 158) {
        return true;
    } else {
        return false;
    }
}