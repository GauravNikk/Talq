package ar.codeslu.Quatro;

public abstract class QuatroUser {
    String name,callId;
    boolean isVoice;

    public QuatroUser(String name, String callId, boolean isVoice) {
        this.name = name;
        this.callId = callId;
        this.isVoice = isVoice;
    }

    public QuatroUser(String name, boolean isVoice) {
        this.name = name;
        this.isVoice = isVoice;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVoice() {
        return isVoice;
    }

    public void setVoice(boolean voice) {
        isVoice = voice;
    }
}
