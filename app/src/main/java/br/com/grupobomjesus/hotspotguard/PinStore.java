package br.com.grupobomjesus.hotspotguard;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

final class PinStore {
    private static final String PREF = "guard_prefs";
    private static final String SALT = "pin_salt";
    private static final String HASH = "pin_hash";
    private static final String UNLOCK = "unlocked_until";
    private static final int ITERATIONS = 120_000;
    private PinStore() {}

    static boolean isConfigured(Context c) { return prefs(c).contains(HASH); }
    static void setPin(Context c, String pin) {
        try {
            byte[] salt = new byte[16]; new SecureRandom().nextBytes(salt);
            byte[] digest = derive(salt, pin);
            prefs(c).edit().putString(SALT, Base64.encodeToString(salt, Base64.NO_WRAP))
                    .putString(HASH, Base64.encodeToString(digest, Base64.NO_WRAP)).apply();
        } catch (Exception e) { throw new IllegalStateException(e); }
    }
    static boolean verify(Context c, String pin) {
        try {
            SharedPreferences p = prefs(c);
            if (!p.contains(HASH)) return false;
            byte[] salt = Base64.decode(p.getString(SALT, ""), Base64.NO_WRAP);
            byte[] expected = Base64.decode(p.getString(HASH, ""), Base64.NO_WRAP);
            return MessageDigest.isEqual(expected, derive(salt, pin));
        } catch (Exception e) { return false; }
    }
    static void unlockForOneMinute(Context c) { prefs(c).edit().putLong(UNLOCK, System.currentTimeMillis() + 60_000L).apply(); }
    static boolean isTemporarilyUnlocked(Context c) { return System.currentTimeMillis() < prefs(c).getLong(UNLOCK, 0L); }
    private static byte[] derive(byte[] salt, String pin) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(pin.toCharArray(), salt, ITERATIONS, 256);
        try { return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded(); }
        finally { spec.clearPassword(); }
    }
    private static SharedPreferences prefs(Context c) { return c.getSharedPreferences(PREF, Context.MODE_PRIVATE); }
}
