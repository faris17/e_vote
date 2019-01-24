package id.kunya.informatikavote.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by muhammad on 12/12/18.
 */

public class PreferencesHelper {
    private static final String SHARED_PREF_NAME = "Settings";
    private static final String TAG_LOGIN = "login";
    private static final String TAG_STATUS = "status";
    private static final String TAG_VOTED = "voted";

    private static PreferencesHelper mInstance;
    private static Context mCtx;

    private PreferencesHelper(Context context) {
        mCtx = context;
    }

    public static synchronized PreferencesHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PreferencesHelper(context);
        }
        return mInstance;
    }

    public boolean saveVoted(String voted){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_VOTED, voted);
        editor.apply();
        return true;
    }

    public String getVoted(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_VOTED, null);
    }

    public boolean saveVotingStatus(String status){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_STATUS, status);
        editor.apply();
        return true;
    }

    public String getVotingStatus(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_STATUS, null);
    }

    public boolean saveLoginID(String nim){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_LOGIN, nim);
        editor.apply();
        return true;
    }

    public String getLoginID(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_LOGIN, null);
    }



}
