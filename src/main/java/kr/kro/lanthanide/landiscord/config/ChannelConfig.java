package kr.kro.lanthanide.landiscord.config;

import java.util.ArrayList;

public class ChannelConfig {
    private String id;
    private ArrayList<String> botUsage;
    private ArrayList<String> webhookUsage;

    public String getId() {
        return id;
    }

    public ArrayList<String> getBotUsage() {
        return botUsage;
    }

    public ArrayList<String> getWebhookUsage() {
        return webhookUsage;
    }
}
