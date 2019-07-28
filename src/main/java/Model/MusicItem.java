package Model;
import java.math.BigDecimal;
import java.util.UUID;

public abstract class MusicItem {
    private String itemID;
    private String title;
    private String genre;
    private Date releaseDate;
    private String artist;
    private BigDecimal price;

    MusicItem(String itemID, String title, String genre, Date releaseDate, String artist, BigDecimal price) {
        this.itemID = itemID;
        this.title = title;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.artist = artist;
        this.price = price;
    }

    MusicItem(String title, String genre, Date releaseDate, String artist, BigDecimal price) {
        this.itemID = UUID.randomUUID().toString().replace("-", "");
        this.title = title;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.artist = artist;
        this.price = price;
    }

    String getItemID() {
        return itemID;
    }

    String getTitle() {
        return title;
    }

    String getGenre() {
        return genre;
    }

    Date getReleaseDate() {
        return releaseDate;
    }

    String getArtist() {
        return artist;
    }

    BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "MusicItem{" +
                "itemID='" + itemID + '\'' +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", releaseDate=" + releaseDate +
                ", artist='" + artist + '\'' +
                ", price=" + price +
                '}';
    }
}
