package com.company;

import com.company.model.Artist;
import com.company.model.Datasource;

import java.util.List;

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

        datasource.close();
    }
}
