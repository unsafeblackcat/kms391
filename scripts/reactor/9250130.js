importPackage(java.lang);
importPackage(java.util);

function act() {
    var em = rm.getEventManager("MinervaPQ");
    var eim = rm.getPlayer().getEventInstance();
    if (em == null || eim == null) {
        rm.mapMessage(5, "정상적으로 입장을 하지 않아 리액터가 정상 작동하지 않습니다. 운영자께 문의해주세요.");
        return;
    }
        em.setProperty("stage7r_clear", "true")
        //rm.gainItem(4001830, -1);
        rm.getPlayer().removeItem(4001830, -1);
        rm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.startMapEffect("여신이 부활하였습니다. 여신 미네르바와 대화해 주세요.", 5120019, true));
        rm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
        rm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.achievementRatio(100));
        rm.getPlayer().getMap().spawnNpc2(9076133, new java.awt.Point(-46, -897));
}