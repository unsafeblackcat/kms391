importPackage(java.util);
importPackage(java.lang);
importPackage(java.io);

importPackage(Packages.constants);
importPackage(Packages.client);
importPackage(Packages.client.stats);
importPackage(Packages.server);
var status = -1;

getmhair = 0;
getfhair = 0;
getmface = 0;
getfface = 0;
count = 0;
nohair = 0;
mhair = [];
fhair = [];
mface = [];
fface = [];
mhair1 = [];
fhair1 = [];
mface1 = [];
fface1 = [];
codyList = [];
st2 = 0;
st = 0;

보라 = "#fMap/MapHelper.img/weather/starPlanet/7#";
파랑 = "#fMap/MapHelper.img/weather/starPlanet/8#";
별회 = "#fUI/GuildMark.img/Mark/Pattern/00004001/15#";
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection, selection2) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        if (status == 1 && st2 == 2 && count == 0) {
            sel1 = selection;
            count++;
        } else {
            status++;
        }
    }
    if (status == 0) {
        if (cm.getPlayer().getAndroid() == null) {
            cm.sendOk("#fs15#안드로이드가 없는 분은 안드로이드 메이크업을 하실 수 없습니다. 안드로이드와 함께 찾아와 주세요.");
            cm.dispose();
            return;
        }
			말 = "#fs15#	                        "+보라+" #k#fc0xFF3B00DB#코디 관련#k "+보라+"\r\n#fc0xFF7112FF#         			";
			말 += "#L0#헤어 변경#l";
			말 += "#L1#성형 변경#l\r\n\r\n\r\n";
			말 += "	                        "+보라+" #k#fc0xFFC900A7#컬러 관련#k "+보라+"\r\n#fc0xFFF361A6#           ";
            		말 += "#L2#일반 염색#l";
            		말 += "#L3#렌즈 변경#l";
            		말 += "#L4#피부 변경#l\r\n";
       		cm.sendSimple(말);
        } else if (status == 1) {
 			if (selection <= 1) {
			if (count == 0) {
				st = selection;
				st2 = cm.getPlayer().getAndroid().getGender();
				var it = MapleItemInformationProvider.getInstance().getAllItems().iterator();
				while (it.hasNext()) {
					var itemPair = it.next();
					getgen = Math.floor(itemPair.getLeft() / 1000);
					if (itemPair.getRight() != "" || itemPair.getRight() != null) {
						if (itemPair.getLeft() % 10 == (cm.getPlayer().getHair()%10) ) {
							if (getgen == 30 || getgen == 32 || getgen == 33 || getgen == 35 || getgen == 36 || getgen == 40 || getgen == 43 || getgen == 45  || getgen == 46 ) {
								mhair1.push(itemPair.getLeft());
							}
						}
						if (cm.getPlayer().getJob() == 10112) {
							gh = cm.getPlayer().getSecondHair();
						} else {
							gh = cm.getPlayer().getHair();
						}
						if (itemPair.getLeft() % 10 == (gh%10)) {
							if (getgen == 31 || getgen == 34 || getgen == 37 || getgen == 38 || getgen == 39 || getgen == 41 || getgen == 42 || getgen == 44 || getgen == 47 || getgen == 48) {
								fhair1.push(itemPair.getLeft())
							}
						}
						if ((Math.floor(itemPair.getLeft() / 100)) % 10 == 0) {
							if (getgen == 20 || getgen == 23 || getgen == 25 || getgen == 27) {
								mface1.push(itemPair.getLeft());
							}
							if (getgen == 21 || getgen == 24 || getgen == 25 || getgen == 26 || getgen == 28) {
								fface1.push(itemPair.getLeft());
							}
						}
					}

				}
				mhair1.sort(function(a, b) {
					return parseInt(a) - parseInt(b)
				});
				fhair1.sort(function(a, b) {
					return parseInt(a) - parseInt(b)
				});
				mface1.sort(function(a, b) {
					return parseInt(a) - parseInt(b)
				});
				fface1.sort(function(a, b) {
					return parseInt(a) - parseInt(b)
				});
				for (i = 0; i < mhair1.length; i++) {
					gar = Math.floor(getmhair / 127);
					if (getmhair % 127 == 0) {
						mhair[gar] = [];
					}
					getmhair++;
					mhair[gar].push(mhair1[i]);
				}
				for (i = 0; i < fhair1.length; i++) {
					gar = Math.floor(getfhair / 127);
					if (getfhair % 127 == 0) {
						fhair[gar] = [];
					}
					getfhair++;
					fhair[gar].push(fhair1[i]);
				}
				for (i = 0; i < mface1.length; i++) {
					gar = Math.floor(getmface / 127);
					if (getmface % 127 == 0) {
						mface[gar] = [];
					}
					getmface++;
					mface[gar].push(mface1[i]);
				}
				for (i = 0; i < fface1.length; i++) {
					gar = Math.floor(getfface / 127);
					if (getfface % 127 == 0) {
						fface[gar] = [];
					}
					getfface++;
					fface[gar].push(fface1[i]);
				}
			}
			말 = "";
			if (st == 0) {
            				말 += ""+파랑+"#e#fs15##fc0xFF6B66FF##e원하는 리스트#n#k#fs15#의 헤어를 선택해보세요!\r\n\r\n"
            if (st2 == 0) {
                for (i = 0; i < mhair.length; i++) {
					if (i == 0) {
						타입 = ""+보라+" #fc0xFF5C1DB5#A"
							} else if (i == 1) {
						타입 = ""+보라+" #fc0xFF4742DB#B"
							} else if (i == 2) {
						타입 = ""+보라+" #fc0xFFBD2B70#C"
							} else if (i == 3) {
						타입 = ""+보라+" #fc0xFF35B62C#D"
							}
									말 += "#L" + i + "#"+타입+" 리스트#k의 헤어를 선택하겠습니다.#l\r\n";
								}
				} else if (st2 == 1) {
                말 += "#r(총 " + fhair.length + "페이지로 구성되어 있습니다.)#k#n\r\n\r\n"
                for (i = 0; i < fface.length; i++) {
					if (i == 0) {
				타입 = ""+보라+" #fc0xFF5C1DB5#A"
					} else if (i == 1) {
				타입 = ""+보라+" #fc0xFF4742DB#B"
					} else if (i == 2) {
				타입 = ""+보라+" #fc0xFFBD2B70#C"
					} else if (i == 3) {
				타입 = ""+보라+" #fc0xFF35B62C#D"
					}
							말 += "#L" + i + "#"+타입+" 리스트#k의 얼굴을 선택하겠습니다.#l\r\n";
						}
				}
			} else if (st == 1) {
				말 += ""+파랑+"#e#fs15##fc0xFF6B66FF##e원하는 리스트#n#k#fs15#의 얼굴을 선택해보세요!\r\n\r\n"
            if (st2 == 0) {
                for (i = 0; i < mface.length; i++) {
	        if (i == 0) {
		타입 = ""+보라+" #fc0xFF5C1DB5#A"
	        } else if (i == 1) {
		타입 = ""+보라+" #fc0xFF4742DB#B"
	        } else if (i == 2) {
		타입 = ""+보라+" #fc0xFFBD2B70#C"
	        } else if (i == 3) {
		타입 = ""+보라+" #fc0xFF35B62C#D"
	        }
                    말 += "#L" + i + "#"+타입+" 리스트#k의 얼굴을 선택하겠습니다.#l\r\n";
                }
				} else if (st2 == 1) {
					for (i = 0; i < mface.length; i++) {
						if (i == 0) {
					타입 = ""+보라+" #fc0xFF5C1DB5#A"
						} else if (i == 1) {
					타입 = ""+보라+" #fc0xFF4742DB#B"
						} else if (i == 2) {
					타입 = ""+보라+" #fc0xFFBD2B70#C"
						} else if (i == 3) {
					타입 = ""+보라+" #fc0xFF35B62C#D"
						}
								말 += "#L" + i + "#"+타입+" 리스트#k의 얼굴을 선택하겠습니다.#l\r\n";
							}
				}
			}
            cm.sendSimple(말);
			} else if (selection == 2) {
				nohair = 1;
				for(i = 0; i < 8; i++)
				codyList.push(Math.floor((cm.getPlayer().getAndroid().getHair()) / 10) * 10 + i);
				cm.askAvatarAndroid("안드로이드에 어울리는 색을 선택해보세요!", codyList);
			} else if (selection == 3) {
				nohair = 2;
				for(i = 0; i < 8; i++)
				codyList.push(Math.floor((cm.getPlayer().getAndroid().getFace()) / 1000) * 1000 + ((cm.getPlayer().getAndroid().getFace()) % 100) + i * 100);
				cm.askAvatarAndroid("안드로이드에 어울리는 렌즈를 선택해보세요!", codyList);
			} else if (selection == 4) {
				nohair = 3;
				codyList = [0, 1, 2, 3, 4, 9, 10, 11, 12, 13, 15, 16, 18, 19, 20, 21, 22, 24, 25, 26, 27];
				cm.askAvatarAndroid("안드로이드에 어울리는 피부를 선택해보세요!", codyList);
			}
        } else if (status == 2) {
            st4 = selection;
	if (nohair >= 1) {
newItem = selection & 0xFF;
		if (nohair == 1) {
			cm.setHairAndroid(codyList[newItem]);
			} else if (nohair == 2) {
				cm.setFaceAndroid(codyList[newItem]);
			} else if (nohair == 3) {
				cm.setSkinAndroid(codyList[newItem]);
			}
		cm.sendOk("#fs15#안드로이드가 이전보다 더 예뻐진거 같은걸요?")
		cm.dispose();
		return;
	} else {
            if (st == 0) {
                if (st2 == 0) {
                    st3 = mhair[selection];
                } else if (st2 == 1) {
                    st3 = fhair[selection];
                }
            	cm.askAvatarAndroid("안드로이드에 어울리는 헤어를 선택해보세요!", st3);
            } else if (st == 1) {
                if (st2 == 0) {
                    st3 = mface[selection];
                } else {
                    st3 = fface[selection];
                }
	            cm.askAvatarAndroid("안드로이드에 어울리는 얼굴을 선택해보세요!", st3);
        	}
	}
        } else if (status == 3) {
            if (st == 0) {
                if (st2 == 0) {
                    st5 = mhair[st4];
                } else {
                    st5 = fhair[st4];
                }
            } else if (st == 1) {
                if (st2 == 0) {
                    st5 = mface[st4];
                } else {
                    st5 = fface[st4];
                }
            }
            setAvatar(st5[selection]);
            cm.sendOk("#fs15#안드로이드가 이전보다 더 예뻐진거 같은걸요?");
            cm.dispose();
            return;
    }
}

function setAvatar(args) {
    if (args < 30000) {
        cm.setFaceAndroid(args);
    } else {
        cm.setHairAndroid(args);
    }
}