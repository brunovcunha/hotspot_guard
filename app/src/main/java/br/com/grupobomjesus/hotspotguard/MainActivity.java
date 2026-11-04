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
        TextView title = new TextView(this); title.setText("Hotspot Guard"); title.setTextSize(26); title.setPadding(0,0,0,20); root.addView(title);
        TextView info = new TextView(this); info.setText("Proteção por PIN para a abertura do aplicativo Configurações.\n\nA proteção depende do serviço de Acessibilidade estar ativado."); info.setTextSize(16); root.addView(info);
        Button pin = new Button(this); pin.setText(PinStore.isConfigured(this) ? "Alterar PIN" : "Criar PIN do administrador"); pin.setOnClickListener(v -> { changingPin = PinStore.isConfigured(this); promptPin(); }); root.addView(pin);
        Button accessibility = new Button(this); accessibility.setText("Ativar serviço de proteção"); accessibility.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))); root.addView(accessibility);
        Button status = new Button(this); status.setText("Verificar configuração"); status.setOnClickListener(v -> Toast.makeText(this, PinStore.isConfigured(this) ? "PIN configurado. Confira se o serviço está ativado." : "Crie um PIN primeiro.", Toast.LENGTH_LONG).show()); root.addView(status);
        setContentView(root);
    }

    private void promptPin() {
        final android.widget.EditText current = new android.widget.EditText(this);
        current.setHint(changingPin ? "PIN atual" : "Crie um PIN numérico (6 dígitos ou mais)"); current.setInputType( android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        LinearLayout box = new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.addView(current);
        final android.widget.EditText next = new android.widget.EditText(this); next.setHint(changingPin ? "Novo PIN (6 dígitos ou mais)" : "Confirme o PIN"); next.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD); box.addView(next);
        new android.app.AlertDialog.Builder(this).setTitle(changingPin ? "Alterar PIN" : "Configurar administrador").setView(box)
            .setNegativeButton("Cancelar", null).setPositiveButton("Salvar", (d,w) -> {
                String a = current.getText().toString(), b = next.getText().toString();
                if (changingPin && !PinStore.verify(this, a)) { Toast.makeText(this,"PIN atual incorreto.",Toast.LENGTH_LONG).show(); return; }
                if (b.length() < 6) { Toast.makeText(this,"O PIN deve ter pelo menos 6 dígitos.",Toast.LENGTH_LONG).show(); return; }
                if (!changingPin && !a.equals(b)) { Toast.makeText(this,"Os PINs não conferem.",Toast.LENGTH_LONG).show(); return; }
                PinStore.setPin(this, b); Toast.makeText(this,"PIN salvo.",Toast.LENGTH_SHORT).show();
            }).show();
    }
}
