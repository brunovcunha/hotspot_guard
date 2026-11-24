package br.com.grupobomjesus.hotspotguard;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class LockActivity extends Activity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        LinearLayout root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setGravity(Gravity.CENTER); root.setPadding(36,36,36,36);
        TextView title = new TextView(this); title.setText(R.string.lock_title); title.setTextSize(24); root.addView(title);
        TextView msg = new TextView(this); msg.setText(R.string.lock_message); msg.setTextSize(16); msg.setPadding(0,16,0,16); root.addView(msg);
        EditText pin = new EditText(this); pin.setHint(R.string.lock_pin_hint); pin.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD); root.addView(pin);
        Button enter = new Button(this); enter.setText(R.string.btn_unlock_one_minute); root.addView(enter);
        enter.setOnClickListener(v -> {
            if (PinStore.verify(this, pin.getText().toString())) { PinStore.unlockForOneMinute(this); finish(); }
            else { pin.setText(""); Toast.makeText(this,R.string.msg_pin_incorrect,Toast.LENGTH_SHORT).show(); }
        });
        Button back = new Button(this); back.setText(R.string.btn_back); back.setOnClickListener(v -> finish()); root.addView(back);
        setContentView(root);
    }
    @Override public void onBackPressed() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(home);
        finish();
    }
}