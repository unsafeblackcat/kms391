
h = "#fUI/UIMiniGame.img/starPlanetRPS/heart#";
g = "#fMap/MapHelper.img/minimap/anothertrader#";
a = "#i3801317#"
b = "#i3801313#"
c = "#i3801314#"
d = "#i3801315#"
p = "#fc0xFFF781D8#"

logo = "#fUI/Logo.img/Script/logo4#";
창고 = "#fUI/Logo.img/Script/3/0#";
캐시삭제 = "#fUI/Logo.img/Script/3/1#";
랭킹 = "#fUI/Logo.img/Script/3/2#";
선물 = "#fUI/Logo.img/Script/3/3#";
위장색 = "#fUI/Logo.img/Script/3/4#";
스킬훔치기 = "#fUI/Logo.img/Script/3/5#";

mTown = ["SixPath", "Henesys", "Ellinia", "Perion", "KerningCity",
	 "Rith", "Dungeon", "Nautilus", "Ereb", "Rien",
	 "Orbis", "ElNath", "Ludibrium", "Folkvillige", "AquaRoad",
	 "Leafre", "Murueng", "WhiteHerb", "Ariant", "Magatia",
	 "Edelstein", "Eurel", "critias", "Haven", "Road of Vanishing", "ChewChew", "Lacheln", "Arcana", "Morass", "esfera", "aliance", "moonBridge", "TheLabyrinthOfSuffering", "Limen"];

cTown = [104020000, 120040000, 101000000, 102000000, 103000000,
	 104000000, 105000000, 120000000, 130000000, 140000000,
	 200000000, 211000000, 220000000, 224000000, 230000000,
	 240000000, 250000000, 251000000, 260000000, 261000000,
	 310000000, 101050000, 241000000, 310070000, 450001000, 450002000, 450003000, 450005000, 450006130, 450007040, 450009050, 450009100, 450011500, 450012300];

var enter = "\r\n";
var status = -1;

function start()
{
	 status = -1;	
	 action (1, 0, 0);
}

function action(mode, type, selection)
{
	if (mode == -1)
	{
		cm.dispose();
		return;
	}
	if (mode == 0)
	{
		status --;
	}
	if (mode == 1)
	{
		status++;
	}

	if (status == 0) {
// 1269
      var msg = "                 "+logo+""+enter;
      msg += "#L1#"+창고+"#l";
      msg += "#L4#"+캐시삭제+"#l";
      msg += "#L8#"+랭킹+"#k#l\r\n";
      //msg += "#L6#"+선물+"#l";
      //msg += "#L13#"+위장색+"#l";
      //msg += "#L7#"+스킬훔치기+"#l" + enter;
      cm.sendSimpleS(msg, 0x2);
	 } else if(status == 1)
	{
		ans_01 = selection;
		selStr = "";
		switch(ans_01)
		{
			case 5:
			cm.dispose();
			cm.warp(200000301,0);
			break;
//순서대로 칸임
			case 1: // 첫번째칸
            cm.dispose();
            cm.openNpc(9062339);
			break;

			case 2:
            cm.dispose();
            cm.openNpc(1540016);
            break;

			case 13: // 5번째칸
			cm.dispose();
			cm.openNpc(1105008);                     
			break;
			case 4:  //2번째칸
                cm.dispose();
                cm.openNpc(1012121);  
             break;
			case 6: // 4번째칸
			cm.dispose();
            cm.openNpc(9010009);  
			break;
			case 7: //6번째칸
			cm.dispose();
            cm.openNpc(2005);  
			break;
			case 10:
			cm.dispose();
			cm.warp(3000400, 0);
			return;

			case 11:
			cm.dispose();
			cm.getClient().getSession().write(Packages.tools.packet.CField.UIPacket.closeUI(62));
			cm.warp(925020000, 0);
			return;

			case 8:
			cm.dispose();
			cm.openNpc(9076004);
			return;

			case 3:
			cm.dispose();
			cm.openNpc(9020011);
			return;

		}
		if (ans_01 == 2 || ans_01 == 3)
		cm.sendSimple(selStr, 2510022);
	}


		}


