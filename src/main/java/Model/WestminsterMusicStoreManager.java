package Model;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.Decimal128;

import java.math.BigDecimal;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

// https://mongodb.github.io/mongo-java-driver/3.4/driver/getting-started/quick-start/
public class WestminsterMusicStoreManager implements StoreManager {
    private ArrayList<MusicItem> items = new ArrayList<>();
    private MongoCollection<Document> musicItemCollection;

    public WestminsterMusicStoreManager(MongoDatabase database) {
        musicItemCollection = database.getCollection("MusicItem");

        musicItemCollection.find().forEach((Block<Document>) document -> {
            Document date = (Document) document.get("releaseDate");
            if(document.getString("type").equals("Vinyl")){
                items.add(
                        new Vinyl(
                                document.getString("itemID"),
                                document.getString("title"),
                                document.getString("genre"),
                                new Date(date.getInteger("year"),
                                        date.getInteger("month"),
                                        date.getInteger("day")),
                                document.getString("artist"),
                                ((Decimal128) document.get("price")).bigDecimalValue(),
                                document.getInteger("speed"),
                                document.getDouble("diameter")
                        )
                );
            }
            else{
                items.add(
                        new CD(
                                document.getString("itemID"),
                                document.getString("title"),
                                document.getString("genre"),
                                new Date(date.getInteger("year"),
                                        date.getInteger("month"),
                                        date.getInteger("day")),
                                document.getString("artist"),
                                ((Decimal128) document.get("price")).bigDecimalValue(),
                                (ArrayList<String>) document.get("songs"),
                                document.getInteger("totalDuration")

                        )
                );
            }
        });
    }

    // TODO: This need to hold max of 1000 items
    @Override
    public void addItem(MusicItem item) {
        if(items.size() > 1000){
            throw new ArrayStoreException("There is no space to store more than 1000 items");
        }

        this.items.add(item);
        Document doc = new Document("itemID", item.getItemID())
                .append("title", item.getTitle())
                .append("genre", item.getGenre())
                .append("releaseDate", new Document("year", item.getReleaseDate().getYear())
                        .append("month", item.getReleaseDate().getMonth())
                        .append("day", item.getReleaseDate().getDay())
                ).append("artist", item.getArtist())
                .append("price", item.getPrice());
        if(item.getClass().getName().equals("Model.Vinyl")){
            Vinyl vinyl = (Vinyl) item;
            doc.append("speed", vinyl.getSpeed())
                    .append("diameter", vinyl.getDiameter())
                    .append("type", "Vinyl");
        }
        else{
            CD cd = (CD) item;
            doc.append("songs", cd.getSongs())
                    .append("totalDuration", cd.getTotalDuration())
                    .append("type", "CD");
        }
        musicItemCollection.insertOne(doc);
    }

    @Override
    public boolean deleteItem(String itemId) {
        for (MusicItem item : items) {
            if (item.getItemID().equals(itemId)) {
                items.remove(item);
                musicItemCollection.deleteOne(eq("itemID", itemId));
                return true;
            }
        }
        return false;
    }

    @Override
    public void sortItems() {
        items.sort((item1, item2) -> item1.getTitle().compareToIgnoreCase(item2.getTitle()));
    }

    @Override
    public void sellItem(String itemId) {

    }

    @Override
    public void updateSalesLog() {

    }
}
