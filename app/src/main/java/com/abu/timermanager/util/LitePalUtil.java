package com.abu.timermanager.util;

import android.content.ContentValues;

import com.abu.timermanager.bean.Memo;

import org.litepal.LitePal;

import java.util.List;

/**
 * LitePal框架工具,增删改查
 */
public class LitePalUtil {

    /**
     * 添加备忘录,没有该条记录添加，有则为修改
     *
     * @param memo 备忘录
     * @return 是否添加成功
     */
    public static boolean addMemo(Memo memo) {
        Memo memoByContent = findMemoByContent(memo.getContent());
        if (memoByContent == null) {
            return memo.save();
        } else {
            return updateMemo(memo);
        }
    }

    /**
     * 查找所有
     *
     * @return 所有memo
     */
    public static List<Memo> findAllMemo() {
        List<Memo> memos = LitePal.findAll(Memo.class);
        return memos;
    }

    /**
     * 根据content查找memo
     *
     * @param content 备忘录内容
     * @return 该条备忘录记录
     */
    public static Memo findMemoByContent(String content) {
        List<Memo> memos = LitePal.where("content = ?", content).find(Memo.class);
        if (memos.size() != 0) {
            return memos.get(0);
        } else {
            return null;
        }
    }

    /**
     * 修改memo
     *
     * @param memo 被修改的memo
     * @return 是否修改成功
     */
    public static boolean updateMemo(Memo memo) {
        ContentValues values = new ContentValues();
        values.put("title", memo.getTitle());
        values.put("content", memo.getContent());
        values.put("bgColor", memo.getBgColor());
        values.put("createTime", memo.getCreateTime());
        values.put("remindTime", memo.getRemindTime());
        int i = LitePal.updateAll(Memo.class, values, "content = ?", memo.getContent());
        return i > 0 ? true : false;
    }

    /**
     * 删除memo
     *
     * @param memo 需要删除的memo
     * @return 是否删除成功
     */
    public static boolean deleteMemo(Memo memo) {
        int i = LitePal.deleteAll(Memo.class, "content = ?", memo.getContent());
        return i > 0 ? true : false;
    }
}
