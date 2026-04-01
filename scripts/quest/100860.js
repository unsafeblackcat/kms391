importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);
importPackage(Packages.tools.packet);
importPackage(Packages.handling.world);
importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.tools.packet);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
importPackage(Packages.server.enchant);
importPackage(java.sql);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);
importPackage(Packages.scripting);

var status = -1, sel = 0;

function start(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        d = status;
        status++;
    }


    if (status == 0) {
        qm.sendSimpleS("#h0#!\r\n예티. 알려준다. <메이플 LIVE>\r\n#L0# #b#e<메이플 LIVE>#n에 대해 알려줘.#k#l\r\n#L1# #b#e<크리에이터 스텝업>#n에 대해 알려줘.#k#l\r\n#L2# #b#e<분홍콩>#n에 대해 알려줘.#k#l\r\n#L3# #b#e<검은콩>#n에 대해 알려줘.#k#l\r\n#L4# #b#e<메이플 버라이어티>#n에 대해 알려줘.#k#l\r\n#L5# #b#e<메이플 ON AIR>#n에 대해 알려줘.#k#l\r\n#L6# #b#e<메이플 E-SPORTS>#n에 대해 알려줘.#k#l\r\n#L7# #b#e<메이플 STUDIO GO!>#n에 대해 알려줘.#k#l", 4, 9062555)
    } else if (status == 1) {
        sel = selection
        switch (sel) {
            case 0:
                qm.sendNextS("예티. 설명 잘 한다.", 4, 9062555)
                break;
            case 1:
                qm.sendNextS("\r\n#b#e<크리에이터 스텝업>#n#k은 핑크빈의 새로은 프로젝트다!\r\n사실 근데 자세한 내용은 모른다...\r\n예티가 핑크빈 불러오겠다!", 4, 9062555)
                break;
            case 2:
                qm.sendNextS("\r\n#b#e<분홍콩>#n#k은 구독자님들의 선물이다!\r\n#r#e분홍콩샵#n#k에서 다양한 물건으로 바꿀 수 있다!", 4, 9062555)
                break;
            case 3:
                qm.sendNextS("\r\n#b#e<검은콩>#n#k은 구독자님들의 조금 더 특별한 선물이다!\r\n근데 사실 자세한 내용은 모른다...\r\n예티가 전문가 불러오겠다.", 4, 9062555)
                break;
            case 4:
                qm.sendNextS("\r\n#b#e<메이플 버라이어티>#n#k는 크리에이터들이 모여서 진행하는 버라이어티 방송이다.\r\n근데 사실 자세한 내용은 모른다...\r\n예티가 전문가 불러오겠다.", 4, 9062555)
                break;
            case 5:
                qm.sendNextS("\r\n#b#e<메이플 ON AIR>#n#k는 방송이다.\r\n예티는 사냥 방송이란 것 밖에 모른다...\r\n예티가 사냥 전문가 불러오겠다!", 4, 9062555)
                break;
            case 6:
                qm.sendNextS("\r\n메이플 LIVE에서 E-SPORTS 개최했다.\r\n예티 인터넷 게임 잘 모른다...\r\n예티가 전문가 데려온다!", 4, 9062555)
                break;
            case 7:
                qm.sendNextS("LIVE 스튜디오에서 펼쳐진다!\r\n생방송 이벤트! #r#e<STUDIO GO!>#n#k", 4, 9062555)
                break;

        }
    } else if (status == 2) {
        switch (sel) {
            case 0:
                qm.sendNextPrevS("#b#e<메이플 LIVE>#n#k는 누구나 방송을 만들 수 있는 방송 플랫폼이다.", 4, 9062555)
                break;
            case 1:
                qm.sendNextPrevS("#r#e크리에이터 스텝업#n#k에 대해 궁금해?", 4, 9062554)
                break;
            case 2:
                qm.sendNextPrevS("핑크빈의 크리에이터 스텝업을 진행하면서 방송을 하면 구독자가 늘어나고 #b#e#i4310312##t4310312##n#k도 얻을 수 있다!", 4, 9062555)
                break;
            case 3:
                qm.sendNextS("흐흐. #b#e#i4310313##t4310313##n#k이 궁금하다고?", 4, 9062558)
                break;
            case 4:
                qm.sendNextS("\r\n안녕하세요! \r\n지금 진행하고 있는 <메이플 버라이어티>는 바로! \r\n\r\n#fs20##e#b탕윤 식당 !!", 4, 9062550)
                break;
            case 5:
                qm.sendNextS("\r\n이 사냥 천재 프리토님께서 특별히 준비한 사냥 방송!\r\n#b#e<메이플 ON AIR>#n#k가 궁금하군.", 4, 9062549)
                break;
            case 6:
                qm.sendNextS("\r\n하핫! 수많은 크리에이터들의 미니게임 멸망전!!\r\n또 한 명의 참가 희망자인가? 그럼 잘 들어보라고!", 4, 9062545)
                break;
            case 7:
                qm.sendNextS("매일 #b#e오전 10시#k#n부터 #b#e다음날 오전 1시 30분#k#n까지,\r\n#b#e매시 30분에 10분 동안#k#n LIVE 스튜디오에 방문만 하면\r\n\r\n시청자가 주는 #r특별한 버프 효과#k를 받는다!", 4, 9062555)
                break;
        }
    } else if (status == 3) {
        switch (sel) {
            case 0:
                qm.sendNextPrevS("다들 #r#e슈퍼스타#n#k를 꿈꾼다. \r\n하지만 재밌는 방송 만들기는 어렵다.", 4, 9062555)
                break;
            case 1:
                qm.sendNextPrevS("#r#e크리에이터 스텝업#n#k은 내가 초보 크리에이터를 위해 준비한 스텝업 미션이야! \r\n미션을 수행하면서 그 모습을 방송으로 촬영하는 거야!", 4, 9062554)
                break;
            case 2:
                qm.sendNextPrevS("#b#e#i4310312##t4310312##n#k은 캐릭터당 일일 #r#e300개#n#k까지 획득할 수 있다!", 4, 9062555)
                break;
            case 3:
                qm.sendNextPrevS("#b#e#i4310313##t4310313##n#k은 아주 강한 보스를 처치하는 방송! \r\n바로 #r#e주간 미션을 클리어#n#k하면 받을 수 있어!", 4, 9062558)
                break;
            case 4:
                qm.sendNextPrevS("\r\n#b#e<탕윤 식당>#n#k은 최소 1명 ~ 최대 3명의 인원이 요리를 만들고 배달까지 완료해서 제한 시간 #e30분#n 동안 #b#e50,000#n#k 포인트를 획득하는 것이 목표랍니다!", 4, 9062550)
                break;
            case 5:
                qm.sendNextPrevS("이번 #b#e<메이플 ON AIR>#n#k는 크리에이터라면 필수로 갖춰야 할 리액션을 배우게 돼. 이름하여 #b#e팡팡 리액션#n#k!", 4, 9062549)
                break;
            case 6:
                qm.sendNextPrevS("\r\n#b#e<해변의 지배자>#n#k는 최소 10명 ~ 최대 40명의 인원이 모여 물방울을 쏴서 상대를 공격하고 제한 시간 3분 동안 #b더 많은 점수를 획득한 팀#k이 승리하지!", 4, 9062545)
                break;
            case 7:
                qm.sendNextS("#b#e짝수 시간의 30분#n#k에는 #b#e블루 스튜디오#n#k에서! \r\n\r\n\r\n #e- 블루 스튜디오 : 30분간 #r경험치 15%#k 증가", 4, 9062555)
                break;
        }
    } else if (status == 4) {
        switch (sel) {
            case 0:
                qm.sendNextPrevS("#b#e<메이플 LIVE>#n#k에 많은 크리에이터들은 구독자를 늘리기 위해 열심히 방송한다.", 4, 9062555)
                break;
            case 1:
                qm.sendNextPrevS("미션을 완료하고 방송을 등록하면 #r#e#i4310314# 구독자#n#k가 쑥쑥!\r\n\r\n구독자님들의 선물 #b#e#i4310312##t4310312##n#k과 #b#e#i4310313##t4310313##n#k도 쑥쑥!\r\n\r\n\r\n #r ※ 미션을 완료하면 구독자와 콩을 받을 수 있습니다.", 4, 9062554)
                break;
            case 2:
                qm.sendNextPrevS("참고로 분홍콩샵은 우리 대장이 운영하고 있다...", 4, 9062555)
                break;
            case 3:
                qm.sendNextPrevS("주간 보스를 처치하면 #b#e#i2633609##t2633609##n#k가 드롭될 거야! \r\n그걸 꼭 주워야 해!\r\n\r\n\r\n #r ※ 검은콩 뭉치는 #e2020년 6월 24일 (목) 0시#n 이후 드롭됩니다.", 4, 9062558)
                break;
            case 4:
                qm.sendNextPrevS("\r\n좌측에 위치한 #b#e<주문판>#n#k에 들어오는 주문을 확인하고\r\n레시피대로 음식을 만들어서 배달해보세요~!", 4, 9062550)
                break;
            case 5:
                qm.sendNextPrevS("#b#e팡팡 리액션#n#k은 #r500마리#k의 #r레벨 범위 몬스터#k를 처치할 때마다, 시청자들이 후원하여 등장하는 #b#e메이플 팡팡 주머니#n#k를 터뜨리는 거야.", 4, 9062549)
                break;
            case 6:
                qm.sendNextPrevS("\r\n#e[팀 구분]#n\r\n\r\n게임에 입장하면 #rRED#k 또는 #bBLUE#k 팀에 소속될 거야.\r\n\r\n소속팀에 따라 캐릭터의 #e명찰, HP, 사용하는 스킬의 색상#n이 다르니까 전장에서 아군과 적군을 구별할 수 있겠지?\r\n\r\n#fUI/UIWindow5.img/2021WaterGunGame/tuto/0#", 4, 9062545)
                break;
            case 7:
                qm.sendNextS("#b#e홀수 시간의 30분#n#k에는 #r#e핑크 스튜디오#n#k에서! \r\n\r\n\r\n #e- 핑크 스튜디오 : 30분간 #b올스탯 15#k 증가,\r\n                       #b최대 HP/MP 1500#k 증가,\r\n                       #b공격력/마력 15#k 증가,\r\n                       #b보스 몬스터 데미지 15%#k 증가,\r\n                       #b방어율 무시 15%#k 증가", 4, 9062555)
                break;
        }
    } else if (status == 5) {
        switch (sel) {
            case 0:
                qm.sendNextPrevS("하지만 예티는 조금 질렸다. 인기인의 삶.", 4, 9062555)
                break;
            case 1:
                qm.sendNextPrevS("#r#e#i4310314# 구독자#n#k님들이 늘어나서 점점 유명해지면 특별한 선물도 받을 수 있지! \r\n\r\n\r\n #r ※ 구독자 1000명이 증가할 때마다 크리에이터 스텝업 보상을 받을 수 있습니다.", 4, 9062554)
                break;
            case 2:
                qm.dispose();
                NPCScriptManager.getInstance().startQuest(qm.getClient(), 9062555, 100860);
                break;
            case 3:
                qm.sendNextPrevS("그리고 주간 미션을 완료하고 방송을 올리면 #b#e#i4310313##t4310313##n#k을 받을 수 있어!", 4, 9062558)
                break;
            case 4:
                qm.sendNextPrevS("\r\n#e[재료 수집]#n\r\n\r\n#b#e주방 공간 좌측#n#k에 위치한 #e5종#n의 재료 앞에서 #r#eNPC/채집키#k#n키다운으로 해당하는 재료를 획득할 수 있답니다.\r\n\r\n", 4, 9062550)
                break;
            case 5:
                qm.sendNextPrevS("#b#e메이플 팡팡 주머니#n#k는 리액션 #r1번#k에 최대 #r1분#k 동안 일정 주기마다 나뉘어서 등장해.", 4, 9062549)
                break;
            case 6:
                qm.sendNextPrevS("\r\n#e[이동]#n\r\n\r\n캐릭터는 마우스 커서가 있는 방향을 바라보게 돼!\r\n키보드 #r#eW,A,S,D#k#n를 누르면 #r#e캐릭터를 이동#k#n시킬 수 있고,\r\n키보드 #b#eSPACE 바#k#n를 누르면 #b#e이동 중인 방향#k#n으로 순간적으로 #b#e돌진#k#n할 수 있어.\r\n\r\n#fUI/UIWindow5.img/2021WaterGunGame/tuto/1#", 4, 9062545)
                break;
            case 7:
                qm.sendNextS("매일 정해진 시간, 생방송에 참여한다!\r\n시청자가 주는 하트 선물, 꼭 받는다! \r\n\r\n#eLIVE 스튜디오로 GO! GO!#k", 4, 9062555)
                break;
        }
    } else if (status == 6) {
        switch (sel) {
            case 0:
                qm.dispose();
                NPCScriptManager.getInstance().startQuest(qm.getClient(), 9062555, 100860);
                break;
            case 1:
                qm.sendNextPrevS("매일 방송을 꾸준히 진행하는 것이 중요하니 일일 미션은 \r\n꾸준히 참여하고 #b#e#i4310312# #t4310312##n#k을 모아봐!\r\n참! 하루에 늘어나는 구독자 최대 수는 정해져 있어!\r\n\r\n\r\n #r ※ 일일 미션은 매일 0시에 리셋되며, 일일 미션 구독자는 월드 당 최대 300명만 증가합니다.", 4, 9062554)
                break;
            case 3:
                qm.sendNextPrevS("흐흐. 검은콩을 가지고 나를 찾아오면 아주 특별하고 강한 물건과 바꿔줄게!", 4, 9062558)
                break;
            case 4:
                qm.sendNextPrevS("\r\n#e[재료 놓기]#n\r\n\r\n#b#e주방 공간 중앙#n#k에 위치한 #e조리대#n 앞에서 #r#eNPC/채집키#k#n 키다운으로 현재 획득한 재료를 놓을 수 있답니다.\r\n\r\n#e각 주문 번호에 해당하는 조리대#n에서만 해당 음식을\r\n조리할 수 있다는 점을 잊지마세요!", 4, 9062550)
                break;
            case 5:
                qm.sendNextPrevS("#b#e메이플 팡팡 주머니#n#k를 빨리 터뜨리면, 리액션 타임을 더 빠르게 끝낼 수 있지. #b#e팡팡 리액션#n#k은 하루에 #r10번#k까지 가능해.", 4, 9062549)
                break;
            case 6:
                qm.sendNextPrevS("\r\n#e[공격] - 기본#n\r\n\r\n#b#e마우스 좌 클릭#k#n을 하면 마우스 커서의 방향으로 #b#e물방울#k#n을 던질 수 있어. 물방울을 던져 상대팀 캐릭터를 맞추면 상대의 #b#eHP#k#n를 깎을 수 있지.\r\n\r\n#fUI/UIWindow5.img/2021WaterGunGame/tuto/2#", 4, 9062545)
                break;
            case 7:
                qm.sendNextS("#e[이벤트 기간]#n\r\n - 2021년 9월 8일 23시 59분까지#n", 4, 9062555)
                break;
        }
    } else if (status == 7) {
        switch (sel) {
            case 1:
                qm.sendNextPrevS("아! 특별히 강한 보스를 잡는 방송은 #b#e#i4310313# #t4310313##n#k을 받을 수 있어!\r\n\r\n\r\n #r ※ 주간 미션은 매주 목요일 0시에 리셋되며 진행 상황은 월드 내 공유됩니다.", 4, 9062554)
                break;
            case 3:
            case 7:
                qm.dispose();
                NPCScriptManager.getInstance().startQuest(qm.getClient(), 9062555, 100860);
                break;
            case 4:
                qm.sendNextPrevS("\r\n#e[가공 도구 들기]#n\r\n\r\n#b#e주방 공간 우측#n#k에 위치한 #e3종#n의 가공 도구 앞에서\r\n#r#eNPC/채집키#k#n 키다운으로 도구를 획득할 수 있답니다.\r\n\r\n#b#e가공 도구#n#k를 획득한 상태에서만 재료 가공을 할 수 있다는 점을 잊지마세요!", 4, 9062550)
                break;
            case 5:
                qm.sendNextPrevS("사냥하면 등장하는 #b#e메이플 팡팡 주머니#n#k를 터뜨려! 그러면 획득하는 #b빵빵한 경험치#k와 함께 #b인기 크리에이터#k로 마음껏 성장해봐!\r\n\r\n#e[이벤트 기간]#n\r\n - 2021년 7월 14일 23시 59분까지\r\n\r\n※팡팡 리액션 횟수는 매일 자정에 초기화 됩니다.\r\n※이미 필드에 팡팡 리액션이 진행중인 경우 팡팡 리액션을 발동할 수 없습니다.", 4, 9062549)
                break;
            case 6:
                qm.sendNextPrevS("\r\n#e[공격] - 강화#n\r\n\r\n그리고 맵 곳곳에 나타나는 #b#e<물방울>#k#n에 충돌하면 #b#e20초간 기본 공격이 강화#k#n돼서, 클릭 한 번에 더 높은 공격력을 가진 물방울이 4개씩 던져질 거야.\r\n\r\n#fUI/UIWindow5.img/2021WaterGunGame/tuto/3#", 4, 9062545)
                break;
        }
    } else if (status == 8) {
        switch (sel) {
            case 1:
                qm.sendNextPrevS("그리고 특별한 도전 방송! \r\n#r#e월간 미션#n#k으로 너의 한계를 시험해 봐!\r\n\r\n\r\n #r ※ 월간 미션은 4주 단위로 리셋되며 진행 상황은 월드 내 공유됩니다.\r\n #r ※ 2021년 7월 15일 (목) 0시 리셋\r\n #r ※ 2021년 8월 12일 (목) 0시 리셋", 4, 9062554)
                break;
            case 4:
                qm.sendNextPrevS("\r\n#e[재료 가공]#n\r\n\r\n#b#e주방 공간 중앙#n#k에 위치한 #e조리대#n 앞에서 #r#eNPC/채집키#n#k 키다운으로 해당 조리대에 놓인 재료를 가공할 수 있답니다.\r\n\r\n#e각 주문 번호에 해당하는 조리대#n에서만 해당 음식을\r\n조리할 수 있다는 점을 잊지마세요!", 4, 9062550)
                break;
            case 5:
                qm.dispose();
                NPCScriptManager.getInstance().startQuest(qm.getClient(), 9062555, 100860);
                break;
            case 6:
                qm.sendNextPrevS("\r\n#e[점수]#n\r\n\r\n물방울 공격으로 상대의 HP가 0이되면 상대가 처치되고,\r\n#e처치한 캐릭터 1명 당 1점씩 #r개인 점수#k#n를 쌓을 수 있어!\r\n\r\n#fUI/UIWindow5.img/2021WaterGunGame/tuto/4#", 4, 9062545)
                break;

        }
    } else if (status == 9) {
        switch (sel) {
            case 1:
                qm.dispose();
                NPCScriptManager.getInstance().startQuest(qm.getClient(), 9062555, 100860);
                break;
            case 4:
                qm.sendNextPrevS("\r\n#e[음식 배달]#n\r\n\r\n#b#e음식이 완성된 조리대#n#k는 음식을 들 수 있는 상태가 되며\r\n해당 #e조리대#n 앞에서 #r#eNPC/채집키#k#n를 꾸욱 누르면\r\n배달을 진행할 수 있답니다.\r\n\r\n우측 배달 공간에서 #e몬스터#n들의 방해를 피해 주문한 손님\r\n앞에서 #r#eNPC/채집키#k#n를 꾸욱 누르면 완료!", 4, 9062550)
                break;
            case 6:
                qm.sendNextPrevS("\r\n#e[점수]#n\r\n\r\n그리고 #e1분마다#n 맵 중앙에 등장하는 #e야자수#n를 처치하면\r\n10개의 #r#e<빛 방울>#k#n이 주변에 나타나고,\r\n<빛 방울>에 충돌하면 #e1개당 1점씩 #r개인 점수#k#n를 추가로 누적할 수 있지!\r\n\r\n#fUI/UIWindow5.img/2021WaterGunGame/tuto/5#", 4, 9062545)
                break;
        }
    } else if (status == 10) {
        switch (sel) {
            case 4:
                qm.sendNextPrevS("\r\n#b#e진정한 크리에이터#n#k라면 역시 실전이죠!\r\n빠르게 직접 체험해보시는 것은 어떠신가요?", 4, 9062550)
                break;
            case 6:
                qm.sendNextPrevS("\r\n게임이 종료되면 획득한 개인 점수와 팀의 게임 결과에 따라 #r#e게임 포인트#k#n를 보상으로 줄게.", 4, 9062545)
                break;
        }
    } else if (status == 11) {
        switch (sel) {
            case 4:
                qm.sendNextPrevS("#e[이벤트 기간]\r\n\r\n2021년 06월 17일 (목) 점검 후 ~ \r\n2021년 07월 14일 (수) 오후 11시 59분", 4, 9062550)
                break;
            case 6:
                qm.sendNextPrevS("\r\n우리 팀이 대결에서 지더라도 내가 열심히 참여했다면 승리에 준하는 보상을 받아 갈 수 있으니 꼭 멋진 대결을 펼쳐 줘!", 4, 9062545)
                break;
        }
    } else if (status == 12) {
        switch (sel) {
            case 4:
            case 6:
                qm.dispose();
                NPCScriptManager.getInstance().startQuest(qm.getClient(), 9062555, 100860);
                break;
        }
    }
}
function statusplus(millsecond) {
    qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}