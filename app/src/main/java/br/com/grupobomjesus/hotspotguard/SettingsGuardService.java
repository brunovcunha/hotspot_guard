package br.com.grupobomjesus.hotspotguard;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

public class SettingsGuardService extends AccessibilityService {
    private long lastLaunch = 0L;
    @Override public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event == null || !PinStore.isConfigured(this) || PinStore.isTemporarilyUnlocked(this)) return;
        CharSequence pkg = event.getPackageName();
        if (pkg == null || !"com.android.settings".contentEquals(pkg)) return;
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && event.getEventType() != AccessibilityEvent.TYPE_WINDOWS_CHANGED) return;
        long now = System.currentTimeMillis();
        if (now - lastLaunch < 1500) return;
        lastLaunch = now;
        Intent lock = new Intent(this, LockActivity.class);
        lock.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(lock);
    }
    @Override public void onInterrupt() { }
}
