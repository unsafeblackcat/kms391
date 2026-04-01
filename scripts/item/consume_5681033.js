var enter = "\r\n";
var seld = -1;

var allstat = 0, atk = 50; // 1회당 올스텟, 공마 증가치

function start() {
    status = -1;
    action(1, 0, 0);
}
function action(mode, type, sel) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        var txt = "#fs15##fc0xFF000000#캐시아이템을 #b#z5681033##fc0xFF000000#로 강화할 수 있다는 사실을 알고 계시나요? #b원하시는 캐시아이템#fc0xFF000000#을 골라주세요.\r\n#r(1회당  공,마 50 증가 중첩가능 / 최대 모든상자합산 50회)#k\r\n";
        for (i = 0; i < cm.getInventory(6).getSlotLimit(); i++) {
            if (cm.getInventory(6).getItem(i) != null) {
                if (cm.isCash(cm.getInventory(6).getItem(i).getItemId())) {
                    txt += "#L" + i + "# #i" + cm.getInventory(6).getItem(i).getItemId() + "# #b#z" + cm.getInventory(6).getItem(i).getItemId() + "#\r\n";
                }
            }
        }
        cm.sendSimple(txt);
    } else if (status == 1) {
        if (!cm.haveItem(5681033, 1)) {
            cm.sendOk("#fs15##fc0xFF000000#1회 강화하기 위해선 #b" + price + " #z5681033##fc0xFF000000#가 필요합니다.");
            cm.dispose();
            return;
        }

        item = cm.getInventory(6).getItem(sel);
        if (item == null) {
            return;
        }
        if (item.getCoption1() >= 9999) {
            cm.sendOk("이 아이템은 이미 50회 스텟 부여를 완료하였습니다.");
            cm.dispose();
            return;
        }
        if (!cm.isCash(item.getItemId())) {
            cm.sendOk("핵 사용 적발");
            World.Broadcast.broadcastMessage(CField.getGameMessage(25, "[" + 서버이름 + "] " + cm.getPlayer().getName() + " 님 이 공지를 보시면 DM부탁드립니다."));
            cm.dispose();
            return;
        }
        item.setOwner((item.getCoption1() + 1) + "강");
        item.setStr(item.getStr() + allstat);
        item.setDex(item.getDex() + allstat);
        item.setInt(item.getInt() + allstat);
        item.setLuk(item.getLuk() + allstat);
        item.setWatk(item.getWatk() + atk);
        item.setMatk(item.getMatk() + atk);
        item.setCoption1(item.getCoption1() + 1);
        cm.gainItem(5681033, -1);
        var chat = "";
        chat += "#fs15#스탯부여에 성공했습니다. \r\n\r\n";

        chat += "#b현재 강화 횟수 : " + item.getCoption1() + "#k\r\n";
        chat += "#bSTR : " + item.getStr() + "\r\n";
        chat += "#bDEX : " + item.getDex() + "\r\n";
        chat += "#bINT : " + item.getInt() + "\r\n";
        chat += "#bLUK  : " + item.getLuk() + "\r\n";
        chat += "#b공격력 : " + item.getWatk() + "\r\n";
        chat += "#b마력 : " + item.getMatk() + "\r\n";
        cm.sendOk(chat);

        cm.dispose();
        cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.getByType(6));
    }
}
