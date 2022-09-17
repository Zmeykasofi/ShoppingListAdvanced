import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.netology.Basket;
import ru.netology.ClientLog;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        boolean loading = true;
        String loadingFileName = null;
        String loadingFileFormat = null;

        boolean saving = true;
        String savingFileName = null;
        String savingFileFormat = null;

        boolean logging = true;
        String loggingFileName = null;

        try {
            File xmlFile = new File("shop.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList loadingList = doc.getElementsByTagName("load");
            for (int i = 0; i < loadingList.getLength(); i++) {
                Node node = loadingList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    loading = Boolean.parseBoolean(eElement.getElementsByTagName("enabled")
                            .item(0).getTextContent());
                    loadingFileName = eElement.getElementsByTagName("fileName")
                            .item(0).getTextContent();
                    loadingFileFormat = eElement.getElementsByTagName("format")
                            .item(0).getTextContent();
                }
            }

            NodeList savingList = doc.getElementsByTagName("save");
            for (int i = 0; i < savingList.getLength(); i++) {
                Node node = savingList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    saving = Boolean.parseBoolean(eElement.getElementsByTagName("enabled")
                            .item(0).getTextContent());
                    savingFileName = eElement.getElementsByTagName("fileName")
                            .item(0).getTextContent();
                    savingFileFormat = eElement.getElementsByTagName("format")
                            .item(0).getTextContent();
                }
            }

            NodeList loggingList = doc.getElementsByTagName("log");
            for (int i = 0; i < savingList.getLength(); i++) {
                Node node = loggingList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    logging = Boolean.parseBoolean(eElement.getElementsByTagName("enabled")
                            .item(0).getTextContent());
                    loggingFileName = eElement.getElementsByTagName("fileName")
                            .item(0).getTextContent();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        String[] products = {"Батон белый", "Бананы (1 кг)", "Шоколад Milka", "Чизкейк", "Кофе"};
        int[] prices = {45, 90, 110, 395, 434};
        int[] inBasket = new int[products.length];

        Basket cart = new Basket(products, prices, inBasket);
        ClientLog clientLog = new ClientLog();

        File basketTxt = new File("basket.txt");
        if (loadingFileName == null) throw new AssertionError();
        File logCsv = new File("log.csv");
        if (loggingFileName == null) throw new AssertionError();
        File basketJson = new File("basket.json");
        if (savingFileName == null) throw new AssertionError();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        if (loading && loadingFileFormat.equals("json")) {
            if (!basketJson.createNewFile()) {
                try (JsonReader reader = new JsonReader(new FileReader(basketJson))) {
                    cart = gson.fromJson(reader, Basket.class);
                    System.out.println("Корзина восстановлена из файла.");
                    cart.printCart();
                    System.out.println("Продолжим покупки!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (loading && loadingFileFormat.equals("txt")){
            if (!basketTxt.createNewFile()) {
                cart = Basket.loadFromTxtFile(basketTxt, products);
                System.out.println("Корзина восстановлена из файла.");
                cart.printCart();
                System.out.println("Продолжим покупки!");
            }
        }

        System.out.println("Список возможных товаров для покупки:");

        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + " " + products[i] + " " + prices[i] + " руб/шт");
        }

        while (true) {
            System.out.println("Выберите товар и количество или введите `end`");
            String input = scanner.nextLine();
            if (input.equals("end")) {
                break;
            }
            String[] parts = input.split(" ");
            int productNum = Integer.parseInt(parts[0]) - 1;
            int amount = Integer.parseInt(parts[1]);
            clientLog.log(productNum, amount);
            cart.addToCart(productNum, amount);

            if (savingFileFormat != null) {
                if (saving && savingFileFormat.equals("json")) {
                    try (FileWriter jsonWriter = new FileWriter(basketJson)) {
                        jsonWriter.write(gson.toJson(cart));
                        jsonWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (saving && savingFileFormat.equals("txt")) {
                    cart.saveTxt(basketTxt);
                }
            }
        }

        logCsv.createNewFile();
        if (logging) {
            clientLog.exportAsCSV(logCsv);
        }
        cart.printCart();
    }
}