package lucas.br.whatsapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import lucas.br.whatsapp.R;
import lucas.br.whatsapp.config.ConfiguracaoFirebase;
import lucas.br.whatsapp.model.Usuario;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth autenticacao;

    private Button btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSair = findViewById(R.id.btn_sair);
        btnSair.setOnClickListener(listenre_sair);
    }

    private View.OnClickListener listenre_sair = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
            autenticacao.signOut();

            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    };
}
