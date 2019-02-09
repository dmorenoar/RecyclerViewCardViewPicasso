package com.example.dmorenoar.recyclerview.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dmorenoar.recyclerview.Adapters.MyAdapter;
import com.example.dmorenoar.recyclerview.Models.Pokemon;
import com.example.dmorenoar.recyclerview.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int contador;

    private List<Pokemon> listPokemons;

    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter myAdapter; //Usamos el tipo Adapter y no nuestro adapter porque nuestro adapter esta heredando del padre tambien
    private RecyclerView.LayoutManager myLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listPokemons = getAllPokemons();

        //Obtenemos nuestro recicler view
        myRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        myLayoutManager = new LinearLayoutManager(this);

        //Como trabajar el recyclerview en el grid o el list.
        //Asignamos otros parametros al gridlayoutManager con numero de columnas
        //myLayoutManager = new GridLayoutManager(this, 2);

        //Trabajamos con el grid dandole el numero de columnas y la orientacion. Nos permitira si tenemos
        //diferentes formatos de vista en nuestro grid layout y hara un efecto chulo
        //Deberiamos no utilizar el recyclerView.setHasFixeSide
        //Simulariamos pinterest, wallapop, celdas con diferentes tamaños.
        //myLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        //Le pasamos el activity con this.
        myAdapter = new MyAdapter(listPokemons, R.layout.recycler_view_item, MainActivity.this, new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Pokemon pokemon, int position) { //Usamos el click de nuestro listener definico

                Toast.makeText(MainActivity.this, pokemon.getName() + "-" + position, Toast.LENGTH_LONG).show();

                /*Definimos la acción del elemento al clickar sobre el*/
                pokemon.addPokemon();
                myAdapter.notifyItemChanged(position);
            }
        });

        //Le damos un tamaño fijo a los elementos de nuestro recyclerview sabiendoq ue no va a cambiar y lo estandarizamos mejroando el perfomance
        myRecyclerView.setHasFixedSize(true);

        //Configurar animaciones, la animacion por defecto
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //Asignamos el adaptador y el layoutManager
        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerView.setAdapter(myAdapter);
    }


    /*Crea las opciones de menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    /*Configuramos la opciones de menu*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_name:
                this.addPokemon(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<Pokemon> getAllPokemons() {
        return new ArrayList<Pokemon>() {{
            add(new Pokemon("Bulbasaur", "A Bulbasaur es fácil verle echándose una siesta al sol. La semilla que tiene en el lomo va creciendo cada vez más a medida que absorbe los rayos del sol. ", R.drawable.bulbasaur, R.drawable.ic_bulbasaur_web));
            add(new Pokemon("Charmander", "La llama que tiene en la punta de la cola arde según sus sentimientos. Llamea levemente cuando está alegre y arde vigorosamente cuando está enfadado. ", R.drawable.charmander, R.drawable.ic_charmander_web));
            add(new Pokemon("Squirtle", "El caparazón de Squirtle no le sirve de protección únicamente. Su forma redondeada y las hendiduras que tiene le ayudan a deslizarse en el agua y le permiten nadar a gran velocidad.", R.drawable.squirtle, R.drawable.ic_squirtle_web));
            add(new Pokemon("Eevee", "La configuración genética de Eevee le permite mutar y adaptarse enseguida a cualquier medio en el que viva.", R.drawable.eevee, R.drawable.ic_eevee_web));
            add(new Pokemon("Meowth", "Meowth retrae las afiladas uñas de sus zarpas para caminar a hurtadillas, dando sigilosos pasos para pasar inadvertido.", R.drawable.meowth, R.drawable.ic_meowth_web));
            add(new Pokemon("Pikachu", "Cada vez que un Pikachu se encuentra con algo nuevo, le lanza una descarga eléctrica. ", R.drawable.pikachu, R.drawable.ic_pikachu_web));
        }};

    }

    /*Métodos para elimiar y añadir un elemento del recyclerview*/
    private void addPokemon(int position) {
        listPokemons.add(0, new Pokemon("Nuevo Pokemon" + (++contador), "Sin descripción", R.drawable.unknown, R.drawable.ic_unknow));
        /*Notificamos al adaptador del cambio y al layout para que nos ubique en la posición
        del pokemon que acabamos de añadir, si quisieramos añadirlo al final cogeríamos el listPokemons.size para obtener la última posición*/
        myAdapter.notifyItemInserted(position);
        //Cuando añadimos uno posicionarnos en la primera linea para observar el que hemos añadido
        //El layout manager se encarga de gestionar el layout
        myLayoutManager.scrollToPosition(position);
    }


}
