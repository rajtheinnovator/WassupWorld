package com.example.android.wassupworld.Utils;

import android.content.Context;
import android.util.DisplayMetrics;

import com.example.android.wassupworld.R;

/**
 * Created by dell on 7/5/2017.
 */

public class Icons {


    public static String getImageUrl(String name) {
        String url;
        if (name == null)
            name = "";
        switch (name) {
            case "abc-news-au":
                url = "http://mobile.abc.net.au/cm/cb/4355924/News+iOS+120x120/data.png";
                break;
            case "al-jazeera-english":
                url = "http://www.aljazeera.com/mritems/assets/images/touch-icon-iphone-retina.png";
                break;
            case "ars-technica":
                url = "http://cdn.arstechnica.net/apple-touch-icon.png";
                break;
            case "associated-press":
                url = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0c/Associated_Press_logo_2012.svg/1200px-Associated_Press_logo_2012.svg.png";
                break;
            case "bbc-news":
                url = "http://m.files.bbci.co.uk/modules/bbc-morph-news-waf-page-meta/1.2.0/apple-touch-icon.png";
                break;
            case "bbc-sport":
                url = "http://static.bbci.co.uk/onesport/2.11.139/images/web-icons/bbc-sport-144.png";
                break;
            case "bild":
                url = "http://bilder.bild.de/fotos/bild-de-35605834/Bild/3.bild.png";
                break;
            case "bloomberg":
                url = "https://assets.bwbx.io/business/public/images/favicons/apple-touch-icon-120x120-ef3226f2bd.png";
                break;
            case "breitbart-news":
                url = "http://www.breitbart.com/apple-touch-icon-152x152.png";
                break;
            case "business-insider":
                url = "http://static5.businessinsider.com/assets/images/us/favicons/apple-touch-icon-120x120.png?v=BI-US-2016-03-31";
                break;
            case "business-insider-uk":
                url = "http://static5.businessinsider.com/assets/images/us/favicons/apple-touch-icon-120x120.png?v=BI-US-2016-03-31";
                break;
            case "buzzfeed":
                url = "https://www.buzzfeed.com/static-assets/img/touch-icon-ios_120.208a0e329cd6e8d831b21ae17fb6aabb.png";
                break;
            case "":
                url = "https://icons.better-idea.org/lettericons/C-120-fab715.png";
                break;
            case "cnbc":
                url = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e3/CNBC_logo.svg/1200px-CNBC_logo.svg.png";
                break;
            case "cnn":
                url = "http://i.cdn.cnn.com/cnn/.e/img/3.0/global/misc/apple-touch-icon.png";
                break;
            case "daily-mail":
                url = "http://www.dailymail.co.uk/apple-touch-icon.png";
                break;
            case "der-tagesspiegel":
                url = "http://www.tagesspiegel.de/images/apple-touch-icon/9800138/2-formatOriginal.png";
                break;
            case "die-zeit":
                url = "http://img.zeit.de/static/img/ZO-ipad-114x114.png";
                break;
            case "engadget":
                url = "https://s.blogsmithmedia.com/www.engadget.com/assets-h36a73bdca1d2ca122a5bb65c38b0fb9f/images/apple-touch-icon-120x120.png?h=232a14b1a350de05a49b584a62abac9e";
                break;
            case "entertainment-weekly":
                url = "http://1.gravatar.com/blavatar/782bdf163168ca78e52d32d27b830793?s=114";
                break;
            case "espn":
                url = "http://a.espncdn.com/wireless/mw5/r1/images/bookmark-icons/espn_icon-152x152.min.png";
                break;
            case "espn-cric-info":
                url = "http://a.espncdn.com/wireless/mw5/r1/images/bookmark-icons/espn_icon-152x152.min.png";
                break;
            case "financial-times":
                url = "https://www.ft.com/__assets/creatives/brand-ft/icons/v2/apple-touch-icon-180x180.png";
                break;
            case "football-italia":
                url = "https://pbs.twimg.com/profile_images/2514923001/kvc336eae86bjgy8ys1s_400x400.jpeg";
                break;
            case "fortune":
                url = "http://1.gravatar.com/blavatar/dab01945b542bffb69b4f700d7a35f8f?s=114";
                break;
            case "four-four-two":
                url = "https://images.cdn.fourfourtwo.com/sites/fourfourtwo.com/themes/fourfourtwo/images/apple-icon-144x144px.png";
                break;
            case "fox-sports":
                url = "https://pbs.twimg.com/profile_images/824007776489738241/pFk_8LLO_400x400.jpg";
                break;
            case "google-news":
                url = "https://ssl.gstatic.com/news-static/img/1703439073-news-thumb-128_w.png";
                break;
            case "hacker-news":
                url = "https://news.ycombinator.com/favicon.ico";
                break;
            case "ign":
                url = "http://m.ign.com/apple-touch-icon-precomposed.png";
                break;
            case "independent":
                url = "http://www.independent.co.uk/sites/all/themes/ines_themes/independent_theme/img/apple-icon-120x120.png";
                break;
            case "mashable":
                url = "http://mashable.com/apple-touch-icon-120x120.png?v=m2Pmw8zNwl";
                break;
            case "metro":
                url = "http://1.gravatar.com/blavatar/72ba1fb4e57339685f67d1d89b3db216?s=120";
                break;
            case "mirror":
                url = "https://lh3.googleusercontent.com/MDO8iNBCRl_94UrF7Gfp1nY6Pb3V-u7JKsAwdLZNK5zo9qD2QdCvtFF9gjAMgx7KnA=w300";
                break;
            case "mtv-news":
                url = "http://www.mtv.com/apple-touch-icon-precomposed.png";
                break;
            case "mtv-news-uk":
                url = "http://www.mtv.com/apple-touch-icon-precomposed.png";
                break;
            case "national-geographic":
                url = "http://logok.org/wp-content/uploads/2014/06/National-Geographic-logo-yellow-frame-880x660.png";
                break;
            case "new-scientist":
                url = "https://static1.squarespace.com/static/577ccaf4414fb56605df7a9a/577ce391e58c6214ec0f5fbb/578f3e01c534a5c08c4625bb/1474888823762/new+scientist+logo.png";
                break;
            case "newsweek":
                url = "http://s.newsweek.com/sites/www.newsweek.com/themes/newsweek/favicons/apple-touch-icon-120x120.png";
                break;
            case "new-york-magazine":
                url = "http://cache.nymag.com/media/nymag/icon.120x120.png";
                break;
            case "nfl-news":
                url = "http://i.nflcdn.com/static/site/7.5/img/apple/apple-touch-icon-114x114.png?7.5hotfix/7.5.18";
                break;
            case "polygon":
                url = "https://cdn2.vox-cdn.com/uploads/hub/sbnu_logo_minimal/405/touch_icon_ipad_retina_1000x1000.7014.png";
                break;
            case "recode":
                url = "https://cdn1.vox-cdn.com/uploads/chorus_asset/file/6397047/recode_favicon-180.0.png";
                break;
            case "reddit-r-all":
                url = "https://www.redditstatic.com/mweb2x/favicon/120x120.png";
                break;

            case "wired-de":
                url = "https://www.wired.de/sites/default/files/favicons/apple-touch-icon-120x120.png";
                break;
            case "reuters":
                url = "http://s1.reutersmedia.net/resources_v3/images/favicon/apple-touch-icon-120x120.png";
                break;
            case "wirtschafts-woche":
                url = "http://www.wiwo.de/images/favicon/4516376/6-formatOriginal.ico";
                break;
            case "t3n":
                url = "http://d1quwwdmdfumn6.cloudfront.net/t3n-rebrush/images/core/icon/AppIcon120.png";
                break;
            case "spiegel-online":
                url = "http://m.spiegel.de/static/V2/logo/favicon/touch-icon120.png";
                break;

            case "talksport":
                url = "https://static-media.streema.com/media/cache/db/cc/dbcc401f32205044cf9fe54649f17ba7.jpg";
                break;
            case "techcrunch":
                url = "https://s0.wp.com/wp-content/themes/vip/techcrunch-2013/assets/images/homescreen_TCIcon_ipad_2x.png";
                break;
            case "techradar":
                url = "http://cdn0.static.techradar.futurecdn.net/20170703/apple-touch-icon.png";
                break;
            case "the-economist":
                url = "https://lh3.googleusercontent.com/j0rfLNcDw7hmSdKKXH88JA6tG6WKANNPIg1_9prDyLL2nLEUESd47CfTmyH1JLpTxxoD=w300";
                break;
            case "the-guardian-uk":
                url = "https://assets.guim.co.uk/images/favicons/cf23080600002e50f5869c72f5a904bd/120x120.png";
                break;
            case "the-guardian-au":
                url = "https://assets.guim.co.uk/images/favicons/cf23080600002e50f5869c72f5a904bd/120x120.png";
                break;
            case "the-hindu":
                url = "https://store-images.s-microsoft.com/image/apps.38416.9007199266250907.217cbcfc-4852-43d4-b3af-766d136f85fa.6ce717fa-8786-45ed-9bfc-ebc414691daa?w=180&h=180&q=60";
                break;
            case "the-huffington-post":
                url = "http://www.centreforcities.org/wp-content/uploads/2017/01/Huffington-Post-Logo.png";
                break;
            case "the-lad-bible":
                url = "http://www.ladbible.com/assets/images/theme/favicons/apple-touch-icon-120x120.png";
                break;
            case "the-new-york-times":
                url = "https://cdn1.nyt.com/mw-static/images/touch-icon-ipad-144.319373aa.png";
                break;
            case "the-next-web":
                url = "https://cdn2.tnwcdn.com/wp-content/themes/cyberdelia/assets/icons/apple-touch-icon-120x120.png?v=1499170217";
                break;
            case "the-sport-bible":
                url = "http://www.sportbible.com/assets/images/theme/favicons/apple-touch-icon-120x120.png";
                break;
            case "the-telegraph":
                url = "http://www.telegraph.co.uk/template/ver1-0/i/telegraphFacebook.jpg";
                break;
            case "the-times-of-india":
                url = "https://icons.better-idea.org/lettericons/I-120-000000.png";
                break;
            case "the-verge":
                url = "https://cdn2.vox-cdn.com/uploads/chorus_asset/file/7395359/ios-icon.0.png";
                break;
            case "the-wall-street-journal":
                url = "https://www.wsj.com/apple-touch-icon-precomposed.png";
                break;
            case "the-washington-post":
                url = "https://www.washingtonpost.com/wp-apps/imrs.php?src=http://www.washingtonpost.com/wp-stat/wp-print-edition-512.png&w=300&h=300&t=20170517a";
                break;
            case "time":
                url = "http://s0.wp.com/wp-content/themes/vip/time2014/img/time-touch_icon_120.png";
                break;
            case "usa-today":
                url = "https://lh3.googleusercontent.com/2i2GvT7d5CxbPh9edK9PJXZDgLKsisFrAfRj90MubTAgY6EFRk0gAjGb1iIP4poB-k0=w300";
                break;
            default:
                url = "https://s-media-cache-ak0.pinimg.com/736x/2f/d3/24/2fd3245c9d5f66483a39f678e05bf8c2--icon-icon-app-icon.jpg";
        }
        return url;

    }

    public static int calculateNoOfColumnsSourcesList(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 75);
        return noOfColumns;
    }
    public static int calculateNoOfColumnsCatList(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 150);
        return noOfColumns;
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
                    id = R.color.blue;
                    break;
                case "Business":
                    id = R.color.indigo;
                    break;
                case "Entertainment":
                    id = R.color.purple;
                    break;
                case "Gaming":
                    id = R.color.grey;
                    break;
                case "Music":
                    id = R.color.yellow;
                    break;
                case "Politics":
                    id = R.color.teal;
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

