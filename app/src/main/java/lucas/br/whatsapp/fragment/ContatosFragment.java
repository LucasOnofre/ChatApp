package lucas.br.whatsapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import lucas.br.whatsapp.R;
import lucas.br.whatsapp.activity.ConversaActivity;
import lucas.br.whatsapp.activity.MainActivity;
import lucas.br.whatsapp.adapter.ContatosAdapter;
import lucas.br.whatsapp.config.ConfiguracaoFirebase;
import lucas.br.whatsapp.helper.Preferencias;
import lucas.br.whatsapp.model.Contato;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private ListView listView;
    private ContatosAdapter adapter;
    private ArrayList<Contato> contatos;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerContatos;

    public ContatosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        firebase.addValueEventListener(valueEventListenerContatos);
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerContatos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
               View view = inflater.inflate(R.layout.fragment_contatos, container, false);

               Preferencias preferencias = new Preferencias(getActivity());
               String identificadorUserLoged = preferencias.getIdentificador();

               firebase = ConfiguracaoFirebase.getFirebase().child("Contatos").child(identificadorUserLoged);

               valueEventListenerContatos = new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {

                       contatos.clear();

                       for (DataSnapshot dados : dataSnapshot.getChildren()){

                           Contato contato = dados.getValue(Contato.class);
                           contatos.add(contato);
                       }

                       adapter.notifyDataSetChanged();
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               };

               contatos  = new ArrayList<>();
               listView  = view.findViewById(R.id.contatos_list_id);
               adapter   = new ContatosAdapter(getActivity(),contatos);
               listView.setAdapter(adapter);

                listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(getActivity(), ConversaActivity.class);

                        Contato contato = contatos.get(position);

                        intent.putExtra("nome" ,contato.getNome());
                        intent.putExtra("email",contato.getEmail());

                        startActivity(intent);
                    }
                });

               return view;
    }

}
