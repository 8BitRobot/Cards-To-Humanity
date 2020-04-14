import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(4567);
        app.get("/", ctx -> ctx.result("The CareCards application is running!"));
    }
}
