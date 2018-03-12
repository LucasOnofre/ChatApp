package lucas.br.whatsapp.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;

import lucas.br.whatsapp.R;
import lucas.br.whatsapp.helper.Preferencias;

public class ValidadorActivity extends AppCompatActivity {

    private EditText campoCodigo;
    private Button   BtnValida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        BtnValida   = findViewById(R.id.btnValidaId);
        campoCodigo = findViewById(R.id.campoCodigoId);


        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher mascaraCodigoValidacao  = new MaskTextWatcher(campoCodigo,simpleMaskFormatter);

        campoCodigo.addTextChangedListener(mascaraCodigoValidacao);
        BtnValida.setOnClickListener(listenerValida);
    }

    private View.OnClickListener listenerValida = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //RECUPERA DADOS DO USUARIO VIA CLASSE PREFERENCIAS
            Preferencias Preferencias = new Preferencias(ValidadorActivity.this);
            HashMap <String,String> usuario = Preferencias.getDadosUsuario();

            String tokenGerado   = usuario.get("token");
            String tokenDigitado = campoCodigo.getText().toString();

            if (tokenDigitado.equals(tokenGerado)){

                Toast.makeText(ValidadorActivity.this, "Token Validade com sucesso", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ValidadorActivity.this, "Código de validação incorreto", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
