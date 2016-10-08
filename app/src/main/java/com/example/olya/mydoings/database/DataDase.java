package com.example.olya.mydoings.database;

/**
 * Created by tyuly on 08.10.2016.
 */

public class DataDase {
    public static final class DoingsTable
    {        public static final String NAME = "doings";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String DONE= "done";
        }
    }

}
