package lucas.br.whatsapp.activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

import lucas.br.whatsapp.R;
import lucas.br.whatsapp.config.ConfiguracaoFirebase;
import lucas.br.whatsapp.model.Usuario;


public class LoginActivity extends AppCompatActivity {

    private Usuario      usuario;
    private Button       btnLogar;
    private EditText     emailLogin;
    private EditText     senhaLogin;
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();

       btnLogar     = findViewById(R.id.btnLogarId);
       senhaLogin   = findViewById(R.id.campoSenhaId);
       emailLogin   = findViewById(R.id.campoEmailId);

       btnLogar.setOnClickListener(listener_btn_logar);

    }

    private View.OnClickListener listener_btn_logar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            usuario = new Usuario();
            usuario.setEmail( emailLogin.getText().toString());
            usuario.setSenha( senhaLogin.getText().toString());
            validarLogin();

        }
    };

    private void verificarUsuarioLogado(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null) {
            abreTelaPrincipal();
        }
    }

    private void validarLogin(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(),usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    abreTelaPrincipal();
                    Toast.makeText(LoginActivity.this, "Sucesso", Toast.LENGTH_SHORT).show();

                } else {
                    String erroExcessao = "";

                    try {
                        throw task.getException();

                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcessao = "Digite uma senha mais forte, contento mais caracteres e com letras e números";

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcessao = "Email digitado é inválido";

                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcessao = "Esse Email já está em uso no App";

                    } catch (Exception e) {
                        erroExcessao = "Erro ao efetuar o cadastro";
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, "Erro" + erroExcessao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void abreTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void listener_cadastroUsuario(View view){
        Intent intent = new Intent(LoginActivity.this,CadastroUsuarioActivity.class);
        startActivity(intent);
    }
}
