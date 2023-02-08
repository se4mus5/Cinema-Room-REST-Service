package cinema;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Seat {
    private int row;
    private int column;
    private int price;
    @JsonIgnore
    private boolean available;
    @JsonIgnore
    private String token;

    private static final int TICKET_LOW_PRICE = 8;
    private static final int TICKET_HI_PRICE = 10;
    private static final int TICKET_PRICE_THRESHOLD_ROW = 4;

    public Seat() {}
    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
        this.price = row <= TICKET_PRICE_THRESHOLD_ROW ? TICKET_HI_PRICE : TICKET_LOW_PRICE;
        this.available = true;
        this.token = "";
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
