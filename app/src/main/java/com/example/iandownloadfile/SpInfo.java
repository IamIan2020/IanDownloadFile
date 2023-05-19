package com.example.iandownloadfile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.File;

/**
 * Created by Kim on 2022/8/17.
 */

public class SpInfo {
    private String SP_KEY = "secret_shared_prefs";
    private SharedPreferences sp;

    public SpInfo(Context context) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            sp = EncryptedSharedPreferences.create(
                    SP_KEY,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public boolean deleteSharedPreferences(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.deleteSharedPreferences(SP_KEY);
        } else {
            context.getSharedPreferences(SP_KEY, context.MODE_PRIVATE).edit().clear().apply();
            File dir = new File(context.getApplicationInfo().dataDir, "shared_prefs");
            return new File(dir, SP_KEY + ".xml").delete();
        }
    }

    public void putSP(String key, String source) {
        sp.edit().putString(key,source).apply();
    }

    public void putSP(String key, int source) {
        sp.edit().putInt(key,source).apply();
    }

    public void putSP(String key, boolean source) {
        sp.edit().putBoolean(key,source).apply();
    }

    public void putSP(String key, long source) {
        sp.edit().putLong(key,source).apply();
    }

    public boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }

    public int getInt(String key) {
        return sp.getInt(key, 0);
    }

    public long getLong(String key) {
        return sp.getLong(key, 0);
    }

    public String getString(String key) {
        return sp.getString(key, "");
    }
}
