package br.com.dio.model;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public abstract class Wallet {

    @Getter
    private final BankService serviceType;

    protected final List<Money> money;

    public Wallet(final BankService serviceType) {
        this.serviceType = serviceType;
        this.money = new ArrayList<>();
    }

    protected List<Money> generateMoney(final long amount, final String description){

    }

    public long getFunds(){
        return money.size();
    }


}
