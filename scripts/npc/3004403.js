var status;
var select;

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode < 0) {
        cm.dispose();
    return;
    } else {
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            var text = "#fs15##b擁有附加方塊碎片 #r#c2430915#個#k. \r\n附加方塊碎片可在 #r10個中兌換1個#k.\r\n\r\n";
                text += "#b#L0##i5062500# #z5062500##l\r\n";
        //text += "#b#L1##i4319995# #z4319995##l\r\n";
        //text += "#b#L2##i4319996# #z4319996##l";
            cm.sendSimple(text);
        } else if(status == 1) {
            if (selection == 0) {
                if (cm.haveItem(2430759, 50)) {
            cm.sendYesNo("#fs15#要兌換 #b#i5062500# #z5062500##k?");
            select = 0;
                } else {
                    cm.sendNext("#fs15#對不起 #b#z2430915##k 好像不够.");
            cm.dispose();
                }
            } else if (selection == 1) {
                if (cm.haveItem(2430759, 50)) {
            cm.sendYesNo("#fs15#要換成 #b#i4319995# #z4319995##k?");
            select = 1;
        } else {
                    cm.sendNext("#fs15#對不起， #b#z2430759##k이 好像不够.");
            cm.dispose();
        }
        } else if (selection == 2) {
                if (cm.haveItem(2430759, 50)) {
            cm.sendYesNo("#fs15#정말 #b#i4319996# #z4319996##k로 바꾸시겠습니까?");
            select = 2;
        } else {
                    cm.sendNext("#fs15#죄송하지만 #b#z2430759##k이 충분하지 않으신것 같네요.");
            cm.dispose();
        }        
            } else {
                cm.dispose();
            }
    } else if (status == 2) {
            if (select == 0) {
                if (cm.canHold(2049501)) {
                    cm.gainItem(2430915, -10);
                    cm.gainItem(5062500, 1);
            cm.sendNext("#fs15#交換已完成.");
                } else {
                    cm.sendNext("#fs15#抱歉，好像沒有足够的庫存空間。 #b消耗#k請清空標籤上的庫存空間.");
                }
                cm.dispose();
            } else if (select == 1) {
        if (cm.canHold(2049300)) {
                    cm.gainItem(2430759, -50);
                    cm.gainItem(4319995, 1);
                } else {
                    cm.sendNext("#fs15#죄송하지만 인벤토리 공간이 충분하지 않으신 것 같네요. #b소비#k탭의 인벤토리 공간을 비워주세요.");
                }
                cm.dispose();
        } else if (select == 2) {
        if (cm.canHold(2049701)) {
                    cm.gainItem(2430759, -50);
                    cm.gainItem(4319996, 1);
                } else {
                    cm.sendNext("#fs15#죄송하지만 인벤토리 공간이 충분하지 않으신 것 같네요. #b캐시#k탭의 인벤토리 공간을 비워주세요.");
                }
                cm.dispose();        
            } else {
                cm.dispose();
            }
        } else { 
            cm.dispose();
        }
    }
}