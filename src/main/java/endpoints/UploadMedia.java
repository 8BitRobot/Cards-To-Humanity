package endpoints;

import java.util.UUID;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UploadedFile;
import storage.DatabaseStorage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;

public class UploadMedia implements Handler {
    private DatabaseStorage databaseStorage;
    private S3Client s3Client;
    private String s3BucketName;

    public UploadMedia(DatabaseStorage databaseStorage, String s3BucketName) {
        this.databaseStorage = databaseStorage;
        this.s3BucketName = s3BucketName;
        this.s3Client = S3Client.builder().region(Region.US_WEST_1).build();
    }

    public void handle(Context ctx) {
        // Get the uploaded file and validate it.
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
            // Convert the uploaded image to JPEG.
            BufferedImage image = ImageIO.read(inputStream);
            BufferedImage image_jpeg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            image_jpeg.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
            ByteArrayOutputStream jpeg_data_stream = new ByteArrayOutputStream();
            ImageIO.write(image_jpeg, "jpeg", jpeg_data_stream);
            byte[] jpeg_image_data = jpeg_data_stream.toByteArray();
            // Save the JPEG data to Amazon S3.
            UUID uuid = UUID.randomUUID();
            String uuidString = uuid.toString() + ".jpeg";
            PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(s3BucketName).key(uuidString).acl("public-read").contentType("image/jpeg").build();
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
            int media_id = databaseStorage.createMedia("https://" + s3BucketName + ".s3-us-west-1.amazonaws.com/" + uuidString);
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
