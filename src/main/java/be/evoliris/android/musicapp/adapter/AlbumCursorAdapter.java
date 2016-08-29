package be.evoliris.android.musicapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.zip.Inflater;

import be.evoliris.android.musicapp.R;
import be.evoliris.android.musicapp.db.dao.AlbumDAO;
import be.evoliris.android.musicapp.model.Album;

/**
 * Created by Evoliris on 29/08/2016.
 */
public class AlbumCursorAdapter extends CursorAdapter {

    private LayoutInflater inflater;


    public AlbumCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {


        return inflater.inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tvTitle= (TextView) view.findViewById(R.id.tv_item_title);
        TextView tvGenre= (TextView) view.findViewById(R.id.tv_list_item_date);
        TextView tvDate= (TextView) view.findViewById(R.id.tv_list_item_date);
        CheckBox cbFavorite= (CheckBox) view.findViewById(R.id.cb_list_item_favorite);
        RatingBar ratingBar= (RatingBar) view.findViewById(R.id.rb_list_item);

        Album a= AlbumDAO.cursorToAlbum(cursor);

        tvTitle.setText(a.getTitle());
        if(a.isFavorite()){
            tvTitle.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
        tvGenre.setText(a.getGenre());
        Calendar c= Calendar.getInstance();
        c.setTime(a.getOutDate());
        tvDate.setText(String.valueOf(c.get(Calendar.YEAR)));
        ratingBar.setRating(a.getRating());
        cbFavorite.setChecked(a.isFavorite());

    }
}
