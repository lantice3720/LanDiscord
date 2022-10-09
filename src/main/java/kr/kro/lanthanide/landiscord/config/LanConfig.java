package kr.kro.lanthanide.landiscord.config;

import java.util.List;

public class LanConfig {
    String token;
    String webhookName;
    List<ChannelConfig> channel;

    public String getToken() {
        return token;
    }

    public String getWebhookName() {
        return webhookName;
    }

    public List<ChannelConfig> getChannel() {
        return channel;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setWebhookName(String webhookName) {
        this.webhookName = webhookName;
    }

    public void setChannel(List<ChannelConfig> channel) {
        this.channel = channel;
    }
}
