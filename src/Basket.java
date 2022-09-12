import java.io.*;

public class Basket implements Serializable {

    protected String[] products;
    protected int[] prices;
    protected int[] inBasket;

    public Basket(String[] products, int[] prices, int[] inBasket) {
        this.products = products;
        this.prices = prices;
        this.inBasket = inBasket;
    }


    public static Basket loadFromBinFile(File file) {
        Basket toRead = null;
        if (file.exists() & file.length() != 0) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))) {
                toRead = (Basket) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                toRead = null;
            }
        }
        return toRead;
    }

    public void addToCart(int productNum, int amount) {
        inBasket[productNum] = inBasket[productNum] + amount;
    }

    public void printCart() {
        int sum = 0;
        System.out.println("Ваша корзина:");
        for (int c = 0; c < products.length; c++) {
            int toPay = inBasket[c] * prices[c];
            if (inBasket[c] > 0) {
                System.out.println(products[c] + ", " + inBasket[c] + " шт., " + toPay + " руб. в сумме");
                sum += toPay;
            }
        }
        System.out.println("Итого к оплате: " + sum + " руб.");
    }

    public void saveBin(File file) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            objectOutputStream.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

