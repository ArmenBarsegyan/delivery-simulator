public enum OrderStatus {
    CREATED("Создан"),
    CONFIRMED("Подтверждён"),
    IN_DELIVERY("В доставке"),
    DELIVERED("Доставлен");

    private final String title;

    OrderStatus(String title) {
        this.title = title;
    }

    public boolean canMoveTo(OrderStatus next) {
        if (this == CREATED) {
            return next == CONFIRMED;
        }
        if (this == CONFIRMED) {
            return next == IN_DELIVERY;
        }
        if (this == IN_DELIVERY) {
            return next == DELIVERED;
        }
        return false;
    }

    @Override
    public String toString() {
        return title;
    }
}