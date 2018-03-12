package lucas.br.whatsapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;
import java.util.Random;

import lucas.br.whatsapp.Manifest;
import lucas.br.whatsapp.R;
import lucas.br.whatsapp.helper.Permissao;
import lucas.br.whatsapp.helper.Preferencias;

public class LoginActivity extends AppCompatActivity {

    private EditText nome;
    private EditText codArea;
    private EditText codPais;
    private EditText telefone;

    private Button cadastrar;

    private String[] permissoesNecessarias = new String[]{
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Permissao.validaPermissoes(1,this,permissoesNecessarias);

        nome        = findViewById(R.id.campoNomeId);
        codPais     = findViewById(R.id.campoNumPaisId);
        codArea     = findViewById(R.id.campoNumAreaId);
        telefone    = findViewById(R.id.campoNumeroTelId);
        cadastrar   = findViewById(R.id.btnCadastrar);


        //DEFINIÇÃO DOS FORMATERS QUE DELIMITAM OS CARACTERES
        SimpleMaskFormatter simpleMaskPais      = new SimpleMaskFormatter("+NN");
        SimpleMaskFormatter simpleMaskArea      = new SimpleMaskFormatter("NN");
        SimpleMaskFormatter simpleMaskTelefone  = new SimpleMaskFormatter("NNNNN-NNNN");

        //SETANDO OS FORMATERS EM SEUS WATCHERS
        MaskTextWatcher maskTextPais     = new MaskTextWatcher(codPais, simpleMaskPais);
        MaskTextWatcher maskTextArea     = new MaskTextWatcher(codArea, simpleMaskArea);
        MaskTextWatcher maskTextTelefone = new MaskTextWatcher(telefone, simpleMaskTelefone);


        //SETANDO OS LISTENERS EM SEUS WATCHERS
        codPais.addTextChangedListener(maskTextPais);
        codArea.addTextChangedListener(maskTextArea);
        telefone.addTextChangedListener(maskTextTelefone);

        //BOTÃO CADASTRAR
        cadastrar.setOnClickListener(listener_cadastrar);
    }


    private View.OnClickListener listener_cadastrar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String nomeUsuario = nome.getText().toString();
            String telefoneCompleto =
                    codPais.getText().toString() +
                            codArea.getText().toString() +
                            telefone.getText().toString();

            String telefoneSemSimbolos = telefoneCompleto.replace("+", "");
            telefoneSemSimbolos = telefoneSemSimbolos.replace("-", "");


            //GERAR TOKEN
            Random randomico = new Random();
            int numeroRandomico = randomico.nextInt(9999 - 1000) + 1000;
            String token = String.valueOf(numeroRandomico);
            String mensagemEnvio = " WhatsApp Código de confirmação " + token;


            //SALVAR DADOS PARA VALIDAÇÃO
            Preferencias preferencias = new Preferencias(getApplicationContext());
            preferencias.salvarUsuarioPreferencias(nomeUsuario, telefoneSemSimbolos, token);

//            HashMap<String,String> usuario = preferencias.getDadosUsuario();
//            Log.i("Token","t" + usuario.get("token"));

            //ENVIA SMS
            telefoneSemSimbolos = "5554";
            boolean enviadoSMS = enviaSMS("+" + telefoneSemSimbolos, mensagemEnvio);

            if (enviadoSMS){
                Intent intent = new Intent(LoginActivity.this,ValidadorActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(LoginActivity.this, "Problema ao enviar o SMS, tente novamente", Toast.LENGTH_SHORT).show();
            }

        }

        public boolean enviaSMS(String telefone, String mensagem) {

            try {

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(telefone, null, mensagem, null, null);
                return  true;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    };

    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[]grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        for (int resultado:grantResults){
            if (resultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao() {
        AlertDialog.Builder buider = new AlertDialog.Builder(this);
        buider.setTitle("Permissões negadas");
        buider.setMessage("Para utilizar esse Aplicativo, é necessário aceitas as permissões");
        buider.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog dialog = buider.create();
        dialog.show();
    }
}
