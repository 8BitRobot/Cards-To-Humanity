import com.github.javafaker.Faker;
import security.HashedPassword;
import storage.DatabaseStorage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class PopulateDummyDatabase {
    private static int users_to_create = 100;
    private static int cards_to_generate = 1000;
    private static int tags_to_generate = 100;
    private static int likes_per_user = 100;

    /*
    private static int users_to_create = 10;
    private static int cards_to_generate = 10;
    private static int tags_to_generate = 100;
    private static int likes_per_user = 100;
    */

    public static void main(String[] args) {
        DatabaseStorage database = new DatabaseStorage();
        Faker faker = new Faker();

        // Create fake users.
        System.out.println("Creating " + users_to_create + " fake users...");
        List<Integer> user_ids = new ArrayList<>();
        for (int i = 0; i < users_to_create; i++) {
            if (i % 10 == 0) {
                System.out.println(((i / (float) users_to_create) * 100.0) + "%");
            }
            String username = faker.name().username();
            String password = faker.harryPotter().spell();
            String display_name = faker.name().fullName();
            String email = faker.internet().emailAddress();
            HashedPassword hashedPassword = new HashedPassword(password.toCharArray());
            int user_id = database.createUser(username, display_name, hashedPassword.getHash(), hashedPassword.getSalt(), email);
            if (user_id == -1) {
                System.out.println("Failed to create user.");
                continue;
            }
            user_ids.add(user_id);
        }

        // Create fake cards with fake images.
        Random random = new Random();
        String[] fileTypes = {"jpeg", "png", "bmp", "gif"};
        System.out.println("Generating " + cards_to_generate + " fake cards...");
        List<Integer> card_ids = new ArrayList<>();
        for (int i = 0; i < cards_to_generate; i++) {
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
            int user_id = user_ids.get(random.nextInt(user_ids.size()));
            int card_id = database.createCard(user_id, media_id, title, caption);
            if (card_id == -1) {
                System.out.println("Failed to create card in database.");
                continue;
            }
            card_ids.add(card_id);
        }

        // Create fake tags.
        System.out.println("Creating " + tags_to_generate + " random tags...");
        List<Integer> tag_ids = new ArrayList<>();
        for (int i = 0; i < tags_to_generate; i++) {
            if (i % 1000 == 0) {
                System.out.println(((i / (float) tags_to_generate) * 100.0) + "%");
            }
            String content = faker.cat().breed();
            int tag_id = database.createTagOrFindExisting(content);
            if (tag_id == -1) {
                System.out.println("Failed to create tag.");
                continue;
            }
            tag_ids.add(tag_id);
        }

        // Tag each card with a random number of randomly-chosen tags.
        System.out.println("Tagging cards randomly...");
        for (int i = 0; i < card_ids.size(); i++) {
            if (i % 100 == 0) {
                System.out.println(((i / (float) card_ids.size()) * 100.0) + "%");
            }
            int card_id = card_ids.get(i);
            for (int j = 0; j < random.nextInt(10); j++) {
                int tag_id = tag_ids.get(random.nextInt(tag_ids.size()));
                int tagging_id = database.tagCard(card_id, tag_id);
                if (tagging_id == -1) {
                    System.out.println("Failed to tag card.");
                }
            }
        }

        // Randomly like cards.
        System.out.println("Liking cards randomly...");
        Map<Integer, List<Integer>> likes_done = new HashMap<>();
        for (int i = 0; i < user_ids.size(); i++) {
            int user_id = user_ids.get(i);
            for (int j = 0; j < likes_per_user; j++) {
                int card_id = card_ids.get(random.nextInt(card_ids.size()));
                if (likes_done.containsKey(user_id) && likes_done.get(user_id).contains(card_id)) {
                    continue;
                }
                database.likeCard(card_id, user_id);
                if (likes_done.containsKey(user_id)) {
                    likes_done.get(user_id).add(card_id);
                }
                else {
                    List<Integer> temp = new ArrayList<>();
                    temp.add(card_id);
                    likes_done.put(user_id, temp);
                }
            }
        }
    }
}
