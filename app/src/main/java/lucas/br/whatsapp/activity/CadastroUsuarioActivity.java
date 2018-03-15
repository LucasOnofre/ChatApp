package lucas.br.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import lucas.br.whatsapp.R;
import lucas.br.whatsapp.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText campoNome;
    private EditText campoEmail;
    private EditText campoSenha;
    private Button   btnCadastrao;
    private Usuario  usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        btnCadastrao    = findViewById(R.id.btnCadastrarId);
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


    }
}
