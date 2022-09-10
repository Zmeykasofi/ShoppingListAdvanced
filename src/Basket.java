import java.io.*;

public class Basket implements Serializable {

    protected String[] products;
    protected int[] prices;
    protected int[] basket;

    public Basket(String[] products, int[] prices) throws IOException, ClassNotFoundException {
        Basket basketBin = loadFromBinFile(new File("basket.bin"));
        if (basketBin != null) {
            this.products = basketBin.getProducts();
            this.prices = basketBin.getPrices();
            this.basket = basketBin.getBasket();
        } else {
            this.products = products;
            this.prices = prices;
            this.basket = new int[products.length];
        }
    }


    public static Basket loadFromBinFile(File file) throws IOException, ClassNotFoundException {
        Basket toRead = null;
        if (file.exists() & file.length() != 0) {
            var fis = new FileInputStream(file);
            var ois = new ObjectInputStream(fis);
            toRead = (Basket) ois.readObject();
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

    public void saveBin(File file) throws IOException {
        var fos = new FileOutputStream(file);
        var oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
    }
}

