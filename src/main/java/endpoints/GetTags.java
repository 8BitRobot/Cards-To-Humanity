package endpoints;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import storage.Tag;
import storage.DatabaseStorage;

public class GetTags implements Handler {
    private Gson gson;
    private DatabaseStorage databaseStorage;

    public GetTags(Gson gson, DatabaseStorage databaseStorage) {
        this.gson = gson;
        this.databaseStorage = databaseStorage;
    }

    public void handle(Context ctx) {
        String content_contains = ctx.queryParam("content_contains", String.class).get();
        int top = ctx.queryParam("top", Integer.class, "100").get();

        Tag[] tags = databaseStorage.getTags(content_contains, top);
        TagsArray tagsArray = new TagsArray(tags);

        ctx.contentType("application/json");
        ctx.result("for(;;);" + gson.toJson(tagsArray, TagsArray.class));
    }

    private class TagsArray {
        private Tag[] result;

        TagsArray(Tag[] result) {
            this.result = result;
        }
    }
}
