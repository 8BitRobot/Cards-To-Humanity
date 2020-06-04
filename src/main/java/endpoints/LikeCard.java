package endpoints;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import storage.DatabaseStorage;

public class LikeCard implements Handler {
    private DatabaseStorage databaseStorage;

    public LikeCard(DatabaseStorage databaseStorage) {
        this.databaseStorage = databaseStorage;
    }

    public void handle(Context ctx) {
        int card_id = ctx.formParam("card_id", Integer.class).get();

        if (!ctx.sessionAttributeMap().containsKey("user_id")) {
            ctx.status(400);
            ctx.contentType("text/plain");
            ctx.result("User is not logged in. Could not like card.");
            return;
        }

        int user_id = ctx.sessionAttribute("user_id");

        int like_id = databaseStorage.likeCard(card_id, user_id);

        if (like_id > 0) {
            ctx.status(200);
            return;
        }
        ctx.status(400);
        ctx.contentType("text/plain");
        ctx.result("Failed to like card.");
    }
}
