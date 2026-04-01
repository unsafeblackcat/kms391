/*
제작자 : qudtlstorl79@nate.com
*/

importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);

파랑 = "#fc0xFF0054FF#";
연파 = "#fc0xFF6B66FF#";
연보 = "#fc0xFF8041D9#";
보라 = "#fc0xFF5F00FF#";
노랑 = "#fc0xFFEDD200#";
검정 = "#fc0xFF191919#";
화이트 = "#fc0xFFFFFFFF#";
틀 = "#fUI/Basic.img/actMark/0#";


icon = [
    ["#fUI/UIWindow4.img/pointShop/100659/iconShop#", 9062352],
    ["#fUI/UIWindow4.img/pointShop/100658/iconShop#", 9062356],
    ["#fUI/UIWindow4.img/pointShop/100657/iconShop#", 9062353],
    ["#fItem/Etc/0431.img/04310156/info/iconShop#", 2155009],
    ["#fItem/Etc/0431.img/04310199/info/iconShop#", 1540893],
    ["#fItem/Etc/0431.img/04310218/info/iconShop#", 3003105],
    ["#fItem/Etc/0431.img/04310249/info/iconShop#", 3003105],
    ["#fUI/UIWindow4.img/pointShop/100660/iconShop#", 9062356],
    ["#fUI/UIWindow4.img/pointShop/100661/iconShop#", 9062353],
];

var status = -1;

function start() {
    action(1, 0, 0);
}

/*
            cm.dispose();
            InterServerHandler.EnterCS(cm.getPlayer().getClient(),cm.getPlayer(), false); 캐시샵
*/

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var choose = "                    " + 틀 + "#l\r\n";
    choose += "#fc0xFFD5D5D5#──────────────────────────#k\r\n";
        choose += "#L12##fs15##b"+icon[1][0]+"#r[HOT]#k 每天 #r熱時間#k 補償!!#l\r\n";
        choose += "#L11##fs15##b"+icon[0][0]+"[NEW]#k 獲取健太的紙片 #d( 重複 )#k#l\r\n";
        choose += "#L10##fs15##b"+icon[3][0]+"[NEW]#k 使用最終的秘寶抽取#l\r\n";
        choose += "#L100##fc0xFFFF3636##b"+icon[2][0]+"[NEW]#k 六個秘寶 #d( 合 成終極秘寶 )#k#l\r\n\r\n";
    choose += "#fc0xFFD5D5D5#──────────────────────────#k\r\n";
        choose += "#L30##fc0xFFDF4D4D#[聯盟] 8000 獲得達成講勵（每個帳戶一次）#l\r\n";
        choose += "#L20##fc0xFFDF4D4D#[Newby]獲得等級達成講勵（每個帳號一次）#l\r\n";
        choose += "#L21##fc0xFFDF4D4D#[推薦人]推薦人査詢&獲得講勵#l\r\n";
        choose += "#L15##fs15##d"+icon[7][0]+"[Fishing] 移動雪歸島的特別釣魚場#l\r\n";
        choose += "#L16##fs15##d"+icon[7][0]+"[Fishing] 填滿各種各樣的魚圖鑒!#l\r\n\r\n";
    choose += "    ";
        cm.sendOk(choose);

    } else if (status == 1) {
        if (selection == 100) { // 사냥이벤트 아이템교환
            cm.dispose();
            cm.openNpc(9062710);
        } else if (selection == 15) { // 낚시
            cm.dispose();
            cm.warp(993000750);
        } else if (selection == 16) { // 낚시 다이어리
                cm.sendOk("#fs15# #b 釣魚圖鑒即將啟動.");
                cm.dispose();
                return;
        } else if (selection == 10) { // 사냥이벤트 아이템뽑기
            cm.dispose();
            cm.openNpc(9062670);
        } else if (selection == 11) { // 종이조각
            cm.dispose();
            cm.openNpc(9060000);
        } else if (selection == 12) { // 핫타임
            cm.dispose();
            cm.openNpc(9063199);
        } else if (selection == 30) {
            if(cm.getPlayer().getAllUnion() < 10000) {
                cm.sendOk("#fs15#對於聯合等級總和 [ #b10000#k ] 以上的 [ #b角色#k ] 這是一次暫停發放的 #r講勵#k，請在滿足後前來領取");
                cm.dispose();
                return;
            } else if(cm.getClient().getKeyValue("UnionReward10000") > 0) {
        cm.sendOk("#fs15#已獲得活動獎勵.");
        cm.dispose();
        } else if (cm.getClient().getKeyValue("UnionReward10000") == null) {
        cm.getClient().setKeyValue("UnionReward10000", "0");
        cm.dispose();
        } else if (cm.getPlayer().getAllUnion() > 10000) {
        cm.getClient().setKeyValue("UnionReward10000", "1");
        cm.sendOk("#fs15#祝賀您達到 #r10000級#k!\r\n\r\n#b#i5060048# #r10個#和 50億金幣.");
        cm.gainItem(5060048, 10);
        cm.gainMeso(5000000000);
        cm.dispose();
        }
        } else if (selection == 40) { // 검키우기
            cm.dispose();
            cm.openNpc(3005575);
        } else if (selection == 50) { // 후원룰렛
            cm.dispose();
            cm.openNpc(9010049);    
        } else if (selection == 4) { // 어빌리티
            if (cm.getPlayer().getInnerSkills().size() > 2) {
                cm.sendOk("#fs15#" + 검정 + "Mobility已經開放了!#k", 9401232);
                cm.dispose();
                return;
            }
            cm.forceCompleteQuest(12394);
            cm.forceCompleteQuest(12395);
            cm.forceCompleteQuest(12396);
            cm.setInnerStats(1);
            cm.setInnerStats(2);
            cm.setInnerStats(3);
            cm.fakeRelog();
            cm.updateChar();
            cm.sendOk("#fs15#" + 검정 + "我為你開放了Mobility。!", 9401232);
            cm.dispose();
        } else if (selection == 6) { // 펫 교환
            말 = "#fs15#" + 검정 + "請選擇想要交換的標籤的寵物.\r\n"
            말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            말 += "#fs15##L0#" + 펫 + "#fc0xFF6B66FF# 我想兌換#k" + 검정 + "標籤寵物.#l\r\n"
            말 += "#fs15##L1#" + 펫1 + "#fc0xFFA566FF# 我想兌換#k" + 검정 + "標籤寵物.#l\r\n"
            cm.sendSimpleS(말, 0x04, 9401232)
        } else if (selection == 7) { //V 코어 매트릭스
            cm.dispose();
            cm.openNpc(1540945);
        } else if (selection == 8) { //자유 전직
            cm.dispose();
            cm.sendUI(164);
        } else if (selection == 21) { //추천인
            cm.dispose();
            cm.openNpc(3001931);
        } else if (selection == 23) { //아라크노
            cm.dispose();
            cm.openNpc(3003536);
        } else if (selection == 10) { //자유전직
            cm.dispose();
            cm.openNpc(1540942);
        } else if (selection == 11) { //검은마법사 재료 교환
            cm.dispose();
            cm.openNpc(3003811);
        } else if (selection == 12) {
            cm.dispose();
            cm.openNpc(9062518);
        } else if (selection == 13) {
            cm.dispose();
           cm.openNpcCustom(cm.getClient(), 9010044, "닉네임변경");
        } else if (selection == 14) {
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 9062294, "원클큐브");
        } else if (selection == 15) {
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 9062294, "원클환불");
        } else if (selection == 16) {
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 9062294, "CashStroage");
        } else if (selection == 17) {
            cm.dispose();
            cm.openNpc(9062543);
        } else if (selection == 20) {
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 1052206, "LevelReward");
//       cm.openNpcCustom(cm.getClient(), 1052206, "LevelReward");
         } else if (selection == 22) {
            cm.dispose();
         cm.openNpcCustom(cm.getClient(), 1052206, "ServerBackRE");
        }  else if (selection == 18) {
        cm.dispose();
        cm.openNpcCustom(cm.getClient(), 1052206, "Stealskill");
         } else if (selection == 6974) {
                    cm.dispose();
                    cm.warp(101084400, 0);
         }
    } else if (status == 2) {
        if (selection == 0) {
            cm.dispose();
            cm.openNpc(2074149);
        } else if (selection == 1) {
            cm.dispose();
            cm.openNpc(2074150);
        }
    }
}