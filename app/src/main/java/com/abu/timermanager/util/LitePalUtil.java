package com.abu.timermanager.util;

import com.abu.timermanager.bean.Memo;

import org.litepal.LitePal;

import java.util.List;

/**
 * LitePal框架工具,增删改查
 */
public class LitePalUtil {

    /**
     * 添加备忘录
     * @param memo 备忘录
     * @return 是否添加成功
     */
    public static boolean addMemo(Memo memo){
        return memo.save();
    }

    /**
     * 查找所有
     * @return
     */
    public static List<Memo> findAllMemo(){
        List<Memo> memos = LitePal.findAll(Memo.class);
        return memos;
    }
}
