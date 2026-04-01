


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    델이 에 의해 만들어 졌습니다.

    엔피시아이디 : 9001153

    엔피시 이름 : Mr.보체

    엔피시가 있는 맵 : 디지털 호라이즌 : 배틀 그라운드(퇴장) (921174002)

    엔피시 설명 : 전투 프로그램


*/
importPackage(Packages.constants);

var status = -1;
var score = 0, coin = 0, sale = 0;
var exit = false;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        /*if (cm.getPlayer().getV("BattlePVPRank") == null || cm.getPlayer().getV("BattlePVPLevel") == null || cm.getPlayer().getV("BattlePVPKill") == null) {
            cm.sendOkS("#b#e<싸워라! 전설의 귀환>#n#k\r\n\r\n이런 #h #님 전장에서 성과를 거둔것이 없군요. 다음 전투땐 꼭 성과를 거두셔서 보상을 받아가시길 바랍니다.",4 , 9001153);
            exit = true;
        } else if (parseInt(cm.getPlayer().getV("BattlePVPLevel")) < 10 && parseInt(cm.getPlayer().getV("BattlePVPKill")) < 3) {
            cm.sendOkS("#b#e<싸워라! 전설의 귀환>#n#k\r\n\r\n이런 #h #님 전장에서 성과를 거둔것이 없군요. 다음 전투땐 꼭 성과를 거두셔서 보상을 받아가시길 바랍니다.",4 , 9001153);
            exit = true;
        } else {*/
            exit = false;
            cm.sendYesNoS("#b#e<싸워라! 전설의 귀환>#n#k\r\n\r\n힘든 전투였군요 #h #님 전장에서 #r#e" + (cm.getPlayer().getV("BattlePVPRank")) + "등#n#k을 하셨습니다! 보상을 받아가시겠어요?", 4, 9001153)
        //}
    } else if (status == 1) {
        if (exit) {
            cm.warp(100000051);
            cm.getPlayer().removeV("returnM")
            cm.getPlayer().removeV("BattlePVPRank")
            cm.getPlayer().removeV("BattlePVPLevel")
            cm.getPlayer().removeV("BattlePVPKill")
            cm.dispose();
        } else {
            rank = parseInt(cm.getPlayer().getV("BattlePVPRank"));
            if (rank == 1) {
                score = 500;
                sale = 5;
                coin = 100;
            } else if (rank == 2) {
                score = 300;
                sale = 4;
                coin = 80;
            } else if (rank == 3) {
                score = 100;
                sale = 3;
                coin = 50;
            } else {
                score = 50;
                sale = 1;
                coin = 30;
            }
            cm.sendYesNoS("#b#e<싸워라! 전설의 귀환>#n#k\r\n\r\n정말 #r#e"+ (cm.getPlayer().getV("BattlePVPRank")) +"등#n#k 보상을 수령 하시겠어요?\r\n\r\n#b#i5121041# 주간 길드포인트"+score+"", 4, 9001153);
        }
    } else if (status == 2) {
        cm.warp(cm.getPlayer().getV("returnM") != null ? parseInt(cm.getPlayer().getV("returnM")) : ServerConstants.WarpMap);
        cm.getPlayer().removeV("returnM")
        if (cm.getPlayer().getV("BattlePVPRank") != null) {
            rank = parseInt(cm.getPlayer().getV("BattlePVPRank"));
            if (cm.getPlayer().getGuildId() > 1) {
                cm.getPlayer().getGuild().setGuildScore(cm.getPlayer().getGuild().getGuildScore() + score);
                cm.getPlayer().dropMessage(5, "<싸워라! 전설의 귀환> 주간 길드포인트 "+score+" 획득!");
            } else {
                cm.getPlayer().dropMessage(5, "<싸워라! 전설의 귀환> 길드에 가입되있지 않아서 길드 포인트 획득이 불가능 합니다.");
            }
            cm.getPlayer().dropMessage(5, "블라썸 티켓 "+sale+"개와 분홍콩 "+coin+"개를 지급하였습니다.");
            cm.gainItem(4310013, sale);
            cm.getPlayer().AddCoin(100828, coin, true);
        }
        cm.getPlayer().removeV("BattlePVPRank")
        cm.getPlayer().removeV("BattlePVPLevel")
        cm.getPlayer().removeV("BattlePVPKill")
        cm.dispose();
    }
}
