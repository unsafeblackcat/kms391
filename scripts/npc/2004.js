/* 
 
    * 此脚本由短篇NPC自动生成脚本创建 
    * (Guardian Project Development Source Script)
    由White制作 
    NPC ID : 2004 
    NPC名称 : 托德 
    NPC所在地图 : The Black : Night Festival (100000000)
    NPC描述 : MISSINGNO 
 
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
var status = -1;
 
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
        if (cm.getClient().getQuestStatus(50003)  == 1 && cm.getClient().getCustomKeyValue(50003,  "1") != 1) {
            cm.getClient().setCustomKeyValue(50003,  "1", "1");
        }
        cm.Entertuto(true);
    } else if (status == 1) {
        cm.sendScreenText("                   #fs30##fc0xFF4641D9#黑色#k 晋升制度说明", false);
    } else if (status == 2) {
        cm.sendScreenText("#fs22##fc0xFFF15F5F#          第一步#k，请从黑色舒皮格尔处获得称号", false);
    } else if (status == 3) {
        cm.sendScreenText("#fs22##fc0xFFF15F5F#          第二步#k，达成晋升所需的等级条件", false);
    } else if (status == 4) {
        cm.sendScreenText("#fs22##fc0xFFF15F5F#          第三步#k，请按照晋升条件收集以下材料", false);
    } else if (status == 5) {
        cm.sendScreenText(" 　　     #fs15##i4310010# #fc0xFF4374D9##z4310010##k　#i4310012# #fc0xFF4374D9##z4310012##k　#i5200002# #fc0xFF4374D9#晋升所需枫币#k\r\n", false);
    } else if (status == 6) {
        cm.sendScreenText("#fs22#              满足以上所有条件即可进行晋升", true);
    } else if (status == 7) {
        cm.sendScreenText("#fs30##fc0xFF4641D9# 什么是晋升?#k\r\n", false);
    } else if (status == 8) {
        cm.sendScreenText("#fs22# 黑色庆典称号共分为 #fc0xFFCC723D#3种#k", false);
    } else if (status == 9) {
        cm.sendScreenText("#fc0xFFF15F5F# 第一种#k，黑色庆典新手", false);
    } else if (status == 10) {
        cm.sendScreenText("#fc0xFFF15F5F# 第二种#k，黑色庆典适应者", false);
    } else if (status == 11) {
        cm.sendScreenText("#fc0xFFF15F5F# 第三种#k，黑色庆典大师", false);
    } else if (status == 12) {
        cm.sendScreenText("#fc0xFFCC3D3D# 每次#k只能晋升一个阶段，晋升后可获得比原先 #fc0xFF6B66FF#属性#k 更高的称号", true);
    } else if (status == 13) {
        cm.Endtuto()
        cm.dispose(); 
    }
}