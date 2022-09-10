import java.io.*;

public class Basket {

    protected String[] products;
    protected int[] prices;
    protected int[] basket;

    public Basket(String[] products, int[] prices, int[] basket) {
        this.products = products;
        this.prices = prices;
        this.basket = basket;
    }


    public Basket(String[] products, int[] prices) throws IOException {
        Basket basketTxt = loadFromTxtFile(new File("basket.txt"));
        if (basketTxt != null) {
            this.products = basketTxt.getProducts();
            this.prices = basketTxt.getPrices();
            this.basket = basketTxt.getBasket();
        } else {
            this.products = products;
            this.prices = prices;
            this.basket = new int[products.length];
        }
    }

    public static Basket loadFromTxtFile(File textFile) throws IOException {
        Basket toRead = null;
        if (textFile.exists() & textFile.length() != 0) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(textFile))) {
                int count = 0;
                int size = 5;
                String[] products = new String[size];
                int[] basket = new int[size];
                int[] price = new int[size];
                String read;
                while ((read = bufferedReader.readLine()) != null) {
                    String[] strings = read.split("/");
                    products[count] = strings[0];
                    basket[count] = Integer.parseInt(strings[1]);
                    price[count] = Integer.parseInt(strings[2]);
                    count++;
                }
                toRead = new Basket(products, price, basket);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return toRead;
    }

    public String[] getProducts() {
        return products;
    }

    public int[] getPrices() {
        return prices;
    }

    public int[] getBasket() {
        return basket;
    }

    public void addToCart(int productNum, int amount) {
        basket[productNum] = basket[productNum] + amount;
    }

    public void printCart() {
        int sum = 0;
        System.out.println("Ваша корзина:");
        for (int c = 0; c < products.length; c++) {
            int toPay = basket[c] * prices[c];
            if (basket[c] > 0) {
                System.out.println(products[c] + ", " + basket[c] + " шт., " + toPay + " руб. в сумме");
                sum += toPay;
            }
        }
        System.out.println("Итого к оплате: " + sum + " руб.");
    }

    public void saveTxt(File textFile) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(textFile))) {

            for (int i = 0; i < products.length; i++) {
                out.write(products[i] + "/" + basket[i] + "/" + prices[i] + "\r\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

