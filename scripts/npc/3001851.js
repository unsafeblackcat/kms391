importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(Packages.server);
importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);

var status = -1;

var enter = "\r\n";

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (cm.getPlayer().getKeyValue(cm.getDate(), "Check_Day") == -1) {
        cm.getPlayer().setKeyValue(cm.getDate(), "Check_Day", "0");
    }

    var 후원등급 = cm.getPlayer().getHgrade();
    var 홍보등급 = cm.getPlayer().getPgrade();
    if (status == 0) {
        if (cm.getPlayer().getKeyValue(cm.getDate(), "Check_Day") > 0) {
            cm.sendOk("이미 #eDAY GIFT#n를 받으셨습니다. #e매일 자정#n에 #r#e초기화#n#k 되니, 그때 다시 말 걸어주세요.");
            cm.dispose();
            return;
        }
        talk = "#e<#bPRAY#k DAY GIFT>#n" + enter + enter;
        talk += "현재 #e#h0##n님의" + enter;
        talk += "후원등급은 #b" + cm.getPlayer().getHgrades() + " #e단계#n#k || 홍보등급은 #b" + cm.getPlayer().getPgrades() + "  #e단계#n#k 입니다." + enter;
        if (후원등급 > 0 || 홍보등급 > 0) {
            talk += "#L1# #eDAY GITF#n를 받고 싶습니다." + enter;
        }
        talk += "#L0# #eDAY GITF#n에 대한 설명듣기" + enter;
        cm.sendSimple(talk);

    } else if (status == 1) {
        if (selection == 0) {
            cm.sendOk("후원, 홍보 유저들을 위한 시스템으로, #r#e등급#n#k에 따라 #e다양한 아이템#n을 제공하는 데일리 시스템이야!");
            cm.dispose();
        } else if (selection == 1) {
            if (후원등급 > 0 || 홍보등급 > 0) {
                cm.getPlayer().setKeyValue(cm.getDate(), "Check_Day", "1");
                cm.getPlayer().setKeyValue(98189, "Check_Day", 1 + "");
                cm.gainItem(2439541, 1);
                cm.gainItem(2439542, 1);
                cm.dispose();
                cm.sendOk("성공적으로 #bDAY GIFT#k 보상을 받았습니다.");
            } else {
                cm.dispose();
                cm.sendOk("현재 후원 또는 홍보 등급이 존재하지 않습니다.");
            }
        }
    }
}