importPackage(java.lang);
importPackage(Packages.server);

var enter = "\r\n";
var seld = -1;
var nTime = 600;

var dungeons = [{
        'name': "초보자던전",
        'content': "초보자 던전은 그누구나 쉽게 깰수있는 던전입니다.",
        'boss': 9601695,
        'bossmap': 993201007,
        'x': -95,
        'y': -62,
        'maplist': [993201002, 993201003, 993201004],
        'stage': [{
                'mapid': 993201002,
                'mobs': [
                    { 'mobid': 9500151, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9500151, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9500151, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9500151, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9500151, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9500151, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9500151, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9500151, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9500151, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9500151, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9500151, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9500151, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9500151, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9500151, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9500151, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9500151, 'qty': 1, 'x': -197, 'y': -67 },
                ]
            },
            {
                'mapid': 993201003,
                'mobs': [
                    { 'mobid': 9832005, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9832005, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9832005, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9832005, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9832005, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9832005, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9832005, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9832005, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9832005, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9832005, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9832005, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9832005, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9832005, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9832005, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9832005, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9832005, 'qty': 1, 'x': -197, 'y': -67 },
                ]
            },
            {
                'mapid': 993201004,
                'mobs': [
                    { 'mobid': 9833229, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9833229, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9833229, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9833229, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9833229, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9833229, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9833229, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9833229, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9833229, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9833229, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9833229, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9833229, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9833229, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9833229, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9833229, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9833229, 'qty': 1, 'x': -197, 'y': -67 },
                ]
            }
        ],
        'reward': [
        { 'itemid': 4310228, 'qty': 1 }
        
        ],
        'rewardPointMin': 10,
        'rewardPointMax': 100
    },

    {
        'name': "하드던전",
        'content': "하드던전은 초보자던전의 보상의 10배가 증정됩니다",
        'boss': 9601015,
        'bossmap': 993201012,
        'x': -95,
        'y': -62,
        'maplist': [993201008, 993201009, 993201010],
        'stage': [{
                'mapid': 993201008,
                'mobs': [
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                ]
            },
            {
                'mapid': 993201009,
                'mobs': [
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                ]
            },
            {
                'mapid': 993201010,
                'mobs': [
                    { 'mobid': 9480083, 'qty': 1, 'x': -196, 'y': -62 },
                    { 'mobid': 9480083, 'qty': 1, 'x': -196, 'y': -62 },
                    { 'mobid': 9480083, 'qty': 1, 'x': -196, 'y': -62 },
                    { 'mobid': 9480083, 'qty': 1, 'x': -196, 'y': -62 },
                ]
            }
        ],
        'reward': [
            { 'itemid': 4310228, 'qty': 10 }
        ],
        'rewardPointMin': 10,
        'rewardPointMax': 100
    },

    {
        'name': "HELL던전",
        'content': "HELL던전은 하드던전 보상의 5배를 지급합니다",
        'boss': 8880700,
        'bossmap': 980001104,
        'x': -95,
        'y': -62,
        'maplist': [980001102, 980001103, 980001104],
        'stage': [{
                'mapid': 980001102,
                'mobs': [
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                ]
            },
            {
                'mapid': 980001103,
                'mobs': [
                    { 'mobid': 9480083, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9480083, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9480083, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9480083, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9480083, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9480083, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9480083, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9480083, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9480083, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9480083, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9480083, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9480083, 'qty': 1, 'x': -197, 'y': -67 },
                ]
            },
            {
                'mapid': 980001104,
                'mobs': [
                    { 'mobid': 9601015, 'qty': 1, 'x': -196, 'y': -62 },
                    { 'mobid': 9601015, 'qty': 1, 'x': -196, 'y': -62 },
                    { 'mobid': 9601015, 'qty': 1, 'x': -196, 'y': -62 },
                    { 'mobid': 9601015, 'qty': 1, 'x': -196, 'y': -62 },
					{ 'mobid': 9601015, 'qty': 1, 'x': -196, 'y': -62 },
                    { 'mobid': 9601015, 'qty': 1, 'x': -196, 'y': -62 },
                    { 'mobid': 9601015, 'qty': 1, 'x': -196, 'y': -62 },
                    { 'mobid': 9601015, 'qty': 1, 'x': -196, 'y': -62 },
					{ 'mobid': 9601015, 'qty': 1, 'x': -196, 'y': -62 },
                    { 'mobid': 9601015, 'qty': 1, 'x': -196, 'y': -62 },
                    { 'mobid': 9601015, 'qty': 1, 'x': -196, 'y': -62 },
                    { 'mobid': 9601015, 'qty': 1, 'x': -196, 'y': -62 },
                ]
            }
        ],
        'reward': [
            { 'itemid': 4310228, 'qty': 50 }
        ],
        'rewardPointMin': 10,
        'rewardPointMax': 100
    },
	
	{
        'name': "Extreme던전",
        'content': "Extreme던전은 하드던전 보상의 4배를 지급합니다",
        'boss': 9440025,
        'bossmap': 401100500,
        'x': -95,
        'y': -62,
        'maplist': [401100100, 401100200, 401100300],
        'stage': [{
                'mapid': 401100100,
                'mobs': [
                    { 'mobid': 8880700, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 8880700, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 8880700, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 8880700, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 8880700, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 8880700, 'qty': 1, 'x': -197, 'y': -67 },
                ]
            },
            {
                'mapid': 401100200,
                'mobs': [
                    { 'mobid': 9400080, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9400080, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9400080, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9400080, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9400080, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9400080, 'qty': 1, 'x': -197, 'y': -67 },
                ]
            },
            {
                'mapid': 401100300,
                'mobs': [
                    { 'mobid': 9460026, 'qty': 1, 'x': -196, 'y': -62 },
                    { 'mobid': 9460026, 'qty': 1, 'x': -196, 'y': -62 },
                    { 'mobid': 9460026, 'qty': 1, 'x': -196, 'y': -62 },
                    { 'mobid': 9460026, 'qty': 1, 'x': -196, 'y': -62 },
					{ 'mobid': 9460026, 'qty': 1, 'x': -196, 'y': -62 },
                    { 'mobid': 9460026, 'qty': 1, 'x': -196, 'y': -62 },
                ]
            }
        ],
        'reward': [
            { 'itemid': 4310228, 'qty': 200 }
        ],
        'rewardPointMin': 10,
        'rewardPointMax': 100
    },
	
	{
        'name': "지옥던전",
        'content': "지옥던전은 하드던전 보상의 4배를 지급합니다",
        'boss': 9601848,                    
        'bossmap': 993217021,
        'x': -95,
        'y': -62,
        'maplist': [910530202, 450001370, 450001390],
        'stage': [{
                'mapid': 910530202,
                'mobs': [
                    { 'mobid': 9500403, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9500403, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9500403, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9500403, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9500403, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9500403, 'qty': 1, 'x': -197, 'y': -67 },
                ]
            },
            {
                'mapid': 450001370,
                'mobs': [
                    { 'mobid': 9601844, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601844, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601844, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601845, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601845, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601845, 'qty': 1, 'x': -197, 'y': -67 },
                ]
            },
            {
                'mapid': 401100300,
                'mobs': [
                    { 'mobid': 9601844, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601844, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601844, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601845, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601845, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601845, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601844, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601844, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601844, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601845, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601845, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601845, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601844, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601844, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601844, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601845, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601845, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601845, 'qty': 1, 'x': -197, 'y': -67 },
                ]
            }
        ],
        'reward': [
            { 'itemid': 4310228, 'qty': 1000 }
        ],
        'rewardPointMin': 10,
        'rewardPointMax': 100
    },
	
	{
        'name': "각성 지옥 던전",
        'content': "각성 지옥던전은 지옥던전 보상의 10배를 지급합니다",
        'boss': 9601848,                    
        'bossmap': 911000820,
        'x': -95,
        'y': -62,
        'maplist': [910530101, 911000860, 911000800],
        'stage': [{
                'mapid': 910530101,
                'mobs': [
                    { 'mobid': 9500151, 'qty': 1, 'x': -187, 'y': -67 },
                    { 'mobid': 9500151, 'qty': 1, 'x': -127, 'y': -67 },
                    { 'mobid': 9500151, 'qty': 1, 'x': -117, 'y': -67 },
                    { 'mobid': 9500151, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9500151, 'qty': 1, 'x': -107, 'y': -67 },
                    { 'mobid': 9500151, 'qty': 1, 'x': -177, 'y': -67 },
                ]
            },
            {
                'mapid': 911000860,
                'mobs': [
                    { 'mobid': 9601844, 'qty': 1, 'x': -197, 'y': -67 },
                ]
            },
            {
                'mapid': 911000800,
                'mobs': [
                    { 'mobid': 9601844, 'qty': 1, 'x': -197, 'y': -67 },
                    { 'mobid': 9601845, 'qty': 1, 'x': -197, 'y': -67 },

                ]
            }
        ],
        'reward': [
            { 'itemid': 4310228, 'qty': 10000 }
        ],
        'rewardPointMin': 10,
        'rewardPointMax': 100
    },
	
	{
        'name': "월보테스트",
        'content': "지옥던전은 하드던전 보상의 4배를 지급합니다",
        'boss': 211070300,                    
        'bossmap': 9500007,
        'x': -95,
        'y': -62,
        'maplist': [211070104, 211070102, 211070200],
        'stage': [{
                'mapid': 211070104,
                'mobs': [
                    { 'mobid': 9500144, 'qty': 1, 'x': -197, 'y': -67 },
                ]
            },
            {
                'mapid': 211070102,
                'mobs': [
                    { 'mobid': 9500144, 'qty': 1, 'x': -197, 'y': -67 },
                ]
            },
            {
                'mapid': 211070200,
                'mobs': [
                    { 'mobid': 9500144, 'qty': 1, 'x': -197, 'y': -67 },
                ]
            }
        ],
        'reward': [
            { 'itemid': 4310228, 'qty': 1000 }
        ],
        'rewardPointMin': 10,
        'rewardPointMax': 100
    },
	
	{
        'name': "신들의전쟁",
        'content': "2023-09-24 신들의 전쟁이 시작됩니다.",
        'boss': 9601848,                    
        'bossmap': 993217004,
        'x': -95,
        'y': -62,
        'maplist': [993217021, 280020001, 211070200],
        'stage': [{
                'mapid': 993217021,
                'mobs': [
                    { 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601695, 'qty': 1, 'x': -197, 'y': -67 },
                ]
            },
            {
                'mapid': 280020001,
                'mobs': [
                    { 'mobid': 9601844, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601845, 'qty': 1, 'x': -197, 'y': -67 },
                ]
            },
            {
                'mapid': 410000680,
                'mobs': [
                    { 'mobid': 9601844, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601845, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601844, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601845, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601844, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601845, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601844, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601845, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601848, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601848, 'qty': 1, 'x': -197, 'y': -67 },
					{ 'mobid': 9601848, 'qty': 1, 'x': -197, 'y': -67 },					
                ]
            }
        ],
        'reward': [
            { 'itemid': 4310228, 'qty': 1000 }
        ],
        'rewardPointMin': 10,
        'rewardPointMax': 100
    }
	
]
var dg = -1;
var cmap = -1;

var selectdg = false;

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
    if (cm.getPlayer().getKeyValue(202005281, "d_enter") == 1) {
        dg = cm.getPlayer().getKeyValue(202005281, "d_dg");
        if (cm.getPlayer().getKeyValue(202005281, "d_boss") != 1) {
            cmap = dungeons[dg]['stage'][cm.getPlayer().getKeyValue(202005281, "d_stage")]['mapid'];
        }
        if (cm.getPlayer().getKeyValue(202005281, "d_boss") == 1) {
            if (cm.getPlayer().getMapId() != dungeons[dg]['bossmap']) {
                clear();
                cm.getPlayer().dropMessage(5, "던전을 벗어나 던전이 다시 봉쇄되었습니다.");
                cm.dispose();
                return;
            }

            if (cm.getMonsterCount(dungeons[dg]['bossmap']) == 0 && cm.getPlayer().getMapId() == dungeons[dg]['bossmap']) {
                cm.warp(120043000);
                var msg = "던전을 클리어하신 것을 축하드립니다!" + enter;
                msg += "보상으로 다음과 같은 것 들을 획득하였습니다.#b" + enter;
                for (i = 0; i < 1; i++) {
                    reward = Randomizer.rand(0, dungeons[dg]['reward'].length - 1);

                    rewarditem = dungeons[dg]['reward'][reward]['itemid'];
                    rewardqty = dungeons[dg]['reward'][reward]['qty'];
                    msg += "#i" + rewarditem + "##z" + rewarditem + "# " + rewardqty + "개" + enter;
                    cm.gainItem(rewarditem, rewardqty);
                }

                rewardpoint = Randomizer.rand(dungeons[dg]['rewardPointMin'], dungeons[dg]['rewardPointMax']);

                cm.sendOk(msg);
                clear();
                cm.dispose();
                return;
            } else {
                if (status == 0)
                    cm.sendSimple("몬스터를 다 처치하기 전까진 다음 스테이지로 넘어갈 수 없습니다.\r\n#L1#아무 것도 아닙니다.#l\r\n#L2#토벌을 포기하고 싶습니다.");
                else if (status == 1) {
                    if (sel == 1) {
                        cm.dispose();
                        return;
                    } else if (sel == 2) {
                        clear();
                        cm.warp(993192100);
                        cm.dispose();
                        return;
                    }
                }
            }
        } else {
            if (cm.getPlayer().getMapId() != cmap) {
                clear();
                cm.getPlayer().dropMessage(5, "던전을 벗어나 던전이 다시 봉쇄되었습니다.");
                cm.dispose();
                return;
            }
            if (cm.getMonsterCount(cmap) == 0 && cm.getPlayer().getMapId() == cmap) {
                if ((cm.getPlayer().getKeyValue(202005281, "d_stage") + 1) == dungeons[dg]['stage'].length) {
                    if (status == 0) {
                        cm.sendYesNo("이 다음은 보스가 존재합니다. 다음 스테이지로 이동하실 준비가 다 되셨습니까?");
                    } else if (status == 1) {
                        cm.getPlayer().setKeyValue(202005281, "d_stage", "" + (cm.getPlayer().getKeyValue(202005281, "d_stage") + 1));
                        cstage = cm.getPlayer().getKeyValue(202005281, "d_stage");
                        cm.getPlayer().setKeyValue(202005281, "d_boss", "1");
                        //cm.warp(dungeons[dg]['bossmap']);
                        cm.timeMoveMap(dungeons[dg]['bossmap'], 993192100, nTime);
                        cm.playerMessage(5, "1 : " + dungeons[dg]['bossmap']);
                        cm.spawnMobOnMap(dungeons[dg]['boss'], 1, dungeons[dg]['x'], dungeons[dg]['y'], dungeons[dg]['bossmap']);
                        cm.dispose();
                        return;
                    }
                } else {
                    if (status == 0) {
                        cm.sendYesNo("#b스테이지 " + (cm.getPlayer().getKeyValue(202005281, "d_stage") + 1) + "#k 클리어를 축하드립니다. 다음 스테이지로 이동하시겠습니까?");
                    } else if (status == 1) {
                        cm.getPlayer().setKeyValue(202005281, "d_stage", "" + (cm.getPlayer().getKeyValue(202005281, "d_stage") + 1));
                        cstage = cm.getPlayer().getKeyValue(202005281, "d_stage");

                        cm.timeMoveMap(dungeons[dg]['stage'][cstage]['mapid'], 993192100, nTime);
                        cm.playerMessage(5, "2 : " + dungeons[dg]['stage'][cstage]['mapid']);
                        for (i = 0; i < dungeons[dg]['stage'][cstage]['mobs'].length; i++) {
                            cm.spawnMobOnMap(dungeons[dg]['stage'][cstage]['mobs'][i]['mobid'], dungeons[dg]['stage'][cstage]['mobs'][i]['qty'], dungeons[dg]['stage'][cstage]['mobs'][i]['x'], dungeons[dg]['stage'][cstage]['mobs'][i]['y'], dungeons[dg]['stage'][cstage]['mapid']);
                        }
                        cm.dispose();
                        return;
                    }
                }
            } else {
                if (status == 0)
                    cm.sendSimple("몬스터를 다 처치하기 전까진 다음 스테이지로 넘어갈 수 없습니다.\r\n#L1#아무 것도 아닙니다.#l\r\n#L2#토벌을 포기하고 싶습니다.");
                else if (status == 1) {
                    if (sel == 1) {
                        cm.dispose();
                        return;
                    } else if (sel == 2) {
                        clear();
                        cm.warp(993192100);
                        cm.dispose();
                        return;
                    }
                }
            }
        }
    } else {
        if (status == 0) {
            sdg = dungeons[status];
            var msg = "#fs14##fn나눔고딕#" + sdg['name'] + "#fs12#" + enter;
            msg += sdg['content'] + enter + enter;
            //msg += "#L1#◀#l";
            msg += "　　　";
            msg += "#L2#▶";
            msg += enter + "#L3#이 던전으로 결정하겠습니다.";

            cm.sendSimple(msg);
        } else if (status >= 1 && status <= dungeons.length) {
            if (sel == 1) status -= 2;
            if (sel == 3) {
                seld = status;
                nSeld = seld - 1;
                status = 999;
                for (i = 0; i < dungeons[nSeld]['maplist'].length; i++) {
                    if (cm.getPlayerCount(dungeons[nSeld]['maplist'][i]) > 0) {
                        cm.sendOk("이미 누군가가 도전중인 것 같습니다. 다른 채널에서 다시 시도해주세요.");
                        cm.dispose();
                        return;
                    }
                }
                cm.sendYesNo("지금 바로 입장할 수 있습니다. 바로 입장하시겠습니까?");
            } else {
                cm.getPlayer().dropMessage(5, status);
                sdg = dungeons[status];
                var msg = "#fs14##fn나눔고딕#" + sdg['name'] + "#fs12#" + enter;
                msg += sdg['content'] + enter + enter;
                if (status != 0)
                    msg += "#L1#◀#l";
                msg += "　　　";
                if (status != 2)
                    msg += "#L2#▶";
                if (status != 4)
                    msg += "#L4#";
                msg += enter + "#L3#이 던전으로 결정하겠습니다.";

                cm.sendSimple(msg);

            }
        } else if (status > dungeons.length) {
            nSeld = seld - 1;
            //} else if (status == 1000) {
            cm.getPlayer().setKeyValue(202005281, "d_enter", "1");
            cm.getPlayer().setKeyValue(202005281, "d_stage", "0");
            cm.getPlayer().setKeyValue(202005281, "d_dg", nSeld + "");
            //cm.warp(dungeons[seld]['stage'][0]['mapid']);
            cm.timeMoveMap(dungeons[nSeld]['stage'][0]['mapid'], 993192100, nTime);
            for (i = 0; i < dungeons[nSeld]['stage'][0]['mobs'].length; i++) {
                cm.spawnMobOnMap(dungeons[nSeld]['stage'][0]['mobs'][i]['mobid'], dungeons[nSeld]['stage'][0]['mobs'][i]['qty'], dungeons[nSeld]['stage'][0]['mobs'][i]['x'], dungeons[nSeld]['stage'][0]['mobs'][i]['y'], dungeons[nSeld]['stage'][0]['mapid']);
            }
            cm.dispose();
            return;
        }
    }
}

function clear() {
    cm.getPlayer().setKeyValue(202005281, "d_boss", "0");
    cm.getPlayer().setKeyValue(202005281, "d_enter", "0");
    cm.getPlayer().setKeyValue(202005281, "d_stage", "-1");
    cm.getPlayer().setKeyValue(202005281, "d_dg", "-1");
}