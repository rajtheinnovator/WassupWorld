package com.example.android.wassupworld.Utils;

import android.content.Context;
import android.util.DisplayMetrics;

import com.example.android.wassupworld.R;



public class Icons {


    public static int getImageUrl(String name) {
        int url;
        if (name == null)
            name = "";
        switch (name) {
            case "abc-news-au":
                url = R.drawable.abc;
                break;
            case "al-jazeera-english":
                url = R.drawable.aljazera;
                break;
            case "ars-technica":
                url = R.drawable.ars;
                break;
            case "associated-press":
                url = R.drawable.ap;
                break;
            case "bbc-news":
                url = R.drawable.bbc;
                break;
            case "bbc-sport":
                url = R.drawable.bbc_sport;
                break;
            case "bild":
                url = R.drawable.blid;
                break;
            case "bloomberg":
                url = R.drawable.bloom;
                break;
            case "breitbart-news":
                url = R.drawable.breitbart;
                break;
            case "business-insider":
                url = R.drawable.businessinsider;
                break;
            case "business-insider-uk":
                url = R.drawable.businessinsider;
                break;
            case "buzzfeed":
                url = R.drawable.buzzfeed;
                break;

            case "cnbc":
                url = R.drawable.cnbc;
                break;
            case "cnn":
                url = R.drawable.cnn;
                break;
            case "daily-mail":
                url = R.drawable.daily;
                break;
            case "der-tagesspiegel":
                url = R.drawable.der;
                break;
            case "die-zeit":
                url = R.drawable.die;
                break;
            case "engadget":
                url = R.drawable.engadget;
                break;
            case "entertainment-weekly":
                url = R.drawable.ew;

                break;
            case "espn":
                url = R.drawable.espn;
                break;
            case "espn-cric-info":
                url = R.drawable.espn;
                break;
            case "financial-times":
                url = R.drawable.ft;
                break;
            case "football-italia":
                url = R.drawable.footbal_italia;
                break;
            case "fortune":
                url = R.drawable.fortune;
                break;
            case "four-four-two":
                url = R.drawable.fourfourtwo;
                break;
            case "fox-sports":
                url = R.drawable.fox_sport;
                break;
            case "google-news":
                url = R.drawable.google;
                break;
            case "hacker-news":
                url = R.drawable.hacker;
                break;
            case "ign":
                url = R.drawable.ign;
                break;
            case "independent":
                url = R.drawable.independent;
                break;
            case "mashable":
                url = R.drawable.mashable;
                break;
            case "metro":
                url = R.drawable.metro;

                break;
            case "mirror":
                url = R.drawable.mirror;
                break;
            case "mtv-news":
                url = R.drawable.mtv;
                break;
            case "mtv-news-uk":
                url = R.drawable.mtv;
                break;
            case "national-geographic":
                url = R.drawable.national;
                break;
            case "new-scientist":
                url = R.drawable.scientist;
                break;
            case "newsweek":
                url = R.drawable.newsweek;
                break;
            case "new-york-magazine":
                url = R.drawable.newyork;
                break;
            case "nfl-news":
                url = R.drawable.nfl;
                break;
            case "polygon":
                url = R.drawable.polygon;
                break;
            case "recode":
                url = R.drawable.recode;
                break;
            case "reddit-r-all":
                url = R.drawable.reddit;
                break;

            case "wired-de":
                url = R.drawable.wired;
                break;
            case "reuters":
                url = R.drawable.reuters;
                break;
            case "wirtschafts-woche":
                url = R.drawable.wirtschafts;
                break;
            case "t3n":
                url = R.drawable.t3n;
                break;
            case "spiegel-online":
                url = R.drawable.spiegel;
                break;
            case "talksport":
                url = R.drawable.talksport;
                break;
            case "techcrunch":
                url = R.drawable.techcrunch;
                break;
            case "techradar":
                url = R.drawable.techradar;
                break;
            case "the-economist":
                url = R.drawable.economist;
                break;
            case "the-guardian-uk":
                url = R.drawable.guardian;
                break;
            case "the-guardian-au":
                url = R.drawable.guardian;
                break;
            case "the-hindu":
                url = R.drawable.the_hindu;
                break;
            case "the-huffington-post":
                url = R.drawable.hulfpost;
                break;
            case "the-lad-bible":
                url = R.drawable.theladbiblr;
                break;
            case "the-new-york-times":
                url = R.drawable.thenewyork;
                break;
            case "the-next-web":
                url = R.drawable.thenextweb;
                break;
            case "the-sport-bible":
                url = R.drawable.sportbible;
                break;
            case "the-telegraph":
                url = R.drawable.telegraph;
                break;
            case "the-times-of-india":
                url = R.drawable.thetimesofindia;
                break;
            case "the-verge":
                url = R.drawable.verge;
                break;
            case "the-wall-street-journal":
                url = R.drawable.wallstreet;
                break;
            case "the-washington-post":
                url = R.drawable.washingtonpost;
                break;
            case "time":
                url = R.drawable.time;
                break;
            case "usa-today":
                url = R.drawable.usa;
                break;
            default:
                url = R.drawable.newsletter_empty;
        }
        return url;

    }

    public static int calculateNoOfColumnsSourcesList(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        return (int) (dpWidth / 75);
    }
    public static int calculateNoOfColumnsCatList(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 150);
    }

    public static int getCategroyIcon(String title) {
        int id=0;

            switch (title) {
                case "General":
                    id = R.drawable.newspaper;
                    break;
                case "Business":
                    id = R.drawable.ic_business_center_white_24dp;
                    break;
                case "Entertainment":
                    id = R.drawable.movie;
                    break;
                case "Gaming":
                    id = R.drawable.gaming;
                    break;
                case "Music":
                    id = R.drawable.music_note;
                    break;
                case "Politics":
                    id = R.drawable.city;
                    break;
                case "Science and Nature":
                    id = R.drawable.earth;
                    break;
                case "Technology":
                    id = R.drawable.lightbulb_on_outline;
                    break;
                case "Sport":
                    id = R.drawable.football;
                    break;

            }
            return id;

    }
    public static int getCategroyColor( String title) {
        int id=0;

            switch (title) {
                case "General":
                    id = R.color.purple;
                    break;
                case "Business":
                    id = R.color.indigo;
                    break;
                case "Entertainment":
                    id = R.color.yellow;
                    break;
                case "Gaming":
                    id = R.color.teal;
                    break;
                case "Music":
                    id = R.color.blue;
                    break;
                case "Politics":
                    id = R.color.grey;
                    break;
                case "Science and Nature":
                    id = R.color.green;
                    break;
                case "Technology":
                    id = R.color.pink;
                    break;
                case "Sport":
                    id = R.color.orange;
                    break;

            }
            return id;

    }



}

