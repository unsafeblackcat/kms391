function act() {
    var em = rm.getEventManager("MinervaPQ");
    var eim = rm.getPlayer().getEventInstance();
    if (em == null || eim == null) {
        rm.mapMessage(5, "정상적으로 입장을 하지 않아 리액터가 정상 작동하지 않습니다. 운영자께 문의해주세요.");
        return;
    }
    rm.dropSingleItem(em.getProperty("stage1r_q3"));
}