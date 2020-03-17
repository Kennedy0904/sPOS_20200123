package com.example.dell.smartpos.CardModule.tradepaypw.app.quickclick;

public class MenuQuickClickProtection extends QuickClickProtection {
    private static MenuQuickClickProtection menuQuickClickProtection;

    private MenuQuickClickProtection(long timeoutMs) {
        super(timeoutMs);
        // TODO Auto-generated constructor stub
    }

    public static synchronized MenuQuickClickProtection getInstance() {
        if (menuQuickClickProtection == null) {
            menuQuickClickProtection = new MenuQuickClickProtection(500);
        }

        return menuQuickClickProtection;
    }

}
