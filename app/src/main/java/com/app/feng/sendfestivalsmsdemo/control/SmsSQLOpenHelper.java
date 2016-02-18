package com.app.feng.sendfestivalsmsdemo.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.feng.sendfestivalsmsdemo.element.CollectMessage;
import com.app.feng.sendfestivalsmsdemo.element.Festival;
import com.app.feng.sendfestivalsmsdemo.element.HistoryMessage;
import com.app.feng.sendfestivalsmsdemo.element.Msg;

/**
 * Created by feng on 2015/10/24.
 */
public class SmsSQLOpenHelper extends SQLiteOpenHelper {
    private static final String DB_name = "smsSend.db";
    private static final int DB_version = 1;

    private static SmsSQLOpenHelper mHelper;

    public static SmsSQLOpenHelper getInstance(Context context) {
        if (mHelper == null) {
            synchronized (SmsSQLOpenHelper.class) {
                if (mHelper == null) {
                    mHelper = new SmsSQLOpenHelper(context.getApplicationContext());
                }
            }
        }
        return mHelper;
    }

    private SmsSQLOpenHelper(Context context) {
        super(context, DB_name, null, DB_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_create = "create table if not exists " + HistoryMessage.TABLE_NAME + "(" +
                "_id integer primary key autoincrement , " +
                HistoryMessage.COLUMN_MSG + " text ," +
                HistoryMessage.COLUMN_NAME + " text ," +
                HistoryMessage.COLUMN_NUMBER + " text ," +
                HistoryMessage.COLUMN_FESTIVAL_NAME + " text ," +
                HistoryMessage.COLUMN_DATE + " text " + ")";
        String sql_create2 = "create table " + Festival.TABLE_FESTIVAL + "(" +
                Festival.FESTIVAL_ID + " integer primary key autoincrement ," +
                Festival.FESTIVAL_NAME + " text ," +
                Festival.FESTIVAL_CODE + " text )";
        String sql_create3 = "create table " + Msg.TABLE_MSG + "(" +
                Msg.MSG_ID + " integer primary key autoincrement ," +
                Msg.MSG_FESTIVAL_NAME + " text ," +
                Msg.MSG_CONTENT + " text )";
        String sql_collect = "create table " + CollectMessage.TABLE_COLLECT + "(" +
                CollectMessage.COL_ID + " integer primary key autoincrement ," +
                CollectMessage.COL_FESTIVAL_NAME + " text," +
                CollectMessage.COL_CONTENT + " text )";

        db.execSQL(sql_create);
        db.execSQL(sql_create2);
        db.execSQL(sql_create3);
        db.execSQL(sql_collect);

        //创建节日表
        ContentValues festival = new ContentValues();
        festival.put(Festival.FESTIVAL_NAME, "国庆节");
        festival.put(Festival.FESTIVAL_CODE, "96");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();
        festival.put(Festival.FESTIVAL_NAME, "中秋节");
        festival.put(Festival.FESTIVAL_CODE, "95");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();
        festival.put(Festival.FESTIVAL_NAME, "元旦");
        festival.put(Festival.FESTIVAL_CODE, "121");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();
        festival.put(Festival.FESTIVAL_NAME, "春节");
        festival.put(Festival.FESTIVAL_CODE, "62");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();
        festival.put(Festival.FESTIVAL_NAME, "端午节");
        festival.put(Festival.FESTIVAL_CODE, "92");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();
        festival.put(Festival.FESTIVAL_NAME, "七夕节");
        festival.put(Festival.FESTIVAL_CODE, "93");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();
        festival.put(Festival.FESTIVAL_NAME, "圣诞节");
        festival.put(Festival.FESTIVAL_CODE, "120");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();
        festival.put(Festival.FESTIVAL_NAME, "除夕");
        festival.put(Festival.FESTIVAL_CODE, "63");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();
        festival.put(Festival.FESTIVAL_NAME, "元宵节");
        festival.put(Festival.FESTIVAL_CODE, "59");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();

        festival.put(Festival.FESTIVAL_NAME, "小年");
        festival.put(Festival.FESTIVAL_CODE, "64");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();

        festival.put(Festival.FESTIVAL_NAME, "情人节");
        festival.put(Festival.FESTIVAL_CODE, "60");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();

        festival.put(Festival.FESTIVAL_NAME, "愚人节");
        festival.put(Festival.FESTIVAL_CODE, "131");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();

        festival.put(Festival.FESTIVAL_NAME, "开业短信");
        festival.put(Festival.FESTIVAL_CODE, "240");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();

        festival.put(Festival.FESTIVAL_NAME, "升职短信");
        festival.put(Festival.FESTIVAL_CODE, "242");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();

        festival.put(Festival.FESTIVAL_NAME, "乔迁短信");
        festival.put(Festival.FESTIVAL_CODE, "241");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();

        festival.put(Festival.FESTIVAL_NAME, "喜得贵子");
        festival.put(Festival.FESTIVAL_CODE, "225");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();

        festival.put(Festival.FESTIVAL_NAME, "结婚短信");
        festival.put(Festival.FESTIVAL_CODE, "221");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();

        festival.put(Festival.FESTIVAL_NAME, "思念短信");
        festival.put(Festival.FESTIVAL_CODE, "256");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();

        festival.put(Festival.FESTIVAL_NAME, "情话短信");
        festival.put(Festival.FESTIVAL_CODE, "176");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();

        festival.put(Festival.FESTIVAL_NAME, "表白短信");
        festival.put(Festival.FESTIVAL_CODE, "231");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();

        festival.put(Festival.FESTIVAL_NAME, "感谢短信");
        festival.put(Festival.FESTIVAL_CODE, "278");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();

        festival.put(Festival.FESTIVAL_NAME, "早安");
        festival.put(Festival.FESTIVAL_CODE, "150");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();

        festival.put(Festival.FESTIVAL_NAME, "晚安");
        festival.put(Festival.FESTIVAL_CODE, "212");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();

        festival.put(Festival.FESTIVAL_NAME, "生活知识");
        festival.put(Festival.FESTIVAL_CODE, "158");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();

        festival.put(Festival.FESTIVAL_NAME, "生病问候");
        festival.put(Festival.FESTIVAL_CODE, "216");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();

        festival.put(Festival.FESTIVAL_NAME, "保健知识");
        festival.put(Festival.FESTIVAL_CODE, "49");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();

        festival.put(Festival.FESTIVAL_NAME, "幽默祝福");
        festival.put(Festival.FESTIVAL_CODE, "68");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();

        festival.put(Festival.FESTIVAL_NAME, "生日祝福");
        festival.put(Festival.FESTIVAL_CODE, "67");
        db.insert(Festival.TABLE_FESTIVAL, null, festival);
        festival.clear();

        //创建短信表
        ContentValues msg = new ContentValues();
        msg.put(Msg.MSG_FESTIVAL_NAME, "国庆节");
        msg.put(Msg.MSG_CONTENT, "云淡风也轻，秋叶飘满天，金秋收获季，共庆国庆节；祝您国庆佳节天天好心情，事事都如意！");
        db.insert(Msg.TABLE_MSG, null, msg);
        msg.clear();
        msg.put(Msg.MSG_FESTIVAL_NAME, "国庆节");
        msg.put(Msg.MSG_CONTENT, "朋字双月并肩行,远隔千里两地明;祝友健康阂家乐,事业顺利展宏程;国庆佳节同喜日,捧杯聚首秋月中");
        db.insert(Msg.TABLE_MSG, null, msg);
        msg.clear();
        msg.put(Msg.MSG_FESTIVAL_NAME, "中秋节");
        msg.put(Msg.MSG_CONTENT, "过几天就是中秋了，不知道现在发祝福短信给你是否有点早，不过我想通了，提前的祝福和迟到的祝福都没有关系，因为我对你祝福的心是永远都不会改变的。中秋快乐！");
        db.insert(Msg.TABLE_MSG, null, msg);
        msg.clear();
        msg.put(Msg.MSG_FESTIVAL_NAME, "中秋节");
        msg.put(Msg.MSG_CONTENT, "送你一个月饼,含量成分:100%纯关心；配料:甜蜜+快乐+开心+宽容+忠诚=幸福；保质期:一辈子；保存方法:珍惜.");
        db.insert(Msg.TABLE_MSG, null, msg);
        msg.clear();
        msg.put(Msg.MSG_FESTIVAL_NAME, "元旦");
        msg.put(Msg.MSG_CONTENT, "新年新气象，百事可乐，万事七喜，心情雪碧，学习芬达，工作红牛，生活茹梦，爱情鲜橙多，天天娃哈哈，月月乐百事");
        db.insert(Msg.TABLE_MSG, null, msg);
        msg.clear();
        msg.put(Msg.MSG_FESTIVAL_NAME, "元旦");
        msg.put(Msg.MSG_CONTENT, "我这份祝福跨过重重高山，掠过条条小溪，跳过马路，窜出胡同，闪过卖冰糖葫芦的老太太，钻进你耳朵里－祝新年快乐！");
        db.insert(Msg.TABLE_MSG, null, msg);
        msg.clear();

        msg.put(Msg.MSG_FESTIVAL_NAME, "春节");
        msg.put(Msg.MSG_CONTENT, "心连心，接受春的赏赐。愿你快快乐乐地迎新年。我们不常拥有新年，却常拥有新的一天。愿你每一天，都充满幸福和喜悦。");
        db.insert(Msg.TABLE_MSG, null, msg);
        msg.clear();
        msg.put(Msg.MSG_FESTIVAL_NAME, "端午节");
        msg.put(Msg.MSG_CONTENT, "端起轻松的酒杯，与美丽举杯；端起如意的酒杯，与成功交杯；端起惬意的酒杯，与健康碰杯；端起幸福的酒杯，与快乐干杯。端午节到了，愿你端起人生美满的酒杯，快乐相随。");
        db.insert(Msg.TABLE_MSG, null, msg);
        msg.clear();
        msg.put(Msg.MSG_FESTIVAL_NAME, "七夕节");
        msg.put(Msg.MSG_CONTENT, "悠悠银河人尽望，牛郎织女情满膛。千里鹊桥来相会，葡萄架下诉忠肠。我劝天下有情人，忙碌莫把祝福忘。祝七夕情人节快乐！");
        db.insert(Msg.TABLE_MSG, null, msg);
        msg.clear();
        msg.put(Msg.MSG_FESTIVAL_NAME, "七夕节");
        msg.put(Msg.MSG_CONTENT, "老天给我最大的恩赐就是让我拥有了你，拥有了你的爱！在这七夕之夜，我祝福我们永远在一起，永不分离！！！");
        db.insert(Msg.TABLE_MSG, null, msg);
        msg.clear();

        msg.put(Msg.MSG_FESTIVAL_NAME, "圣诞节");
        msg.put(Msg.MSG_CONTENT, "深深祝福，丝丝情谊，串串思念，化作一份礼物，留在你的心田，祝你圣诞快乐，新年幸福！");
        db.insert(Msg.TABLE_MSG, null, msg);
        msg.clear();
        msg.put(Msg.MSG_FESTIVAL_NAME, "圣诞节");
        msg.put(Msg.MSG_CONTENT, "音乐卡是我的挂念，钟声是我的问候,歌声是我的祝福，雪花是我的贺卡，美酒是我的飞吻，快乐是我的礼物！圣诞快乐！");
        db.insert(Msg.TABLE_MSG, null, msg);
        msg.clear();
        msg.put(Msg.MSG_FESTIVAL_NAME, "除夕");
        msg.put(Msg.MSG_CONTENT, "生活奔忙一天天，身心放松抽根烟；适当进补多锻炼，防寒保暖多睡眠；大雪小雪降瑞雪，大寒小寒心不寒；坐票站票买到票，欢欢喜喜过大年！");
        db.insert(Msg.TABLE_MSG, null, msg);
        msg.clear();
        msg.put(Msg.MSG_FESTIVAL_NAME, "除夕");
        msg.put(Msg.MSG_CONTENT, "今日大寒，满天雪花飞舞，临近年关仅六天，老叟童依皆大欢；瑞雪兆丰年，龙年伊始话慨感；悦心不减当年；炮声处处映门庭；欢畅笑语迎新年！");
        db.insert(Msg.TABLE_MSG, null, msg);
        msg.clear();
        msg.put(Msg.MSG_FESTIVAL_NAME, "元宵节");
        msg.put(Msg.MSG_CONTENT, "元宵送你一份汤圆，里面包的是我的心愿:愿你在生活中十分热情九分优雅八分聪慧七分敏锐六分风趣五分温柔四个密友三分豪放二分含蓄一分浪漫！");
        db.insert(Msg.TABLE_MSG, null, msg);
        msg.clear();
        msg.put(Msg.MSG_FESTIVAL_NAME, "元宵节");
        msg.put(Msg.MSG_CONTENT, "天上的月儿圆，锅里的元宵圆，吃饭的桌儿圆，你我的情更圆，就像元宵一样黏黏呼呼团团圆圆。");
        db.insert(Msg.TABLE_MSG, null, msg);
        msg.clear();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Msg.TABLE_MSG);
        db.execSQL("DROP TABLE IF EXISTS " + Festival.TABLE_FESTIVAL);

        onCreate(db);
    }
}
