package lucas.br.whatsapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import lucas.br.whatsapp.R;
import lucas.br.whatsapp.config.ConfiguracaoFirebase;
import lucas.br.whatsapp.helper.Base64Custom;
import lucas.br.whatsapp.helper.Preferencias;
import lucas.br.whatsapp.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private Usuario      usuario;
    private EditText     campoNome;
    private EditText     campoEmail;
    private EditText     campoSenha;
    private Button       btnCadastrao;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        btnCadastrao = findViewById(R.id.btnCadastrarId);
        campoNome    = findViewById(R.id.campoCadastroNomeId);
        campoSenha   = findViewById(R.id.campoCadastroSenhaId);
        campoEmail   = findViewById(R.id.campoCadastroEmailId);

         btnCadastrao.setOnClickListener(listener_cadastro);

    }

    public View.OnClickListener listener_cadastro = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            usuario = new Usuario();
            usuario.setNome  (campoNome.getText().toString());
            usuario.setEmail (campoEmail.getText().toString());
            usuario.setSenha (campoSenha.getText().toString());
            cadastrarUsuario();
        }
    };

    private void cadastrarUsuario(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if( task.isSuccessful() ){

                    Toast.makeText(CadastroUsuarioActivity.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_LONG ).show();



                    String identificadorUsuario = Base64Custom.codificarBase64( usuario.getEmail() );
                    usuario.setId( identificadorUsuario );
                    usuario.salvar();

                    //salva o email do usuário nas preferencias
                    Preferencias preferencias = new Preferencias(CadastroUsuarioActivity.this);
                    preferencias.salvarDados(identificadorUsuario,usuario.getNome());

                    abrirLoginUsuario();

                }else{

                    String erro = "";
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Escolha uma senha que contenha, letras e números.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "Email indicado não é válido.";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erro = "Já existe uma conta com esse e-mail.";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuarioActivity.this, "Erro ao cadastrar usuário: " + erro, Toast.LENGTH_LONG ).show();
                }

            }
        });

    }

    private void abrirLoginUsuario() {
        Intent intent = new Intent(CadastroUsuarioActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}