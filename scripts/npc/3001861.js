


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    블랙 에 의해 만들어 졌습니다.

    엔피시아이디 : 3001860

    엔피시 이름 : 세드릭

    엔피시가 있는 맵 : The Black : Night Festival (100000000)

    엔피시 설명 : MISSINGNO


*/

importPackage(Packages.server);
importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.server.enchant);
importPackage(java.sql);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);

검정 = "#fc0xFF191919#"
레드 = "#fc0xFFF15F5F#"
블루 = "#fc0xFF6B66FF#"
남색 = "#fc0xFF2457BD#"
노랑 = "#fc0xFF998A00#"
보라 = "#fc0xFF6E2FC7#"
초록 = "#fc0xFF47C83E#"
주황 = "#fc0xFFFF8224#"
펫 = "#fUI/CashShop.img/CashItem_label/9#";

var skillid = 0;
var skillname = "";
var skilltesc = "";
var level = 0;
var 구매 = -1;
var 설명 = -1;
var suc = 50;
var firstpet;
var secondpet;
var check = false;

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
        if (status == 19) {
            cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(false, false, false, false));
            cm.getClient().send(SLFCGPacket.SetStandAloneMode(false));
            cm.warp(100000051, 10);
            cm.sendOkS("마음이 바뀌면 언제든 다시 찾아오게나. 크크.", 4, 2192030)
            cm.dispose();
            return;
        } else if (status = 3) {
            cm.sendOkS("음... 뭐 고민할 시간이 필요한가 보구만, 알겠네. 크크.", 4, 2192030)
            cm.dispose();
            return;
        }
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        말 = "#fs11#" + 검정 + "#h #! 자네는 " + 보라 + "PRAY 쁘띠 펫#k" + 검정 + "을 가지고 있나? 성능을 극대화 시킬수 있는 방법을 알려주겠네! 크크\r\n";
        말 += "#fc0xFFD5D5D5#────────────────────────────#k#fs11#\r\n";
        말 += "#L1##bPRAY 쁘띠 펫을 성장시키고 싶어요.\r\n";
        말 += "#L2##b재료 아이템을 구매하고 싶어요.\r\n";
        말 += "#L0##r무엇인지 궁금해요.\r\n";
        cm.sendSimpleS(말, 0x04, 2192030);
    } else if (status == 1) {
        if (selection == 0) {
            설명 = 1
            말 = "#fs11#" + 검정 + "우선 자네는 " + 펫 + " " + 보라 + "PRAY 쁘띠 펫#k" + 검정 + "이 있다는 가정하에 설명하겠네!\r\n\r\n";
            말 += "우선, PRAY 쁘띠는 " + 레드 + "1기, 2기, 3기#k" + 검정 + "로 나뉘어져 있다네.\r\n";
            말 += "1기와 2기펫" + 검정 + "은 #b#z5069100##k" + 검정 + "을 이용해 뽑기가 가능하지만,\r\n3기펫은 " + 남색 + "성장 시스템#k" + 검정 + "으로만 구할수 있다네.\r\n";
            말 += "성장 성공 확률은 #r50%#k" + 검정 + " 이며, #i4310014# #b#z4310014##k" + 검정 + " 아이템을 소지하면 추가로 20%가 올라간다네.";
            cm.sendNextS(말, 0x04, 2192030);
        } else if (selection == 1) {
            구매 = 2;
            if (!cm.haveItem(5069101, 1)) {
                cm.sendOkS("#fs11#" + 검정 + "자네는 #i5069101# #b#z5069101##k" + 검정 + "이 없는거 같네.", 0x04, 2192030);
                cm.dispose();
                return;
            }
            leftslot = cm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot();
            leftslot1 = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
            if (leftslot < 2 && leftslot1 < 2) {
                cm.sendOk("#fs11##r소비창과 기타창 2 칸 이상을 확보하게.");
                cm.dispose();
                return;
            }
            var c = false;
            inz = cm.getInventory(5)
            txt = 검정 + "#fs11#자, 그렇다면 #r성장#k" + 검정 + " 시킬 첫번째 아이를 골라보게나.\r\n\r\n";
            for (w = 0; w < inz.getSlotLimit(); w++) {
                if (!inz.getItem(w) || inz.getItem(w).getPet() == null) {
                    continue;
                }
                if ((inz.getItem(w).getItemId() >= 5000930 && inz.getItem(w).getItemId() <= 5000932)) {
                    var check = false;
                    for (i = 0; i < cm.getPlayer().getPets().length; i++) {
                        if (cm.getPlayer().getPets()[i] != null) {
                            if (cm.getPlayer().getPets()[i].getInventoryPosition() == inz.getItem(w).getPet().getInventoryPosition()) {
                                check = true;
                                break;
                            }
                        }
                    }
                    if (!check) {
                        c = true;
                        txt += "#L" + w + "##i" + inz.getItem(w).getItemId() + "# #z" + inz.getItem(w).getItemId() + "##l\r\n";
                    }
                }
            }
            if (!c) {
                cm.sendOkS("#fs11#" + 검정 + "이봐, #h # 성장 시킬수 있는 펫이 없는것 같은데?", 0x04, 2192030);
                cm.dispose();
            } else {
                cm.sendSimpleS(txt, 0x04, 2192030);
            }
        } else if (selection == 2) {
            구매 = 1;
            말 = "#fs11#" + 검정 + "성능을 극대화 시키기 위한 과정에 꼭 필요한 아이템이라네!\r\n"
            말 += "#L0##fs11#" + 검정 + "#i5069101# #z5069101##l\r\n"
            말 += "　　　　　　└#b200,000,000 메소#k" + 검정 + "\r\n"
            말 += "　　　　　　└#b#z4310012# #b300 개#k" + 검정 + ""
            cm.sendSimpleS(말, 0x04, 2192030);
        }
    } else if (status == 2) {
        if (구매 >= 0) {
            if (구매 == 1) {
                if (!cm.haveItem(4310012, 300) && cm.getPlayer().getMeso() < 200000000) {
                    cm.sendOkS("자네는 #z5069101#을 구매하기 위한 비용이 부족한거 같네.", 4, 2192030);
                } else {
                    말 = "#fs11#" + 검정 + "구매에 성공했네, 나도 성장에 응원하겠네! 크크.\r\n\r\n"
                    말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
                    말 += "#i5069101# #z5069101#\r\n\r\n"
                    말 += "#L0##b성장시키러 가겠습니다.\r\n"
                    말 += "#L1##r대화 그만하기"
                    cm.sendSimpleS(말, 0x04, 2192030);
                    cm.gainItem(4310012, -300);
                    cm.gainMeso(-200000000);
                    cm.gainItem(5069101, 1);
                }
            } else if (구매 == 2) {
                firstpet = cm.getInventory(5).getItem(selection);
                var c = false;
                inz = cm.getInventory(5)
                txt = 검정 + "#fs11#자, 그렇다면 #r성장#k" + 검정 + " 시킬 두번째 아이를 골라보게나.\r\n\r\n";
                for (w = 0; w < inz.getSlotLimit(); w++) {
                    if (!inz.getItem(w) || inz.getItem(w).getPet() == null) {
                        continue;
                    }
                    if ((inz.getItem(w).getItemId() >= 5002079 && inz.getItem(w).getItemId() <= 5002081)) {
                        var check = false;
                        for (i = 0; i < cm.getPlayer().getPets().length; i++) {
                            if (cm.getPlayer().getPets()[i] != null) {
                                if (cm.getPlayer().getPets()[i].getInventoryPosition() == inz.getItem(w).getPet().getInventoryPosition()) {
                                    check = true;
                                    break;
                                }
                            }
                        }
                        if (!check) {
                            c = true;
                            txt += "#L" + w + "##i" + inz.getItem(w).getItemId() + "# #z" + inz.getItem(w).getItemId() + "##l\r\n";
                        }
                    }
                }
                if (!c) {
                    cm.sendOkS("#fs11#이봐, #h # 성장 시킬수 있는 #r2기#k 펫이 없는것 같은데?", 0x04, 2192030);
                    cm.dispose();
                } else {
                    cm.sendSimpleS(txt, 0x04, 2192030);
                }
            }

        } else if (설명 > 0) {
            if (설명 == 1) {
                말 = "#fs11#" + 초록 + "쁘띠 페어리 세트 펫#k\r\n";
                말 += "#d#i5000930# #z5000930# #i5000931# #z5000931# #i5000932# #z5000932##k" + 검정 + " 중 한마리와\r\n\r\n";
                말 += "" + 주황 + "쁘띠 포니 세트 펫#k\r\n";
                말 += "#d#i5002079# #z5002079# #i5002080# #z5002080# #i5002081# #z5002081##k" + 검정 + " 중 한마리를\r\n";
                말 += "선택해서 " + 남색 + "성장#k" + 검정 + "이 가능하다네.";
                cm.sendNextS(말, 0x04, 2192030);
            }
        }
    } else if (status == 3) {
        if (구매 == 2) {
            secondpet = cm.getInventory(5).getItem(selection);
            말 = "#fs11#" + 검정 + "자 정말 자네가 선택한 아이들이 이 아이들이 맞는가?\r\n\r\n";
            말 += "#i" + firstpet.getItemId() + "# #b#z" + firstpet.getItemId() + "##k\r\n"
            말 += "#i" + secondpet.getItemId() + "# #b#z" + secondpet.getItemId() + "##k\r\n"
            cm.sendYesNoS(말, 0x04, 2192030);
        } else {
            if (selection == 0) {
                cm.dispose();
                cm.openNpc(3001861);
            } else if (selection == 1) {
                cm.dispose();
            } else if (설명 == 1) {
                말 = "#fs11#" + 블루 + "쁘띠 초롱나비 세트 펫#k" + 검정 + "들은 엄청난 힘을 가지고 있다네.\r\n\r\n";
                말 += "#i5002197# 1마리의 효과로 " + 초록 + "쁘띠 페어리 3셋 옵션#k" + 검정 + "을 획득하고,\r\n";
                말 += "#i5002198# 2마리의 효과로 " + 주황 + "쁘띠 포니 3셋 옵션#k" + 검정 + "을 획득하며,\r\n";
                말 += "#i5002199# 3마리의 효과로,\r\n\r\n";
                말 += "" + 레드 + "보스 몬스터 공격 시 데미지 + 30% 효과#k" + 검정 + "와,\r\n";
                말 += "" + 레드 + "크리티컬 데미지 + 10% 효과#k" + 검정 + "를 획득한다네. 크크\r\n";
                cm.sendOkS(말, 0x04, 2192030);
                cm.dispose();
            }
        }
    } else if (status == 4) {
        if (!check) {
            cm.warp(100000009, 0);
            cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, true, false, false));
            cm.getClient().send(SLFCGPacket.CharReLocationPacket(-915, -47));
            cm.getClient().send(SLFCGPacket.SetStandAloneMode(true));
            cm.getClient().send(SLFCGPacket.MakeBlind(1, -1, 0, 0, 0, 500, 2));
            cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 1000, 0, -47, -250));
            cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x11, 1, 50, 0));
            npc1 = cm.spawnNpc2(9062342, new Point(-605, -47));
            cm.getClient().send(SLFCGPacket.SetNpcSpecialAction(npc1, "summon", 0, false));
            statusplus(1000);
            check = true;
        }
    } else if (status == 5) {
        cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 1000, 0));
        statusplus(2000);
    } else if (status == 6) {
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 6000, 1000, 6000, -750, -419));
        cm.getPlayer().dropMessage(-1, "숲 속 사이 요정들의 축복이 담긴 푸른 보름달 PRAY 드림이 드러납니다.");
        statusplus(6000);
    } else if (status == 7) {
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 6000, 600, 6000, -750, -100));
        statusplus(7000);
    } else if (status == 8) {
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 6000, 1300, 6000, -750, -100));
        statusplus(7000);
    } else if (status == 9) {
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x11, 2, 1, 0));
        cm.getClient().send(SLFCGPacket.Chatonchr(cm.getPlayer(), "여긴 뭐하는 곳이지..?", 3000));
        statusplus(3000);
    } else if (status == 10) {
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/oh", 2, 0, 0, 0, 0, 1, npc1, 0, 0, 0));
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/OnUserEff.img/emotion/oh", 2, 0, 0, 0, 0, 1, cm.getPlayer().getId(), 0, 0, 0));
        statusplus(3000);
    } else if (status == 11) {
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x11, 2, 200, 0));
        statusplus(1500);
    } else if (status == 12) {
        cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
        statusplus(1500);
    } else if (status == 13) {
        cm.sendNextS("어? #r#p9062342##k씨 복장이 바뀌셨네요!? 되게 분위기가 바뀌셨어요!", 0x39, 2);
    } else if (status == 14) {
        cm.sendNextS("자네에게 행운을 빌어주기 위해 나도 한번 옷을 갈아 입어봤다네, #r행운의 옷#k이 #h #에게 #r행운#k을 가져다 줄것이라네! 크크.", 0x25, 9062342);
    } else if (status == 15) {
        cm.sendNextS("#r#h #!#k 이곳을 소개하지! 이곳은 바로 요정들의 #b푸른 달빛의 축복#k을 받을 수 있는 PRAY 호수라네!", 0x25, 9062342);
    } else if (status == 16) {
        cm.sendNextS("이곳에선 #r특정한 행동#k을 한 후에 기도를 하면 #b기도가 이루어 진다는 전설이 있지#k", 0x25, 9062342);
    } else if (status == 17) {
        cm.sendNextS("(참! 분명 내가 #r초롱나비 세트 펫#k을 입양하기 위해 이쪽으로 온것이였지..?)\r\n\r\n우와! 그런 전설이..? 그렇다면 그 전설을 저에게 알려주세요! 저도 #b요정의 푸른 달빛의 축복#k을 받고 싶어요!", 0x39, 2);
    } else if (status == 18) {
        cm.sendNextS("크크, 하지만 기도가 이루어 지지 않을수도 있다네, 자네만 괜찮다면 내가 기도 #r방법#k을 상세히 알려주도록 하겠네!", 0x25, 9062342);
    } else if (status == 19) {
        if (cm.haveItem(4310014, 1)) {
            suc += 20;
        }
        cm.sendFriendsYesNo("자, 정말 마지막으로 묻겠네! #b푸른 요정들의 기운#k을 받아 볼텐가!?\r\n\r\n선택은 #r신중#k히 하게나 다시 되돌릴 수 없다네! 또한 기도가 이루어지지 않더라도 내 탓은 하지 말게나. 크크\r\n\r\n(기본 성공 확률은 #b50%#k이며 #i4310014#를 소지시 성공 확률이 #b20%#k가 증가합니다.)", 9062342);
    } else if (status == 20) {
        cm.sendNextS("좋아! 후회없는 선택이길 바라네.\r\n기도 방법을 상세하게 알려주도록 하지.", 0x25, 9062342);
    } else if (status == 21) {
        cm.sendNextS("첫번째, #r제단 가운데#k에 서서 #fs20##b#e엎드렸다 일어났다 3번#n#k", 0x25, 9062342);
    } else if (status == 22) {
        cm.sendNextS("두번째, #fs20##r#e좌측 우측 3번 번갈아 보기#n#k", 0x25, 9062342);
    } else if (status == 23) {
        cm.sendNextS("마지막으로, #fs20##r#e푸른 달을 보며 간절하게 빌기#n#k 순서라네!#fs16#\r\n쉽지 않나!? 그렇다면 #b#h ##k, 자네에게 행운의 여신의 미소가 깃들길 크크.", 0x25, 9062342);
    } else if (status == 24) {
        cm.sendNextS("좋아... 방법도 익혔겠다 한번 #r기도#k를 시작해볼까?", 0x39, 2);
    } else if (status == 25) {
        cm.removeNpc(9062342);
        cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, true, false, false));
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x11, 1, 50, 0));
        statusplus(1500);
    } else if (status == 26) {
        cm.removeNpc(9062342);
        cm.getClient().send(SLFCGPacket.Chatonchr(cm.getPlayer(), "첫번째는 #e#r엎드렸다 일어나기 3번#k#n 이였지..?", 2000));
        statusplus(2000);
    } else if (status == 27) {
        cm.removeNpc(9062342);
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0, 25, 1000));
        statusplus(1000);
    } else if (status == 28) {
        cm.getClient().send(SLFCGPacket.Chatonchr(cm.getPlayer(), "#e#fs15##r한번...!#k", 1000));
        statusplus(1000);
    } else if (status == 29) {
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0, 25, 1000));
        statusplus(1000);
    } else if (status == 30) {
        cm.getClient().send(SLFCGPacket.Chatonchr(cm.getPlayer(), "#e#fs15##r두번...!#k", 1000));
        statusplus(1000);
    } else if (status == 31) {
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0, 25, 1000));
        statusplus(1000);
    } else if (status == 32) {
        cm.getClient().send(SLFCGPacket.Chatonchr(cm.getPlayer(), "#e#fs15##r세번...!#k", 1000));
        statusplus(1000);
    } else if (status == 33) {
        cm.getClient().send(SLFCGPacket.Chatonchr(cm.getPlayer(), "두번째는 분명 #e#r좌측 우측 번갈아 보기 3번#k#n 이였지..?", 3000));
        statusplus(3000);
    } else if (status == 34) {
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x11, 2, 1, 0));
        statusplus(1000);
    } else if (status == 35) {
        cm.getClient().send(SLFCGPacket.Chatonchr(cm.getPlayer(), "#e#fs15##r한번...!#k", 1000));
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x11, 1, 1, 0));
        statusplus(1000);
    } else if (status == 36) {
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x11, 2, 1, 0));
        statusplus(1000);
    } else if (status == 37) {
        cm.getClient().send(SLFCGPacket.Chatonchr(cm.getPlayer(), "#e#fs15##r두번...!#k", 1000));
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x11, 1, 1, 0));
        statusplus(1000);
    } else if (status == 38) {
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x11, 2, 1, 0));
        statusplus(1000);
    } else if (status == 39) {
        cm.getClient().send(SLFCGPacket.Chatonchr(cm.getPlayer(), "#e#fs15##r세번...!#k", 1000));
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x11, 1, 1, 0));
        statusplus(1500);
    } else if (status == 40) {
        cm.getClient().send(SLFCGPacket.Chatonchr(cm.getPlayer(), "좋아, 마지막으로 #b#e푸른 달을 보며#n#k 간절하게 기도하기...!", 3000));
        statusplus(3000);
    } else if (status == 41) {
        cm.getClient().send(SLFCGPacket.Chatonchr(cm.getPlayer(), "달님... 제발 성공하게 해주세요 제발요!!!!!!!!!!!", 5000));
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0, 25, 5000));
        statusplus(100);
    } else if (status == 42) {
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 6000, 1000, 6000, -750, -400));
        statusplus(2000);
    } else if (status == 43) {
        cm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 3000, 0));
        statusplus(3000);
    } else if (status == 44) {
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 1300, 0, -750, -100));
        cm.sendScreenText("잠시후...", true);
    } else if (status == 45) {
        cm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 3000, 0));
        statusplus(4000);
    } else if (status == 46) {
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/CharacterEff.img/event_fireworks/0", 2, 0, 0, 0, 0, 1, cm.getPlayer().getId(), 0, 0, 0));
        statusplus(1000);
    } else if (status == 47) {
        cm.getClient().send(SLFCGPacket.InGameDirectionEvent("Effect/CharacterEff.img/event_fireworks/1", 2, 0, 0, 0, 0, 1, cm.getPlayer().getId(), 0, 0, 0));
        statusplus(3000);
    } else if (status == 48) {
        cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
        MapleInventoryManipulator.removeFromSlot(cm.getClient(), GameConstants.getInventoryType(firstpet.getItemId()), firstpet.getPosition(), firstpet.getQuantity(), false);
        MapleInventoryManipulator.removeFromSlot(cm.getClient(), GameConstants.getInventoryType(secondpet.getItemId()), secondpet.getPosition(), secondpet.getQuantity(), false);
        if (cm.haveItem(4310014, 1)) {
            cm.gainItem(4310014, -1);
        }
        if (Randomizer.isSuccess(suc - 15)) {
            cm.gainItem(2630127, 1);
            cm.gainItem(5069101, -1);

            item = cm.getPlayer().getInventory(2).findById(2630127);
            World.Broadcast.broadcastMessage(CWvsContext.serverMessage(11, cm.getPlayer().getClient().getChannel(), "", cm.getPlayer().getName() + "님이 PRAY 쁘띠 펫 성장에 성공하여 {}를 획득하셨습니다!", true, item));
            cm.sendNextS("무~야호~! 내 정성을 담은 기도가 달에게 닿아서인지 #b기도를 들어주셨다!#k\r\n좋아, 마을로 돌아가자!", 0x39, 2);
        } else {
            if (cm.haveItem(4310014, 1)) {
                cm.gainItem(4310014, -1);
            }
            cm.gainItem(2430043, 1);
            cm.gainItem(5069101, -1);
            cm.sendNextS("내 기도에 무언가 부족했던 걸까..? 실패했다... 아쉽지만 마을로 돌아가자...", 0x39, 2);
        }
    } else if (status == 49) {
        cm.warp(100000051);
        cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(false, false, false, false));
        cm.getClient().send(SLFCGPacket.SetStandAloneMode(false));
        cm.dispose();
    }
}

function statusplus(millsecond) {
    cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}
