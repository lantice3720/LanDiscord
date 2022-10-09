package kr.kro.lanthanide.landiscord.config;

import java.util.ArrayList;
import java.util.Objects;

public class ChannelConfig {
    String id;
    ArrayList<String> botUsage;
    ArrayList<String> webhookUsage;

    public String getId() {
        return id;
    }

    public ArrayList<String> getUsage(String which) {
        return switch (which) {
            case "bot" -> botUsage;
            case "webhook" -> webhookUsage;
            default -> null;
        };
    }

    public ArrayList<String> getBotUsage() {
        return botUsage;
    }

    public ArrayList<String> getWebhookUsage() {
        return webhookUsage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBotUsage(ArrayList<String> botUsage) {
        this.botUsage = botUsage;
    }

    public void setWebhookUsage(ArrayList<String> webhookUsage) {
        this.webhookUsage = webhookUsage;
    }
}
