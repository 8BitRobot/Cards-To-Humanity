package endpoints;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import storage.Card;
import storage.DatabaseStorage;

public class GetCards implements Handler {
    private Gson gson;
    private DatabaseStorage databaseStorage;

    public GetCards(Gson gson, DatabaseStorage databaseStorage) {
        this.gson = gson;
        this.databaseStorage = databaseStorage;
    }

    public void handle(Context ctx) {
        String tagged_with = ctx.queryParam("tagged_with");
        int top = Integer.parseInt(ctx.queryParam("top", "100"));
        String title_contains = ctx.queryParam("title_contains");
        String caption_contains = ctx.queryParam("caption_contains");

        Card[] cards = databaseStorage.getCards(tagged_with, top, title_contains, caption_contains);
        CardsArray response = new CardsArray(cards);

        ctx.contentType("application/json");
        ctx.result("for(;;);" + gson.toJson(response, CardsArray.class));
    }

    private class CardsArray {
        private Card[] result;

        CardsArray(Card[] result) {
            this.result = result;
        }
    }
}
