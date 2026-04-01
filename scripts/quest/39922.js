importPackage(Packages.server.quest);
importPackage(Packages.client.inventory);
var status = -1;
var str, ab;
var replace = false, change = false;
var questName = "Authentic2"
var quest1 = 39923;//퀘스트최소대역
var quest2 = 39928;//퀘스트최대대역
var questcount = 5;//퀘스트 몇개줄건지
var questarray = [];
var practicearray = [];
//세르니움조사
function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0 && !change) {
        if (status == 1) {
            qm.sendOk("변덕이 심하군. 임무를 받으려면 나에게 다시 말을 걸어라.");
            qm.dispose();
            return;
        }
        status += 2;
    }
    if (mode == 1 && !change) {
        status++;
    }

    if (status == 0) {
        if (qm.getPlayer().getV(questName) == null) {
            qm.SelectQuest(questName, quest1, quest2, questcount);
        }
        txt = "원하는 임무 선택해라.\r\n\r\n"
        str = qm.getPlayer().getV(questName);
        ab = str.split(",");
        for (var a = 0; a < ab.length; a++) {
            txt += "#L"+a+"##b#e#y" + ab[a] + "##k#n\r\n"
        }
        qm.sendSimple(txt);
    } else if (status == 1) {
            //수행
            qm.sendOk("임무가 끝나시거든 제게 오셔서 완료하시면 돼요. 꼭 오늘 자정까지 오셔야 합니다. 그럼 안녕히 다녀오세요.");
            startQuest(selection)
    }
}


function startQuest(questnum) {
    str = qm.getPlayer().getV(questName);
    ab = str.split(",");
    MapleQuest.getInstance(ab[questnum]).forceStart(qm.getPlayer(), 0, null);
    qm.forceStartQuest();
    qm.dispose();
}

// function end(mode, type, selection) {

//     if (mode == -1) {
//         qm.dispose();
//         return;
//     }
//     if (mode == 0) {
//         if (status == 1) {
//             qm.sendOk("나는 항상 같은자리에 있으니 언제라도 다시 말을 걸어주게.");
//             qm.dispose();
//             return;
//         } else {
//             status--;
//         }
//     }
//     if (mode == 1) {
//         status++;
//     }

//     if (status == 0) {
//         leftslot = qm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot();
//         leftslot1 = qm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot();  leftslot2 = qm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
//         if (leftslot < 2 && leftslot1 < 5) {
//             qm.sendOk("인벤토리에 자리가 없군요.");
//             qm.dispose();
//             return;
//         }
//         qm.sendNext("오늘의 명령 " + questcount + "가지를 모두 완수하셨군요! 자, 여기\r\n#i1712006# #z1712006# 5개,\r\n#i2435902# #z2435902# 3개를 드리겠습니다.");
//     } else if (status == 1) {
//         // for (a = 0; a < 5; a++) {
//         //     qm.gainItem(1712006, 1);
//         // }
//         // qm.gainItem(2435902, 3);
//         qm.getPlayer().removeKeyValue(39819);
//         qm.getPlayer().dropMessage(5,"고생요");
//         qm.forceCompleteQuest();
//         qm.dispose();
//     }
// }