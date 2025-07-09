import java.util.ArrayList;

public class Customer {
    private String name;
    private String phoneNum;
    private String address;
    private ArrayList<String> orders;
    //private Boolean isDelivery;
    //private String date;

    public Customer(String name, String phoneNum, String address){
        this.name = name;
        this.phoneNum = phoneNum;
        this.address = address;
        this.orders = new ArrayList<>();
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public void addOrder(String order){
        orders.add(order);
    }

    public ArrayList<String> getOrders(){
        return orders;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(",").append(phoneNum).append(",").append(address);

        if(!orders.isEmpty()) {
            sb.append(",").append(String.join("|", orders));
        }

        return sb.toString();
    }

    //add arrayList for order to store order history

    /* customer search

    public customerSearch(ArrayList<String> list, String target){
        int start =0;
        int end = list.size()-1;
        while (start<= end){
            int mid = (start + end)//2;
            if(list[mid]==target){
                return ;
            } elif (list[mid] < target){
                start = mid +1;
            } else {
                right = mid -1;
            }
        }

    }*/


}

