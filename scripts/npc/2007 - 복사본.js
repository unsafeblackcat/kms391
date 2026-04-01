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
        cm.Entertuto(true);
    } else if (status == 1) {
        cm.sendScreenText("#fs25##fc0xFFF2CB61# 黎明系統#k說明", false);
    } else if (status == 2) {
        cm.sendScreenText("#fs17##i1182000#  #z1182000#　　　　　　　　　　　　　　　　　　　　　　　　#i1182001# #z1182001# (1次元)　　　　　　　　　　　　　　　　　　　　　#i1182002# #z1182002# (2次元)　　　　　　　　　　　　　　　　　　　　#i1182003# #z1182003# (3次元)　　　　　　　　　　　　　　　　　　　　#i1182004# #z1182004# (4次元)　　　　　　　　　　　　　　　　　　　　　#i1182005# #z1182005# (5次元)", false);
    } else if (status == 3) {
        cm.sendScreenText("#fc0xFFF2CB61# 黎明道具#k共分為#fc0xFF6B66FF#6種#k", true);
    } else if (status == 4) {
        cm.sendScreenText("#fs25##i1182000#  #fc0xFFF2CB61##z1182000#是什麼？#k", false);
    } else if (status == 5) {
        cm.sendScreenText("#fs17##fc0xFFFFBB00# 黎明選項#k擁有特殊屬性", false);
    } else if (status == 6) {
        cm.sendScreenText("#fc0xFF6B66FF# 次元解放#k，是單純用於升級的#fc0xFFF29661#基礎裝備道具#k", false);
    } else if (status == 7) {
        cm.sendScreenText(" 與其他徽章不同，可使用#fc0xFFFFE400#星力#k和#fc0xFF6799FF#專用卷軸#k", false);
    } else if (status == 8) {
        cm.sendScreenText(" 之後可透過#fc0xFF6B66FF#次元解放#k系統開啟特殊能力", true);
    } else if (status == 9) {
        cm.sendScreenText("#fs25##fc0xFFFFBB00# 黎明選項#k有#r2種#k開啟方式", false);
    } else if (status == 10) {
        cm.sendScreenText("#fs22##fc0xFFF15F5F# 第一種#k，使用#i4310006# #z4310006#升級　　　　　#fs15##z4310006#可透過#fc0xFFF29661#每日任務、組隊任務、BOSS#k獲得", false);
    } else if (status == 11) {
        cm.sendScreenText("#fs22##fc0xFFF15F5F# 第二種#k，使用#i4310004# 次元之石升級　　　　　#fs15#次元之石可用#d石頭碎片#k製作", true);
    } else if (status == 12) {
        cm.sendScreenText("#fs25##fc0xFF6B66FF# 黎明泉水#k與#fc0xFFA566FF#次元之石#k的差異？", false);
    } else if (status == 13) {
        cm.sendScreenText("#fs22##fc0xFF6B66FF# 黎明泉水#k是#fc0xFFF15F5F#綁定道具#k", false);
    } else if (status == 14) {
        cm.sendScreenText(" 每次升級成功率會下降", false);
    } else if (status == 15) {
        cm.sendScreenText("#fc0xFFA566FF# 次元之石#k可交易", false);
    } else if (status == 16) {
        cm.sendScreenText("#r100%#k 機率升級成功", true);
    } else if (status == 17) {
        cm.Endtuto()
        cm.dispose(); 
    }
}