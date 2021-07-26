package com.example.kuime;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CaInfo {

    public static final int DEFAULT_REQUEST_NOTICE_COUNT = 10;

    public SimpleDateFormat m_dfStd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public SimpleDateFormat m_dfyyyyMMddhhmmStd=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public SimpleDateFormat m_dfyyyyMMddhhmm=new SimpleDateFormat("yyyyMMddHHmm");
    public SimpleDateFormat m_dfyyyyMMddhhmmss=new SimpleDateFormat("yyyyMMddHHmmss");
    public SimpleDateFormat m_dfyyyyMMdd=new SimpleDateFormat("yyyyMMdd");
    public SimpleDateFormat m_dfyyyyMMddhhmm_ampm=new SimpleDateFormat("yyyy-MM-dd hh:mm a");

    public DecimalFormat m_dfKwh = new DecimalFormat("0.#"); // 12345.7
    public DecimalFormat m_dfPercent = new DecimalFormat("0.##"); // 12345.7
    public DecimalFormat m_dfWon = new DecimalFormat("#,##0"); // 87,654


    public int m_nResultCode =0;
    public int m_nSeqAdmin=0;
    public int m_nTeamType=0;

    public String m_strAdminName="";
    public String m_strAdminPhone="";
    public int m_nUnreadNoticeCount=0;
    public int m_nUnreadAlarmCount=0;
    public Date m_dtLastLogin=null;
    public Date m_dtCreated=null;
    public Date m_dtModified=null;
    public Date m_dtChangePassword =null;
    public int m_nSeqTeam=0;
    public String m_strTeamName="";
    public boolean m_bNotiAll=true;
    public boolean m_bNotiKwh=true;
    public boolean m_bNotiWon=true;
    public boolean m_bNotiSavingStandard=true;
    public boolean m_bNotiSavingGoal=true;
    public boolean m_bNotiUsageAtTime=true;
    public double m_dThresholdThisMonthKwh=0.0;
    public double m_dThresholdThisMonthWon=0.0;
    public int m_nHourNotiThisMonthUsage=0;

    public int m_nSeqSite = 0;
    public int m_nSiteType=0;
    public String m_strSiteName="";
    public int m_nBuiltYear=0;
    public int m_nBuiltMonth=0;
    public String m_strFloorInfo="";
    public String m_strHomePage="";
    public String m_strSiteFax="";
    public String m_strSitePhone="";
    public String m_strSiteAddress="";
    public double m_dSiteDx=0.0;
    public double m_dSiteDy=0.0;
    public double m_dKwContracted=0.0;
    public int m_nReadDay=0;
    public int m_nSeqSavePlanActive=0;

    public int m_nSeqSavePlan=0;
    public int m_nSeqSaveRef=0;
    public String m_strSavePlanName ="";
    public String m_strSaveRefName = "";
    public double m_dSaveKwhTotalFromElem=0.0;
    public double m_dSaveWonTotalFromElem=0.0;
    public double m_dSaveKwhTotalFromMeter=0.0;
    public double m_dSaveWonTotalFromMeter=0.0;
    public double m_dKwhRefForAllMeter=0.0;
    public double m_dKwhPlanForAllMeter=0.0;
    public double m_dKwhRealForAllMeter=0.0;
    public double m_dWonRefForAllMeter=0.0;
    public double m_dWonPlanForAllMeter=0.0;
    public double m_dWonRealForAllMeter=0.0;
    public Date m_dtSavePlanCreated= null;
    public Date m_dtSavePlanEnded= null;
    public int m_nActCount = 0;
    public int m_nActCountWithHistory=0;

    //
    // GetSaveResult
    public int m_nTotalSaveActCount = 0;
    public int m_nTotalSaveActWithHistoryCount=0;
    public double m_dAvgKwhForAllMeter=0.0;
    public double m_dAvgWonForAllMeter=0.0;





    public int m_nSeqMember=0;
    public int m_nSeqProjectSelected=0;
    public int m_nMemberType=0;
    public String m_strAdminId="";
    public String m_strPassword="";
    public String m_strMemberName="";
    public String m_strMemberPhone="";
    public String m_strMemberMail="";
    public String m_strMemberCompany="";
    public String m_strMemberRank="";



    public boolean m_bShowPush=true;

    public String m_strMmsTarget="01094569304";

    public int m_nCountNoticeTop=0;
    public int m_nCountNoticeTotal=0;

    public Date m_dtNoticeCreatedMaxForNextRequest=null;

    public Date m_dtPicSendCreatedMaxForNextRequest=null;

    public String m_strPushId="";

    public String m_strMemberNameSubscribing="";
    public String m_strPhoneSubscribing="";


    public int nReserveType = 0;
    public boolean bCarRegistered = false;

    /*
    public int m_nAuthType=CaEngine.AUTH_TYPE_UNKNOWN;

    public CaProject m_caProject=new CaProject();

    public ArrayList<CaNotice> m_alNotice=new ArrayList<>();
    public ArrayList<CaSafety> m_alSafety=new ArrayList<>();
    public ArrayList<CaProject> m_alProject=new ArrayList<>();
    public ArrayList<CaMms> m_alMms=new ArrayList<>();
    public ArrayList<CaReport> m_alReport=new ArrayList<>();
    public ArrayList<CaSurvey> m_alSurvey=new ArrayList<>();
    public ArrayList<CaJobCode> m_alJobCode=new ArrayList<>();

     */

    public int m_nAuthType = CaEngine.AUTH_TYPE_UNKNOWN;

   /*
    public String getNoticeReadListString() {
        String strResult="";

        for (CaNotice notice : m_alNotice) {
            if (notice.m_bReadStateChanged) {
                strResult = (strResult + Integer.toString(notice.m_nSeqNotice)+",");
            }
        }

        if (strResult.isEmpty()) return strResult;

        strResult = strResult.substring(0, strResult.length() - 1);
        return strResult;
    }
*/


    public Date parseDate(String str) {
        Calendar cal=new GregorianCalendar(1970, 1, 1, 0, 0, 0);

        Date dt;

        try {
            dt = CaApplication.m_Info.m_dfStd.parse(str);
        }
        catch (ParseException e) {
            // e.printStackTrace();
            dt=cal.getTime();
        }

        return dt;
    }



    public static String getDecoPhoneNumber(String src) {
        if (src == null) {
            return "";
        }
        if (src.length() == 8) {
            return src.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
        } else if (src.length() == 12) {
            return src.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
        }
        return src.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
    }
}
