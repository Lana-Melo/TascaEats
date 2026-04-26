package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.Entity;

@Entity
public class Card extends APayment {
    
    private String cardFlag;

    public Card() {}

    public Card(double amount, Order order, String cardFlag) {
        super(amount, order);
        this.cardFlag = cardFlag;
    }

    public String getCardFlag() {
        return this.cardFlag;
    }

    public void setCardFlag(String cardFlag) {
        this.cardFlag = cardFlag;
    }
}
