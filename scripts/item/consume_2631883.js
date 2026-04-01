importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(Packages.server);
importPackage(Packages.server.quest);
importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);

var status = -1;

var icon;
var k, key;
var choice = -1;

//난이도, 보스 이름, 보스 키, 초기화권코드, 초기화권 소모 갯수 아이템코드 맨뒤 보스 코드
var bosslist = [ 
    //["Normal", "[Normal] 자쿰", "Normal_Zakum", 2430030, 1, 1, 4001083], //ok
    ["Normal", "[Chaos] 자쿰", "Chaos_Zakum", 2430030, 1, 1, 4001083],
    ["Normal", "[Hard] 힐라", "Hard_Hillah", 2430030, 1, 3, 4033304],
    ["Normal", "[카오스] 핑크빈", "Chaos_Pinkbean", 2430030, 1, 11, 4032002], //OK
    ["Normal", "[Normal] 시그너스", "Normal_Cygnus", 2430030, 1, 12, 4033670], //
    ["Normal", "[Hard] 매그너스", "Hard_Magnus", 2430030, 1, 10, 4033578],
    ["Normal", "[Chaos] 파풀라투스", "Chaos_Populatus", 2430030, 1, 22, 4001084],
    ["Normal", "[Chaos] 피에르", "Chaos_Pierre", 2430030, 1, 4, 4032301],
    ["Normal", "[Chaos] 반반", "Chaos_VonBon", 2430030, 1, 5, 4033047],
    ["Normal", "[Chaos] 블리디퀸", "Chaos_BloodyQueen", 2430030, 1, 6, 4001034],
    ["Normal", "[Chaos] 벨룸", "Chaos_Vellum", 2430030, 1, 7, 4036456],
    ["Normal", "[Hard] 스우", "Hard_Lotus", 2430030, 1, 13,  4001877],
    ["Normal", "[Hard] 데미안", "Hard_Demian", 2430030, 1, 15, 4031569],
    ["Normal", "[Hard] 루시드", "Hard_Lucid", 2430030, 1, 19, 4036315],
    ["Normal", "[Hard] 윌", "Hard_Will", 2430030, 1, 23, 4033644],
    ["Normal", "[Chaos] 더스크", "Chaos_Dusk", 2430030, 1, 26, 4031857],
    ["Normal", "[Hard] 듄켈", "Normal_Dunkel", 2430030, 1, 27, 4033450],
    ["Hard", "[Hard] 진 힐라", "Normal_JinHillah", 2430030, 1, 24, 4036454],
    ["Hard", "[Hard] 검은마법사", "Black_Mage", 2430030, 1, 25, 4033115],
    ["Hard", "[Hard] 세렌", "Hard_Seren", 2430030, 1, 28, 4032927]
    //["Normal", "[Normal] 혼테일", "Normal_Horntail", 2430030, 1, 2, 4320003], //no
    //["Normal", "[Chaos] 혼테일", "Chaos_Horntail", 2430030, 1, 2, 403330],
    //["Normal", "[Normal] 카웅", "Normal_Kawoong", 2430030, 1, 21, 403243], //ok
    //["Normal", "[Normal] 힐라", "Normal_Hillah", 2430030, 1, 3, 2430030], //ok
    //["Normal", "[노멀하드] 반 레온", "Normal_VonLeon", 2430030, 1, 8, 2430030], //OK
    //["Normal", "[Hard] 반 레온", "Hard_VonLeon", 2430030, 1, 8, 4036166],
    //["Normal", "[이지노멀] 아카이럼", "Easy_Arkarium", 2430030, 1, 9, 2430030], //OK
    //["Normal", "[Normal] 아카이럼", "Normal_Arkarium", 2430030, 1, 9, 2430030],
    //["Normal", "[노멀] 핑크빈", "Normal_Pinkbean", 2430030, 1, 11, 2430030], //OK
    //["Normal", "[Normal] 매그너스", "Normal_Magnus", 2430030, 1, 10, 2430030],
    //["Normal", "[이지노멀] 파풀라투스", "Normal_Populatus", 2430030, 1, 22, 2430030], //ok
    //["Normal", "[Normal] 피에르", "Normal_Pierre", 2430030, 1, 4, 2430030],
    //["Normal", "[Normal] 반반", "Normal_VonBon", 2430030, 1, 5, 2430030],
    //["Normal", "[Normal] 블러디퀸", "Normal_BloodyQueen", 2430030, 1, 6, 2430030],
    //["Normal", "[Normal] 벨룸", "Normal_Vellum", 2430030, 1, 7, 2430030],
    //["Normal", "[Normal] 스우", "Normal_Lotus", 2430030, 1, 13, 2430030],
    //["Normal", "[Normal] 데미안", "Normal_Demian", 2430030, 1, 15, 2430030],
    //["Normal", "[Normal] 루시드", "Normal_Lucid", 2430030, 1, 19, 2430030],
    //["Normal", "[Normal] 윌", "Normal_Will", 2430030, 1, 23, 2430030],
    //["Normal", "[Normal] 더스크", "Normal_Dusk", 2430030, 1, 26, 2430030],
];

function start() {
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        cm.getPlayer().removeV("Normal_Zakum"); //자쿰
        cm.getPlayer().getQuestNAdd(MapleQuest.getInstance(7003)).setCustomData("0");
        cm.getPlayer().removeV("Normal_Kawoong"); //카웅
        cm.getPlayer().getQuestNAdd(MapleQuest.getInstance(3590)).setCustomData("0");
        cm.getPlayer().removeV("Easy_Populatus"); //이지파풀
        cm.getPlayer().getQuestNAdd(MapleQuest.getInstance(7200)).setCustomData("0");
        cm.getPlayer().removeV("Normal_Populatus"); //노멀파풀
        cm.getPlayer().getQuestNAdd(MapleQuest.getInstance(7201)).setCustomData("0");
        cm.getPlayer().removeV("Normal_VonBon"); //노멀 반반
        cm.getPlayer().getQuestNAdd(MapleQuest.getInstance(30039)).setCustomData("0");
        cm.getPlayer().removeV("Chaos_VonBon"); //카오스 반반
        cm.getPlayer().getQuestNAdd(MapleQuest.getInstance(30044)).setCustomData("0");
        cm.getPlayer().removeV("Normal_BloodyQueen"); //노멀 블퀸
        cm.getPlayer().getQuestNAdd(MapleQuest.getInstance(30033)).setCustomData("0");
        cm.getPlayer().removeV("Chaos_BloodyQueen"); //카오스 블퀸
        cm.getPlayer().getQuestNAdd(MapleQuest.getInstance(30045)).setCustomData("0");
        cm.getPlayer().removeV("Normal_Pierre"); //노멀 피에르
        cm.getPlayer().getQuestNAdd(MapleQuest.getInstance(30032)).setCustomData("0");
        cm.getPlayer().removeV("Chaos_Pierre"); //카오스 피에르
        cm.getPlayer().getQuestNAdd(MapleQuest.getInstance(30043)).setCustomData("0");
        cm.getPlayer().removeV("Normal_Vellum"); //노멀 벨름
        cm.getPlayer().getQuestNAdd(MapleQuest.getInstance(30041)).setCustomData("0");
        cm.getPlayer().removeV("Chaos_Vellum"); //카오스 벨름
        cm.getPlayer().getQuestNAdd(MapleQuest.getInstance(30046)).setCustomData("0");
        cm.getPlayer().removeV("Normal_Magnus"); //노멀매그너스
        cm.getPlayer().getQuestNAdd(MapleQuest.getInstance(3993)).setCustomData("0");
        cm.getPlayer().removeV("Hard_Magnus"); //하드매그너스
        cm.getPlayer().getQuestNAdd(MapleQuest.getInstance(3992)).setCustomData("0");
        cm.getPlayer().removeV("Easy_Cygnus"); //이지시그너스
        cm.getPlayer().getQuestNAdd(MapleQuest.getInstance(31199)).setCustomData("0");
        cm.getPlayer().removeV("Normal_Cygnus"); //노멀시그너스
        cm.getPlayer().getQuestNAdd(MapleQuest.getInstance(31199)).setCustomData("0");
        cm.getPlayer().removeV("Hard_Seren"); //세렌
        cm.getPlayer().getQuestNAdd(MapleQuest.getInstance(39932)).setCustomData("0");
        cm.getPlayer().removeV("Hard_Seren"); //세렌
        cm.getPlayer().getQuestNAdd(MapleQuest.getInstance(3687)).setCustomData("0");
        var say = "일일보스 입장횟수를 1회씩 늘려주는 엔피시입니다.\r\n" +
        "해당 보스를 일일 1회 입장하신 이후에만 사용 가능합니다.\r\n" +
        "어떤 보스의 입장횟수를 1회 늘리시겠습니까?";
       
        say += "\r\n\r\n#b#e[Normal]#k#n\r\n";
      
        for (var i = 0; i < bosslist.length; i++) {
            if (bosslist[i][0] == "Normal") {
	            k = cm.getPlayer().getV(bosslist[i][2]);
	            key = k == null ? 0 : Long.parseLong(cm.getPlayer().getV(bosslist[i][2]));
	            icon = "#fUI/UIWindow2.img/UserList/Main/Boss/BossList/"+bosslist[i][5]+"/Icon/normal/0#";
                say += "#L" + i + "# " + icon + " " + bosslist[i][1] + "\r\n소모 아이템 : #i" + bosslist[i][6] + "##z" + bosslist[i][6] + "##l\r\n\r\n\r\n";
            }
        }
       
       // say += "\r\n\r\n#r#e[Hard] Or [Chaos]#k#n\r\n\r\n";
       
        for (var i = 0; i < bosslist.length; i++) {
            if (bosslist[i][0] == "Hard" || bosslist[i][0] == "Chaos") {
	            k = cm.getPlayer().getV(bosslist[i][2]);
	            key = k == null ? 0 : Long.parseLong(cm.getPlayer().getV(bosslist[i][2]));
                icon = "#fUI/UIWindow2.img/UserList/Main/Boss/BossList/"+bosslist[i][5]+"/Icon/normal/0#";
                say += "#L" + i + "# " + icon + " " + bosslist[i][1] + "\r\n소모 아이템 : #i" + bosslist[i][6] + "##z" + bosslist[i][6] + "##l\r\n\r\n\r\n";
            }
        }

        cm.sendSimple(say);

    } else if (status == 1) {
        choice = selection;
        cm.sendYesNo("정말로 " + bosslist[selection][1] + "의 일일 입장횟수를 1회 초기화 하시겠습니까?");

    } else if (status == 2) {
        if (!cm.haveItem(bosslist[choice][6])) {
            cm.sendOk("소모 아이템이 부족하여 초기화를 할 수 없습니다.");
            cm.dispose();
            return;
        }

        var va =String(key-1);
        key = k == null ? 0 : Long.parseLong(cm.getPlayer().getV(bosslist[choice][2]));
        cm.gainItem(bosslist[choice][6], -1);
        cm.getPlayer().addKV(bosslist[choice][2], va);
        switch (bosslist[choice][2]) {
            case "Normal_Zakum": // 자쿰 ok
                cm.getPlayer().getQuestNAdd(MapleQuest.getInstance(bosslist[choice][7])).setCustomData("0");
                cm.getPlayer().getQuestNAdd(MapleQuest.getInstance(bosslist[choice][8])).setCustomData("0");
                break;
            case "Easy_VonLeon": // 반레온 ok
            case "Normal_VonLeon":
            case "Hard_VonLeon":
                cm.getPlayer().updateInfoQuest(7850, "eNum=0;lastDate=0");
                break;
                case "Normal_Hillah": //힐라 ok
                cm.getPlayer().updateInfoQuest(3981, "eNum=0;lastDate=0");
                break;
            case "Normal_Horntail": // 혼테일 ok
            case "Chaos_Horntail":
                cm.getPlayer().updateInfoQuest(7312, "eNum=0;lastDate=0");
                break;
                case "Easy_Magnus": //매그너스
                case "Normal_Magnus":
                cm.getPlayer().updateInfoQuest(3993, "eNum=0;lastDate=0");
                break;
            case "Easy_Arkarium": //아카이럼 ok
            case "Normal_Arkarium":
                cm.getPlayer().updateInfoQuest(7851, "eNum=0;lastDate=0");
                break;
            case "Normal_Pinkbean": //노멀핑크빈 ok
            cm.getPlayer().updateInfoQuest(7403, "eNum=0;lastDate=0");
            break;
            case "Chaos_Pinkbean":
                cm.getPlayer().updateInfoQuest(7403, "eNumC=0;lastDateC=0");
                break;
            case "Hard_Lotus": //스우 ok
                cm.getPlayer().updateInfoQuest(33126, "eNum=0;lastDate=0");
                break;
            case "Hard_Demian": //데미안 ok
                cm.getPlayer().setKeyValue(34016, "lastDate", "0");
                break;
            case "Normal_Dunkel": //둔켈 OK
            case "Hard_Dunkel":
                    cm.getPlayer().setKeyValue(35138, "lastDateN", "0");
                    cm.getPlayer().setKeyValue(35140, "lastDateH", "0");
                break;
            case "Normal_Dusk": //더스크 OK
            case "Chaos_Dusk":
                cm.getPlayer().setKeyValue(35137, "lastDateN", "0");
                cm.getPlayer().setKeyValue(35139, "lastDateH", "0");
                break;
            case "Easy_Lucid":
            case "Normal_Lucid": //루시드 OK
            case "Hard_Lucid":
                 cm.getPlayer().updateInfoQuest(34364, "eNum=0;lastDate=0");
                 cm.getPlayer().updateInfoQuest(34364, "eNumE=0;lastDateE=0");
                 cm.getPlayer().updateInfoQuest(34364, "eNumH=0;lastDateH=0");
                 break;
            case "Normal_Will": //윌 OK
            case "Hard_Will":   
                 cm.getPlayer().updateInfoQuest(35100, "eNumN=0;lastDateN=0");
                 cm.getPlayer().updateInfoQuest(34364, "eNum=0;lastDate=0");
                 break;
            case "Normal_JinHillah": //진힐라 OK
                 cm.getPlayer().updateInfoQuest(35260, "eNum=0;lastDate=0");
                break;
            case "Black_Mage": //검은마법사 ok
                cm.getPlayer().updateInfoQuest(35377, "eNum=0;lastDate=0");
                 break;
            case "Hard_Seren":
                cm.getPlayer().setKeyValue(39932, "lastDate", "0");
                cm.getPlayer().setKeyValue(3687, "lastDate", "0");
                break;

        }

        cm.sendOk(bosslist[choice][1] + "보스의 일일 입장횟수를 1회 늘렸습니다.");
        cm.dispose();
    } else {
        cm.dispose();
        return;
    }
}