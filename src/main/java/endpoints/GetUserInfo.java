package endpoints;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import storage.DatabaseStorage;
import storage.UserInfo;

public class GetUserInfo implements Handler {
    private Gson gson;
    private DatabaseStorage databaseStorage;

    public GetUserInfo(Gson gson, DatabaseStorage databaseStorage) {
        this.gson = gson;
        this.databaseStorage = databaseStorage;
    }

    public void handle(Context ctx) {
        if (!ctx.sessionAttributeMap().containsKey("user_id") || (Integer) ctx.sessionAttribute("user_id") == 1) {
            ctx.status(400);
            ctx.contentType("text/plain");
            ctx.result("The user must be logged in to retrieve user info. The anonymous user does not have user info.");
            return;
        }

        int user_id = ctx.sessionAttribute("user_id");

        UserInfo userInfo = databaseStorage.getUserInfo(user_id);

        if (userInfo == null) {
            ctx.status(400);
            ctx.contentType("text/plain");
            ctx.result("Query failed.");
            return;
        }

        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result("for(;;);" + gson.toJson(userInfo, UserInfo.class));
    }
}
