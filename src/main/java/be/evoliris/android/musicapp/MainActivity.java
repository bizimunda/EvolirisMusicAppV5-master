package be.evoliris.android.musicapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.evoliris.android.musicapp.adapter.AlbumCursorAdapter;
import be.evoliris.android.musicapp.db.dao.AlbumDAO;
import be.evoliris.android.musicapp.model.Album;

public class MainActivity extends AppCompatActivity {

    private Button btnAdd;
    private ListView listView;
    //private AdapterAlbum adapterAlbum;
    AlbumDAO albumDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_main);
        btnAdd = (Button) findViewById(R.id.btn_main_add);
        //ArrayList<Album> albumArrayList= new ArrayList<>();
        //adapterAlbum=new AdapterAlbum(this,albumArrayList );
        //listView.setAdapter(adapterAlbum);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView) view;
                Toast.makeText(MainActivity.this, "Click on: " + tv.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });

        updateListView();

        registerForContextMenu(listView);

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (preferences.getBoolean("news", false)) {
            String email=preferences.getString("email","No email");
            Toast.makeText(MainActivity.this, "sending a mail.. "+ email.toString(), Toast.LENGTH_SHORT).show();
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(addIntent);
            }
        });

    }


    //region Old cod
    //        Album a = new Album();
//        a.setTitle("The Old Album");
//        Calendar c = Calendar.getInstance();
//        c.set(1965, Calendar.NOVEMBER, 17);
//        a.setOutDate(c.getTime());
//        a.setImageUrl("https://upload.wikimedia.org/wikipedia/en/8/80/TheOffspringSmashalbumcover.jpg");
//        a.setGenre("Pop-Punk");
//        a.setFavorite(false);
//        a.setRating(0.5f);
//
//        albumDAO = new AlbumDAO(MainActivity.this);
//        albumDAO.openWritable();
//        if (albumDAO.create(a)) {
//            Toast.makeText(MainActivity.this, "Album créé !", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(MainActivity.this, "Erreur :'(", Toast.LENGTH_SHORT).show();
//        }
//
//        Album a2 = albumDAO.readById(a.getId());
//        if (a2 != null) {
//            Toast.makeText(MainActivity.this, "Album : " + a2.getTitle(), Toast.LENGTH_SHORT).show();
//            Log.i("Album", a2.getTitle() + " " + a2.getOutDate() + " " + a2.getRating());
//        }
//
//        List<Album> albums = albumDAO.readAll();
//        for (Album album : albums) {
//            Log.i("Albums", album.getTitle());
//        }
//
//        albumDAO.close();
    //endregion




    @Override
    protected void onResume() {
        super.onResume();
        updateListView();
    }

    private void updateListView() {


        AlbumDAO albumDAO= new AlbumDAO(MainActivity.this);
        Cursor c= albumDAO.openReadable().readAllCursor();
        AlbumCursorAdapter adapter= new AlbumCursorAdapter(MainActivity.this, c);
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                Toast.makeText(this, "Everything is deleted", Toast.LENGTH_SHORT).show();
                albumDAO.openWritable().deleteAll();
                albumDAO.close();
                updateListView();
                break;
            case R.id.preferences:
                Intent intentPreference = new Intent(this, AppPreferenceActivity.class);
                startActivity(intentPreference);
                break;
            default:
                break;
        }
        return true;
    }


    //region ContextMenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_main_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();

        TextView tv = (TextView) info.targetView;
        Log.i(getLocalClassName(), tv.getText().toString());

        switch (item.getItemId()) {
            case R.id.context_menu_edit:

                break;
            case R.id.context_menu_delete:
                albumDAO = new AlbumDAO(this);
                albumDAO.openWritable().deleteByTitle(tv.getText().toString());
                Toast.makeText(this, "Title: " + tv.getText().toString() + " is deleted", Toast.LENGTH_SHORT).show();
                albumDAO.close();
                updateListView();
                break;
            default:
                break;
        }
        return true;
    }
    //endregion


}
