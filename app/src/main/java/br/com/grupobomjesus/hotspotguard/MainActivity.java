package br.com.grupobomjesus.hotspotguard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private boolean changingPin = false;
    @Override public void onCreate(Bundle state) { super.onCreate(state); showScreen(); }

    private void showScreen() {
        LinearLayout root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(36, 48, 36, 36); root.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView title = new TextView(this); title.setText(R.string.app_name); title.setTextSize(26); title.setPadding(0,0,0,20); root.addView(title);
        TextView info = new TextView(this); info.setText(R.string.main_info); info.setTextSize(16); root.addView(info);
        Button pin = new Button(this); pin.setText(PinStore.isConfigured(this) ? R.string.btn_change_pin : R.string.btn_create_admin_pin); pin.setOnClickListener(v -> { changingPin = PinStore.isConfigured(this); promptPin(); }); root.addView(pin);
        Button accessibility = new Button(this); accessibility.setText(R.string.btn_activate_service); accessibility.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))); root.addView(accessibility);
        Button status = new Button(this); status.setText(R.string.btn_check_config); status.setOnClickListener(v -> Toast.makeText(this, PinStore.isConfigured(this) ? R.string.msg_pin_configured : R.string.msg_create_pin_first, Toast.LENGTH_LONG).show()); root.addView(status);
        setContentView(root);
    }

    private void promptPin() {
        final android.widget.EditText current = new android.widget.EditText(this);
        current.setHint(changingPin ? R.string.hint_current_pin : R.string.hint_create_pin); current.setInputType( android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        LinearLayout box = new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.addView(current);
        final android.widget.EditText next = new android.widget.EditText(this); next.setHint(changingPin ? R.string.hint_new_pin : R.string.hint_confirm_pin); next.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD); box.addView(next);
        new android.app.AlertDialog.Builder(this).setTitle(changingPin ? R.string.dialog_title_change_pin : R.string.dialog_title_setup_admin).setView(box)
            .setNegativeButton(R.string.btn_cancel, null).setPositiveButton(R.string.btn_save, (d,w) -> {
                String a = current.getText().toString(), b = next.getText().toString();
                if (changingPin && !PinStore.verify(this, a)) { Toast.makeText(this,R.string.msg_current_pin_incorrect,Toast.LENGTH_LONG).show(); return; }
                if (b.length() < 6) { Toast.makeText(this,R.string.msg_pin_min_length,Toast.LENGTH_LONG).show(); return; }
                if (!changingPin && !a.equals(b)) { Toast.makeText(this,R.string.msg_pins_dont_match,Toast.LENGTH_LONG).show(); return; }
                PinStore.setPin(this, b); Toast.makeText(this,R.string.msg_pin_saved,Toast.LENGTH_SHORT).show();
            }).show();
    }
}
