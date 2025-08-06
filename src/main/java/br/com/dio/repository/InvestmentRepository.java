package br.com.dio.repository;

import br.com.dio.exception.AccountWithInvestmentException;
import br.com.dio.exception.PixInUseException;
import br.com.dio.exception.WalletNotFoundException;
import br.com.dio.model.AccountWallet;
import br.com.dio.model.Investment;
import br.com.dio.model.InvestmentWallet;

import java.util.ArrayList;
import java.util.List;

import static br.com.dio.repository.CommonsRepository.checkFundForTransaction;

public class InvestmentRepository {

    private long nextId;

    private final List<Investment> investments = new ArrayList<>();
    private  final List<InvestmentWallet> wallets = new ArrayList<>();

    public Investment create(final long tax, final long initialFunds){
        this.nextId ++;
        var investment = new Investment(this.nextId, tax, initialFunds);
        investments.add(investment);
        return investment;
    }

    public InvestmentWallet initInvestment(final AccountWallet account, final long id){
        var accountInUse = wallets.stream().map(InvestmentWallet::getAccount).toList();

        if (accountInUse.contains(account)) {
            throw new AccountWithInvestmentException("A conta '" + account + "' já possui investimento");
        }

        var investment = findById(id);
        checkFundForTransaction(account, investment.initialFunds());
        var wallet = new InvestmentWallet(investment, account, investment.initialFunds());
        wallets.add(wallet);
        return wallet;
    }

    public InvestmentWallet deposit(final String pix, final long funds){
        var wallet = findWalletByAccount(pix);
        wallet.addMoney(wallet.getAccount().reduceMoney(funds), wallet.getService(), "Investimento");
        return wallet;
    }

    public InvestmentWallet withdraw(final String pix, final long funds){
        var wallet = findWalletByAccount(pix);
        checkFundForTransaction(wallet, funds);
        wallet.getAccount().addMoney(wallet.reduceMoney(funds), wallet.getService(), "Saque de investimentos");
        if (wallet.getFunds() == 0){
            wallets.remove(wallet);
        }
        return wallet;
    }

    public void updateAmount(){
        wallets.forEach(w -> w.updateAmount(w.getInvestment().tax()));
    }

    public Investment findById(final long id){
        return investments.stream()
                .filter(a -> a.id() == id)
                .findFirst()
                .orElseThrow(
                        () -> new WalletNotFoundException("O investimento '" + id + "' não foi encontrado")
                );
    }

    public InvestmentWallet findWalletByAccount(final String pix){
        return wallets.stream()
                        .filter(w -> w.getAccount()
                        .getPix()
                        .contains(pix))
                        .findFirst()
                        .orElseThrow(
                                () -> new WalletNotFoundException("A carteira não foi encontrada")
                        );
    }

    public List<InvestmentWallet> listWallets(){
        return this.wallets;
    }

    public List<Investment> list(){
        return this.investments;
    }

}
