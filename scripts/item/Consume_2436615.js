/*
제작자 : 백란(vmfhvlfqhwlak@nate.com)
수정자 : 타임(time_amd@nate.com)
*/
importPackage(Packages.server);
importPackage(Packages.database);
importPackage(Packages.client.inventory);
importPackage(Packages.tools);
importPackage(java.lang);
importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(Packages.server);
importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(Packages.client.inventory);
importPackage(Packages.server);
importPackage(Packages.server.items);

one = Math.floor(Math.random() * 10) + 1 // 최소 10 최대 35 , 혼테일
two = Math.floor(Math.random() * 6) + 1 // 최소 5, 최대 15, 혼테일
three = Math.floor(Math.random() * 1) + 1 // 최소 10 최대 35 , 혼테일
four = Math.floor(Math.random() * 40) + 1 // 최소 10 최대 35 , 혼테일
five = Math.floor(Math.random() * 3) + 1 // 최소 10 최대 35 , 혼테일
six = Math.floor(Math.random() * 2) + 1 // 최소 10 최대 35 , 혼테일
seven = Math.floor(Math.random() * 1) + 1 // 최소 10 최대 35 , 혼테일
seven2 = Math.floor(Math.random() * 1) + 1 // 최소 10 최대 35 , 혼테일
seven3 = Math.floor(Math.random() * 3) + 1 // 최소 10 최대 35 , 혼테일
seven4 = Math.floor(Math.random() * 2) + 1 // 최소 10 최대 35 , 혼테일
seven5 = Math.floor(Math.random() * 3) + 1 // 최소 10 최대 35 , 혼테일
seven6 = Math.floor(Math.random() * 22) + 7 // 최소 10 최대 35 , 혼테일
sevena = Math.floor(Math.random() * 22) + 7 // 최소 10 최대 35 , 혼테일
seven7 = Math.floor(Math.random() * 75) + 20 // 최소 10 최대 35 , 혼테일
seven8 = Math.floor(Math.random() * 75) + 20 // 최소 10 최대 35 , 혼테일
seven9 = Math.floor(Math.random() * 4) + 1 // 최소 10 최대 35 , 혼테일
meso = Math.floor(Math.random() * 40000000) + 20000000 * 0.5 // 최소 10 최대 35 , 혼테일

var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
        cm.dispose();
    }
    if (status == 0) {
        cm.gainItem(2436615, -1);//아이템 사라지게
        cm.gainMeso(meso);
        cm.sendOk("#fs15# #z2436615#에서 " + Comma(meso) + "#k#n 메소를 얻었습니다!!");
        //World.Broadcast.broadcastMessage(CWvsContext.serverNotice(5, cm.getPlayer().getName() + "님이 중급 유니온 메소 박스에서 " + meso + " 메소를 얻었습니다!"));
        cm.dispose();
    }
}

function Comma(i) {
    var reg = /(^[+-]?\d+)(\d{3})/;
    i += '';
    while (reg.test(i))
        i = i.replace(reg, '$1' + ',' + '$2');
    return i;
}