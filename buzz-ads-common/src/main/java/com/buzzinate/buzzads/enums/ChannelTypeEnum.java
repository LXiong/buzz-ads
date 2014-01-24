package com.buzzinate.buzzads.enums;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.buzzinate.common.util.hibernate.IntegerValuedEnum;


/**
 * 媒体类别：汽车、女性等，类别信息请使用考Buzzinate Taxonomy的一级类别
 * 
 * @author Qiong Wang<qiong.wang@buzzinate.com>
 * 
 *         2013-5-13
 */
public enum ChannelTypeEnum implements IntegerValuedEnum {
    Culture(1),
    JobsAndEducation(2),
    Games(3),
    ConsumerElectronics(4),
    HealthAndWellness(5),
    BeautyAndCosmetics(6),
    Automobiles(7),
    SportsAndFitness(8),
    InternetAndTechnology(9),
    BabyAndMaternity(10),
    PersonalFinance(11),
    ConsultingAndServices(12),
    Industrial(13),
    FemaleFashion(14),
    LeisureTravelAndBusinessTravel(15),
    FoodAndDrink(16),
    HomeAndLifestyle(17),
    RealEstateAndConstruction(18),
    OfficeEquipment(19),
    Science(20),
    Alcohol(21),
    Politics(22),
    ArtsAndEntertainment(23),
    ReligionandPsychics(24),
    Traffic(25),
    Commerce(26),
    UNKNOWN(99);

    private static Map<Integer, String> typeSelectorMap = new LinkedHashMap<Integer, String>();
    private int value;
    
    static {
        typeSelectorMap.put(1, "文化艺术");
        typeSelectorMap.put(2, "教育培训");
        typeSelectorMap.put(3, "游戏");
        typeSelectorMap.put(4, "消费数码");
        typeSelectorMap.put(5, "健康医疗");
        typeSelectorMap.put(6, "美容及化妆品");
        typeSelectorMap.put(7, "汽车");
        typeSelectorMap.put(8, "运动健身");
        typeSelectorMap.put(9, "IT及信息产业");
        typeSelectorMap.put(10, "母婴育儿");
        typeSelectorMap.put(11, "金融财经");
        typeSelectorMap.put(12, "咨询及服务");
        typeSelectorMap.put(13, "工业");
        typeSelectorMap.put(14, "时尚");
        typeSelectorMap.put(15, "旅游商旅");
        typeSelectorMap.put(16, "食品美食");
        typeSelectorMap.put(17, "家居生活");
        typeSelectorMap.put(18, "房地产及建筑");
        typeSelectorMap.put(19, "电脑办公");
        typeSelectorMap.put(20, "科学");
        typeSelectorMap.put(21, "酒类");
        typeSelectorMap.put(22, "时政");
        typeSelectorMap.put(23, "娱乐");
        typeSelectorMap.put(24, "宗教和灵媒");
        typeSelectorMap.put(25, "交通");
        typeSelectorMap.put(26, "商业");
        typeSelectorMap.put(99, "其它");
    }

    ChannelTypeEnum(int value) {
        this.value = value;
    }
    
    @Override
    public int getCode() {
        return this.value;
    }
    
    public static ChannelTypeEnum findByValue(int param) {
        ChannelTypeEnum[] values = values();
        for (ChannelTypeEnum e : values) {
            if (e.getCode() == param) {
                return e;
            }
        }
        return ChannelTypeEnum.UNKNOWN;
    }
    
    /**
     * Gets a Map for a selector html element.
     * @return
     */
    public static Map<Integer, String> getSelector() {
        return typeSelectorMap;
    }

    /**
     * Gets a Map for a selector html element.
     * @return
     */
    public static Map<ChannelTypeEnum, String> typeSelector() {
        Map<ChannelTypeEnum, String> typeSelector = new HashMap<ChannelTypeEnum, String>(values().length);
        for (ChannelTypeEnum type : values()) {
            typeSelector.put(type, typeSelectorMap.get(type.getCode()));
        }
        return typeSelector;
    }
}

