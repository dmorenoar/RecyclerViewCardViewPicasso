package com.example.dmorenoar.recyclerview.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dmorenoar.recyclerview.Models.Pokemon;
import com.example.dmorenoar.recyclerview.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


    private List<Pokemon> listPokemons;
    private int layout;
    private Activity activity;
    private OnItemClickListener itemClickListener;


    //Hemos de pasar el activity para poder inflar el context menu
    public MyAdapter(List<Pokemon> listPokemons, int layout, Activity activity, OnItemClickListener listener) {
        this.listPokemons = listPokemons;
        this.layout = layout;
        this.activity = activity;
        this.itemClickListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        //Inflamos la vista utilizando el activity en lugar del parent viewGroup
        View view = LayoutInflater.from(activity).inflate(layout, viewGroup, false);
        //Creamos nuestro viewholder y le pasamos directamente la vista inflada
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //Nos permite cambiar los datos en la vista
        //Se lanza una vez para nuestras listas del nuevo elemento pero no para los anteriores
        //Por lo tanto, solo
        //actualizaria uno, usamos el click para poder actualizar todos.

        //Le pasamos al metodo bind la posicion de la lista y el click listener
        viewHolder.bind(listPokemons.get(position), itemClickListener);

    }

    @Override
    public int getItemCount() {
        return this.listPokemons.size();
    }


    /*Definimos nuestro propio OnItemClickListener, ya uqe no podemos hacerlo igual que el list o el grid
     * Y precera como nativo*/
    public interface OnItemClickListener {
        void onItemClick(Pokemon animal, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView textViewName;
        public TextView textViewDescription;
        public TextView textViewCounter;
        public ImageView imageViewPokemon;
        public TextView textViewCounterName;


        public ViewHolder(View view) {
            super(view);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            textViewCounter = (TextView) itemView.findViewById(R.id.textViewCounter);
            textViewCounterName = (TextView) itemView.findViewById(R.id.textViewCounterName);
            imageViewPokemon = (ImageView) itemView.findViewById(R.id.imageViewPokemon);

            /*Añadimos al view el listener para el context menu, en vez de hacerlo en el activity mediante el método
             * registerForContextMenu*/
            view.setOnCreateContextMenuListener(this);
        }


        /*Nos sirve para que cuando se cree un nuevo elemento el adapter tenga la posicion correcta. Si añádo
         * uno nuevo el anterior como no se actualiza contendra el mismo numero de posicion por ello se usa getAdapterPosition.*/


        //Gestionamos la recarga de todos los elementos con el click
        public void bind(final Pokemon pokemon, final OnItemClickListener listener) {

            //Procesamos los datos a renderizar por cada animal que nos llega
            textViewName.setText(pokemon.getName());
            textViewCounter.setText(String.valueOf(pokemon.getQuantity()));
            textViewDescription.setText(pokemon.getDescription());
            textViewCounterName.setText(pokemon.getName());


            if (pokemon.getQuantity() == 0) {
                textViewCounter.setTextColor(ContextCompat.getColor(activity, R.color.colorAlert));
            } else {
                textViewCounter.setTextColor(ContextCompat.getColor(activity, R.color.cardview_dark_background));
            }


            /*En lugar de asignar la imagen al imageview con el setImageResource
             * hacemos uso de picasso que no hace un fit de la imagen en el imageView*/
            Picasso.get().load(pokemon.getImg()).fit().into(imageViewPokemon);

            //imageViewAnimal.setImageResource(animal.getImg());

            //Viene heredada de la clase RecyvlerView
            //Puedo ponerle el click a el elemento que quiera,

            imageViewPokemon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Le pasamos al adaptador el nombre y el adapter nos da la posicion
                    listener.onItemClick(pokemon, getAdapterPosition());
                }
            });

            /**Si dejamos la pulsación larga no podemos invocar al contextMenu
             //Se podría implementar un OnLongClickListener para cuando se deja pulsado
             itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
            Toast.makeText(v.getContext(), "Pulsacion larga", Toast.LENGTH_LONG).show();
            return true;
            }
            });*/
        }


        /*Soobreescribimos el método OnMenuItemClick dentro del ViewHolder
         * en lugar de hacerlo en el activity bajo el onContextItemSelected*/
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.deletePokemon:
                    listPokemons.remove(getAdapterPosition()); //Borramos un pokemon haciendo uso de la posición del adaptador
                    notifyItemRemoved(getAdapterPosition());
                    return true;
                case R.id.releaseOne:
                    //Liberamos un pokemon

                    if (listPokemons.get(getAdapterPosition()).getQuantity() > 0) {
                        listPokemons.get(getAdapterPosition()).releasePokemon();
                        notifyItemChanged(getAdapterPosition());
                    } else {
                        Toast.makeText(activity,"No puedes liberar a ningún " + listPokemons.get(getAdapterPosition()).getName(), Toast.LENGTH_LONG).show();
                    }

                    return true;
                default:
                    return false;
            }
        }

        /*Sobreescribimos el contextMenu en el viewholder en lugar de hacerlo en el activity*/
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //Recogemos la posición del elemento seleccionado
            Pokemon pokemon = listPokemons.get(getAdapterPosition());

            //Establecemos el titulo e icono para cada elemento
            menu.setHeaderTitle(pokemon.getName());
            menu.setHeaderIcon(pokemon.getIcon());

            //Inflamos el menu
            MenuInflater inflater = activity.getMenuInflater();
            inflater.inflate(R.menu.context_menu_pokemon, menu);

            /*
             * Añadimos las funcionalidades de cada elementos del contextmenu para
             * controlar las acciones en el viewholder en lugar del onContextItemSelected del activity
             * */

            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setOnMenuItemClickListener(this);
            }


            //Opción si no hubieramos implementado en la clase el OnMenuItemClickListener
            /*
            for (int i = 0; i < menu.size(); i++){
                menu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return false;
                    }
                });
            }*/


        }
    }

}
