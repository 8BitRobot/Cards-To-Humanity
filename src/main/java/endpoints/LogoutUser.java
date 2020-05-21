package endpoints;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class LogoutUser implements Handler {
    public void handle(Context ctx) {
        ctx.req.getSession().invalidate();
        ctx.status(200);
    }
}
