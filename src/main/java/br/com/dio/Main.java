package br.com.dio;

import br.com.dio.exception.AccountNotFoundException;
import br.com.dio.exception.NoFoundsEnoughException;
import br.com.dio.repository.AccountRepository;
import br.com.dio.repository.InvestmentRepository;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private final static AccountRepository accountRepository = new AccountRepository();
    private final static InvestmentRepository investmentRepository = new InvestmentRepository();

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args){
        System.out.println("Olá, seja bem vindo!");
        while(true){
            System.out.println("Selecione a opção  desejada");
            System.out.println("1 - criar uma conta");
            System.out.println("2 - criar um investimento");
            System.out.println("3 - fazer um investimento");
            System.out.println("4 - depositar na conta");
            System.out.println("5 - sacar da conta");
            System.out.println("6 - transferenia entre contas");
            System.out.println("7 - investir");
            System.out.println("8 - sacar investimento");
            System.out.println("9 - listar contas");
            System.out.println("10 - listar investimentos");
            System.out.println("11 - listar carteiras de investimento");
            System.out.println("12 - atualizar investimentos");
            System.out.println("13 - histórico de conta");
            System.out.println("14 - sair");
            var option = scanner.nextInt();
            switch (option){
                case 1: createAccount();
                case 2: createInvestment();
                case 3:
                case 4: deposit();
                case 5: withdraw();
                case 6:
                case 7:
                case 8:
                case 9: accountRepository.list().forEach(System.out::println);
                case 10: investmentRepository.list().forEach(System.out::println);
                case 11: investmentRepository.listWallets().forEach(System.out::println);
                case 12: {
                    investmentRepository.updateAmount();
                    System.out.println("Investimentos reajustados");
                }
                case 13:
                case 14: System.exit(0);
                default: System.out.println("Opção inválida");
            }
        }
    }

    private static void createAccount(){
        System.out.println("Informe as chaves pix (separadas por ';'");
        var pix = Arrays.stream(scanner.next().split(";")).toList();
        System.out.println("Informe o valor inicial do depósito");
        var amount = scanner.nextLong();
        var wallet = accountRepository.create(pix, amount);
        System.out.println("conta criada: " + wallet);
    }

    private static void createInvestment(){
        System.out.println("Informe a taxa do investimento");
        var pix = scanner.nextInt();
        System.out.println("Informe o valor inicial do depósito");
        var initialFunds = scanner.nextLong();
        var investment = investmentRepository.create(pix, initialFunds);
        System.out.println("investimento criado: " + investment);
    }

    private static void deposit(){
        System.out.println("Informe a chave pix da conta para depósito");
        var pix = scanner.next();
        System.out.println("Infome o valor que será depositado");
        var amount = scanner.nextLong();
        try {
            accountRepository.deposit(pix, amount);
        } catch (AccountNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void withdraw(){
        System.out.println("Informe a chave pix da conta para saque");
        var pix = scanner.next();
        System.out.println("Infome o valor que será sacado");
        var amount = scanner.nextLong();
        try {
            accountRepository.withdraw(pix, amount);
        } catch (NoFoundsEnoughException | AccountNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
