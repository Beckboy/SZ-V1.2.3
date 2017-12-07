package com.hyphenate.easeui.model;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojicon.Type;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;
import com.hyphenate.easeui.utils.EaseSmileUtils;

import java.util.Arrays;

public class EaseDefaultEmojiconDatas_gongdou {
    
    private static String[] emojis = new String[]{
        EaseSmileUtils.ee_xiexie,
        EaseSmileUtils.ee_nihao,
//        EaseSmileUtils.ee_xiexie,
//        EaseSmileUtils.ee_nihao,
//        EaseSmileUtils.ee_xiexie,
//        EaseSmileUtils.ee_nihao,
//        EaseSmileUtils.ee_xiexie,
//        EaseSmileUtils.ee_nihao,
//        EaseSmileUtils.ee_xiexie,
//        EaseSmileUtils.ee_nihao,
       
    };
    
    private static int[] icons = new int[]{
        R.drawable.ee_xiexie,
        R.drawable.ee_nihao,
//        R.drawable.ee_xiexie,
//        R.drawable.ee_nihao,
//        R.drawable.ee_xiexie,
//        R.drawable.ee_nihao,
//        R.drawable.ee_xiexie,
//        R.drawable.ee_nihao,
//        R.drawable.ee_xiexie,
//        R.drawable.ee_nihao,
    };

    
    
    private static final EaseEmojiconGroupEntity DATA = createData();
    
    private static EaseEmojiconGroupEntity createData(){
        EaseEmojiconGroupEntity emojiconGroupEntity = new EaseEmojiconGroupEntity();
        EaseEmojicon[] datas = new EaseEmojicon[icons.length];
        for(int i = 0; i < icons.length; i++){
            datas[i] = new EaseEmojicon(icons[i], emojis[i], Type.BIG_EXPRESSION);
            datas[i].setBigIcon(icons[i]);
            //you can replace this to any you want
            datas[i].setName(emojis[i]);
            datas[i].setIdentityCode("KSee" + (1000 + i + 1));
        }
        emojiconGroupEntity.setEmojiconList(Arrays.asList(datas));
        emojiconGroupEntity.setIcon(R.drawable.ee_xiexie);
        emojiconGroupEntity.setType(Type.BIG_EXPRESSION);
        return emojiconGroupEntity;
    }
    
    public static EaseEmojiconGroupEntity getData(){
        return DATA;
    }
}
