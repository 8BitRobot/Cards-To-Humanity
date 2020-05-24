package endpoints;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import storage.DatabaseStorage;
import storage.Media;

public class GetMedia implements Handler {
    private DatabaseStorage databaseStorage;

    public GetMedia(DatabaseStorage databaseStorage) {
        this.databaseStorage = databaseStorage;
    }

    public void handle(Context ctx) {
        String media_id_string = ctx.queryParam("media_id");
        if (media_id_string == null) {
            ctx.result("You need to provide media_id for this query.");
            ctx.status(400);
            return;
        }

        int media_id = Integer.parseInt(media_id_string);
        Media media = databaseStorage.getMedia(media_id);

        ctx.contentType(media.getMediaMimeType());
        ctx.result(media.getMediaContent());
        ctx.status(200);
    }
}
