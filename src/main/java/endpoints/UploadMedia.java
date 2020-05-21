package endpoints;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UploadedFile;
import org.eclipse.jetty.util.ByteArrayOutputStream2;
import storage.DatabaseStorage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UploadMedia implements Handler {
    private DatabaseStorage databaseStorage;

    public UploadMedia(DatabaseStorage databaseStorage) {
        this.databaseStorage = databaseStorage;
    }

    public void handle(Context ctx) {
        UploadedFile uploadedFile = ctx.uploadedFile("media_file");
        if (uploadedFile == null) {
            ctx.result("media_file parameter was not supplied in request.");
            ctx.status(400);
            return;
        }
        if (uploadedFile.getSize() > 15000000) {
            ctx.result("File larger than 15 MB.");
            ctx.status(400);
            return;
        }
        InputStream inputStream = uploadedFile.getContent();
        try {
            BufferedImage image = ImageIO.read(inputStream);
            ByteArrayOutputStream convertedImage = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", convertedImage);
            byte[] convertedImageData = convertedImage.toByteArray();
            int media_id = databaseStorage.createMedia("image/jpeg", convertedImageData);
            if (media_id < 0) {
                ctx.result("Database failed to create media.");
                ctx.status(400);
                return;
            }
            else {
                ctx.contentType("text/plain");
                ctx.result(String.valueOf(media_id));
                ctx.status(200);
            }
        }
        catch (IOException exception) {
            exception.printStackTrace();
            ctx.status(400);
            return;
        }
    }
}
