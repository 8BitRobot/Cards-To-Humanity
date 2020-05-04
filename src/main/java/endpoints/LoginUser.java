package endpoints;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import security.HashedPassword;
import security.PasswordSecurity;
import storage.DatabaseStorage;

public class LoginUser implements Handler {
    private DatabaseStorage databaseStorage;

    public LoginUser(DatabaseStorage databaseStorage) {
        this.databaseStorage = databaseStorage;
    }

    public void handle(Context ctx) {
        String username_or_email = ctx.formParam("username_or_email", String.class).get();
        char[] password = ctx.formParam("password", String.class).get().toCharArray();

        int user_id = databaseStorage.userExists(username_or_email);
        if (user_id == -1) {
            ctx.status(400);
            return;
        }

        HashedPassword hashedPassword = databaseStorage.getHashedPassword(user_id);
        if (hashedPassword == null) {
            ctx.status(400);
            return;
        }

        if (PasswordSecurity.validatePassword(password, hashedPassword)) {
            hashedPassword.erase();
            ctx.sessionAttribute("user_id", user_id);
            ctx.req.changeSessionId();
            ctx.status(200);
            return;
        }

        hashedPassword.erase();

        ctx.status(400);
    }
}
