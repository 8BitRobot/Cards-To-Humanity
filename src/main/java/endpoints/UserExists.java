package endpoints;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import storage.DatabaseStorage;

public class UserExists implements Handler {
    private Gson gson;
    private DatabaseStorage databaseStorage;

    public UserExists(Gson gson, DatabaseStorage databaseStorage) {
        this.gson = gson;
        this.databaseStorage = databaseStorage;
    }

    public void handle(Context ctx) {
        String username_or_email = ctx.queryParam("username_or_email");
        boolean exists = false;
        if (username_or_email != null && databaseStorage.userExists(username_or_email) > 0) {
            exists = true;
        }
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("result", exists);
        ctx.contentType("application/json");
        ctx.result("for(;;);" + gson.toJson(jsonResponse));
    }
}
