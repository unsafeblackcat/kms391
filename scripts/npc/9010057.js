/*
?젣?옉?옄 : qudtlstorl79@nate.com
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

집 = "#fUI/Basic.img/theblackcoin/10#";
코인 = "#fUI/Basic.img/theblackcoin/26#";
모루 = "#fUI/Basic.img/theblackcoin/30#";
훈장 = "#fUI/Basic.img/theblackcoin/31#";
의자 = "#fUI/Basic.img/theblackcoin/22#";
뎀스 = "#fUI/Basic.img/theblackcoin/23#";
라이딩 = "#fUI/Basic.img/theblackcoin/32#";
강화 = "#fUI/Basic.img/theblackcoin/34#";
커플 = "#fUI/Basic.img/theblackcoin/35#";
결혼 = "#fUI/Basic.img/theblackcoin/36#";
퀘스트 = "#fUI/Basic.img/theblackcoin/37#";
무릉 = "#fUI/Basic.img/theblackcoin/39#";
Dream스킬 = "#fUI/Basic.img/theblackcoin/40#";
검정 = "#fc0xFF191919#"
노랑 = "#fc0xFFE0B94F#"
보라 = "#fc0xFFB677FF#"
보라색 = "#fc0xFF8041D9#"
블루 = "#fc0xFF4641D9#"
회색 = "#fc0xFF4C4C4C#"
연파랑 = "#fc0xFF6799FF#"
동글보라 = "#fMap/MapHelper.img/weather/starPlanet/8#";
네오젬 = "#fUI/UIWindow4.img/pointShop/100712/iconShop#";
룰렛 = "#fUI/UIWindow4.img/pointShop/17015/iconShop#";
포켓 = "#fUI/Basic.img/theblackcoin/17#";
뛰어라 = "#fUI/Basic.img/theblackcoin/33#";
펫 = "#fUI/CashShop.img/CashItem_label/9#";
마라벨 = "#fUI/CashShop.img/CashItem_label/3#";
컨텐츠 = "#fUI/Basic.img/theblack/4#";

var sssss = 0;
var suc = 0;
var sel = 0;
var sell = 0;
var selching = 0;
var sel2 = 0;
var status = -1;
var succ = false;
var minusitemid = 0;
var rewarditemid = 0;
var etc = 0;
var etc1 = 0;
var sale = 0;
var sale1 = 0;
var ssssitem = 0;
var ssssitemc = 0;
var ssssitem2 = 0;
var ssssitemc2 = 0;
var ssssmeso = 0;
var 말;
var citem = 1142922;
var allstat = 77;
var atk = 77;
var nitem = [
    [1182193, 1],
    [1182194, 1],
    [1182195, 1],
    [1182196, 1],
    [1182197, 1],
    [1182198, 1],
    [1182199, 1],
];
var nmeso = 10000000000;

function start() {
    action(1, 0, 0);
}

/*
            cm.dispose();
            InterServerHandler.EnterCS(cm.getPlayer().getClient(),cm.getPlayer(), false); 罹먯떆?꺏
*/

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        if (status == 2 && sel == 4) {//승급 거부
            cm.sendOkS("#fs15#그래! 마음이 바뀌면 다시 돌아오게나!", 0x04, 9401232);
        }
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    
    말 = "#fs15#"
    if (status == 0) {
        if(cm.getPlayer().isGM()) {
            
    /*for(i = 0; i < nitem.length; i++) {
        cm.gainItem(nitem[i][0], nitem[i][1]);
    }*/
        } 
        말 += "#fs15##i" + citem + "# #b#z" + citem + "##k를 원하는 거야?"
        말 += "\r\n제작을 하려면 아래의 재료를 모아와줘!\r\n"
        말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
        for(i = 0; i < nitem.length; i++) {
            말 += "#i" + nitem[i][0] + "#  #b#z" + nitem[i][0] + "##k #r" + nitem[i][1] + "개#k\r\n"
        }
        말 += nmeso > 0 ? "#i5200002#  #r" + djr(nmeso) + "#k #b메소#k\r\n\r\n" : "\r\n"
        말 += "정말로 제작하시겠습니까?"
        cm.sendYesNo(말);//, 0x04, 9401232);
    } else if (status == 1) {
        if (cm.getPlayer().getMeso() < nmeso || !cm.haveItem(1182193, 1) || !cm.haveItem(1182194, 1) || !cm.haveItem(1182195, 1) || !cm.haveItem(1182196, 1) || !cm.haveItem(1182197, 1) || !cm.haveItem(1182198, 1) || !cm.haveItem(1182199, 1)) {   //재료체크
            말 += "#fs15##b#z1142922##k를 제작하기 위한 재료 아이템이 부족한거 같은데...\r\n\r\n아래와 같은 아이템을 확인해봐!!\r\n\r\n";
            needitem();
            cm.sendOk(말);
            cm.dispose();
            return;
        } else {
            if(nmeso > 2000000000) {
                while (nmeso > 2000000000){
                cm.gainMeso(-2000000000);
                nmeso -= 2000000000;
                }
                cm.gainMeso(-nmeso);
            } else {
                cm.gainMeso(-nmeso);
            }
            for(i = 0; i < nitem.length; i++) {
                cm.getPlayer().removeItem(nitem[i][0], -nitem[i][1]);
            }
            citem = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(citem);
               citem.setStr(allstat);
               citem.setDex(allstat);
               citem.setInt(allstat);
               citem.setLuk(allstat);
               citem.setWatk(atk);
               citem.setMatk(atk);
               citem.setIgnorePDR(40);
            Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), citem, false);
            말 += "#fs15#축하합니다! 칠요 몬스터파커 훈장을 제작하셨습니다.\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n"
            말 += "#i1142922##b#z1142922##k"
            cm.sendOk(말);
            cm.dispose();
        }
    }
}

function djr(i) {
    return i/100000000 + "억";
}

function checkitem() {
    for(i = 0; i < nitem.length; i++) {
        if(!cm.haveItem(nitem[i][0], nitem[i][1])) {
            return true;
        }
    }
    return false;
}

function needitem() {
    for(i = 0; i < nitem.length; i++) {
        if(!cm.haveItem(nitem[i][0], nitem[i][1])) {
            말 += "#i" + nitem[i][0] + "#  #z" + nitem[i][0] + "# " + (nitem[i][1] - cm.itemQuantity(nitem[i][0])) + "개\r\n";
        }
    }
    말 += cm.getPlayer().getMeso() < nmeso ? (nmeso - cm.getPlayer().getMeso()) + "메소" : "";
}
