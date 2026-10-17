public class PickupDelivery implements DeliveryMethod {
    @Override
    public String getName() {
        return "Самовывоз";
    }

    @Override
    public double calculateCost(double orderSum) {
        return 0;
    }

    @Override
    public int estimateDays() {
        return 1;
    }

    @Override
    public boolean requiresCourier() {
        return false;
    }
}