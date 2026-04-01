//이브 온라인 전용 스크립트 
//자석펫 랜덤 뽑기

importPackage(Packages.client);
importPackage(java.lang);

var status = -1;
var 엔터 = "\r\n";
var 쌍엔터 = "\r\n\r\n";
var 포인트 = -50000; //사용할 포인트
var 핑크 = "#fc0xFFAF66DF#";
var 검정 = "#fc0xFF000000#";
var colorBlack = "#fc0xFF191919#";

var PetList = [5002561,5002562,5002563];

function start() {
    status = -1;
    action(1,0,0);
}

function action(mode, type, secletion) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    switch(status) {
        case 0:
            var chat = "5기 자석펫 3종! 랜덤 뽑기" + 쌍엔터 + "#k";
            chat += "● " + 핑크 + cm.getPlayer().getName() + 검정 +"님의 홍보 포인트 : "+ 핑크 + cm.getPlayer().getHPoint() + "#k" + 엔터;
            chat += "     └ 1회당 5만 포인트 차감 됩니다" + 엔터;
            //chat += "#L1##b 자석펫 뽑기 리스트" + 쌍엔터;
            chat += "#L2##d뽑기";
            cm.sendSimple(chat);
            break;
        case 1:
            if (secletion == 1) {
                var chat = "#fs15##fc0xFB1B66FF#자석펫 뽑기 리스트#k" + 쌍엔터;
                for (i=0; i < PetList.length; i++) {
                    chat += "#i" + PetList[i] + "#";
                }
                cm.gainPet(5002561, "", 1, 100, 100, 60, 103);
                cm.sendSimple(chat);
                cm.dispose();
            } else if (secletion == 2) {
                펫리스트 = PetList[Math.floor(Math.random() * PetList.length)];
                if (cm.getPlayer().getHPoint() >= 50000) {
                    cm.getPlayer().gainHPoint(포인트);
                    cm.gainPet(펫리스트, "", 1, 100, 100, 60, 103);
                    chat = "과연 어떤 펫이 나왔을까요? 두둥두둥 ! " + 쌍엔터 + "#i" + 펫리스트 + "#" + 핑크 +"#z " + 펫리스트+ "#";
                    cm.sendSimple(chat);
                    cm.dispose();
                } else {
                    cm.sendOk("홍보 포인트가 부족하여 뽑기를 진행 할수 없습니다");
                    cm.dispose();
                }
            }
            break;
        default:
            cm.dispose();
            break;
    }
}