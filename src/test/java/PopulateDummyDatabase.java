import com.github.javafaker.Faker;
import security.HashedPassword;
import storage.DatabaseStorage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class PopulateDummyDatabase {
    private static int users_to_create = 100;
    private static int cards_to_generate = 1000;

    public static void main(String[] args) {
        DatabaseStorage database = new DatabaseStorage();
        Faker faker = new Faker();

        // Create 1K fake users.
        System.out.println("Creating " + users_to_create + " fake users...");
        for (int i = 1; i < users_to_create; i++) {
            if (i % 100 == 0) {
                System.out.println(((i / (float) users_to_create) * 100.0) + "%");
            }
            String username = faker.name().username();
            String password = faker.harryPotter().spell();
            String display_name = faker.name().fullName();
            String email = faker.internet().emailAddress();
            HashedPassword hashedPassword = new HashedPassword(password.toCharArray());
            database.createUser(username, display_name, hashedPassword.getHash(), hashedPassword.getSalt(), email);
        }

        // Create 10K fake cards with fake images.
        Random random = new Random();
        String[] fileTypes = {"jpeg", "png", "bmp", "gif"};
        System.out.println("Generating " + cards_to_generate + " fake cards...");
        for (int i = 1; i < cards_to_generate; i++) {
            if (i % 100 == 0) {
                System.out.println(((i / (float) cards_to_generate) * 100.0) + "%");
            }
            // Generate fake image data in a variety of different image file formats.
            int width = random.nextInt(1000) + 100;
            int height = random.nextInt(1000) + 100;
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = bufferedImage.createGraphics();
            graphics2D.setColor(new Color((float) Math.random(), (float) Math.random(), (float) Math.random()));
            graphics2D.fillRect(0, 0, width, height);
            graphics2D.setColor(new Color((float) Math.random(), (float) Math.random(), (float) Math.random()));
            graphics2D.drawString(faker.hobbit().location(), 10, height / 2);
            graphics2D.dispose();
            ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();
            String fileType = fileTypes[random.nextInt(fileTypes.length)];
            try {
                ImageIO.write(bufferedImage, fileType, imageOutputStream);
            }
            catch (IOException exception) {
                exception.printStackTrace();
                continue;
            }
            int media_id = database.createMedia("image/" + fileType, imageOutputStream.toByteArray());
            if (media_id == -1) {
                System.out.println("Failed to create media in database.");
                continue;
            }
            // Generate the rest of the fake card.
            String title = faker.funnyName().name();
            String caption = faker.shakespeare().hamletQuote();
            int user_id = random.nextInt(users_to_create - 1) + 1; // Here, we make the dangerous assumption that user id's are contiguous and sequential. This is ONLY true for when this test is run on an empty database with a single thread. Do not use this assumption anywhere in production.
            int card_id = database.createCard(user_id, media_id, title, caption);
            if (card_id == -1) {
                System.out.println("Failed to create card in database.");
            }
        }
    }
}
