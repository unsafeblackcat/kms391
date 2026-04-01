var enter = "\r\n";
var need = 2438117;
var seld = -15;
var selectedItem;

var enhanceOption = 15;

var successProbability = 30;

var ItemResult = [
"[E] 1강",
"[E] 2강",
"[E] 3강",
"[E] 4강",
"[E] 5강",
"[E] 6강",
"[E] 7강",
"[E] 8강",
"[E] 9강",
"[E] 10강",
"[E] 11강",
"[E] 12강",
"[E] 13강",
"[E] 14강",
"[E] 15강"
];

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 1)
        status++;
    else
        status--;
    if (status == 0) {

        var chat = "#fs15#";

        chat += "#b#i" + need + "#" + " #z" + need + "##k으로 아이템을 강화하시겠습니까?" + enter;
        chat += "#b ■ [강화 성공시 부여되는 옵션] ■#k" + enter;
        chat += "#e" + "⊙ DEX + 15" + "#n" + enter;
        chat += "#e#b강화 성공확률 : " + 30 +"% #k| #r강화 실패확률 : " + (100 - 30) + "%#n#k" + enter;
        chat += "#fc0xFFD5D5D5#─────────────────────────#k" + enter;

        for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
            if (cm.getInventory(1).getItem(i) != null) {
                    chat += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId() + "# #fc0xFF6B66FF##z" + cm.getInventory(1).getItem(i).getItemId() + "#\r\n";
            }
        }

        cm.sendSimple(chat);
    } else if (status == 1) {
        var successProb = 15;
        if (cm.haveItem(need, 15)) {
            var prob = Packages.server.Randomizer.rand(1, 100);

            selectedItem = cm.getInventory(1).getItem(selection);

            if (successProbability > prob) {
            var enhanceValue = selectedItem.getOwner();

            switch(enhanceValue) {
                case ItemResult[0]: // 1강 -> 2강
                    selectedItem.setOwner(ItemResult[1]);
                    break;
                case ItemResult[1]: // 2강 -> 3강
                    selectedItem.setOwner(ItemResult[2]);
                    break;
                case ItemResult[2]: // 3강 -> 4강
                    selectedItem.setOwner(ItemResult[3]);
                    break;
                case ItemResult[3]: // 4강 -> 5강
                    selectedItem.setOwner(ItemResult[4]);
                    break;
                case ItemResult[4]: // 5강 -> 6강
                    selectedItem.setOwner(ItemResult[5]);
                    break;
                case ItemResult[5]: // 6강 -> 7강
                    selectedItem.setOwner(ItemResult[6]);
                    break;
                case ItemResult[6]: // 7강 -> 8강
                    selectedItem.setOwner(ItemResult[7]);
                    break;
                case ItemResult[7]: // 8강 -> 9강
                    selectedItem.setOwner(ItemResult[8]);
                    break;
                case ItemResult[8]: // 9강 -> 10강
                    selectedItem.setOwner(ItemResult[9]);
                    break;
                case ItemResult[9]: // 10강 -> 11강
                    selectedItem.setOwner(ItemResult[10]);
                    break;
                case ItemResult[10]: // 11강 -> 12강
                    selectedItem.setOwner(ItemResult[11]);
                    break;
                case ItemResult[11]: // 12강 -> 13강
                    selectedItem.setOwner(ItemResult[12]);
                    break;
                case ItemResult[12]: // 13강 -> 14강
                    selectedItem.setOwner(ItemResult[13]);
                    break;
                case ItemResult[13]: // 14강 -> 15강
                    selectedItem.setOwner(ItemResult[14]);
                    break;
                case ItemResult[14]: // 15강에서는 Stop
                  cm.sendOk("#fs15#해당 아이템은 더 이상 강화할수 없습니다.");
                  cm.dispose();
                  return;
                default: // null 일때 1강 주기
                    if (selectedItem.getOwner() == "") {
                        selectedItem.setOwner(ItemResult[0]);
                    }
                break;
            }
                var beforeOption = selectedItem.getDex();

                selectedItem.setDex(selectedItem.getDex() + enhanceOption);
                cm.getPlayer().forceReAddItem(selectedItem, Packages.client.inventory.MapleInventoryType.getByType(1));

                var chat = "#fs15#";
                chat += "#b#e■ 아이템 강화에 성공하셨습니다. #n#k" + enter;
                chat += "#fc0xFFD5D5D5#─────────────────────────#k" + enter;
                chat += "#d" + "현재 아이템 강화 등급 : #b" + selectedItem.getOwner() + enter;
                chat += "#d" + "변경된 능력치 : [DEX] #k" + beforeOption + " + #b15" + enter + enter;

                chat += cm.getPlayer().getName() + "님 이용해주셔서 감사합니다. 오늘도 PRAY에서 좋은 하루 보내세요.";
                cm.sendOk(chat);
                cm.gainItem(need, -15);

                cm.dispose();
                return;
            } else {
                var chat = "#fs15#";
                chat += "아쉽지만 강화에 실패하였습니다."+ enter;
                 chat += "다음 기회에 다시 도전해주세요!" + enter + enter;
                chat += "#b" + cm.getPlayer().getName() + "님 이용해주셔서 감사합니다. 오늘도 PRAY에서 좋은 하루 보내세요.";
                cm.gainItem(need, -15);
                cm.sendOk(chat);
                cm.dispose();
                return;
            }
        } else {
            cm.sendOk("#fs15#강화에 필요한 아이템이 부족합니다.");
            cm.dispose();
            return;
        }
        return;
    }
}