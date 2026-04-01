function act() {
    var em = rm.getEventManager("MinervaPQ");
    var eim = rm.getPlayer().getEventInstance();
    if (em == null || eim == null) {
        rm.mapMessage(5, "정상적으로 입장을 하지 않아 리액터가 정상 작동하지 않습니다. 운영자께 문의해주세요.");
        return;
    }
        rm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
        em.setProperty("stage1r_clear", "1");
        rm.getPlayer().dropMessageGM(6,"알맞은 음악이 재생되었습니다. 이크에게 말을 걸어 첫번째 조각을 받으세요.");
}