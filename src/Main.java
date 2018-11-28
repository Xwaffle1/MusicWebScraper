import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Created by Chase(Xwaffle) on 11/25/2018.
 */
public class Main {

    public static void main(String[] args) {
        Document doc = null;
        try {
            doc = Jsoup.connect("https://play.google.com/store/music/album/Logic_Everybody?id=Bw73weoehfbvyxt33tdb4tb66gm").get();
        } catch (IOException e) {
            e.printStackTrace();
        }


        String albumName = doc.getElementsByClass("document-title").first().children().first().text();
        String artistName = doc.getElementsByClass("document-subtitle primary").first().text();
        String releaseDate = doc.getElementsByClass("left-info").first().children().get(0).text();

        if (releaseDate.contains(artistName)) {
            releaseDate = releaseDate.replace(artistName + " ", "");
        }

        if (releaseDate.contains(",")) {
            releaseDate = releaseDate.substring(releaseDate.indexOf(",") + 2, releaseDate.length());
        }


        System.out.println("INSERT INTO albums VALUES(NULL, (SELECT artistID FROM Artists WHERE name=\"" + artistName + "\"), \"" + albumName + "\", " + releaseDate + ");");


        System.out.println("-- " + artistName + " " + albumName);
        for (Element trackList : doc.getElementsByClass("track-list")) {
            for (Element element : trackList.getElementsByClass("track-list-row")) {
                Element trackData = element.getElementsByClass("playback-icon-cell").first();
                Element titleData = element.getElementsByClass("title-cell").first();
                String titleName = titleData.getElementsByClass("title").text();
                String trackString = trackData.getElementsByClass("track-number").text();
                int trackNum = Integer.parseInt(trackString);

                System.out.println("INSERT INTO songs VALUES(NULL, (SELECT albumID FROM Albums WHERE name=\"" + albumName + "\"), (SELECT artistID FROM Artists WHERE name=\"" + artistName + "\"), " + trackNum + ", \"" + titleName + "\");");
            }
        }


    }
}
