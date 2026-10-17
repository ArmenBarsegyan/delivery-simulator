public interface DeliveryMethod {
    String getName();

    double calculateCost(double orderSum);

    int estimateDays();

    boolean requiresCourier();
}