importPackage(java.lang);

var status = -1;

var items = [ //強化等級, 強化成功機率, 各項屬性, 攻魔
    [0, 0, 0, 0],
    [1, 50, 2, 2],
    [2, 50, 2, 2],
    [3, 50, 2, 2],
    [4, 50, 2, 2],
    [5, 50, 2, 2],
    [6, 50, 2, 2],
    [7, 50, 2, 2],
    [8, 50, 2, 2],
    [9, 50, 2, 2],
    [10, 50, 2, 2],
    [11, 50, 2, 2],
    [12, 50, 2, 2],
    [13, 50, 2, 2],
    [14, 50, 2, 2],
    [15, 50, 2, 2],
    [16, 50, 2, 2],
    [17, 50, 2, 2],
    [18, 50, 2, 2],
    [19, 50, 2, 2],
    [20, 50, 2, 2],
    [21, 50, 2, 2],
    [22, 50, 2, 2],
    [23, 50, 2, 2],
    [24, 50, 2, 2],
    [25, 50, 2, 2],
    [26, 50, 2, 2],
    [27, 50, 2, 2],
    [28, 50, 2, 2],
    [29, 50, 2, 2],
    [30, 50, 2, 2],
    [31, 50, 2, 2],
    [32, 50, 2, 2],
    [33, 50, 2, 2],
    [34, 50, 2, 2],
    [35, 50, 2, 2],
    [36, 50, 2, 2],
    [37, 50, 2, 2],
    [38, 50, 2, 2],
    [39, 50, 2, 2],
    [40, 50, 2, 2],
    [41, 50, 2, 2],
    [42, 50, 2, 2],
    [43, 50, 2, 2],
    [44, 50, 2, 2],
    [45, 50, 2, 2],
    [46, 50, 2, 2],
    [47, 50, 2, 2],
    [48, 50, 2, 2],
    [49, 50, 2, 2],
    [50, 50, 2, 2]    
];

function getAddEnhance(item) {
    var owner = item.getOwner();
    return owner == "★1強★" ? 1 : 
            owner == "★2強★" ? 2 : 
            owner == "★3強★" ? 3 : 
            owner == "★4強★" ? 4 : 
            owner == "★5強★" ? 5 : 
            owner == "★6強★" ? 6 : 
            owner == "★7強★" ? 7 : 
            owner == "★8強★" ? 8 : 
            owner == "★9強★" ? 9 : 
            owner == "★10強★" ? 10 : 
            owner == "★11強★" ? 11 : 
            owner == "★12強★" ? 12 : 
            owner == "★13強★" ? 13 : 
            owner == "★14強★" ? 14 : 
            owner == "★15強★" ? 15 : 
            owner == "★16強★" ? 16 : 
            owner == "★17強★" ? 17 : 
            owner == "★18強★" ? 18 : 
            owner == "★19強★" ? 19 : 
            owner == "★20強★" ? 20 : 
            owner == "★21強★" ? 21 : 
            owner == "★22強★" ? 22 : 
            owner == "★23強★" ? 23 : 
            owner == "★24強★" ? 24 : 
            owner == "★25強★" ? 25 : 
            owner == "★26強★" ? 26 : 
            owner == "★27強★" ? 27 : 
            owner == "★28強★" ? 28 : 
            owner == "★29強★" ? 29 : 
            owner == "★30強★" ? 30 : 
            owner == "★31強★" ? 31 : 
            owner == "★32強★" ? 32 : 
            owner == "★33強★" ? 33 : 
            owner == "★34強★" ? 34 : 
            owner == "★35強★" ? 35 : 
            owner == "★36強★" ? 36 : 
            owner == "★37強★" ? 37 : 
            owner == "★38強★" ? 38 : 
            owner == "★39強★" ? 39 : 
            owner == "★40強★" ? 40 : 
            owner == "★41強★" ? 41 : 
            owner == "★42強★" ? 42 : 
            owner == "★43強★" ? 43 : 
            owner == "★44強★" ? 44 : 
            owner == "★45強★" ? 45 : 
            owner == "★46強★" ? 46 : 
            owner == "★47強★" ? 47 : 
            owner == "★48強★" ? 48 : 
            owner == "★49強★" ? 49 : 
            owner == "★50強★" ? 50 : 
            0; 
}

var mat = 4310237; //狩獵硬幣
var count = 20; //數量

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
    if (status == 0) {
        var count1 = 0;
        var say = "請選擇要強化的裝飾道具。\r\n\r\n";
        for (i = 0; i < cm.getInventory(6).getSlotLimit(); i++) {
            if (cm.getInventory(6).getItem(i) != null) {
                if (Packages.server.MapleItemInformationProvider.getInstance().isCash(cm.getInventory(6).getItem(i).getItemId())) {
                    say += "#L" + i + "##e#b#i" + cm.getInventory(6).getItem(i).getItemId() + "# #z" + cm.getInventory(6).getItem(i).getItemId() + "# (" + i + "號槽位)#l\r\n";
                    count1++;
                }
            }
        }
        if (count1 <= 0) {
            cm.sendOk("請確認是否持有可強化的裝飾道具。");
            cm.dispose();
            return;
        }
        cm.sendSimple(say);
    } else if (status == 1) {
        if (re == 0) {
            slot = selection;
            item = cm.getInventory(6).getItem(selection);
        }
        if (item.getOwner().equals("★50強★")) {
            cm.sendOk("此道具已強化至50強等級。");
            cm.dispose();
            return;
        }
        itemid = item.getItemId(); 
        say = "";
        say += "<強化材料資訊>\r\n";
        say += "#i" + mat + "##z" + mat + "# 需要" + count + "個\r\n\r\n";
        say += "<強化資訊>\r\n";
        say += "強化等級：" + getAddEnhance(item) + "強 -> " + (getAddEnhance(item) + 1) + "強\r\n";
        say += "強化成功時全屬性 +" + items[getAddEnhance(item) + 1][2] + "，攻魔 +" + items[getAddEnhance(item) + 1][3] + "提升\r\n";
        say += "成功機率 " + items[getAddEnhance(item) + 1][1] + "%\r\n\r\n"; 
        say += "<道具資訊>\r\n";
        say += "強化道具：#i" + itemid + "# #z" + itemid + "#\r\n";
        say += "STR：" + item.getStr() + "  |  DEX：" + item.getDex() + "  |  INT：" + item.getInt() + "  |  LUK " + item.getLuk() + "\r\n";
        say += "攻擊力：" + item.getWatk() + "  |  魔力：" + item.getMatk() + "  | 星之力：" + item.getEnhance() + "星\r\n";
        say += "全屬性：" + item.getAllStat() + "%  |  總傷害：" + item.getTotalDamage() + "%  |  首領攻擊力：" + item.getBossDamage() + "%\r\n";
        say += "道具強化次數：" + getAddEnhance(item) + "強\r\n\r\n";
        cm.sendYesNo(say + "確定要強化此道具嗎？");
    } else if (status == 2) {
        if (cm.haveItem(mat, count)) {
            cm.gainItem(mat, -count);
        } else {
            cm.sendOk("#i" + mat + "##z" + mat + "# 數量不足" + count + "個，無法強化。");
            cm.dispose();
            return;
        }
        if (cm.getInventory(1).getItem(slot) != null) {
            if (item.getOwner().equals("★50強★")) {
                cm.sendOk("此道具已強化至50強等級。");
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
                item.setOwner("★"+(getAddEnhance(item)+1)+"強★");
                cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.DECORATION);
                say = "";     
                say += "<道具資訊>\r\n";
                say += "強化道具：#i" + itemid + "# #z" + itemid + "#\r\n";
                say += "STR：" + item.getStr() + "  |  DEX：" + item.getDex() + "  |  INT：" + item.getInt() + "  |  LUK " + item.getLuk() + "\r\n";
                say += "攻擊力：" + item.getWatk() + "  |  魔力：" + item.getMatk() + "  | 星之力：" + item.getEnhance() + "星\r\n";
                say += "全屬性：" + item.getAllStat() + "%  |  總傷害：" + item.getTotalDamage() + "%  |  首領攻擊力：" + item.getBossDamage() + "%\r\n";
                say += "道具強化次數：" + getAddEnhance(item) + "強\r\n\r\n\r\n";
                cm.sendYesNo("強化成功。\r\n若要繼續強化，請點擊「是」。\r\n\r\n" + say);
                Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(2, "[系統]：" + cm.getPlayer().getName() + "玩家的" + Packages.server.MapleItemInformationProvider.getInstance().getName(itemid) + "已達成" + getAddEnhance(item) + "強。"));
                re = 1;
                status = 1;
            } else {
                item.setStr(item.getStr() - items[getAddEnhance(item)][2]);
                item.setDex(item.getDex() - items[getAddEnhance(item)][2]);
                item.setInt(item.getInt() - items[getAddEnhance(item)][2]);
                item.setLuk(item.getLuk() - items[getAddEnhance(item)][2]);
                item.setWatk(item.getWatk() - items[getAddEnhance(item)][3]);
                item.setMatk(item.getMatk() - items[getAddEnhance(item)][3]);
                item.setOwner("★"+(getAddEnhance(item)-1)+"強★");
                cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.DECORATION);
                say = "";
                say += "<道具資訊>\r\n";
                say += "強化道具：#i" + itemid + "# #z" + itemid + "#\r\n";
                say += "STR：" + item.getStr() + "  |  DEX：" + item.getDex() + "  |  INT：" + item.getInt() + "  |  LUK " + item.getLuk() + "\r\n";
                say += "攻擊力：" + item.getWatk() + "  |  魔力：" + item.getMatk() + "  | 星之力：" + item.getEnhance() + "星\r\n";
                say += "全屬性：" + item.getAllStat() + "%  |  總傷害：" + item.getTotalDamage() + "%  |  首領攻擊力：" + item.getBossDamage() + "%\r\n";
                say += "道具強化次數：" + getAddEnhance(item) + "強\r\n\r\n\r\n";
                cm.sendYesNo("強化失敗，屬性下降。\r\n若要繼續強化，請點擊「是」。\r\n\r\n" + say);
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