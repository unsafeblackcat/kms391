function act() {
	var now = Number(rm.getPlayer().getEventInstance().getProperty("stage4_box"));
	var box1 = rm.getPlayer().getEventInstance().getProperty("stage4_box")
	var box2 = rm.getPlayer().getEventInstance().getProperty("stage4_box_ran")
	rm.getPlayer().getEventInstance().setProperty("stage4_box", ""+(now+1));
	if(box1 == box2) {
		rm.dropSingleItem(4001134);
	}
}