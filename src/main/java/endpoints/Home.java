package endpoints;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class Home implements Handler {
    public void handle(Context ctx) {
        ctx.redirect("/home.html");
    }
}
