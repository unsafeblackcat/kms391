function start() {
    status = -1;
    action(1, 0, 0);
}

var price_coin = 4310029;
var price_coin_count = 500;
var base_poten = 40057; //방어력%
var new_poten = 40603; //재감2초
var new_poten_str = "크리티컬 데미지";
var new_poten_st = "보스 공격력";
var select_item;
var select_line = -1;

function action(mode, type, selection)
{
    if (mode == 1)
	{
        status++;
    } else {
        cm.dispose();
        return;
    }
    chat = "#fs15#";
    if (status == 0)
	{
		chat += "#e#b<코디 장비 강화 시스템 - 코디 장비 특별 잠재능력 부여>#n#k\r\n";
		chat += "#e치장 아이템#n에 #e#r" + new_poten_str + " 잠재 능력#k#n을 부여해주고 있어.\r\n";
		chat += "1회 이용에 #b#z" + price_coin + "# " + price_coin_count + "\개#k가 필요해.\r\n";
		if(!cm.haveItem(price_coin, price_coin_count))
		{
			chat += "\r\n#b#e너는 이용에 필요한 아이템이 부족한 것 같네.#k#n\r\n아이템을 모아서 다시 찾아와줘.";
			cm.sendOk(chat);
			cm.dispose();
			return;
		}
		else
		{
			chat += "\r\n이용하려면 아래에서 원하는 아이템을 골라.\r\n\r\n";
			
            for (i = 0; i < cm.getInventory(6).getSlotLimit(); i++) {
                if (cm.getInventory(6).getItem(i) != null && cm.isCash(cm.getInventory(6).getItem(i).getItemId())) {
                    chat += "#L" + i + "# #i" + cm.getInventory(6).getItem(i).getItemId() + "# #b#z" + cm.getInventory(6).getItem(i).getItemId() + "##k#l\r\n"
                }
            }
			cm.sendSimple(chat);
		}
    }
	else if (status == 1)
	{
		select_item = cm.getInventory(6).getItem(selection);
		chat = "#fs15#";
		chat += "선택한 아이템 #e#b<#t" + select_item.getItemId() + "#>#n#k\r\n\r\n";
		if(select_item.getPotential1() != new_poten) chat += "#L1##e1번#n 슬롯에 #r" + new_poten_str + "#k 잠재능력을 부여하겠습니다.#l\r\n";
		if(select_item.getState() == 20)
		{
			if(select_item.getPotential2() != new_poten) chat += "#L2##e2번#n 슬롯에 #r" + new_poten_str + "#k 잠재능력을 부여하겠습니다.#l\r\n";
			if(select_item.getPotential3() != new_poten) chat += "#L3##e3번#n 슬롯에 #r" + new_poten_str + "#k 잠재능력을 부여하겠습니다.#l\r\n";
			if(select_item.getPotential4() != new_poten) chat += "#L4##e4번#n 슬롯에 #r" + new_poten_str + "#k 잠재능력을 부여하겠습니다.#l\r\n";
			if(select_item.getPotential5() != new_poten) chat += "#L5##e5번#n 슬롯에 #r" + new_poten_str + "#k 잠재능력을 부여하겠습니다.#l\r\n";
			if(select_item.getPotential6() != new_poten) chat += "#L6##e6번#n 슬롯에 #r" + new_poten_str + "#k 잠재능력을 부여하겠습니다.#l\r\n";
		}
		else
		{
			if(select_item.getPotential2() != new_poten) chat += "\r\n\t#Cgray#2번 슬롯에 " + new_poten_str + " 잠재능력을 부여하겠습니다.\r\n";
			if(select_item.getPotential3() != new_poten) chat += "\t3번 슬롯에 " + new_poten_str + " 잠재능력을 부여하겠습니다.\r\n";
			if(select_item.getPotential4() != new_poten) chat += "\t4번 슬롯에 " + new_poten_str + " 잠재능력을 부여하겠습니다.\r\n";
			if(select_item.getPotential5() != new_poten) chat += "\t5번 슬롯에 " + new_poten_str + " 잠재능력을 부여하겠습니다.\r\n";
			if(select_item.getPotential6() != new_poten) chat += "\t6번 슬롯에 " + new_poten_str + " 잠재능력을 부여하겠습니다.#k\r\n";
		}
		cm.sendSimple(chat);
    }
	else if (status == 2)
	{
		select_line = selection;
		chat = "#fs15#";
		chat += "정말 #e#b#t" + select_item.getItemId() + "##n#k의 #e#b" + select_line + "번#n#k 슬롯에 #r" + new_poten_str + "#k 잠재능력을 부여하시겠습니까?";
		cm.sendYesNo(chat);
    }
	else if (status == 3)
	{
		chat = "#fs15#";
		if(!cm.haveItem(price_coin, price_coin_count))
		{
			chat += "필요한 아이템이 부족해요.";
		}
		else
		{
			if(select_item.getState() != 20) select_item.setState(20);
			switch(select_line)
			{
				case 1:
					select_item.setPotential1(base_poten);
					break;
				case 2:
					select_item.setPotential2(base_poten);
					break;
				case 3:
					select_item.setPotential3(base_poten);
					break;
				case 4:
					select_item.setPotential4(base_poten);
					break;
				case 5:
					select_item.setPotential5(base_poten);
					break;
				case 6:
					select_item.setPotential6(base_poten);
					break;
			}
			cm.getPlayer().forceReAddItem(select_item, Packages.client.inventory.MapleInventoryType.getByType(6));
			cm.gainItem(price_coin, -price_coin_count);
			chat += "#e#b#t" + select_item.getItemId() + "##n#k의 #e#b" + select_line + "번#n#k 슬롯에 #r" + new_poten_str + "#k 잠재능력이 부여되었습니다.";
		}
		cm.sendOk(chat);
		cm.dispose();
		return;
    }
}