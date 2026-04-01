var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (cm.getPlayer().getKeyValue(20190211, "karuta_3") < 0) {
        cm.getPlayer().setKeyValue(20190211, "karuta_3", 0);
    }
    itemlist = [1003797, 1003798, 1003799, 1003800, 1003801];
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        if (status == 0 && selection < 100) {
            status++;
        }
        if (status == 0 && selection == 101) {
            cm.dispose();
            return;
        }
        status++;
    }
    if (status == 0) {
        if (!cm.canHold(itemlist[0])) {
            cm.sendOk("#fs15#請清空1格以上的裝備視窗後重新使用!");
            cm.dispose();
            return;
        }
        if (cm.itemQuantity(2434586) < 5) {
            cm.sendOk("#fs15#碎片好像不够.");
            cm.dispose();
            return;
        }
        talk = "#b#e幸運時間:#r"+cm.getPlayer().getKeyValue(20190211, "karuta_3")+"%#k#n\r\n\r\n"
        if (cm.getPlayer().getKeyValue(20190211, "karuta_3") >= 100) {
            talk+= "發動#r#e機會時間!#k#n\r\n"
            talk+= "#b#e出現更好的附加選項#k#n的概率新增.\r\n\r\n"
        }
        talk += "#fs15#首先推薦當前職業可穿戴的裝備.\r\n請選擇要收到的防具.\r\n"
        getClass = getClassById(cm.getPlayer().getJob());
        if (getClass == -1) {
            cm.getPlayer().dropMessage(5, "初學者不能使用. 轉職後請再試一次.");
            cm.dispose();
            return;
        }
        talk += "#L" + getClass + "# #i" + itemlist[getClass] + "# #b#z" + itemlist[getClass] + "##k#l\r\n"
        talk += "\r\n#L100##b査看可用全部物品.#k#l\r\n"
        talk += "#L101##b取消使用#k#l"
        cm.sendSimple(talk);
    } else if (status == 1) {
        talk = "#fs15#選擇要接收的防具.\r\n"
        for (i=0; i<itemlist.length; i++) {
            talk+= "#L"+i+"# #i"+itemlist[i]+"# #b#z"+itemlist[i]+"##k#l\r\n";
        }
        cm.sendSimple(talk);
    } else if (status == 2) {
        selected = selection;
        talk = "#fs15#確定想要這個裝備?\r\n"
        talk+= "#i"+itemlist[selected]+"# #z"+itemlist[selected]+"#\r\n\r\n"
        talk+= "#r#e※注意※#k#n\r\n"
        talk+= "一旦將碎片兌換成物品 將抹除#r5#k個碎片, 無法再次兌換成其他物品.\r\n\r\n"
        talk+= "#L0##b是!沒錯.\r\n"
        talk+= "#L1##b重新選擇.";
        cm.sendSimple(talk);
    } else if (status == 3) {
        if (selection == 1) {
            cm.sendOk("#fs15#重新使用碎片.");
            cm.dispose();
            return;
        }
        if (cm.getPlayer().getKeyValue(20190211, "karuta_3") == 100) {
            cm.getPlayer().setKeyValue(20190211, "karuta_3", 0)
        }
        cm.getPlayer().setKeyValue(20190211, "karuta_3", cm.getPlayer().getKeyValue(20190211, "karuta_3") + Packages.server.Randomizer.rand(7,11));
        if (cm.getPlayer().getKeyValue(20190211, "karuta_3") >= 100) {
            cm.getPlayer().setKeyValue(20190211, "karuta_3", 100)
        }
        cm.gainItem(itemlist[selected], 1);
        cm.gainItem(2434586, -5);
        cm.dispose();
    }
}

function getClassById(paramint) {
    getClass = (Math.floor(paramint / 100)) % 10;
    switch (getClass) {
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
            return getClass - 1;
            break;
        case 6:
            return 3;
            break;
        case 7:
            return Math.floor(paramint / 100) == 27 ? 1 : 0;
            break;
        default:
            return -1;
            break;
    }
}