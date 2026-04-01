importPackage(Packages.tools.packet);
importPackage(Packages.handling.world);
importPackage(java.lang);
importPackage(java.io);
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.client.inventory);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(Packages.server);
importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);

req = [
    [0, [
        [4310900, 1000]
    ], 0],
    [0, [
        [4310900, 2000]
    ], 0],
    [0, [
        [4310900, 3000]
    ], 0],
    [0, [
        [4310900, 3500]
    ], 0],
    [0, [
        [4310900, 4500]
    ], 0],
    [0, [
        [4310900, 5000]
    ], 0],
    [0, [
        [4310900, 5500]
    ], 0],
    [0, [
        [4310900, 6500]
    ], 0],
    [0, [
        [4310900, 8000]
    ], 0],
    [0, [
        [4310900, 9500]
    ], 0],
    [0, [
        [4310900, 10000]
    ], 0],
    [0, [
        [4310900, 12000]
    ], 0],
    [0, [
        [4310900, 14000]
    ], 0],
    [0, [
        [4310900, 16000]
    ], 0],
    [0, [
        [4310900, 18000]
    ], 0],
    [0, [
        [4310900, 20000]
    ], 0],
    [0, [
        [4310900, 25000]
    ], 0],
    [0, [
        [4310900, 30000]
    ], 0],
    [0, [
        [4310900, 35000]
    ], 0],
    [0, [
        [4310900, 50000]
    ], 0],

]

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode <= 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        if (gK() - 1 >= 20) {
            cm.sendOk("최고단계까지 승급을 하여 더 이상 승급을 하실 수 없습니다.");
            cm.dispose();
            return;
        }

        if (gK() <= 20) {

            말 = "#fs11##fc0xFF990033##e메인랭크 승급 시스템#n#fc0xFF000000#이라네\r\n#b메인랭크 승급#fc0xFF000000#을 통해 더욱 더 강해져보지 않겠나!?\r\n\r\n"
            말 += "#L0##b다음 랭크로 승급하고 싶습니다.#l#k#n\r\n";
         //   말 += "#L1##b랭크의 혜택이 궁금합니다.#l#k#n\r\n\r\n";
            말 += "현재 등급 : " + gK() + "성\r\n";
        }
        if (gK() >= 20) {
            말 = "#fs11#자네는 이미 최대 랭크에 도달했다네";
        }
        말 += "#L1##d설명을 듣는다.#l"
        cm.sendSimple(말);
    } else if (status == 1) {
if(selection == 2 ) {
 cm.getPlayer().setKeyValue(190823, "grade", "0");
cm.dispose();
}
        if (selection == 0) {
            말 = "#fs11#다음 랭크로 승급을 하기 위해선 아래와 같은 재료가 필요하다네\r\n\r\n"
            말 += "#k\r\n";
            for (i = 0; i < req[gK()][1].length; i++) {
                말 += "#i" + req[gK()][1][i][0] + "# #b#z" + req[gK()][1][i][0] + "##r " + req[gK()][1][i][1] + "개#k\r\n"; // 개수 : cm.itemQuantity(req[gK()][1][i][0])
            }

            말 += "#r#e주의 : 아이템을 지급받을 장비 칸과 소비칸을 5칸 이상 비워주게.#k#n\r\n"
            말 += "#r정말 승급을 하겠나?#k"
            cm.sendYesNo(말);
        } else {
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 9062294, "mainrankinfo");
        }
    } else if (status == 2) {
        for (i = 0; i < req[gK()][1].length; i++) {
            if (cm.itemQuantity(req[gK()][1][i][0]) < req[gK()][1][i][1]) {
                cm.sendOk("승급에 필요한 #e재료#n가 부족한게 아닌가?");
                cm.dispose();
                return;
            }
        }
        before = gK();
      
            if (before == 0) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] " + cm.getPlayer().getName() + "님이 1성 으로 승급하셨습니다."));
            }
            if (before == 1) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] " + cm.getPlayer().getName() + "님이 2성 으로 승급하셨습니다."));
            }
            if (before == 2) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] " + cm.getPlayer().getName() + "님이 3성 으로 승급하셨습니다."));
            }
            if (before == 3) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] " + cm.getPlayer().getName() + "님이 4성 으로 승급하셨습니다."));
            }
            if (before == 4) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] " + cm.getPlayer().getName() + "님이 5성 으로 승급하셨습니다."));
            }
            if (before == 5) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] " + cm.getPlayer().getName() + "님이 6성 으로 승급하셨습니다."));
            }
            if (before == 6) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] " + cm.getPlayer().getName() + "님이 7성 으로 승급하셨습니다."));
            }
            if (before == 7) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] " + cm.getPlayer().getName() + "님이 8성 으로 승급하셨습니다."));
            }
            if (before == 8) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] " + cm.getPlayer().getName() + "님이 9성 으로 승급하셨습니다."));
            }
            if (before == 9) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] " + cm.getPlayer().getName() + "님이 10성 으로 승급하셨습니다."));
            }
            if (before == 10) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] " + cm.getPlayer().getName() + "님이 11성 으로 승급하셨습니다."));
            }
            if (before == 11) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] " + cm.getPlayer().getName() + "님이 12성 으로 승급하셨습니다."));
            }
            if (before == 12) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] " + cm.getPlayer().getName() + "님이 13성 으로 승급하셨습니다."));
            }
            if (before == 13) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] " + cm.getPlayer().getName() + "님이 14성 으로 승급하셨습니다."));
            }
            if (before == 14) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] " + cm.getPlayer().getName() + "님이 15성 으로 승급하셨습니다."));
            }
            if (before == 15) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] " + cm.getPlayer().getName() + "님이 16성 으로 승급하셨습니다."));
            }
            if (before == 16) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] " + cm.getPlayer().getName() + "님이 17성 으로 승급하셨습니다."));
            }
            if (before == 17) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] " + cm.getPlayer().getName() + "님이 18성 으로 승급하셨습니다."));
            }
            if (before == 18) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] " + cm.getPlayer().getName() + "님이 19성 으로 승급하셨습니다."));
            }
            if (before == 19) {
                World.Broadcast.broadcastMessage(CField.getGameMessage(8, "[랭크] " + cm.getPlayer().getName() + "님이 20성 으로 승급하셨습니다."));
            }
            말 = "승급에 성공하셨습니다. 아래의 보상이 지급됩니다.\r\n\r\n"
           
            wList = [];
            wList2 = [];
            GainItem(gK());
            GainItem2(gK());
            for (i = 0; i < wList.length; i++) {
                말 += "#i" + wList[i] + ":# #t" + wList[i] + ":# " + wList2[i] + "개#l\r\n";
                cm.gainItem(wList[i], wList2[i]);
            }
 cm.setZodiacGrade(gK() + 1);
            cm.sendOk(말);
  if (Math.floor(Math.random() * 100) < 100) {
	  if (before == 0) {
            cm.gainItem(4310900, -1000);
            cm.getPlayer().gainNeoStone(2000);
        }
        if (before == 1) {
            cm.gainItem(4310900, -2000);
            cm.getPlayer().gainNeoStone(3000);
        }
        if (before == 2) {
            cm.gainItem(4310900, -3000);
            cm.getPlayer().gainNeoStone(4000);
        }
        if (before == 3) {
            cm.gainItem(4310900, -3500);
            cm.getPlayer().gainNeoStone(5000);
        }
        if (before == 4) {
            cm.gainItem(4310900, -4500);
            cm.getPlayer().gainNeoStone(6000);
        }
        if (before == 5) {
            cm.gainItem(4310900, -5000);
            cm.getPlayer().gainNeoStone(6500);
        }
        if (before == 6) {
            cm.gainItem(4310900, -5500);
            cm.getPlayer().gainNeoStone(7500);
        }
        if (before == 7) {
            cm.gainItem(4310900, -6500);
            cm.getPlayer().gainNeoStone(8000);
        }
        if (before == 8) {
            cm.gainItem(4310900, -8000);
            cm.getPlayer().gainNeoStone(9000);
        }
        if (before == 9) {
            cm.gainItem(4310900, -9500);
            cm.getPlayer().gainNeoStone(10000);
        }
	  if (before == 10) {
            cm.gainItem(4310900, -10000);
            cm.getPlayer().gainNeoStone(11000);
        }
        if (before == 11) {
            cm.gainItem(4310900, -12000);
            cm.getPlayer().gainNeoStone(12000);
        }
        if (before == 12) {
            cm.gainItem(4310900, -14000);
            cm.getPlayer().gainNeoStone(13000);
        }
        if (before == 13) {
            cm.gainItem(4310900, -16000);
            cm.getPlayer().gainNeoStone(14000);
        }
        if (before == 14) {
            cm.gainItem(4310900, -18000);
            cm.getPlayer().gainNeoStone(15000);
        }
        if (before == 15) {
            cm.gainItem(4310900, -20000);
            cm.getPlayer().gainNeoStone(16000);
        }
        if (before == 16) {
            cm.gainItem(4310900, -25000);
            cm.getPlayer().gainNeoStone(17000);
        }
        if (before == 17) {
            cm.gainItem(4310900, -30000);
            cm.getPlayer().gainNeoStone(18000);
        }
        if (before == 18) {
            cm.gainItem(4310900, -35000);
            cm.getPlayer().gainNeoStone(19000);
        }
        if (before == 19) {
            cm.gainItem(4310900, -50000);
            cm.getPlayer().gainNeoStone(20000);
        }
        } else {
            cm.sendOk("승급에 실패 하였습니다.")
        }
      
        cm.dispose();
    }
}

function GainItem(i) {
    switch (i) {
        case 0:
            wList.push(2635903);
            wList.push(4001716);
            break;
        case 1:
            wList.push(4001716);
            wList.push(2048717);
            break;
        case 2:
            wList.push(4310218);
            wList.push(2631527);
            break;
        case 3:
            wList.push(4310218);
            wList.push(5060048);
            wList.push(4001716);
            break;
        case 4:
            wList.push(2049398);
            wList.push(2049360);
            wList.push(4001716);
            break;
        case 5:
            wList.push(5062006);
            wList.push(2049398);
            wList.push(4001716);
            break;
        case 6:
            wList.push(5062006);
            wList.push(2644000);
            wList.push(4001716);
            break;
        case 7:
            wList.push(5062006);
            wList.push(2644000);
            wList.push(4001716);
            break;
        case 8:
            wList.push(5062006);
            wList.push(2644000);
            wList.push(4001716);
            break;
        case 9:
            wList.push(2635903);
            wList.push(5062006);
            wList.push(2049376);
            wList.push(4001716);
            break;
        case 10:
            wList.push(2635903);
            wList.push(5062006);
            wList.push(4001716);
            break;
        case 11:
            wList.push(2635909);
            wList.push(5062006);
            wList.push(4001716);
            break;
        case 12:
            wList.push(2635909);
            wList.push(5062006);
            wList.push(4001716);
            break;
        case 13:
            wList.push(2635909);
            wList.push(5062006);
            wList.push(4001716);
            break;
        case 14:
            wList.push(2635909);
            wList.push(5062006);
            wList.push(4001716);
            break;
        case 15:
            wList.push(2635909);
            wList.push(2635903);
            wList.push(5062006);
            wList.push(4001716);
            break;
        case 16:
            wList.push(2635909);
            wList.push(5062006);
            wList.push(4001716);
            break;
        case 17:
            wList.push(2635909);
            wList.push(5062006);
            wList.push(4001716);
            break;
        case 18:
            wList.push(2635909);
            wList.push(5062006);
            wList.push(4001716);
            break;
        case 19:
            wList.push(2635909);
            wList.push(2635903);
            wList.push(2049383);
            wList.push(5062006);
            wList.push(4001716);
            break;
    }
}

function GainItem2(i) {
    switch (i) {
        case 0:
            wList2.push(1);
            wList2.push(1);
            break;
        case 1:
            wList2.push(1);
            wList2.push(100);
            break;
        case 2:
            wList2.push(5);
            wList2.push(50);
            break;
        case 3:
            wList2.push(10);
            wList2.push(5);
            wList2.push(3);
            break;
        case 4:
            wList2.push(1);
            wList2.push(5);
            wList2.push(3);
            break;
        case 5:
            wList2.push(5);
            wList2.push(1);
            wList2.push(5);
            break;
        case 6:
            wList2.push(10);
            wList2.push(1);
            wList2.push(10);
            break;
        case 7:
            wList2.push(15);
            wList2.push(1);
            wList2.push(12);
            break;
        case 8:
            wList2.push(20);
            wList2.push(1);
            wList2.push(13);
            break;
        case 9:
            wList2.push(2);
            wList2.push(30);
            wList2.push(1);
            wList2.push(15);
            break;
        case 10:
            wList2.push(1);
            wList2.push(35);
            wList2.push(18);
            break;
        case 11:
            wList2.push(4);
            wList2.push(40);
            wList2.push(21);
            break;
        case 12:
            wList2.push(6);
            wList2.push(45);
            wList2.push(24);
            break;
        case 13:
            wList2.push(8);
            wList2.push(50);
            wList2.push(27);
            break;
        case 14:
            wList2.push(10);
            wList2.push(50);
            wList2.push(30);
            break;
        case 15:
            wList2.push(15);
            wList2.push(1);
            wList2.push(55);
            wList2.push(33);
            break;
        case 16:
            wList2.push(20);
            wList2.push(60);
            wList2.push(27);
            break;
        case 17:
            wList2.push(25);
            wList2.push(65);
            wList2.push(30);
            break;
        case 18:
            wList2.push(30);
            wList2.push(70);
            wList2.push(33);
            break;
        case 19:
            wList2.push(50);
            wList2.push(2);
            wList2.push(1);
            wList2.push(80);
            wList2.push(39);
            break;
    }
}

function gK() {
    return cm.getPlayer().getKeyValue(190823, "grade");
}