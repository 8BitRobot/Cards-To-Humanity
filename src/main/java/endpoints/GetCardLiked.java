package endpoints;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import storage.DatabaseStorage;

public class GetCardLiked implements Handler {
    private Gson gson;
    private DatabaseStorage databaseStorage;

    public GetCardLiked(Gson gson, DatabaseStorage databaseStorage) {
        this.gson = gson;
        this.databaseStorage = databaseStorage;
    }

    public void handle(Context ctx) {
        // Get the user id. If there is no user id, default to user id 1 (the anonymous user).
        int user_id = 1;
        if (ctx.sessionAttributeMap().containsKey("user_id")) {
            user_id = ctx.sessionAttribute("user_id");
        }

        // Get the form parameters.
        int card_id = ctx.queryParam("card_id", Integer.class).get();

        // Query the database.
        boolean card_liked = databaseStorage.getCardLiked(user_id, card_id);

        // Build the response.
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("result", card_liked);
        ctx.contentType("application/json");
        ctx.result("for(;;);" + gson.toJson(jsonResponse));
    }
}