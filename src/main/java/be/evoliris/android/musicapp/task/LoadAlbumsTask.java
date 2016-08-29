package be.evoliris.android.musicapp.task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import be.evoliris.android.musicapp.adapter.AlbumCursorAdapter;
import be.evoliris.android.musicapp.db.dao.AlbumDAO;


public class LoadAlbumsTask extends AsyncTask <Void, String, Cursor>{

    private Context context;

    public LoadAlbumsTask (Context context){
        this.context=context;
    }

    private LoadAlbumsAsyncTaskCallback callback;

    public void setCallback(LoadAlbumsAsyncTaskCallback callback){
        this.callback=callback;
    }

    @Override
    protected Cursor doInBackground(Void... voids) {

        AlbumDAO albumDAO= new AlbumDAO(context);
        Cursor c= albumDAO.openReadable().readAllCursor();
        AlbumCursorAdapter adapter= new AlbumCursorAdapter(context, c);
        return c;
    }


    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);

        if(callback!=null){
                callback.postUpdateUI(cursor);
        }
    }

    public interface LoadAlbumsAsyncTaskCallback{

        void postUpdateUI(Cursor c);
    }

}




