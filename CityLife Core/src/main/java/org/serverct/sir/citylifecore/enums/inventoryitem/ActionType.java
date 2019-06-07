package org.serverct.sir.citylifecore.enums.inventoryitem;

public enum ActionType {
    COMMAND("执行命令"),
    SUGGEST("建议命令"),
    MESSAGE("发送简单信息"),
    FORMATEDMSG("发送格式化信息"),
    CHATREQUEST("发起聊天栏数据请求");

    private final String type;
    ActionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
