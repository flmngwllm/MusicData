package com.company;

import com.company.model.Artist;
import com.company.model.Datasource;
import com.company.model.SongArtist;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Datasource datasource = new Datasource();
        if(!datasource.open()){
            System.out.println("Can't open datasource");
            return;
        }

        //Calling the list of artists from database
        List<Artist> artists = datasource.queryArtists(Datasource.ORDER_BY_ASC);
        if(artists == null) {
            System.out.println("No artists!");
            return;
        }
    //looping through all elements in query
        for(Artist artist : artists){
            System.out.println("ID = " + artist.getId() + ", Name = " + artist.getName());
        }
    List<String> albumsForArtist =
            datasource.queryAlbumsForArtist("Iron Maiden", Datasource.ORDER_BY_ASC);

        for(String album : albumsForArtist){
            System.out.println(album);

        }

        List<SongArtist> songArtists = datasource.querryArtistForSong("Heartless", Datasource.ORDER_BY_ASC);
        if(songArtists == null) {
            System.out.println("Couldnt find the artist for the song");
            return;
        }

        for(SongArtist artist : songArtists){
            System.out.println("Artist name = " + artist.getArtistName() +
                    " Album name = " + artist.getAlbumName() +
                    " Track = " + artist.getTrack());
        }

        int count = datasource.getCount(Datasource.TABLE_SONGS);
        System.out.println("Number of songs is: " + count);
        datasource.querySongsMetaData();

        datasource.createViewForSongArtists();


        //adding the ability for user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a song title: ");
        String title = scanner.nextLine();


songArtists = datasource.querySongInfoView(title);

//checking for null as well
if(songArtists.isEmpty()){
    System.out.println("Couldn't find the artist for the song");
    return;
}

for(SongArtist artist : songArtists){
    System.out.println("FROM VIEW - Artist name = " + artist.getArtistName() +
            " Album name = " + artist.getAlbumName() +
            " Track number = " + artist.getTrack());
}
        datasource.close();
    }
}
