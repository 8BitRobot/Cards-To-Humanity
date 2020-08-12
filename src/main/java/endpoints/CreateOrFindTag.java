package endpoints;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import storage.DatabaseStorage;

public class CreateOrFindTag implements Handler {
    private DatabaseStorage databaseStorage;

    public CreateOrFindTag(DatabaseStorage databaseStorage) {
        this.databaseStorage = databaseStorage;
    }

    public void handle(Context ctx) {
        String content = ctx.formParam("content", String.class).get();
        ctx.contentType("text/plain");
        ctx.result(String.valueOf(databaseStorage.createTagOrFindExisting(content, System.currentTimeMillis() / 1000)));
    }
}
