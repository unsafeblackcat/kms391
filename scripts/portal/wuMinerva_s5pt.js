var random0, random1, random2, random3, random4, random5, random6, random7, random8;

var stage1 = false;

	if(random0 == null){
		random0 = Packages.server.Randomizer.rand(1, 4);
		random1 = Packages.server.Randomizer.rand(1, 4);
		random2 = Packages.server.Randomizer.rand(1, 4);
		random3 = Packages.server.Randomizer.rand(1, 4);
		random4 = Packages.server.Randomizer.rand(1, 4);
		random5 = Packages.server.Randomizer.rand(1, 4);
		random6 = Packages.server.Randomizer.rand(1, 4);
		random7 = Packages.server.Randomizer.rand(1, 4);
	}

function enter(pi) {
	pi.getPlayer().getMap().setrpportal(random0);
	pi.getPlayer().dropMessage(5, random0+"/"+random1+"/"+random2+"/"+random3+"/"+random4+"/"+random5+"/"+random6+"/"+random7);
    if (pi.getPortal().getName().startsWith("rp01" + random0)) {
		pi.getPlayer().getClient().getSession().writeAndFlush(Packages.tools.packet.CField.instantMapWarp(pi.getPlayer(), pi.getPlayer().getMap().getPortal("rp024").getId()));
		pi.getPlayer().getMap().movePlayer(pi.getPlayer(), new java.awt.Point(pi.getPlayer().getMap().getPortal("rp024").getPosition()));
		pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("an1"+ random0, 2));
		pi.getPlayer().getMap().setrpportal(random1);
    } else if (pi.getPortal().getName().startsWith("rp02" + random1)) {
		pi.getPlayer().getClient().getSession().writeAndFlush(Packages.tools.packet.CField.instantMapWarp(pi.getPlayer(), pi.getPlayer().getMap().getPortal("rp034").getId()));
		pi.getPlayer().getMap().movePlayer(pi.getPlayer(), new java.awt.Point(pi.getPlayer().getMap().getPortal("rp034").getPosition()));
		pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("an2"+ random1, 2));
		pi.getPlayer().getMap().setrpportal(random2);
    } else if (pi.getPortal().getName().startsWith("rp03" + random2)) {
		pi.getPlayer().getClient().getSession().writeAndFlush(Packages.tools.packet.CField.instantMapWarp(pi.getPlayer(), pi.getPlayer().getMap().getPortal("rp044").getId()));
		pi.getPlayer().getMap().movePlayer(pi.getPlayer(), new java.awt.Point(pi.getPlayer().getMap().getPortal("rp044").getPosition()));
		pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("an3"+ random2, 2));
		pi.getPlayer().getMap().setrpportal(random3);
    } else if (pi.getPortal().getName().startsWith("rp04" + random3)) {
		pi.getPlayer().getClient().getSession().writeAndFlush(Packages.tools.packet.CField.instantMapWarp(pi.getPlayer(), pi.getPlayer().getMap().getPortal("pt00").getId()));
		pi.getPlayer().getMap().movePlayer(pi.getPlayer(), new java.awt.Point(pi.getPlayer().getMap().getPortal("pt00").getPosition()));
		pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("an4"+ random3, 2));
		pi.getPlayer().getMap().setrpportal(random4);
		stage1 = true;
    } else if (pi.getPortal().getName().startsWith("rp05" + random4)) {
		pi.getPlayer().getClient().getSession().writeAndFlush(Packages.tools.packet.CField.instantMapWarp(pi.getPlayer(), pi.getPlayer().getMap().getPortal("rp064").getId()));
		pi.getPlayer().getMap().movePlayer(pi.getPlayer(), new java.awt.Point(pi.getPlayer().getMap().getPortal("rp064").getPosition()));
		pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("an5"+ random4, 2));
		pi.getPlayer().getMap().setrpportal(random5);
    } else if (pi.getPortal().getName().startsWith("rp06" + random5)) {
		pi.getPlayer().getClient().getSession().writeAndFlush(Packages.tools.packet.CField.instantMapWarp(pi.getPlayer(), pi.getPlayer().getMap().getPortal("rp074").getId()));
		pi.getPlayer().getMap().movePlayer(pi.getPlayer(), new java.awt.Point(pi.getPlayer().getMap().getPortal("rp074").getPosition()));
		pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("an6"+ random5, 2));
		pi.getPlayer().getMap().setrpportal(random6);
    } else if (pi.getPortal().getName().startsWith("rp07" + random6)) {
		pi.getPlayer().getClient().getSession().writeAndFlush(Packages.tools.packet.CField.instantMapWarp(pi.getPlayer(), pi.getPlayer().getMap().getPortal("rp084").getId()));
		pi.getPlayer().getMap().movePlayer(pi.getPlayer(), new java.awt.Point(pi.getPlayer().getMap().getPortal("rp084").getPosition()));
		pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("an7"+ random6, 2));
		pi.getPlayer().getMap().setrpportal(random7);
    } else if (pi.getPortal().getName().startsWith("rp08" + random7)) {
		pi.getPlayer().getClient().getSession().writeAndFlush(Packages.tools.packet.CField.instantMapWarp(pi.getPlayer(), pi.getPlayer().getMap().getPortal("pt01").getId()));
		pi.getPlayer().getMap().movePlayer(pi.getPlayer(), new java.awt.Point(pi.getPlayer().getMap().getPortal("pt01").getPosition()));
		pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("an8"+ random7, 2));
    } else if(stage1 == false){
		pi.getPlayer().getClient().getSession().writeAndFlush(Packages.tools.packet.CField.instantMapWarp(pi.getPlayer(), pi.getPlayer().getMap().getPortal("rp014").getId()));
		pi.getPlayer().getMap().movePlayer(pi.getPlayer(), new java.awt.Point(pi.getPlayer().getMap().getPortal("rp014").getPosition()));
    } else if(stage1 == true){
		pi.getPlayer().getClient().getSession().writeAndFlush(Packages.tools.packet.CField.instantMapWarp(pi.getPlayer(), pi.getPlayer().getMap().getPortal("pt00").getId()));
		pi.getPlayer().getMap().movePlayer(pi.getPlayer(), new java.awt.Point(pi.getPlayer().getMap().getPortal("pt00").getPosition()));
    }
}
