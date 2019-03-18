package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "3DPlayer";
    private final static int version = 1;
    private static SQLiteHelper instance = null;
    SQLiteDatabase db;

    public static SQLiteHelper getInstance(Context context){
        if(instance == null){
            instance = new SQLiteHelper(context);
        }
        return instance;
    }

    public void createSqliteDatabase(Context context){
        db = SQLiteHelper.getInstance(context).getWritableDatabase();
    }

    public SQLiteHelper(Context context){
        super(context,DB_NAME,null,version);
    }

    @Override
    public String getDatabaseName() {
        return super.getDatabaseName();
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE VIDEO("+
        "ID INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                "NAME VARCHAR(50) ,"+
                "ROUTE VARCHAR(100) ,"+
                "ISLIKED INTEGER );";
        db.execSQL(createTable);
        String insertTest1 = "INSERT INTO VIDEO(NAME,ROUTE,ISLIKED) VALUES('1520504659755.mp4','/storage/tencent/MicroMsg/WeiXin/1520504659755.mp4',0)";
        db.execSQL(insertTest1);
/*        String insertTest2 = "INSERT INTO VIDEO(NAME,ROUTE,ISLIKED) VALUES('1520889262466.mp4','/tencent/MicroMsg/WeiXin/1520889262466.mp4',0)";
        db.execSQL(insertTest2);
        String insertTest3 = "INSERT INTO VIDEO(NAME,ROUTE,ISLIKED) VALUES('VID_20181026_094403.mp4','/DCIM/Camera/VID_20181026_094403.mp4',0)";
        db.execSQL(insertTest3);*/
    }

    public List<Video> getAllVideos(){
        List<Video> videos = new ArrayList<>();
        String sql = "SELECT * FROM VIDEO";
        Cursor cursor = db.rawQuery(sql,null);
        while(cursor.moveToNext()){
            Video video = new Video();
            video.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("ID"))));
            video.setName(cursor.getString(cursor.getColumnIndex("NAME")));
            video.setRoute(cursor.getString(cursor.getColumnIndex("ROUTE")));
            video.setLiked(Integer.parseInt(cursor.getString(cursor.getColumnIndex("ISLIKED"))));
            videos.add(video);
        }
        cursor.close();
        return videos;
    }

    public void addVideo(Video video){
        String sql = "INSERT INTO VIDEO(NAME,ROUTE,ISLIKED) VALUES(\'" +
                video.getName() + "\',\'" +
                video.getRoute() + "\'," +
                video.isLiked() +
                ")";
        db.execSQL(sql);
    }

    public void likeVideo(Video video){
        String sql = "UPDATE VIDEO SET ISLIKED = "+ video.isLiked()+" WHERE ID = "+video.getId();
        db.execSQL(sql);
    }

    public void deleteVideo(int id){
        String sql = "DELETE FROM VIDEO WHERE ID = " + id;
        db.execSQL(sql);
    }

    public void printVideos(){
        List<Video> videos = new ArrayList<>();
        String sql = "SELECT * FROM VIDEO";
        Cursor cursor = db.rawQuery(sql,null);
        System.out.println("-------------TABLE VIDEO-------------");
        System.out.println("---- ID | NAME | ROUTE | ISLIKED -----");
        while(cursor.moveToNext()){
            System.out.print(cursor.getString(cursor.getColumnIndex("ID"))+"  ");
            System.out.print(cursor.getString(cursor.getColumnIndex("NAME"))+"  ");
            System.out.print(cursor.getString(cursor.getColumnIndex("ROUTE"))+"  ");
            System.out.print(cursor.getString(cursor.getColumnIndex("ISLIKED"))+"  ");
            System.out.println("   ");
        }
        cursor.close();
    }
}