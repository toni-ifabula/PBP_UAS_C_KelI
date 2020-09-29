package com.antoniuswicaksana.project_pbp;

import java.util.ArrayList;

public class DaftarEvent {
    public ArrayList<Event> EVENT;

    public DaftarEvent(){
        EVENT = new ArrayList();
        EVENT.add(WEDDING_A);
        EVENT.add(WEDDING_B);
        EVENT.add(WEDDING_C);
        EVENT.add(BIRTHDAY_A);
        EVENT.add(BIRTHDAY_B);
        EVENT.add(KONSER_A);
        EVENT.add(KONSER_B);
    }

    public static final Event WEDDING_A = new Event("Wedding Paket A", 100000000,
            "100", "https://upload.wikimedia.org/wikipedia/commons/thumb/6/60/YellowLabradorLooking.jpg/250px-YellowLabradorLooking.jpg");

    public static final Event WEDDING_B = new Event("Wedding Paket B", 70000000,
            "70", "https://cdn1-www.cattime.com/assets/uploads/2011/12/file_2744_british-shorthair-460x290-460x290.jpg");

    public static final Event WEDDING_C = new Event("Wedding Paket C", 50000000,
            "50", "https://anjingdijual.com/files/jenis-anjing/foto/golden-retriever/g1.jpg");

    public static final Event BIRTHDAY_A = new Event("Birthday Paket A", 15000000,
            "50", "https://asset-a.grid.id//crop/0x0:0x0/360x240/photo/2019/10/31/71888328.jpg");

    public static final Event BIRTHDAY_B = new Event("Birthday Paket B", 10000000,
            "30", "https://hips.hearstapps.com/digitalspyuk.cdnds.net/18/09/1519714967-john-cena-raw.jpg?crop=0.566xw:1.00xh;0.225xw,0&resize=480:*");

    public static final Event KONSER_A = new Event("Konser Paket A", 100000000,
            "1000", "https://asset-a.grid.id//crop/0x0:0x0/360x240/photo/2019/10/31/71888328.jpg");

    public static final Event KONSER_B = new Event("Konser Paket B", 50000000,
            "500", "https://asset-a.grid.id//crop/0x0:0x0/360x240/photo/2019/10/31/71888328.jpg");
}
