package lucas.br.whatsapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import lucas.br.whatsapp.R;
import lucas.br.whatsapp.adapter.TabAdapter;
import lucas.br.whatsapp.config.ConfiguracaoFirebase;
import lucas.br.whatsapp.helper.Base64Custom;
import lucas.br.whatsapp.helper.Preferencias;
import lucas.br.whatsapp.helper.SlidingTabLayout;
import lucas.br.whatsapp.model.Contato;
import lucas.br.whatsapp.model.Usuario;

public class MainActivity extends AppCompatActivity {

    private Toolbar          toolbar;
    private ViewPager        viewPager;
    private SlidingTabLayout slidingTabLayout;
    private DatabaseReference referenciaDatabase;
    private FirebaseAuth     usuarioAutenticacao;
    private String           identificadorContato;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar);

        viewPager        = findViewById(R.id.vp_pagina);
        slidingTabLayout = findViewById(R.id.stl_tabs);

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());

        viewPager       .setAdapter(tabAdapter);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);

        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this,R.color.colorAccent));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId){
            case R.id.menu_sair:
                deslogarUsuario();
                return true;

            case R.id.menu_settings:
                return true;

            case R.id.menu_pesquisar:
                return true;

            case R.id.menu_adicionar:
                abrirCadastroContato();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void abrirCadastroContato() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Novo contato");
        alertDialog.setMessage("E-mail do usuário");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(MainActivity.this);
        alertDialog.setView(editText);

        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String emailContato = editText.getText().toString();


                //valida se foi escrito algo
                if (emailContato.isEmpty()){
                    Toast.makeText(MainActivity.this, "Preecha o e-mail corretamente", Toast.LENGTH_SHORT).show();

                    //verifica se o Email ja está cadastrado no firebase
                }else{
                    identificadorContato = Base64Custom.codificarBase64(emailContato);

                    //recuperar instancia firebase
                    referenciaDatabase = ConfiguracaoFirebase.getFirebase();
                    referenciaDatabase = referenciaDatabase.child("Users").child(identificadorContato);

                    referenciaDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getValue() != null){
                                Usuario usuarioContato = dataSnapshot.getValue(Usuario.class);

                                Preferencias preferencias         = new Preferencias(MainActivity.this);
                                String identificadorUsuarioLogado = preferencias.getIdentificador();

                                referenciaDatabase = ConfiguracaoFirebase.getFirebase();
                                referenciaDatabase = referenciaDatabase.child("Contatos")
                                                                       .child(identificadorUsuarioLogado)
                                                                       .child(identificadorContato);

                                Contato contato = new Contato();
                                contato.setIdentificadorUsuario(identificadorContato);
                                contato.setEmail(usuarioContato.getEmail());
                                contato.setNome(usuarioContato.getNome());
                                referenciaDatabase.setValue(contato);

                            }else{
                                Toast.makeText(MainActivity.this, "Usuário não possui cadastro", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });

        alertDialog.create().show();
    }

    private void deslogarUsuario(){

        usuarioAutenticacao.signOut();
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
