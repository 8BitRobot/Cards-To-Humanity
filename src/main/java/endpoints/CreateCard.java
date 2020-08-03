package endpoints;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UploadedFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import storage.DatabaseStorage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class CreateCard implements Handler {

    private DatabaseStorage databaseStorage;
    private String s3BucketName;
    private S3Client s3Client;

    public CreateCard(DatabaseStorage databaseStorage, String s3BucketName) {
        this.databaseStorage = databaseStorage;
        this.s3BucketName = s3BucketName;
        this.s3Client = S3Client.builder().region(Region.US_WEST_1).build();
    }

    public void handle(Context ctx) {
        // Get the user id. If there is no user id, default to user id 1 (the anonymous user).
        int user_id = 1;
        if (ctx.sessionAttributeMap().containsKey("user_id")) {
            user_id = ctx.sessionAttribute("user_id");
        }

        // Get the form parameters.
        String title = ctx.formParam("title", String.class).get();
        String caption = ctx.formParam("caption", String.class).get();
        String[] tags = ctx.formParam("tags", String.class).get().split(",");


        // Get the uploaded media file and validate it, then upload it as a JPEG to Amazon S3.
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
        int media_id;
        try {
            // Convert the uploaded image to JPEG.
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                ctx.result("Uploaded image was invalid.");
                ctx.status(400);
                return;
            }
            BufferedImage image_jpeg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            image_jpeg.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
            ByteArrayOutputStream jpeg_data_stream = new ByteArrayOutputStream();
            ImageIO.write(image_jpeg, "jpeg", jpeg_data_stream);
            byte[] jpeg_image_data = jpeg_data_stream.toByteArray();
            // Save the JPEG data to Amazon S3.
            UUID uuid = UUID.randomUUID();
            String uuidString = uuid.toString() + ".jpeg";
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                                          .bucket(s3BucketName)
                                                                          .key(uuidString)
                                                                          .acl("public-read")
                                                                          .contentType("image/jpeg")
                                                                          .contentDisposition("attachment;filename=\"" + title + ".jpeg\"")
                                                                          .build();
            try {
                s3Client.putObject(putObjectRequest, RequestBody.fromBytes(jpeg_image_data));
            }
            catch (Exception exception) {
                exception.printStackTrace();
                ctx.result("S3 upload failed.");
                ctx.status(400);
                return;
            }
            // Save the S3 URL of the image to the database.
            media_id = databaseStorage.createMedia("https://" + s3BucketName + ".s3-us-west-1.amazonaws.com/" + uuidString, System.currentTimeMillis() / 1000);
            if (media_id < 0) {
                ctx.result("Database failed to create media.");
                ctx.status(400);
                return;
            }
        }
        catch (IOException exception) {
            exception.printStackTrace();
            ctx.status(400);
            return;
        }

        long creation_time = System.currentTimeMillis() / 1000;
        int card_id = databaseStorage.createCard(user_id, media_id, title, caption, creation_time);

        if (card_id < 0) {
            ctx.result("Card could not be created.");
            ctx.status(400);
        }
        else {
            for (String tag : tags) {
                databaseStorage.tagCard(card_id, databaseStorage.createTagOrFindExisting(tag, creation_time));
            }
            ctx.result(String.valueOf(card_id));
            ctx.status(200);
        }
    }
}
