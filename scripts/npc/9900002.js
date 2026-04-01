/*
 
    * 此脚本由NPC自动生成脚本工具创建 
 
    * (Guardian Project Development Source Script)
 
    由 Black 制作 
 
    NPC ID : 9900002
 
    NPC 名称 : 信用合作社
 
    NPC所在地图 : 维多利亚路 : 艾琳森林 (180000000)
 
    NPC说明 : MISSINGNO 
 
 
*/
importPackage(java.lang); 
importPackage(Packages.constants); 
importPackage(Packages.handling.channel.handler); 
importPackage(Packages.tools.packet); 
importPackage(Packages.handling.world); 
importPackage(java.lang); 
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
var 克隆装备 = [[1032800], [1022700], [1012950], [1032801], [1022701], [1012951], [1032802], [1022702], [1012952], [1032803], [1022703], [1012953]]
var status = -1;
var sel = 0;
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
        对话 = "#fs15#欢迎！你知道#e克隆装备#n吗？呵呵。\r\n"
        对话 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
        对话 += "#L0##fc0xFF8041D9#克隆装备 - 史诗等级#k 物品制作\r\n";
        对话 += "#L1##fc0xFFEDA900#克隆装备 - 独特等级#k 物品制作\r\n";
        对话 += "#L2##fc0xFF47C83E#克隆装备 - 传说等级#k 物品制作\r\n\r\n";
        对话 += "#L10##b查看克隆装备种类\r\n";
        对话 += "#L99##r结束对话\r\n";
        cm.sendSimpleS( 对话, 0x04, 2192030);
    } else if (status == 1) {
        sel = selection;
        if (selection == 0) {
            对话 = "#fs15#请选择要制作的物品。\r\n";
            对话 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            对话 += "#L0##i1032801# #fc0xFF8041D9##z1032801# 制作\r\n";
            对话 += "#L1##i1022701# #fc0xFF8041D9##z1022701# 制作\r\n";
            对话 += "#L2##i1012951# #fc0xFF8041D9##z1012951# 制作\r\n";
            cm.sendSimpleS( 对话, 0x04, 2192030);
        } else if (selection == 1) {
            对话 = "#fs15#请选择要制作的物品。\r\n";
            对话 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            对话 += "#L10##i1032802# #fc0xFFEDA900##z1032802# 制作\r\n";
            对话 += "#L11##i1022702# #fc0xFFEDA900##z1022702# 制作\r\n";
            对话 += "#L12##i1012952# #fc0xFFEDA900##z1012952# 制作\r\n";
            cm.sendSimpleS( 对话, 0x04, 2192030);
        } else if (selection == 2) {
            对话 = "#fs15#请选择要制作的物品。\r\n";
            对话 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            对话 += "#L20##i1032803# #fc0xFF47C83E##z1032803# 制作\r\n";
            对话 += "#L21##i1022703# #fc0xFF47C83E##z1022703# 制作\r\n";
            对话 += "#L22##i1012953# #fc0xFF47C83E##z1012953# 制作\r\n";
            cm.sendSimpleS( 对话, 0x04, 2192030);
        } else if (selection == 10) {
            对话 = "#fs15#克隆装备列表：\r\n\r\n"
            for (var a = 0; a < 克隆装备.length; a++) {
                对话 += "#i" + 克隆装备[a][0] + "# #b#z" + 克隆装备[a][0] + "##k\r\n"
                if (a == 11) break;
            }
            cm.sendOkS( 对话, 0x04, 2192030);
            cm.dispose(); 
        } else if (seletion == 99) {
            cm.dispose(); 
        }
    } else if (status == 2) {
        if (selection == 0) {
            对话 = "#fs15##i1032801# #b#z1032801##k 要制作这个物品吗？"
            对话 += "\r\n制作需要以下材料：\r\n"
            对话 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            对话 += "#i1032800# #b#z1032800##k #r1个#k\r\n"
            对话 += "#i1022700# #b#z1022700##k #r1个#k\r\n"
            对话 += "#i1012950# #b#z1012950##k #r1个#k\r\n"
            对话 += "#i4310022# #b#z4310022##k #r25个#k\r\n\r\n"
            对话 += "制作成功率为 #b75%#k。确定要制作吗？"
            cm.sendYesNoS( 对话, 0x04, 2192030);
        } else if (selection == 1) {
            对话 = "#fs15##i1022701# #b#z1022701##k 要制作这个物品吗？"
            对话 += "\r\n制作需要以下材料：\r\n"
            对话 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            对话 += "#i1032800# #b#z1032800##k #r1个#k\r\n"
            对话 += "#i1022700# #b#z1022700##k #r1个#k\r\n"
            对话 += "#i1012950# #b#z1012950##k #r1个#k\r\n"
            对话 += "#i4310022# #b#z4310022##k #r25个#k\r\n\r\n"
            对话 += "制作成功率为 #b75%#k。确定要制作吗？"
            cm.sendYesNoS( 对话, 0x04, 2192030);
        } else if (selection == 2) {
            对话 = "#fs15##i1012951# #b#z1012951##k 要制作这个物品吗？"
            对话 += "\r\n制作需要以下材料：\r\n"
            对话 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            对话 += "#i1032800# #b#z1032800##k #r1个#k\r\n"
            对话 += "#i1022700# #b#z1022700##k #r1个#k\r\n"
            对话 += "#i1012950# #b#z1012950##k #r1个#k\r\n"
            对话 += "#i4310022# #b#z4310022##k #r25个#k\r\n\r\n"
            对话 += "制作成功率为 #b75%#k。确定要制作吗？"
            cm.sendYesNoS( 对话, 0x04, 2192030);
        }
    } else if (status == 3) {
        item = cm.getInventory(6) 
        if (sel == 0) {
            if (!cm.haveItem(1032800,  1) || !cm.haveItem(1022700,  1) || !cm.haveItem(1012950,  1) || !cm.haveItem(4310022,  25)) {   //材料检查
                cm.sendOkS("#fs15##b#z1032801##k  制作所需的材料不足。", 0x04, 2192030);
                cm.dispose(); 
                return;
            }
            if (Math.floor(Math.random()  * 100) <= 75) {// 成功时 
                var a = 0, b = 0, c = 0;
                for (w = 0; w < inz.getSlotLimit();  w++) {
                    if (inz.getItem(w)  == null) {
                        continue;
                    }
                    if (1032800 == inz.getItem(w).getItemId()  && a < qty[0]) {
                        MapleInventoryManipulator.removeFromSlot(cm.getPlayer().getClient(),  GameConstants.getInventoryType(inz.getItem(w).getItemId()),  inz.getItem(w).getPosition(),  inz.getItem(w).getQuantity(),  false);
                        a++;
                        if (a >= 1) {
                            break;
                        }
                    }
                }
                for (w = 0; w < inz.getSlotLimit();  w++) {
                    if (inz.getItem(w)  == null) {
                        continue;
                    }
                    if (1022700 == inz.getItem(w).getItemId()  && b < qty[1]) {
                        MapleInventoryManipulator.removeFromSlot(cm.getPlayer().getClient(),  GameConstants.getInventoryType(inz.getItem(w).getItemId()),  inz.getItem(w).getPosition(),  inz.getItem(w).getQuantity(),  false);
                        b++;
                        if (b >= 1) {
                            break;
                        }
                    }
                }
                for (w = 0; w < inz.getSlotLimit();  w++) {
                    if (inz.getItem(w)  == null) {
                        continue;
                    }
                    if (1012950 == inz.getItem(w).getItemId()  && c < qty[2]) {
                        MapleInventoryManipulator.removeFromSlot(cm.getPlayer().getClient(),  GameConstants.getInventoryType(inz.getItem(w).getItemId()),  inz.getItem(w).getPosition(),  inz.getItem(w).getQuantity(),  false);
                        c++;
                        if (c >= 1) {
                            break;
                        }
                    }
                }
                cm.gainItem(4310022,  -25);
                cm.gainItem(1032801,  1);
                对话 = "#fs15##b#z1032801##k 制作成功！\r\n\r\n"
                对话 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n" 
                对话 += "#i1032801##b#z1032801##k"
                cm.sendOkS( 对话, 0x04, 2192030);
                cm.dispose(); 
            } else { // 失败时 
                cm.getInventory(6).removeItem(1022700,  -1, true);
                cm.getInventory(6).removeItem(1012950,  -1, true);
                cm.getInventory(6).removeItem(1032800,  -1, true);
                cm.gainItem(4310022,  -25);
                cm.sendOkS("#fs15##r 制作失败...下次一定会成功的！", 0x04, 2192030);
                cm.dispose(); 
                return;
            }
        }
    }
} 