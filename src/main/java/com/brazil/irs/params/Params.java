package com.brazil.irs.params;

import lombok.Data;

/**
 * @Author: 飞
 * @Date: 2021/3/29 16:48
 */
public class Params {


    @Data
    public static class AuthInput {
        private String aesSecret;
    }


    @Data
    public static class CommonInput {
        private String startDate;
        private String endDate;
    }


    @Data
    public static class ClientsOutput {
        private String clienteExchangeNome;	//Complete name of the customer
        private String clienteDataNascimento;	//Birthday date in the format YYY-MM-DD
        private String clienteEmail;//	E-mail
        private String clienteExchangeTipoDocumento;//	Type of the document(CPF, CNPJ, NIF, PASSPORT)
        private String ClienteExchangeCPFCNPJ;//	Number of the customer's document(only numbers and letters)
        private String clienteExchangeEndereco;//	Complete address in the following format(STREET, NUMBER, COMPLEMENT(Apt/suitr), DISTRICT | CITY | STATE | COUNTRY | ZIP CODE) NO accents or special characters
        private String obs;
    }

    /**
     * 充值
     */
    @Data
    public static class DepositosOutput {
        private String ID;// - unique ID used to prevent duplicity
        private String clienteExchangeCPFCNPJ;	//Number of the customer's document
        private String operacaoData;//	Date of the operation(YYYY-MM-DD)
        private String operacaoTaxaValor;//	Fee amount displayed in BRL with 2 decimals
        private String criptoativoSimbolo;//	Asset ticker (BTC / LTC / ETH )
        private String criptoativoQuantidade;//	Amount deposited, with 10 decimals
    }

    /**
     * 提现
     */
    @Data
    public static class RetiradasOutput {
        private String ID;//	ID - unique ID used to prevent duplicity
        private String clienteExchangeCPFCNPJ;//	Number of the customer's document
        private String operacaoData;//	Date of the operation(YYYY-MM-DD)
        private String operacaoTaxaValor;//	Fee amount displayed in BRL with 2 decimals
        private String criptoativoSimbolo;	//Asset ticker (BTC / LTC / ETH )
        private String criptoativoQuantidade;//	Amount withdrawn, with 10 decimals
    }


    /**
     * 销售
     */
    @Data
    public static class SellOutput {

        private String ID;//	ID - unique ID used to prevent duplicity
        private String compradorCPFCNPJ;//	Number of the buyer's document
        private String vendedorCPFCNPJ;//	Number of the salesman's document
        private String operacaoData;//	Date of the operation(YYYY-MM-DD)
        private String operacaoValor;//	Amount of the operation, displayed in BRL, excluding fees, with 2 decimals
        private String operacaoTaxaValor;//	Fee amount displayed in BRL with 2 decimals
        private String criptoativoSimbolo;//	Asset ticker (BTC / LTC / ETH )
        private String criptoativoQuantidade;//	Asset ticker (BTC / LTC / ETH) of the permutant 2 (Merchant who receives the payment)

    }

    @Data
    public static class PayOutput {
        private String ID;//	ID - unique ID used to prevent duplicity
        private String permutante1CPFCNPJ;//	Number of the permutant 1(customer who is paying)
        private String permutante2CPFCNPJ;//	Number of the permutant 2(merchant who receives the payment)
        private String operacaoData;//	Date of the operation(YYYY-MM-DD)
        private String operacaoTaxaValor;//	Fee amount displayed in BRL with 2 decimals
        private String criptoativoPermutante1Simbolo;//	Asset ticker (BTC / LTC / ETH ) of the permutant 1 (customer who is paying)
        private String criptoativoPermutante2Simbolo;//	Asset ticker (BTC / LTC / ETH) of the permutant 2 (Merchant who receives the payment)
        private String criptoativoPermutante1Quantidade;//	Amount of the permuted asset 1 (asset used by the customer to pay), in crypto, with 10 decimals
        private String criptoativoPermutante2Quantidade;//	Amount of the permuted asset 2(stablecoin received by the merchant), in crypto, with 10 decimals
    }

}
