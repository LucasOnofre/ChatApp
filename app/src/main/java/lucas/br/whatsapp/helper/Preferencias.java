package lucas.br.whatsapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Lucas on 06/03/2018.
 */

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private int MODE = 0;
    private final String NOME_ARQUIVO = "whatsapp.preferencias";

    private SharedPreferences.Editor editor;

    private String CHAVE_IDENTIFICADOR       = "identificadorUsuarioLogado";

    public Preferencias(Context contextoParametro){

        contexto    = contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO,MODE);
        editor      = preferences.edit();

    }

    public void salvarDados (String identificadorUsuario){


        editor.putString(CHAVE_IDENTIFICADOR,identificadorUsuario);
        editor.commit();
    }

    public String getIdentificador(){
        return preferences.getString(CHAVE_IDENTIFICADOR,null);
    }


}
