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

var items = [//강화수, 강화성공확률, 각스탯, 공마
    [0, 0, 0, 0],
    [1, 75, 1, 1],
    [2, 75, 1, 1],
    [3, 75, 1, 1],
    [4, 75, 1, 1],
    [5, 75, 1, 1],
    [6, 75, 1, 1],
    [7, 75, 1, 1],
    [8, 75, 1, 1],
    [9, 75, 1, 1],
    [10, 65, 1, 1],
    [11, 65, 1, 1],
    [12, 65, 1, 1],
    [13, 65, 1, 1],
    [14, 65, 1, 1],
    [15, 65, 1, 1],
    [16, 65, 1, 1],
    [17, 65, 1, 1],
    [18, 65, 1, 1],
    [19, 65, 1, 1],
    [20, 65, 1, 1],
    [21, 65, 1, 1],
    [22, 65, 1, 1],
    [23, 65, 1, 1],
    [24, 65, 1, 1],
    [25, 65, 1, 1],
    [26, 65, 1, 1],
    [27, 65, 1, 1],
    [28, 65, 1, 1],
    [29, 65, 1, 1],
    [30, 65, 1, 1],
    [31, 65, 1, 1],
    [32, 65, 1, 1],
    [33, 65, 1, 1],
    [34, 65, 1, 1],
    [35, 65, 1, 1],
    [36, 65, 1, 1],
    [37, 65, 1, 1],
    [38, 65, 1, 1],
    [39, 65, 1, 1],
    [40, 65, 1, 1],
    [41, 65, 1, 1],
    [42, 65, 1, 1],
    [43, 65, 1, 1],
    [44, 65, 1, 1],
    [45, 65, 1, 1],
    [46, 65, 1, 1],
    [47, 65, 1, 1],
    [48, 65, 1, 1],
    [49, 65, 1, 1],
    [50, 65, 1, 1],
    [51, 40, 1, 1],
    [52, 40, 1, 1],
    [53, 40, 1, 1],
    [54, 40, 1, 1],
    [55, 40, 1, 1],
    [56, 40, 1, 1],
    [57, 40, 1, 1],
    [58, 40, 1, 1],
    [59, 40, 1, 1],
    [60, 40, 1, 1],
    [61, 40, 1, 1],
    [62, 40, 1, 1],
    [63, 40, 1, 1],
    [64, 40, 1, 1],
    [65, 40, 1, 1],
    [66, 40, 1, 1],
    [67, 40, 1, 1],
    [68, 40, 1, 1],
    [69, 40, 1, 1],
    [70, 30, 1, 1],
    [71, 30, 1, 1],
    [72, 30, 1, 1],
    [73, 30, 1, 1],
    [74, 30, 1, 1],
    [75, 30, 1, 1],
    [76, 30, 1, 1],
    [77, 30, 1, 1],
    [78, 30, 1, 1],
    [79, 30, 1, 1],
    [80, 30, 1, 1],
    [81, 30, 1, 1],
    [82, 30, 1, 1],
    [83, 30, 1, 1],
    [84, 30, 1, 1],
    [85, 30, 1, 1],
    [86, 30, 1, 1],
    [87, 30, 1, 1],
    [88, 30, 1, 1],
    [89, 30, 1, 1],
    [90, 15, 1, 1],
    [91, 15, 1, 1],
    [92, 15, 1, 1],
    [93, 15, 1, 1],
    [94, 15, 1, 1],
    [95, 15, 1, 1],
    [96, 15, 1, 1],
    [97, 15, 1, 1],
    [98, 15, 1, 1],
    [99, 15, 1, 1],
    [100, 15, 1, 1]
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

var mat = 4031788; //사냥 코인
var count = 200; //갯수

var item, itemid, slot, choice, say;
var re = 0;

function start() {
    action(1, 0, 0);
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
                //item.setOwner("★100강★");
                item.setOwner("★" + (getAddEnhance(item) + 1) + "강★");
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
                item.setOwner("★" + (getAddEnhance(item) - 1) + "강★");
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