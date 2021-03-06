package com.company.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    public static final String DB_NAME = "music.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:/Users/chaosmegaman/IdeaProjects/Music/" + DB_NAME;

    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUM_ARTIST = "artist";
    public static final int INDEX_ALBUM_ID = 1;
    public static final int INDEX_ALBUM_NAME = 2;
    public static final int INDEX_ALBUM_ARTIST = 3;

    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTIST_ID = "_id";
    public static final String COLUMN_ARTIST_NAME = "name";
    public static final int INDEX_ARTIST_ID = 1;
    public static final int INDEX_ARTIST_NAME = 2;

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONGS_ID = "_id";
    public static final String COLUMN_SONGS_TRACK = "track";
    public static final String COLUMN_SONGS_TITLE = "title";
    public static final String COLUMN_SONGS_ALBUM = "album";
    public static final int INDEX_SONGS_ID = 1;
    public static final int INDEX_SONGS_TRACK = 2;
    public static final int INDEX_SONGS_TITLE = 3;
    public static final int INDEX_SONGS_ALBUM = 4;

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC =2;
    public static final int ORDER_BY_DESC = 3;

    public static final String QUERY_ALBUMS_BY_ARTIST_START =
            "SELECT " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME + " FROM " + TABLE_ALBUMS +
                    " INNER JOIN " +TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST +
                    " WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + " = \"";

    public static final String QUERY_ALBUMS_BY_ARTIST_SORT =
            " ORDER BY " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";


    public static final String QUERY_ARTIST_FOR_SONG_START =
            "SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
                    TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", " +
                    TABLE_SONGS + "." + COLUMN_SONGS_TRACK + " FROM " + TABLE_SONGS +
                    " INNER JOIN " + TABLE_ALBUMS + " ON " +
                    TABLE_SONGS + "." + COLUMN_SONGS_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID +
                    " INNER JOIN " + TABLE_ARTISTS + " ON " +
                    TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
                    " WHERE " + TABLE_SONGS + "." + COLUMN_SONGS_TITLE + " = \"";

    public static final String QUERY_ARTIST_FOR_SONG_SORT =
            " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
                    TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";

//CREATE VIEW IF NOT EXIST artist_list AS SELECT artist.name, albums.name AS album,
// songs.track, songs.title FROM songs INNER JOIN albums ON songs.album = albums._id
// INNER JOIN artists ON albums.artist = artists._id ORDER BY artists.name
// albums.name, songs.track

    public static final String TABLE_ARTIST_SONG_VIEW = "artist_list";
    public static final String CREATE_ARTIST_FOR_SONG_VIEW = "CREATE VIEW IF NOT EXISTS " +
            TABLE_ARTIST_SONG_VIEW + " AS SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " AS " + COLUMN_SONGS_ALBUM + ", " +
            TABLE_SONGS + "." + COLUMN_SONGS_TRACK + ", " + TABLE_SONGS + "." + COLUMN_SONGS_TITLE +
            " FROM " + TABLE_SONGS +
            " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS +
            "." + COLUMN_SONGS_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID +
            " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST +
            " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
            " ORDER BY " +
            TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", " +
            TABLE_SONGS + "." + COLUMN_SONGS_TRACK;

    public static final String QUERY_VIEW_SONG_INFO = "SELECT " + COLUMN_ARTIST_NAME + ", " +
            COLUMN_SONGS_ALBUM + ", " + COLUMN_SONGS_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW +
            " WHERE " + COLUMN_SONGS_TITLE + " = \"";


    // SELECT name, album, track, FORM artist_list WHERE title = ?

    public static final String QUERY_VIEW_SONG_INFO_PREP = "SELECT " + COLUMN_ARTIST_NAME + ", " +
            COLUMN_SONGS_ALBUM + ", " + COLUMN_SONGS_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW +
            " WHERE " + COLUMN_SONGS_TITLE + " = ?";

    public static final String INSERT_ARTIST = "INSERT INTO " + TABLE_ARTISTS +
            '(' + COLUMN_ARTIST_NAME + ") VALUES(?)";

    public static final String INSERT_ALBUMS = "INSERT INTO " + TABLE_ALBUMS +
            '(' + COLUMN_ALBUM_NAME + ", " + COLUMN_ALBUM_ARTIST + ") VALUES(?, ?)";

    public static final String INSERT_SONGS = "INSERT INTO " + TABLE_SONGS +
            '(' + COLUMN_SONGS_TITLE + ", " + COLUMN_SONGS_TRACK + ", " + COLUMN_SONGS_ALBUM +
            ") VALUES(?, ?, ?)";

    public static final String QUERY_ARTIST = "SELECT " + COLUMN_ARTIST_ID + " FROM " +
            TABLE_ARTISTS + " WHERE " + COLUMN_ARTIST_NAME + " = ?";

    public static final String QUERY_ALBUM = "SELECT " + COLUMN_ALBUM_ID + " FROM " +
            TABLE_ALBUMS + " WHERE " + COLUMN_ALBUM_NAME + " = ?";








    //create connection instance
    private Connection conn;


    private PreparedStatement querySongInfoView;
    private PreparedStatement insertIntoArtist;
    private PreparedStatement insertIntoAlbum;
    private PreparedStatement insertIntoSongs;
    private PreparedStatement queryArtist;
    private PreparedStatement queryAlbum;



    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            querySongInfoView = conn.prepareStatement(QUERY_VIEW_SONG_INFO_PREP);
            insertIntoArtist = conn.prepareStatement(INSERT_ARTIST, Statement.RETURN_GENERATED_KEYS);
            insertIntoAlbum = conn.prepareStatement(INSERT_ALBUMS, Statement.RETURN_GENERATED_KEYS);
            insertIntoSongs = conn.prepareStatement(INSERT_SONGS);
            queryArtist = conn.prepareStatement(QUERY_ARTIST);
            queryAlbum = conn.prepareStatement(QUERY_ALBUM);


            return true;
        } catch (SQLException e) {
            System.out.println("Couldnt connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if(querySongInfoView != null){
                querySongInfoView.close();
            }

            if (insertIntoArtist != null){
                insertIntoArtist.close();
            }

            if (insertIntoAlbum != null) {
                insertIntoAlbum.close();
            }

                if (insertIntoSongs != null) {
                    insertIntoSongs.close();
                }

                if(queryArtist != null){
                    queryArtist.close();
                }

                if (queryAlbum != null) {
                    queryAlbum.close();
                }

            if (conn != null) {
                conn.close();


            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection " + e.getMessage());
        }
    }


    //return a list of artists
    public List<Artist> queryArtists(int sortOrder) {

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_ARTISTS);
        if (sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(COLUMN_ARTIST_NAME);
            sb.append(" COLLATE NOCASE ");
            if (sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        //try with resources to close automatically
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {


            //create a list of artist objects to add the instance to the list
            List<Artist> artists = new ArrayList<>();
            while (results.next()) {
                Artist artist = new Artist();
                artist.setId(results.getInt(INDEX_ALBUM_ID));
                artist.setName(results.getString(INDEX_ALBUM_NAME));
                artists.add(artist);
            }
            return artists;

        } catch (SQLException e) {
            System.out.println("Query failed" + e.getMessage());
            return null;
        }

    }
        public List<String> queryAlbumsForArtist(String artistName, int sortOrder) {

       // SELECT albums.name FROM albums INNER JOIN artists ON albums.artist = artists._id WHERE artists.name = "Carole KING" ORDER BY albums.name COLLATE NOCASE
        StringBuilder sb = new StringBuilder(QUERY_ALBUMS_BY_ARTIST_START);
       sb.append(artistName);
       sb.append("\"");

if(sortOrder != ORDER_BY_NONE) {
    sb.append(QUERY_ALBUMS_BY_ARTIST_SORT);
    if(sortOrder == ORDER_BY_DESC){
        sb.append("DESC");
    }else{
        sb.append("ASC");
    }
}

            System.out.println("SQL statement = " + sb.toString());

try (Statement statement = conn.createStatement();
ResultSet results = statement.executeQuery(sb.toString())){

    List<String> albums = new ArrayList<>();
    while(results.next()) {
        albums.add(results.getString(1));
    }

    return albums;

} catch(SQLException e) {
    System.out.println("Query failed: "+ e.getMessage());
    return null;
}

    }

    public List<SongArtist> querryArtistForSong(String songName, int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_ARTIST_FOR_SONG_START);
        sb.append(songName);
        sb.append("\"");

        if(sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ARTIST_FOR_SONG_SORT);
            if(sortOrder == ORDER_BY_DESC){
                sb.append("DESC");
            }else{
                sb.append("ASC");

            }
        }

        System.out.println("SQL Statement: " + sb.toString());

        try (Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(sb.toString())){

            //List of song artists

           List<SongArtist> songArtists = new ArrayList<>();

           while (results.next()){
               SongArtist songArtist = new SongArtist();
               songArtist.setArtistName(results.getString(1));
               songArtist.setAlbumName(results.getString(2));
               songArtist.setTrack(results.getInt(3));
               songArtists.add(songArtist);
           }

           // SELECT COUNT(*) FROM songs

           return songArtists;
        } catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }


    //getting schema data
    public void querySongsMetaData(){
        String sql = "SELECT * FROM " + TABLE_SONGS;

        try(Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(sql)) {


            ResultSetMetaData meta = results.getMetaData();
            int numColumns = meta.getColumnCount();

            //loop to print column name
            for (int i = 1; i <= numColumns; i++) {
                System.out.format("Columns %d in the songs table is names %s\n",
                        i, meta.getColumnName(i));
            }
        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());

        }
    }

    //get count from resultset and assign column names
    public int getCount(String table) {
        String sql = "SELECT COUNT(*) AS count FROM " + table;
        try(Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(sql)) {

            //how you get the results by treating it as a column
            int count = results.getInt("count");

            System.out.format("Count = %d\n", count);
            return count;
        } catch(SQLException e){
            System.out.println("Query failed: " + e.getMessage() );
            return -1;
        }
    }

    // create method for the view
    public boolean createViewForSongArtists(){

        try(Statement statement = conn.createStatement()) {

           statement.execute(CREATE_ARTIST_FOR_SONG_VIEW);
        return true;



        } catch(SQLException e){
            System.out.println("Create View failed: " + e.getMessage() );
            return false;
        }
    }

    //SELECT name, album, track FROM artist_list WHERE title = "Go Your Own Way"

    public List<SongArtist> querySongInfoView(String title) {


        try {
            querySongInfoView.setString(1, title);
            ResultSet results = querySongInfoView.executeQuery();

                List<SongArtist> songArtists = new ArrayList<>();
                while (results.next()) {
                    SongArtist songArtist = new SongArtist();
                    songArtist.setArtistName(results.getString(1));
                    songArtist.setAlbumName(results.getString(2));
                    songArtist.setTrack(results.getInt(3));
                    songArtists.add(songArtist);
                }

                return songArtists;


            } catch(SQLException e){
                System.out.println(" Query failed: " + e.getMessage());
                return null;


            }
        }


        //method for inserting artist
        private int insertArtist(String name) throws SQLException{
// checking to see if artist exist
        queryArtist.setString(1, name);
        ResultSet results = queryArtist.executeQuery();
        if(results.next()){
            return results.getInt(1);
        } else {
            // Insert the artist
            insertIntoArtist.setString(1, name);
            int affectedRows = insertIntoArtist.executeUpdate();
            if (affectedRows != 1){
                throw new SQLException("Couldn't inset artist");
            }
// retrieving id for new created record
            ResultSet generateKeys = insertIntoArtist.getGeneratedKeys();
            if(generateKeys.next()){
                return generateKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for artist");
            }
        }
        }


    //method for inserting artist
    private int insertAlbum(String name, int artistId) throws SQLException{
// checking to see if artist exist
        queryAlbum.setString(1, name);
        ResultSet results = queryAlbum.executeQuery();
        if(results.next()){
            return results.getInt(1);
        } else {
            // Insert the artist
            insertIntoAlbum.setString(1, name);
            insertIntoAlbum.setInt(2, artistId);
            int affectedRows = insertIntoAlbum.executeUpdate();

            if (affectedRows != 1){
                throw new SQLException("Couldn't inset album");
            }
// retrieving id for new created record
            ResultSet generateKeys = insertIntoAlbum.getGeneratedKeys();
            if(generateKeys.next()){
                return generateKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for album");
            }
        }
    }


    //method for inserting artist
    public void insertSongs(String title, String artist, String album,  int track){

        try{
            conn.setAutoCommit(false);

            int artistId = insertArtist(artist);
            int albumId = insertAlbum(album, artistId);
            insertIntoSongs.setInt(1, track);
            insertIntoSongs.setString(2, title);
            insertIntoSongs.setInt(3, albumId);


            int affectedRows = insertIntoSongs.executeUpdate();

                if (affectedRows == 1){
                    conn.commit();
                } else {
                    throw new SQLException("The song insert failed");
                }

        } catch(SQLException e) {
            System.out.println("Insert song exception " + e.getMessage());
            try{
                System.out.println("Performing rollback");
                conn.rollback();

            }catch (SQLException e2){
                System.out.println("Things are bad! " + e2.getMessage());
            }
        } finally {
            try{
                System.out.println("Resetting default commit behaviour");
                conn.setAutoCommit(true);
            }catch(SQLException e) {
                System.out.println("Couldn't reset auto commit! " + e.getMessage());
            }
        }

    }
}



