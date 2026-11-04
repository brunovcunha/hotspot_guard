# Hotspot Guard

Projeto Android nativo em Java, sem bibliotecas externas. O app solicita PIN ao detectar a abertura do pacote `com.android.settings`, usando um AccessibilityService habilitado manualmente pelo usuário.

## Abrir e compilar
1. Instale o Android Studio (versão compatível com Android Gradle Plugin 8.7.3) e o JDK 17 ou superior.
2. Abra esta pasta no Android Studio.
3. Aguarde a sincronização do Gradle e instale as plataformas Android SDK 35 e Build Tools solicitadas.
4. Use **Build > Build APK(s)**. O APK de depuração será gerado em `app/build/outputs/apk/debug/app-debug.apk`.

## Configuração no tablet
1. Instale o APK.
2. Abra **Hotspot Guard** e crie o PIN do administrador (mínimo de 6 dígitos).
3. Toque em **Ativar serviço de proteção**.
4. Na tela de Acessibilidade, abra o serviço **Hotspot Guard - proteção das Configurações** e habilite-o.
5. Teste abrindo Configurações. O app deve solicitar o PIN.

## Limitações importantes
- É uma barreira de acesso às Configurações, não uma política Android de bloqueio de tethering.
- Não bloqueia de forma garantida o botão de hotspot nos Ajustes rápidos, menus do System UI, comandos ADB ou caminhos alternativos.
- A janela de liberação após PIN correto é de 1 minuto para permitir o uso administrativo das Configurações.
- O usuário pode desativar o serviço de Acessibilidade se conseguir acessar as configurações do sistema. Para bloqueio robusto é necessário Device Owner/MDM e política `DISALLOW_WIFI_TETHERING`.
- O serviço não lê o conteúdo da tela nem captura dados; apenas observa eventos de mudança de janela e o nome do pacote.
- O app não modifica pacotes Samsung/Knox nem exige root.

Este projeto é um ponto de partida funcional; teste em um tablet de homologação antes de distribuir para outros dispositivos.
