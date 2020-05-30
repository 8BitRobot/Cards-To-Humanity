package endpoints;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import storage.Card;
import storage.DatabaseStorage;

public class GetCard implements Handler {
    private Gson gson;
    private DatabaseStorage databaseStorage;

    public GetCard(Gson gson, DatabaseStorage databaseStorage) {
        this.gson = gson;
        this.databaseStorage = databaseStorage;
    }

    public void handle(Context ctx) {
        int card_id = ctx.queryParam("card_id", Integer.class).get();

        Card card = databaseStorage.getCard(card_id);

        ctx.contentType("application/json");
        ctx.result("for(;;);" + gson.toJson(card, Card.class));
    }
}
