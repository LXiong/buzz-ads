
namespace java com.buzzinate.buzzads.thrift

enum AdNetworkEnum {
	LEZHI = 0,
	BSHARE = 1,
	BUZZADS = 2,
	WJF = 3,
	MULTIMEDIA = 4,
}

enum AdEntryTypeEnum {
    TEXT = 0,
    IMAGE = 1,
    Flash = 2,
    UNKNOWN = 99,
}

enum AdEntrySizeEnum {
    SIZE80x80 = 1,
    SIZE660x90 = 2,
    SIZE610x100 = 3,
    SIZE300x250 = 4,
    SIZE300x100 = 5,
    SIZE200x200 = 6,
    SIZE300x280 = 7,
}

struct AdParam {
    1: required string url,
    2: optional string title,
    3: optional string keywords,
    4: required AdEntryTypeEnum resourceType = AdEntryTypeEnum.IMAGE,
    5: optional i32 count = 1,
    6: optional string userid = "1",
    7: optional string ip,
    8: required AdNetworkEnum netWork = AdNetworkEnum.LEZHI,
    9: optional string uuid,
    10: optional AdEntrySizeEnum resourceSize = AdEntrySizeEnum.SIZE80x80,
}

struct AdItem {
    1: required string url,
    2: required string title,
    3: optional string pic,
    4: optional i32 adEntryId,
    5: optional string displayUrl,
    6: optional string description,
}


service AdServices {
    list<AdItem> serve(1:AdParam param),
}


