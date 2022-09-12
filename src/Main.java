import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Scanner scanner = new Scanner(System.in);

        String[] products = {"Батон белый", "Бананы (1 кг)", "Шоколад Milka", "Чизкейк", "Кофе"};
        int[] prices = {45, 90, 110, 395, 434};
        int[] inBasket = new int[products.length];

        Basket cart = new Basket(products, prices, inBasket);

        File basketBin = new File("basket.bin");

        if (!basketBin.createNewFile()) {
            cart = Basket.loadFromBinFile(basketBin);
            System.out.println("Корзина восстановлена из файла.");
            System.out.println("Продолжим покупки!");
        }

        System.out.println("Список возможных товаров для покупки:");

        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + " " + products[i] + " " + prices[i] + " руб/шт");
        }

        while (true) {
            System.out.println("Выберите товар и количество или введите `end`");
            String input = scanner.nextLine();
            if (input.equals("end")) {
                cart.saveBin(basketBin);
                break;
            }
            String[] parts = input.split(" ");
            int productNum = Integer.parseInt(parts[0]) - 1;
            int amount = Integer.parseInt(parts[1]);
            cart.addToCart(productNum, amount);
        }
        cart.printCart();
    }
}
