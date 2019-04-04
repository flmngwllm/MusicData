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
        List<Artist> artists = datasource.queryArtists();
        if(artists == null) {
            System.out.println("No artists!");
            return;
        }
    //looping through all elements in query
        for(Artist artist : artists){
            System.out.println("ID = " + artist.getId() + ", Name = " + artist.getName());
        }


        datasource.close();
    }
}
