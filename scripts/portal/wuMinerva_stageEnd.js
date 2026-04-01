var mob =[
[9300931, 903, -795],
[9300931, 1042, -794],
[9300931, 1208, -796],
[9300931, 1379, -796],
[9300931, 1566, -802],
[9300931, -1405, -735],
[9300931, -1232, -735],
[9300931, -1078, -740],
[9300931, -881, -737],
[9300931, -686, -735],
[9300932, -1377, -320],
[9300932, -1221, -317],
[9300932, -1038, -312],
[9300932, -853, -311],
[9300932, -683, -317],
[9300932, 827, -313],
[9300932, 1027, -311],
[9300932, 1214, -314],
[9300932, 1409, -312],
[9300932, 1608, -311],
[9300933, 1539, -1281],
[9300933, 1025, -1275],
[9076131, 552, -138],
[9300933, -1035, -1164],
[9300933, -683, -1155],
[9300933, -1404, -1158],
[9300933, -859, -1155],
[9300933, -1215, -1162],
[9300933, 1367, -1284],
[9300933, 1203, -1280],
[9300933, 861, -1280],
]


function enter(pi) {
    em = pi.getEventManager("MinervaPQ");
    if(pi.getMapId() == 933032000 && em.getProperty("stage1r_clear") == "true1"){
        pi.resetMap(933033000);
        pi.warpParty(933033000);
        var a = 1;
        while(a < 6){
            pi.getPlayer().getMap().spawnMonsterWithEffectBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300041), new java.awt.Point(-30, -503), 12);
            pi.getPlayer().getMap().spawnMonsterWithEffectBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300042), new java.awt.Point(-30, -715), 12);
            pi.getPlayer().getMap().spawnMonsterWithEffectBelow(Packages.server.Luna.MapleLunaFactory.getMonster(9300043), new java.awt.Point(-30, -907), 12);
            a++;
        }
    } else if(pi.getMapId() == 933033000 && em.getProperty("stage2r_clear") == "true1"){
        pi.resetMap(933034000);
        pi.warpParty(933034000);
        for(var i = 0; i<mob.length; i++){
            pi.getPlayer().getMap().spawnMonsterWithEffectBelow(Packages.server.Luna.MapleLunaFactory.getMonster(mob[i][0]), new java.awt.Point(mob[i][1], mob[i][2]), 12);
        }
    } else if(pi.getMapId() == 933034000 && em.getProperty("stage3r_clear") == "true1"){
        pi.resetMap(933035000);
        pi.warpParty(933035000);
    } else if(pi.getMapId() == 933035000 && em.getProperty("stage4r_clear") == "true1"){
        em.setProperty("stage5r_m", "");
        em.setProperty("stage5r_clear", "false");
        em.setProperty("stage6r_clear", "false");
        em.setProperty("stage7r_clear", "false");
        pi.resetMap(933036000);
        pi.warpParty(933036000, 3);
    } else if(pi.getMapId() == 933037000 && em.getProperty("stage6r_clear") == "true1"){
        pi.warpParty(933036000, 3);
    } else if(pi.getMapId() == 933037000 && em.getProperty("stage6r_clear") != "true1"){
        pi.getPlayer().dropMessage(5, "시종 이크에게 말을 걸어 생명의 풀을 획득해주세요.");
    } else {
        pi.getPlayer().dropMessage(5, "시종 이크에게 말을 걸어 여신상의 조각을 획득해주세요.");
    }
}