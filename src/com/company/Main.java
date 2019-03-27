package com.company;

import com.company.model.Datasource;

public class Main {

    public static void main(String[] args) {
        Datasource datasource = new Datasource();
        if(!datasource.open()){
            System.out.println("Cant open datasource");
            return;
        }

        datasource.close();
    }
}
