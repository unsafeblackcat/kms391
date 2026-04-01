importPackage(Packages.client.inventory);
importPackage(Packages.packet.creators);
importPackage(Packages.constants);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(java.lang);


function start()
{
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S)
{
	if(M != 1)
	cm.dispose();

	else
	St++;

	if(St == 0)
	{
		SET_GIV = cm.getClient().getKeyValue("GM_SETTING_GIV");
		SET_DEL = cm.getClient().getKeyValue("GM_SETTING_DEL");

		if(!cm.getPlayer().isGM())
		{
			cm.dispose();
			return;
		}

		//log("access", "NPC 접속 \r\n");
		cm.sendSimpleS("#fs15#管理員 #b#e#h ##k#n您好，很高興見到您。請問有什麼可以幫忙的嗎？\r\n#b"
			+ "#L0#角色査詢#l\r\n"
			+ "#L1#設定變更#l\r\n\r\n", 4);
		}

	else if(St == 1)
	{
		S1 = S;
		switch(S1)
		{

			//checkOnOff()
			case 1:
			selStr = "目前設定如下。點擊選項可切換 #bON#k/#rOFF#k 狀態。\r\n"
			selStr += "#L10#"+checkOnOff(SET_GIV, "color")+"道具發放時顯示通知公告 #e("+checkOnOff(SET_GIV, "string")+")#n\r\n";
			selStr += "#L11#"+checkOnOff(SET_DEL, "color")+"道具刪除時顯示通知公告 #e("+checkOnOff(SET_DEL, "string")+")#n\r\n"; 
			cm.sendSimpleS(selStr, 4);
			break;

			case 0:
			cm.sendGetText("管理員 #b#e#h ##k#n您好，請輸入要查詢的角色名稱。僅限目前在線的角色。");
			break;
		}
	}

	else if(St == 2)
	{
		S2 = S;

		switch(S1)
		{
			case 1: //設定變更
			switch(S2)
			{
				case 10:
				if(SET_GIV == -1)
				cm.getClient().setKeyValue("GM_SETTING_GIV", 1);

				else
				cm.getClient().setKeyValue("GM_SETTING_GIV", -1);

				cm.getPlayer().dropMessage(5, "道具發放時顯示通知公告的設定已更改為 "+checkOnOff(SET_GIV * -1, "string")+"。");
				break;

				case 11:
				if(SET_DEL == -1) //OFF
				cm.getClient().setKeyValue("GM_SETTING_DEL", 1);

				else
				cm.getClient().setKeyValue("GM_SETTING_DEL", -1);

				cm.getPlayer().dropMessage(5, "道具刪除時顯示通知公告的設定已更改為 "+checkOnOff(SET_DEL * -1, "string")+"。");
				break;
			}
			cm.dispose();
			cm.openNpc(9010017);
			break;

			case 0:
			name = cm.getText();
			ch = Packages.handling.world.World.Find.findChannel(name);
			if (ch >= 0)
			{
				chr = Packages.handling.channel.ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);
				if (chr != null)
				{
					//log("access", ""+name+"("+chr.getId()+") 角色已連接\r\n");
					cm.sendSimpleS("#b#e"+chr.getName()+"#k#n 角色已連接。請問需要什麼服務？#b\r\n\r\n#fn細明體##fs11#"
						+ "   - 持有楓幣數量 | #fn細明體#"+Comma(chr.getMeso())+" (10億錢包 : "+Comma(chr.itemQuantity(4001716))+"個)#fn細明體#\r\n"
						+ "   - 持有水之幣數量 | "+Comma(chr.itemQuantity(4310237))+"個\r\n"
						+ "   - 持有新石 | "+Comma(chr.getKeyValue(100711, "point"))+"\r\n"
						+ "   - 持有新果醬 | "+Comma(chr.getKeyValue(100712, "point"))+"\r\n"
						+ "   - 持有新核心 | "+Comma(chr.getKeyValue(501215, "point"))+"\r\n"
						+ "   - 持有推廣點數 | "+Comma(chr.getHPoint())+"\r\n"
						+ "   - 持有贊助點數 | "+Comma(chr.getDPoint())+"\r\n#fs12##fn細明體#"
						//+ "#L16#推廣點數發放#l #L17#贊助點數發放#l\r\n"
						+ "#L13#新石發放#l#L14#新果醬發放#l#L15#新核心發放#l\r\n\r\n"
						+ "#L18#新石回收#l#L19#新果醬回收#l#L20#新核心回收#l\r\n\r\n"

							+ "#L21#贊助點數設定#l\r\n\r\n"
						+ "#L0#查看裝備中的物品#l\r\n"
						+ "#L1#查看裝備欄#l\r\n"
						+ "#L2#查看消耗品欄#l\r\n"
						+ "#L3#查看設置欄#l\r\n"
						+ "#L4#查看其他欄#l\r\n"
						+ "#L5#查看現金欄#l\r\n"
						+ "#L6#查看裝扮欄#l\r\n\r\n"
						+ "#L7#查看倉庫#l\r\n\r\n"
						+ "#L10#向角色發送消息#l\r\n"
						+ "#L11#向角色發放道具#l\r\n"
						+ "#L12#巨集查詢#l\r\n", 4);
				}
				else
				{
					cm.sendOk("無法訪問該角色。可能正在切換頻道或已離線。");
					cm.dispose();
					return;
				}
			}
			else
			{
				cm.sendOk("#b#e"+name+"#k#n 角色目前未在線。");
				//log("fail", "查詢失敗	"+name+"	未在線\r\n");
				cm.dispose();
				return;
			}
		}
	}

	else if(St == 3)
	{
		S3 = S;
		switch(S1)
		{

			case 0:
			if(chr == null)
			{
				cm.sendOkS("該角色已離線，連接已中斷。");
				//log("fail", "查詢失敗	"+name+"	查詢期間離線\r\n");
				cm.dispose();
				return;
			}

			if(S3 == 999)
			{
				chr.addKV("Hard_Will", "0");
				chr.dropMessage(5, "次數已重置。");
				cm.dispose();
				return;
			}

			if(S3 == 10)
			{
				cm.sendGetText("請輸入要發送給"+chr.getName()+"的消息。太長的消息可能會被截斷。");
				return;
			}

			if(S3 == 11)
			{
				cm.sendGetNumber("請輸入要發送給"+chr.getName()+"的道具代碼。", 0, 0, 5999999);
				return;
			}


			if(S3 == 12)
			{
				if(chr == null)
				{
					cm.sendOkS("該角色已離線，連接已中斷。");
					//log("fail", "查詢失敗	"+name+"	查詢期間離線\r\n");
					cm.dispose();
					return;
				}

				cm.dispose();
				cm.openNpcCustom2(chr.getClient(), 2007, "macro");
				cm.getPlayer().dropMessage(5, "已對"+name+"角色進行巨集測試。");
				return;
			}

			if(S3 >= 13 && S3 <= 15)
			{
				cm.sendGetNumber("請輸入要發放給"+chr.getName()+"的數量。", 1, 1, 9999);
				return;
			}

			if(S3 >= 16 && S3 <= 17)
			{
				cm.sendGetNumber("請輸入要發放給"+chr.getName()+"的點數。", 1, 1, 999999);
				return;
			}
			if(S3 >= 18 && S3 <= 20)
			{
				cm.sendGetNumber("請輸入要從"+chr.getName()+"回收的點數。", 1, 1, 999999);
				return;
			}

			if(S3 == 21)
			{
				cm.sendGetNumber("請輸入要為"+chr.getName()+"設定的點數。", 1, 1, 999999);
				return;
			}

			if(S3 == 7)
			{
				//log("access", ""+name+"("+chr.getId()+") 角色的倉庫已連接\r\n");

				selStr = "#b#e"+chr.getName()+"#k#n 角色的 #e#r倉庫#k#n 已連接。倉庫物品僅供查詢。\r\n\r\n#b";

				for (w = 0; w < chr.getStorage().getItems().size(); w++)
				selStr += "#i"+chr.getStorage().getItems().get(w).getItemId()+"# #z"+chr.getStorage().getItems().get(w).getItemId()+"# #e"+chr.getStorage().getItems().get(w).getQuantity()+"個#n\r\n";

				cm.sendOkS(selStr, 4);
				cm.dispose();
				return;
			}

			if(S3 < 7) // 物品欄
			{
				invType = S3 == 0 ? "裝備中" : S3 == 1 ? "裝備" : S3 == 2 ? "消耗品" : S3 == 3 ? "設置" : S3 == 4 ? "其他" : S3 == 5 ? "現金" : "裝扮";

				//log("access", ""+name+"("+chr.getId()+") 角色的"+invType+"物品欄已連接\r\n");

				selStr = "#b#e"+chr.getName()+"#k#n 角色的 #e#r"+invType+"#k#n 物品欄已連接。您想查看哪個物品？\r\n#b";
				inv = (S3 != 0) ? chr.getInventory(S3) : chr.getInventory(MapleInventoryType.EQUIPPED);

				if(S3 != 0)
				{
					for(z = 0; z < inv.getSlotLimit(); z++)
					{
						if(inv.getItem(z) == null)
						continue;
	
						selStr += "#L"+z+"# #i"+inv.getItem(z).getItemId()+":# #t"+inv.getItem(z).getItemId()+":# (數量 : "+inv.getItem(z).getQuantity()+"個)\r\n";
					}
				}
				else
				{
					for(z = 0; z < 100; z++)
					{
						a = -1 * z;
						if(inv.getItem(a) == null)
						continue;
	
						selStr += "#L"+z+"# "+z+". #i"+inv.getItem(a).getItemId()+":# #t"+inv.getItem(a).getItemId()+":#\r\n";

						if(z > 100)
						break;
					}

				}
				cm.sendSimpleS(selStr, 4);
			}
			else
			{
				cm.sendOk("該功能尚未實現。");
				cm.dispose();
			}
			break;
		}
	}

	else if(St == 4)
	{
		S4 = (S3 != 0) ? S : -1 * S;
		switch(S1)
		{
			case 0:
			if(chr == null)
			{
				cm.sendOkS("該角色已離線，連接已中斷。");
				//log("fail", "查詢失敗	"+name+"	物品欄查詢期間離線\r\n");
				cm.dispose();
				return;
			}

			if(S3 < 10) // 物品欄
			{
				//log("access", ""+name+"("+chr.getId()+") 角色的"+invType+"物品欄中的"+inv.getItem(S4).getItemId()+"道具已連接\r\n");

				selStr = "#b#e"+chr.getName()+"#k#n 角色的 #e#r"+invType+"#k#n 物品欄已連接。\r\n";
				selStr += "目前選擇的道具是 #e#b#t"+inv.getItem(S4).getItemId()+":##k#n。\r\n#b"; 
				if(S3 < 2) //裝備
				{
					selStr += "#L20010#將選擇的道具複製到我的物品欄 #e複製#n#l\r\n";

					if(S3 == 1)
					selStr += "#L20000#將選擇的道具從我的物品欄中 #r#e刪除#n#b#l";

					selStr += "\r\n\r\n\r\n#e[詳細屬性]#k#n\r\n#fs11#";
					selStr += "  - 力量 #e+"+inv.getItem(S4).getStr()+"#n, 敏捷 #e+"+inv.getItem(S4).getDex()+"#n, 智力 #e+"+inv.getItem(S4).getInt()+"#n, 幸運 #e+"+inv.getItem(S4).getLuk()+"#n\r\n"
					selStr += "  - 攻擊力 #e+"+inv.getItem(S4).getWatk()+"#n, 魔力 #e+"+inv.getItem(S4).getMatk()+"#n\r\n";
					selStr += "  - 特殊選項狀態 : #e"+inv.getItem(S4).getOwner()+"#n\r\n";

					selStr += "\r\n\r\n#e#fs12##b[附加能力]#k#n\r\n#fs11#";
					selStr += "  - 第1行 : #e"+toString(inv.getItem(S4).getPotential1())+"#n\r\n";
					selStr += "  - 第2行 : #e"+toString(inv.getItem(S4).getPotential2())+"#n\r\n";
					selStr += "  - 第3行 : #e"+toString(inv.getItem(S4).getPotential3())+"#n\r\n";
					selStr += "  - 第4行 : #e"+toString(inv.getItem(S4).getPotential4())+"#n\r\n";
					selStr += "  - 第5行 : #e"+toString(inv.getItem(S4).getPotential5())+"#n\r\n";
					selStr += "  - 第6行 : #e"+toString(inv.getItem(S4).getPotential6())+"#n\r\n";


					cm.sendSimpleS(selStr, 4);
				}
				else
				{
					selStr += "\r\n#k目前該插槽中 #e#r"+inv.getItem(S4).getQuantity()+"個#k#n。#e#r請輸入要刪除的數量。";
					cm.sendGetNumber(selStr, 0, 1, inv.getItem(S4).getQuantity());
				}
			}

			if(S3 == 10)
			{
				txt = cm.getText();
				if(txt.contains("cm") || txt.contains("Packages"))
				{
					cm.dispose();
					return;
				}
				selStr  = "確定要向 #e"+name+"#n 發送以下消息嗎？請選擇消息發送類型。\r\n\r\n#d"+txt+"#r\r\n";
				selStr += "#L5#粉紅色公告#l\r\n";
				selStr += "#L6#藍色公告\r\n";

				if(txt.length < 40)
				selStr += "#L1#彈出公告 (限40字以內)#l\r\n";
				cm.sendSimpleS(selStr, 4);
			}

            if(S3 == 11)
			{
				number = S4;
				ii = Packages.server.MapleItemInformationProvider.getInstance();
				if (!ii.itemExists(number)) {
				    cm.sendOk("不存在的道具。");
				    cm.dispose();
				    return;
				}
				cm.sendGetNumber("請輸入要發送給"+chr.getName()+"的道具數量。", 1, 1, 32767);
				return;
			}
            if(S3 >= 13 && S3 <= 15)
			{
				number = S4;
                if(S3 == 13){  
                    txt = name+" 角色已獲得 "+number+" 個新石。";
                    chr.setKeyValue(100711, "point", chr.getKeyValue(100711, "point")+number);
                } else if(S3 == 14) {
                    txt = name+" 角色已獲得 "+number+" 個新果醬。";
                    chr.setKeyValue(100712, "point", chr.getKeyValue(100712, "point")+number);
                } else if(S3 == 15) {
                    txt = name+" 角色已獲得 "+number+" 個新核心。";
                    chr.setKeyValue(501215, "point", chr.getKeyValue(501215, "point")+number);
                }
                cm.getPlayer().dropMessage(6, txt);
                cm.dispose();
				return;
			}
            if(S3 >= 16 && S3 <= 17)
			{
				number = S4;
                if(S3 == 13){  
                    txt = name+" 角色已獲得 "+number+" 點推廣點數。";
                    chr.setKeyValue(100711, "point", chr.getKeyValue(100711, "point")+number);
                } else if(S3 == 14) {
                    txt = name+" 角色已獲得 "+number+" 點贊助點數。";
                    chr.setKeyValue(100712, "point", chr.getKeyValue(100712, "point")+number);
                }
                cm.getPlayer().dropMessage(6, txt);
                cm.dispose();
				return;
			}
			 if(S3 >= 18 && S3 <= 20)
			{
				number = S4;
                if(S3 == 18){  
                    txt = name+" 角色已被回收 "+number+" 個新石。";
                    chr.setKeyValue(100711, "point", chr.getKeyValue(100711, "point")-number);
                } else if(S3 == 19) {
                    txt = name+" 角色已被回收 "+number+" 個新果醬。";
                    chr.setKeyValue(100712, "point", chr.getKeyValue(100712, "point")-number);
                } else if(S3 == 20) {
                    txt = name+" 角色已被回收 "+number+" 個新核心。";
                    chr.setKeyValue(501215, "point", chr.getKeyValue(501215, "point")-number);
                }
                cm.getPlayer().dropMessage(6, txt);
                cm.dispose();
				return;
			}
			if (S3 == 21) 
			{
				number = S4;
				txt = name+" 角色的贊助點數已設定為 " +number+ "。\r\n";

                  chr.setDPoint(number);
				  txt += "設定的點數 : " + chr.getDPoint();
				      cm.getPlayer().dropMessage(6, txt);
			}
			break;
		}
	}

	else if(St == 5)
	{
		S5 = S;
		switch(S1)
		{
			case 0:
			if(chr == null)
			{
				cm.sendOkS("該角色已離線，連接已中斷。");
				//log("fail", "查詢失敗	"+name+"	物品欄修改期間離線\r\n");
				cm.dispose();
				return;
			}

			if(S3 < 10) // 物品欄
			{
				if(S5 != 20010) // 不是複製
				{
					selStr = "\r\n#e#r刪除原因#k#n 請輸入。輸入以下數字可自動填入內容。\r\n\r\n如有其他原因，請自行填寫。\r\n";
					selStr += "#e1 : #n誤發放道具回收\r\n"
					selStr += "#e2 : #n非正當途徑獲得的道具回收\r\n"
					selStr += "#e3 : #n交易詐騙獲得的道具回收\r\n\r\n";
				}
				else
				{
					selStr = "\r\n#e#r複製原因#k#n 請輸入。輸入以下數字可自動填入內容。\r\n\r\n如有其他原因，請自行填寫。\r\n";
					selStr += "#e4 : #n交易詐騙獲得的道具回收\r\n"
					selStr += "#e5 : #n非正當途徑獲得的道具保留\r\n"
				}
				cm.sendGetText(selStr);
			}


			if(S3 == 10) // 公告發送
			{

				//log("notice", "發送成功	"+name+"	"+S5+"	"+txt+"\r\n");

				chr.dropMessage(S5, txt);
				cm.getPlayer().dropMessage(5, "已向"+name+"發送公告。請繼續下一步操作。");
				cm.dispose();
				cm.openNpc(9010017);
				return;
			}
			if(S3 == 11)
			{
				count = S5;
				if (count > 32767 || count <= 0) {
				    cm.sendOk("數值不正確。請重新輸入。");
				    cm.dispose();
				    return;
				}
				if (!Packages.server.MapleInventoryManipulator.checkSpace(chr.getClient(), number, count, "")) {
				    cm.sendOk("目標的物品欄空間不足。");
				    cm.dispose();
				    return;
				}
				if (count > 1 && Math.floor(number / 1000000) == 1) {
				    for (i = 0; i < count; ++i) {
					chr.gainItem(number, 1);
				    }
				} else if (Packages.constants.GameConstants.isPet(number)) {
				    Packages.server.MapleInventoryManipulator.addId(chr.getClient(), number, 1, "", Packages.client.inventory.MaplePet.createPet(number, -1), 1, "", false);
				} else {
				    chr.gainItem(number, count);
				}
				cm.getPlayer().dropMessage(5, "已向"+name+"發送道具。請繼續下一步操作。");
				cm.dispose();
				cm.openNpc(9010017);
				return;
			}
		}
	}
	else if(St == 6)
	{
		S6 = S;
		switch(S1)
		{
			case 0:

			if(chr == null)
			{
				cm.sendOkS("該角色已離線，連接已中斷。");
				//log("fail", "查詢失敗	"+name+"	物品欄修改期間離線\r\n");
				cm.dispose();
				return;
			}

			if(S3 < 10) // 物品欄
			{
				REASON = function() {
						switch(cm.getText())
						{
							case "1": return "誤發放道具回收";
							case "2": return "非正當途徑獲得的道具回收";
							case "3": return "交易詐騙獲得的道具回收";
							case "4": return "交易詐騙獲得的道具回收";
							case "5": return "非正當途徑獲得的道具保留";
							default: return cm.getText();
						}
					}
			
				TYPES = S3 == 0 ? MapleInventoryType.EQUIPPED :
					S3 == 1 ? MapleInventoryType.EQUIP : 
					S3 == 2 ? MapleInventoryType.USE :
					S3 == 3 ? MapleInventoryType.SETUP :
					S3 == 4 ? MapleInventoryType.ETC :
					MapleInventoryType.CASH;


				switch(S5)
				{
					case 20000: // 刪除
					reason = S5 == 20000 ? "道具誤發放" : S5 == 20001 ? "道具回收" : "其他";

					if(SET_DEL == 1)
					chr.dropMessage(5, " ");
					chr.dropMessage(5, "[通知] "+cm.getItemNameById(inv.getItem(S4).getItemId())+" 道具因 '"+REASON()+"' 原因被刪除。");
					chr.dropMessage(5, " ");

					//log("success", "刪除	"+chr.getId()+"	"+name+"	"+invType+"	"+inv.getItem(S4).getItemId()+"	1個	"+REASON()+"\r\n");
					MapleInventoryManipulator.removeFromSlot(chr.getClient(), TYPES, S4, 1, false);
					cm.sendOk("道具刪除完成。");

	 
					cm.dispose();
					break;

					case 20010: // 複製到我的物品欄
					//log("success", "複製	"+chr.getId()+"	"+name+"	"+invType+"	"+inv.getItem(S4).getItemId()+"	1個	"+REASON()+"\r\n");
					items = inv.getItem(S4).copy();
					MapleInventoryManipulator.addFromDrop(cm.getClient(), items, true);
					cm.sendOk("道具複製完成。");

					cm.dispose();
					break;

					default:

					if(SET_DEL == 1)
					chr.dropMessage(5, " ");
					chr.dropMessage(5, "[通知] "+cm.getItemNameById(inv.getItem(S4).getItemId())+" 道具 "+S5+" 個因 '"+REASON()+"' 原因被刪除。");
					chr.dropMessage(5, " ");

					//log("success", "刪除	"+chr.getId()+"	"+name+"	"+invType+"	"+inv.getItem(S4).getItemId()+"	"+S5+"個	"+REASON()+"\r\n");
					MapleInventoryManipulator.removeFromSlot(chr.getClient(), TYPES, S4, S5, false);

					cm.sendOk("道具刪除完成。");
					cm.dispose();

				}
			}
		}
	}
}

function checkOnOff(i, type)
{
	if(i == -1) // off
	{
		switch(type)
		{
			case "color":
			return "#r";

			case "string":
			return "OFF";
		}
	}
	else
	{
		switch(type)
		{
			case "color":
			return "#b";

			case "string":
			return "ON";
		}
	}
}

//內容部分輸入
function log(type, i)
{
	switch(type)
	{
		//추가內容 필요 없음
		case "access": //접속 시
		fn = "1_Access.txt";
		break;

		//추가內容 : interaction, fail reason
		case "fail": //실패 시
		fn = "2_Failure.txt";
		break;

		//추가內容 : interaction, 공지 여부
		case "success": //성공 시
		fn = "3_Success.txt";
		break;

		case "notice": //공지사항
		fn = "4_notice.txt";
		break;
	}

	fw = new java.io.FileWriter("SearchingLog/" + fn, true);
	fw.write(new Date() + "	" + cm.getPlayer().getId() + "	" + cm.getPlayer().getName() + "	" + i);
	fw.close();
}

function toString(i)
{
	switch(i)
	{
		case 0:
		return "#Cgray#未設定#k";

		case 10041:
		case 20041:
		case 30041:
		case 40041:
		return "STR%";

		case 10042:
		case 20042:
		case 30042:
		case 40042:
		return "DEX%";

		case 10043:
		case 20043:
		case 30043:
		case 40043:
		return "INT%";

		case 10044:
		case 20044:
		case 30044:
		case 40044:
		return "LUK%";

		case 20086:
		case 30086:
		case 40086:
		return "全屬性%";

		case 10045:
		case 20045:
		case 30045:
		case 40045:
		return "最大HP%";

		case 10046:
		case 20046:
		case 30046:
		case 40046:
		return "最大MP%";

		case 10070:
		case 20070:
		case 30070:
		case 40070:
		return "傷害%";

		case 10051:
		case 20051:
		case 30051:
		case 40051:
		return "攻擊力%";

		case 10052:
		case 20052:
		case 30052:
		case 40052:
		return "魔力%";

		case 30601:
		case 40601:
		case 30602:
		case 40602:
		case 40603:
		return "對BOSS傷害%";

		case 10291:
		case 20291:
		case 30291:
		case 40291:
		case 40292:
		return "忽略怪物防禦率%";

		case 10055:
		case 20055:
		case 30055:
		case 40055:
		return "爆擊率%";

		case 40056:
		return "爆擊傷害%";

		case 40650:
		return "楓幣獲得量%";

		case 40656:
		return "道具掉落率%";
	
		case 20366:
		case 30366:
		case 40366:
		return "被攻擊時無敵時間+";

		case 40556:
		return "冷卻時間 -1秒";

		case 40557:
		return "冷卻時間 -2秒";

		default:
		return i + "";
	}
}

function Comma(i)
{
	var reg = /(^[+-]?\d+)(\d{3})/;
	i+= '';
	while (reg.test(i))
	i = i.replace(reg, '$1' + ',' + '$2');
	return i;
}