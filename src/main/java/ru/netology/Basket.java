package ru.netology;

import java.io.*;
import java.util.Arrays;

public class Basket {

    protected String[] products;
    protected int[] prices;
    protected int[] inBasket;

    protected int sum = 0;
    public Basket(String[] products, int[] prices, int[] inBasket) {
        this.products = products;
        this.prices = prices;
        this.inBasket = inBasket;
    }
    public static Basket loadFromTxtFile(File textFile, String[] products) {
        Basket toRead = null;
        if (textFile.exists() & textFile.length() != 0) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(textFile))) {
                int count = 0;
                int size = products.length;
                products = new String[size];
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

    public void addToCart(int productNum, int amount) {
        inBasket[productNum] += amount;
        sum += prices[productNum] * inBasket[productNum];
    }

    public void printCart() {
        System.out.println("Ваша корзина:");
        for (int c = 0; c < products.length; c++) {
            if (inBasket[c] > 0) {
                System.out.println(products[c] + ", " + inBasket[c] + " шт., " + (inBasket[c] * prices[c]) + " руб. в сумме");
            }
        }
        System.out.println("Итого к оплате: " + sum + " руб.");
    }
    public void saveTxt(File textFile) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(textFile))) {
            for (int i = 0; i < products.length; i++) {
                if (inBasket[i] > 0) {
                    out.write(products[i] + "/" + inBasket[i] + "/" + prices[i] + "\r\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Basket{" +
                "products=" + Arrays.toString(products) +
                ", prices=" + Arrays.toString(prices) +
                ", inBasket=" + Arrays.toString(inBasket) +
                ", sum=" + sum +
                '}';
    }
}