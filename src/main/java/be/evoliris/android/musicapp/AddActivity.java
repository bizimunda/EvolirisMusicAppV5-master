package be.evoliris.android.musicapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import be.evoliris.android.musicapp.db.dao.AlbumDAO;
import be.evoliris.android.musicapp.model.Album;

public class AddActivity extends AppCompatActivity {

    private EditText etTitle, etGenre, etUrl;
    private DatePicker datePicker;
    private CheckBox cbFavorite;
    private RatingBar ratingBar;
    private Button btnAdd, btnShow;
    int checkBox;

    String title, genre, url, rating, date;

    AlbumDAO albumDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //region Déclaration
        etTitle = (EditText) findViewById(R.id.et_addActivity_title);
        etGenre = (EditText) findViewById(R.id.et_addActivity_genre);
        etUrl = (EditText) findViewById(R.id.et_addActivity_url);
        cbFavorite = (CheckBox) findViewById(R.id.cb_addActivity_favorite);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar_addActivity_rating);
        btnAdd = (Button) findViewById(R.id.btn_addActivity_add);
        btnShow=(Button)findViewById(R.id.btn_addActivity_show);

        datePicker = (DatePicker) findViewById(R.id.datePicker_addActivity_outDate);
        //endregion

        final Integer dobYear = datePicker.getYear();
        final Integer dobMonth = datePicker.getMonth();
        final Integer dobDate = datePicker.getDayOfMonth();
        StringBuilder sb = new StringBuilder();
        sb.append(dobYear.toString()).append("-").append(dobMonth.toString()).append("-").append(dobDate.toString()).append("00:00:00");
        date = sb.toString();


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = etTitle.getText().toString();
                genre = etGenre.getText().toString();
                url = etUrl.getText().toString();
                Float rating_ = ratingBar.getRating();
                rating = String.valueOf(rating_);

                addListenerCheckBox();
                Toast.makeText(getApplicationContext(), "---" + title + "---" + genre + "---" + url + "---" + checkBox + "---" + date + "---" + rating_, Toast.LENGTH_SHORT).show();

                Album a = new Album();
                a.setTitle(title);
                Calendar c = Calendar.getInstance();
                c.set(dobYear, dobMonth, dobDate);
                a.setOutDate(c.getTime());
                a.setImageUrl(url);
                a.setGenre(genre);
                a.setFavorite(addListenerCheckBox());
                a.setRating(rating_);

                albumDAO = new AlbumDAO(AddActivity.this);
                albumDAO.openWritable();
                if (albumDAO.create(a)) {
                    Toast.makeText(AddActivity.this, "Album créé !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddActivity.this, "Erreur :'(", Toast.LENGTH_SHORT).show();
                }
                albumDAO.close();
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albumDAO = new AlbumDAO(AddActivity.this);
                albumDAO.openWritable();
                List<Album> albums = albumDAO.readAll();
                for (Album album : albums) {
                    Toast.makeText(AddActivity.this,"\n"+album.getTitle(), Toast.LENGTH_LONG).show();
                }
                albumDAO.close();
            }
        });
    }

    public boolean addListenerCheckBox() {
        if (cbFavorite.isChecked()) {
            checkBox = 1;
            return true;
        } else {
            checkBox = 0;
            return false;
        }
    }


}
