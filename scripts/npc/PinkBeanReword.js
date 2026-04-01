


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    블랙 에 의해 만들어 졌습니다.

    엔피시아이디 : 9110007

    엔피시 이름 : 로보

    엔피시가 있는 맵 : 몬스터파크 : 몬스터파크 (951000000)

    엔피시 설명 : 라면 요리사


*/

var status = -1;
var sel = 0, base = 200;
var str, ab;
var Key = "PinkBeanReward";
importPackage(Packages.constants);

function start() {
    status = -1;
    action(1, 0, 0);
}
var a = 0;
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
        if (cm.getClient().getKeyValue(Key) == null) {
            cm.getClient().setKeyValue(Key, "00000");
        }
        str = cm.getClient().getKeyValue(Key);
        ab = str.split("");
        말 = "#fs11#이야~! 대단한걸 벌써 #b#e육성 완료 보상#k#n을 받을수 있는거야~?!\r\n#r#e대신 주의해! 메이플ID당 1번만 받을 수 있어!#n#k\r\n\r\n#b"
        말 += "#L0#200레벨 보상 받기\r\n"
        말 += "#L1#210레벨 보상 받기\r\n"
        말 += "#L2#220레벨 보상 받기\r\n"
        말 += "#L3#230레벨 보상 받기\r\n"
        말 += "#L4#240레벨 보상 받기\r\n"
        말 += "#L5#250레벨 보상 받기\r\n"
        cm.sendSimpleS(말, 0x04, 9062276);
    } else if (status == 1) {
        sel = selection;
        말 = "";
        if (sel == 0) { //200
            말 += "아래의 보상을 정말로 받을거야?\r\n\r\n"
            말 += "#i2435719# #b#z2435719##k #r10개#k\r\n"
            말 += "#i2048759# #b#z2048759##k #r50개#k\r\n"
            말 += "#i5200002# #r30,000,000 메소#k\r\n"
        } else if (sel == 1) { //210
            말 += "아래의 보상을 정말로 받을거야?\r\n\r\n"
            말 += "#i5062010# #b#z5062010##k #r300개#k\r\n"
            말 += "#i5062500# #b#z5062500##k #r300개#k\r\n"
            말 += "#i2048757# #b#z2048757##k #r50개#k\r\n"
            말 += "#i5200002# #r100,000,000 메소#k\r\n"
        } else if (sel == 2) { //220
            말 += "아래의 보상을 정말로 받을거야?\r\n\r\n"
            말 += "#i5068300# #b#z5068300##k #r10개#k\r\n"
            말 += "#i5069100# #b#z5069100##k #r2개#k\r\n"
            말 += "#i5200002# #r150,000,000 메소#k\r\n"
        } else if (sel == 3) { //230
            말 = "아래의 보상을 정말로 받을거야?\r\n\r\n"
            말 += "#i5068300# #b#z5068300##k #r5개#k\r\n"
            말 += "#i5069100# #b#z5069100##k #r1개#k\r\n"
            말 += "#i2049704# #b#z2049704##k #r5개#k\r\n"
            말 += "#i2049372# #b#z2049372##k #r1개#k\r\n"
            말 += "#i2049360# #b#z2049360##k #r3개#k\r\n"
            말 += "#i5200002# #r1,000,000,000 메소#k\r\n"
        } else if (sel == 4) { //240
            말 += "아래의 보상을 정말로 받을거야?\r\n\r\n"
            말 += "#i5068301# #b#z5068301##k #r1개#k\r\n"
            말 += "#i5062005# #b#z5062005##k #r30개#k\r\n"
            말 += "#i2049376# #b#z2049376##k #r1개#k\r\n"
            말 += "#i2049360# #b#z2049360##k #r5개#k\r\n"
            말 += "#i5200002# #r1,500,000,000 메소#k\r\n"
        } else if (sel == 5) { //250
            말 += "아래의 보상을 정말로 받을거야?\r\n\r\n"
            말 += "#i5062006# #b#z5062006##k #r30개#k\r\n"
            말 += "#i2049376# #b#z2049376##k #r1개#k\r\n"
            말 += "#i5060048# #b#z5060048##k #r20개#k\r\n"
            말 += "#i2048768# #b#z2048768##k #r100개#k\r\n"
            말 += "#i2049360# #b#z2049360##k #r7개#k\r\n"
            말 += "#i5068301# #b#z5068301##k #r5개#k\r\n"
            말 += "#i5200002# #r3,000,000,000 메소#k\r\n"
        }
        cm.sendYesNoS(말, 0x04, 9062276);
    } else if (status == 2) {
        if (ab[sel] == 1) {
            cm.sendOkS("응? 이미 #r#e보상#n#k을 받아갔잖아! 두번 받을수는 없어!", 4, 9062276);
            cm.dispose();
            return;
        }
        if (!GameConstants.isPinkBean(cm.getPlayer().getJob())) {
            cm.sendOkS("보상은 #b#e핑크빈#n#k 캐릭터로만 받아갈 수 있어!\r\n\r\n#r※보상 아이템은 #e메이플 보관함#n에 지급됩니다.#k", 4, 9062276);
            cm.dispose();
            return;
        }
        if ((base + (sel * 10)) > cm.getPlayer().getLevel()) {
            cm.sendOkS("보상을 받아가기 위한 #r#e레벨#n#k이 부족해!\r\n\r\n#r※ 해당 보상을 받으려면 #e"+(base + (sel * 10))+" 레벨#n이 되어야 합니다.", 4, 9062276);
            cm.dispose();
            return;
        }
        ab[sel] = 1;
        var fi = "";
        for (var a = 0; a < ab.length; a++) {
            fi += ab[a];
        }
        cm.getClient().setKeyValue(Key, fi);
        cm.sendOkS("보상을 #r#e메이플 보관함#n#k에 지급해줬어, 반드시 원하는 캐릭터에 수령하길 바래!", 4, 9062276);
        if (sel == 0) {
            cm.getPlayer().gainCabinetItem(2435719, 10);
            cm.getPlayer().gainCabinetItem(2048759, 50);
            cm.gainMeso(30000000);
            cm.dispose();
            return;
        } else if (sel == 1) {
            cm.getPlayer().gainCabinetItem(5062010, 300);
            cm.getPlayer().gainCabinetItem(5062500, 300);
            cm.getPlayer().gainCabinetItem(2048757, 50);
            cm.gainMeso(100000000);
            cm.dispose();
            return;
        } else if (sel == 2) {
            cm.getPlayer().gainCabinetItem(5068300, 10);
            cm.getPlayer().gainCabinetItem(5069100, 2);
            cm.gainMeso(150000000);
            cm.dispose();
            return;
        } else if (sel == 3) {
            cm.getPlayer().gainCabinetItem(5068300, 5);
            cm.getPlayer().gainCabinetItem(5069100, 1);
            cm.getPlayer().gainCabinetItem(2049704, 5);
            cm.getPlayer().gainCabinetItem(2049372, 1);
            cm.getPlayer().gainCabinetItem(2049360, 3);
            cm.gainMeso(1000000000);
            cm.dispose();
            return;
        } else if (sel == 4) {
            cm.getPlayer().gainCabinetItem(5068301, 1);
            cm.getPlayer().gainCabinetItem(5062005, 30);
            cm.getPlayer().gainCabinetItem(2049376, 1);
            cm.getPlayer().gainCabinetItem(2049360, 5);
            cm.gainMeso(1500000000);
            cm.dispose();
            return;
        } else if (sel == 5) {
            cm.getPlayer().gainCabinetItem(5062006, 30);
            cm.getPlayer().gainCabinetItem(2049376, 1);
            cm.getPlayer().gainCabinetItem(5060048, 20);
            cm.getPlayer().gainCabinetItem(2048768, 100);
            cm.getPlayer().gainCabinetItem(2049360, 7);
            cm.getPlayer().gainCabinetItem(5068301, 5);
            cm.gainMeso(2000000000);
            cm.gainMeso(1000000000);
            cm.dispose();
            return;
        }
        
    }
}