package endpoints;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import storage.DatabaseStorage;

public class SignUpEmail implements Handler {
    private DatabaseStorage databaseStorage;

    public SignUpEmail(DatabaseStorage databaseStorage) {
        this.databaseStorage = databaseStorage;
    }

    public void handle(Context ctx) {
        String email_address = ctx.formParam("email_address", String.class).get();
        String first_name = ctx.formParam("first_name", String.class).get();
        String last_name = ctx.formParam("last_name", String.class).get();

        int signup_id = databaseStorage.signUpEmail(email_address, first_name, last_name, System.currentTimeMillis() / 1000L);

        if (signup_id > 0) {
            ctx.status(200);
            return;
        }

        ctx.status(400);
        ctx.contentType("text/plain");
        ctx.result("Failed to sign up email.");
    }
}
