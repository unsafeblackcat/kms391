/*
제작 : 판매요원 (267→295)
용도 : 판매요원 팩
*/

status = -1;
검정 = "#fc0xFF191919#"
파랑 = "#fc0xFF4641D9#"
남색 = "#fc0xFF4641D9#"
초록 = "#fc0xFF0BC904#"
importPackage(java.lang);
importPackage(Packages.tools.packet);
importPackage(Packages.client.inventory);
var questt = "tuto3"; // jump_고유번호
var 서버이름 = "몽[夢]";
function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 4) {
            qm.sendOkS("이런.. 언제라도 마음이 바뀌면 다시 찾아오게나.", 4, 9401232);
            qm.dispose();
            return;
        } else {
            status--;
        }
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        qm.sendNextS("이번엔 #r클론 아바타#k에 대해서 알려주겠네!", 0x04, 9401232);
    } else if (status == 1) {
        qm.sendNextPrevS("#r클론 아바타#k란 무엇이냐? 캐시 아이템이지만 특별한 세트 옵션을 가지고 있다네!\r\n\r\n"+남색+"#i1012949##i1012950##i1012951##i1012952##i1012953\r\n\r\n#i1022699##i1022700##i1022701##i1022702##i1022703#\r\n#i1032799##i1032800##i1032801##i1032802##i1032803#\r\n위 아이템은 클론 아바타의 종류라네!", 0x04, 9401232);
    } else if (status == 2) {
        qm.sendNextPrevS("#r클론 아바타#k는 기본적으로 장착 시, #b투명 상태#k를 유지하게 된다네. 대신, #r캐시 모루#k기능을 이용 할 수 있어서 옵션과 코디 두마리의 토끼를 다 잡을 수 있는 좋은 조건 아닌가!", 0x04, 9401232);
    } else if (status == 3) {
        qm.sendOkS("#e[#b컨텐츠시스템#k -> #r클론 강화#k]#n에가서 클론 아바타 종류를 확인 해보게!", 4, 9401232);
        qm.forceStartQuest(true);
        qm.dispose();
    }
}

status = -1;
function end(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 4) {
            qm.sendOkS("나는 항상 같은자리에 있으니 언제라도 다시 말을 걸어주게.");
            qm.dispose();
            return;
        } else {
            status--;
        }
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        qm.sendNextS("#e클론 아바타#n를 보니 어떤가? 스펙업에 욕구가 생겼지 않는가? 크크크. #b"+서버이름+" 스킬#k, #r치장 강화#k 만큼이나 중요한 스펙업 수단이라네! 기회가 된다면 강화도 한번 해보게!", 0x04, 9401232);
    } else if (status == 1) {
        말 = "아래의 보상을 준비해봤다네.\r\n\r\n"
        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n";
        말 += "#i4310012:# #b#t4310012:# 100개\r\n";
        말 += "#i2433928:# #b#t2433928:# 3개\r\n";
        qm.sendNextS(말, 0x04, 9401232);
    } else if (status == 2) {
        if (qm.getClient().getKeyValue(questt) == null)
            qm.getClient().setKeyValue(questt, "0");

        if (Integer.parseInt(qm.getClient().getKeyValue(questt)) > 0) {
            qm.sendOk("#fs15#" + 검정 + "이미 보상을 받으셨습니다.", 9010061);
            qm.dispose();
            qm.forceCompleteQuest(true);
            return;
        }
        leftslot = qm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        leftslot1 = qm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot();
        if (leftslot < 1 && leftslot1 < 1) {
            qm.sendOkS("#r#h #! 소비창과 기타창 한칸 이상을 비워두게!", 0x04, 9401232);
            qm.dispose();
            return;
        }
        qm.gainItem(4310012, 100);
        qm.gainItem(2433928, 3);
        qm.forceCompleteQuest(true);
        qm.dispose();
    }
}


function GlowQuest(start) {
    var str = qm.getClient().getKeyValue("GrowQuest");
    var ab = str.split("");
    var fi = "";
    ab[3] = start+"";
    for (var a = 0; a < ab.length; a++) {
        fi += ab[a];
    }
    qm.getClient().setKeyValue("GrowQuest", fi);
}
