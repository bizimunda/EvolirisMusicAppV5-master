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

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        AlbumDAO albumDAO= new AlbumDAO(context);
         return albumDAO.openReadable().readAllCursor();

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




