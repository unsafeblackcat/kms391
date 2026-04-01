/**
 * @projectDescription 偷竊技能系統 
 *
 * @author 奈科 
 * @version 1.0 
 * @sdoc scripts/npc 
 */
 
importPackage(Packages.constants); 
importPackage(Packages.client); 
 
var GameConstants = Packages.constants.GameConstants; 
 
var enter = '\r\n';
var reset = '#l#k';
var IS_DEBUGGING = false;
 
var status = -1;
var selectedStealSkillSlot;
var selectedJobIndex;
var selectedSkillIndex;
var selectedSkills;
var chat 
var jobList = [];
 
var Job = function(name, id) {
    this.name  = name 
    this.id  = id;
}
var stealSkills = [];
stealSkills[0] = [
    1001005, //劍士 
    2001008, 2001002, //魔法師 
    3001004, //弓箭手 
    3011004, //開拓者 
    4001334, 4001344, 4001003, // 盜賊 
    5001002, 5001003, //海盜 
    5011000, 5011001 //凱撒    
];
 
stealSkills[1] = [
    1101011, 1101006, //戰士 
    1201015, 1201013, //見習騎士 
    1301012, 1301007, //槍騎兵 
    2301005, 2301002, 2301004, //祭司 
    2101004, 2101005, 2101001, 2100010, //火毒 
    2201008, 2201005, 2201001, //冰雷 
    3101005, //獵人 
    3201011, //弩弓手 
    3301003, //開拓者 
    4101013, 4101010, //刺客 
    4201012, //俠盜 
    4301004, //影武者 
    4311002, 4311003, //夜使者 
    5201001, 5201018, //槍神 
    5101012, //打手 
    5301000, 5301001, 5301003 //重砲兵 
];
 
stealSkills[2] = [
    1111010, 1111012, //十字軍 
    1211018, 1211010, 1211012, 1211013, 1211014, 1211011, //騎士 
    1311011, 1311012, 1311015, //狂戰士 
    2311004, 2311011, 2311002, 2311003, 2311009, //僧侶 
    2111002, 2111003, //火毒 
    2211002, 2211014, //冰雷 
    3111013, //遊俠 
    3211011, 3211012, //狙擊手 
    3311012, //開拓者 
    4111010, 4111015, //隱士 
    4211011, 4211002, //神偷 
    4321006, 4321002, //暗影雙刀 
    4331000, 4331011, 4331006, //閃雷悍將 
    5211008, 5211010, 5211007, //蒼龍俠客 
    5111009, 5111007, //拳霸 
    5311000, 5311010, 5311004, 5311005 //機甲戰神 
];
 
stealSkills[3] = [
    1121016, //英雄 
    1221009, 1221014, 1221011, 1221016, //聖騎士 
    1321014, 1321012, //黑騎士 
    2321007, 2321008, 2321006, 2321005, //主教 
    2121006, 2121007, 2121011, //火毒 
    2221006, 2221011, 2221007, 2221012, //冰雷 
    3121020, 3121015, 3121002, //箭神 
    3221007, 3221014, 3221002, //神射手 
    3321022, //開拓者 
    4121013, 4121017, 4121016, 4121015, //暗夜行者 
    4221014, 4221017, 4221010, //影武者 
    4341002, 4341011, 4341004, 4341009, //幻影俠盜 
    5221004, 5221016, 5221015, 5221017, 5221013, 5221018, //狂豹獵人 
    5121007, 5121013, 5121015, 5121010, 5121009, //煉獄巫師 
    5321000, 5321012, 5321001, //機甲司令 
];
 
stealSkills[4] = [
    1121054, //英雄 
    1221054, //聖騎士 
    1321054, //黑騎士 
    2121054, //火毒 
    2221054, //冰雷   
    3121054, //箭神 
    3221054, //神射手 
    3321034, //開拓者 
    4121054, //暗夜行者 
    5221054, //狂豹獵人 
    5121054, //煉獄巫師 
]
 
var jobList = [];
 
function start() {
    status = -1;
    action(1, 0, 0);
}
 
function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else if (mode == -1 || mode == 0) {
        cm.dispose(); 
        return;
    } else {
        cm.dispose(); 
        return;
    }
 
    chat = '#fs15#'
    if (status == 0) {
        chat += '這是只有幻影職業才能使用的技能偷竊系統。'
        cm.sendSimple(chat) 
    } else if (status == 1) {
        if (GameConstants.isPhantom(cm.getPlayer().getJob()))  {
            for (var i = 0; i < 5; i++) {
                chat += '#L' + i + '#';
                chat += getStealSkillSlotName(i);
                chat += enter;
            }
 
            cm.sendSimple(chat) 
        } else {
            chat += '只有幻影職業才能使用此功能';
            cm.dispose() 
            cm.sendOk(chat) 
        }
 
    } else if (status == 2) {        
        if (selectedStealSkillSlot == null) {
            selectedStealSkillSlot = selection;
        }
 
        chat += '請選擇您想偷竊的職業群' + enter 
 
        for (var i = 0; i < stealSkills[selectedStealSkillSlot].length; i++) {
            var skillId = stealSkills[selectedStealSkillSlot][i];
            var jobId = Math.floor(skillId  / 10000);
            if (jobList[jobList.length - 1] != jobId) {
                chat += '#L' + jobList.length  + '#'
                chat += getJobNameById(jobId);
                chat += enter;
 
                jobList.push(jobId); 
            }
        }
        cm.sendSimple(chat) 
    } else if (status == 3) {
        if (selectedJobIndex == null) {
            selectedJobIndex = selection;
        }
 
        chat += '請選擇您想偷竊的技能' + enter 
 
        var selectedJobId = jobList[selectedJobIndex];
        for (var i = 0; i < stealSkills[selectedStealSkillSlot].length; i++) {
            var skillId = stealSkills[selectedStealSkillSlot][i];
            var jobId = Math.floor(skillId  / 10000);
            if (jobId == selectedJobId) {
                chat += '#L' + i + '#';
                chat += '#s' + skillId + '#';
                chat += SkillFactory.getSkillName(skillId); 
                chat += enter;
            }
        }
        cm.sendSimple(chat); 
    } else if (status == 4) {
        if (selectedSkillIndex == null) {
            selectedSkillIndex = selection;
        }
        chat += '請選擇要放置該技能的欄位' + enter;
        var stolenSkillArray = getStoleanSkillArray();
        
        for (var i = 0; i < GameConstants.getNumSteal(selectedStealSkillSlot  + 1); i++) {
            var stolenSkill = stolenSkillArray[selectedStealSkillSlot][i];
            chat += '#L' + i + '#';
            chat += (i + 1) + '號欄位: '
            if (stolenSkill != null) {
                chat += '#s' + stolenSkill.left  + '#';
                chat += SkillFactory.getSkillName(stolenSkill.left); 
 
                if (stolenSkill.right)  {
                    chat += '(已裝備的技能)';
                }
 
            } else {
                chat += '空欄位';
            }
            chat += enter;
 
        }
 
        cm.sendSimple(chat) 
 
    } else if (status == 5) {
        var skill = SkillFactory.getSkill(stealSkills[selectedStealSkillSlot][selectedSkillIndex]); 
        var tupleSlotSkill = getStoleanSkillArray()[selectedStealSkillSlot][selection]
        var isUpdate = false;
        var skillLevel = skill.getMaxLevel() 
 
        if (tupleSlotSkill == null || !tupleSlotSkill.right)  {
            var isreplaceSkill = tupleSlotSkill == null ? false : true;
 
            chat += getStealSkillSlotName(selectedStealSkillSlot)
            chat += ' ' + (selection + 1) + '號欄位' + enter 
 
            if (isreplaceSkill) {
                chat += '#s' + tupleSlotSkill.left  + '#';
                chat += SkillFactory.getSkillName(tupleSlotSkill.left)  + '已變更為' + enter 
            }
 
            chat += '#s' + skill.getId()  + '#';
            chat += SkillFactory.getSkillName(skill.getId()); 
            chat += (isreplaceSkill ? '。' : '已新增。');
 
            if (isreplaceSkill) {
                cm.getPlayer().removeStolenSkill(tupleSlotSkill.left); 
            }
            cm.getPlayer().addStolenSkill(skill.getId(),  skillLevel);
            isUpdate = true;
        } else {
            chat += '無法更換已裝備的技能'
        }
 
        if (isUpdate) {
            cm.getPlayer().reloadChar() 
        }
 
        cm.sendOk(chat) 
        cm.dispose(); 
    }
 
}
 
function getStoleanSkillArray() {
    var stolenSkills = cm.getPlayer().getStolenSkills(); 
    var stolenSkillIterator = stolenSkills.iterator(); 
    var stolenSkillArray = [];
 
    for (var i = 0; i < 5; i++) {
        stolenSkillArray[i] = [];
    }
 
    while (stolenSkillIterator.hasNext())  {
        var stolenSkill = stolenSkillIterator.next(); 
        var skillJobLevel = GameConstants.getJobNumber(stolenSkill.left)  - 1;
 
        stolenSkillArray[skillJobLevel].push(stolenSkill);
    }
    return stolenSkillArray;
}
 
function getStealSkillSlotName(index) {
    var name = ''
    if (index == 4) {
        name += '超技能';
    } else {
        name += (index + 1) + '轉偷竊技能';
    }
    return name;
}
 
//////////// 
function Color(a, r, g, b) {
    var hexcode = ''
    var alpha = 'FF';
    var red;
    var green;
    var blue;
 
    if (b != null) {
        alpha = formattedHex(a);
        red = formattedHex(r);
        green = formattedHex(g);
        blue = formattedHex(b);
 
    } else if (g != null) {
        red = formattedHex(a);
        green = formattedHex(r);
        blue = formattedHex(g);
    }
 
    if (red == null) {
        hexcode = a;
    } else {
        hexcode = alpha + red + green + blue 
    }
 
    return '#fc0x' + hexcode + '#'
}
 
function formattedHex(c) {
    var hex = c.toString(16); 
    return hex.length  == 1 ? '0' + hex : hex;
}
 
function FFColor(hexcode) {
    return Color('FF' + hexcode);
}
 
function formattedMeso(meso) {
    var upperMeso = Math.floor(meso  / 100000000);
    var upperLeftMeso = meso % 100000000;
    var lowerMeso = Math.floor(upperLeftMeso  / 10000);
    var lowerLeftMeso = upperLeftMeso % 10000;
 
    var mesoString = '';
 
    if (upperMeso >= 1)
        mesoString += upperMeso + '億';
    if (lowerMeso > 0)
        mesoString += lowerMeso + '萬';
    if (lowerLeftMeso > 0)
        mesoString += lowerLeftMeso;
 
    mesoString += '楓幣';
 
    return mesoString;
}
 
function getIcon(name) {
    var itemMap = new newMap();
    itemMap.put(' 好感度', 3800452)
    itemMap.put(' 鼠', 3801286)
    itemMap.put(' 牛', 3801287)
    itemMap.put(' 虎', 3801288)
    itemMap.put(' 兔', 3801289)
    itemMap.put(' 龍', 3801290)
    itemMap.put(' 馬', 3801292)
    itemMap.put(' 羊', 3801293)
    itemMap.put(' 猴', 3801294)
    itemMap.put(' 雞', 3801295)
    itemMap.put(' 狗', 3801296)
    itemMap.put(' 豬', 3801297)
    itemMap.put(' 楓幣', 3801305)
    itemMap.put(' 上箭頭', 3801306)
    itemMap.put(' 下箭頭', 3801307)
    itemMap.put(' 楓葉', 3801309)
    itemMap.put(' 核心寶石', 3801310)
    itemMap.put(' 勳章', 3801311)
    itemMap.put(' 菇菇寶貝', 3801312)
    itemMap.put(' 綠水靈', 3801313)
    itemMap.put(' 皮卡啾', 3801314)
    itemMap.put(' 雪吉拉', 3801315)
    itemMap.put(' 企鵝王', 3801316)
    itemMap.put(' 石靈', 3801317)
 
    return '#i' + itemMap.get(name)  + '#'
}
 
function newMap() {
    var map = {};
    map.value  = {};
    map.getKey  = function(id) {
        return 'k_' + id;
    };
    map.put  = function(id, value) {
        var key = map.getKey(id); 
        map.value[key]  = value;
    };
    map.contains  = function(id) {
        var key = map.getKey(id); 
        if (map.value[key])  {
            return true;
        } else {
            return false;
        }
    };
    map.get  = function(id) {
        var key = map.getKey(id); 
        if (map.value[key])  {
            return map.value[key]; 
        }
        return null;
    };
    map.remove  = function(id) {
        var key = map.getKey(id); 
        if (map.contains(id))  {
            map.value[key]  = undefined;
        }
    };
 
    map.getList  = function() {
        return map.value; 
    }
 
    return map;
}
 
function sleep(delay) {
    var start = new Date().getTime();
    while (new Date().getTime() < start + delay);
}
 
function print(text) {
    java.lang.System.out.println(text) 
}
 
function getMonsterImage(mobId) {
    return '#fMob/' + mobId + '.img/stand/0#'
}
 
function numberToKorean(number) {
    var inputNumber = number < 0 ? false : number;
    var unitWords = ['', '萬', '億', '兆', '京'];
    var splitUnit = 10000;
    var splitCount = unitWords.length; 
    var resultArray = [];
    var resultString = '';
 
    for (var i = 0; i < splitCount; i++) {
        var unitResult = (inputNumber % Math.pow(splitUnit,  i + 1)) / Math.pow(splitUnit,  i);
        unitResult = Math.floor(unitResult); 
        if (unitResult > 0) {
            resultArray[i] = unitResult;
        }
    }
 
    for (var i = 0; i < resultArray.length;  i++) {
        if (!resultArray[i]) continue;
        resultString = String(resultArray[i]) + unitWords[i] + resultString;
    }
 
    return resultString;
}
 
function getDate() {
    var data = new Date();
    var month = data.getMonth()  < 10 ? '0' + (data.getMonth()  + 1) : (data.getMonth()  + 1) + '';
    var day = data.getDate()  < 10 ? '0' + data.getDate()  : data.getDate()  + ''
    var date = (data.getYear()  + 1900) + '' + month + '' + day;
    return date;
}
 
function getFamilarJobCode(jobCode) {
    var jobList = [];
    var jobCodeLength = jobCode.toString().length; 
    var baseJobCode = Math.floor(jobCode  / 100) * 100;
    var subJobCode = 1;
    var minorJobCode = 2;
 
    if (GameConstants.isDualBlade(jobCode))  {
        minorJobCode = 4;
    } else if (GameConstants.isEvan(jobCode))  {
        minorJobCode = 8;
    }
 
    if (jobCodeLength == 3) {
        subJobCode = parseInt(jobCode.toString()[1]); 
    } else {
        jobList.push(baseJobCode); 
    }
 
    jobList.push(baseJobCode) 
    jobList.push(baseJobCode  + (subJobCode * 10));
    for (var i = 1; i <= minorJobCode; i++) {
        jobList.push(baseJobCode  + (subJobCode * 10) + i);
    }
 
    return jobList;
}
 
function getJobNameById(job) {
    switch (job) {
        case 0:
            return "初心者";
        case 100:
            return "劍士";
        case 110:
            return "戰士";
        case 111:
            return "十字軍";
        case 112:
            return "英雄";
        case 120:
            return "見習騎士";
        case 121:
            return "騎士";
        case 122:
            return "聖騎士";
        case 130:
            return "槍騎兵";
        case 131:
            return "狂戰士";
        case 132:
            return "黑騎士";
        case 200:
            return "魔法師";
        case 210:
            return "火毒巫師";
        case 211:
            return "火毒魔導士";
        case 212:
            return "火毒大魔導士";
        case 220:
            return "冰雷巫師";
        case 221:
            return "冰雷魔導士";
        case 222:
            return "冰雷大魔導士";
        case 230:
            return "祭司";
        case 231:
            return "僧侶";
        case 232:
            return "主教";
        case 300:
            return "弓箭手";
        case 310:
            return "獵人";
        case 311:
            return "遊俠";
        case 312:
            return "箭神";
        case 320:
            return "弩弓手";
        case 321:
            return "狙擊手";
        case 322:
            return "神射手";
        case 400:
            return "盜賊";
        case 410:
            return "刺客";
        case 411:
            return "隱士";
        case 412:
            return "暗夜行者";
        case 420:
            return "俠盜";
        case 421:
            return "神偷";
        case 422:
            return "影武者";
        case 430:
            return "影武者";
        case 431:
            return "夜使者";
        case 432:
            return "暗影雙刀";
        case 433:
            return "閃雷悍將";
        case 434:
            return "幻影俠盜";
        case 500:
            return "海盜";
        case 510:
            return "打手";
        case 511:
            return "拳霸";
        case 512:
            return "煉獄巫師";
        case 520:
            return "槍神";
        case 521:
            return "蒼龍俠客";
        case 522:
            return "狂豹獵人";
        case 800:
            return "管理員";
        case 900:
            return "遊戲管理員";
        case 1000:
            return "貴族";
        case 1100:
        case 1110:
        case 1111:
        case 1112:
            return "聖魂劍士";
        case 1200:
        case 1210:
        case 1211:
        case 1212:
            return "烈焰巫師";
        case 1300:
        case 1310:
        case 1311:
        case 1312:
            return "破風使者";
        case 1400:
        case 1410:
        case 1411:
        case 1412:
            return "暗夜行者";
        case 1500:
        case 1510:
        case 1511:
        case 1512:
            return "閃雷悍將";
        case 2000:
            return "傳說";
        case 2100:
        case 2110:
        case 2111:
        case 2112:
            return "亞蘭";
        case 2001:
        case 2200:
        case 2210:
        case 2211:
        case 2212:
        case 2213:
        case 2214:
        case 2215:
        case 2216:
        case 2217:
        case 2218:
            return "幻影";
        case 3000:
            return "市民";
        case 3200:
        case 3210:
        case 3211:
        case 3212:
            return "龍魔導士";
        case 3300:
        case 3310:
        case 3311:
        case 3312:
            return "狂狼勇士";
        case 3500:
        case 3510:
        case 3511:
        case 3512:
            return "機甲戰神";
        case 501:
            return "海盜(重砲兵)";
        case 530:
            return "重砲兵";
        case 531:
            return "機甲戰神";
        case 532:
            return "機甲司令";
        case 2002:
        case 2300:
        case 2310:
        case 2311:
        case 2312:
            return "精靈遊俠";
        case 3001:
        case 3100:
        case 3110:
        case 3111:
        case 3112:
            return "惡魔殺手";
        case 2003:
        case 2400:
        case 2410:
        case 2411:
        case 2412:
            return "幻影俠盜";
        case 2004:
        case 2700:
        case 2710:
        case 2711:
        case 2712:
            return "夜光";
        case 5000:
        case 5100:
        case 5110:
        case 5111:
        case 5112:
            return "米哈逸";
        case 6000:
        case 6100:
        case 6110:
        case 6111:
        case 6112:
            return "凱撒";
        case 6001:
        case 6500:
        case 6510:
        case 6511:
        case 6512:
            return "天使破壞者";
        case 3101:
        case 3120:
        case 3121:
        case 3122:
            return "惡魔復仇者";
        case 3002:
        case 3600:
        case 3610:
        case 3611:
        case 3612:
            return "傑諾";
        case 10000:
            return "零式JR";
        case 10100:
            return "零式10100";
        case 10110:
            return "零式10110";
        case 10111:
            return "零式10111";
        case 10112:
            return "零式";
        case 2005:
            return "???";
        case 2500:
        case 2510:
        case 2511:
        case 2512:
            return "隱月";
        case 14000:
        case 14200:
        case 14210:
        case 14211:
        case 14212:
            return "凱內西斯";
        case 15000:
        case 15200:
        case 15210:
        case 15211:
        case 15212:
            return "伊利恩";
        case 15001:
        case 15500:
        case 15510:
        case 15511:
        case 15512:
            return "阿爾克納";
 
        case 301:
        case 330:
        case 331:
        case 332:
            return "開拓者";
        case 16000:
        case 16400:
        case 16410:
        case 16411:
        case 16412:
            return "虎影";
 
        case 15002:
        case 15100:
        case 15110:
        case 15111:
        case 15112:
            return "阿戴爾";
 
        default:
            return "未知職業";
    }
}