package endpoints;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import storage.DatabaseStorage;

public class CreateCard implements Handler {
    private DatabaseStorage databaseStorage;

    public CreateCard(DatabaseStorage databaseStorage) {
        this.databaseStorage = databaseStorage;
    }

    public void handle(Context ctx) {
        int user_id = 1;
        if (ctx.sessionAttributeMap().containsKey("user_id")) {
            user_id = ctx.sessionAttribute("user_id");
        }

        int media_id = ctx.formParam("media_id", Integer.class).get();
        String title = ctx.formParam("title", String.class).get();
        String caption = ctx.formParam("caption", String.class).get();

        int card_id = databaseStorage.createCard(user_id, media_id, title, caption);

        if (card_id < 0) {
            ctx.result("Card could not be created.");
            ctx.status(400);
        }
        else {
            ctx.result(String.valueOf(card_id));
            ctx.status(200);
        }
    }
}
