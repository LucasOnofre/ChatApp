package lucas.br.whatsapp.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import lucas.br.whatsapp.R;
import lucas.br.whatsapp.adapter.TabAdapter;
import lucas.br.whatsapp.config.ConfiguracaoFirebase;
import lucas.br.whatsapp.helper.SlidingTabLayout;
import lucas.br.whatsapp.model.Usuario;

public class MainActivity extends AppCompatActivity {

    private Toolbar          toolbar;
    private ViewPager        viewPager;
    private FirebaseAuth     usuarioAutenticacao;
    private SlidingTabLayout slidingTabLayout;

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
        slidingTabLayout.setDistributeEvenly(true);

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());

        viewPager       .setAdapter(tabAdapter);
        slidingTabLayout.setViewPager(viewPager);

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

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deslogarUsuario(){

        usuarioAutenticacao.signOut();
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
