import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final OrderService service;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(OrderService service) {
        this.service = service;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Выберите пункт: ");
            try {
                switch (choice) {
                    case 1:
                        createOrder();
                        break;
                    case 2:
                        showOrders();
                        break;
                    case 3:
                        chooseDelivery();
                        break;
                    case 4:
                        assignCourier();
                        break;
                    case 5:
                        changeStatus();
                        break;
                    case 6:
                        running = false;
                        System.out.println("Работа завершена.");
                        break;
                    default:
                        System.out.println("Нет такого пункта меню.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== Служба доставки ===");
        System.out.println("1. Создать заказ");
        System.out.println("2. Показать заказы");
        System.out.println("3. Выбрать способ доставки");
        System.out.println("4. Назначить курьера");
        System.out.println("5. Изменить статус заказа");
        System.out.println("6. Выход");
    }

    private void createOrder() {
        String name = readText("Имя клиента: ");
        String phone = readText("Телефон: ");
        String address = readText("Адрес доставки: ");
        Order order = service.createOrder(new Customer(name, phone, address));

        do {
            String product = readText("Название товара: ");
            int quantity = readInt("Количество: ");
            double price = readDouble("Цена за штуку: ");
            try {
                order.addItem(new OrderItem(product, quantity, price));
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        } while (readText("Добавить ещё позицию? (да/нет): ").equalsIgnoreCase("да"));

        System.out.println("Создан заказ №" + order.getId());
    }

    private void showOrders() {
        if (service.getOrders().isEmpty()) {
            System.out.println("Заказов пока нет.");
            return;
        }
        for (Order order : service.getOrders()) {
            System.out.println(order);
        }
    }

    private void chooseDelivery() {
        Order order = service.findOrder(readInt("Номер заказа: "));
        List<DeliveryMethod> methods = service.getDeliveryMethods();
        for (int i = 0; i < methods.size(); i++) {
            DeliveryMethod m = methods.get(i);
            System.out.println((i + 1) + ". " + m.getName()
                    + ", стоимость " + m.calculateCost(order.getItemsCost())
                    + " руб., срок " + m.estimateDays() + " дн.");
        }
        int number = readInt("Выберите способ: ");
        if (number < 1 || number > methods.size()) {
            throw new IllegalArgumentException("Нет такого способа доставки");
        }
        order.setDeliveryMethod(methods.get(number - 1));
        System.out.println("Способ доставки выбран.");
    }

    private void assignCourier() {
        Order order = service.findOrder(readInt("Номер заказа: "));
        List<Courier> couriers = service.getCouriers();
        for (int i = 0; i < couriers.size(); i++) {
            System.out.println((i + 1) + ". " + couriers.get(i));
        }
        int number = readInt("Выберите курьера: ");
        if (number < 1 || number > couriers.size()) {
            throw new IllegalArgumentException("Нет такого курьера");
        }
        order.assignCourier(couriers.get(number - 1));
        System.out.println("Курьер назначен.");
    }

    private void changeStatus() {
        Order order = service.findOrder(readInt("Номер заказа: "));
        OrderStatus[] statuses = OrderStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.println((i + 1) + ". " + statuses[i]);
        }
        int number = readInt("Новый статус: ");
        if (number < 1 || number > statuses.length) {
            throw new IllegalArgumentException("Нет такого статуса");
        }
        order.changeStatus(statuses[number - 1]);
        System.out.println(order);
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Введите число (дробную часть через точку).");
            }
        }
    }

    private String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = scanner.nextLine().trim();
            if (!text.isEmpty()) {
                return text;
            }
            System.out.println("Поле не может быть пустым.");
        }
    }
}