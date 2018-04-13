package lucas.br.whatsapp.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.Exclude;

import lucas.br.whatsapp.R;
import lucas.br.whatsapp.config.ConfiguracaoFirebase;
import lucas.br.whatsapp.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText campoNome;
    private EditText campoEmail;
    private EditText campoSenha;
    private Button   btnCadastrao;
    private FirebaseAuth autenticacao;
    private Usuario  usuario;

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
            usuario.setNome (campoNome.getText().toString());
            usuario.setEmail(campoEmail.getText().toString());
            usuario.setSenha (campoSenha.getText().toString());
            cadadastrarUsuario();
        }
    };

    private void cadadastrarUsuario() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(),usuario.getSenha())
                .addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CadastroUsuarioActivity.this, "Usuário cadastrado com sucesso",
                                    Toast.LENGTH_SHORT).show();

                            FirebaseUser usuarioFirebase = task.getResult().getUser();
                            usuario.setId(usuarioFirebase.getUid());
                            usuario.salvar();

                            autenticacao.signOut();
                            finish();

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
                            Toast.makeText(CadastroUsuarioActivity.this, "Erro " + erroExcessao, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }    }