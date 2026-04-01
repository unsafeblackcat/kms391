var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, sel) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        var chat = "#fs11#";
        chat += "안녕하세요! 저는 스카이에서  퀘스트를 진행하고 있는 #b슬라임#k이라고 해요. ";
        chat += "어떤 퀘스트를 진행하시겠어요?\r\n\r\n";
        if (cm.getPlayer().getLevel() < 10) {
            chat += "#L1# #fc0xFFF361DC##e[Lv.10] 주황버섯의 갓 30개 모아오기 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 10 && cm.getPlayer().getKeyValue(1000, "Level10_Quest") == 0) {
            chat += "#L1# #fc0xFFF361DC##e[Lv.10] 주황버섯의 갓 30개 모아오기 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 10 && cm.getPlayer().getKeyValue(1000, "Level10_Quest") == 1) {
            chat += "#L1# #fc0xFFF361DC##e[Lv.10] 주황버섯의 갓 30개 모아오기 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 10) {
            chat += "#L1# #fc0xFFF361DC##e[Lv.10] 주황버섯의 갓 30개 모아오기 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 50) {
            chat += "#L2# #fc0xFFF361DC##e[Lv.50] 와일드 보어의 송곳니 50개 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 50 && cm.getPlayer().getKeyValue(1000, "Level50_Quest") == 0) {
            chat += "#L2# #fc0xFFF361DC##e[Lv.50] 와일드 보어의 송곳니 50개 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 50 && cm.getPlayer().getKeyValue(1000, "Level50_Quest") == 1) {
            chat += "#L2# #fc0xFFF361DC##e[Lv.50] 와일드 보어의 송곳니 50개 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 50) {
            chat += "#L2# #fc0xFFF361DC##e[Lv.50] 와일드 보어의 송곳니 50개 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 80) {
            chat += "#L3# #fc0xFFF361DC##e[Lv.80] 루 광석 80개 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 80 && cm.getPlayer().getKeyValue(1000, "Level80_Quest") == 0) {
            chat += "#L3# #fc0xFFF361DC##e[Lv.80] 루 광석 80개 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 80 && cm.getPlayer().getKeyValue(1000, "Level80_Quest") == 1) {
            chat += "#L3# #fc0xFFF361DC##e[Lv.80] 루 광석 80개 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 80) {
            chat += "#L3# #fc0xFFF361DC##e[Lv.80] 루 광석 80개 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 100) {
            chat += "#L4# #fc0xFFF361DC##e[Lv.100] 게릴라 스펙터 100마리 처치 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 100 && cm.getPlayer().getKeyValue(1000, "Level100_Quest") == 0) {
            chat += "#L4# #fc0xFFF361DC##e[Lv.100] 게릴라 스펙터 100마리 처치 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 100 && cm.getPlayer().getKeyValue(1000, "Level100_Quest") == 1) {
            chat += "#L4# #fc0xFFF361DC##e[Lv.100] 게릴라 스펙터 100마리 처치 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 100) {
            chat += "#L4# #fc0xFFF361DC##e[Lv.100] 게릴라 스펙터 100마리 처치 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 140) {
            chat += "#L5# #fc0xFFF361DC##e[Lv.140] 추억의 사제 200마리 처치 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 140 && cm.getPlayer().getKeyValue(1000, "Level140_Quest") == 0) {
            chat += "#L5# #fc0xFFF361DC##e[Lv.140] 추억의 사제 200마리 처치 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 140 && cm.getPlayer().getKeyValue(1000, "Level140_Quest") == 1) {
            chat += "#L5# #fc0xFFF361DC##e[Lv.140] 추억의 사제 200마리 처치 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 140) {
            chat += "#L5# #fc0xFFF361DC##e[Lv.140] 추억의 사제 200마리 처치 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 150) {
            chat += "#L6# #fc0xFFF361DC##e[Lv.150] 노멀 혼테일 격파 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 150 && cm.getPlayer().getKeyValue(1000, "Level150_Quest") == 0) {
            chat += "#L6# #fc0xFFF361DC##e[Lv.150] 노멀 혼테일 격파 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 150 && cm.getPlayer().getKeyValue(1000, "Level150_Quest") == 1) {
            chat += "#L6# #fc0xFFF361DC##e[Lv.150] 노멀 혼테일 격파 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 150) {
            chat += "#L6# #fc0xFFF361DC##e[Lv.150] 노멀 혼테일 격파 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 180) {
            chat += "#L7# #fc0xFFF361DC##e[Lv.180] 상급기사D 300마리 처치 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 180 && cm.getPlayer().getKeyValue(1000, "Level180_Quest") == 0) {
            chat += "#L7# #fc0xFFF361DC##e[Lv.180] 상급기사D 300마리 처치 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 180 && cm.getPlayer().getKeyValue(1000, "Level180_Quest") == 1) {
            chat += "#L7# #fc0xFFF361DC##e[Lv.180] 상급기사D 300마리 처치 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 180) {
            chat += "#L7# #fc0xFFF361DC##e[Lv.180] 상급기사D 300마리 처치 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 200) {
            chat += "#L8# #fc0xFFF361DC##e[Lv.200] 블러디 퀸 머리 돌리기 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 200 && cm.getPlayer().getKeyValue(1000, "Level200_Quest") == 0) {
            chat += "#L8# #fc0xFFF361DC##e[Lv.200] 블러디 퀸 머리 돌리기 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 200 && cm.getPlayer().getKeyValue(1000, "Level200_Quest") == 1) {
            chat += "#L8# #fc0xFFF361DC##e[Lv.200] 블러디 퀸 머리 돌리기 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 200) {
            chat += "#L8# #fc0xFFF361DC##e[Lv.200] 블러디 퀸 머리 돌리기 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 205) {
            chat += "#L9# #fc0xFFF361DC##e[Lv.205] 아르마의 부하 200마리 처치 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 205 && cm.getPlayer().getKeyValue(1000, "Level205_Quest") == 0) {
            chat += "#L9# #fc0xFFF361DC##e[Lv.205] 아르마의 부하 200마리 처치 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 205 && cm.getPlayer().getKeyValue(1000, "Level205_Quest") == 1) {
            chat += "#L9# #fc0xFFF361DC##e[Lv.205] 아르마의 부하 200마리 처치 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 205) {
            chat += "#L9# #fc0xFFF361DC##e[Lv.205] 아르마의 부하 200마리 처치 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 210) {
            chat += "#L10# #fc0xFFF361DC##e[Lv.210] 레벨 210 달성하기 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 210 && cm.getPlayer().getKeyValue(1000, "Level210_Quest") == 0) {
            chat += "#L10# #fc0xFFF361DC##e[Lv.210] 레벨 210 달성하기 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 210 && cm.getPlayer().getKeyValue(1000, "Level210_Quest") == 1) {
            chat += "#L10# #fc0xFFF361DC##e[Lv.210] 레벨 210 달성하기 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 210) {
            chat += "#L10# #fc0xFFF361DC##e[Lv.210] 레벨 210 달성하기 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 215) {
            chat += "#L11# #fc0xFFF361DC##e[Lv.215] 잘익은 울프롯 300마리 처치 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 215 && cm.getPlayer().getKeyValue(1000, "Level215_Quest") == 0) {
            chat += "#L11# #fc0xFFF361DC##e[Lv.215] 잘익은 울프롯 300마리 처치 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 215 && cm.getPlayer().getKeyValue(1000, "Level215_Quest") == 1) {
            chat += "#L11# #fc0xFFF361DC##e[Lv.215] 잘익은 울프롯 300마리 처치 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 215) {
            chat += "#L11# #fc0xFFF361DC##e[Lv.215] 잘익은 울프롯 300마리 처치 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 220) {
            chat += "#L12# #fc0xFFF361DC##e[Lv.220] 레벨 220 달성하기 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 220 && cm.getPlayer().getKeyValue(1000, "Level220_Quest") == 0) {
            chat += "#L12# #fc0xFFF361DC##e[Lv.220] 레벨 220 달성하기 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 220 && cm.getPlayer().getKeyValue(1000, "Level220_Quest") == 1) {
            chat += "#L12# #fc0xFFF361DC##e[Lv.220] 레벨 220 달성하기 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 220) {
            chat += "#L12# #fc0xFFF361DC##e[Lv.220] 레벨 220 달성하기 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 225) {
            chat += "#L13# #fc0xFFF361DC##e[Lv.225] 족장 버샤크 500마리 처치 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 225 && cm.getPlayer().getKeyValue(1000, "Level225_Quest") == 0) {
            chat += "#L13# #fc0xFFF361DC##e[Lv.225] 족장 버샤크 500마리 처치 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 225 && cm.getPlayer().getKeyValue(1000, "Level225_Quest") == 1) {
            chat += "#L13# #fc0xFFF361DC##e[Lv.225] 족장 버샤크 500마리 처치 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 225) {
            chat += "#L13# #fc0xFFF361DC##e[Lv.225] 족장 버샤크 500마리 처치 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 230) {
            chat += "#L14# #fc0xFFF361DC##e[Lv.230] 레벨 230 달성하기 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 230 && cm.getPlayer().getKeyValue(1000, "Level230_Quest") == 0) {
            chat += "#L14# #fc0xFFF361DC##e[Lv.230] 레벨 230 달성하기 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 230 && cm.getPlayer().getKeyValue(1000, "Level230_Quest") == 1) {
            chat += "#L14# #fc0xFFF361DC##e[Lv.230] 레벨 230 달성하기 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 230) {
            chat += "#L14# #fc0xFFF361DC##e[Lv.230] 레벨 230 달성하기 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 232) {
            chat += "#L15# #fc0xFFF361DC##e[Lv.232] 노멀 스우 격파 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 232 && cm.getPlayer().getKeyValue(1000, "Level232_Quest") == 0) {
            chat += "#L15# #fc0xFFF361DC##e[Lv.232] 노멀 스우 격파 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 232 && cm.getPlayer().getKeyValue(1000, "Level232_Quest") == 1) {
            chat += "#L15# #fc0xFFF361DC##e[Lv.232] 노멀 스우 격파 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 232) {
            chat += "#L15# #fc0xFFF361DC##e[Lv.232] 노멀 스우 격파 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 235) {
            chat += "#L16# #fc0xFFF361DC##e[Lv.235] 아투스 200마리 처치 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 235 && cm.getPlayer().getKeyValue(1000, "Level235_Quest") == 0) {
            chat += "#L16# #fc0xFFF361DC##e[Lv.235] 아투스 200마리 처치 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 235 && cm.getPlayer().getKeyValue(1000, "Level235_Quest") == 1) {
            chat += "#L16# #fc0xFFF361DC##e[Lv.235] 아투스 200마리 처치 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 235) {
            chat += "#L16# #fc0xFFF361DC##e[Lv.235] 아투스 200마리 처치 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 240) {
            chat += "#L17# #fc0xFFF361DC##e[Lv.240] 레벨 240 달성하기 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 240 && cm.getPlayer().getKeyValue(1000, "Level240_Quest") == 0) {
            chat += "#L17# #fc0xFFF361DC##e[Lv.240] 레벨 240 달성하기 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 240 && cm.getPlayer().getKeyValue(1000, "Level240_Quest") == 1) {
            chat += "#L17# #fc0xFFF361DC##e[Lv.240] 레벨 240 달성하기 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 240) {
            chat += "#L17# #fc0xFFF361DC##e[Lv.240] 레벨 240 달성하기 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 245) {
            chat += "#L18# #fc0xFFF361DC##e[Lv.245] 어둠의 집행자 800마리 처치 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 245 && cm.getPlayer().getKeyValue(1000, "Level245_Quest") == 0) {
            chat += "#L18# #fc0xFFF361DC##e[Lv.245] 어둠의 집행자 800마리 처치 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 245 && cm.getPlayer().getKeyValue(1000, "Level245_Quest") == 1) {
            chat += "#L18# #fc0xFFF361DC##e[Lv.245] 어둠의 집행자 800마리 처치 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 245) {
            chat += "#L18# #fc0xFFF361DC##e[Lv.245] 어둠의 집행자 800마리 처치 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 250) {
            chat += "#L19# #fc0xFFF361DC##e[Lv.250] 혼돈의 피조물 500마리 처치 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 250 && cm.getPlayer().getKeyValue(1000, "Level250_Quest") == 0) {
            chat += "#L19# #fc0xFFF361DC##e[Lv.250] 혼돈의 피조물 500마리 처치 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 250 && cm.getPlayer().getKeyValue(1000, "Level250_Quest") == 1) {
            chat += "#L19# #fc0xFFF361DC##e[Lv.250] 혼돈의 피조물 500마리 처치 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 250) {
            chat += "#L19# #fc0xFFF361DC##e[Lv.250] 혼돈의 피조물 500마리 처치 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 255) {
            chat += "#L20# #fc0xFFF361DC##e[Lv.255] 절망의 날개 500마리 처치 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 255 && cm.getPlayer().getKeyValue(1000, "Level255_Quest") == 0) {
            chat += "#L20# #fc0xFFF361DC##e[Lv.255] 절망의 날개 500마리 처치 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 255 && cm.getPlayer().getKeyValue(1000, "Level255_Quest") == 1) {
            chat += "#L20# #fc0xFFF361DC##e[Lv.255] 절망의 날개 500마리 처치 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 255) {
            chat += "#L20# #fc0xFFF361DC##e[Lv.255] 절망의 날개 500마리 처치 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 260) {
            chat += "#L21# #fc0xFFF361DC##e[Lv.260] 안세스티온 1000 마리 처치 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 260 && cm.getPlayer().getKeyValue(1000, "Level260_Quest") == 0) {
            chat += "#L21# #fc0xFFF361DC##e[Lv.260] 안세스티온 1000 마리 처치 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 260 && cm.getPlayer().getKeyValue(1000, "Level260_Quest") == 1) {
            chat += "#L21# #fc0xFFF361DC##e[Lv.260] 안세스티온 1000 마리 처치 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 260) {
            chat += "#L21# #fc0xFFF361DC##e[Lv.260] 안세스티온 1000 마리 처치 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 265) {
            chat += "#L22# #fc0xFFF361DC##e[Lv.265] 레벨 265 달성하기 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 265 && cm.getPlayer().getKeyValue(1000, "Level265_Quest") == 0) {
            chat += "#L22# #fc0xFFF361DC##e[Lv.265] 레벨 265 달성하기 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 265 && cm.getPlayer().getKeyValue(1000, "Level265_Quest") == 1) {
            chat += "#L22# #fc0xFFF361DC##e[Lv.265] 레벨 265 달성하기 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 265) {
            chat += "#L22# #fc0xFFF361DC##e[Lv.265] 레벨 265 달성하기 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 270) {
            chat += "#L23# #fc0xFFF361DC##e[Lv.270] 레벨 270 달성하기 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 270 && cm.getPlayer().getKeyValue(1000, "Level270_Quest") == 0) {
            chat += "#L23# #fc0xFFF361DC##e[Lv.270] 레벨 270 달성하기 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 270 && cm.getPlayer().getKeyValue(1000, "Level270_Quest") == 1) {
            chat += "#L23# #fc0xFFF361DC##e[Lv.270] 레벨 270 달성하기 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 270) {
            chat += "#L23# #fc0xFFF361DC##e[Lv.270] 레벨 270 달성하기 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 272) {
            chat += "#L24# #fc0xFFF361DC##e[Lv.272] 엠브리온 1000마리 처치 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 272 && cm.getPlayer().getKeyValue(1000, "Level272_Quest") == 0) {
            chat += "#L24# #fc0xFFF361DC##e[Lv.272] 엠브리온 1000마리 처치 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 272 && cm.getPlayer().getKeyValue(1000, "Level272_Quest") == 1) {
            chat += "#L24# #fc0xFFF361DC##e[Lv.272] 엠브리온 1000마리 처치 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 272) {
            chat += "#L24# #fc0xFFF361DC##e[Lv.272] 엠브리온 1000마리 처치 #fc0xFFA566FF#(시작가능)#n\r\n";
        }

        if (cm.getPlayer().getLevel() < 275) {
            chat += "#L25# #fc0xFFF361DC##e[Lv.275] 레벨 275 달성하기 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 275 && cm.getPlayer().getKeyValue(1000, "Level275_Quest") == 0) {
            chat += "#L25# #fc0xFFF361DC##e[Lv.275] 레벨 275 달성하기 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 275 && cm.getPlayer().getKeyValue(1000, "Level275_Quest") == 1) {
            chat += "#L25# #fc0xFFF361DC##e[Lv.275] 레벨 275 달성하기 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 275) {
            chat += "#L25# #fc0xFFF361DC##e[Lv.275] 레벨 275 달성하기 #fc0xFFA566FF#(시작가능)#n\r\n";
        }
        if (cm.getPlayer().getLevel() < 10) {
            chat += "#L26# #fc0xFFF361DC##e[Lv.10] 에테르넬 #fc0xFFA566FF#(레벨 부족)#n\r\n";
        } else if ( cm.getPlayer().getKeyValue(1000, "Eternal") == 0) {
            chat += "#L26# #fc0xFFF361DC##e[Lv.10] 에테르넬 #fc0xFFA566FF#(진행중)#n\r\n";
        } else if (cm.getPlayer().getKeyValue(1000, "Eternal") == 1) {
            chat += "#L26# #fc0xFFF361DC##e[Lv.10] 에테르넬 #fc0xFFA566FF#(완료)#n\r\n";
        } else if (cm.getPlayer().getLevel() >= 10) {
            chat += "#L26# #fc0xFFF361DC##e[Lv.10] 에테르넬 #fc0xFFA566FF#(시작가능)#n\r\n";
        }
        cm.sendSimple(chat);

    } else if (status == 1) {
        switch (sel) {
            case 1:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level10_Quest");
                break;
            case 2:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level50_Quest");
                break;
            case 3:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level80_Quest");
                break;
            case 4:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level100_Quest");
                break;
            case 5:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level140_Quest");
                break;
            case 6:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level150_Quest");
                break;
            case 7:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level180_Quest");
                break;
            case 8:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level200_Quest");
                break;
            case 9:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level205_Quest");
                break;
            case 10:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level210_Quest");
                break;
            case 11:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level215_Quest");
                break;
            case 12:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level220_Quest");
                break;
            case 13:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level225_Quest");
                break;
            case 14:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level230_Quest");
                break;
            case 15:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level232_Quest");
                break;
            case 16:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level235_Quest");
                break;
            case 17:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level240_Quest");
                break;
            case 18:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level245_Quest");
                break;
            case 19:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level250_Quest");
                break;
            case 20:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level255_Quest");
                break;
            case 21:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level260_Quest");
                break;
            case 22:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level265_Quest");
                break;
            case 23:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level270_Quest");
                break;
            case 24:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level272_Quest");
                break;
            case 25:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000385, "Level275_Quest");
                break;
                case 26:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9000174, "Eternal");
                break;
        }
    }
}