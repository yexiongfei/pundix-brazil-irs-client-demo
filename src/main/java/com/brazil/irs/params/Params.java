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
        private String clienteExchangeNome;	//Complete name of the customer	完整的用户姓名	必填
        private String clienteDataNascimento;	//Birthday date in the format YYY-MM-DD	YYYY-MM-DD格式的生日	选填
        private String clienteEmail;//	E-mail	邮箱	必填
        private String clienteExchangeTipoDocumento;//	Type of the document(CPF, CNPJ, NIF, PASSPORT)	证件类型(含CPF, CNPJ, NIF, PASSPORT)	必填
        private String ClienteExchangeCPFCNPJ;//	Number of the customer's document(only numbers and letters)	证件号(只支持数字字母)	必填
        private String clienteExchangeEndereco;//	Complete address in the following format(STREET, NUMBER, COMPLEMENT(Apt/suitr), DISTRICT | CITY | STATE | COUNTRY | ZIP CODE) NO accents or special characters	完整的地址(例："RUA OROS, 135, ROOM 5, CENTRO | SAO PAULO | SP | BR | 05442020")	必填
        private String obs;
    }

    /**
     * 充值
     */
    @Data
    public static class DepositosOutput {
        private String ID;// - unique ID used to prevent duplicity	订单ID，用来防止重复的唯一ID	必填
        private String clienteExchangeCPFCNPJ;	//Number of the customer's document	用户证件号	必填	无证件类型，直接传输证件号
        private String operacaoData;//	Date of the operation(YYYY-MM-DD)	操作时间	必填
        private String operacaoTaxaValor;//	Fee amount displayed in BRL with 2 decimals	巴西货币计价的手续费，两位小数	必填	我方充值手续费一律为0
        private String criptoativoSimbolo;//	Asset ticker (BTC / LTC / ETH )	货币类型	必填
        private String criptoativoQuantidade;//	Amount deposited, with 10 decimals	充值数量，十位小数	必填
    }

    /**
     * 提现
     */
    @Data
    public static class RetiradasOutput {
        private String ID;//	ID - unique ID used to prevent duplicity	订单ID，用来防止重复的唯一ID	必填
        private String clienteExchangeCPFCNPJ;//	Number of the customer's document	用户证件号	必填	无证件类型，直接传输证件号
        private String operacaoData;//	Date of the operation(YYYY-MM-DD)	操作时间	必填
        private String operacaoTaxaValor;//	Fee amount displayed in BRL with 2 decimals	巴西货币计价的手续费，两位小数	必填	我方提现手续费需转化为当时的BRL的发币金额，如没有该数值，需要建表储存
        private String criptoativoSimbolo;	//Asset ticker (BTC / LTC / ETH )	货币类型	必填
        private String criptoativoQuantidade;//	Amount withdrawn, with 10 decimals	提现数量，十位小数	必填
    }


    /**
     * 销售
     */
    @Data
    public static class SellOutput {

        private String ID;//	ID - unique ID used to prevent duplicity	订单ID，用来防止重复的唯一ID	必填
        private String compradorCPFCNPJ;//	Number of the buyer's document	顾客方，买方的证件号	必填	这儿API里没有证件类型，正在确认中
        private String vendedorCPFCNPJ;//	Number of the salesman's document	卖方，销售方的证件号	必填	这儿API里没有证件类型，正在确认中
        private String operacaoData;//	Date of the operation(YYYY-MM-DD)	操作时间	必填
        private String operacaoValor;//	Amount of the operation, displayed in BRL, excluding fees, with 2 decimals	操作金额，不含手续费，巴西元计价，2位小数	必填	输入的销售金额，扣减商户所得的手续费后，的实际所得额
        private String operacaoTaxaValor;//	Fee amount displayed in BRL with 2 decimals	巴西货币计价的手续费，两位小数	必填	= 商户收取的手续费 + 分销商收取的手续费
        private String criptoativoSimbolo;//	Asset ticker (BTC / LTC / ETH )	顾客购得的数字货币类型	必填
        private String criptoativoQuantidade;//	Asset ticker (BTC / LTC / ETH) of the permutant 2 (Merchant who receives the payment)	商户接受的货币类型	必填

    }

    @Data
    public static class PayOutput {
        private String ID;//	ID - unique ID used to prevent duplicity	订单ID，用来防止重复的唯一ID	必填
        private String permutante1CPFCNPJ;//	Number of the permutant 1(customer who is paying)	支付方用户证件号	必填	无需传证件类型
        private String permutante2CPFCNPJ;//	Number of the permutant 2(merchant who receives the payment)	收款方的证件号	必填	无需传证件类型
        private String operacaoData;//	Date of the operation(YYYY-MM-DD)	操作时间	必填
        private String operacaoTaxaValor;//	Fee amount displayed in BRL with 2 decimals	巴西货币计价的手续费，两位小数	必填	= 平台收取手续费 + 商户收取的服务费 + 分销商收取的佣金
        private String criptoativoPermutante1Simbolo;//	Asset ticker (BTC / LTC / ETH ) of the permutant 1 (customer who is paying)	顾客支付的货币类型	必填
        private String criptoativoPermutante2Simbolo;//	Asset ticker (BTC / LTC / ETH) of the permutant 2 (Merchant who receives the payment)	商户接受的货币类型	必填	一律为稳定币USDT或DAI
        private String criptoativoPermutante1Quantidade;//	Amount of the permuted asset 1 (asset used by the customer to pay), in crypto, with 10 decimals	顾客支付的货币数量，十位小数	必填
        private String criptoativoPermutante2Quantidade;//	Amount of the permuted asset 2(stablecoin received by the merchant), in crypto, with 10 decimals	商户接受到的数字货币数量，(稳定币)，十位小数	必填
    }

}
