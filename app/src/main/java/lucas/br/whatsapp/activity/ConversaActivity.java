package lucas.br.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import lucas.br.whatsapp.R;
import lucas.br.whatsapp.adapter.MensagensAdapter;
import lucas.br.whatsapp.config.ConfiguracaoFirebase;
import lucas.br.whatsapp.helper.Base64Custom;
import lucas.br.whatsapp.helper.Preferencias;
import lucas.br.whatsapp.model.Mensagem;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String   nomeUsuarioDestinatario;
    private String   emailUsuarioDestinatario;
    private EditText espaco_mensagem;
    private ImageView btn_enviar;

    private DatabaseReference firebase;

    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;

    private ListView lista_mensagens;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter <Mensagem> adapter;
    private ValueEventListener valueEventListenerMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);


        toolbar          = findViewById(R.id.toolbar_conversa);
        btn_enviar       = findViewById(R.id.bt_enviar);
        espaco_mensagem  = findViewById(R.id.edit_mensagem);
        lista_mensagens  = findViewById(R.id.lv_conversas);

        btn_enviar.setOnClickListener(listenre_bt_enviar);

        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUsuarioRemetente = preferencias.getIdentificador();


        Bundle extra = getIntent().getExtras();

        if (extra != null){
            nomeUsuarioDestinatario  = extra.getString("nome");
            emailUsuarioDestinatario = extra.getString("email");

            idUsuarioDestinatario = Base64Custom.codificarBase64(emailUsuarioDestinatario);

        }

        toolbar.setTitle(nomeUsuarioDestinatario);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);


        mensagens = new ArrayList<>();
        adapter = new MensagensAdapter(ConversaActivity.this,mensagens);
        lista_mensagens.setAdapter(adapter);


        firebase = ConfiguracaoFirebase.getFirebase()
                .child("Mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);

        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mensagens.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Mensagem mensagem = dados.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        };

        firebase.addValueEventListener(valueEventListenerMensagem);
    }
    private View.OnClickListener listenre_bt_enviar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String textoMensagem = espaco_mensagem.getText().toString();

            if (textoMensagem.isEmpty()){
                Toast.makeText(ConversaActivity.this, "Digite uma mensagem para enviar", Toast.LENGTH_SHORT).show();
            }else{
                Mensagem mensagem = new Mensagem();
                mensagem.setIdUsuario(idUsuarioRemetente);
                mensagem.setMensage  (textoMensagem);

                salvarMensagem(idUsuarioRemetente,idUsuarioDestinatario, mensagem);
                salvarMensagem(idUsuarioDestinatario,idUsuarioRemetente, mensagem);
                espaco_mensagem.setText("");


            }
        }
    };

    private boolean salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem){
        try{

            firebase = ConfiguracaoFirebase.getFirebase().child("Mensagens");
            firebase.child(idRemetente)
                    .child(idDestinatario).push().setValue(mensagem);

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerMensagem);
    }
}
