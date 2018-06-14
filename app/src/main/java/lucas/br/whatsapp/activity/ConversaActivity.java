package lucas.br.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import lucas.br.whatsapp.R;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String nomeUsuarioDestinatario;
    private String emailUsuarioDestinatario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        toolbar =  findViewById(R.id.toolbar_conversa);

        Bundle extra = getIntent().getExtras();

        if (extra != null){
            nomeUsuarioDestinatario  = extra.getString("nome");
            emailUsuarioDestinatario = extra.getString("email");

        }

        toolbar.setTitle(nomeUsuarioDestinatario);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);
    }
}
