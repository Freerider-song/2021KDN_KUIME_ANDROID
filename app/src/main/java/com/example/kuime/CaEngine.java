package com.example.kuime;

import android.content.Context;
import android.util.Log;

import java.sql.Time;

public class CaEngine {

    public static final int CB_NONE = 0;

    //API 요청
    public static final int CB_CHECK_BLD_LOGIN = 1001;
    public static final int CB_GET_BLD_ADMIN_INFO = 1002;
    public static final int CB_CHANGE_ADMIN_PASSWORD = 1003;
    public static final int CB_REQUEST_AUTH_CODE = 1004;
    public static final int CB_CHECK_AUTH_CODE = 1005;
    public static final int CB_CHECK_ADMIN_CANDIDATE = 1006;
    public static final int CB_GET_ADMIN_USAGE_CURRENT = 1007;
    public static final int CB_GET_ADMIN_ALARM_LIST = 1008;
    public static final int CB_GET_BLD_NOTICE_LIST = 1009;
    public static final int CB_SET_BLD_NOTICE_AS_READ = 1010;
    public static final int CB_GET_SAVE_RESULT_DAILY = 1011;
    public static final int CB_GET_SAVE_RESULT = 1012;
    public static final int CB_GET_USAGE_FOR_ALL_METER_DAY = 1013;
    public static final int CB_GET_USAGE_FOR_ALL_METER_MONTH = 1014;
    public static final int CB_GET_USAGE_FOR_ALL_METER_YEAR = 1015;
    public static final int CB_CHANGE_ADMIN_BLD_SETTINGS= 1016;
    public static final int CB_GET_BLD_ALARM_LIST=1017;
    public static final int CB_SET_SAVE_ACT_BEGIN = 1018;
    public static final int CB_SET_SAVE_ACT_END = 1019;
    public static final int CB_GET_UNREAD_BLD_NOTICE_COUNT = 1020;
    public static final int CB_GET_UNREAD_BLD_ALARM_COUNT = 1021;



    public static final int AUTH_TYPE_UNKNOWN = 1000;
    public static final int AUTH_TYPE_SUBSCRIBE = 1001;
    public static final int AUTH_TYPE_CHANGE_PASSWORD = 1002;

    public static final int MENU_USAGE = 100;
    public static final int MENU_USAGE_DAILY = 101;
    public static final int MENU_USAGE_MONTHLY = 102;
    public static final int MENU_USAGE_YEARLY = 103;
    public static final int MENU_HOME = 200;
    public static final int MENU_SAVING = 300;
    public static final int MENU_ALARM = 500;
    public static final int MENU_NOTICE = 600;
    public static final int MENU_SETTING = 800;
    public static final int MENU_LOGOUT = 900;

    public static final String[] NO_CMD_ARGS = new String[]{};

    public static final int ALARM_TYPE_UNKNOWN = 0;
    public static final int ALARM_TEST = 2;
    public static final int ALARM_NEW_NOTICE = 3001; // 새 공지사항발생
    public static final int ALARM_PLAN_ELEM_BEGIN = 3002; //절감항목 시작
    public static final int ALARM_PLAN_ELEM_END = 3003; // 절감항목종료
    public static final int ALARM_SAVE_ACT_MISSED = 3004; // 미시행절감조치 있음 알림
    public static final int ALARM_THIS_MONTH_USAGE_AT = 3005; //정해진 시간에 사용량과 사용요금 알림
    public static final int ALARM_THIS_MONTH_KWH_OVER = 3006; //설정한 사용량 임계치 초과 알림
    public static final int ALARM_THIS_MONTH_WON_OVER = 3007; //설정한 사용요금 임계치 초과 알림
    public static final int ALARM_METER_KWH_OVER_SAVE_REF = 3008; //계측기별 사용량이 절감기준 사용량 초과
    public static final int ALARM_METER_KWH_OVER_SAVE_PLAN = 3009;// 계측기별 사용량이 절감목표 사용량 초과

    public CaEngine() {

    }

    public final CaResult executeCommand(CaArg Arg, final int nCallMethod, final boolean bSync, final boolean bShowWaitDialog, Context Ctx, IaResultHandler ResultHandler) {

        CaTask Task = new CaTask(nCallMethod, bShowWaitDialog, Ctx, ResultHandler);
        Task = (CaTask) Task.execute(Arg);

        CaResult Result = null;

        if (bSync) {
            try {
                Result = Task.get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return Result;
    }

    public void CheckLogin(final String Id, final String Password, Context Ctx, IaResultHandler ResultHandler){
        Log.i("ENGINE", "Id=" + Id + ", Password=" + Password);

        CaArg Arg = new CaArg("CheckLogin", NO_CMD_ARGS, null);
        Arg.addArg("Id", Id);
        Arg.addArg("Password", Password);



        executeCommand(Arg, CB_CHECK_BLD_LOGIN, false, true, Ctx, ResultHandler);
    }


}
