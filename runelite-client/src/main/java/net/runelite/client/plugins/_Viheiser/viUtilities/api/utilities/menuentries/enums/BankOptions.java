package net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.menuentries.enums;

public enum BankOptions {
    WITHDRAW_1("Withdraw-1"),
    WITHDRAW_5("Withdraw-5"),
    WITHDRAW_10("Withdraw-10"),
    WITHDRAW_14("Withdraw-14"),
    WITHDRAW_X("Withdraw-X"),
    WITHDRAW_ALL("Withdraw-All"),
    DEPOSIT_ALL("Deposit-All");

    private final String text;
    BankOptions(String optionText) {
        text = optionText;
    }

    public String getText() {
        return text;
    }
}
