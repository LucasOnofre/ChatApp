package lucas.br.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import lucas.br.whatsapp.R;
import lucas.br.whatsapp.model.Contato;

public class ContatosAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Contato> items;

    public ContatosAdapter(Context context, ArrayList<Contato> items ){

        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Contato getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (items != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            Contato contato = items.get(position);
            view = inflater.inflate(R.layout.lista_contato,parent,false);

            TextView nomeContato = view.findViewById(R.id.contato_nome);
            nomeContato.setText(contato.getNome());

            TextView emailContato = view.findViewById(R.id.contato_email);
            emailContato.setText(contato.getEmail());
        }

        return view;
    }
}
