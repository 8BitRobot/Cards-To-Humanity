package endpoints;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import storage.DatabaseStorage;

public class TagCard implements Handler {
    private DatabaseStorage databaseStorage;

    public TagCard(DatabaseStorage databaseStorage) {
        this.databaseStorage = databaseStorage;
    }

    public void handle(Context ctx) {
        int card_id = ctx.formParam("card_id", Integer.class).get();
        int tag_id = ctx.formParam("tag_id", Integer.class).get();

        int tagging_id = databaseStorage.tagCard(card_id, tag_id);
        if (tagging_id > 0) {
            ctx.status(200);
            return;
        }
        ctx.contentType("text/plain");
        ctx.result("Failed to tag card.");
        ctx.status(400);
    }
}
