package com.ipanel.join.jilin.setting.provider;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ipanel.jilinsetting.R;
import com.ipanel.join.jilin.setting.service.SettingConfigsTool;
import com.ipanel.join.jilin.setting.tools.SettingsData;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;


public class SettingProvider extends ContentProvider {

    public static final String AUTHORITY = "ipaneltv.settings.data.provider";
    public static final String ACTION_SETTINGS_CHANGED_SUCCESSED = "com.ipanel.action.SETTINGS_CHANGED_SUCCESSED";
    private static final int AudioVedio = 12;
    private static final int Menulist = 11;

    private static final UriMatcher mUriMatcher;
    private String TAG = "SettingProvider";

    private SettingConfigsTool scfTool;
    private Context mContext;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, "Menulist", Menulist);
        mUriMatcher.addURI(AUTHORITY, "AudioVedio", AudioVedio);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public Uri insert(Uri uri, ContentValues initValues) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        scfTool = SettingConfigsTool.getInstance(mContext);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query--selection=="+selection);
        switch (mUriMatcher.match(uri)) {
            case Menulist:
                String[] menuid_list = {"AudioVedio","Ui","SoftwareUpdate","SystemInfo","ResetDefault",
                        "NetworkSet","WifiSet","PonInfo","NetworkInfo","NetworkTest","ScreenpbSwitch","AutoStandbySet"};
                int[] menuname_list = {R.string.audioandvedio, R.string.uiset, R.string.softwareupdate, R.string.systeminfo,
                        R.string.resetdefault, R.string.networkset, R.string.wifiset, R.string.poninfo, R.string.networkinfo,
                        R.string.networktest, R.string.screenpb_switch, R.string.timer_setting};
                String[] colums = new String[]{"json_data"};
                MatrixCursor cur = new MatrixCursor(colums);
                try {
                    JSONArray jsonArray = new JSONArray();
                    for(int i=0; i< menuid_list.length;i++){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", menuid_list[i]);
                        jsonObject.put("name", mContext.getString(menuname_list[i]));
                        jsonArray.put(jsonObject);
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("menulist", jsonArray);
                    Log.d(TAG, " jsonObject.toString() ="+ jsonObject.toString() );
                    cur.addRow(new String[] { jsonObject.toString() });
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return cur;
            case AudioVedio:
                //AudioVedio 请求音视频设置界面数据
                if(selection == null){
                //get
//				type : 0(开关选项，例如开启，关闭)
//				1（选项切换，例如全屏，4:3, 16:9），
//				2（数值切换例如0-100）
//				3（文本输入，例如输入密码等），
//				4（点击启动下一层）
//				5（只是信息展示）
                    String[] audioVedioColums = new String[]{"json_data"};
                    MatrixCursor audioVedioCur = new MatrixCursor(audioVedioColums);
                    try {
                        JSONArray audioVedioJsonArray = new JSONArray();
                        //第一个JSONObject	"id":"vedio_type"
                        JSONObject audioVedioObject1 = new JSONObject();
                        audioVedioObject1.put("id", "vedio_type");
                        audioVedioObject1.put("name", mContext.getString(R.string.vediotype));
                        audioVedioObject1.put("type", "1");
                        audioVedioObject1.put("current_value", scfTool.getParam(SettingsData.vedio_type));
                        //id=vedio_type的valuelist
                        JSONArray vedioTypeValuelist = new JSONArray();
                        JSONObject vedioTypeValue0 = new JSONObject();
                        vedioTypeValue0.put("value_id", "0");
                        vedioTypeValue0.put("value_name", "全屏");
                        vedioTypeValuelist.put(vedioTypeValue0);

                        JSONObject vedioTypeValue1 = new JSONObject();
                        vedioTypeValue1.put("value_id", "1");
                        vedioTypeValue1.put("value_name", "自动");
                        vedioTypeValuelist.put(vedioTypeValue1);

                        JSONObject vedioTypeValue2 = new JSONObject();
                        vedioTypeValue2.put("value_id", "2");
                        vedioTypeValue2.put("value_name", "4:3");
                        vedioTypeValuelist.put(vedioTypeValue2);

                        JSONObject vedioTypeValue3 = new JSONObject();
                        vedioTypeValue3.put("value_id", "3");
                        vedioTypeValue3.put("value_name", "16:9");
                        vedioTypeValuelist.put(vedioTypeValue3);
                        audioVedioObject1.put("valuelist", vedioTypeValuelist);

                        audioVedioJsonArray.put(audioVedioObject1);

                        //第二个JSONObject	"id":"vedio_solution"
                        JSONObject audioVedioObject2 = new JSONObject();
                        audioVedioObject2.put("id", "vedio_solution");
                        audioVedioObject2.put("name", mContext.getString(R.string.vediosolution));
                        audioVedioObject2.put("type", "1");
                        audioVedioObject2.put("current_value", "0");
                        //id=vedio_solution的valuelist
                        JSONArray vedioSolutionValuelist = new JSONArray();
                        JSONObject vedioSolutionValue0 = new JSONObject();
                        vedioSolutionValue0.put("value_id", "0");
                        vedioSolutionValue0.put("value_name", "1080i");
                        vedioSolutionValuelist.put(vedioSolutionValue0);

                        JSONObject vedioSolutionValue1 = new JSONObject();
                        vedioSolutionValue1.put("value_id", "1");
                        vedioSolutionValue1.put("value_name", "1080P");
                        vedioSolutionValuelist.put(vedioSolutionValue1);

                        JSONObject vedioSolutionValue2 = new JSONObject();
                        vedioSolutionValue2.put("value_id", "2");
                        vedioSolutionValue2.put("value_name", "1024");
                        vedioSolutionValuelist.put(vedioSolutionValue2);

                        JSONObject vedioSolutionValue3 = new JSONObject();
                        vedioSolutionValue3.put("value_id", "3");
                        vedioSolutionValue3.put("value_name", "720P");
                        vedioSolutionValuelist.put(vedioSolutionValue3);

                        JSONObject vedioSolutionValue4 = new JSONObject();
                        vedioSolutionValue4.put("value_id", "4");
                        vedioSolutionValue4.put("value_name", "4k");
                        vedioSolutionValuelist.put(vedioSolutionValue4);

                        JSONObject vedioSolutionValue5 = new JSONObject();
                        vedioSolutionValue5.put("value_id", "5");
                        vedioSolutionValue5.put("value_name", "自动");
                        vedioSolutionValuelist.put(vedioSolutionValue5);
                        audioVedioObject2.put("valuelist", vedioSolutionValuelist);

                        audioVedioJsonArray.put(audioVedioObject2);

                        //第三个JSONObject	"id":"soundchannel_default"
                        JSONObject audioVedioObject3 = new JSONObject();
                        audioVedioObject3.put("id", "soundchannel_default");
                        audioVedioObject3.put("name", mContext.getString(R.string.soundchannel_default));
                        audioVedioObject3.put("type", "1");
                        audioVedioObject3.put("current_value", "0");
                        //id=soundchannel_default的valuelist
                        JSONArray soundchannelDefaultValuelist = new JSONArray();
                        JSONObject soundchannelDefaultValue0 = new JSONObject();
                        soundchannelDefaultValue0.put("value_id", "0");
                        soundchannelDefaultValue0.put("value_name", "立体声");
                        soundchannelDefaultValuelist.put(soundchannelDefaultValue0);

                        JSONObject soundchannelDefaultValue1 = new JSONObject();
                        soundchannelDefaultValue1.put("value_id", "1");
                        soundchannelDefaultValue1.put("value_name", "左声道");
                        soundchannelDefaultValuelist.put(soundchannelDefaultValue1);

                        JSONObject soundchannelDefaultValue2 = new JSONObject();
                        soundchannelDefaultValue2.put("value_id", "2");
                        soundchannelDefaultValue2.put("value_name", "右声道");
                        soundchannelDefaultValuelist.put(soundchannelDefaultValue2);

                        audioVedioObject3.put("valuelist", soundchannelDefaultValuelist);

                        audioVedioJsonArray.put(audioVedioObject3);

                        //第四个JSONObject	"id":"image_bright"
                        JSONObject audioVedioObject4 = new JSONObject();
                        audioVedioObject4.put("id", "image_bright");
                        audioVedioObject4.put("name", mContext.getString(R.string.imgbright));
                        audioVedioObject4.put("type", "2");
                        audioVedioObject4.put("current_value", "50");
                        audioVedioObject4.put("maxvalue", "100");
                        audioVedioObject4.put("minvalue", "10");
                        audioVedioJsonArray.put(audioVedioObject4);

                        //第五个JSONObject	"id":"image_compare"
                        JSONObject audioVedioObject5 = new JSONObject();
                        audioVedioObject5.put("id", "image_compare");
                        audioVedioObject5.put("name", mContext.getString(R.string.imgcompare));
                        audioVedioObject5.put("type", "2");
                        audioVedioObject5.put("current_value", "50");
                        audioVedioObject5.put("maxvalue", "100");
                        audioVedioObject5.put("minvalue", "10");
                        audioVedioJsonArray.put(audioVedioObject5);

                        //第六个JSONObject	"id":"image_saturation"
                        JSONObject audioVedioObject6 = new JSONObject();
                        audioVedioObject6.put("id", "image_saturation");
                        audioVedioObject6.put("name", mContext.getString(R.string.imgsaturation));
                        audioVedioObject6.put("type", "2");
                        audioVedioObject6.put("current_value", "50");
                        audioVedioObject6.put("maxvalue", "100");
                        audioVedioObject6.put("minvalue", "10");
                        audioVedioJsonArray.put(audioVedioObject6);

                        //第七个JSONObject	"id":"soundoutput_mode"
                        JSONObject audioVedioObject7 = new JSONObject();
                        audioVedioObject7.put("id", "soundoutput_mode");
                        audioVedioObject7.put("name", mContext.getString(R.string.soundoutput_mode));
                        audioVedioObject7.put("type", "1");
                        audioVedioObject7.put("current_value", "0");

                        //id=soundoutput_mode的valuelist
                        JSONArray soundoutputModeValuelist = new JSONArray();
                        JSONObject soundoutputModeValue0 = new JSONObject();
                        soundoutputModeValue0.put("value_id", "0");
                        soundoutputModeValue0.put("value_name", "Dolby D");
                        soundoutputModeValuelist.put(soundoutputModeValue0);

                        JSONObject soundoutputModeValue1 = new JSONObject();
                        soundoutputModeValue1.put("value_id", "1");
                        soundoutputModeValue1.put("value_name", "PCM");
                        soundoutputModeValuelist.put(soundoutputModeValue1);
                        audioVedioObject7.put("valuelist", soundoutputModeValuelist);

                        audioVedioJsonArray.put(audioVedioObject7);


                        //第八个JSONObject	"id":"adjustscreen"
                        JSONObject audioVedioObject8 = new JSONObject();
                        audioVedioObject8.put("id", "adjustscreen");
                        audioVedioObject8.put("name", mContext.getString(R.string.adjustscreen));
                        audioVedioObject8.put("type", "4");
                        audioVedioObject8.put("packagename", "com.ipanel.jilinsetting");
                        audioVedioObject8.put("classname", "com.ipanel.join.jilin.setting.activity.AdjustScreenActivity");
                        audioVedioJsonArray.put(audioVedioObject8);

                        //返回"menulist"
                        JSONObject audioVedioJsonObject = new JSONObject();
                        audioVedioJsonObject.put("menulist", audioVedioJsonArray);
                        Log.d(TAG, "audioVedioJsonObject.toString() ="+ audioVedioJsonObject.toString() );
                        audioVedioCur.addRow(new String[] { audioVedioJsonObject.toString() });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return audioVedioCur;
                }
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        Log.d(TAG, "update uri = " + uri + " values = " + values + " where  = " + where + " whereArgs = " + Arrays.toString(whereArgs));
        switch (mUriMatcher.match(uri)) {
            case AudioVedio:
                Log.d(TAG, "update AudioVedio");
//			String operation = values.getAsString("operation");
                String id = values.getAsString("id");
                String value = values.getAsString("value");
                Log.d(TAG, "update id = " + id + " value = " + value);
//			if(TextUtils.isEmpty(id)  || TextUtils.isEmpty(value)) {
//				//返回0代表设置失败
//				return 0;
//			}
                if((whereArgs != null && whereArgs[0].equals("vedio_type")) || "vedio_type".equals(id)){
                    scfTool.setParam(SettingsData.vedio_type, value);
                    Intent intent = new Intent();
                    intent.putExtra("info", mContext.getString(R.string.settings_change_successed));
                    intent.setAction(ACTION_SETTINGS_CHANGED_SUCCESSED);
                    mContext.sendBroadcast(intent);
                }
                break;
            default:
                break;
        }
        //返回1代表设置成功
        return 1;
    }

}
