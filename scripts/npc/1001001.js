importPackage(java.lang);

importPackage(Packages.constants);
importPackage(Packages.client);
importPackage(Packages.tools);
importPackage(Packages.launch);
importPackage(Packages.packet.creators);
importPackage(Packages.constants);
importPackage(Packages.tools);
importPackage(Packages.handling.world);
importPackage(java.util);
importPackage(Packages.server);
importPackage(Packages.database);


var status = -1;

var items = [ //강화수, 강화성공확률, 각스탯, 공마
    [0, 0, 0, 0],
    [1, 100, 2, 1],
    [2, 100, 2, 1],
    [3, 100, 2, 1],
    [4, 100, 2, 1],
    [5, 100, 2, 1],
    [6, 100, 2, 1],
    [7, 100, 2, 1],
    [8, 100, 2, 1],
    [9, 100, 2, 1],
    [10, 100, 2, 1],
    [11, 100, 2, 1],
    [12, 100, 2, 1],
    [13, 100, 2, 1],
    [14, 100, 2, 1],
    [15, 100, 2, 1],
    [16, 100, 2, 1],
    [17, 100, 2, 1],
    [18, 100, 2, 1],
    [19, 100, 2, 1],
    [20, 100, 2, 1],
    [21, 100, 2, 1],
    [22, 100, 2, 1],
    [23, 100, 2, 1],
    [24, 100, 2, 1],
    [25, 100, 2, 1],
    [26, 100, 2, 1],
    [27, 100, 2, 1],
    [28, 100, 2, 1],
    [29, 100, 2, 1],
    [30, 100, 2, 1],
    [31, 100, 2, 1],
    [32, 100, 2, 1],
    [33, 100, 2, 1],
    [34, 100, 2, 1],
    [35, 100, 2, 1],
    [36, 100, 2, 1],
    [37, 100, 2, 1],
    [38, 100, 2, 1],
    [39, 100, 2, 1],
    [100, 100, 2, 1],
    [41, 100, 2, 1],
    [42, 100, 2, 1],
    [43, 100, 2, 1],
    [44, 100, 2, 1],
    [45, 100, 2, 1],
    [46, 100, 2, 1],
    [47, 100, 2, 1],
    [48, 100, 2, 1],
    [49, 100, 2, 1],
    [100, 100, 2, 1],
    [51, 100, 2, 1],
    [52, 100, 2, 1],
    [53, 100, 2, 1],
    [54, 100, 2, 1],
    [55, 100, 2, 1],
    [56, 100, 2, 1],
    [57, 440, 2, 1],
    [58, 100, 2, 1],
    [59, 100, 2, 1],
    [100, 100, 2, 1],
    [61, 100, 2, 1],
    [62, 100, 2, 1],
    [63, 100, 2, 1],
    [64, 100, 2, 1],
    [65, 100, 2, 1],
    [66, 100, 2, 1],
    [67, 100, 2, 1],
    [68, 100, 2, 1],
    [69, 100, 2, 1],
    [70, 100, 2, 1],
    [71, 100, 2, 1],
    [72, 100, 2, 1],
    [73, 100, 2, 1],
    [74, 100, 2, 1],
    [75, 100, 2, 1],
    [76, 100, 2, 1],
    [77, 100, 2, 1],
    [78, 100, 2, 1],
    [79, 100, 2, 1],
    [80, 100, 2, 1],
    [81, 100, 2, 1],
    [82, 100, 2, 1],
    [83, 100, 2, 1],
    [84, 100, 2, 1],
    [85, 100, 2, 1],
    [86, 100, 2, 1],
    [87, 100, 2, 1],
    [88, 100, 2, 1],
    [89, 100, 2, 1],
    [90, 100, 2, 1],
    [91, 100, 2, 1],
    [92, 100, 2, 1],
    [93, 100, 2, 1],
    [94, 100, 2, 1],
    [95, 100, 2, 1],
    [96, 100, 2, 1],
    [97, 100, 2, 1],
    [98, 100, 2, 1],
    [99, 100, 2, 1],
    [100, 100, 2, 1]    
];

function getAddEnhance(item) {
	var owner = item.getOwner();
    return owner == "★1강★" ? 1 : 
	        owner == "★2강★" ? 2 : 
	        owner == "★3강★" ? 3 : 
	        owner == "★4강★" ? 4 : 
	        owner == "★5강★" ? 5 : 
	        owner == "★6강★" ? 6 : 
	        owner == "★7강★" ? 7 : 
	        owner == "★8강★" ? 8 : 
	        owner == "★9강★" ? 9 : 
	        owner == "★10강★" ? 10 : 
	        owner == "★11강★" ? 11 : 
	        owner == "★12강★" ? 12 : 
	        owner == "★13강★" ? 13 : 
	        owner == "★14강★" ? 14 : 
	        owner == "★15강★" ? 15 : 
	        owner == "★16강★" ? 16 : 
	        owner == "★17강★" ? 17 : 
	        owner == "★18강★" ? 18 : 
	        owner == "★19강★" ? 19 : 
	        owner == "★20강★" ? 20 : 
            owner == "★21강★" ? 21 : 
	        owner == "★22강★" ? 22 : 
	        owner == "★23강★" ? 23 : 
	        owner == "★24강★" ? 24 : 
	        owner == "★25강★" ? 25 : 
	        owner == "★26강★" ? 26 : 
	        owner == "★27강★" ? 27 : 
	        owner == "★28강★" ? 28 : 
	        owner == "★29강★" ? 29 : 
	        owner == "★30강★" ? 30 : 
	        owner == "★31강★" ? 31 : 
	        owner == "★32강★" ? 32 : 
	        owner == "★33강★" ? 33 : 
	        owner == "★34강★" ? 34 : 
	        owner == "★35강★" ? 35 : 
	        owner == "★36강★" ? 36 : 
	        owner == "★37강★" ? 37 : 
	        owner == "★38강★" ? 38 : 
	        owner == "★39강★" ? 39 : 
	        owner == "★40강★" ? 40 : 
            owner == "★41강★" ? 41 : 
	        owner == "★42강★" ? 42 : 
	        owner == "★43강★" ? 43 : 
	        owner == "★44강★" ? 44 : 
	        owner == "★45강★" ? 45 : 
	        owner == "★46강★" ? 46 : 
	        owner == "★47강★" ? 47 : 
	        owner == "★48강★" ? 48 : 
	        owner == "★49강★" ? 49 : 
	        owner == "★50강★" ? 50 : 
            owner == "★51강★" ? 51 : 
	        owner == "★52강★" ? 52 : 
	        owner == "★53강★" ? 53 : 
	        owner == "★54강★" ? 54 : 
	        owner == "★55강★" ? 55 : 
	        owner == "★56강★" ? 56 : 
	        owner == "★57강★" ? 57 : 
	        owner == "★58강★" ? 58 : 
	        owner == "★59강★" ? 59 : 
	        owner == "★60강★" ? 60 : 
	        owner == "★61강★" ? 61 : 
	        owner == "★62강★" ? 62 : 
	        owner == "★63강★" ? 63 : 
	        owner == "★64강★" ? 64 : 
	        owner == "★65강★" ? 65 : 
	        owner == "★66강★" ? 66 : 
	        owner == "★67강★" ? 67 : 
	        owner == "★68강★" ? 68 : 
	        owner == "★69강★" ? 69 : 
	        owner == "★70강★" ? 70 : 
            owner == "★71강★" ? 71 : 
	        owner == "★72강★" ? 72 : 
	        owner == "★73강★" ? 73 : 
	        owner == "★74강★" ? 74 : 
	        owner == "★75강★" ? 75 : 
	        owner == "★76강★" ? 76 : 
	        owner == "★77강★" ? 77 : 
	        owner == "★78강★" ? 78 : 
	        owner == "★79강★" ? 79 : 
	        owner == "★80강★" ? 80 : 
	        owner == "★81강★" ? 81 : 
	        owner == "★82강★" ? 82 : 
	        owner == "★83강★" ? 83 : 
	        owner == "★84강★" ? 84 : 
	        owner == "★85강★" ? 85 : 
	        owner == "★86강★" ? 86 : 
	        owner == "★87강★" ? 87 : 
	        owner == "★88강★" ? 88 : 
	        owner == "★89강★" ? 89 : 
	        owner == "★90강★" ? 90 : 
            owner == "★91강★" ? 91 : 
	        owner == "★92강★" ? 92 : 
	        owner == "★93강★" ? 93 : 
	        owner == "★94강★" ? 94 : 
	        owner == "★95강★" ? 95 : 
	        owner == "★96강★" ? 96 : 
	        owner == "★97강★" ? 97 : 
	        owner == "★98강★" ? 98 : 
	        owner == "★99강★" ? 99 : 
	        owner == "★100강★" ? 100 : 
            0; 
}

var mat = 4310237; //사냥 코인
var count = 200; //갯수

var item, itemid, slot, choice, say;
var re = 0;

function start () {
    action (1, 0, 0);
}

function action (mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
	cm.getPlayer().setKeyValue(13191, "skinroom", 96 + "");
    if (status == 0) {
        var count1 = 0;
        var say = "강화할 치장아이템을 선택해 주세요.\r\n\r\n";
        for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
            if (cm.getInventory(1).getItem(i) != null) {
                if (Packages.server.MapleItemInformationProvider.getInstance().getEquipById(cm.getInventory(1).getItem(i).getItemId())) {
                    say += "#L" + i + "##e#b#i" + cm.getInventory(1).getItem(i).getItemId() + "# #z" + cm.getInventory(1).getItem(i).getItemId() + "# (" + i + "번째 슬롯)#l\r\n";
                    count1++;
                }
            }
        }
        if (count1 <= 0) {
            cm.sendOk("강화할 치장아이템 소지하고 있는지 확인해 주세요.");
            cm.dispose();
            return;
        }
        cm.sendSimple(say);
    } else if (status == 1) {
        if (re == 0) {
            slot = selection;
            item = cm.getInventory(1).getItem(selection);
        }
        if (item.getOwner().equals("★100강★")) {
            cm.sendOk("이미 100강까지 강화가 완료된 아이템 입니다.");
            cm.dispose();
            return;
        }
        itemid = item.getItemId(); 
        say = "";
        say += "<강화 재료 정보>\r\n";
        say += "#i" + mat + "##z" + mat + "# " + count + "개 필요\r\n\r\n";
        say += "<강화 정보>\r\n";
        say += "강화 : " + getAddEnhance(item) + "강 -> " + (getAddEnhance(item) + 1) + "강\r\n";
        say += "강화 성공 시 올스탯 +" + items[getAddEnhance(item) + 1][2] + ", 공마 +" + items[getAddEnhance(item) + 1][3] + " 증가\r\n";
        say += "성공확률 " + items[getAddEnhance(item) + 1][1] + "%\r\n\r\n"; 
        say += "<아이템 정보>\r\n";
        say += "강화할 아이템 : #i" + itemid + "# #z" + itemid + "#\r\n";
        say += "STR : " + item.getStr() + "  |  DEX : " + item.getDex() + "  |  INT : " + item.getInt() + "  |  LUK " + item.getLuk() + "\r\n";
        say += "공격력 : " + item.getWatk() + "  |  마력 : " + item.getMatk() + "  | 스타포스 : " + item.getEnhance() + "성\r\n";
        say += "올 스탯 : " + item.getAllStat() + "%  |  총 데미지 : " + item.getTotalDamage() + "%  |  보스 공격력 : " + item.getBossDamage() + "%\r\n";
        say += "아이템 강화 횟수 : " + getAddEnhance(item) + "강\r\n\r\n";
        cm.sendYesNo(say + "정말로 이 아이템을 강화 하시겠습니까?");
    } else if (status == 2) {
        if (cm.haveItem(mat, count)) {
            cm.gainItem(mat, -count);
        } else {
            cm.sendOk("#i" + mat + "##z" + mat + "# " + count + "개가 없어 강화가 불가능합니다.");
            cm.dispose();
            return;
        }
        if (cm.getInventory(1).getItem(slot) != null) {
            if (item.getOwner().equals("★100강★")) {
                cm.sendOk("이미 100강까지 강화가 완료된 아이템 입니다.");
                cm.dispose();
                return;
            }
            var rand = Math.ceil(Math.random() * 100);
            if (rand >= 0 && rand <= items[getAddEnhance(item) + 1][1]) {
                item.setStr(item.getStr() + items[getAddEnhance(item) + 1][2]);
                item.setDex(item.getDex() + items[getAddEnhance(item) + 1][2]);
                item.setInt(item.getInt() + items[getAddEnhance(item) + 1][2]);
                item.setLuk(item.getLuk() + items[getAddEnhance(item) + 1][2]);
                item.setWatk(item.getWatk() + items[getAddEnhance(item) + 1][3]);
                item.setMatk(item.getMatk() + items[getAddEnhance(item) + 1][3]);
                item.setOwner("★100강★");
                //item.setOwner("★"+(getAddEnhance(item)+1)+"강★");
                cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.EQUIP);
                say = "";     
                say += "<아이템 정보>\r\n";
                say += "강화할 아이템 : #i" + itemid + "# #z" + itemid + "#\r\n";
                say += "STR : " + item.getStr() + "  |  DEX : " + item.getDex() + "  |  INT : " + item.getInt() + "  |  LUK " + item.getLuk() + "\r\n";
                say += "공격력 : " + item.getWatk() + "  |  마력 : " + item.getMatk() + "  | 스타포스 : " + item.getEnhance() + "성\r\n";
                say += "올 스탯 : " + item.getAllStat() + "%  |  총 데미지 : " + item.getTotalDamage() + "%  |  보스 공격력 : " + item.getBossDamage() + "%\r\n";
                say += "아이템 강화 횟수 : " + getAddEnhance(item) + "강\r\n\r\n\r\n";
                cm.sendYesNo("강화에 성공하였습니다.\r\n계속 강화하시려면 '예'를 눌러 주세요.\r\n\r\n" + say);
                //Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 " + cm.getItemName(item['itemid']) + " 아이템으로 승급하셨습니다."));
                re = 1;
                status = 1;
            } else {
                item.setStr(item.getStr() - items[getAddEnhance(item)][2]);
                item.setDex(item.getDex() - items[getAddEnhance(item)][2]);
                item.setInt(item.getInt() - items[getAddEnhance(item)][2]);
                item.setLuk(item.getLuk() - items[getAddEnhance(item)][2]);
                item.setWatk(item.getWatk() - items[getAddEnhance(item)][3]);
                item.setMatk(item.getMatk() - items[getAddEnhance(item)][3]);
                item.setOwner("★"+(getAddEnhance(item)-1)+"강★");
                cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.EQUIP);
                say = "";
                say += "<아이템 정보>\r\n";
                say += "강화할 아이템 : #i" + itemid + "# #z" + itemid + "#\r\n";
                say += "STR : " + item.getStr() + "  |  DEX : " + item.getDex() + "  |  INT : " + item.getInt() + "  |  LUK " + item.getLuk() + "\r\n";
                say += "공격력 : " + item.getWatk() + "  |  마력 : " + item.getMatk() + "  | 스타포스 : " + item.getEnhance() + "성\r\n";
                say += "올 스탯 : " + item.getAllStat() + "%  |  총 데미지 : " + item.getTotalDamage() + "%  |  보스 공격력 : " + item.getBossDamage() + "%\r\n";
                say += "아이템 강화 횟수 : " + getAddEnhance(item) + "강\r\n\r\n\r\n";
                cm.sendYesNo("강화에 실패하여 옵션이 하락하였습니다.\r\n계속 강화하시려면 '예'를 눌러 주세요.\r\n\r\n" + say);
                re = 1;
                status = 1;
            }
        } else {
            cm.dispose();
            return;
        }
    } else {
        cm.dispose();
        return;
    }
}