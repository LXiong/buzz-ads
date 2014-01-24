

namespace java com.buzzinate.buzzads.data.thrift

// 广告状态
enum TAdStatusEnum {
    READY = 0,
    ENABLED = 1,        // 投放中
    PAUSED = 2,         // 已暂停
    DISABLED = 3,       // 已禁用
    SUSPENDED = 4,      // 已挂起
    DELETED = 5,        // 已删除
    VERIFYING = 6,      // 认证中
    REJECTED = 7,       // 已拒绝
}

// 广告投放网络
enum TAdNetworkEnum {
    LEZHI = 0,
    BSHARE = 1,
    BUZZADS = 2,
    WJF = 3,
    MULTIMEDIA = 4,
}

// 广告出价类型
enum TBidTypeEnum {
    CPM = 0,
    CPA = 1,
    CPS = 2,
    CPC = 3,
    CPT = 4,
    CPD = 5,
}

// 广告资源类型
enum TResourceTypeEnum {
    TEXT = 0,
    IMAGE = 1,
    Flash = 2,
    UNKNOWN = 99,
}
// 广告投放星期
enum TWeekDay{
    MONDAY=1,
    TUESDAY=2,
    WEDNESDAY=3,
    THURSDAY=4,
    FRIDAY=5,
    SATURDAY=6,
    SUNDAY=7,
}

enum TAdsTypeEnum {
    AVERAGE = 0,
    ACCELERATE = 1,
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

// 设置查询广告的条件
struct TAdCriteria {
   1: optional i32 campaignId,
   2: optional i32 orderId,
   3: optional i32 entryId,
   4: optional i32 advertiserId,
   5: optional set<TAdStatusEnum> status,
   6: optional set<TAdNetworkEnum> network,
   7: optional set<TBidTypeEnum> bidType,
}

// 指定返回结果的分页
struct TPagination {
   1: i16 start = 0,
   2: i16 count = 50,
}

// 广告计划投放时间段
struct TScheduleTime {
    1: i64 start,
    2: i64 end,
}

// 广告信息
struct TAdItem {
   1: i32 campaignId,
   2: i32 orderId,
   3: i32 entryId,
   4: i32 advertiserId,
   5: TAdStatusEnum status,
   6: optional i32 bidPrice,
   7: optional string keywords,
   8: optional string link,
   9: optional TResourceTypeEnum resourceType,
   10: optional string resourceUrl,
   11: optional string displayUrl,
   12: optional string title,
   13: optional string description,
   14: optional set<TAdNetworkEnum> network,
   15: optional TBidTypeEnum bidType,
   16: optional i64 startDate,
   17: optional i64 endDate,
   18: optional set<TWeekDay> scheduleDay,
   19: optional set<TScheduleTime> scheduleTime,
   20: optional set<string> locations,
   21: optional set<i32> audienceCategories,
   22: optional string destination,
   23: i32 orderFrequency,
   24: i32 entryFrequency,
   25: TAdsTypeEnum adsType = TAdsTypeEnum.AVERAGE,
   26: optional set<string> channels,
   27: required AdEntrySizeEnum resourceSize = AdEntrySizeEnum.SIZE80x80,
}

// 广告数据
struct TAdStats {
   1: i32 entryId,
   2: i64 dateDay,
   3: TAdNetworkEnum network,
   4: i32 views,
   5: i32 clicks,
   6: i32 cpcClicks,
}

// 广告数据查询的条件
struct TAdStatsCriteria {
   1: optional i32 entryId,
   2: optional i64 dateDay,
   3: optional set<TAdNetworkEnum> network,
}

// 广告活动的预算数据
struct TCampaignBudget {
   1: i32 campaignId,
   2: i32 advertiserId,
   3: i64 advertiserBalance,
   4: i64 budgetDay,
   5: i64 budgetTotal,
   6: i64 maxBudgetDay,
   7: i64 maxBudgetTotal,
}

// 媒体冻结数据
struct TFrozenChannel {
    1: required string uuid,
    2: required i32 channelLevel,
    3: required string domain,
}


enum TPublisherContactRevMethod {
    ALIPAY = 0,
}

enum TPublisherContactStatus {
    NORMAL = 0,
    FROZEN = 1,
}

struct TPublisherContactInfo {
    1: i32 userId,
    2: optional string name,
    3: optional string email,
    4: optional string mobile,
    5: optional string qq,
    6: optional TPublisherContactRevMethod revMethod,
    7: optional string revAccount,
    8: optional string revName,
    9: optional string revBank,
    10: optional TPublisherContactStatus status, 
}

struct TPublisherSiteConfig {
    1: string uuid,
    2: optional string blackKeywords,
    3: optional set<string> blackDomains,
    4: optional set<string> blackEntryLinks,
}


exception MissingRequiredFieldException {}

exception PublisherContactNotFoundException {}


service TAdDataAccessServices {

    // Find all ad items by criteria
    list<TAdItem> findAdItems(1: TAdCriteria criteria, 2: TPagination pagination),

    // Find all ad stats by criteria
    list<TAdStats> findAdStats(1: TAdStatsCriteria criteria, 2: TPagination pagination),

    // Find budget info of campaigns
    list<TCampaignBudget> findCampaignBudgets(1: list<i32> campaignIds),
    
    // Update an ad item.
    // Only update the fields that exist in the parameter.
    void updateAdItem(1: TAdItem adItem)
        throws (1: MissingRequiredFieldException mrfe),
    
    // Find the publisher contact by user id
    TPublisherContactInfo findPublisherContact(1: i32 userId)
        throws (1: PublisherContactNotFoundException pcnfe),
    
    // Save or update publisher contact info.
    // Only update the fields that exist in the parameter publisherContact.
    void saveOrUpdatePublisherContact(1: TPublisherContactInfo publisherContact)
        throws (1: MissingRequiredFieldException mrfe),
        
    TPublisherSiteConfig getPublisherSiteConfig(1: string uuid),

    list<TFrozenChannel> findAllFrozenList(),
}

