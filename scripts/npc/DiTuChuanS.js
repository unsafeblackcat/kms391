importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

파랑 = "#fc0xFF0054FF#";
연파 = "#fc0xFF6B66FF#";
연보 = "#fc0xFF8041D9#";
보라 = "#fc0xFF5F00FF#";
노랑 = "#fc0xFFEDD200#";
검정 = "#fc0xFF191919#";
화이트 = "#fc0xFFFFFFFF#";

틀 = "#fs15##fUI/Basic.img/actMark/15#";
獵場 = "#k 練級地圖 #r";
村莊 = "#k 村莊地圖 #r";
老闆 = "#k 首領地圖 #r";
疲勞 = "#k 經驗地圖 #r";
跳跳 = "#k 跳跳地圖 #r";
其他 = "#k 自動地圖 #r";
新建 = "#k GMisMap #r";

mTown = ["SixPath", "Henesys", "Ellinia", "Perion", "KerningCity",
	"Rith", "Dungeon", "Nautilus", "Ereb", "Rien",
	"Orbis", "ElNath", "Ludibrium", "Folkvillige", "AquaRoad",
	"Leafre", "Murueng", "WhiteHerb", "Ariant", "Magatia",
	"Edelstein", "Eurel", "critias", "Haven", "Road of Vanishing", "ChewChew", "Lacheln", "Arcana", "Morass", "esfera", "aliance", "moonBridge", "TheLabyrinthOfSuffering", "Limen"];

cTown = [104020000, 100000000, 101000000, 102000000, 103000000,
	104000000, 105000000, 120000000, 130000000, 140000000,
	200000000, 211000000, 220000000, 224000000, 230000000,
	240000000, 250000000, 251000000, 260000000, 261000000,
	310000000, 101050000, 241000000, 310070000, 450001000, 450002000, 450003000, 450005000, 450006130, 450007040, 450009050, 450009100, 450011500, 450012300];


var 별1 = "#fUI/UIWindow.img/ToolTip/WorldMap/StarForce#";
var 별2 = "#fUI/UIWindow.img/ToolTip/WorldMap/ArcaneForce#";
var 별3 = "#fUI/UIWindow.img/ToolTip/WorldMap/AuthenticForce#";
var 티켓 = 4033235;
var 갯수 = 1;
var status = -1;
var enter = "\r\n";
var talkType = 0x86;
var NoramlPass = "Serenity_Noraml_Pass_Info";
var PrimeumPass = "Serenity_Premium_Pass_Info";
var status = -1;

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

        var msg = "                    " + 틀 + "#l\r\n";

		msg += "#fs20##L2##fc0xFFFF3636#" + 獵場 + "#l";
		msg += "#L1##fc0xFFFF3636#" + 村莊 + "#l";
                msg += "#L4##fc0xFFFF3636#" + 疲勞 + "#l";
                msg += "#L6##fc0xFFFF3636#" + 其他 + "#l\r\n";
		msg += "#L3##fc0xFFFF3636#" + 跳跳 + "#l";
		msg += "#L5##fc0xFFFF3636#" + 老闆 + "#l";
		msg += "#L7##fc0xFFFF3636#" + 新建 + "#l\r\n";


		if (GameConstants.isWildHunter(cm.getPlayer().getJob()))
		msg += "#L9##fs15#b捕獲 捷豹#n #k\r\n";
		cm.sendSimpleS(msg, talkType);

	} else if (status == 1) {
		ans_01 = selection;
		selStr = "";
		switch (ans_01) {
			case 1:
				cm.dispose();
				cm.openNpc(3000012);
				break;
			case 5:
		cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9063140, "BOSSCS");
                return;
			case 2:
				selStr += "\r\n";
				selStr += "#L1##fs20# #e練級#n#b 地圖#l";
				selStr += "      #L3#" + 별2 + "#e#b 神秘河#n#b 地圖#l\r\n";
				selStr += "#L2# #e#r星力#n#b 地圖#l";
				selStr += "      #L4#" + 별3 + "#e#b 原初力#n#b 地圖#l\r\n";
				selStr += "";
				cm.sendSimpleS(selStr, talkType);
				break;
			case 3:
				selStr += "#L1# #fs15##r(Fancy)#k #b賭場街機#k#l\r\n\r\n";
				selStr += "───────────────────────────\r\n";
				selStr += "#L4# #fs15##b毅力森林#n #k移動\r\n";
				selStr += "#L5# #b忍耐之林#n #k移動\r\n";
				selStr += "#L6# #b向著高地#n #k移動\r\n";
				selStr += "#L7# #b寵物步道[海涅西斯]#n #k移動\r\n";
				selStr += "#L8# #b寵物步道[魯迪布裡姆]#n #k移動\r\n";
				cm.sendSimpleS(selStr, talkType);
				break;
			case 4:
	                        selStr += "#fs20#\r\n疲勞狩獵場通過疲勞恢復劑恢復後，將消耗 疲勞與普通狩獵場不同 可以獲得多個物品\r\n#fc0xFF000000#如果所有人都使用疲勞度都低，請注意。\r\n#r注意：疲勞度在午夜後重置爲0。\r\n";
	                        selStr += "#b#L321##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.220 #fc0xFFFF9436#萊特硏究所-硏究所A-3地區 #b（下級）#l\r\n";
	                        selStr += "#b#L322##fMap/MapHelper.img/minimap/anothertrader# #fc0xFF6799FF#Lv.260 #fc0xFFFF9436#蒙特鳩硏究所-硏究所203號 #b(中級)#l\r\n";
	                        cm.sendSimpleS(selStr, talkType);
			break;

			case 6:
			        selStr += "#fs20#\r\n自動打怪狩獵場，可掛機自動狩獵。\r\n\r\n";
				selStr += "[自動打怪狩獵場] #b普通#k";
				selStr += "#L2#普通狩獵場#n #k移動\r\n\r\n#l";
				selStr += "[自動打怪狩獵場] #b高級#k ( #r需要入場券#k )";
				selStr += "#L6#高級狩獵場#n #k移動\r\n";
				cm.sendSimpleS(selStr, talkType);
			break;

			case 7:
		cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9063140, "XinCS");
                return;



		}
	if (ans_01 == 2 || ans_01 == 3 || ans_01 == 4 || ans_01 == 6 || ans_01 == 7 || ans_01 == 8)
		cm.sendSimpleS(selStr, 4);

	} else if (status == 2) {
		ans_02 = selection;

		if (ans_01 == 3) {
			switch (ans_02) {
				case 1:
					cm.dispose();
					cm.warp(993050400, 0);
					return;
				case 2:
					cm.dispose();
					cm.warp(221023300, 0);
					break;
				case 321:
					cm.dispose();
					cm.warp(261020700, 0);
					break;
				case 2:
					cm.dispose();
					cm.warp(221023300, 0);
					break;
				case 3:
					cm.dispose();
					cm.warp(300030010, 0);
					return;
					break;
				case 4:
					cm.dispose();
					cm.warp(910530000, 0);
					break;
				case 5:
					cm.dispose();
					cm.warp(910130000, 0);
					return;
					break;
				case 6:
					cm.dispose();
					cm.warp(109040000, 0);
					return;
					break;
				case 7:
					cm.dispose();
					cm.warp(100000202, 0);
					return;
					break;
				case 8:
					cm.dispose();
					cm.warp(220000006, 0);
					return;
					break;

			}
		} else if (ans_01 == 4) {
			switch (ans_02) {
				case 321:
					cm.dispose();
					cm.warp(261020700, 0);
					break;
				case 322:
					cm.dispose();
					cm.warp(261010103, 0);
					break;
				case 1:
					cm.dispose();
					cm.warp(101, 0);
					break;
				case 2:
					cm.dispose();
					cm.warp(102, 0);
					return;
					break;
				case 3:
					cm.dispose();
					cm.warp(103, 0);
					return;
					break;
				case 4:
					cm.dispose();
					cm.warp(109, 0);
					return;
					break;
				case 5:
					cm.dispose();
					cm.warp(105, 0);
					return;
					break;
				case 6:
					cm.dispose();
					cm.warp(100, 0);
					return;
					break;
				case 7:
					cm.dispose();
					cm.warp(106, 0);
					return;
					break;
				case 8:
					cm.dispose();
					cm.warp(107, 0);
					return;
					break;
				case 9:
					cm.dispose();
					cm.warp(108, 0);
					return;
					break;
				case 10:
					cm.dispose();
					cm.warp(104, 0);
					return;
					break;
			}


		} else if (ans_01 == 8) {
			switch (ans_02) {
				case 1:
					cm.dispose();
					cm.warp(910530000, 0);
					break;
				case 2:
					cm.dispose();
					cm.warp(910130000, 0);
					return;
					break;
				case 3:
					cm.dispose();
					cm.warp(109040000, 0);
					return;
					break;
				case 4:
					cm.dispose();
					cm.warp(100000202, 0);
					return;
					break;
				case 5:
					cm.dispose();
					cm.warp(220000006, 0);
					return;
					break;
				case 50:
					cm.dispose();
					cm.warp(701000000, 0);
					return;
					break;
				case 6:
					cm.dispose();
					cm.warp(740000000, 0);
					return;
					break;
				case 7:
					cm.dispose();
					cm.warp(500000000, 0);
					return;
					break;
				case 8:
					cm.dispose();
					cm.warp(910130000, 0);
					return;
					break;
			}
			} if (ans_01 == 6) {
			switch (ans_02) {
				case 2:
					cm.dispose();
					cm.warp(921170004, 0); //기존 일반
					return;
					break;
				case 3:
					cm.dispose();
					cm.warp(13000, 0); //신규 일반
					return;
					break;
				case 4:
					cm.dispose();
					cm.warp(13001, 0);
					return;
					break;
				case 5:
					cm.dispose();
					cm.warp(13002, 0);
					return;
					break;
				case 6:
					cm.dispose();
						cm.warp(921170011, 0); //기존 후원사냥터
					return;
					break;
				case 7:
					cm.dispose();
					if(cm.haveItem(티켓, 갯수)) {
						cm.warp(13004, 0); //신규 후원사냥터
					}
					return;
					break;
				case 8:
					cm.dispose();
					if(cm.haveItem(티켓, 갯수)) {
						cm.warp(13005, 0); 
					}
					return;
					break;
				case 9:
					cm.dispose();
					if(cm.haveItem(티켓, 갯수)) {
						cm.warp(13006, 0); 
					}		
					return;
					break;
				case 10:
					cm.dispose();
					if(cm.haveItem(티켓, 갯수)) {
						cm.warp(13007, 0); 
					}
					return;
					break;
			}
		} else if (ans_01 == 7) {
			switch (ans_02) {
				case 1:
					cm.dispose();
					cm.warp(993016000, 0);
					break;
				case 2:
					cm.dispose();
					cm.warp(993050000, 0);
					return;
					break;
				case 3:
					cm.dispose();
					cm.warp(993110000, 0);
					return;
					break;
				case 4:
					cm.dispose();
					cm.warp(993177000, 0);
					return;
					break;
				case 5:
					cm.dispose();
					cm.warp(993189100, 0);
					return;
					break;
				case 50:
					cm.dispose();
					cm.warp(701000000, 0);
					return;
					break;
				case 6:
					cm.dispose();
					cm.warp(740000000, 0);
					return;
					break;
				case 7:
					cm.dispose();
					cm.warp(500000000, 0);
					return;
					break;
				case 8:
					cm.dispose();
					cm.warp(910130000, 0);
					return;
					break;
			}
		} else {
			selStr = "";
			switch (ans_02) {
				case 1:
					selStr += "#fs20#\r\n請檢査出現在獵人場 #fc0xFF1DDB16##e怪物的平均等級#n#k后移動.#fs15#\r\n";
					//selStr += "#L 931000500 #Lv.0   │ 美洲豹棲息地#l\r\n";
					selStr += "#L 104010000 #Lv.000  │ 維多利亞港外圍#l";
					selStr += "         #L 101020000 #Lv.007  │ 接近風的地方#l\r\n";
					selStr += "#L 100020000 #Lv.010  │ 胞子山丘#l";
					selStr += "                  #L 100040000 #Lv.015  │ 石人寺院入口#l\r\n";
					selStr += "#L 101020401 #Lv.020  │ 風吹之地#l";
					selStr += "                  #L 120040100 #Lv.035  │ 海岸草叢1#l\r\n";
					selStr += "#L 120041800 #Lv.042  │ 巨浪#l";
					selStr += "                        #L 103020420 #Lv.045  │ 2號線第3區間#l\r\n";
					selStr += "#L 102030000 #Lv.055  │ 野猪的領土#l";
					selStr += "               #L 102040301 #Lv.062  │ 第1軍營#l\r\n";
					selStr += "#L 105010000 #Lv.066  │ 寂靜的濕地#l";
					selStr += "               #L 260020600 #Lv.090  │ 沙哈地帶2#l\r\n";
					selStr += "#L 261020400 #Lv.095  │ 硏究所C-2地區#l";
					selStr += "          #L 220020600 #Lv.114 │ 玩具工廠<機房>#l\r\n";
					selStr += "#L 401053001 #Lv.115  │ 暴君城堡3層走廊#l";
					selStr += "         #L 211040600 #Lv.121 │ 高聳的懸崖4#l\r\n";
					selStr += "#L 250020000 #Lv.126  │ 初級修煉場#l";
					selStr += "                #L 251010402 #Lv.130 │ 紅鼻子海盜團老巢2#l\r\n";
					selStr += "#L 240040000 #Lv.136  │ 龍的峽谷#l";
					selStr += "                   #L 271030400 #Lv.176 │ 騎士團第4區域#l\r\n";
					selStr += "#L 221030800 #Lv.179  │ 操縱室1#l";
					selStr += "                    #L 241000213 #Lv.186 │ 開始的悲劇森林3#l\r\n";
					selStr += "#L 273040300 #Lv.196  │ 荒廢的發掘地區4#l";
					selStr += "         #L 241000216 #Lv.200 │ 墮落的魔力森林1#l\r\n";
					selStr += "#L 241000206 #Lv.205  │ 墮落的魔力森林2#l";
					selStr += "        #L 310070140 #Lv.205 │ 機械墳墓山丘4#l\r\n";
					selStr += "#L 24226 #Lv.210  │ 墮落的魔力森林3#l";
					selStr += "        #L 310070210 #Lv.218 │ 天際線1#l\r\n";
					selStr += "#L 310070300 #Lv.218  │ 黑色天堂甲板1#l";
					selStr += "           #L 105300301 #Lv.226 │ 上部樹枝叉路#l\r\n ";
					cm.sendSimpleS(selStr, talkType);
					break;

				case 2:
					selStr += "#fs20#\r\n請確認獵場的 #e#fc0xFFFF9436#星級要求#k#n和 #e#b怪物的平均等級#k#n後再進行移動。\r\n#fs15#";
					selStr += "   #fs15##e#r[神木村]#n\r\n#d";
					selStr += "#L 240010600 ##fc0xFFFF9436#Lv.107#d | " + 별1 + " #fc0xFFFF9436#5   | 天空之巢Ⅱ#l\r\n";
					selStr += "#L 240010520 ##fc0xFFFF9436#Lv.107#d | " + 별1 + " #fc0xFFFF9436#5   | 天空之巢3#l\r\n";
					selStr += "#L 240010510 ##fc0xFFFF9436#Lv.107#d | " + 별1 + " #fc0xFFFF9436#5   | 山羊峽谷2#l\r\n";
					selStr += "#L 240020300 ##fc0xFFFF9436#Lv.109#d | " + 별1 + " #fc0xFFFF9436#15  | 氷冷死亡戰場#l\r\n";
					selStr += "#L 240020210 ##fc0xFFFF9436#Lv.110#d | " + 별1 + " #fc0xFFFF9436#15  | 黑暗與火的戰場#l\r\n";
					selStr += "#L 240020200 ##fc0xFFFF9436#Lv.110#d | " + 별1 + " #fc0xFFFF9436#15  | 暗黑半人馬領土#l\r\n\r\n";
					selStr += "   #fs15##e#r[玩具城]#d#n\r\n";
					selStr += "#L 220060000 ##fc0xFFFF9436#Lv.116#d | " + 별1 + " #fc0xFFFF9436#25  | 杻曲的時間之路<1>#l\r\n";
					selStr += "#L 220070000 ##fc0xFFFF9436#Lv.116#d | " + 별1 + " #fc0xFFFF9436#25  | 遺忘的時間之路<1>#l\r\n";
					selStr += "#L 220060100 ##fc0xFFFF9436#Lv.117#d | " + 별1 + " #fc0xFFFF9436#25  | 杻曲的時間之路<2>#l\r\n";
					selStr += "#L 220070100 ##fc0xFFFF9436#Lv.117#d | " + 별1 + " #fc0xFFFF9436#25  | 遺忘的時間之路<2>#l\r\n";
					selStr += "#L 220060200 ##fc0xFFFF9436#Lv.118#d | " + 별1 + " #fc0xFFFF9436#26  | 杻曲的時間之路<3>#l\r\n";
					selStr += "#L 220070200 ##fc0xFFFF9436#Lv.118#d | " + 별1 + " #fc0xFFFF9436#26  | 遺忘的時間之路<3>#l\r\n";
					selStr += "#L 220060300 ##fc0xFFFF9436#Lv.119#d | " + 별1 + " #fc0xFFFF9436#27  | 杻曲的時間之路<4>#l\r\n";
					selStr += "#L 220070300 ##fc0xFFFF9436#Lv.119#d | " + 별1 + " #fc0xFFFF9436#27  | 遺忘的時間之路<4>#l\r\n";
					selStr += "#L 220060400 ##fc0xFFFF9436#Lv.120#d | " + 별1 + " #fc0xFFFF9436#28  | 杻曲的徊廊#l\r\n";
					selStr += "#L 220070400 ##fc0xFFFF9436#Lv.120#d | " + 별1 + " #fc0xFFFF9436#28  | 遺忘的徊廊#l\r\n\r\n";
					selStr += "   #fs15##e#r[獅子王城堡]#d#n\r\n";
					selStr += "#L 211080400 ##fc0xFFFF9436#Lv.130#d | " + 별1 + " #fc0xFFFF9436#50  | 隱藏庭院1#l\r\n";
					selStr += "#L 211080500 ##fc0xFFFF9436#Lv.132#d | " + 별1 + " #fc0xFFFF9436#50  | 隱藏庭院2#l\r\n";
					selStr += "#L 211080600 ##fc0xFFFF9436#Lv.134#d | " + 별1 + " #fc0xFFFF9436#50  | 隱藏庭院3#l\r\n\r\n";
					selStr += "   #fs15##e#r[氷峰雪域]#d#n\r\n";
					selStr += "#L 211041100 ##fc0xFFFF9436#Lv.132#d | " + 별1 + " #fc0xFFFF9436#50  | 亡者之林Ⅰ#l\r\n";
					selStr += "#L 211041200 ##fc0xFFFF9436#Lv.132#d | " + 별1 + " #fc0xFFFF9436#50  | 亡者之林Ⅱ#l\r\n";
					selStr += "#L 211041300 ##fc0xFFFF9436#Lv.132#d | " + 별1 + " #fc0xFFFF9436#50  | 亡者之林Ⅲ#l\r\n";
					selStr += "#L 211041400 ##fc0xFFFF9436#Lv.132#d | " + 별1 + " #fc0xFFFF9436#50  | 亡者之林Ⅳ#l\r\n";
					selStr += "#L 211042000 ##fc0xFFFF9436#Lv.132#d | " + 별1 + " #fc0xFFFF9436#55  | 試煉的洞穴1#l\r\n";
					selStr += "#L 211042100 ##fc0xFFFF9436#Lv.135#d | " + 별1 + " #fc0xFFFF9436#55  | 試煉的洞穴2#l\r\n";
					selStr += "#L 211042200 ##fc0xFFFF9436#Lv.136#d | " + 별1 + " #fc0xFFFF9436#55  | 試煉的洞穴3#l\r\n\r\n";
					selStr += "   #fs15##e#r[龍的峽谷]#d#n\r\n";
					selStr += "#L 240040300 ##fc0xFFFF9436#Lv.141#d | " + 별1 + " #fc0xFFFF9436#65  | 峽谷西側路#l\r\n";
					selStr += "#L 240040320 ##fc0xFFFF9436#Lv.141#d | " + 별1 + " #fc0xFFFF9436#65  | 黑翼龍巢穴#l\r\n";
					selStr += "#L 240040510 ##fc0xFFFF9436#Lv.150#d | " + 별1 + " #fc0xFFFF9436#65  | 死龍巢穴#l\r\n";
					selStr += "#L 240040511 ##fc0xFFFF9436#Lv.150#d | " + 별1 + " #fc0xFFFF9436#70  | 被遺留的龍之巢穴1#l\r\n";
					selStr += "#L 240040512 ##fc0xFFFF9436#Lv.150#d | " + 별1 + " #fc0xFFFF9436#70  | 被遺留的龍之巢穴2#l\r\n\r\n";
					selStr += "   #fs15##e#r[新葉城]#d#n\r\n";
					selStr += "#L 103041119 ##fc0xFFFF9436#Lv.155#d | " + 별1 + " #fc0xFFFF9436#80  | 2樓Coffee廳<4>#l\r\n";
					selStr += "#L 103041129 ##fc0xFFFF9436#Lv.157#d | " + 별1 + " #fc0xFFFF9436#80  | 3樓文具店<4>#l\r\n";
					selStr += "#L 103041139 ##fc0xFFFF9436#Lv.159#d | " + 별1 + " #fc0xFFFF9436#80  | 4樓唱片店<4>#l\r\n";
					selStr += "#L 103041149 ##fc0xFFFF9436#Lv.161#d | " + 별1 + " #fc0xFFFF9436#80  | 5樓化裝品店<4>#l\r\n";
					selStr += "#L 103041159 ##fc0xFFFF9436#Lv.162#d | " + 별1 + " #fc0xFFFF9436#80  | 6樓髮廊<4>#l\r\n\r\n";
					selStr += "   #fs15##e#r[時間神殿]#d#n\r\n";
					selStr += "#L 270030600 ##fc0xFFFF9436#Lv.160#d | " + 별1 + " #fc0xFFFF9436#90  | 令一個忘却之路1#l\r\n";
					selStr += "#L 270030610 ##fc0xFFFF9436#Lv.162#d | " + 별1 + " #fc0xFFFF9436#90  | 令一個忘却之路2#l\r\n";
					selStr += "#L 270030620 ##fc0xFFFF9436#Lv.164#d | " + 별1 + " #fc0xFFFF9436#90  | 令一個忘却之路3#l\r\n";
					selStr += "#L 270030630 ##fc0xFFFF9436#Lv.165#d | " + 별1 + " #fc0xFFFF9436#90  | 令一個忘却之路4#l\r\n\r\n";
					selStr += "   #fs15##e#r[騎士團要塞]#d#n\r\n";
					selStr += "#L 271030101 ##fc0xFFFF9436#Lv.169#d | " + 별1 + " #fc0xFFFF9436#120 | 第1練武場#l\r\n";
					selStr += "#L 271030102 ##fc0xFFFF9436#Lv.169#d | " + 별1 + " #fc0xFFFF9436#120 | 第2練武場#l\r\n";
					selStr += "#L 271030310 ##fc0xFFFF9436#Lv.173#d | " + 별1 + " #fc0xFFFF9436#120 | 武器庫1#l\r\n";
					selStr += "#L 271030320 ##fc0xFFFF9436#Lv.175#d | " + 별1 + " #fc0xFFFF9436#120 | 武器庫2#l\r\n\r\n";
					selStr += "   #fs15##e#r[地球防衛本部]#d#n\r\n";
					selStr += "#L 221030640 ##fc0xFFFF9436#Lv.178#d | " + 별1 + " #fc0xFFFF9436#140 | 走廊 H01#l\r\n";
					selStr += "#L 221030650 ##fc0xFFFF9436#Lv.179#d | " + 별1 + " #fc0xFFFF9436#140 | 走廊 H02#l\r\n";
					selStr += "#L 221030660 ##fc0xFFFF9436#Lv.180#d | " + 별1 + " #fc0xFFFF9436#140 | 走廊 H03#l\r\n ";
					cm.sendSimpleS(selStr, talkType);
					break;

				case 3:
					selStr += "#fs15#\r\n請確認獵場的 #e#fc0xFF6799FF#神秘之力要求#k#n和 #e#b怪物的平均等級後再進行移動。\r\n";
					selStr += "\r\n#e#r[消亡旅途]#d#n\r\n";
					selStr += "#L 450001010 ##fc0xFF6799FF#Lv.200 | " + 별2 + " 30  | 風乾喜悅之地#l\r\n";
					selStr += "#L 450001100 ##fc0xFF6799FF#Lv.204 | " + 별2 + " 40  | 海市蜃樓崖#l\r\n";
					selStr += "#L 450001200 ##fc0xFF6799FF#Lv.207 | " + 별2 + " 60  | 洞穴入口#l\r\n";
					selStr += "#L 450001260 ##fc0xFF6799FF#Lv.209 | " + 별2 + " 80  | 隱藏湖畔#l\r\n";
					selStr += "#L 450001261 ##fc0xFF6799FF#Lv.209 | " + 별2 + " 80  | 隱藏火焰地帶#l\r\n";
					selStr += "#L 450001262 ##fc0xFF6799FF#Lv.209 | " + 별2 + " 80  | 隱秘洞穴#l\r\n";
					selStr += "\r\n\r\n#e#r[丘丘島]#d#n\r\n"
					selStr += "#L 450002001 ##fc0xFF6799FF#Lv.210 | " + 별2 + " 100 | 東山入口#l\r\n";
					selStr += "#L 450002006 ##fc0xFF6799FF#Lv.212 | " + 별2 + " 100 | 狹長圓林1#l\r\n";
					selStr += "#L 450002012 ##fc0xFF6799FF#Lv.214 | " + 별2 + " 130 | 激流地帶 1#l\r\n";
					selStr += "#L 450002016 ##fc0xFF6799FF#Lv.217 | " + 별2 + " 160 | 鯨山入口#l\r\n";
					selStr += "\r\n\r\n#e#r[反轉之城]#d#n\r\n";
					selStr += "#L 450014020 ##fc0xFF6799FF#Lv.210 | " + 별2 + " 100 | 地下鐵路1#l\r\n";
					selStr += "#L 450014100 ##fc0xFF6799FF#Lv.212 | " + 별2 + " 100 | T-boy的硏究列車1#l\r\n";
					selStr += "#L 450014140 ##fc0xFF6799FF#Lv.214 | " + 별2 + " 100 | 地鐵1#l\r\n";
					selStr += "#L 450014200 ##fc0xFF6799FF#Lv.214 | " + 별2 + " 160 | M高塔1#l\r\n";
					selStr += "#L 450014300 ##fc0xFF6799FF#Lv.217 | " + 별2 + " 100 | 隱藏硏究列車#l\r\n";
					selStr += "#L 450014310 ##fc0xFF6799FF#Lv.217 | " + 별2 + " 100 | 隱藏的地鐵#l\r\n";
					selStr += "\r\n\r\n#e#r[拉克蘭]#d#n\r\n";
					selStr += "#L 450003200 ##fc0xFF6799FF#Lv.220 | " + 별2 + " 190 | 匪盜街1#l\r\n";
					selStr += "#L 450003300 ##fc0xFF6799FF#Lv.221 | " + 별2 + " 210 | 肌飛狗跳之地1#l\r\n";
					selStr += "#L 450003400 ##fc0xFF6799FF#Lv.223 | " + 별2 + " 210 | 原形畢露之地1#l\r\n";
					selStr += "#L 450003440 ##fc0xFF6799FF#Lv.225 | " + 별2 + " 210 | 舞鞋占領地1#l\r\n";
					selStr += "#L 450003500 ##fc0xFF6799FF#Lv.226 | " + 별2 + " 240 | 惡夢時間塔1樓#l\r\n";
					selStr += "#L 450003510 ##fc0xFF6799FF#Lv.226 | " + 별2 + " 240 | 惡夢時間塔2樓#l\r\n";
					selStr += "#L 450003520 ##fc0xFF6799FF#Lv.226 | " + 별2 + " 240 | 惡夢時間塔3樓#l\r\n";
					selStr += "#L 450003530 ##fc0xFF6799FF#Lv.226 | " + 별2 + " 240 | 惡夢時間塔4樓#l\r\n";
					selStr += "#L 450003540 ##fc0xFF6799FF#Lv.226 | " + 별2 + " 240 | 惡夢時間塔5樓#l\r\n";
					selStr += "\r\n\r\n#e#r[阿爾喀那]#d#n\r\n";
					selStr += "#L 450005100 ##fc0xFF6799FF#Lv.230 | " + 별2 + " 280 | 草笛的森林#l\r\n";
					selStr += "#L 450005200 ##fc0xFF6799FF#Lv.233 | " + 별2 + " 320 | 森林深處#l\r\n";
					selStr += "#L 450005230 ##fc0xFF6799FF#Lv.235 | " + 별2 + " 320 | 劇毒森林#l\r\n";
					selStr += "#L 450005500 ##fc0xFF6799FF#Lv.237 | " + 별2 + " 360 | 五叉洞穴#l\r\n ";
					selStr += "\r\n\r\n#e#r[莫拉斯]#d#n\r\n";
					selStr += "#L 450006010 ##fc0xFF6799FF#Lv.236 | " + 별2 + " 400 | 通往珊瑚林的路2#l\r\n";
					selStr += "#L 450006140 ##fc0xFF6799FF#Lv.238 | " + 별2 + " 440 | 潑皮地盤#l\r\n";
					selStr += "#L 450006210 ##fc0xFF6799FF#Lv.239 | " + 별2 + " 480 | 影舞之地2#l\r\n";
					selStr += "#L 450006300 ##fc0xFF6799FF#Lv.241 | " + 별2 + " 480 | 封鎖區#l\r\n ";
					selStr += "#L 450006410 ##fc0xFF6799FF#Lv.245 | " + 별2 + " 480 | 那天的特魯埃博2#l\r\n ";
					selStr += "\r\n\r\n#e#r[埃斯佩拉]#d#n\r\n";
					selStr += "#L 450007010 ##fc0xFF6799FF#Lv.240 | " + 별2 + " 560 | 生命起源處2#l\r\n";
					selStr += "#L 450007050 ##fc0xFF6799FF#Lv.242 | " + 별2 + " 560 | 生命起源處5#l\r\n";
					selStr += "#L 450007100 ##fc0xFF6799FF#Lv.244 | " + 별2 + " 600 | 鏡澄之海#l\r\n";
					selStr += "#L 450007210 ##fc0xFF6799FF#Lv.248 | " + 별2 + " 640 | 鏡中的光明神殿2#l\r\n";
					selStr += "\r\n\r\n#e#r[月橋]#d#n\r\n"
					selStr += "#L 450009110 ##fc0xFF6799FF#Lv.250 | " + 별2 + " 670 | 極思之境1#l\r\n";
					selStr += "#L 450009210 ##fc0xFF6799FF#Lv.252 | " + 별2 + " 700 | 未知迷霧1#l\r\n";
					selStr += "#L 450009310 ##fc0xFF6799FF#Lv.254 | " + 별2 + " 730 | 虛空波濤1#l\r\n";
					selStr += "\r\n\r\n#e#r[痛苦迷宮]#d#n\r\n"
					selStr += "#L 450011420 ##fc0xFF6799FF#Lv.255 | " + 별2 + " 760 | 痛苦迷宮內部1#l\r\n";
					selStr += "#L 450011510 ##fc0xFF6799FF#Lv.257 | " + 별2 + " 790 | 痛苦迷宮中心區1#l\r\n";
					selStr += "#L 450011600 ##fc0xFF6799FF#Lv.259 | " + 별2 + " 820 | 痛苦迷宮核心區1#l\r\n";
					selStr += "\r\n\r\n#e#r[黎曼]#d#n\r\n";
					selStr += "#L 450012030 ##fc0xFF6799FF#Lv.260 | " + 별2 + " 850 | 世界的眼淚下方2#l\r\n";
					selStr += "#L 450012100 ##fc0xFF6799FF#Lv.261 | " + 별2 + " 850 | 世界的眼淚中段1#l\r\n";
					selStr += "#L 450012330 ##fc0xFF6799FF#Lv.262 | " + 별2 + " 880 | 世界終結之處 1-4#l\r\n";
					selStr += "#L 450012430 ##fc0xFF6799FF#Lv.262 | " + 별2 + " 880 | 世界終結之處 2-4#l\r\n";
					selStr += "#L 450012470 ##fc0xFF6799FF#Lv.264 | " + 별2 + " 880 | 世界的末端 2-7#l\r\n";
					//selStr += "#L993072000##fc0xFF6799FF#Lv.263#d | " + 별2 + " 880 | 레지스탕스 함선 갑판#l\r\n";
					//selStr += "#L993072500##fc0xFF6799FF#Lv.264#d | " + 별2 + " 880 | 레지스탕스 함선 갑판6#l\r\n";
					cm.sendSimpleS(selStr, talkType);
					break;

					case 4:
					selStr += "#fs15#\r\n請確認獵場的 #e#fc0xFF6799FF#原初之力要求#k#n和 #e#b怪物的平均等級#k#n \r\n後再進行移動。\r\n#fs15#";
					selStr += "\r\n#e#r[塞爾提烏]#n#k#l\r\n";
					selStr += "#L 410000530 # #fc0xFF6799FF#Lv.260 | " + 별3 + " 30  | 海邊岩石地帶 2#l\r\n";
					selStr += "#L 410000650 # #fc0xFF6799FF#Lv.260 | " + 별3 + " 30  | 塞爾提烏東城牆 2#l\r\n";
					selStr += "#L 410000740 # #fc0xFF6799FF#Lv.260 | " + 별3 + " 30  | 王室圖書館第5地區#l\r\n";
					selStr += "\r\n\r\n#e#r[燃燒的塞爾提烏]#n#k#l\r\n";
					selStr += "#L 410000950 # #fc0xFF6799FF#Lv.265 | " + 별3 + " 50 | 決戰的西方城牆 4#l\r\n";
					selStr += "#L 410001000 # #fc0xFF6799FF#Lv.265 | " + 별3 + " 50 | 決戰的東邊城牆 3#l\r\n";
					selStr += "#L 410000890 # #fc0xFF6799FF#Lv.265 | " + 별3 + " 50 | 燃燒的王立圖書館第6區域#l\r\n";
					selStr += "\r\n\r\n#e#r[亞克斯旅館]#n#k#l\r\n";
					selStr += "#L 410003070 # #fc0xFF6799FF#Lv.270 | " + 별3 + " 70 | 不法之徒所支配的荒野4#l\r\n";
					selStr += "#L 410003140 # #fc0xFF6799FF#Lv.270 | " + 별3 + " 70 | 浪漫已逝的汽車電影院6#l\r\n";
					selStr += "#L 410003160 # #fc0xFF6799FF#Lv.270 | " + 별3 + " 100 | 沒有目的地的橫貫列車2#l\r\n";
					selStr += "\r\n\r\n#e#r[奧迪溫]#n#k#l\r\n"
					selStr += "#L 410007005 # #fc0xFF6799FF#Lv.275 | " + 별3 + " 130 | 通往城門的路 5#l\r\n";
					selStr += "#L 410007009 # #fc0xFF6799FF#Lv.275 | " + 별3 + " 160 | 被占領的小巷 4#l\r\n";
					selStr += "#L 410007014 # #fc0xFF6799FF#Lv.275 | " + 별3 + " 180 | 陽光灑落的實驗室 3#l\r\n";
					selStr += "#L 410007017 # #fc0xFF6799FF#Lv.275 | " + 별3 + " 200 | 緊鎖的門後的實驗室 4#l\r\n";
					selStr += "\r\n\r\n#e#r[桃源境]#n#k#l\r\n"
					selStr += "#L 410007027 # #fc0xFF6799FF#Lv.275 | " + 별3 + " 230 | 生機悠然的春天 2#l\r\n";
					selStr += "#L 410007030 # #fc0xFF6799FF#Lv.275 | " + 별3 + " 260 | 微光閃碩的夏天 2#l\r\n";
					selStr += "#L 410007033 # #fc0xFF6799FF#Lv.275 | " + 별3 + " 280 | 言色淺淡的秋天 2#l\r\n";
					selStr += "#L 410007036 # #fc0xFF6799FF#Lv.275 | " + 별3 + " 300 | 殘酷痕跡的冬天 2#l\r\n";
					selStr += "\r\n\r\n#e#r[阿爾特利亞]#n#k#l\r\n"
					selStr += "#L 410007522 # #fc0xFF6799FF#Lv.280 | " + 별3 + " 330 | 北方外圍區域#l\r\n";
					selStr += "#L 410007529 # #fc0xFF6799FF#Lv.280 | " + 별3 + " 360 | 最下層通道 3#l\r\n";
					selStr += "#L 410007538 # #fc0xFF6799FF#Lv.280 | " + 별3 + " 400 | 最上層通道 2#l\r\n";
					selStr += "#L 410007552 # #fc0xFF6799FF#Lv.280 | " + 별3 + " 400 | 最上層通道 6#l\r\n";
					selStr += "\r\n\r\n#e#r[喀西翁]#n#k#l\r\n"
					selStr += "#L 410007624 # #fc0xFF6799FF#Lv.285 | " + 별3 + " 430 | 平靜的海岸邊 2#l\r\n";
					selStr += "#L 410007644 # #fc0xFF6799FF#Lv.285 | " + 별3 + " 460 | 黑暗降臨的樹幹 2#l\r\n";
					selStr += "#L 410007662 # #fc0xFF6799FF#Lv.285 | " + 별3 + " 500 | 令人窒息的洞窟 3#l\r\n";
					selStr += "#L 410007667 # #fc0xFF6799FF#Lv.285 | " + 별3 + " 500 | 塵寂的遺址 4#l\r\n";
					selStr += " ";
					cm.sendSimpleS(selStr, talkType);
					break;
			}
			cm.sendSimpleS(selStr, 4);
		}
	} else if (status == 3) {
		ans_03 = selection;
		switch (ans_02) {
			case 1:
			case 2:
			case 3:
				cm.warp(ans_03, "sp");
				cm.dispose();
				break;
			case 4:
				cm.warp(ans_03, "sp");
				cm.dispose();
				break;
		}
	}
}

