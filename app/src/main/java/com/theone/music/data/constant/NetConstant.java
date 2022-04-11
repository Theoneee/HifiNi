package com.theone.music.data.constant;//  ┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//    ┃　　　┃                  神兽保佑
//    ┃　　　┃                  永无BUG！
//    ┃　　　┗━━━┓
//    ┃　　　　　　　┣┓
//    ┃　　　　　　　┏┛
//    ┗┓┓┏━┳┓┏┛
//      ┃┫┫　┃┫┫
//      ┗┻┛　┗┻┛

import java.util.HashMap;
import java.util.Map;

import rxhttp.wrapper.annotation.DefaultDomain;

/**
 * @author The one
 * @date 2022-04-11 11:05
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class NetConstant {

    @DefaultDomain
    public static final String BASE_URL = "https://www.hifini.com/";

    public static final String INDEX = "index-%d.htm";
    public static final String FORUM = "forum-%d-%d.htm";
    public static final String SEARCH = "search-%s.htm";
    public static final String HOT = "index-0-2.htm";
    public static final String RANK = "index-0-%d.htm";
    public static final String TAG = "tag-%d-%d.htm";

    private static Map<String,Integer> CATEGORY;

    public static Map<String,Integer> getCategoryList(){
        if(null == CATEGORY){
            CATEGORY = new HashMap<>();
            CATEGORY.put("华语",1);
            CATEGORY.put("日韩",15);
            CATEGORY.put("欧美",10);
            CATEGORY.put("Remix",11);
            CATEGORY.put("纯音乐",12);
            CATEGORY.put("异次元",13);
        }
        return CATEGORY;
    }

    private static Map<String,Integer> RANK_TYPES;

    public static Map<String,Integer> getRankTypes(){
        if(null == RANK_TYPES){
            RANK_TYPES = new HashMap<>();
            RANK_TYPES.put("日榜",5);
            RANK_TYPES.put("周榜",4);
            RANK_TYPES.put("月榜",3);
        }
        return RANK_TYPES;
    }

}
