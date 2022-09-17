import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import ru.netology.Basket;
import ru.netology.ClientLog;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);

        String[] products = {"Батон белый", "Бананы (1 кг)", "Шоколад Milka", "Чизкейк", "Кофе"};
        int[] prices = {45, 90, 110, 395, 434};
        int[] inBasket = new int[products.length];

        Basket cart = new Basket(products, prices, inBasket);
        ClientLog clientLog = new ClientLog();

        File basketTxt = new File("basket.txt");
        File logCsv = new File("log.csv");
        File basketJson = new File("basket.json");
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        if (!basketTxt.createNewFile()) {
            cart = Basket.loadFromTxtFile(basketTxt, products);
        }

        if (!basketJson.createNewFile()) {
            try (JsonReader reader = new JsonReader(new FileReader(basketJson))) {
                cart = gson.fromJson(reader, Basket.class);
                System.out.println("Корзина восстановлена из файла.");
                System.out.println("Продолжим покупки!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            cart.printCart();
        }

        System.out.println("Список возможных товаров для покупки:");

        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + " " + products[i] + " " + prices[i] + " руб/шт");
        }

        while (true) {
            System.out.println("Выберите товар и количество или введите `end`");
            String input = scanner.nextLine();
            if (input.equals("end")) {
                cart.saveTxt(basketTxt);
                break;
            }
            String[] parts = input.split(" ");
            int productNum = Integer.parseInt(parts[0]) - 1;
            int amount = Integer.parseInt(parts[1]);
            clientLog.log(productNum, amount);
            cart.addToCart(productNum, amount);

            try (FileWriter jsonWriter = new FileWriter(basketJson)) {
                jsonWriter.write(gson.toJson(cart));
                jsonWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cart.printCart();
        logCsv.createNewFile();
        clientLog.exportAsCSV(logCsv);
    }
}