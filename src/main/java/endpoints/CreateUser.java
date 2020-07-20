package endpoints;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import security.HashedPassword;
import storage.DatabaseStorage;

import java.util.Arrays;

public class CreateUser implements Handler {
    private DatabaseStorage databaseStorage;

    public CreateUser(DatabaseStorage databaseStorage) {
        this.databaseStorage = databaseStorage;
    }

    public void handle(Context ctx) {
        String username = ctx.formParam("username");
        String display_name = ctx.formParam("display_name");
        char[] password = ctx.formParam("password").toCharArray();
        String email = ctx.formParam("email");

        if (username.equals("") || password.equals("") || email.equals("")) {
            ctx.status(400);
            ctx.contentType("text/plain");
            ctx.result("The username, password, and email fields are mandatory.");
            Arrays.fill(password, '\0');
            return;
        }

        if (databaseStorage.userExists(username) != -1) {
            ctx.status(400);
            ctx.contentType("text/plain");
            ctx.result("A user with this username already exists. Please choose a different username.");
            Arrays.fill(password, '\0');
            return;
        }

        if (databaseStorage.userExists(email) != -1) {
            ctx.status(400);
            ctx.contentType("text/plain");
            ctx.result("A user with this email already exists. Please choose a different email.");
            Arrays.fill(password, '\0');
            return;
        }

        if (password.length < 8) {
            ctx.status(400);
            ctx.contentType("text/plain");
            ctx.result("Your password is too short. Passwords should be at least 8 characters long.");
            Arrays.fill(password, '\0');
            return;
        }

        if (display_name.length() == 0) {
            display_name = username;
        }

        HashedPassword hashedPassword = new HashedPassword(password);
        databaseStorage.createUser(username, display_name, hashedPassword.getHash(), hashedPassword.getSalt(), email);
        hashedPassword.erase();

        ctx.status(200);
    }
}
